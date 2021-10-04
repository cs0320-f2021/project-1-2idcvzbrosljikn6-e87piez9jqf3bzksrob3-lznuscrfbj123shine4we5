package edu.brown.cs.student.main;

import com.google.gson.Gson;
import edu.brown.cs.student.apiClient.ApiClient;
import edu.brown.cs.student.runway.Runway;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;

/**
 * A generic file parser.
 */
public class FileParser {

  private BufferedReader bufRead = null;

  /**
   * A FP constructor.
   *
   * @param file - a String file path
   */
  public FileParser(String file) {

    try {
      this.bufRead = new BufferedReader(new FileReader(file));
    } catch (FileNotFoundException f) {
      System.out.println("ERROR: File not found");
    }
  }

  /**
   * Reads a new line in the file and returns a String.
   *
   * @return a read String line
   */
  public String readNewLine() {
    if (bufRead != null) {
      try {
        String ln = bufRead.readLine();
        return ln;
      } catch (IOException e) {
        System.out.println("ERROR: Read");
        return null;
      }
    } else {
      return null;
    }
  }

  public static String readIntoString(String arg) {
    try {
      String json = Files.readString(Path.of(arg)).strip();
      return json;
    } catch (IOException e) {
      System.out.println("ERROR: Unable to read from file " + arg);
    } catch (InvalidPathException e) {
      System.out.println("ERROR: Invalid path " + arg);
    }
    return "ERROR";
  }
}
