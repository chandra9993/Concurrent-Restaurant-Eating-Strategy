package com.barclaycard.restaurant;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/***
 * Class to solve the concurrent restaurant problem
 * @author Chandra
 *
 */
public class ConcurrentRestaurant {
	private static final int NO_OF_DINER = 5;
	private static final Logger LOGGER=LoggerFactory.getLogger(ConcurrentRestaurant.class);
	/***
	 * Main method to create a thread pool and execute them
	 * @param args
	 */
	public static void main(String args[]) {
		LOGGER.info("Program starting...");
		ExecutorService executorService = null;
		Diner[] diners = null;
		try {
			diners = new Diner[NO_OF_DINER];
			Fork[] forks = new Fork[NO_OF_DINER];
			for (int i = 0; i < NO_OF_DINER; i++) {
				forks[i] = new Fork(i);
			}
			executorService = Executors.newFixedThreadPool(NO_OF_DINER);
			for (int i = 0; i < NO_OF_DINER; i++) {
				diners[i] = new Diner(i, forks[i], forks[(i + 1)
						% NO_OF_DINER]);
				executorService.execute(diners[i]);
			}
		}catch(Exception exception){
			LOGGER.error("Exception while running the Concurrant program",exception);
		}
		finally {
			// Close everything down.
			executorService.shutdown();
			// Wait for all thread to finish
			while (!executorService.isTerminated()) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					LOGGER.error("Exception in finally block  ",e);
				}
			}
		}
		LOGGER.info("program ended.");
	}
}