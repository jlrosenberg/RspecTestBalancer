package model;

import model.rspec.RspecTestCase;
import org.json.simple.JSONObject;

/**
 * Represents a language/framework agnostic TestFile. All TestFiles contain 1 or more
 * TestCases, and have an estimated run time and filepath.
 */
public interface TestFile extends Comparable<TestFile> {

  /**
   * Adds a {@link RspecTestCase} to the file.
   * @param testCase the TestCase to be added
   */
  void addTestCase(TestCase testCase);

  /**
   * Calculates the estimated runTime of all of the testCases in the file.
   * @return a double estimate of the runTime of this model.TestFile
   */
  double getEstimatedRunTime();

  /**
   * Gets the number of testCases in this TestFile.
   * @return the integer number of testCases in this file
   */
  int getNumberOfTestCases();

  /**
   * Retrieves the relative Filepath of this TestFile.
   * @return the String filepath of this file
   */
  String getFilePath();

  int compareTo(TestFile o);

  /**
   * Checks if the testFile is a support file, and cannot be invoked directly
   * @return a boolean representing whether or not this TestFile is a support file
   */
  boolean isSupportFile();

  String toString();

  JSONObject toJson();

  TestType getType();
}
