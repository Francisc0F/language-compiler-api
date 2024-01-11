package francisco.languagecompiler.resource.langadapters;

import francisco.languagecompiler.resource.model.BuildOperation;

import java.io.IOException;

public class JSAdapter extends BaseCompilerAdapter {

    public JSAdapter(BuildOperation operation) {
        super(operation);
    }

    @Override
    int langCompiler(BuildOperation op) throws InterruptedException, IOException {
        String fileName = createFile("js");

        // although this works - we are compiling and running not just compiling lol
        // because javascript is cooler than the others...
        String buildCommand = "node " +  fileName;

        int exitCode = runProcessFor(op, buildCommand);
        if(exitCode == 0){
            op.setExecutablePath(fileName);
        }

        return exitCode;
    }
}
