package se.lnu.os.ht25.a1.required.impl;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import se.lnu.os.ht25.a1.TestUtils;
import se.lnu.os.ht25.a1.provided.Reporter;
import se.lnu.os.ht25.a1.provided.Scheduler;
import se.lnu.os.ht25.a1.provided.data.ProcessInformation;
import se.lnu.os.ht25.a1.provided.impl.ReporterIOImpl;
import se.lnu.os.ht25.a1.required.PrioritySchedulerImpl;

class TestSubmissionA1PriorityScheduler {

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	Scheduler scheduler;
	Reporter reporter;
	ArrayList<ProcessInformation> toCheck;
	long startingTime;

	@BeforeEach
	public void createNewFifoScheduler() {
		reporter = ReporterIOImpl.create();
		//scheduler = PreemptiveSjfSchedulerImpl.createInstance(reporter);
		scheduler = PrioritySchedulerImpl.createInstance(reporter);
		toCheck = new ArrayList<ProcessInformation>();
		startingTime = System.currentTimeMillis();
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void oneProcess() throws InterruptedException {

		System.out.println("======Starting a scheduler of one process=====");

		createRequestAndEntry("Process1", 1, 1.0);

		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.println("Final Report: " + reporter.getProcessesReport().toString());

		assertEquals(1, scheduler.getProcessesReport().size(),
				"scheduler report did not contain the expected number of elements.");
		assertEquals(1, toCheck.size(), "list of elements to check did not contain the expected number of elements.");
		assertTrue(TestUtils.checkEqual(scheduler.getProcessesReport(), toCheck));

	}
	
	
	@Test
	void testExampleInProblemStatement() throws InterruptedException {
		System.out.println("======Starting the eight processes in two batches  with assertions=====");

		ProcessInformation p = createRequestAndEntry("P1", 1, 5.0);
		p.setCpuScheduledTime(0).setEndTime(7);
		Thread.sleep(1000); // Advance time to Arrival time 1
		p = createRequestAndEntry("P2",2, 2.0);
		p.setArrivalTime(1).setCpuScheduledTime(8).setEndTime(10);

		Thread.sleep(1000); // Advance time to Arrival time 2
		p = createRequestAndEntry("P3",1, 1.0);
		p.setArrivalTime(2).setCpuScheduledTime(7).setEndTime(8);
		changeFinishingPositionOfProcess(p, 2);

		Thread.sleep(1000); // Advance time to Arrival time 4
		p = createRequestAndEntry("P4",0, 2.0);
		p.setArrivalTime(3).setCpuScheduledTime(3).setEndTime(5);
		changeFinishingPositionOfProcess(p, 1);

		
		Thread.sleep(8000); // Advance to time 11. All should have finished.

		System.out.println("Final Report: : " + reporter.getProcessesReport().toString());
		System.out.println("Mock: " + toCheck.toString());

		assertEquals(4, reporter.getProcessesReport().size(),
				"scheduler report did not contain the expected number of elements.");
		assertEquals(4, toCheck.size(), "list of elements to check did not contain the expected number of elements.");
		assertTrue(TestUtils.checkEqual(reporter.getProcessesReport(), toCheck));
		
		
	}

	private ProcessInformation createRequestAndEntry(String processName, int priority, double cpuBurstDuration) {
		ProcessInformation v = (ProcessInformation) ProcessInformation
				.createProcessInformation().setArrivalTime(now())
				.setProcessName(processName)
				.setEndTime(now() + cpuBurstDuration)
				.setCpuBurstDuration(cpuBurstDuration)
				.setCpuScheduledTime(now());

		scheduler.newProcess(processName, priority, cpuBurstDuration);

		toCheck.add(v);
		return v;
	}

	private void changeFinishingPositionOfProcess(ProcessInformation p, int i) {
		toCheck.remove(p);
		toCheck.add(i - 1, p);

	}
	
	private double now() {
		return (System.currentTimeMillis() - startingTime) / 1000.0;
	}

}
