package se.lnu.os.ht25.a1.provided;

public interface Scheduler extends ReportProvider{

	void newProcess(String processName, int priority, double cpuBurstDuration);
	
}
