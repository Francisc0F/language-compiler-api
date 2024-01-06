package francisco.languagecompiler.resource.service;

import francisco.languagecompiler.resource.model.Job;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Service
public class JobsService {
    List<Job> jobs = new ArrayList<>();


    public Stream<Job> getStream() {
        return jobs.stream();
    }

    public Job get(String id) {
        return jobs.stream()
                .filter(op -> Objects.equals(op.getId(), id))
                .findFirst()
                .orElse(null);
    }

    public void removeBuildById(String id) {
        // Remove the Build with the specified ID
        jobs.removeIf(build -> Objects.equals(build.getId(), id));
    }

    public Job add(Job op) {
            jobs.add(op);
            return op;
    }
}
