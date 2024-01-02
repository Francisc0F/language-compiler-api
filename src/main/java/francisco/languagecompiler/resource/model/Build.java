package francisco.languagecompiler.resource.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.protobuf.FieldMask;
import francisco.languagecompiler.resource.util.FieldMaskMapper;
import francisco.languagecompiler.resource.util.ResponseMaker;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

public class Build extends BaseResource implements ResponseMaker {

    @Getter
    @Setter
    private String name;
    @Getter
    @Setter
    private String code;
    @JsonProperty("language")
    private BuildLang language;

    @Getter
    @Setter
    private BuildStatus status = BuildStatus.PENDING;
    private String reason;
    private Date startedAt;
    private Date completedAt;

    List<Operation> previousRunned = new ArrayList<>();
    Operation opToRun;

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

    public Map<String, Object> toMap(FieldMask fm) {
        return FieldMaskMapper.createHashMapWithFields(this, fm.toString().split(","));
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, code);
    }

    public BuildLang getLang() {
        return language;
    }


    public class BuildResultTOperation {

        @Getter
        @Setter
        public int exitCode;


        @Getter
        @Setter
        List<String> stdErrorLines = new ArrayList<>();
        @Getter
        @Setter
        List<String> stdOutLines = new ArrayList<>();
        @Getter
        @Setter
        String executablePath;

    }
}
