package com.gvenzl.simplethreadpool;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestExecutor
{

	Executor exec;
	
	public TestExecutor() {

	}
	
	@Before
	public void setup() {
		exec = new Executor();
	}
	
	@Test
	public void test_instantiation() {
		new Executor();
		new Executor(new Thread());
		new Executor(1, new Thread());
		new Executor(1, 1, new Thread());
	}
	
	@Test
	public void test_setPoolSize() {
		int poolSize = 10;
		exec.setMaxPoolSize(10);
		exec.setPoolSize(poolSize);
		Assert.assertTrue(poolSize == exec.getPoolSize());
	}
	
	public void test_getPoolSize() {
		
	}
}
