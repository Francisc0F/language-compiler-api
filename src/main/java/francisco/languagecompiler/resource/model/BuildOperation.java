package francisco.languagecompiler.resource.model;

import com.google.protobuf.FieldMask;
import francisco.languagecompiler.resource.langadapters.GccAdapter;
import francisco.languagecompiler.resource.langadapters.JSAdapter;
import francisco.languagecompiler.resource.langadapters.JavacAdapter;
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

        LangAdapter adapter = createLanguageAdapter();
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

    private LangAdapter createLanguageAdapter() {
        LangAdapter adapter = null;

        if (Objects.requireNonNull(this.build.getLanguage()) == BuildLang.C) {
            adapter = new GccAdapter(this);
        }
        if (Objects.requireNonNull(this.build.getLanguage()) == BuildLang.Javascript) {
            adapter = new JSAdapter(this);
        }

        if (Objects.requireNonNull(this.build.getLanguage()) == BuildLang.Java) {
            adapter = new JavacAdapter(this);
        }

        return adapter;
    }

    public void setStatus(BuildStatus buildStatus) {
        buildResult();
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

    public void setExecutionStoppedReason(String message) {
        buildResult();
        getResult().errBuildReason = message;
    }
}
