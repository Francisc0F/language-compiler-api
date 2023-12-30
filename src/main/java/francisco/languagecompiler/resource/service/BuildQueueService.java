package francisco.languagecompiler.resource.service;

import francisco.languagecompiler.resource.model.Build;
import francisco.languagecompiler.resource.model.ExecutableBuild;
import org.springframework.stereotype.Service;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.LinkedList;
import java.util.Queue;

import static francisco.languagecompiler.resource.model.BuildStatus.IN_QUEUE;


@Service
public class BuildQueueService implements PropertyChangeListener {
    BuildNotifier notifier = new BuildNotifier();
    BuildRunner runnner = new BuildRunner(notifier);
    private final Queue<ExecutableBuild> buildQueue = new LinkedList<>();
    private boolean isRunning = false;

    BuildQueueService() {
        notifier.addObserver(runnner);
        notifier.addObserverComplete(this);
    }

/*    private synchronized void runBuild(ExecutableBuild build) {
        buildQueue.offer(build);
        if (!isRunning) {
            startNextBuild();
        }
    }

    private void startNextBuild() {
        if (!buildQueue.isEmpty()) {
            ExecutableBuild nextBuild = buildQueue.poll();
            isRunning = true;
            nextBuild.execute();
            buildComplete();

        } else {
            isRunning = false;
        }
    }*/

  /*  private void buildComplete() {
        isRunning = false;
        startNextBuild();
    }*/

    public void addToQueue(Build build) {
        build.setStatus(IN_QUEUE);
        if(buildQueue.isEmpty()){
            attachToNotifier(build);
        }
        buildQueue.offer(build);
    }

    synchronized private void attachToNotifier(Build build) {
        runnner.setBuild(build);
        notifier.notify("Trigger build " + build);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        // on complete build
        Build completedBuild = (Build) evt.getNewValue();

        System.out.println("Completed = " + completedBuild);

        buildQueue.remove(completedBuild);

        if (!buildQueue.isEmpty()) {
            Build nextBuild = (Build) buildQueue.poll();
            attachToNotifier(nextBuild);
        }
    }
}
