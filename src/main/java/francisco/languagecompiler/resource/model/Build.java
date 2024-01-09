package francisco.languagecompiler.resource.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

public class Build extends BaseResource  {

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

    @Override
    public int hashCode() {
        return Objects.hash(id, name, code);
    }
}
