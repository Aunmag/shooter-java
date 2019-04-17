package aunmag.shooter.core.utilities;

import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class Lazy<T> {

    private final Supplier<T> supplier;
    @Nullable
    private T value = null;
    private boolean isRecomputing = false;

    public Lazy(Supplier<T> supplier) {
        this.supplier = supplier;
    }

    public void recompute() {
        if (isRecomputing) {
            throw new RuntimeException("Recursive recomputing");
        } else {
            isRecomputing = true;
            value = supplier.get();
            isRecomputing = false;
        }
    }

    public T get() {
        if (value == null) {
            recompute();
        }

        return value;
    }

    @Nullable
    public T getRaw() {
        return value;
    }

}
