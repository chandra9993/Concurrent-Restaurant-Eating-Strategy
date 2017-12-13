package com.barclaycard.restaurant;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/***
 * Dinner thread class
 * @author Chandra
 *
 */
public class Diner implements Runnable {
    // Id for each diner
    private final int id;
    // The forks on either side of diner.
    private final Fork leftFork;
    private final Fork rightFork;
    private static final String LEFT="left";
    private static final String RIGHT="right";
    private static final int TIME_UNIT=100;
    // To randomize eat/Think time
    private Random randomGenerator = new Random();
    private int eatTime=0;
    private static final Logger LOGGER=LoggerFactory.getLogger(Diner.class);
    /***
     * Diner Constructor 
     * @param id
     * @param leftFork
     * @param rightFork
     */
    public Diner(int id, Fork leftFork, Fork rightFork) {
      this.id = id;
      this.leftFork = leftFork;
      this.rightFork = rightFork;
    }

    @Override
    public void run() {

      try {
        while (true) {
            // Make the mechanism obvious.
          if (leftFork.pickUp(this, LEFT)) {
            if (rightFork.pickUp(this, RIGHT)) {
              // Eat some.
              eat();
              // Finished.
              //Pausing to digest
              digest();
              rightFork.putDown(this, RIGHT);
            }
            // Finished.
            leftFork.putDown(this,LEFT);
          }
        }
      } catch (Exception e) {
    	  LOGGER.error("Exception in run method for {}",this,e);
      }
    }
    /***
     * Method to digest
     * @throws InterruptedException
     */
    private void digest() throws InterruptedException {
      LOGGER.info(this+" ate for "+eatTime+"ms. Pausing to digest..");
      Thread.sleep(randomGenerator.nextInt(TIME_UNIT));
    }
    /***
     * Method to eat
     * @throws InterruptedException
     */
    private void eat() throws InterruptedException {
      LOGGER.info(this + " is eating");
      eatTime=randomGenerator.nextInt(TIME_UNIT);
      Thread.sleep(eatTime);
    }

    @Override
    public String toString() {
      return "Diner" + id;
    }
  }
