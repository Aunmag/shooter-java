package aunmag.shooter.game.environment.weapon;

import aunmag.shooter.core.audio.Source;
import aunmag.shooter.core.math.BodyLine;
import aunmag.shooter.core.utilities.Operative;
import aunmag.shooter.core.utilities.UtilsRandom;
import aunmag.shooter.game.client.Context;
import aunmag.shooter.game.environment.World;
import aunmag.shooter.game.environment.magazine.Magazine;
import aunmag.shooter.game.environment.projectile.Projectile;
import aunmag.shooter.game.environment.weapon.components.Striker;
import aunmag.shooter.game.environment.weapon.components.Trigger;

public class Weapon extends Operative {

    public final BodyLine body;
    public final World world;
    public final WeaponType type;
    public final Magazine magazine;
    public final Striker striker;
    public final Trigger trigger;
    private Source audioSource;

    public Weapon(World world, WeaponType type) {
        this.body = new BodyLine(0, 0, 0, 0);
        this.world = world;
        this.type = type;
        this.magazine = new Magazine(world, type.magazine);
        this.striker = new Striker(world, type.shotsPerMinute);
        this.trigger = new Trigger(type.isAutomatic);

        audioSource = new Source();

        if (type.sample != null) {
            audioSource.setSample(type.sample);
        }
    }

    @Override
    public void update() {
        var muzzleLength = type.texture.getCenterX();
        var x = body.positionTail.x + muzzleLength * (float) Math.cos(body.radians);
        var y = body.positionTail.y + muzzleLength * (float) Math.sin(body.radians);
        body.position.set(x, y);

        magazine.update();

        if (trigger.isFiring() && striker.isCocked() && magazine.takeNextCartridge()) {
            makeShot();
        }
    }

    private void makeShot() {
        audioSource.play();
        trigger.getShooter().shake(calculateRandomRecoil(), true);

        for (int bullet = 0; bullet < magazine.type.projectile.shot; bullet++) {
            makeBullet(body.position.x, body.position.y);
        }
    }

    private void makeBullet(float x, float y) {
        var projectile = new Projectile(
                world,
                magazine.type.projectile,
                x,
                y,
                calculateRandomRadians(),
                calculateRandomVelocity(),
                trigger.getShooter()
        );
        world.projectiles.all.add(projectile);
    }

    private float calculateRandomRecoil() {
        return UtilsRandom.deviation(type.recoil, type.recoilDeflection);
    }

    private float calculateRandomRadians() {
        return UtilsRandom.deviation(body.radians, type.radiansDeflection);
    }

    private float calculateRandomVelocity() {
        return UtilsRandom.deviation(type.velocity, type.velocityDeflection);
    }

    @Override
    public void render() {
        if (Context.main.isDebug()) {
            body.render();
        } else {
            type.texture.renderOnWorld(
                    body.positionTail.x,
                    body.positionTail.y,
                    body.radians
            );
        }
    }

}
