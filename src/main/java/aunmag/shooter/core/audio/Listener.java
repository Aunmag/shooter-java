package aunmag.shooter.core.audio;

import aunmag.shooter.core.utilities.Operative;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALC10;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

public class Listener extends Operative {

    private final long device = ALC10.alcOpenDevice((ByteBuffer) null);
    private final long context = ALC10.alcCreateContext(device, (IntBuffer) null);

    public Listener() {
        ALC10.alcMakeContextCurrent(context);
        AL.createCapabilities(ALC.createCapabilities(device));
    }

    @Override
    protected void onRemove() {
        ALC10.alcDestroyContext(context);
        ALC10.alcCloseDevice(device);
    }

    public void setPosition(float x, float y, float direction) {
        AL10.alListener3f(AL10.AL_POSITION, x, y, 0);

        var orientation = new Vector3f(0, 1, 0)
                .rotate(new Quaternionf(0, 0, 0, 0).rotateZ(direction));

        var orientationBuffer = ByteBuffer.allocateDirect(6 * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();

        orientationBuffer.put(0, orientation.x); // look at X
        orientationBuffer.put(1, orientation.y); // look at Y
        orientationBuffer.put(2, orientation.z); // look at Z
        orientationBuffer.put(3, 0); // up X
        orientationBuffer.put(4, 0); // up Y
        orientationBuffer.put(5, 1); // up Z

        AL10.alListenerfv(AL10.AL_ORIENTATION, orientationBuffer);
    }

}
