For a developer to add a new command to the REPL, he or she should decide what
the input to the REPL will be and then create a new helper method in our Main
class which executes the desired functionality, optionally passing in a string
array of arguments. Then the developer should add a new line to our createHashMap
method with the key being a String with the name of the command and the value
either the name of the helper method in the case where we need extra arguments,
or (String[] args) -> method call when we don't.

private HashMap<String, Command> createHashMap() {
    HashMap<String, Command> commands = new HashMap<>();
    commands.put("get_users", (String[] args) -> client.usersApiCall(false));
    commands.put("get_rents", (String[] args) -> client.rentsApiCall());
    commands.put("get_reviews", (String[] args) -> client.reviewsApiCall());
    commands.put("users", this::usersHelper);
    commands.put("similar", this::similarHelper);
    commands.put("classify", this::classifyReplHelper);
    commands.put("recsys_load", this::loadHelper);
    commands.put("recsys_rec", this::recHelper);
    commands.put("recsys_gen_groups", this::makeTeamHelper);
    return commands;
  }