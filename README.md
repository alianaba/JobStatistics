# JobStatistics

Job Lineage: Run class JobLineage
Assumptions: 
    In job_metadata.csv the job start time is always the same for multiple entries for the same job name.
    A job can have any number of dependent jobs
  
  
Last and Average Run Times: Run class JobRunTimes
Assumptions: 
    A job is run only once a day
    We are not interested in failed jobs so I have chosen to not include them in the load
    
Late Starts: Run class JobLateStarts
Assumptions: 


Upstream Late Start: Run class UpstreamLateStart
Assumptions: We display the first dependent job that caused a late start of the job. We look no further for other causing dependent jobs.
