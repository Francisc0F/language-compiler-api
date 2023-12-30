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

    public String getText() {
        return text;
    }
}