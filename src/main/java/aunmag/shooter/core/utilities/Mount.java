package aunmag.shooter.core.utilities;

import org.jetbrains.annotations.Nullable;
import org.joml.Vector2f;

public class Mount {

    public final Vector2f item;
    @Nullable public Vector2f holder;
    public float length = 0;
    public float radians = 0;

    public Mount(Vector2f item, @Nullable Vector2f holder) {
        this.item = item;
        this.holder = holder;
    }

    public void apply() {
        if (holder == null) {
            return;
        }

        var x = holder.x + length * (float) Math.cos(radians);
        var y = holder.y + length * (float) Math.sin(radians);
        item.set(x, y);
    }

}
