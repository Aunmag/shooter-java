package aunmag.shooter.core.utilities;

import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

public abstract class ResourceManager<T extends Operative> {

    private final HashMap<String, T> all = new HashMap<>();

    @Nullable
    public T provide(String name) {
        return all.computeIfAbsent(name, n -> {
            var path = toPath(n);
            var resource = (T) null;

            try {
                resource = load(path);
            } catch (Exception e) {
                UtilsFile.printReadError(path);
            }

            return resource;
        });
    }

    public abstract T load(String path) throws Exception;

    public String toPath(String name) {
        return "/" + name;
    }

    public void clear() {
        for (var resource : all.values()) {
            resource.remove();
        }

        all.clear();
    }

}
