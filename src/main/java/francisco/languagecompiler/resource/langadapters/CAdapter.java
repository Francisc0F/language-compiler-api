package francisco.languagecompiler.resource.langadapters;

import francisco.languagecompiler.resource.model.Build;
import francisco.languagecompiler.resource.model.BuildStatus;

import java.io.IOException;
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

            // Wait for the process to complete
            int exitCode = process.waitFor();
            System.out.println("Exit code " +exitCode);
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
