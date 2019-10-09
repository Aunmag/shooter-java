package aunmag.shooter.game.environment.weapon.components;

import aunmag.shooter.game.environment.actor.Actor;

public class Trigger {

    private boolean isAutomaticMode;
    private boolean isClicked = false;
    private boolean isPressed = false;
    private Actor shooter = null;

    public Trigger(boolean isAutomaticMode) {
        this.isAutomaticMode = isAutomaticMode;
    }

    public void pressBy(Actor shooter) {
        if (!isPressed) {
            this.shooter = shooter;
            isClicked = true;
        }

        isPressed = true;
    }

    public void release() {
        isPressed = false;
        shooter = null;
    }

    public boolean isFiring() {
        return isAutomaticMode ? isPressed : isClicked();
    }

    public boolean isClicked() {
        // TODO: Improve
        var wasClicked = isClicked;
        isClicked = false;
        return wasClicked;
    }

    public boolean isPressed() {
        return isPressed;
    }

    public Actor getShooter() {
        return shooter;
    }

}
