package aunmag.shooter.game.data

import aunmag.shooter.core.audio.Source
import aunmag.shooter.core.utilities.UtilsAudio

val soundAmbiance = initializeSoundAmbiance()
val soundAtmosphere = initializeSoundAtmosphere()
val soundGameOver = initializeSoundGameOver()

private fun initializeSoundAmbiance(): Source? {
    val sound = UtilsAudio.provideSound("sounds/ambiance/birds")
    sound?.setVolume(0.4f)
    sound?.setLooped(true)
    return sound
}

private fun initializeSoundAtmosphere(): Source? {
    val sound = UtilsAudio.provideSound("sounds/music/gameplay_atmosphere")
    sound?.setVolume(0.06f)
    sound?.setLooped(true)
    return sound
}

private fun initializeSoundGameOver(): Source? {
    val sound = UtilsAudio.provideSound("sounds/music/death")
    sound?.setVolume(0.6f)
    return sound
}
