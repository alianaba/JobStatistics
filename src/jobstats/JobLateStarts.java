package jobstats;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TreeMap;

public class JobLateStarts {
	
	/** Prints all jobs with a late start given a date
	 * @param date
	 */
	public static void printLateStarts(String date) {
		System.out.println("User provided date - " + date);
		for (JobMetaData jobMd: JobLineage.jobdata.values()) {
			String expectedStart = jobMd.jobStartTime;
			TreeMap<String, JobExecutionLog> jobLog = JobRunTimes.joblog.get(jobMd.jobName);
			JobExecutionLog execLog = null;
			if (jobLog.containsKey(date))
				execLog = jobLog.get(date);
			
			if (execLog == null)
				continue;
			SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
			Date dtStart;
			Date dtExpectedStart;
			try {
				dtStart = sdf.parse(execLog.startTime);
				dtExpectedStart = sdf.parse(expectedStart);
				
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				continue;
			}
			if (dtExpectedStart.compareTo(dtStart) < 0) {
				System.out.println(jobMd.jobName + "\t" + execLog.startTime + "\t" + expectedStart);
			}
				
		}
	
	}
	
	public static void main(String[] args) {
		String inputDate = "2018-07-05";
		
		JobLineage.loadJobMetaData();
		JobRunTimes.loadJobExecutionLog();
		printLateStarts(inputDate);
	}
}
