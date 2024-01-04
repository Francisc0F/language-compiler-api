package francisco.languagecompiler.resource.langadapters;

import francisco.languagecompiler.resource.model.Build;
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

// more like a decorator
public class CAdapter implements LangAdapter {
    BuildOperation operation;

    public CAdapter(BuildOperation operation) {
        this.operation = operation;
    }

    @Override
    public void execute() {

        try {
            String fileName = createCFile();

            String buildCommand = "gcc -o " + fileName.replace(".c", "") + " " + fileName;
            ProcessBuilder processBuilder = new ProcessBuilder(buildCommand.split("\\s+"));
            Process process = processBuilder.start();

            InputStream inputStream = process.getInputStream();
            InputStream errorStream = process.getErrorStream();

            BufferedReader outputReader = new BufferedReader(new InputStreamReader(inputStream));
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(errorStream));

            String outputLine;
            while ((outputLine = outputReader.readLine()) != null) {
                this.operation.addStdOutLine(outputLine);
            }

            String errorLine;
            while ((errorLine = errorReader.readLine()) != null) {
                this.operation.addStdErrorLine(errorLine);
            }
            outputReader.close();
            errorReader.close();
            inputStream.close();
            errorStream.close();

            int exitCode = process.waitFor();
            System.out.println("Exit code " + exitCode);
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

    private String createCFile() throws IOException {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = this.operation.getOperationName() + "_" + timestamp + ".c";
        Path filePath = Path.of(fileName);
        Files.write(filePath, this.operation.getBuildCode().getBytes(), StandardOpenOption.CREATE);
        return fileName;
    }
}
