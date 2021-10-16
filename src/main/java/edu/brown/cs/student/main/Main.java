package edu.brown.cs.student.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.collect.ImmutableMap;

import com.google.gson.Gson;
import edu.brown.cs.student.apiClient.ApiClient;
import edu.brown.cs.student.orm.Database;
import edu.brown.cs.student.recommender.Interests;
import edu.brown.cs.student.recommender.RecommenderImpl;
import edu.brown.cs.student.recommender.RecommenderResponse;
import edu.brown.cs.student.recommender.Skills;
import edu.brown.cs.student.runway.Runway;
import freemarker.template.Configuration;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import spark.ExceptionHandler;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Spark;
import spark.TemplateViewRoute;
import spark.template.freemarker.FreeMarkerEngine;

/**
 * The Main class of our project. This is where execution begins.
 */
public final class Main {

  // use port 4567 by default when running server
  private static final int DEFAULT_PORT = 4567;
  private KDTree kdTree = null;
  private RecommenderImpl recommender = null;

  /**
   * The initial method called when execution begins.
   *
   * @param args An array of command line arguments
   */
  public static void main(String[] args) {
    new Main(args).run();
  }

  private String[] args;
  private ApiClient client;

  private Main(String[] args) {
    this.args = args;
    this.client = new ApiClient();
  }

