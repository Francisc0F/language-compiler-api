package francisco.languagecompiler.resource.service;

import francisco.languagecompiler.resource.model.Build;
import francisco.languagecompiler.resource.model.ExecutableOperation;
import org.springframework.stereotype.Service;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.LinkedList;
import java.util.Queue;


@Service
public class OperationQueueService implements PropertyChangeListener {
    OperationNotifier notifier = new OperationNotifier();
    OperationRunner runner = new OperationRunner(notifier);
    private final Queue<ExecutableOperation> operationsQueue = new LinkedList<>();

    OperationQueueService() {
        notifier.addObserver(runner);
        notifier.addObserverComplete(this);
    }

    public void addToQueue(ExecutableOperation build) {
        if(operationsQueue.isEmpty()){
            attachToNotifier(build);
        }
        operationsQueue.offer(build);
    }


    synchronized private void attachToNotifier(ExecutableOperation op) {
        runner.setExecutableOperation(op);
        notifier.notify("Trigger build " + op);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        // on complete build
        ExecutableOperation completedBuild = (ExecutableOperation) evt.getNewValue();

        System.out.println("Completed = " + completedBuild);

        operationsQueue.remove(completedBuild);

        if (!operationsQueue.isEmpty()) {
            ExecutableOperation nextBuild = operationsQueue.poll();
            attachToNotifier(nextBuild);
        }
    }
}
