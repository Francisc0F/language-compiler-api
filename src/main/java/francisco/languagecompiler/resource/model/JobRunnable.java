package francisco.languagecompiler.resource.model;

import francisco.languagecompiler.resource.service.OperationQueueService;
import francisco.languagecompiler.resource.service.OperationsService;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;


public class JobRunnable implements Runnable {

    private final Job job;
    private Build build;
    private OperationQueueService operationQueueService;
    private OperationsService operationsService;
    private ScheduledExecutorService scheduler;
    private ScheduledFuture<?> scheduledFuture;

    public JobRunnable(Job job, OperationQueueService operationQueueService, Build build) {
        this.job = job;
        this.build = build;
        this.operationQueueService = operationQueueService;
    }

    @Override
    public void run() {
        System.out.println("Running job: " + this.job.getName());
        Operation operation = new BuildOperation(build);
        this.operationsService.add(operation);
        this.operationQueueService.addToQueue(operation);
        this.job.incRuns();
        if (this.job.completed()) {
            stopJob();
        }
    }

    public void scheduleJob() {
        this.job.setScheduled(true);
        scheduler = Executors.newScheduledThreadPool(1);
        scheduledFuture = scheduler.scheduleAtFixedRate(this, this.job.getInitialDelay(), this.job.getPeriod(), TimeUnit.MILLISECONDS);
    }

    public void stopJob() {
        if (scheduledFuture != null && !scheduledFuture.isCancelled()) {
            scheduledFuture.cancel(true);
        }
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdown();
        }
        this.job.setScheduled(false);
    }
    public void setOperationsService(OperationsService operationsService) {
        this.operationsService = operationsService;
    }
}
