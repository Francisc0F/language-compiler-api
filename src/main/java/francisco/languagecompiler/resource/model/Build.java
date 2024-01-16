package francisco.languagecompiler.resource.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

public class Build extends BaseResource {

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

    public Build(){}

    public Build(String name, String code, BuildLang language) {
        super();
        this.name = name;
        this.code = code;
        this.language = language;
    }

    private Build(String id, String name, String code, BuildLang language) {
        super(id);
        this.name = name;
        this.code = code;
        this.language = language;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, code);
    }


    // Builder class
    public static class Builder {
        private String id;
        private String name;
        private String code;
        private BuildLang language;

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder code(String code) {
            this.code = code;
            return this;
        }

        public Builder language(BuildLang language) {
            this.language = language;
            return this;
        }

        public Build build() {
            Build build = new Build();
            build.id = this.id != null ? this.id : build.getId();
            build.name = this.name;
            build.code = this.code;
            build.language = this.language;
            return build;
        }

        public Build Java(String name, String code) {
            this.code = code;
            this.language = BuildLang.Java;
            this.name = name;
            return build();
        }

        public Build C(String uuid, String name, String code) {
            this.id = uuid;
            this.code = code;
            this.language = BuildLang.C;
            this.name = name;
            return build();
        }

        public Build C(String name, String code) {
            this.code = code;
            this.language = BuildLang.C;
            this.name = name;
            return build();
        }

        public Build Js(String helloWorld, String code) {
            this.code = code;
            this.language = BuildLang.Javascript;
            this.name = helloWorld;
            return build();
        }

        public Build CPP(String name, String code) {
            this.code = code;
            this.language = BuildLang.CPlusPlus;
            this.name = name;
            return build();
        }
    }


}
