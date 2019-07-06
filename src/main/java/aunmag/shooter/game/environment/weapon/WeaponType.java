package aunmag.shooter.game.environment.weapon;

import aunmag.shooter.core.audio.Sample;
import aunmag.shooter.core.structures.Texture;
import aunmag.shooter.game.environment.magazine.MagazineType;
import aunmag.shooter.game.environment.projectile.ProjectileType;
import org.jetbrains.annotations.Nullable;

public class WeaponType {

    public static final float GRIP_OFFSET_STEP = 1f / 32f;
    public static final float GRIP_OFFSET_SHORT = GRIP_OFFSET_STEP * 2;
    public static final float GRIP_OFFSET_COMMON = GRIP_OFFSET_STEP * 9;
    public static final float GRIP_OFFSET_EXTENDED = GRIP_OFFSET_STEP * 10;
    public static final int SEMI_AUTO_SHOTS_PER_MINUTE = 400;

    public final String name;
    public final Texture texture;
    @Nullable
    public final Sample sample;
    public final int shotsPerMinute;
    public final float velocity;
    public final float velocityDeflection;
    public final float radiansDeflection;
    public final float recoil;
    public final float recoilDeflection;
    public final boolean isAutomatic;
    public final MagazineType magazine;
    public final float length;
    public final float gripOffset;

    protected WeaponType(
            String name,
            int shotsPerMinute,
            float velocity,
            float radiansDeflection,
            float recoil,
            boolean isAutomatic,
            MagazineType magazine,
            float gripOffset
    ) {
        String path = "weapons/" + name;
        this.name = name;
        this.texture = Texture.getOrCreate(path + "/image", Texture.Type.SPRITE);
        this.sample = Sample.manger.provide(path + "/shot");
        this.shotsPerMinute = shotsPerMinute;
        this.velocity = velocity;
        this.velocityDeflection = velocity * 0.03f;
        this.radiansDeflection = radiansDeflection;
        this.recoil = recoil;
        this.recoilDeflection = recoil * 0.25f;
        this.isAutomatic = isAutomatic;
        this.magazine = magazine;
        this.length = texture.getWidth();
        this.gripOffset = gripOffset;
    }

    /* Types */

    public static final WeaponType laserGun = new WeaponType(
            "Laser Gun",
            1200,
            5000,
            0f,
            0f,
            true,
            new MagazineType(ProjectileType.laser, true, 0, 0),
            GRIP_OFFSET_EXTENDED
    );

    public static final WeaponType pm = new WeaponType(
            "PM",
            SEMI_AUTO_SHOTS_PER_MINUTE,
            315,
            0.05f,
            6_500,
            false,
            new MagazineType(ProjectileType._9x18mm_pm, true, 8, 2f),
            GRIP_OFFSET_SHORT
    );

    public static final WeaponType tt = new WeaponType(
            "TT",
            SEMI_AUTO_SHOTS_PER_MINUTE,
            430,
            0.05f,
            7_500,
            false,
            new MagazineType(ProjectileType._7_62x25mm_tt, true, 8, 2f),
            GRIP_OFFSET_SHORT
    );

    public static final WeaponType mp43sawedOff = new WeaponType(
            "MP-43 sawed-off",
            SEMI_AUTO_SHOTS_PER_MINUTE,
            260,
            0.08f,
            45_000,
            false,
            new MagazineType(ProjectileType._12_76_magnum, false, 2, 0.5f),
            GRIP_OFFSET_STEP * 4
    );

    public static final WeaponType mp27 = new WeaponType(
            "MP-27",
            SEMI_AUTO_SHOTS_PER_MINUTE,
            410,
            0.06f,
            38_000,
            false,
            new MagazineType(ProjectileType._12_76_magnum, false, 2, 0.5f),
            GRIP_OFFSET_EXTENDED
    );

    public static final WeaponType pp91kedr = new WeaponType(
            "PP-91 Kedr",
            900,
            310,
            0.04f,
            7_000,
            true,
            new MagazineType(ProjectileType._9x18mm_pm, true, 20, 1.8f),
            GRIP_OFFSET_COMMON
    );

    public static final WeaponType pp19bizon = new WeaponType(
            "PP-19 Bizon",
            680,
            330,
            0.03f,
            7_500,
            true,
            new MagazineType(ProjectileType._9x18mm_pm, true, 64, 1.5f),
            GRIP_OFFSET_COMMON
    );

    public static final WeaponType aks74u = new WeaponType(
            "AKS-74U",
            675,
            735,
            0.03f,
            12_000,
            true,
            new MagazineType(ProjectileType._5_45x39mm, true, 30, 2f),
            GRIP_OFFSET_COMMON
    );

    public static final WeaponType ak74m = new WeaponType(
            "AK-74M",
            600,
            910,
            0.028f,
            14_000,
            true,
            new MagazineType(ProjectileType._5_45x39mm, true, 30, 2f),
            GRIP_OFFSET_COMMON
    );

    public static final WeaponType rpk74 = new WeaponType(
            "RPK-74",
            600,
            960,
            0.025f,
            19_000,
            true,
            new MagazineType(ProjectileType._5_45x39mm, true, 45, 2f),
            GRIP_OFFSET_COMMON
    );

    public static final WeaponType saiga12k = new WeaponType(
            "Saiga-12K",
            SEMI_AUTO_SHOTS_PER_MINUTE,
            410,
            0.07f,
            32_000,
            false,
            new MagazineType(ProjectileType._12_76_magnum, true, 8, 2f),
            GRIP_OFFSET_COMMON
    );

    public static final WeaponType pkm = new WeaponType(
            "PKM",
            650,
            825,
            0.021f,
            22_000,
            true,
            new MagazineType(ProjectileType._7_62x54mmR, true, 100, 8f),
            GRIP_OFFSET_EXTENDED
    );

    public static final WeaponType pkpPecheneg = new WeaponType(
            "PKP Pecheneg",
            650,
            825,
            0.02f,
            22_000,
            true,
            new MagazineType(ProjectileType._7_62x54mmR, true, 100, 8f),
            GRIP_OFFSET_EXTENDED
    );

}
