package francisco.languagecompiler.resource.model;

import com.google.protobuf.FieldMask;
import francisco.languagecompiler.resource.util.ResponseMaker;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static francisco.languagecompiler.resource.util.FieldMaskMapper.validateFieldMask;

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
     *  create operation for each run
     */
    private boolean createOperation;

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
