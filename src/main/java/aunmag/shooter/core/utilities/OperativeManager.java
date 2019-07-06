package aunmag.shooter.core.utilities;

import java.util.ArrayList;

public class OperativeManager<T extends Operative> {

    public final ArrayList<T> all = new ArrayList<>();

    public void update() {
        for (var i = all.size() - 1; i >= 0; i--) {
            var operative = all.get(i);

            operative.update();

            if (!operative.isActive()) {
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
