package francisco.languagecompiler.resource.langadapters;

import francisco.languagecompiler.resource.model.BuildOperation;

import java.io.IOException;

public class JavacAdapter extends BaseCompilerAdapter {

    public JavacAdapter(BuildOperation operation) {
        super(operation);
    }

    @Override
    int langCompiler(BuildOperation op) throws InterruptedException, IOException {
        String fileName = createFile("java");
        //todo: fix java files will always give error, they require Class name = file name
        String buildCommand = "javac " + fileName;
        String runnableJava = fileName.replace(".java", "");

        int exitCode = run(op, buildCommand, runnableJava, fileName);
        return exitCode;
    }
}
