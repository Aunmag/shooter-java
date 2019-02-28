package aunmag.shooter.core.utilities;

public abstract class Operative {

    private boolean isRemoved = false;

    public void update() {}

    public void render() {}

    public void remove() {
        isRemoved = true;
    }

    public boolean isRemoved() {
        return isRemoved;
    }

}
