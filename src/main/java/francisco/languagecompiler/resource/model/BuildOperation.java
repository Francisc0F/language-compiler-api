package francisco.languagecompiler.resource.model;

import com.google.protobuf.FieldMask;
import francisco.languagecompiler.resource.langadapters.CAdapter;
import francisco.languagecompiler.resource.langadapters.LangAdapter;
import francisco.languagecompiler.resource.util.DateUtil;

import java.util.Date;
import java.util.Map;
import java.util.Objects;

import static francisco.languagecompiler.resource.util.FieldMaskMapper.validateFieldMask;

public class BuildOperation extends Operation<BuildResultExecution> {

    Build build;
    String buildId;

    public String getOperationFileName() {
        return "Op_" + build.getName().replace(" ", "_");
    }

    public BuildOperation(Build build) {
        super();
        this.build = build;
        metadata = new Metadata();
        metadata.setType("codecompile");
        metadata.setId(build.getId());
    }

    public BuildOperation(String buildId) {
        super();
        this.buildId = buildId;
        metadata = new Metadata();
        metadata.setType("codecompile");
        metadata.setId(this.buildId);
    }

    @Override
    public void execute() {
        this.metadata.setStartTime(new Date());
        System.out.println("Started - " + this + " at " + DateUtil.formatDate(this.metadata.getStartTime()));
        setStatus(BuildStatus.IN_PROGRESS);

        LangAdapter adapter = null;

        if (Objects.requireNonNull(this.build.getLanguage()) == BuildLang.C) {
            adapter = new CAdapter(this);
        }

        if (adapter == null) {
            //reason = "No adapter for " + this.language.getText();
            setStatus(BuildStatus.ABORTED);

        }

        assert adapter != null;
        adapter.execute();

        System.out.println("Started - " + this + " at " + DateUtil.formatDate(this.metadata.getEndTime()));
        this.metadata.setEndTime(new Date());
        setStatus(BuildStatus.SUCCESS);
    }

    public void setStatus(BuildStatus buildStatus) {
        getResult().setStatus(buildStatus);
    }

    public String getBuildCode() {
        return build.getCode();
    }

    public void addStdOutLine(String outputLine) {
        buildResult();
        getResult().stdOutLines.add(outputLine);
    }

    public void addStdErrorLine(String outputLine) {
        buildResult();
        getResult().stdErrorLines.add(outputLine);
    }
    public void setExecutablePath(String runPath) {
        buildResult();
        getResult().setExecutablePath(runPath);
    }

    public void setExitCode(int exitCode) {
        buildResult();
        getResult().exitCode = exitCode;
    }

    public Map<String, Object> toMap(FieldMask fm) {
        return validateFieldMask(this, fm);
    }

    private void buildResult() {
        if (getResult() == null) {
            setResult(new BuildResultExecution());
        }
    }
}
