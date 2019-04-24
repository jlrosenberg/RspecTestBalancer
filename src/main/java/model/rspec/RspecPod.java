package model.rspec;

import model.Pod;
import model.TestFile;
import model.TestType;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RspecPod implements Pod {

    private List<TestFile> files;
    private String name;
    private double estimatedTime;


    public RspecPod(String name){
        this.name = name;
        estimatedTime=0;
        files= new ArrayList<TestFile>();

    }

    @Override
    public String getName(){
        return this.name;
    }

    @Override
    public void addFile(TestFile testFile){
        if(testFile.getType() != this.getTestType()){
            throw new IllegalArgumentException("TestFile cannot be added because it contains a different type of Test than" +
                "this pod is configured to execute");
        }

        //TODO Add check to make sure we aren't adding a duplicate file
        if(!testFile.isSupportFile()){
            files.add(testFile);
            estimatedTime += testFile.getEstimatedRunTime();
        }else{
            throw new IllegalArgumentException("Test file is a support file, and cannot be added");
        }

    }

    @Override
    public List<TestFile> getFiles(){
        //TODO Return a deep clone of files list instead of actual object to prevent accidental mutation.
        return files;
    }

    @Override
    public double getEstimatedRunTime(){
        return this.estimatedTime;
    }

    @Override
    public String getPatternString(){
        return files.stream().map(f -> f.getFilePath()).collect(Collectors.joining(", "));
    }

    protected String getCommandString(){
       return "--pattern \""+getPatternString()+"\"";
    }

    @Override
    public JSONObject toJson(){
        JSONObject out = new JSONObject();
        out.put("pod_name", getName());
        out.put("estimated_run_time", getEstimatedRunTime());
        out.put("number_of_files", this.getFiles().size());
        out.put("pattern_string", this.getCommandString());

        JSONArray fileArray = new JSONArray();
        for(TestFile f: files){
            fileArray.add(f.toJson());
        }

        out.put("files", fileArray);
        return out;

    }

    @Override
    public TestType getTestType() {
        return TestType.RSPEC;
    }
}
