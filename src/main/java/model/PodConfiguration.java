package model;

import model.rspec.RspecPod;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.*;

/**
 * Represents a set of kubernetes pods used to run tests in parallel in a CI build.
 */
public class PodConfiguration {

  private int numPods;
  private int numCatchAll;
  private List<Pod> pods;

  private PodConfiguration(int numPods, int numCatchAll, List<TestFile> files){
    this.numPods = numPods;
    this.numCatchAll = numCatchAll;
    pods = distributeTestFilesIntoPods(files);
  }

  /**
   * Distributes the test files equally among the pods so that they run in an equivalent
   * amount of time (or as close to that as possible, anyways).
   *
   * @param files The list of TestFiles to distribute among the pods
   * @return A list of pods that the TestFiles have been distributed into. Does not include
   * catch all pods
   */
  private List<Pod> distributeTestFilesIntoPods(List<TestFile> files){
    if(numPods<1){
      throw new IllegalArgumentException("Number of pods must be greater than 0");
    }

    List<Pod> pods = new LinkedList<Pod>();

    for (int i = 0; i < numPods - 1; i++) {
      pods.add(new RspecPod("Test-Pod-" + i));
    }

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

  public JSONObject toJson(){
    JSONObject out = new JSONObject();

    out.put("version", "0.2.0");
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

  public String toString(){
    return null;
  }

  public static class Builder{
    private int numPods;
    private int numCatchAll;
    private List<TestFile> files;

    public Builder(){
      numPods=0;
      numCatchAll=1;
      files=new ArrayList<TestFile>();
    }

    public void addTestFiles(List<TestFile> files){
      this.files.addAll(files);
    }

    public void setNumPods(int numPods){
      this.numPods=numPods;
    }

    /**
     * *****NOT YET SUPPORTED - CURRENTLY DEFAULTS TO 1 *****
     * @param numCatchAll
     */
    public void setNumCatchAll(int numCatchAll){
      this.numCatchAll=numCatchAll;
    }

    public PodConfiguration build(){
      return new PodConfiguration(numPods, numCatchAll, files);
    }
  }
}
