package francisco.languagecompiler.resource.requests;

import lombok.Getter;

public class OperationRequest {

    @Getter
    private String buildId;

    public OperationRequest(String buildId) {
        this.buildId = buildId;
    }
}
