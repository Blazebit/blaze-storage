package com.blazebit.storage.nfs.rar;

import javax.resource.spi.work.Work;
import javax.resource.spi.work.WorkException;
import javax.resource.spi.work.WorkManager;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.ReentrantLock;

public class WorkManagerExecutorService implements ExecutorService {

    private final WorkManager workManager;
    private volatile boolean shutdown;

    public WorkManagerExecutorService(WorkManager workManager) {
        this.workManager = workManager;
    }

    @Override
    public void shutdown() {
        shutdown = true;
    }

    @Override
    public List<Runnable> shutdownNow() {
        shutdown = true;
        return Collections.emptyList();
    }

    @Override
    public boolean isShutdown() {
        return shutdown;
    }

    @Override
    public boolean isTerminated() {
        return shutdown;
    }

    @Override
    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        return shutdown;
    }

    @Override
    public <T> Future<T> submit(Callable<T> task) {
        if (shutdown) {
            throw new RejectedExecutionException("Already closed");
        }
        return schedule(new CallableWorkFuture<>(task));
    }

    @Override
    public <T> Future<T> submit(Runnable task, T result) {
        if (shutdown) {
            throw new RejectedExecutionException("Already closed");
        }
        return schedule(new CallableWorkFuture<>(task, result));
    }

    @Override
    public Future<?> submit(Runnable task) {
        if (shutdown) {
            throw new RejectedExecutionException("Already closed");
        }
        return schedule(new CallableWorkFuture<>(task, null));
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException {
        if (shutdown) {
            throw new RejectedExecutionException("Already closed");
        }
        List<Future<T>> futures = new ArrayList<>(tasks.size());
        for (Callable<T> task : tasks) {
            futures.add(schedule(new CallableWorkFuture<>(task)));
        }
        return futures;
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException {
        if (shutdown) {
            throw new RejectedExecutionException("Already closed");
        }
        List<Future<T>> futures = new ArrayList<>(tasks.size());
        for (Callable<T> task : tasks) {
            futures.add(schedule(new CallableWorkFuture<>(task)));
        }
        return futures;
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
        List<Future<T>> futures = invokeAll(tasks);
        ExecutionException lastEx = null;
        for (Future<T> future : futures) {
            try {
                return future.get();
            } catch (ExecutionException ex) {
                lastEx = ex;
            }
        }

        if (lastEx == null) {
            return null;
        } else {
            throw lastEx;
        }
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        List<Future<T>> futures = invokeAll(tasks, timeout, unit);
        ExecutionException lastEx = null;
        for (Future<T> future : futures) {
            try {
                return future.get(timeout, unit);
            } catch (ExecutionException ex) {
                lastEx = ex;
            }
        }

        if (lastEx == null) {
            return null;
        } else {
            throw lastEx;
        }
    }

    @Override
    public void execute(Runnable command) {
        if (shutdown) {
            throw new RejectedExecutionException("Already closed");
        }
        try {
            workManager.scheduleWork(new RunnableWork(command));
        } catch (WorkException ex) {
            throw new RejectedExecutionException(ex);
        }
    }

    private <T> Future<T> schedule(CallableWorkFuture<T> callableWork) {
        try {
            workManager.scheduleWork(callableWork);
        } catch (WorkException ex) {
            throw new RejectedExecutionException(ex);
        }
        return callableWork;
    }

    private static class RunnableWork implements Work {

        private final Runnable runnable;

        public RunnableWork(Runnable runnable) {
            this.runnable = runnable;
        }

        @Override
        public void release() {
        }

        @Override
        public void run() {
            runnable.run();
        }
    }

    private static class CallableRunnable<T> implements Callable<T> {
        private final Runnable runnable;
        private final T result;

        public CallableRunnable(Runnable runnable, T result) {
            this.runnable = runnable;
            this.result = result;
        }

        @Override
        public T call() throws Exception {
            runnable.run();
            return result;
        }
    }

    private static class CallableWorkFuture<T> implements Work, Future<T> {

        private final Callable<T> callable;
        private final ReentrantLock lock = new ReentrantLock();
        private volatile boolean done;
        private T result;
        private Throwable throwable;

        public CallableWorkFuture(Runnable runnable, T result) {
            this.callable = new CallableRunnable<>(runnable, result);
        }

        public CallableWorkFuture(Callable<T> callable) {
            this.callable = callable;
        }

        @Override
        public void release() {
        }

        @Override
        public void run() {
            lock.lock();
            try {
                result = callable.call();
            } catch (Throwable t) {
                this.throwable = t;
            } finally {
                done = true;
                lock.unlock();
            }
        }

        @Override
        public boolean cancel(boolean mayInterruptIfRunning) {
            return false;
        }

        @Override
        public boolean isCancelled() {
            return false;
        }

        @Override
        public boolean isDone() {
            return done;
        }

        @Override
        public T get() throws InterruptedException, ExecutionException {
            if (done) {
                return get0();
            }
            lock.lockInterruptibly();
            lock.unlock();
            return get0();
        }

        @Override
        public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
            if (done) {
                return get0();
            }
            if (lock.tryLock(timeout, unit)) {
                lock.unlock();
                return get0();
            }
            if (Thread.currentThread().isInterrupted()) {
                throw new InterruptedException();
            } else {
                throw new TimeoutException();
            }
        }

        private T get0() throws ExecutionException {
            if (throwable == null) {
                return result;
            } else {
                throw new ExecutionException(throwable);
            }
        }
    }
}
