package aunmag.shooter.core.utilities;

import java.util.ArrayList;
import java.util.List;

public class OperativeManager<T extends Operative> {

    public final List<T> all = new ArrayList<>();

    /**
     * Note that if an object has became inactive after updating, it will only be removed
     * during the next iteration to render the object once before it is removed.
     */
    public void update() {
        for (var i = all.size() - 1; i >= 0; i--) {
            var operative = all.get(i);

            if (operative.isActive()) {
                operative.update();
            } else {
                operative.remove();
                all.remove(i);
            }
        }
    }

    public void render() {
        for (var operative : all) {
            operative.render();
        }
    }

    public void remove() {
        for (var operative : all) {
            operative.remove();
        }

        all.clear();
    }

}
