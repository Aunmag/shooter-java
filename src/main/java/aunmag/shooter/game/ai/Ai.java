package aunmag.shooter.game.ai;

import java.util.Arrays;
import aunmag.shooter.core.math.CollisionCC;
import aunmag.shooter.core.utilities.Operative;
import aunmag.shooter.core.utilities.TimeFlow;
import aunmag.shooter.core.utilities.Timer;
import aunmag.shooter.core.utilities.UtilsMath;
import aunmag.shooter.game.environment.actor.Actor;
import aunmag.shooter.game.environment.actor.ActorType;

public class Ai extends Operative {

    private final Timer reactionWatch;
    private final Timer reactionLookAround;
    private final Timer reactionChangeStrategy;
//    private int strategyDeviationWay = 0; // TODO: Use it
    private Actor subject;
    private AiMemoryTarget memoryTarget = new AiMemoryTarget();

    public Ai(Actor subject) {
        this.subject = subject;

        TimeFlow time = subject.world.getTime();
        float reaction = subject.type.reaction;
        reactionWatch = new Timer(time, reaction, 0.125f);
        reactionLookAround = new Timer(time, reaction * 8, 0.125f);
        reactionChangeStrategy = new Timer(time, reaction * 128, 0.125f);

        changeStrategy();
    }

    public void update() {
        if (subject.isRemoved() || !subject.isAlive()) {
            remove();
            return;
        }

        if (reactionChangeStrategy.isDone()) {
            changeStrategy();
            reactionChangeStrategy.next();
        }

        if (reactionLookAround.isDone()) {
            searchTarget();
            reactionLookAround.next();
        }

        if (memoryTarget.isInMemory()) {
            if (reactionWatch.isDone()) {
                updateTargetData();
                reactionWatch.next();
            }

            if (memoryTarget.isReached()) {
                doAttack();
            } else {
                chaseTarget();
            }
        } else {
            doNothing();
        }
    }

    private void changeStrategy() {
//        strategyDeviationWay = UtilsMath.randomizeBetween(-1, +1); // TODO: Use it
    }

    private void searchTarget() {
        memoryTarget.forget();

        for (Actor actor: subject.world.getActors().all) {
            if (actor.isAlive()
                    && Arrays.asList(
                            ActorType.playableTypes).contains(actor.type)) {
                memoryTarget.setActor(actor);
                break;
            }
        }
    }

    private void updateTargetData() {
        Actor targetActor = memoryTarget.getActor();

        if (targetActor == null) {
            return;
        }

        float x = subject.body.position.x();
        float y = subject.body.position.y();
        float targetX = targetActor.body.position.x();
        float targetY = targetActor.body.position.y();

        memoryTarget.setDistance(
                UtilsMath.calculateDistanceBetween(x, y, targetX, targetY)
        );

        memoryTarget.setDirection(
                UtilsMath.calculateRadiansBetween(targetX, targetY, x, y)
        );

        memoryTarget.setReached(
                new CollisionCC(subject.hands.coverage, targetActor.body).isTrue()
        );

        memoryTarget.setRadiansDifference(UtilsMath.radiansDifference(
                targetActor.body.radians,
                memoryTarget.getDirection()
        ));
    }

    public boolean isBehindTarget() {
        return Math.abs(memoryTarget.getRadiansDifference()) < UtilsMath.PIx0_5;
    }

    private void chaseTarget() {
        subject.isAttacking = false;
        subject.isWalkingForward = true;
        subject.isSprinting = isBehindTarget();
        turnOnTarget();

        if (Math.abs(memoryTarget.getRadiansDifference()) > UtilsMath.PIx0_5) {
            deviateRoute();
        } else if (memoryTarget.getDistance() < 3) {
            subject.isSprinting = true;
        }
    }

    private void turnOnTarget() {
        float timeDelta = (float) subject.world.getTime().getDelta();
        float velocity = subject.type.velocityRotation;
        float velocityFuture = (subject.kinetics.velocityRadians + velocity) * timeDelta;
        float radiansDifference = UtilsMath.radiansDifference(
                subject.body.radians,
                memoryTarget.getDirection()
        );

        if (velocityFuture * 2f > Math.abs(radiansDifference)) {
            return;
        }

        if (radiansDifference < 0) {
            velocity = -velocity;
        }

        subject.kinetics.addEnergy(0, 0, velocity, timeDelta);
    }

    @Deprecated
    private void deviateRoute() {
        // TODO: Fix this method. It was broken after new AI rotation system

        /*
        float targetDistance = memoryTarget.getDistance();
        int distanceMin = 3;
        int distanceMax = 20;

        if (distanceMin < targetDistance && targetDistance < distanceMax) {
            float intensity = targetDistance / distanceMax;
            float radians = (float) (UtilsMath.PIx0_5) * intensity;
            subject.body.radians += radians * strategyDeviationWay;
            subject.body.correctRadians();
        }
        */
    }

    private void doAttack() {
        subject.isAttacking = true;
        subject.isWalkingForward = false;
    }

    private void doNothing() {
        subject.isAttacking = false;
        subject.isSprinting = false;
        subject.isWalkingForward = false;
    }

    public void render() {}

}
