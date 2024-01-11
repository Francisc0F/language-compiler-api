package francisco.languagecompiler.resource.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class BuildResultExecution extends Execution implements Cloneable {

    public int exitCode;

    public BuildStatus status;

    List<String> stdErrorLines = new ArrayList<>();

    List<String> stdOutLines = new ArrayList<>();

    String errBuildReason;

    String executablePath;

    private Date startedAt;
    private Date completedAt;

    @Override
    public BuildResultExecution clone() {
        BuildResultExecution clonedExecution = new BuildResultExecution();
        clonedExecution.exitCode = this.exitCode;
        clonedExecution.startedAt = this.startedAt;
        clonedExecution.completedAt = this.completedAt;
        clonedExecution.errBuildReason = this.errBuildReason;
        clonedExecution.status = status;
        clonedExecution.stdErrorLines = new ArrayList<>(this.stdErrorLines);
        clonedExecution.stdOutLines = new ArrayList<>(this.stdOutLines);
        clonedExecution.executablePath = this.executablePath;
        return clonedExecution;
    }
}