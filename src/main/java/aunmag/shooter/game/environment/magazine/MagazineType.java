package aunmag.shooter.game.environment.magazine;

import aunmag.shooter.game.environment.projectile.ProjectileType;

public class MagazineType {

    public final ProjectileType projectile;
    public final boolean isAutomatic;
    public final int capacity;
    public final float timeReloading;

    public MagazineType(
            ProjectileType projectile,
            boolean isAutomatic,
            int capacity,
            float timeReloading
    ) {
        this.projectile = projectile;
        this.isAutomatic = isAutomatic;
        this.capacity = capacity;
        this.timeReloading = timeReloading;
    }

    public boolean isUnlimited() {
        return capacity == 0;
    }

}
