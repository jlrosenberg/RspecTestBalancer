import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;


import model.*;
import model.rspec.RSpecTestFile;
import model.rspec.RspecPod;
import model.rspec.RspecTestCase;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 */
public class Main {

  private static Map<String, List<String>> parameters;

  /**
   * The order that the parameters are inputted should not matter, assuming that the input is well formed and
   * that the parameters provided match with their corresponding input flags.
   * <p>
   * In the future, --out will be optional, and it will print to the console if --out is not provided.
   * <p>
   * Currently expects input in this form:
   * --in inputfile.json --pods 4 --out outputfile.json
   * <p>
   * Future form:
   * --in inputfile1.json inputfile2.json inputfile3.json --pods 4 --catchpods 2 --out outputfile.json
   *
   * @param args
   */
  public static void main(String[] args) throws Exception {
    JSONObject input = getInputJson(getUserParameter("--in", args));
    Map<String, TestFile> testFiles = parseInput(input);
    List<Pod> pods = distributeToPods(testFiles, Integer.parseInt(getUserParameter("--pods", args)));
    System.out.println(generateOutputJson(pods));
  }


  /**
   * SOON TO BE DEPRECATED
   *
   * @param parameterCommand
   * @param args
   * @return
   * @throws NoSuchFieldException
   */
  private static String getUserParameter(String parameterCommand, String[] args) throws NoSuchFieldException {
    for (int i = 0; i < args.length - 1; i++) {
      if (args[i].equals(parameterCommand)) {
        return args[i + 1];
      }
    }
    throw new NoSuchFieldException("Parameter not found for " + parameterCommand);
  }

  /**
   * Fetches a list of the string inputs that the user provided after the given input flag. Will throw an exception
   * if that flag was not provided at all in the input.
   * @param parameterCommand
   * @return
   * @throws NoSuchFieldException
   */
  private static List<String> getUserParameters(String parameterCommand) throws NoSuchFieldException {
    if (parameters.containsKey(parameterCommand)) {
      return parameters.get(parameterCommand);
    }

    throw new NoSuchFieldException("Parameter not found for " + parameterCommand);
  }

  /**
   * Attempts to instantiate the filereader based on the filename the user provided for input. Throws an exception
   * if it cannot find that file.
   * @param filename The filename of the file to read
   * @return The FileReader associated the file, if the file was found
   * @throws IllegalArgumentException if no file with the passed filename exists in this directory
   */
  private static FileReader instantiateFileReader(String filename) throws FileNotFoundException {
    try {
      return new FileReader(filename);
    } catch (FileNotFoundException e) {
      throw new FileNotFoundException(filename + " does not match the path of any known file. " +
          "Please check your input parameters and try again.");
    }
  }

  /**
   * Given the filename of the input json file, constructs an {@link JSONObject} based on the contents of the file,
   * if it exists.
   * @param filename The filename of the json file to read in
   * @return An {@link JSONObject} with the contents of the json input file
   * @throws IOException If the json file is not well formed
   * @throws FileNotFoundException if the json input file cannot be found at the path provided.
   */
  private static JSONObject getInputJson(String filename) throws IOException, FileNotFoundException{
    try {
      return (JSONObject) new JSONParser().parse(instantiateFileReader(filename));
    } catch (ParseException e) {
      throw new IOException("Could not parse input file. Make sure it is well-formed");
    }
  }

  private static Map<String, TestFile> parseInput(JSONObject input) {
    Map<String, TestFile> fileMap = new HashMap<String, TestFile>();
    List<JSONObject> examples = ((List<JSONObject>) input.get("examples"));

    for (JSONObject j : examples) {
      // Create the test case object
      TestCase c = new RspecTestCase(
          (Double) j.get("run_time"),
          (String) j.get("full_description")
      );

      // Add it to the corresponding model.TestFile by looking it up in the HashMap, and creating it
      // if it is not already present
      String filepath = (String) j.get("file_path");
      if (fileMap.containsKey(filepath)) {
        fileMap.get(filepath).addTestCase(c);
      } else {
        fileMap.put(filepath, new RSpecTestFile(filepath));
        fileMap.get(filepath).addTestCase(c);
      }
    }


    for (TestFile f : fileMap.values()) {
      System.out.println(f);
    }

    return fileMap;
  }

  /**
   * Given the constructed TestFiles, and the number of pods that should be used, distributes the testcases evenly
   * among all but 1 of the pods so that they execute in the same amount of time, and leaves the last pod as a
   * catch-all pod to catch any new tests that might have been added (or files that were renamed) since the test
   * configuration was generated.
   *
   * @param testFiles The map of populated testfiles generated from the profiled test data provided by the user.
   * @param numPods   The number of pods to distribute the tests between. The tests will be distributed between n-1 pods,
   *                  with the Nth pod being used as a 'catch-all' that catches any new tests that might have been added
   *                  since the last configuration change.
   * @return
   */
  private static List<Pod> distributeToPods(Map<String, TestFile> testFiles, int numPods) {
    if(numPods<1){
      throw new IllegalArgumentException("Number of pods must be greater than 0");
    }

    List<Pod> pods = new LinkedList<Pod>();

    for (int i = 0; i < numPods - 1; i++) {
      pods.add(new RspecPod("Rspec-Test-model.Pod-" + i));
    }

    List<TestFile> files = new ArrayList<>(testFiles.values());
    Collections.sort(files);
    for (TestFile file : files) {
        getLightestPod(pods).addFile(file);

    }

    return pods;
  }

  private static Pod getLightestPod(List<Pod> pods) {
    Pod lightest = pods.get(0);
    for (Pod p : pods) {
      if (p.getEstimatedRunTime() < lightest.getEstimatedRunTime()) {
        lightest = p;
      }
    }

    return lightest;
  }

  private static JSONObject generateOutputJson(List<Pod> pods) {
    JSONObject out = new JSONObject();

    out.put("version", "0.1.0");
    out.put("number_of_pods", pods.size() + 1);
    out.put("date_generated", new Date().toString());

    JSONArray podsArray = new JSONArray();

    for (Pod p : pods) {
      podsArray.add(p.toJson());
    }

    podsArray.add(getCatchAllPodJson(pods));
    out.put("pods", podsArray);

    return out;
  }

  private static JSONObject getCatchAllPodJson(List<Pod> pods) {
    return new CatchAllPod(pods).toJson();
  }
}


