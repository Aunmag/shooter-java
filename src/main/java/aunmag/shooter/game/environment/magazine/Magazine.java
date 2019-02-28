package aunmag.shooter.game.environment.magazine;

import aunmag.shooter.core.utilities.Timer;
import aunmag.shooter.game.environment.World;

public class Magazine {

    public final World world;
    public final MagazineType type;
    private int cartridgesQuantity;
    private boolean isReloading = false;
    private final Timer reloadingTimer;

    public Magazine(World world, MagazineType type) {
        this.world = world;
        this.type = type;
        cartridgesQuantity = type.getCapacity();

        float reloadingTime = type.getTimeReloading();

        if (type.isAutomatic()) {
            reloadingTime /= (float) type.getCapacity();
        }

        reloadingTimer = new Timer(world.getTime(), reloadingTime, 0.125f);
    }

    public void update() {
        if (isReloading && reloadingTimer.isDone()) {
            cartridgesQuantity++;

            if (isFull() || !type.isAutomatic()) {
                isReloading = false;
            } else {
                reloadingTimer.next();
            }
        }
    }

    public boolean takeNextCartridge() {
        boolean hasCartridge = !isEmpty();

        if (hasCartridge && !type.isUnlimited()) {
            cartridgesQuantity--;
        }

        return hasCartridge;
    }

    public void reload() {
        if (isReloading || isFull()) {
            return;
        }

        if (reloadingTimer.getDuration() == 0) {
            if (type.isAutomatic()) {
                cartridgesQuantity = type.getCapacity();
            } else {
                cartridgesQuantity++;
            }
            return;
        }

        isReloading = true;
        reloadingTimer.next();

        if (type.isAutomatic()) {
            cartridgesQuantity = 0;
        }
    }

    /* Getters */

    public boolean isFull() {
        return cartridgesQuantity == type.getCapacity();
    }

    public boolean isEmpty() {
        return isReloading || cartridgesQuantity == 0 && !type.isUnlimited();
    }

    public boolean isReloading() {
        return isReloading;
    }

    public float calculateVolumeRatio() {
        if (type.isUnlimited()) {
            return 1.0f;
        } else {
            return cartridgesQuantity / (float) type.getCapacity();
        }
    }

}
