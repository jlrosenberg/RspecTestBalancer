package model.rspec;

import model.TestCase;
import model.TestFile;
import model.TestType;
import model.rspec.RspecTestCase;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an rspec test file. Each model.TestFile contains 1 or more {@link RspecTestCase}s.
 */
public class RSpecTestFile implements TestFile {

    private String filePath;
    private List<TestCase> tests;
    private double estimatedRunTime;

    /**
     * Constructs a new model.TestFile located at a filePath.
     * @param filePath the relative location of the test file in the file system
     */
    public RSpecTestFile(String filePath){
        this.filePath=filePath;
        tests=new ArrayList<TestCase>();
        estimatedRunTime=0;
    }

    /**
     * Adds a {@link TestCase} to the file.
     * @param testCase the model.TestCase to be added
     */
    @Override
    public void addTestCase(TestCase testCase){
        tests.add(testCase);
        estimatedRunTime += testCase.getEstimatedRunTime();
    }

    /**
     * Calculates the estimated runTime of all of the testCases in the file.
     * @return a double estimate of the runTime of this model.TestFile
     */
    @Override
    public double getEstimatedRunTime(){
        return estimatedRunTime;
    }

    /**
     * Gets the number of testCases in this model.TestFile.
     * @return the integer number of testCases in this file
     */
    @Override
    public int getNumberOfTestCases(){
        return tests.size();
    }

    /**
     * Retrieves the relative Filepath of this model.TestFile.
     * @return the String filepath of this file
     */
    @Override
    public String getFilePath(){
        return filePath.substring(2);
    }

    @Override
    public int compareTo(TestFile o) {
        if(this.getEstimatedRunTime() > o.getEstimatedRunTime()){
            return -1;
        }else{
            return 1;
        }
    }

    /**
     * Checks if the testFile is a support file, and cannot be invoked directly
     * @return a boolean representing whether or not this TestFile is a support file
     */
    public boolean isSupportFile(){
        return (filePath.contains("spec/support"));
    }

    @Override
    public String toString(){
        return this.getFilePath() + ",   "+estimatedRunTime+", "+ tests.size()+", ";
    }

    @Override
    public JSONObject toJson(){
        JSONObject out=new JSONObject();
        out.put("filepath", this.getFilePath());
        out.put("estimated_run_time", this.getEstimatedRunTime());
        out.put("test_cases", this.getNumberOfTestCases());

        return out;
    }

    public TestType getType(){
        return TestType.RSPEC;
    }


}

