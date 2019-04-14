package aunmag.shooter.game.ai;

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
                subject.control.attack();
            } else {
                chaseTarget();
            }
        }
    }

    private void changeStrategy() {
//        strategyDeviationWay = UtilsMath.randomizeBetween(-1, +1); // TODO: Use it
    }

    private void searchTarget() {
        memoryTarget.forget();

        for (Actor actor: subject.world.getActors().all) {
            if (actor.isAlive()
                    && (actor.type == ActorType.human
                        || actor.type == ActorType.humanCowboy)) {
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
        subject.control.attack();
        subject.control.walkForward();

        if (isBehindTarget()) {
            subject.control.sprint();
        }

        subject.control.turnTo(memoryTarget.getDirection());

        if (Math.abs(memoryTarget.getRadiansDifference()) > UtilsMath.PIx0_5) {
            deviateRoute();
        } else if (memoryTarget.getDistance() < 3) {
            subject.control.sprint();
        }
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

    public void render() {}

}
