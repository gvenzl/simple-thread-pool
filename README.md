#SimpleThreadPool

#Overview
A simple Java thread pool which is dynamically resizable during runtime.

#Usage
A thread pool which executes a simple task in multiple threads.
The pool allows to be dynamically resized during runtime.
This is particular convenient when one would for example like to run
a simple self-contained task in multiple threads and dynamically scale those threads up or down
(usually very useful for load benchmarking).
The pool is kept very simple, i.e. it doesn't provide things like min/max size, idle timeout, etc.
You just define how many threads you would like to run and which Runnable that should be executed.
It is up to the runnable to for example implement a endless loop if you would like to re-execute
the task until the user stops the workload.
The thread pool provides mechanism to stop the thread pool but it is for the Runnable to act on the stop signal.