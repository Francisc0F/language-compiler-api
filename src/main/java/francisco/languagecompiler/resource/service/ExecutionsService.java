package francisco.languagecompiler.resource.service;

import francisco.languagecompiler.resource.model.Execution;
import org.springframework.stereotype.Service;


@Service
public class ExecutionsService extends BaseService<Execution> {
    public ExecutionsService() {
        Execution a = new Execution();
        items.add(a);
    }
}
