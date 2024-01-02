package francisco.languagecompiler.resource.model;

import com.google.protobuf.FieldMask;
import francisco.languagecompiler.resource.util.FieldsHelper;
import francisco.languagecompiler.resource.util.ResponseMaker;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public abstract class Operation<resultT> extends BaseResource implements ResponseMaker {

    @Getter
    @Setter
    public boolean done = false;

    @Getter
    public Metadata metadata;

    public Operation() {
        super();
    }

    public class Metadata {


        @Getter
        @Setter
        // code build only in this case
        private String type;

        // id of the type item reference -> build
        @Getter
        @Setter
        private String id;
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

        }

        public Map<String, Object> toMap(FieldMask fm, String prefix) {

            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("id", this.id);

            if (FieldsHelper.setField(prefix +".type", fm) ) {
                resultMap.put("type", this.type);
            }

            if (FieldsHelper.setField(prefix +".startTime", fm)) {
                resultMap.put("startTime", this.startTime);
            }

            if (FieldsHelper.setField(prefix +".endTime", fm)) {
                resultMap.put("endTime", this.endTime);
            }

      /*  if (fieldMask.toString().contains("completedAt")) {
            resultMap.put("completedAt", this.completedAt);
        }

        if (fieldMask.toString().contains("startedAt")) {
            resultMap.put("startedAt", this.startedAt);
        }*/

            return resultMap;
        }
    }


    public Map<String, Object> toMap(FieldMask fm) {

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("id", this.id);

        if (FieldsHelper.setField("done", fm) ) {
            resultMap.put("done", this.done);
        }
        resultMap.get(metadata);

        if (FieldsHelper.contains("metadata.", fm)) {

           // this.metadata.toMap(fm)
        }

        return resultMap;
    }
}
