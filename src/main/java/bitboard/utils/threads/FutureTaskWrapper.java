package bitboard.utils.threads;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public abstract class FutureTaskWrapper<T> extends FutureTask<T> {

    public FutureTaskWrapper(Callable<T> c) {
        super(c);
    }

    abstract int getTaskId();
}
