package francisco.languagecompiler.resource.service;



import francisco.languagecompiler.resource.model.Build;
import francisco.languagecompiler.resource.model.ExecutableBuild;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Queue;
import java.util.concurrent.CompletableFuture;

public class BuildRunner implements PropertyChangeListener {
    Queue<ExecutableBuild> buildQueue;
    ExecutableBuild build;
    BuildNotifier notifier;

    BuildRunner(ExecutableBuild build, BuildNotifier notifier) {
        this.build = build;
        this.notifier = notifier;
    }

    BuildRunner(BuildNotifier notifier) {
        this.notifier = notifier;
    }

    public void setBuild(ExecutableBuild build) {
        this.build = build;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        CompletableFuture.runAsync(() -> {
            try {
                build.execute();
                notifier.notifyComplete((Build) build);
            } catch (Exception ex) {
                ex.printStackTrace();
                System.out.println("Not able to complete -");
            }
        });
    }
}
