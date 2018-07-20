package jobstats;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Set;
import java.util.LinkedHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author anabawy
 *
 */
public class JobRunTimes {

	static LinkedHashMap<String, LinkedHashMap<String, JobExecutionLog>> joblog = new LinkedHashMap<String, LinkedHashMap<String, JobExecutionLog>>();
	public static void loadJobExecutionLog() {
		String fileName = "JobStatistics/src/resources/job_execution_log.csv";
		JobExecutionLog jobLog ;
		
		File file = new File(fileName);
		String jobName;
		try {
			int rowCount=-1;
			Scanner inputStream = new Scanner(file);
			while (inputStream.hasNext()) {
				String data = inputStream.next();
				String[] values = data.split(",");
	
				rowCount++;
				//skip header
				if (rowCount == 0)
					continue;
				
				if (values.length < 5)
					continue;
				
				jobName = values[0];
				jobLog = new JobExecutionLog() ;
				jobLog.startTime = values[1];
				jobLog.endTime = values[2];
				jobLog.status = values[3];
				jobLog.runDate = values[4];
			    
				//skip over failed jobs
				if (!jobLog.status.equalsIgnoreCase("SUCCESS"))
					continue;
				
				LinkedHashMap<String, JobExecutionLog> execLog = joblog.get(jobName);
				
				if (execLog == null) {	
					execLog = new LinkedHashMap<String, JobExecutionLog>();
				}
			
				DateFormat sdf = new SimpleDateFormat("hh:mm:ss");
			    try {
					Date dateStart = sdf.parse(jobLog.startTime);
					Date dateEnd = sdf.parse(jobLog.endTime);
					
					jobLog.runTime = Math.abs(dateEnd.getTime() - dateStart.getTime());
				    
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			    execLog.put(jobLog.runDate, jobLog);
				joblog.put(jobName, execLog);
			}
			inputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	private static long getJobAverageRuntime(String jobName) {
		LinkedHashMap<String, JobExecutionLog> execLog = joblog.get(jobName);
		long totalRunTime = 0;
		
		for (String key : execLog.keySet()) {
			JobExecutionLog log = execLog.get(key);
			totalRunTime = totalRunTime + log.runTime;
		}
		return totalRunTime/execLog.size();
	}
	private static String getFormattedTime(long time) {
		return String.format("%02d:%02d:%02d", 
				TimeUnit.MILLISECONDS.toHours(time),
				TimeUnit.MILLISECONDS.toMinutes(time) -  
				TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(time)), 
				TimeUnit.MILLISECONDS.toSeconds(time) - 
				TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(time))); 
	}
	public static void printLastAveRunTimes() {
		if (joblog.isEmpty())
			return;
		System.out.println("job_name" + "\t" + "last_run_date" + "\t" + "last_run_time" +  "\t" + "avg_run_time_7");
		for (String key: joblog.keySet()) {
			LinkedHashMap<String, JobExecutionLog> execLog = joblog.get(key);
			Set<String> keys = execLog.keySet();
			String logKey = null;
			//get last inserted key date
			for (String keyDate: keys) {
				logKey = keyDate;
			}
			JobExecutionLog log = execLog.get(logKey);
			
			System.out.println(key + "\t\t" + log.runDate +  "\t" +  getFormattedTime(log.runTime) + "\t" + getFormattedTime(getJobAverageRuntime(key)));
			
		}
	}
	public static void main(String[] args) {
		loadJobExecutionLog();
		printLastAveRunTimes();
	}
}
