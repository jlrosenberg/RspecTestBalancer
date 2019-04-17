import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Pod {

    private List<TestFile> files;
    private String name;
    private double estimatedTime;


    public Pod(String name){
        this.name = name;
        estimatedTime=0;
        files= new ArrayList<TestFile>();

    }

    public String getName(){
        return this.name;
    }

    public void addFile(TestFile testFile){
        //TODO Add check to make sure we aren't adding a duplicate file
        if(!testFile.isSupportFile()){
            files.add(testFile);
            estimatedTime += testFile.getEstimatedRunTime();
        }else{
            throw new IllegalArgumentException("Test file is a support file, and cannot be added");
        }

    }

    public List<TestFile> getFiles(){
        //TODO Return a deep clone of files list instead of actual object to prevent accidental mutation.
        return files;
    }

    public double getEstimatedRunTime(){
        return this.estimatedTime;
    }

    public String getPatternString(){
        return files.stream().map(f -> f.getFilePath()).collect(Collectors.joining(", "));
    }

    protected String getCommandString(){
       return "--pattern \""+getPatternString()+"\"";
    }

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

}
