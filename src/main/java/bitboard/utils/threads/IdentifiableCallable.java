package bitboard.utils.threads;

import java.util.concurrent.Callable;
import java.util.concurrent.RunnableFuture;

public interface IdentifiableCallable<T> extends Callable<T> {
    int getId();
    void cancelTask();
    RunnableFuture<T> newTask();
}
