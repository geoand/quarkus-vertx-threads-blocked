# Purpose

Reproduce an issue with Vert.x threads being blocked

# How

## Build and start application

```
./mvnw clean package
java -jar target/blocked-vertx-1.0-SNAPSHOT-runner.jar
```

## Load test the application

I used [wrk](https://github.com/wg/wrk) using these settings (on a quad-core, 8 thread machine ):

```
wrk -t4 -c400 -d10s http://127.0.0.1:8080/movie
```

The observed behavior was that after around 60 seconds I bunch of exceptions started showing up in the console and the application could no longer serve requests. An example stacktrace looked like:

```
2019-09-24 12:53:59,545 WARNING [io.ver.cor.imp.BlockedThreadChecker] (vertx-blocked-thread-checker) Thread Thread[vert.x-worker-thread-9,5,main]=Thread[vert.x-worker-thread-9,5,main] has been blocked for 65250 ms, time limit is 60000 ms: io.vertx.core.VertxException: Thread blocked
	at java.lang.Object.wait(Native Method)
	at java.lang.Object.wait(Object.java:502)
	at io.quarkus.resteasy.runtime.standalone.VertxBlockingOutput.awaitWriteable(VertxBlockingOutput.java:96)
	at io.quarkus.resteasy.runtime.standalone.VertxBlockingOutput.write(VertxBlockingOutput.java:63)
	at io.quarkus.resteasy.runtime.standalone.VertxHttpResponse.writeBlocking(VertxHttpResponse.java:179)
	at io.quarkus.resteasy.runtime.standalone.VertxOutputStream.close(VertxOutputStream.java:118)
	at org.jboss.resteasy.util.CommitHeaderOutputStream.close(CommitHeaderOutputStream.java:87)
	at io.quarkus.resteasy.runtime.standalone.VertxHttpResponse.finish(VertxHttpResponse.java:136)
	at io.quarkus.resteasy.runtime.standalone.VertxRequestHandler.dispatch(VertxRequestHandler.java:109)
	at io.quarkus.resteasy.runtime.standalone.VertxRequestHandler.dispatchRequestContext(VertxRequestHandler.java:64)
	at io.quarkus.resteasy.runtime.standalone.VertxRequestHandler.lambda$handle$0(VertxRequestHandler.java:55)
	at io.quarkus.resteasy.runtime.standalone.VertxRequestHandler$$Lambda$194/55174876.handle(Unknown Source)
	at io.vertx.core.impl.ContextImpl.lambda$executeBlocking$2(ContextImpl.java:316)
	at io.vertx.core.impl.ContextImpl$$Lambda$191/332933750.run(Unknown Source)
	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1149)
	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:624)
	at io.netty.util.concurrent.FastThreadLocalRunnable.run(FastThreadLocalRunnable.java:30)
	at java.lang.Thread.run(Thread.java:748)

2019-09-24 12:53:59,546 WARNING [io.ver.cor.imp.BlockedThreadChecker] (vertx-blocked-thread-checker) Thread Thread[vert.x-worker-thread-0,5,main]=Thread[vert.x-worker-thread-0,5,main] has been blocked for 65250 ms, time limit is 60000 ms: io.vertx.core.VertxException: Thread blocked
	at java.lang.Object.wait(Native Method)
	at java.lang.Object.wait(Object.java:502)
	at io.quarkus.resteasy.runtime.standalone.VertxBlockingOutput.awaitWriteable(VertxBlockingOutput.java:96)
	at io.quarkus.resteasy.runtime.standalone.VertxBlockingOutput.write(VertxBlockingOutput.java:63)
	at io.quarkus.resteasy.runtime.standalone.VertxHttpResponse.writeBlocking(VertxHttpResponse.java:179)
	at io.quarkus.resteasy.runtime.standalone.VertxOutputStream.close(VertxOutputStream.java:118)
	at org.jboss.resteasy.util.CommitHeaderOutputStream.close(CommitHeaderOutputStream.java:87)
	at io.quarkus.resteasy.runtime.standalone.VertxHttpResponse.finish(VertxHttpResponse.java:136)
	at io.quarkus.resteasy.runtime.standalone.VertxRequestHandler.dispatch(VertxRequestHandler.java:109)
	at io.quarkus.resteasy.runtime.standalone.VertxRequestHandler.dispatchRequestContext(VertxRequestHandler.java:64)
	at io.quarkus.resteasy.runtime.standalone.VertxRequestHandler.lambda$handle$0(VertxRequestHandler.java:55)
	at io.quarkus.resteasy.runtime.standalone.VertxRequestHandler$$Lambda$194/55174876.handle(Unknown Source)
	at io.vertx.core.impl.ContextImpl.lambda$executeBlocking$2(ContextImpl.java:316)
	at io.vertx.core.impl.ContextImpl$$Lambda$191/332933750.run(Unknown Source)
	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1149)
	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:624)
	at io.netty.util.concurrent.FastThreadLocalRunnable.run(FastThreadLocalRunnable.java:30)
	at java.lang.Thread.run(Thread.java:748)
```

