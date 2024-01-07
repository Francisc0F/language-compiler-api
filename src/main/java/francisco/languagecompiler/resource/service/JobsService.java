package francisco.languagecompiler.resource.service;

import francisco.languagecompiler.resource.model.Execution;
import francisco.languagecompiler.resource.model.Job;
import org.springframework.stereotype.Service;

@Service
public class JobsService extends BaseService<Job>{
    public JobsService() {
        Job a = new Job();
        a.setPeriod(1000);
        a.setInitialDelay(1000);
        a.setName("Job 1");
        a.setMaxRuns(10);
        items.add(a);
    }

}
