package se.lnu.os.ht25.a1.provided.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import se.lnu.os.ht25.a1.provided.Reporter;
import se.lnu.os.ht25.a1.provided.data.ProcessInformation;

public class ReporterIOImpl implements Reporter {

	private List<ProcessInformation> recordBook;

	private ReporterIOImpl() {

		recordBook = new ArrayList<ProcessInformation>();
	}

	public synchronized List<ProcessInformation> getProcessesReport() {
		List<ProcessInformation> copy = new ArrayList<ProcessInformation>(recordBook);
		Collections.copy(copy, recordBook);
		return copy;
	}

	public void addProcessReport(ProcessInformation v) throws InterruptedException {
		recordBook.add(v);
	}

	public static Reporter create() {
		return new ReporterIOImpl();
	}


}
