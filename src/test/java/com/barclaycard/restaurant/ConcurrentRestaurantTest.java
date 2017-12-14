package com.barclaycard.restaurant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Assert;
import org.junit.Test;
 
public class ConcurrentRestaurantTest {
	
	  @Test
	    public void threadCount01() throws InterruptedException, ExecutionException {
	        test(1);
	    }
	  
	  @Test
	    public void threadCount02() throws InterruptedException, ExecutionException {
	        test(2);
	    }
	  
	  @Test
	    public void threadCount03() throws InterruptedException, ExecutionException {
	        test(3);
	    }
	  
	  @Test
	    public void threadCount04() throws InterruptedException, ExecutionException {
	        test(4);
	    }
	  
	  @Test
	    public void threadCount05() throws InterruptedException, ExecutionException {
	        test(5);
	    }
  
	 /**
     * Generates sequential unique IDs starting with 1, 2, 3, and so on.
     * <p>
     * This class is thread-safe.
     * </p>
     */
    static class UniqueIdGenerator {
        private final AtomicInteger counter = new AtomicInteger();
 
        public int nextId() {
            return counter.incrementAndGet();
        }
    }
 
    private void test(final int threadCount) throws InterruptedException, ExecutionException {
    	final UniqueIdGenerator domainObject = new UniqueIdGenerator();   
    	Callable<Diner> task = new Callable<Diner>() {
               @Override
               public Diner call() {
                   return new Diner(domainObject.nextId(),new Fork(domainObject.nextId()),
                		   new Fork((domainObject.nextId()+1)%5));
               }
           };
        List<Callable<Diner>> tasks = Collections.nCopies(threadCount, task);
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        List<Future<Diner>> futures = executorService.invokeAll(tasks);
        List<Diner> resultList = new ArrayList<>(futures.size());
        // Check for exceptions
        for (Future<Diner> future : futures) {
            // Throws an exception if an exception was thrown by the task.
            resultList.add(future.get());
        }
        // Validate the IDs
        Assert.assertEquals(threadCount, futures.size());
        List<Diner> expectedList = new ArrayList<>(threadCount);
        for (int i = 0; i < threadCount; i++) {
            expectedList.add(new Diner(i,new Fork(i),new Fork((i+1)%5)));
          
        }
        Assert.assertEquals(expectedList.size(), resultList.size());
    }
 
}