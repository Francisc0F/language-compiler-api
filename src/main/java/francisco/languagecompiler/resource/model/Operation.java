package francisco.languagecompiler.resource.model;

import francisco.languagecompiler.resource.util.ResponseMaker;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

public abstract class Operation<resultT> extends BaseResource implements ResponseMaker, ExecutableOperation {

    @Getter
    @Setter
    public boolean done = false;

    @Getter
    public Metadata metadata;

    public Operation() {
        super();
    }

    public class Metadata extends BaseResource{


        @Getter
        @Setter
        // code build only in this case
        private String type;

        // id of the type item reference -> build

        @Getter
        @Setter
        private Date startTime;
        @Getter
        @Setter
        private Date endTime;

        @Getter
        @Setter
        // progress in percentage
        private int progress;
        @Getter
        @Setter
        private resultT result;

        Metadata(){
            super();
        }

    }
}
