package francisco.languagecompiler.resource.model;


import com.google.protobuf.FieldMask;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Build extends BaseResource implements ExecutableBuild {

    private String name;
    private String code;
    private BuildStatus status = BuildStatus.PENDING;

    private Date startedAt;
    private Date completedAt;

    public Build(String id, String name, String code) {
        super(id);
        this.name = name;
        this.code = code;
    }

    public Build(String name, String code) {
        super();
        this.name = name;
        this.code = code;
    }

    public void setStatus(BuildStatus status) {
        this.status = status;
    }

    public Map<String, Object> toMap(FieldMask fieldMask) {
        if (fieldMask == null) {
            return this.toMap();
        }

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("id", this.id);

        if (fieldMask.toString().contains("name")) {
            resultMap.put("name", this.name);
        }

        if (fieldMask.toString().contains("code")) {
            resultMap.put("code", this.code);
        }

        if (fieldMask.toString().contains("status")) {
            resultMap.put("status", this.status);
        }

        if (fieldMask.toString().contains("completedAt")) {
            resultMap.put("completedAt", this.completedAt);
        }

        if (fieldMask.toString().contains("startedAt")) {
            resultMap.put("startedAt", this.startedAt);
        }

        return resultMap;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("id", this.id);
        resultMap.put("name", this.name);
        resultMap.put("code", this.code);
        resultMap.put("status", this.status);
        resultMap.put("completedAt", this.completedAt);
        resultMap.put("startedAt", this.startedAt);
        return resultMap;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }


    // Override hashCode() method
    @Override
    public int hashCode() {
        return Objects.hash(id, name, code);
    }

    @Override
    public String toString() {
        return "Build{" + "name='" + name + '\'' + ", code='" + code + '\'' + ", status=" + status + ", startedAt=" + formatDate(startedAt) + ", completedAt=" + formatDate(completedAt) + '}';
    }

    @Override
    public void execute() {
        startedAt = new Date();
        System.out.println("Started - " + this + " at " + formatDate(startedAt));
        status = BuildStatus.IN_PROGRESS;

        try {
            Thread.sleep(30 * 1000);
        } catch (InterruptedException e) {
            status = BuildStatus.FAILURE;
        }

        completedAt = new Date();
        System.out.println("Finished at " + formatDate(completedAt));
        status = BuildStatus.SUCCESS;
    }

    private String formatDate(Date date) {
        if(date == null){
            return "";
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(date);
    }

    public BuildStatus getStatus() {
        return status;
    }

    public static class Builder {
        private String id;
        private String name;
        private String code;

        public Builder() {
            // Initialize with default values or leave them null if not applicable
        }

        public Builder setId(String id) {
            this.id = id;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setCode(String code) {
            this.code = code;
            return this;
        }

        public Build build() {
            return new Build(name, code);
        }
    }

    public static Builder newBuilder() {
        return new Builder();
    }
}
