package jobstats;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TreeMap;

public class UpstreamLateStart {
	public static boolean IsLateStart(String date, String jobName) {
		JobMetaData jobMd = JobLineage.jobdata.get(jobName);
		String expectedStart = jobMd.jobStartTime;
		TreeMap<String, JobExecutionLog> jobLog = JobRunTimes.joblog.get(jobMd.jobName);
		JobExecutionLog execLog = null;
		if (jobLog.containsKey(date))
			execLog = jobLog.get(date);
		if (execLog == null)
			return false;
		
		SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
		Date dtStart = null;
		Date dtExpectedStart = null;
		try {
			dtStart = sdf.parse(execLog.startTime);
			dtExpectedStart = sdf.parse(expectedStart);
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		
		if (dtStart==null || dtExpectedStart==null)
			 return false;
		
		return dtExpectedStart.compareTo(dtStart) < 0 ;
	
	}
	
	
	public static void main(String[] args) {
		String inputDate = "2018-07-05";
		String jobName = "job_J";
		
		JobLineage.loadJobMetaData();
		JobRunTimes.loadJobExecutionLog();
		if (IsLateStart(inputDate, jobName))
			FindUpstreamJob(inputDate, jobName);
		
	}

	private static void FindUpstreamJob(String date, String jobName) {
		JobMetaData jobMd = JobLineage.jobdata.get(jobName);
		
		System.out.println("User provided date - " + date);
		System.out.println("User provided job_name - " + jobName);
		
		if (jobMd.depJobs.size() == 0)
			System.out.println("This job has no dependencies");
		else {
			for (JobMetaData jobMetad: jobMd.depJobs ) {
				TreeMap<String, JobExecutionLog> jobLog = JobRunTimes.joblog.get(jobMd.jobName);
				JobExecutionLog execLog = null;
				if (jobLog.containsKey(date))
					execLog = jobLog.get(date);
				
				if (execLog == null)
					continue;
				
				if (IsLateStart(date, jobMetad.jobName)) {
					System.out.println(jobName + " started late on " + date + " because upstream " + jobMetad.jobName + " started late");
				}
			}
		}
		
	}
}
