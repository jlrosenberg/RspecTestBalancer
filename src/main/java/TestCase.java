/**
 * Represents a single rspec test case.
 */
public class TestCase {

    private double estimatedRunTime;
    private String description;

    /**
     * Constructs a new TestCase object given the estimated runTime of the TestCase and the string descriptor
     * that identifies it in the rspec file.
     * @param estimatedRunTime a double that estimates the runtime of the TestCase
     * @param description the String description that identifies the TestCase in its corresponding {@link TestFile}
     */
    public TestCase(double estimatedRunTime, String description){
        this.estimatedRunTime = estimatedRunTime;
        this.description = description;
    }

    /**
     * Returns an estimated runtime of the test case in seconds.
     * @return a double representing the estimated run time of this test case in unit seconds
     */
    public double getEstimatedRunTime(){
        return estimatedRunTime;
    }
}

