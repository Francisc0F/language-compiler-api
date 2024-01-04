package francisco.languagecompiler.resource.service;

import francisco.languagecompiler.resource.model.Build;
import francisco.languagecompiler.resource.model.BuildLang;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Service
public class BuildsService {
    List<Build> buildsList = new ArrayList<>();

    public BuildsService() {
        buildsList.add(new Build("C Build", "#include <stdio.h>\n int main() {\n printf(\"Hello, World!\");\n return 0;\n}", BuildLang.C));
        buildsList.add(new Build("C Build with Error", "#include <stdio.h>\n int maXin() {\n printf(\"Hello, World!\");\n return 0;\n}", BuildLang.C));
    }

    public Stream<Build> getStream() {
        return buildsList.stream();
    }

    public Build getBuildById(String id) {
        return buildsList.stream()
                .filter(build -> Objects.equals(build.getId(), id))
                .findFirst()
                .orElse(null);
    }

    public void removeBuildById(String id) {
        // Remove the Build with the specified ID
        buildsList.removeIf(build -> Objects.equals(build.getId(), id));
    }

    public Build addbuild(Build newBuild) {
        buildsList.add(newBuild);
        return newBuild;
    }
}