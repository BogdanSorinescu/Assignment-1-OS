package se.lnu.os.ht25.a1.provided;

import se.lnu.os.ht25.a1.provided.data.ProcessInformation;

public interface Reporter extends ReportProvider{

	
	void addProcessReport(ProcessInformation v) throws InterruptedException;
	
	
	
}
