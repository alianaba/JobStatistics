package jobstats;

import java.util.Scanner;
import java.util.LinkedHashMap;
import java.io.FileNotFoundException;
import java.io.File;

public class JobLineage {
	static LinkedHashMap<String, JobMetaData> jobdata = new LinkedHashMap<String, JobMetaData>();
	
	/**
	 * Print job hierarchy of dependencies
	 */
	public static void loadJobMetaData() {
		String fileName = "job_metadata.csv";
		JobMetaData jobMd ;
		
		File file = new File(fileName);
		String jobName;
		try {
			Scanner inputStream = new Scanner(file);
			int rowCount=-1;
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
				jobMd = jobdata.get(jobName);
				if (jobMd == null) 
					jobMd = new JobMetaData() ;
				
				String dependentJob = values[3];
				JobMetaData dependentOn = null;
				if (!dependentJob.isEmpty()) {
					dependentOn = jobdata.get(dependentJob);
				
					if (dependentOn == null) {
						dependentOn = new JobMetaData() ;
						dependentOn.jobName = dependentJob;
						jobdata.put(dependentJob, dependentOn);
					}
					jobMd.depJobs.add(dependentOn);
				}
	
				jobMd.jobName = jobName;		
				jobMd.jobOwner = values[1];
				jobMd.jobStartTime = values[2];
				jobMd.jobDate = values[4];
			
				jobdata.put(jobName, jobMd);
			}
			inputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private static void printJobLineageT(JobMetaData jobMd, int indentLevel) {
		if (jobMd == null)
			return;
		for (int i = 0; i < indentLevel; i++) 
			System.out.print("\t");
        
		System.out.println(jobMd.jobName);
		for (int i = 0; i < jobMd.depJobs.size(); i++) {
			printJobLineageT(jobMd.depJobs.get(i), indentLevel+1);
		}
	}
	
	public static void prinJobLineage() {
		if (jobdata.isEmpty())
			return;
		JobMetaData jobMd;
		for (String key: jobdata.keySet()) {
			jobMd = jobdata.get(key);
			printJobLineageT(jobMd, 0);
		}
	}
	public static void main(String[] args) {
		loadJobMetaData();
		prinJobLineage();
	}
 }
