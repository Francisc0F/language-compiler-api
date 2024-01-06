package francisco.languagecompiler.resource.model;

import com.fasterxml.jackson.annotation.JsonValue;

public enum BuildLang {

    Unknown("Unknown"),
    C("C"),
    CPlusPlus("C++"),
    Java("Java");

    private final String text;

    BuildLang(String txt) {
        this.text = txt;
    }

    @JsonValue
    public String getText() {
        return text;
    }


    public static BuildLang getBuildLangFromString(String languageStr) {
        for (BuildLang lang : BuildLang.values()) {
            if (lang.getText().equalsIgnoreCase(languageStr)) {
                return lang;
            }
        }
        return null;  // or throw an exception if the string is not a valid language
    }
}