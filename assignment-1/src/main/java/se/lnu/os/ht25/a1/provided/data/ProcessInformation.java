package se.lnu.os.ht25.a1.provided.data;

public class ProcessInformation implements Cloneable{

	private String processName;
	private double cpuBurstDuration;
	private double arrivalTime;
	private double endTime;

	private double cpuScheduledTime;

	
	protected ProcessInformation() {
		
		arrivalTime = System.currentTimeMillis();
	}
	
	public static ProcessInformation createProcessInformation() { // maybe needed the implements cloneable
		
		ProcessInformation theVisit = new ProcessInformation();
		return theVisit;
		
	}



	
	public String getProcessName() {
		return processName;
	}

	public ProcessInformation setProcessName(String processName) {
		this.processName = processName;
		return this;
	}

	public double getCpuBurstDuration() {
		return cpuBurstDuration;
	}

	public ProcessInformation setCpuBurstDuration(double cpuBurstDuration) {
		this.cpuBurstDuration = cpuBurstDuration;
		return this;
	}

	public double getArrivalTime() {
		return arrivalTime;
	}

	public ProcessInformation setArrivalTime(double arrivalTime) {
		this.arrivalTime = arrivalTime;
		return this;
	}

	public double getEndTime() {
		return endTime;
	}

	public ProcessInformation setEndTime(double endTime) {
		this.endTime = endTime;
		return this;
	}

	public double getCpuScheduledTime() {
		return cpuScheduledTime;
	}

	public ProcessInformation setCpuScheduledTime(double cpuScheduledTime) {
		this.cpuScheduledTime = cpuScheduledTime;
		return this;
	}

	@Override
	public String toString() {
		String result="Process name= "+ getProcessName()+ "; ";
				result+="It wanted to execute at time "+ getArrivalTime();
				result+=" asking for a cpuBurst of "+ getCpuBurstDuration() + " seconds;";
				result+=" It had to wait until "+ getCpuScheduledTime() + " for being scheduled to the CPU; ";
				result+=" It executed and left the CPU at "+ getEndTime() + "." + System.getProperty("line.separator");
				
		return result;
	}
	
	 @Override
	 public Object clone() throws CloneNotSupportedException {
	 return super.clone();
	 }
	
}
