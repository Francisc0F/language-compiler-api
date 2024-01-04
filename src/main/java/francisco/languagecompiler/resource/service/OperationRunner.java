package francisco.languagecompiler.resource.service;



import francisco.languagecompiler.resource.model.Build;
import francisco.languagecompiler.resource.model.ExecutableOperation;
import francisco.languagecompiler.resource.model.Operation;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.concurrent.CompletableFuture;

public class OperationRunner implements PropertyChangeListener {
    ExecutableOperation executableOperation;
    OperationNotifier notifier;

    OperationRunner(OperationNotifier notifier) {
        this.notifier = notifier;
    }

    public void setExecutableOperation(ExecutableOperation executableOperation) {
        this.executableOperation = executableOperation;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        CompletableFuture.runAsync(() -> {
            try {
                executableOperation.execute();
                notifier.notifyComplete((Operation) executableOperation);
            } catch (Exception ex) {
                ex.printStackTrace();
                System.out.println("Not able to complete -");
            }
        });
    }
}
