package aunmag.shooter.game.environment.actor;

import org.jetbrains.annotations.Nullable;

public class Control {

    private boolean isWalkingForward = false;
    private boolean isWalkingBack = false;
    private boolean isWalkingLeft = false;
    private boolean isWalkingRight = false;
    private boolean isSprinting = false;
    private boolean isAttacking = false;
    private boolean isAiming = false;
    @Nullable
    private Float turningTo = null;

    public void reset() {
        isWalkingForward = false;
        isWalkingBack = false;
        isWalkingLeft = false;
        isWalkingRight = false;
        isSprinting = false;
        isAttacking = false;
        isAiming = false;
        turningTo = null;
    }

    /* Setters */

    public void walkForward() {
        isWalkingForward = true;
    }

    public void walkBack() {
        isWalkingBack = true;
    }

    public void walkLeft() {
        isWalkingLeft = true;
    }

    public void walkRight() {
        isWalkingRight = true;
    }

    public void sprint() {
        isSprinting = true;
    }

    public void attack() {
        isAttacking = true;
    }

    public void aim() {
        isAiming = true;
    }

    public void turnTo(float angle) {
        turningTo = angle;
    }

    public boolean isWalking() {
        return isWalkingForward()
                || isWalkingBack()
                || isWalkingLeft()
                || isWalkingRight();
    }

    public boolean isWalkingForward() {
        return isWalkingForward;
    }

    public boolean isWalkingBack() {
        return isWalkingBack;
    }

    public boolean isWalkingLeft() {
        return isWalkingLeft;
    }

    public boolean isWalkingRight() {
        return isWalkingRight;
    }

    public boolean isSprinting() {
        return isSprinting;
    }

    public boolean isAttacking() {
        return isAttacking;
    }

    public boolean isAiming() {
        return isAiming;
    }

    public boolean isTurning() {
        return turningTo != null;
    }

    @Nullable
    public Float getTurningTo() {
        return turningTo;
    }

}
