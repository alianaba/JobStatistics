package jobstats;

import java.util.ArrayList;

public class JobMetaData {
	public final static int	MAX_DEPENDENTS=10;			//init capacity to maximum of 10 dependent jobs
	public JobMetaData() {		
		depJobs = new ArrayList<JobMetaData>(MAX_DEPENDENTS);
	}
	String 			jobName;
	String 			jobOwner;
	String 			jobStartTime;
	String 			jobDate;
	ArrayList<JobMetaData> 	depJobs; 	
}
