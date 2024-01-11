package francisco.languagecompiler.resource.langadapters;

import francisco.languagecompiler.resource.model.BuildOperation;

import java.io.IOException;

public class GccAdapter extends BaseCompilerAdapter  {

    public GccAdapter(BuildOperation operation) {
        super(operation);
    }

    public int langCompiler(BuildOperation op) throws InterruptedException, IOException {
        String fileName = createFile("c");

        String runableCProgram = fileName.replace(".c", "");
        String buildCommand = "gcc -o " + runableCProgram + " " + fileName;

        int exitCode = runProcessFor(op, buildCommand);
        if(exitCode == 0){
            op.setExecutablePath(runableCProgram);
        }

        return exitCode;
    }
}
