package francisco.languagecompiler.resource.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class BuildResultExecution extends Execution {

    public int exitCode;

    public BuildStatus status;

    List<String> stdErrorLines = new ArrayList<>();

    List<String> stdOutLines = new ArrayList<>();

    String executablePath;
}