package aunmag.shooter.game.client.states;

import aunmag.shooter.game.environment.actor.ActorType;

public class ScenarioStatus {
    // Текущий выбранный тип игрока
    public ActorType actorType = ActorType.human;

    /* Статусы для каждого сценария */

    // Scenario Encircling
    public static final ScenarioStatus scenarioEncircling =
            new ScenarioStatus();
}
