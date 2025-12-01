package se.lnu.os.ht25.a1;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import se.lnu.os.ht25.a1.provided.data.ProcessInformation;

public class TestUtils {

	private static double toleranceMillis = 0.250;

	public static boolean checkEqual(List<ProcessInformation> a, List<ProcessInformation> b) {
		assertEquals(a.size(), b.size());

		return checkEqualUpToIndex(a, b, a.size());

	}

	public static boolean checkEqualUpToIndex(List<ProcessInformation> a, List<ProcessInformation> b, int length) {
		assertTrue(a.size() >= length,
				"Expected that the size of the array a is at least the number of elements to check " + a.size() + "/"
						+ length);
		assertTrue(b.size() >= length,
				"Expected that the size of the array b is at least the number of elements to check " + b.size() + "/"
						+ length);
		for (int i = 0; i < length; i++) {
			if (!checkEquals(a.get(i), b.get(i))) {
				return false;
			}
		}
		return true;
	}

	private static boolean checkEquals(ProcessInformation a, ProcessInformation b) {

		System.out.println("Process names:" + a.getProcessName() + "/" + b.getProcessName() + "arrival times:"
				+ a.getArrivalTime()  + "/" + b.getArrivalTime() 
				+ "   end times: " + a.getEndTime()  + "/" + b.getEndTime() 
				+ " (difference " + (a.getEndTime() - b.getEndTime()) + ")   scheduledTime:"
				+ a.getCpuScheduledTime()  + "/" + b.getCpuScheduledTime() );
		assertTrue(similar(a.getArrivalTime(), b.getArrivalTime()),
				"expected similar: " + a.getArrivalTime() + "," + b.getArrivalTime());
		assertEquals(a.getProcessName(), b.getProcessName());
		assertTrue(similar(a.getEndTime(), b.getEndTime()),
				"expected similar: " + a.getEndTime() + "," + b.getEndTime());
		assertTrue(similar(a.getCpuScheduledTime(), b.getCpuScheduledTime()), "expecting similar values between scheduled times " + a.getCpuScheduledTime()+"/" + b.getCpuScheduledTime());
		assertTrue(same(a.getCpuBurstDuration(), b.getCpuBurstDuration()),
				"expected same for" + a.getProcessName()+ "/" +b.getProcessName()+ ": " + a.getCpuBurstDuration() + ",  " + b.getCpuBurstDuration());
		return true;
	}

	private static boolean similar(double d, double e) {
		return Math.abs(d - e) < toleranceMillis;
	}

	private static boolean same(double a, double b) {
		// Some small tolerance for doubles in case of format conversions during the
		// student's code
		return Math.abs(a - b) < 0.01;
	}

}
