package aunmag.shooter.game.client.states;

import aunmag.shooter.game.environment.actor.ActorType;

public class ScenarioStatus {
    // Selected player type in the current scenario
    public ActorType actorType = ActorType.human;

    /* Statuses for each scenario */

    // Scenario Encircling
    public static final ScenarioStatus scenarioEncircling =
            new ScenarioStatus();
}
