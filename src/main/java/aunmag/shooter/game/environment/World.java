package aunmag.shooter.game.environment;

import aunmag.shooter.core.Application;
import aunmag.shooter.core.utilities.Operative;
import aunmag.shooter.core.utilities.OperativeManager;
import aunmag.shooter.core.utilities.TimeFlow;
import aunmag.shooter.game.ai.Ai;
import aunmag.shooter.game.client.Context;
import aunmag.shooter.game.data.Sounds;
import aunmag.shooter.game.environment.actor.Actor;
import aunmag.shooter.game.environment.decorations.Decoration;
import aunmag.shooter.game.environment.projectile.Projectile;
import aunmag.shooter.game.environment.terrain.Terrain;
import aunmag.shooter.game.environment.utils.TreesGenerator;
import aunmag.shooter.game.items.ItemWeapon;
import org.lwjgl.opengl.GL11;

public class World extends Operative {

    public final TimeFlow time = new TimeFlow();
    public final Terrain terrain = new Terrain();
    public final OperativeManager<Decoration> ground = new OperativeManager<>();
    public final OperativeManager<Ai> ais = new OperativeManager<>();
    public final OperativeManager<Actor> actors = new OperativeManager<>();
    public final OperativeManager<Projectile> projectiles = new OperativeManager<>();
    public final OperativeManager<ItemWeapon> itemsWeapon = new OperativeManager<>();
    public final OperativeManager<Decoration> trees = new OperativeManager<>();

    public World() {
        new TreesGenerator(this, 96, 1f / 48f, 3.5f).generate();
    }

    @Override
    public void update() {
        time.add(Application.time.getDelta(), true);
        ais.update();
        actors.update();
        projectiles.update();
        itemsWeapon.update();
    }

    @Override
    public void render() {
        terrain.render();
        Application.getShader().bind();

        if (!Context.main.isDebug()) {
            ground.render();
        }

        itemsWeapon.render();
        actors.render();
        projectiles.render();
        GL11.glLineWidth(1f);

        if (Context.main.isDebug()) {
            ais.render();
        } else {
            Application.getShader().bind();
            trees.render();
        }
    }

    @Override
    protected void onRemove() {
        ground.remove();
        trees.remove();
        ais.remove();
        actors.remove();
        projectiles.remove();
        itemsWeapon.remove();
        stopSounds();
    }

    public void playSounds() {
        if (Sounds.soundAmbiance != null) {
            Sounds.soundAmbiance.play();
        }

        if (Sounds.soundAtmosphere != null) {
            Sounds.soundAtmosphere.play();
        }
    }

    public void pauseSounds() {
        if (Sounds.soundAmbiance != null) {
            Sounds.soundAmbiance.pause();
        }

        if (Sounds.soundAtmosphere != null) {
            Sounds.soundAtmosphere.pause();
        }
    }

    public void stopSounds() {
        if (Sounds.soundAmbiance != null) {
            Sounds.soundAmbiance.stop();
        }

        if (Sounds.soundAtmosphere != null) {
            Sounds.soundAtmosphere.stop();
        }
    }

}
