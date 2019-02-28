package aunmag.shooter.game.environment.magazine

import aunmag.shooter.game.environment.projectile.ProjectileType

class MagazineType(
        val projectile: ProjectileType,
        val isAutomatic: Boolean,
        val capacity: Int,
        val timeReloading: Float
) {
    val isUnlimited = capacity == 0
}
