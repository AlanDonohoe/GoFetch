package com.gofetch.utils;

/**
 * Utility class to measure the speed in MSecs of algorithm under observation
 * Timer starts in the constructor and total time is returned by method: getTime()
 * @author alandonohoe
 * ref: http://www.vogella.com/articles/JavaPerformance/article.html
 * 
 */
public class PerformanceUtils {

	private long startTime, stopTime;
	private static final long MEGABYTE = 1024L * 1024L;


	public PerformanceUtils(){

		startTime = System.currentTimeMillis();
	}

	public long getTime(){

		stopTime = System.currentTimeMillis();
		return(stopTime - startTime);
	}

	public long bytesToMegabytes(long bytes) {
		return bytes / MEGABYTE;
	}
	
	public String getAllMemoryDetails(){
		
		String memDetails;
		long usedMemory, totalMemory,freeMemory;
		
		Runtime runtime = Runtime.getRuntime();
		runtime.gc(); // Run the garbage collector
		totalMemory = runtime.totalMemory();
		freeMemory = runtime.freeMemory();
		usedMemory = totalMemory - freeMemory;
		memDetails = ("usedMemory: " + String.valueOf(usedMemory) + " totalMemory: " + String.valueOf(totalMemory) + " freeMemory: " + String.valueOf(freeMemory) + " in bytes");
		return(memDetails);
		
	}

	public String usedMemory(){

		long memory;
		
		Runtime runtime = Runtime.getRuntime();
		runtime.gc(); // Run the garbage collector
		memory = runtime.totalMemory() - runtime.freeMemory(); // Calculate the used memory
		return(String.valueOf(memory));
	}
	
	public String freeMemory(){
	
		Runtime runtime = Runtime.getRuntime();
		runtime.gc(); // Run the garbage collector
		return(String.valueOf(runtime.freeMemory())); // Calculate the used memory

	}
	
	public String totalMemory(){
		
		Runtime runtime = Runtime.getRuntime();
		runtime.gc(); // Run the garbage collector
		return(String.valueOf(runtime.totalMemory())); // Calculate the used memory

	}
	
}
