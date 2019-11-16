package aunmag.shooter.game.client.player;

import aunmag.shooter.core.utilities.Envelope;
import aunmag.shooter.core.utilities.TimeFlow;
import aunmag.shooter.core.utilities.UtilsMath;
import aunmag.shooter.game.client.Context;

public class CameraShaker {

    private static final float FACTOR = 0.125f;
    private static final float ENVELOPE_ATTACK = 0.04f;
    private static final float ENVELOPE_RELEASE = 0.32f;
    private static final float ENVELOPE_TENSITY = 0.8f;

    private final Envelope envelope;

    public CameraShaker(TimeFlow time) {
        envelope = new Envelope(
            ENVELOPE_ATTACK,
            0, // TODO: Try to use sustain
            ENVELOPE_RELEASE,
            ENVELOPE_TENSITY,
            time
        );
    }

    public void shake(float force) {
        envelope.start(force * FACTOR);
    }

    public void update() {
        envelope.update();

        if (envelope.getValue() == 0.0f) {
            return;
        }

        var camera = Context.main.getCamera();
        var radians = UtilsMath.correctRadians(camera.getRadians() + envelope.getValue());
        camera.setRadians(radians);
        camera.mount.radians = radians;
        camera.mount.apply();
    }

}
