package francisco.languagecompiler.resource.service;

import francisco.languagecompiler.resource.model.Build;
import francisco.languagecompiler.resource.model.BuildOperation;
import francisco.languagecompiler.resource.model.Operation;
import org.springframework.stereotype.Service;

@Service
public class OperationsService extends BaseService<Operation> {

    OperationsService() {
        this.items.add(new BuildOperation(new Build()));
    }
}
