package com.gvenzl.simplethreadpool;

import java.util.LinkedList;

public class Executor
{
	private LinkedList<Thread> pool;

	private int threadPoolSize = Integer.MAX_VALUE;
	private int maxThreadPoolSize;
	private boolean terminated = false;
	private boolean running = false;
	
	private Runnable submission;
	
	public Executor() {
		super();
	}
	
	public Executor(Runnable task) {
		submission = task;
	}
	
	public Executor(int poolSize, Runnable task) {
		threadPoolSize = poolSize;
		submission = task;
	}
	
	public Executor(int poolSize, int maxPoolSize, Runnable task) {
		threadPoolSize = poolSize;
		maxThreadPoolSize = maxPoolSize;
		submission = task;
	}
	
	/**
	 * Sets the thread pool size
	 * @param size The size of the thread pool
	 * @throws IllegalArgumentException An IllegalArgumentExcpetion is thrown
	 * if the pool size is higher than the max pool size.
	 */
	public void setPoolSize(int size) throws IllegalArgumentException {
		if (size > maxThreadPoolSize) {
			throw new IllegalArgumentException("Thread pool size higher than max thread pool size");
		}
	
		// Only if the pool is currently running resize it
		if(running) {
			if (size > threadPoolSize) {
				for (int i=threadPoolSize; i<=size; i++) {
					Thread t = new Thread(submission);
					t.start();
					pool.add(t);
				}
			}
			else if (size < threadPoolSize) {
				LinkedList<Thread> kill = new LinkedList<Thread>();
				// Interrupt all the threads at once until the threadPoolSize is equal to the new size
				for (int i=threadPoolSize; i==size; i--) {
					Thread t = pool.remove();
					t.interrupt();
					kill.add(t);
				}
				
				// Kill the threads and wait for them to join
				for (Thread t : kill) {
					try {
						t.join();
					}
					catch (InterruptedException e) {
						// Interrupted, break loop
						break;
					}
				}
			}
		}
		
		threadPoolSize = size;
	}
	
	/**
	 * Returns the thread pool size
	 * @return The thread pool size
	 */
	public int getPoolSize() {
		return threadPoolSize;
	}
	
	/**
	 * Sets the max thread pool size.
	 * Regardless of what the thread pool size is set to the pool will never
	 * exceed the max thread pool size.
	 * @param maxSize The maximum number of threads within the pool
	 */
	public void setMaxPoolSize(int maxSize) {
		maxThreadPoolSize = maxSize;
	}
	
	/**
	 * Returns the maximum thread pool size.
	 * @return The maximum thread pool size.
	 */
	public int getMaxPoolSize() {
		return maxThreadPoolSize;
	}
	

	/**
	 * Submits a task to the thread pool.
	 * All the threads will work on a separate copy of the task.
	 * Thread safety is not guaranteed.
	 * @param task The task to work on.
	 */
	public void submit(Runnable task) {
		submission = task;
	}

	/**
	 * Runs the tasks within the pool
	 * @throws IllegalStateException One of the following: <br/> <li>submission is null</li>
	 */
	public void run() throws IllegalStateException {
		if (null == submission) {
			throw new IllegalStateException("No submission passed");
		}
		
		pool = new LinkedList<Thread>();
		for (int i=1; i<=threadPoolSize &&
				      i<=maxThreadPoolSize; i++) {
			Thread t = new Thread(submission);
			t.start();
			pool.add(t);
		}
		
		running = true;
		terminated = false;
	}
	
	/**
	 * Gracefully shuts down the thread pool
	 */
	public void shutdown() {
		while(!pool.isEmpty()) {
			Thread t = pool.remove();
			t.interrupt();
		}
		terminated = true;
		running = false;
	}
	
	/**
	 * Checks whether a thread pool is terminated or not.
	 * @return
	 */
	public boolean isTerminated() {
		return terminated;
	}
	
	/**
	 * Determines whether the thread pool is currently running or not
	 * @return
	 */
	public boolean isRunning() {
		return running;
	}
}
