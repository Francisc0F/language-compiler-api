package francisco.languagecompiler.resource.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

public class ErrorResponse {
    private List<String> errors;

    public ErrorResponse() {
        this.errors = new ArrayList<>();
    }

    public static ErrorResponse.Builder builder() {
        return new Builder();
    }

    public ResponseEntity<Object> buildErrorResponse(HttpStatus status) {
        if (errors.isEmpty()) {
            errors.add("Unknown error occurred");
        }

        return ResponseEntity.status(status).body(errors);
    }

    public static class Builder {

        private ErrorResponse customErrorResponse;

        private Builder() {
            this.customErrorResponse = new ErrorResponse();
        }

        public boolean hasError(){
            return !customErrorResponse.errors.isEmpty();
        }
        public Builder addError(String error) {
            customErrorResponse.errors.add(error);
            return this;
        }

        public ResponseEntity<Object> build(HttpStatus status) {
            if (customErrorResponse.errors.isEmpty()) {
                customErrorResponse.errors.add("Unknown error occurred");
            }

            return ResponseEntity.status(status).body(customErrorResponse.errors);
        }
        public ResponseEntity<Object> badRequest() {
            if (customErrorResponse.errors.isEmpty()) {
                customErrorResponse.errors.add("Unknown error occurred");
            }

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(customErrorResponse.errors);
        }
        public ResponseEntity<Object> notFound() {
            if (customErrorResponse.errors.isEmpty()) {
                customErrorResponse.errors.add("Unknown error occurred");
            }

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(customErrorResponse.errors);
        }
    }


}
