package model;

public interface TestCase {
  /**
   * Returns an estimated runtime of the test case in seconds.
   * @return a double representing the estimated run time of this test case in unit seconds
   */
  double getEstimatedRunTime();

  /**
   * Returns a description that specifies the purpose of this test case
   * @return the String description of this testCase.
   */
  String getDescription();
}
