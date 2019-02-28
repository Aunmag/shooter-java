package aunmag.shooter.core.audio;

import org.joml.Vector3f;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALC10;
import org.lwjgl.openal.ALCCapabilities;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public final class AudioMaster {

    private static long device;
    private static long context;
    private static final int LISTENER_OFFSET_Z = 0;

    private AudioMaster() {}

    public static void initialize() {
        device = ALC10.alcOpenDevice((ByteBuffer) null);
        ALCCapabilities deviceCaps = ALC.createCapabilities(device);

        context = ALC10.alcCreateContext(device, (IntBuffer) null);
        ALC10.alcMakeContextCurrent(context);
        AL.createCapabilities(deviceCaps);
    }

    public static void terminate() {
        ALC10.alcDestroyContext(context);
        ALC10.alcCloseDevice(device);
    }

    /* Setters */

    public static void setListenerPosition(
            float x,
            float y,
            float z,
            Vector3f orientationVector
    ) {
        AL10.alListener3f(AL10.AL_POSITION, x, y, z + LISTENER_OFFSET_Z);

        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(6 * 4);
        byteBuffer.order(ByteOrder.nativeOrder());
        FloatBuffer orientationBuffer = byteBuffer.asFloatBuffer();
        orientationBuffer.put(0, orientationVector.x); // Look at X
        orientationBuffer.put(1, orientationVector.y); // Look at Y
        orientationBuffer.put(2, orientationVector.z); // Look at Z
        orientationBuffer.put(3, 0); // Up X
        orientationBuffer.put(4, 0); // Up Y
        orientationBuffer.put(5, 1); // Up Z

        AL10.alListenerfv(AL10.AL_ORIENTATION, orientationBuffer);
    }

    public static void setListenerVelocity(Vector3f velocity) {
        AL10.alListener3f(AL10.AL_VELOCITY, velocity.x, velocity.y, velocity.z);
    }

}
