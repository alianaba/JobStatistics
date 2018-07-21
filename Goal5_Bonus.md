
Loading
One problem with large datasets is holding the entire content of the file dataset in memory. This doesnt scale very well with large files. The current implementation uses the streaming "Scanner" class which loads one line at a time, data is consumed and line is discarded from memory before loading next line. This load implementation should scale for large files.

Memory Consumption
I chose the LinkedHashMap datastructure due its O(1) time complexity retrieval and adding operations so performance is maximized with this datastructure. Its memory consumption is larger than the similar TreeMap datastructure which occupies less memory but is slower having an O(log n) time complexity of same operations. TreeMap is based on an underlying BST imnplementation. If memory becomes an issue switching to TreeMap may be benficial.

Currently the full datasets for jobmetadata and  JobExecution Log are in memory. Job metadata has to be fully in memory due to the interdependency between nodes. Job_execution_log is does not have this interdependency between each element. If out of memory becomes a problem we can choose to persist the nodes in a database that provides structured records. Primary key would be a combination of job name and date. There will be a degredation in performance as in memory speed cannot be matched.

On a different note a Spring Batch implementation would be well suited for this problem https://docs.spring.io/spring-batch/3.0.x/reference/html/spring-batch-intro.html for large date-sets and concurrent processing.


