package francisco.languagecompiler.resource.service;

import francisco.languagecompiler.resource.model.Build;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Service
public class BuildsService {
    List<Build> buildsList = new ArrayList<>();
    public BuildsService() {
        buildsList.add(new Build("d4a46ad8-c0f5-4945-8c34-eea862d13b94", "Java", "System.out.prinlm"));
        buildsList.add(new Build("f97de13a-de0a-403e-9b8c-11faf5faffaa", "C", "printf"));
        buildsList.add(new Build( "f683a420-6bbd-4dc3-a5f6-8debcfff1c5a", "C++ ", ">>cout"));
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