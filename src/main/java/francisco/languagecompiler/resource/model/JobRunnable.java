package francisco.languagecompiler.resource.model;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;


public class JobRunnable implements Runnable {

    private final Job job;
    private ScheduledExecutorService scheduler;
    private ScheduledFuture<?> scheduledFuture;

    public JobRunnable(Job job) {
        this.job = job;
    }

    @Override
    public void run() {
        System.out.println("Running job: " + this.job.getName());


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

}
