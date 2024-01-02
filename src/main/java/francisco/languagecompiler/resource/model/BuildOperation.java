package francisco.languagecompiler.resource.model;

import francisco.languagecompiler.resource.langadapters.CAdapter;
import francisco.languagecompiler.resource.langadapters.LangAdapter;
import francisco.languagecompiler.resource.util.DateUtil;
import lombok.Getter;

import java.util.Date;
import java.util.Objects;

public class BuildOperation extends Operation<Build.BuildResultTOperation> implements ExecutableOperation {

    Build build;

    public String getOperationName(){
        return "Op_" + build.getName();
    }

    public BuildOperation(Build build) {
        super();
        this.build = build;
        metadata = new Metadata();
        metadata.setType("codecompile");
        metadata.setId(build.getId());
    }

    @Override
    public void execute() {
        this.metadata.setStartTime(new Date());
        System.out.println("Started - " + this + " at " + DateUtil.formatDate(this.metadata.getStartTime()));
        this.build.setStatus(BuildStatus.IN_PROGRESS);


        LangAdapter adapter = null;

        if (Objects.requireNonNull(this.build.getLang()) == BuildLang.C) {
            adapter = new CAdapter(this);
        }

        if (adapter == null) {
            //reason = "No adapter for " + this.language.getText();
            this.build.setStatus(BuildStatus.ABORTED);
        }

        assert adapter != null;
        adapter.execute();

        System.out.println("Started - " + this + " at " + DateUtil.formatDate(this.metadata.getEndTime()));
        this.metadata.setEndTime(new Date());
        this.build.setStatus(BuildStatus.SUCCESS);
    }

    public void setStatus(BuildStatus buildStatus) {
        build.setStatus(buildStatus);
    }

    public String getBuildCode() {
        return build.getCode();
    }

    public void addStdOutLine(String outputLine) {
        this.metadata.getResult().stdOutLines.add(outputLine);
    }

    public void addStdErrorLine(String outputLine) {
        this.metadata.getResult().stdErrorLines.add(outputLine);
    }

    public void setExitCode(int exitCode) {
        this.metadata.getResult().exitCode = exitCode;
    }
}
