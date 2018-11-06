/*
 * Since: December, 2014
 * Author: gvenzl
 * Name: TestExecutor.java
 * Description:
 *
 * Copyright (c) 2018 Gerald Venzl
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.gvenzl.simplethreadpool;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestExecutor
{
	private Executor exec;

	/**
	 * Dummy class to run JUnit tests on shutdown/run
	 * @author gvenzl
	 *
	 */
	static public class TestClass implements Runnable {
		@Override
		public void run() {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) { }
		}
		
	}
	
	@Before
	public void setup() {
		exec = new Executor(Thread.class);
	}
	
	@Test
	public void test_instantiation() {
		new Executor(Thread.class);
		new Executor(1, Thread.class);
		new Executor(1, 1, Thread.class);
	}
	
	@Test
	public void test_setMaxPoolSize() {
		exec.setMaxPoolSize(10);
	}
	
	@Test
	public void test_getMaxPoolSize () {
		int maxSize = 10;
		exec.setMaxPoolSize(maxSize);
		Assert.assertEquals(maxSize, exec.getMaxPoolSize());
	}
	
	@Test
	public void test_getPoolSize() {
		int poolSize = 10;
		exec.setMaxPoolSize(poolSize);
		exec.setPoolSize(poolSize);
		Assert.assertEquals(poolSize, exec.getPoolSize());
	}
	
	@Test
	public void test_setPoolSize() {
	    int poolSize = 1;
		exec.setMaxPoolSize(poolSize);
		exec.setPoolSize(poolSize);
		Assert.assertEquals(poolSize, exec.getPoolSize());
	}

	@Test
	public void test_start() throws Exception {
		exec.run();
	}


	@Test public void test_isRunningTrue() throws Exception {
		Executor execTemp = new Executor(TestClass.class);
		execTemp.run();
		Assert.assertTrue(execTemp.isRunning());
	}
	@Test
	public void test_isRunningFalse() {
		Assert.assertFalse(exec.isRunning());
	}
	
	@Test
	public void test_isTerminatedFalse() {
		Assert.assertFalse(exec.isTerminated());
	}
	
	@Test
	public void test_isTerminatedTrue() throws Exception {
		exec.submit(TestClass.class);
		exec.run();
		exec.shutdown();
		
		Assert.assertTrue(exec.isTerminated());
	}

	@Test
	public void test_shutdownNotRunning() {
		exec.shutdown();
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void negativeTest_setPoolSizeTooHigh() {
		exec.setMaxPoolSize(1);
		exec.setPoolSize(2);
	}
	
	@Test(expected=IllegalStateException.class)
	public void negativeTest_runNoSubmission() throws Exception {
		exec.submit(null);
		exec.run();
	}
	
	@Test(expected=InstantiationException.class)
	public void negativeTest_runNoDefaultConstructor() throws Exception {
		class Test implements Runnable {
			public void run() {
			}
		}
		
		exec.submit(Test.class);
		exec.run();
	}
	
	@Test
	public void test_runIncreasePoolSize() throws Exception {
		int initPoolSize = 1;
		int maxPoolSize = 10;
		
		exec.setMaxPoolSize(maxPoolSize);
		exec.setPoolSize(initPoolSize);
		exec.submit(TestClass.class);
		exec.run();
		Assert.assertEquals(initPoolSize, exec.getPoolSize());
		exec.setPoolSize(maxPoolSize);
		Assert.assertEquals(maxPoolSize, exec.getPoolSize());
	}

	@Test
	public void test_runDecreasePoolSize() throws Exception {
		int lowLevelPoolSize = 1;
		int maxPoolSize = 10;
		
		exec.setMaxPoolSize(maxPoolSize);
		exec.setPoolSize(maxPoolSize);
		exec.submit(TestClass.class);
		exec.run();
		Assert.assertEquals(maxPoolSize, exec.getPoolSize());
		exec.setPoolSize(lowLevelPoolSize);
		Assert.assertEquals(lowLevelPoolSize, exec.getPoolSize());
	}
}
