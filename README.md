#SimpleThreadPool

#Overview
A simple Java thread pool which is dynamically resizable during runtime.

#Usage
A thread pool which executes a simple task in multiple threads. The pool allows to be dynamically resized during runtime. This is particular convenient when one would like to run a simple self-contained task in multiple threads and dynamically scale them or down (usually very useful for load benchmarking).