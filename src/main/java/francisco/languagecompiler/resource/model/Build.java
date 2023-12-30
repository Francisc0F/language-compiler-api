package francisco.languagecompiler.resource.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.protobuf.FieldMask;
import francisco.languagecompiler.resource.langadapters.CAdapter;
import francisco.languagecompiler.resource.langadapters.LangAdapter;
import francisco.languagecompiler.resource.util.ResponseMaker;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Build extends BaseResource implements ExecutableBuild, ResponseMaker {

    private String name;
    private String code;
    @JsonProperty("language")
    private BuildLang language;
    private BuildStatus status = BuildStatus.PENDING;
    private String reason;
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

    public Build() {
        super();
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

        if (fieldMask.toString().contains("language")) {
            resultMap.put("language", this.language);
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
        resultMap.put("language", this.language);
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

        LangAdapter adapter = null;
        if (Objects.requireNonNull(this.language) == BuildLang.C) {
            adapter = new CAdapter(this);
        }

        if (adapter == null) {
            reason = "No adapter for " + this.language.getText();
            status = BuildStatus.ABORTED;
        }

        assert adapter != null;
        adapter.execute();

        completedAt = new Date();
        System.out.println("Finished at " + formatDate(completedAt));
        status = BuildStatus.SUCCESS;
    }


    private String formatDate(Date date) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(date);
    }

    public BuildStatus getStatus() {
        return status;
    }

    public BuildLang getLang() {
        return language;
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
