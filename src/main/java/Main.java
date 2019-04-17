import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Main {

    /**
     * @param args
     */
    public static void main(String[] args) {
        try {
            JSONObject input = getInputJson(getUserParameter("--in", args));
            Map<String, TestFile> testFiles = parseInput(input);
            List<Pod> pods = distributeToPods(testFiles, Integer.parseInt(getUserParameter("--pods", args)));
            System.out.println(generateOutputJson(pods));

        } catch (Exception e) {
            System.err.println("Execution Failed: " + e.getMessage());
            System.exit(1);
        }
    }


    private static String getUserParameter(String parameterCommand, String[] args) throws NoSuchFieldException {
        for (int i = 0; i < args.length - 1; i++) {
            if (args[i].equals(parameterCommand)) {
                return args[i + 1];
            }
        }
        throw new NoSuchFieldException("Parameter not found for " + parameterCommand);
    }

    private static FileReader instantiateFileReader(String filename) {
        try {
            return new FileReader(filename);
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException(filename + " does not match the path of any known file. " +
                    "Please check your input parameters and try again.");
        }
    }

    private static JSONObject getInputJson(String filename) throws IOException {
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
            TestCase c = new TestCase(
                    (Double) j.get("run_time"),
                    (String) j.get("full_description")
            );

            // Add it to the corresponding TestFile by looking it up in the HashMap, and creating it
            // if it is not already present
            String filepath = (String) j.get("file_path");
            if (fileMap.containsKey(filepath)) {
                fileMap.get(filepath).addTestCase(c);
            } else {
                fileMap.put(filepath, new TestFile(filepath));
                fileMap.get(filepath).addTestCase(c);
            }
        }


        for (TestFile f : fileMap.values()) {
            System.out.println(f);
        }

        return fileMap;
    }

    private static List<Pod> distributeToPods(Map<String, TestFile> testFiles, int numPods){
        List<Pod> pods = new LinkedList<Pod>();

        for(int i=0; i<numPods-1; i++){
            pods.add(new Pod("Rspec-Test-Pod-"+i));
        }

        List<TestFile> files = new ArrayList<>(testFiles.values());
        Collections.sort(files);
        for(TestFile file:files){
            if(!file.isSupportFile()){
                getLightestPod(pods).addFile(file);
            }
        }

        return pods;
    }

    private static Pod getLightestPod(List<Pod> pods){
        Pod lightest = pods.get(0);
        for(Pod p:pods){
            if(p.getEstimatedRunTime()<lightest.getEstimatedRunTime()){
                lightest = p;
            }
        }

        return lightest;
    }

    private static JSONObject generateOutputJson(List<Pod> pods){
        JSONObject out = new JSONObject();

        out.put("version", "0.1.0");
        out.put("number_of_pods", pods.size()+1);
        out.put("date_generated", new Date().toString());

        JSONArray podsArray = new JSONArray();

        for(Pod p: pods){
            podsArray.add(p.toJson());
        }

        podsArray.add(getCatchAllPodJson(pods));
        out.put("pods", podsArray);

        return out;
    }

    private static JSONObject getCatchAllPodJson(List<Pod> pods){
        return new CatchAllPod(pods).toJson();
    }
}


