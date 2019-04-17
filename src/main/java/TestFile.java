import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an rspec test file. Each TestFile contains 1 or more {@link TestCase}s.
 */
public class TestFile implements Comparable<TestFile> {

    private String filePath;
    private List<TestCase> tests;
    private double estimatedRunTime;

    /**
     * Constructs a new TestFile located at a filePath.
     * @param filePath the relative location of the test file in the file system
     */
    public TestFile(String filePath){
        this.filePath=filePath;
        tests=new ArrayList<TestCase>();
        estimatedRunTime=0;
    }

    /**
     * Adds a {@link TestCase} to the file.
     * @param testCase the TestCase to be added
     */
    public void addTestCase(TestCase testCase){
        tests.add(testCase);
        estimatedRunTime += testCase.getEstimatedRunTime();
    }

    /**
     * Calculates the estimated runTime of all of the testCases in the file.
     * @return a double estimate of the runTime of this TestFile
     */
    public double getEstimatedRunTime(){
        return estimatedRunTime;
    }

    /**
     * Gets the number of testCases in this TestFile.
     * @return the integer number of testCases in this file
     */
    public int getNumberOfTestCases(){
        return tests.size();
    }

    /**
     * Retrieves the relative Filepath of this TestFile.
     * @return the String filepath of this file
     */
    public String getFilePath(){
        return filePath.substring(2);
    }

    public int compareTo(TestFile o) {
        if(this.getEstimatedRunTime() > o.getEstimatedRunTime()){
            return -1;
        }else{
            return 1;
        }
    }

    /**
     * Checks if the testFile is a support file, and cannot be invoked directly
     * @return
     */
    public boolean isSupportFile(){
        return (filePath.contains("spec/support"));
    }

    public String toString(){
        return this.getFilePath() + ",   "+estimatedRunTime+", "+ tests.size()+", ";
    }

    public JSONObject toJson(){
        JSONObject out=new JSONObject();
        out.put("filepath", this.getFilePath());
        out.put("estimated_run_time", this.getEstimatedRunTime());
        out.put("test_cases", this.getNumberOfTestCases());

        return out;
    }


}

