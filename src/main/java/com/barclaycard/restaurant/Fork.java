package com.barclaycard.restaurant;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/***
 * Class for Fork
 * @author Chandra
 *
 */
public class Fork {
	private static final Logger LOGGER=LoggerFactory.getLogger(Fork.class);
    //TO Make sure only one diner can have me at any time.
    Lock lock = new ReentrantLock(true);
    private final int id;
    private static final int TIME_TO_TRY_LOCK=10;
    
    public Fork(int id) {
      this.id = id;
    }
    /***
     * Method to pickup fork
     * @param diner
     * @param name
     * @return
     * @throws InterruptedException
     */
    public boolean pickUp(Diner diner,final String  name) throws InterruptedException {
      if (lock.tryLock(TIME_TO_TRY_LOCK, TimeUnit.MILLISECONDS)) {
    	  LOGGER.info(diner + " picks up " + name + " fork");
        return true;
      }
      return false;
    }
    /***
     * method to put down the fork
     * @param diner
     * @param name
     */
    public void putDown(Diner diner, String name) {
      lock.unlock();
      LOGGER.info(diner + " puts down " + name +" fork");
    }

    @Override
    public String toString() {
      return "fork-" + id;
    }
  }