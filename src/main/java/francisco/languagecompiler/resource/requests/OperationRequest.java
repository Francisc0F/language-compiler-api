package francisco.languagecompiler.resource.requests;

import lombok.Getter;
import lombok.Setter;

@Getter
public class OperationRequest {

    @Setter
    private String buildId;

    public OperationRequest() {
    }

    public OperationRequest(String buildId) {
        this.buildId = buildId;
    }
}
