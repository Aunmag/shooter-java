package aunmag.shooter.game.environment.decorations;

import aunmag.shooter.core.structures.Texture;

public class DecorationType {

    public final Texture texture;

    public DecorationType(String name) {
        texture = Texture.getOrCreate("images/textures/" + name, Texture.Type.SPRITE);
    }

    public static final DecorationType bluff = new DecorationType("bluff");
    public static final DecorationType bluffCorner = new DecorationType("bluff_corner");
    public static final DecorationType tree1 = new DecorationType("tree_1");
    public static final DecorationType tree2 = new DecorationType("tree_2");
    public static final DecorationType tree3 = new DecorationType("tree_3");

}
