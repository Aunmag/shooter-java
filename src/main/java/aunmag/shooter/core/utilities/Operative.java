package aunmag.shooter.core.utilities;

public abstract class Operative {

    private boolean isActive = true;

    public void update() {}

    public void render() {}

    public final void remove() {
        if (isActive) {
            isActive = false;
            onRemove();
        }
    }

    protected void onRemove() {}

    public boolean isActive() {
        return isActive;
    }

}