  private void run() {
    // set up parsing of command line flags
    OptionParser parser = new OptionParser();

    // "./run --gui" will start a web server
    parser.accepts("gui");

    // use "--port <n>" to specify what port on which the server runs
    parser.accepts("port").withRequiredArg().ofType(Integer.class)
        .defaultsTo(DEFAULT_PORT);

    OptionSet options = parser.parse(args);
    if (options.has("gui")) {
      runSparkServer((int) options.valueOf("port"));
    }

    HashMap<String, Command> commands = this.createHashMap();

    try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
      String input;
      while ((input = br.readLine()) != null) {
        try {
          input = input.trim();
          List<String> matchList = new ArrayList<>();
          Pattern regex = Pattern.compile("[^\\s\"']+|\"([^\"]*)\"|'([^']*)'");
          Matcher regexMatcher = regex.matcher(input);
          while (regexMatcher.find()) {
            if (regexMatcher.group(1) != null) {
              // Add double-quoted string without the quotes
              matchList.add(regexMatcher.group(1));
            } else if (regexMatcher.group(2) != null) {
              // Add single-quoted string without the quotes
              matchList.add(regexMatcher.group(2));
            } else {
              // Add unquoted word
              matchList.add(regexMatcher.group());
            }
          }
          String[] arguments = matchList.toArray(new String[0]);
          if (arguments.length == 0) {
            continue;
          }
          if (commands.containsKey(arguments[0])) {
            commands.get(arguments[0]).run(arguments);
          } else {
            System.out.println("ERROR: invalid command.");
            System.out.println("Valid commands: get_users, get_rents, "
                + "get_reviews, users, similar, classify");
          }
        } catch (Exception e) {
          System.out.println("ERROR: We couldn't process your input");
          e.printStackTrace();
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println("ERROR: Invalid input for REPL");
    }
  }

  /**
   * Helper method that returns a HashMap of the command keywords with their relevant method to
   * run (the relevant Command).
   * @return a HashMap of command strings to Commands
   */
  private HashMap<String, Command> createHashMap() {
    HashMap<String, Command> commands = new HashMap<>();
    commands.put("get_users", (String[] args) -> client.usersApiCall(false));
    commands.put("get_rents", (String[] args) -> client.rentsApiCall());
    commands.put("get_reviews", (String[] args) -> client.reviewsApiCall());
    commands.put("users", this::usersHelper);
    commands.put("similar", this::similarHelper);
    commands.put("classify", this::classifyReplHelper);
    commands.put("recsys_load", this::loadHelper);
    return commands;
  }

  private void loadHelper(String[] arguments) {
    /*
    if second argument is "responses" continue else print "inavalid argument"

    aData = data from Api
    bData =  data from Database

    Create a Recommender object with the aData and bData as inputs
    Types for these?
     */

    if (arguments.length != 2 || !arguments[1].equals("responses")) {
      System.out.println("ERROR: Invalid argument.");
      System.out.println("Valid usage: recsys_load responses");
      return;
    }

//    RecommenderResponse[] responses = client.recommenderUsers();
      RecommenderResponse[] responses = client.localRecommenderUsers();
    try {
      Database db = new Database("data/integration.sqlite3");
      for (RecommenderResponse response: responses) {
        response.fetchDatabaseData(db);
      }

      recommender = new RecommenderImpl(responses);
      System.out.println("Loaded Recommender with " + responses.length + " students");

    } catch (ClassNotFoundException e) {
      System.out.println("ERROR: Database not found");
    } catch (SQLException e) {
      System.out.println("ERROR: Illegal SQL input");
    }
  }

  /**
   * Helper method called when the user runs the classify command.
   * @param arguments the array of command line arguments
   */
  private void classifyReplHelper(String[] arguments) {
    if (kdTree == null) {
      System.out.println("ERROR: No KD Tree found. "
          + "Load a KD Tree using the \"users\" command first.");
      return;
    }
    // if userID input
    if (arguments.length == 3) {
      boolean userExists = false;
      for (Runway user : DataStore.getRunways()) {
        try {
          if (user.getUserId() == Integer.parseInt(arguments[2])) {
            Runway[] classify = kdTree.knn(Integer.parseInt(arguments[1]),
                    user.getWeight(), user.getHeight(), user.getAge());
            classifyHelper(classify);
            userExists = true;
            break; // if we've found the user, break out of the loop
          }
        } catch (NumberFormatException e) {
          System.out.println("ERROR: Argument not integer.");
        }
      }
      if (!userExists) {
        System.out.println("ERROR: No user with that ID could be found. "
            + "Please enter valid ID.");
      }
    } else if (arguments.length == 5) {
      try {
        Runway[] classify = kdTree.knn(Integer.parseInt(arguments[1]),
                Integer.parseInt(arguments[2]),
                Integer.parseInt(arguments[3]),
                Integer.parseInt(arguments[4]));
        classifyHelper(classify);
      } catch (NumberFormatException e) {
        System.out.println("ERROR: Argument not integer.");
      }
    } else {
      System.out.println("ERROR: Invalid number of arguments for classify.");
    }
  }

  /**
   * Helper method called when the similar command is run.
   * @param arguments the array of command line arguments
   */
  private void similarHelper(String[] arguments) {
    if (kdTree == null) {
      System.out.println("ERROR: No KD Tree found. "
          + "Load a KD Tree using the \"users\" command first.");
      return;
    }
    // if userID input
    if (arguments.length == 3) {
      boolean userExists = false;
      for (Runway user : DataStore.getRunways()) {
        try {
          if (user.getUserId() == Integer.parseInt(arguments[2])) {
            Runway[] similar = kdTree.knn(Integer.parseInt(arguments[1]),
                    user.getWeight(), user.getHeight(), user.getAge());
            for (Runway neighbor : similar) {
              System.out.println(neighbor.getUserId());
            }
            userExists = true;
            break;
          }
        } catch (NumberFormatException e) {
          System.out.println("Incorrect input format.");
        }
      }
      if (!userExists) {
        System.out.println("ERROR: No user with that ID could be found. "
            + "Please enter valid ID.");
      }
    } else if (arguments.length == 5) { //if coordinate input
      try {
        Runway[] similar = kdTree.knn(Integer.parseInt(arguments[1]),
                Integer.parseInt(arguments[2]),
                Integer.parseInt(arguments[3]),
                Integer.parseInt(arguments[4]));
        for (Runway neighbor : similar) {
          System.out.println(neighbor.getUserId());
        }
      } catch (NumberFormatException e) {
        System.out.println("ERROR: Argument not integer.");
      }
    } else {
      System.out.println("ERROR: Invalid number of arguments for similar.");
    }
  }

  /**
   * Helper method called in the classifyReplHelper method to print the nearest neighbours zodiac
   * table.
   * @param classify the array of users (Runway) to print the zodiacs of
   */
  private void classifyHelper(Runway[] classify) {
    Hashtable<String, Integer> zodiac = new Hashtable<>();
    String[] signs = new String[] {"Aries", "Taurus", "Gemini",
        "Cancer", "Leo", "Virgo",
        "Libra", "Scorpio", "Sagittarius",
        "Capricorn", "Aquarius", "Pisces"};
    for (Runway user : classify) {
      zodiac.put(user.getHoroscope(), zodiac.getOrDefault(user.getHoroscope(), 0) + 1);
    }
    for (String sign : signs) {
      System.out.println(sign + ": " + zodiac.getOrDefault(sign, 0));
    }
  }

  /**
   * Helper method called when the user runs the users command.
   * Loads the users data from an inputted file into a KDTree.
   * @param arguments the array of command line arguments
   */
  private void usersHelper(String[] arguments) {
    if (arguments.length != 2) {
      System.out.println("ERROR: Invalid number of arguments for users");
      System.out.println("Usage: users <filepath> or users online");
      return;
    }

    boolean usersLoaded = false; // tracks whether "users online" has been invoked or "users
    // <filename>"
    if (arguments[1].strip().equals("online")) {
      client.usersApiCall(true);
      usersLoaded = true;
    }

    try {
      Runway[] data;
      if (!usersLoaded) { // if data hasn't already been loaded through the API
        String json = ApiClient.normaliseJson(Files.readString(Path.of(arguments[1])).strip());
        data = new Gson().fromJson(json, Runway[].class);
        DataStore.setRunways(data);
      } else {
        data = DataStore.getRunways();
      }

      String[] dimensions = new String[] {"weight", "height", "age"};
      kdTree = new KDTree(data, dimensions);
      System.out.println("Loaded " + data.length + " users from " + arguments[1]);
    } catch (IOException e) {
      System.out.println("ERROR: Unable to read from file " + arguments[1]);
    } catch (InvalidPathException e) {
      System.out.println("ERROR: Invalid path " + arguments[1]);
    }
  }

  private static FreeMarkerEngine createEngine() {
    Configuration config = new Configuration(Configuration.VERSION_2_3_0);

    // this is the directory where FreeMarker templates are placed
    File templates = new File("src/main/resources/spark/template/freemarker");
    try {
      config.setDirectoryForTemplateLoading(templates);
    } catch (IOException ioe) {
      System.out.printf("ERROR: Unable use %s for template loading.%n",
          templates);
      System.exit(1);
    }
    return new FreeMarkerEngine(config);
  }

  private void runSparkServer(int port) {
    // set port to run the server on
    Spark.port(port);

    // specify location of static resources (HTML, CSS, JS, images, etc.)
    Spark.externalStaticFileLocation("src/main/resources/static");

    // when there's a server error, use ExceptionPrinter to display error on GUI
    Spark.exception(Exception.class, new ExceptionPrinter());

    // initialize FreeMarker template engine (converts .ftl templates to HTML)
    FreeMarkerEngine freeMarker = createEngine();

    // setup Spark Routes
    Spark.get("/", new MainHandler(), freeMarker);
  }

  /**
   * Display an error page when an exception occurs in the server.
   */
  private static class ExceptionPrinter implements ExceptionHandler<Exception> {
    @Override
    public void handle(Exception e, Request req, Response res) {
      // status 500 generally means there was an internal server error
      res.status(500);

      // write stack trace to GUI
      StringWriter stacktrace = new StringWriter();
      try (PrintWriter pw = new PrintWriter(stacktrace)) {
        pw.println("<pre>");
        e.printStackTrace(pw);
        pw.println("</pre>");
      }
      res.body(stacktrace.toString());
    }
  }

  /**
   * A handler to serve the site's main page.
   *
   * @return ModelAndView to render.
   * (main.ftl).
   */
  private static class MainHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      // this is a map of variables that are used in the FreeMarker template
      Map<String, Object> variables = ImmutableMap.of("title",
          "Go go GUI");

      return new ModelAndView(variables, "main.ftl");
    }
  }
}
