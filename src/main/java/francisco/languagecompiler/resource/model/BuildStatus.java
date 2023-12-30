package francisco.languagecompiler.resource.model;

public enum BuildStatus {
    SUCCESS("Build succeeded", "success"),
    FAILURE("Build failed", "failed"),
    IN_PROGRESS("Build in progress", "in_progress"),
    PENDING("Build pending", "pending"),
    ABORTED("Build aborted", "aborted"),
    IN_QUEUE("In execution line","queue");

    private final String description;
    private final String text;

    BuildStatus(String description, String txt) {
        this.description = description;
        this.text = txt;
    }

    public String getDescription() {
        return description;
    }

    public static BuildStatus fromString(String statusStr) {
        try {
            return BuildStatus.valueOf(statusStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            // Handle the case where the provided statusStr is not a valid BuildStatus
            throw new IllegalArgumentException("Invalid BuildStatus: " + statusStr);
        }
    }
}
