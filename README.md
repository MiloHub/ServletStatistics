# ServletStatistics

Sample maven style java project for getting statistics for HTTP GET call.

provided with fixed delay and fixed pool concept to find mean , SD , percentile .

Uses httpclient apache jar, junt 4 , log4j .

navigate to test resource to find example.

#########
Project intented for to

Make 100 HTTP GET requests and print

the following statistics for the response time to stdout: 

• 10th, 50th, 90th, 95th, 99th Percentile 

• Mean 

• Standard Deviation


Uses multi threaded parallel processing with N request at a give time. 

Run program

Project build using Maven style. 

maven install to clean and compile.

Use maven test to run the program. 



Program has three types of stats collection 

1. Bucketed fixed pool statistics  
2. Bucketed fixed delay statistics
3. ThreadPool fixed size statistics
