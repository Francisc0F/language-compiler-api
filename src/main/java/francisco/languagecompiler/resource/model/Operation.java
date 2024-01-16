package francisco.languagecompiler.resource.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

public class Operation<resultExecutionT extends Execution> extends BaseResource implements ExecutableOperation {

    @Getter
    @Setter
    public boolean done = false;

    @Getter
    @Setter
    private resultExecutionT result;

    @Getter
    public Metadata metadata;

    public Operation() {
        super();
    }

    @Override
    public void execute() {
        throw new UnsupportedOperationException("execute method is not implemented");
    }

    public class Metadata extends BaseResource{

        @Getter
        @Setter
        // code build only in this case
        private String type;

        @Getter
        @Setter
        private Date startTime;
        @Getter
        @Setter
        private Date endTime;


        Metadata(){
            super();
        }

    }
}
