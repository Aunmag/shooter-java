package aunmag.shooter.core.utilities;

import java.util.ArrayList;

public class OperativeManager<T extends Operative> {

    public final ArrayList<T> all = new ArrayList<>();

    public void update() {
        for (int i = all.size() - 1; i >= 0; i--) {
            T operative = all.get(i);

            operative.update();

            if (operative.isRemoved()) {
                all.remove(i);
            }
        }
    }

    public void render() {
        for (T operative: all) {
            operative.render();
        }
    }

    public void remove() {
        for (T operative: all) {
            operative.remove();
        }

        all.clear();
    }

}
