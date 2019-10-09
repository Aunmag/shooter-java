package aunmag.shooter.game.environment.actor;

import aunmag.shooter.core.structures.Texture;
import aunmag.shooter.game.environment.weapon.WeaponType;
import org.jetbrains.annotations.Nullable;

public class ActorType {

    public static final float STRENGTH_DEFAULT = 7500;

    public final String name;
    public final Genus genus;
    public final float radius;
    public final float mass;
    public final float strength;
    public final float velocity;
    public final float velocityFactorSprint;
    public final float velocityRotation;
    public final float damage;
    public final float reaction;
    @Nullable public final WeaponType primaryWeapon;
    public final Texture texture;

    public ActorType(
            String name,
            Genus genus,
            float radius,
            float mass,
            float strength,
            float velocity,
            float velocityFactorSprint,
            float velocityRotation,
            float damage,
            float reaction,
            @Nullable WeaponType primaryWeapon
    ) {
        this.name = name;
        this.genus = genus;
        this.radius = radius;
        this.mass = mass;
        this.strength = strength;
        this.velocity = velocity;
        this.velocityFactorSprint = velocityFactorSprint;
        this.velocityRotation = velocityRotation;
        this.damage = damage;
        this.reaction = reaction;
        this.primaryWeapon = primaryWeapon;

        var texture = Texture.manager.asSprite().provide("actors/" + name + "/image");

        if (texture == null) {
            texture = Texture.empty;
        }

        this.texture = texture;
    }

    public static ActorType clone(ActorType type, float skill) {
        return new ActorType(
                type.name,
                Genus.HUMAN,
                type.radius,
                type.mass,
                skill * type.strength,
                skill * type.velocity,
                type.velocityFactorSprint,
                type.velocityRotation,
                type.damage,
                type.reaction,
                type.primaryWeapon
        );
    }

    /* Types */

    public static final ActorType soldier = new ActorType(
            "soldier",
            Genus.HUMAN,
            0.225f,
            80_000,
            STRENGTH_DEFAULT,
            2.58f,
            2.76f,
            8,
            STRENGTH_DEFAULT / 16f,
            0.06f,
            WeaponType.pm
    );

   public static final ActorType bandit = new ActorType(
           "bandit",
           Genus.HUMAN,
           soldier.radius * 1.1f,
           soldier.mass * 1.1f,
           soldier.strength * 0.8f,
           soldier.velocity * 1.2f,
           soldier.velocityFactorSprint * 1.2f,
           soldier.velocityRotation * 1.2f,
           soldier.damage * 0.8f,
           soldier.reaction * 0.8f,
           WeaponType.tt
   );

    public static final ActorType zombie = new ActorType(
            "zombie",
            Genus.ZOMBIE,
            soldier.radius,
            70_000,
            0.4f * soldier.strength,
            0.4f * soldier.velocity,
            0.4f * soldier.velocityFactorSprint,
            0.4f * soldier.velocityRotation,
            soldier.strength / 8f,
            0.2f,
            null
    );

    public static final ActorType zombieAgile = new ActorType(
            "zombie agile",
            Genus.ZOMBIE,
            0.8f * zombie.radius,
            40_000,
            0.6f * zombie.strength,
            1.5f * zombie.velocity,
            zombie.velocityFactorSprint,
            2.5f * zombie.velocityRotation,
            0.4f * zombie.damage,
            0.1f,
            null
    );

    public static final ActorType zombieHeavy = new ActorType(
            "zombie heavy",
            Genus.ZOMBIE,
            1.2f * zombie.radius,
            120_000,
            2.0f * zombie.strength,
            0.7f * zombie.velocity,
            zombie.velocityFactorSprint,
            0.7f * zombie.velocityRotation,
            1.8f * zombie.damage,
            0.3f,
            null
    );


    public enum Genus {
        HUMAN,
        ZOMBIE
    }
}
