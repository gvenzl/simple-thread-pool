#SimpleThreadPool

#Overview
A simple Java thread pool which is dynamically resizable during runtime.

#Usage
A thread pool which executes a simple task in multiple threads. The pool allows to be dynamically resized during runtime. This is particular convenient when one would like to run a simple self-contained task within multiple threads and dynamically scale them up or down (usually very useful for load benchmarking).

#Examples
Following a simple example of how to use SimpleThreadPool.

## Creating a thread pool
This example creates a thread pool with a `poolSize` of 10 threads and a `maxPoolSize` of 100 threads. MyRunnable is the user defined class that implements the Runnable interface:

    Executor threadPoolExecutor = new Executor(10, 100, MyRunnable.class);

## Starting the thread pool
All you need to do is to invoke the `run` method:

    threadPoolExecutor.run();

## Resizing the thread pool
If you want to scale the thread pool **up** you just have to increase new pool size. However, this only works if your new pool size does't exceed the `maxPoolSize`. If the thread pool is already running the pool will increased dynamically:

    threadPoolExecutor.setPoolSize(20);

If you want to scale the pool size **down** you just have to decrease the pool size. If the thread pool is already running the pool will be decreased dynamically:

    threadPoolExecutor.setPoolSize(5);

## Stopping the thread pool
To stop the thread pool all you need is to invoke the `shutdown` method. That will interrupt all thread within the thread pool and wait for them to terminate:

    threadPoolExecutor.shutdown();