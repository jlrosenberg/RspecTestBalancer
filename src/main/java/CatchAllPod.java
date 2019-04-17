import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.List;
import java.util.stream.Collectors;

public class CatchAllPod extends Pod {

    private List<Pod> otherPods;
    public CatchAllPod(List<Pod> pods) {
        super("RspecTestCatchAllPod");
        otherPods = pods;
    }

    @Override
    public void addFile(TestFile testFile) {
        throw new IllegalArgumentException("Cannot add files to the catch-all pod");
    }



    public String getPatternString() {
        //TODO Do the toString method for the catch all pod pattern.
        return otherPods.stream().map(p -> p.getPatternString()).collect(Collectors.joining(", "));

        //return super.getPatternString();
    }
    @Override
    protected String getCommandString(){
        return "--exclude-pattern \""+this.getPatternString()+"\"";
    }


}
