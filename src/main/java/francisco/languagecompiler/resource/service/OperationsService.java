package francisco.languagecompiler.resource.service;

import francisco.languagecompiler.resource.model.Operation;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Service
public class OperationsService {
    List<Operation> operationList = new ArrayList<>();


    public Stream<Operation> getStream() {
        return operationList.stream();
    }

    public Operation get(String id) {
        return operationList.stream()
                .filter(build -> Objects.equals(build.getId(), id))
                .findFirst()
                .orElse(null);
    }

    public void removeBuildById(String id) {
        // Remove the Build with the specified ID
        operationList.removeIf(build -> Objects.equals(build.getId(), id));
    }

    public Operation add(Operation op) {
        if (Objects.equals(op.getMetadata().getType(), "codecompile")) {
            operationList.add(op);
            return op;
        }
        return null;
    }
}
