package com.gvenzl.simplethreadpool;

import java.util.LinkedList;

public class Executor
{
	private LinkedList<Thread> pool;

	private int threadPoolSize = Integer.MAX_VALUE;
	private int maxThreadPoolSize;
	private boolean terminated = false;
	private boolean running = false;
	
	private Class<? extends Runnable> submission;
	
	public Executor(Class<? extends Runnable> task) {
		pool = new LinkedList<Thread>();
		submission = task;
	}
	
	public Executor(int poolSize, Class<? extends Runnable> task) {
		threadPoolSize = poolSize;
		pool = new LinkedList<Thread>();
		submission = task;
	}
	
	public Executor(int poolSize, int maxPoolSize, Class<? extends Runnable> task) {
		threadPoolSize = poolSize;
		maxThreadPoolSize = maxPoolSize;
		pool = new LinkedList<Thread>();
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
				for (int i=threadPoolSize; i<size; i++) {
					Thread t;
					try{
						t = new Thread(submission.newInstance());
						t.start();
						pool.add(t);
					}
					catch (InstantiationException | IllegalAccessException e) {
						shutdown();
						e.printStackTrace();
					}
				}
			}
			else if (size < threadPoolSize) {
				LinkedList<Thread> kill = new LinkedList<Thread>();
				// Interrupt all the threads at once until the threadPoolSize is equal to the new size
				for (int i=threadPoolSize; i>size; i--) {
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
	public void submit(Class<? extends Runnable> task) {
		submission = task;
	}

	/**
	 * Runs the tasks within the pool
	 * @throws IllegalStateException One of the following: <br/> <li>submission is null</li>
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	public void run() throws IllegalStateException, InstantiationException, IllegalAccessException {
		if (!running) {
			if (null == submission) {
				throw new IllegalStateException("No submission passed");
			}
	
			for (int i=pool.size(); i< threadPoolSize &&
					      i<= maxThreadPoolSize; i++) {
				Thread t;
				try {
					t = new Thread(submission.newInstance());
					t.start();
					pool.add(t);
				}
				catch(InstantiationException | IllegalAccessException e) {
					shutdown();
					e.printStackTrace();
				}
			}
			
			running = true;
			terminated = false;
		}
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
