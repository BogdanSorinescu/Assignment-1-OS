package se.lnu.os.ht25.a1.required;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import se.lnu.os.ht25.a1.provided.Reporter;
import se.lnu.os.ht25.a1.provided.Scheduler;
import se.lnu.os.ht25.a1.provided.data.ProcessInformation;

public class PrioritySchedulerImpl implements Scheduler {

    private final Reporter reporter;
    private final long startingTime;
    private final Thread cpuThread;
    private final Object queueLock = new Object();
    private Deque<ScheduledProcess> priority0;
    private Deque<ScheduledProcess> priority1;
    private Deque<ScheduledProcess> priority2;
    private ScheduledProcess currentProcess;

    private PrioritySchedulerImpl(Reporter r) {
        this.reporter = r;
        startingTime = System.currentTimeMillis();
        cpuThread = new Thread(this::runCpuLoop, "priority-scheduler-cpu");
    }

    // Factory method to create an instance of the scheduler
    public static Scheduler createInstance(Reporter reporter) {
        Scheduler s = (new PrioritySchedulerImpl(reporter)).initialize();
        return s;
    }

    // Fetches the report of all processes managed by the scheduler
    @Override
    public List<ProcessInformation> getProcessesReport() {
        return reporter.getProcessesReport();
    }

    private Scheduler initialize() {
        priority0 = new ArrayDeque<>();
        priority1 = new ArrayDeque<>();
        priority2 = new ArrayDeque<>();
        cpuThread.start();
        return this;
    }

    /**
     * Handles a new process to schedule from the user. When the user invokes it, a
     * {@link ProcessInformation} object is created to record the process name,
     * arrival time, and the length of the cpuBurst to schedule.
     */
    @Override
    public void newProcess(
        String processName,
        int priority,
        double cpuBurstDuration
    ) {
        ScheduledProcess process = new ScheduledProcess(
            ProcessInformation.createProcessInformation()
                .setProcessName(processName)
                .setCpuBurstDuration(cpuBurstDuration)
                .setArrivalTime(now()),
            cpuBurstDuration,
            priority
        );

        synchronized (queueLock) {
            Deque<ScheduledProcess> targetQueue = getQueueForPriority(priority);
            targetQueue.addLast(process);
            queueLock.notifyAll();
            if (currentProcess != null && priority < currentProcess.priority) {
                cpuThread.interrupt();
            }
        }
    }

    /*
     * This method may help you get the number of seconds since the execution
     * started. Do not feel force to use it, only if you think that it helps your
     * solution
     */
    private double now() {
        return (System.currentTimeMillis() - startingTime) / 1000.0;
    }

    private void runCpuLoop() {
        while (true) {
            // Wait for a process to become ready
            ScheduledProcess toRun;
            synchronized (queueLock) {
                toRun = nextReadyProcess();
                while (toRun == null) {
                    try {
                        queueLock.wait();
                    } catch (InterruptedException ie) {
                        // A new process may have arrived with higher priority; loop to re-evaluate.
                    }
                    toRun = nextReadyProcess();
                }
                currentProcess = toRun;
                if (!toRun.started) {
                    toRun.started = true;
                    toRun.info.setCpuScheduledTime(now());
                }
            }

            // Wait for the process to finish
            double startedAt = now();
            try {
                long sleepMillis = (long) Math.ceil(toRun.remaining * 1000);
                Thread.sleep(sleepMillis);

                double finishedAt = now();
                toRun.info.setEndTime(finishedAt);
                addReport(toRun.info);

                synchronized (queueLock) {
                    currentProcess = null;
                }
            } catch (InterruptedException ie) {
                double elapsed = now() - startedAt;
                if (elapsed < 0) elapsed = 0;
                // Handle interrupted exception
                synchronized (queueLock) {
                    toRun.remaining = Math.max(0, toRun.remaining - elapsed);
                    if (toRun.remaining > 0) {
                        getQueueForPriority(toRun.priority).addFirst(toRun);
                    } else {
                        toRun.info.setEndTime(now());
                        addReport(toRun.info);
                    }
                    currentProcess = null;
                }
            }
        }
    }

    private Deque<ScheduledProcess> getQueueForPriority(int priority) {
        switch (priority) {
            case 0:
                return priority0;
            case 1:
                return priority1;
            case 2:
                return priority2;
            default:
                throw new IllegalArgumentException(
                    "Unknown priority level: " + priority
                );
        }
    }

    private ScheduledProcess nextReadyProcess() {
        if (!priority0.isEmpty()) return priority0.pollFirst();
        if (!priority1.isEmpty()) return priority1.pollFirst();
        if (!priority2.isEmpty()) return priority2.pollFirst();
        return null;
    }

    private void addReport(ProcessInformation info) {
        synchronized (reporter) {
            try {
                reporter.addProcessReport(info);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private static class ScheduledProcess {

        final ProcessInformation info;
        double remaining;
        final int priority;
        boolean started;

        ScheduledProcess(
            ProcessInformation info,
            double remaining,
            int priority
        ) {
            this.info = info;
            this.remaining = remaining;
            this.priority = priority;
            this.started = false;
        }
    }
}
