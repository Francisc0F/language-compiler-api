package francisco.languagecompiler.resource.service;

import francisco.languagecompiler.resource.model.Build;
import francisco.languagecompiler.resource.model.BuildLang;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Service
public class BuildsService extends BaseService<Build> {

    public BuildsService() {
        items.add(new Build("5d4e1e78-804b-4417-a4c7-fbcf2eddd8ea",
                "C Build",
                "#include <stdio.h>\n int main() {\n printf(\"Hello, World!\");\n return 0;\n}",
                BuildLang.C));
        items.add(new Build("C Build with Error", "#include <stdio.h>\n int maXin() {\n printf(\"Hello, World!\");\n return 0;\n}", BuildLang.C));
    }
}