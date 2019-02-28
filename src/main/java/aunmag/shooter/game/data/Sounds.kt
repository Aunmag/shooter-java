package aunmag.shooter.game.data

import aunmag.shooter.core.audio.AudioSource
import aunmag.shooter.core.utilities.UtilsAudio

val soundAmbiance = initializeSoundAmbiance()
val soundAtmosphere = initializeSoundAtmosphere()
val soundGameOver = initializeSoundGameOver()

private fun initializeSoundAmbiance(): AudioSource {
    val sound = UtilsAudio.getOrCreateSoundOgg("sounds/ambiance/birds")
    sound.setVolume(0.4f)
    sound.setIsLooped(true)
    return sound
}

private fun initializeSoundAtmosphere(): AudioSource {
    val sound = UtilsAudio.getOrCreateSoundOgg("sounds/music/gameplay_atmosphere")
    sound.setVolume(0.06f)
    sound.setIsLooped(true)
    return sound
}

private fun initializeSoundGameOver(): AudioSource {
    val sound = UtilsAudio.getOrCreateSoundOgg("sounds/music/death")
    sound.setVolume(0.6f)
    return sound
}
