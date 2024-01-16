package francisco.languagecompiler.resource.langadapters;

import francisco.languagecompiler.resource.model.BuildOperation;

import java.io.IOException;

public class GplusplusAdapter extends BaseCompilerAdapter  {

    public GplusplusAdapter(BuildOperation operation) {
        super(operation);
    }

    public int langCompiler(BuildOperation op) throws InterruptedException, IOException {
        String fileName = createFile("cpp");

        String runableCProgram = fileName.replace(".cpp", "");
        String buildCommand = "g++ -o " + runableCProgram + " " + fileName;

        int exitCode = run(op, buildCommand, runableCProgram, fileName);
        return exitCode;
    }
}
