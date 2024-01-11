package francisco.languagecompiler.resource.service;


import francisco.languagecompiler.resource.model.BuildOperation;
import francisco.languagecompiler.resource.model.BuildResultExecution;
import francisco.languagecompiler.resource.model.ExecutableOperation;
import francisco.languagecompiler.resource.model.Operation;
import francisco.languagecompiler.resource.util.observer.EventListener;
import francisco.languagecompiler.resource.util.observer.OperationPublisher;

import java.util.concurrent.CompletableFuture;

public class OperationRunner implements EventListener<Operation> {
    ExecutableOperation executableOperation;
    OperationPublisher operationPublisher;

    OperationRunner(OperationPublisher operationPublisher) {
        this.operationPublisher = operationPublisher;
    }

    public void setExecutableOperation(ExecutableOperation executableOperation) {
        this.executableOperation = executableOperation;
    }


    private void run(Operation obj) {
        CompletableFuture.runAsync(() -> {
            try {
                executableOperation.execute();
            } catch (Exception ex) {
                ((BuildOperation) obj).setExecutionStoppedReason(ex.getMessage());
            } finally {
                operationPublisher.notify("complete", (Operation) executableOperation);
                obj.setDone(true);
            }
        });
    }


    /**
     * On operation start
     *
     * @param eventType
     * @param obj
     */
    @Override
    public void handleEvent(String eventType, Operation obj) {
        System.out.println("Event Type: " + eventType + " Op: " + obj);
        run(obj);
    }
}
