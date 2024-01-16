package francisco.languagecompiler.resource.langadapters;

import francisco.languagecompiler.resource.model.BuildOperation;
import francisco.languagecompiler.resource.model.BuildStatus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.Date;

public abstract  class BaseCompilerAdapter implements LangAdapter{

    protected BuildOperation operation;

    BaseCompilerAdapter(BuildOperation operation){
        this.operation = operation;
    }

    abstract int langCompiler(BuildOperation op) throws InterruptedException, IOException;

    protected int runProcessFor(BuildOperation op, String buildCommand) throws IOException, InterruptedException {
        ProcessBuilder processBuilder = new ProcessBuilder(buildCommand.split("\\s+"));
        Process process = processBuilder.start();

        InputStream inputStream = process.getInputStream();
        InputStream errorStream = process.getErrorStream();

        BufferedReader outputReader = new BufferedReader(new InputStreamReader(inputStream));
        BufferedReader errorReader = new BufferedReader(new InputStreamReader(errorStream));

        String outputLine;
        while ((outputLine = outputReader.readLine()) != null) {
            op.addStdOutLine(outputLine);
        }

        String errorLine;
        while ((errorLine = errorReader.readLine()) != null) {
            op.addStdErrorLine(errorLine);
        }


        outputReader.close();
        errorReader.close();
        inputStream.close();
        errorStream.close();

        int exitCode = process.waitFor();
        return exitCode;
    }

    protected  String createFile(String fileFormat) throws IOException {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = this.operation.getOperationFileName() + "_" + timestamp + "." + fileFormat;
        Path filePath = Path.of(fileName);
        Files.write(filePath, this.operation.getBuildCode().getBytes(), StandardOpenOption.CREATE);
        return fileName;
    }

    protected void deleteFile(String fileName) throws IOException {
        Path filePath = Path.of(fileName);
        Files.delete(filePath);
    }

    @Override
    public void execute() {

        try {
            int exitCode = langCompiler(this.operation);

            this.operation.setExitCode(exitCode);
            if (exitCode == 0) {
                this.operation.setStatus(BuildStatus.SUCCESS);
            } else {
                this.operation.setStatus(BuildStatus.FAILURE);
            }
        } catch (InterruptedException e) {
            this.operation.setStatus(BuildStatus.FAILURE);

        } catch (IOException e) {
            this.operation.setStatus(BuildStatus.FAILURE);
            throw new RuntimeException(e);
        }
    }

    protected int run(BuildOperation op, String buildCommand, String runableCProgram, String fileName) throws IOException, InterruptedException {
        op.setStarted();
        int exitCode = runProcessFor(op, buildCommand);
        if(exitCode == 0){
            op.setExecutablePath(runableCProgram);
        } else {
            deleteFile(fileName);
        }
        op.setCompleted();
        return exitCode;
    }
}
