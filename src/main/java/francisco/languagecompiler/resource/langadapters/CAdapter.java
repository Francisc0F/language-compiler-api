package francisco.languagecompiler.resource.langadapters;

import francisco.languagecompiler.resource.model.Build;
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

public class CAdapter implements LangAdapter {
    Build build;

    public CAdapter(Build build) {
        this.build = build;
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

            // todo log this stuff into de the build object
            BufferedReader outputReader = new BufferedReader(new InputStreamReader(inputStream));
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(errorStream));

            String outputLine;
            while ((outputLine = outputReader.readLine()) != null) {
                System.out.println("Standard Output: " + outputLine);
            }

            String errorLine;
            while ((errorLine = errorReader.readLine()) != null) {
                System.err.println("Standard Error: " + errorLine);
            }

// Close the streams
            outputReader.close();
            errorReader.close();
            inputStream.close();
            errorStream.close();

            // Wait for the process to complete
            int exitCode = process.waitFor();
            System.out.println("Exit code " + exitCode);
            if (exitCode == 0) {

            } else {

                this.build.setStatus(BuildStatus.FAILURE);
            }
        } catch (InterruptedException e) {
            this.build.setStatus(BuildStatus.FAILURE);
        } catch (IOException e) {
            this.build.setStatus(BuildStatus.FAILURE);
            throw new RuntimeException(e);
        }
    }

    private String createCFile() throws IOException {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = this.build.getName() + "_" + timestamp + ".c";
        Path filePath = Path.of(fileName);
        Files.write(filePath, this.build.getCode().getBytes(), StandardOpenOption.CREATE);
        return fileName;
    }
}
