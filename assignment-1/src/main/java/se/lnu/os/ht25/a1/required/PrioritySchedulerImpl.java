package se.lnu.os.ht25.a1.required;

import java.util.List;

import se.lnu.os.ht25.a1.provided.Reporter;
import se.lnu.os.ht25.a1.provided.Scheduler;
import se.lnu.os.ht25.a1.provided.data.ProcessInformation;

public class PrioritySchedulerImpl implements Scheduler {

	private final Reporter reporter;
	private final long startingTime;

	private PrioritySchedulerImpl(Reporter r) {
		this.reporter = r;
		startingTime = System.currentTimeMillis();
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
		// TODO You have to write this method to initialize your Scheduler:
		// For instance, create the CPUthread, the ReporterManager thread, the necessary
		// queues lists/sets, etc.

		return this;
	}

	/**
	 * Handles a new process to schedule from the user. When the user invokes it, a
	 * {@link ProcessInformation} object is created to record the process name,
	 * arrival time, and the length of the cpuBurst to schedule.
	 */
	@Override
	public void newProcess(String processName, int priority, double cpuBurstDuration) {
		// TODO You have to write this method.
	}

	/*
	 * This method may help you get the number of seconds since the execution
	 * started. Do not feel force to use it, only if you think that it helps your
	 * solution
	 */
	private double now() {
		return (System.currentTimeMillis() - startingTime) / 1000.0;
	}
}
