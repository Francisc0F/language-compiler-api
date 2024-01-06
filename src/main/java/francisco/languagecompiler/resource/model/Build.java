package francisco.languagecompiler.resource.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.protobuf.FieldMask;
import francisco.languagecompiler.resource.util.ResponseMaker;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

import static francisco.languagecompiler.resource.util.FieldMaskMapper.validateFieldMask;

public class Build extends BaseResource implements ResponseMaker {

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private String code;
    @JsonProperty("language")
    @Getter
    @Setter
    private BuildLang language;

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
    public Build(String name, String code, BuildLang language) {
        super();
        this.name = name;
        this.code = code;
        this.language = language;
    }
    public Build(String id, String name, String code, BuildLang language) {
        super(id);
        this.name = name;
        this.code = code;
        this.language = language;
    }

    public Build() {
        super();
    }

    public Map<String, Object> toMap(FieldMask fm) {
        return validateFieldMask(this, fm);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, code);
    }


    public static class BuildResultTOperation {

        @Getter
        @Setter
        public int exitCode;

        @Getter
        @Setter
        public BuildStatus status;

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
