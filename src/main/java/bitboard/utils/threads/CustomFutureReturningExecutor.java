package bitboard.utils.threads;

import java.util.concurrent.*;

public class CustomFutureReturningExecutor<H> extends ThreadPoolExecutor {
    public CustomFutureReturningExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    @Override
    protected <T> RunnableFuture<T> newTaskFor(Callable<T> callable) {
        if (callable instanceof IdentifiableCallable) {
            return ((IdentifiableCallable<T>) callable).newTask();
        }
        return super.newTaskFor(callable);
    }
}
