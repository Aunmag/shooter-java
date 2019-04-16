package aunmag.shooter.game.client.states;

import aunmag.shooter.game.environment.actor.ActorType;

public class ScenarioStatus {
    // Selected player type in the current scenario
    public ActorType actorType = ActorType.human;

    /* Global Settings */

    public enum CrosshairControl {
        PRESS,
        HOLD,
    }
    public static CrosshairControl crosshairControl = CrosshairControl.PRESS;

    /* Statuses for each scenario */

    // Scenario Encircling
    public static final ScenarioStatus scenarioEncircling =
            new ScenarioStatus();
}
