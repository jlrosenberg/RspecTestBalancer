package model.rspec;

import model.TestCase;

/**
 * Represents a single rspec test case.
 */
public class RspecTestCase implements TestCase {

    private double estimatedRunTime;
    private String description;

    /**
     * Constructs a new model.TestCase object given the estimated runTime of the model.TestCase and the string descriptor
     * that identifies it in the rspec file.
     * @param estimatedRunTime a double that estimates the runtime of the model.TestCase
     * @param description the String description that identifies the model.TestCase in its corresponding {@link RSpecTestFile}
     * @throws IllegalArgumentException if the provided runtime of the TestCase is negative
     */
    public RspecTestCase(double estimatedRunTime, String description){
        if(estimatedRunTime<0){
            throw new IllegalArgumentException("The estimated runtime of a test case cannot be negative.");
        }
        this.estimatedRunTime = estimatedRunTime;
        this.description = description;
    }

    /**
     * Returns an estimated runtime of the test case in seconds.
     * @return a double representing the estimated run time of this test case in unit seconds
     */
    @Override
    public double getEstimatedRunTime(){
        return estimatedRunTime;
    }

    /**
     * Returns a description that specifies the purpose of this test case
     * @return the String description of this testCase.
     */
    @Override
    public String getDescription(){
        return this.description;
    }
}

