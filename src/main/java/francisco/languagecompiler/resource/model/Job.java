package francisco.languagecompiler.resource.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

public class Job extends BaseResource  {

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private boolean done;


    @Getter
    @Setter
    /**
     *  associated configuration to run
     */
    private String buildId;

    @Getter
    @Setter
    private int runs = 0;

    public void incRuns(){
        runs++;
    }

    public boolean completed(){
        return runs == maxRuns;
    }

    @Getter
    @Setter
    private int maxRuns = 100;


    @Getter
    @Setter
    boolean scheduled;

    @Getter
    @Setter
    long initialDelay; // in milliseconds

    @Getter
    @Setter
    long period; // in milliseconds

    public Job() {
        super();
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
