error id: GgeyddHPwOKo4vuDnSofPw==
### Bloop error:

Unexpected error when copying <WORKSPACE>/.scala-build/.bloop/voxel_dfcb79a7c1/bloop-internal-classes/main-hy1YHcwuTWm1YhqE14YiSg==/Window.class to <WORKSPACE>/.scala-build/voxel_dfcb79a7c1/classes/main/Window.class, you might need to restart the build server.
java.nio.file.NoSuchFileException: <WORKSPACE>/.scala-build/.bloop/voxel_dfcb79a7c1/bloop-internal-classes/main-hy1YHcwuTWm1YhqE14YiSg==/Window.class
	at java.base/sun.nio.fs.UnixException.translateToIOException(UnixException.java:92)
	at java.base/sun.nio.fs.UnixException.rethrowAsIOException(UnixException.java:108)
	at java.base/sun.nio.fs.UnixException.rethrowAsIOException(UnixException.java:113)
	at java.base/sun.nio.fs.UnixFileSystem.copy(UnixFileSystem.java:946)
	at java.base/sun.nio.fs.UnixFileSystemProvider.copy(UnixFileSystemProvider.java:281)
	at java.base/java.nio.file.Files.copy(Files.java:1196)
	at bloop.io.ParallelOps$.copy$1(ParallelOps.scala:222)
	at bloop.io.ParallelOps$.$anonfun$copyDirectories$10(ParallelOps.scala:267)
	at scala.runtime.java8.JFunction0$mcV$sp.apply(JFunction0$mcV$sp.java:23)
	at monix.eval.internal.TaskRunLoop$.startFuture(TaskRunLoop.scala:494)
	at monix.eval.Task.runToFutureOpt(Task.scala:586)
	at monix.eval.internal.TaskDeprecated$Extensions.runSyncMaybeOptPrv(TaskDeprecated.scala:128)
	at monix.eval.internal.TaskDeprecated$Extensions.$anonfun$coeval$1(TaskDeprecated.scala:303)
	at monix.eval.Coeval$Always.apply(Coeval.scala:1451)
	at monix.eval.Coeval.value(Coeval.scala:258)
	at bloop.io.ParallelOps$.$anonfun$copyDirectories$9(ParallelOps.scala:288)
	at monix.reactive.internal.consumers.ForeachAsyncConsumer$$anon$1.onNext(ForeachAsyncConsumer.scala:44)
	at monix.reactive.internal.consumers.LoadBalanceConsumer$$anon$1.$anonfun$signalNext$1(LoadBalanceConsumer.scala:218)
	at monix.execution.internal.InterceptRunnable.run(InterceptRunnable.scala:27)
	at java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1090)
	at java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:614)
	at java.base/java.lang.Thread.run(Thread.java:1516)
#### Short summary: 

Unexpected error when copying <WORKSPACE>/.scala-build/.bloop/voxel_dfcb79a7c1/bloop-internal-classe...