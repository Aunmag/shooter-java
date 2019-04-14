package aunmag.shooter.core.utilities;

import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class Lazy<T> {

    private final Supplier<T> supplier;
    @Nullable
    private T value = null;

    public Lazy(Supplier<T> supplier) {
        this.supplier = supplier;
    }

    public void recompute() {
        value = supplier.get();
    }

    public T get() {
        if (value == null) {
            recompute();
        }

        return value;
    }

}
