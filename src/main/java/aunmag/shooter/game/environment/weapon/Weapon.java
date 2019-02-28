package aunmag.shooter.game.environment.weapon;

import aunmag.shooter.core.audio.AudioSource;
import aunmag.shooter.core.math.BodyLine;
import aunmag.shooter.core.utilities.Operative;
import aunmag.shooter.core.utilities.UtilsMath;
import aunmag.shooter.game.client.App;
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
    private AudioSource audioSource;

    public Weapon(World world, WeaponType type) {
        this.body = new BodyLine(0, 0, 0, 0);
        this.world = world;
        this.type = type;
        this.magazine = new Magazine(world, type.magazine);
        this.striker = new Striker(world, type.shotsPerMinute);
        this.trigger = new Trigger(type.isAutomatic);

        audioSource = new AudioSource();
        audioSource.setSample(type.sample);
    }

    public void update() {
        float muzzleLength = type.texture.getCenterX();
        float x = body.positionTail.x + muzzleLength * (float) Math.cos(body.radians);
        float y = body.positionTail.y + muzzleLength * (float) Math.sin(body.radians);
        body.position.set(x, y);

        magazine.update();

        if (trigger.isFiring() && striker.isCocked() && magazine.takeNextCartridge()) {
            makeShot();
        }
    }

    private void makeShot() {
        audioSource.play();
        trigger.getShooter().push(calculateRandomRecoil());

        for (int bullet = 0; bullet < magazine.type.getProjectile().shot; bullet++) {
            makeBullet(body.position.x, body.position.y);
        }
    }

    private void makeBullet(float x, float y) {
        Projectile projectile = new Projectile(
                world,
                magazine.type.getProjectile(),
                x,
                y,
                calculateRandomRadians(),
                calculateRandomVelocity(),
                trigger.getShooter()
        );
        world.getProjectiles().all.add(projectile);
    }

    private float calculateRandomRecoil() {
        float recoil = UtilsMath.randomizeFlexibly(type.recoil, type.recoilDeflection);

        if (UtilsMath.random.nextBoolean()) {
            recoil = -recoil;
        }

        return recoil;
    }

    private float calculateRandomRadians() {
        return UtilsMath.randomizeFlexibly(body.radians, type.radiansDeflection);
    }

    private float calculateRandomVelocity() {
        return UtilsMath.randomizeFlexibly(type.velocity, type.velocityDeflection);
    }

    @Override
    public void render() {
        if (App.main.isDebug()) {
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
