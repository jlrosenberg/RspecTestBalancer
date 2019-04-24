package model;

import org.json.simple.JSONObject;

import java.util.List;

public interface Pod {

  /**
   * Get the name of this pod. This name will be used as the job name in Jenkins, and will also be used as the name
   * of the test result output files.
   * @return the String name of this Pod
   */
  String getName();

  /**
   * Adds a {@link TestFile} to this pod. TestFiles must be of the correct framework for each pod or an exception will
   * be thrown.
   * @param testFile the {@link TestFile} to add to this Pod
   * @throws IllegalArgumentException if the TestFile to be added is of a different framework than the pod is configured
   * to use.
   */
  void addFile(TestFile testFile) throws IllegalArgumentException;

  /**
   * Returns a list of all of the TestFiles that are currently in this pod. Will return an empty list if no tests have
   * been added to the pod so far.
   * @return a {@link List<TestFile>} containing all of the Files that are in this pod.
   */
  List<TestFile> getFiles();

  /**
   * Calculates the estimated run-time it will take to execute all of the testCases in all of the TestFiles contained
   * within this pod. This does not include the time it takes to spin up a pod, or to save/output the results of the
   * tests.
   * @return a double estimate of the runtime this pods should take.
   */
  double getEstimatedRunTime();

  String getPatternString();

  JSONObject toJson();

  /**
   * Gets the Type of test that this pod is configured to execute.
   * @return the {@link TestType} that this pod is configured to execute
   */
  TestType getTestType();
}
