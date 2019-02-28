package aunmag.shooter.game.environment.terrain

import aunmag.shooter.core.Application
import aunmag.shooter.core.Configs
import aunmag.shooter.core.structures.Texture
import aunmag.shooter.core.utilities.UtilsGraphics
import aunmag.shooter.game.client.App
import org.lwjgl.opengl.GL11

class Terrain {

    private val blockSize = 4
    private val shader = ShaderTerrain()
    private val texture: Texture
    private val textureQuantity: Int

    init {
        textureQuantity = Math.ceil(
                (Application.getCamera().distanceView + blockSize) / blockSize.toDouble()
        ).toInt()
        val size = (blockSize * textureQuantity).toFloat()

        texture = Texture.getOrCreate(
            "images/textures/terrain",
            Texture.Type.SPRITE,
            null, null,
            size, size
        )
    }

    fun render() {
        if (App.main.isDebug) {
            renderGrid()
        } else {
            renderTexture()
        }
    }

    private fun renderTexture() {
        val camera = Application.getCamera()

        val offsetX = calculateAxisOffset(camera.position.x, blockSize / 2.0f)
        val offsetY = calculateAxisOffset(camera.position.y, blockSize / 2.0f)
        val x = camera.position.x + offsetX - (camera.position.x - offsetX) % blockSize
        val y = camera.position.y + offsetY - (camera.position.y - offsetY) % blockSize
        val projection = camera.calculateViewProjection(x, y, 0.0f)

        shader.bind()
        shader.setUniformProjection(projection)
        shader.setUniformQuantity(textureQuantity)
        texture.bind()
        texture.render()
        shader.setUniformQuantity(1)
    }

    private fun calculateAxisOffset(value: Float, offset: Float): Float {
        return when {
            value > +offset -> +offset
            value < -offset -> -offset
            else -> 0.0f
        }
    }

    private fun renderGrid() {
        val camera = Application.getCamera()

        val step = 1.0f
        val size = camera.distanceView / camera.scaleFull * Configs.getPixelsPerMeter()
        val center = removeRemainder(size * 1.25f, step * 2.0f) / 2.0f

        val xMin = camera.position.x - center
        val xMax = camera.position.x + center
        val yMin = camera.position.y - center
        val yMax = camera.position.y + center
        val gridX = removeRemainder(camera.position.x, step)
        val gridY = removeRemainder(camera.position.y, step)

        UtilsGraphics.drawPrepare()
        updateColor(0)

        var counter = -center + step
        while (counter <= center) {
            val x = gridX + counter * calculateMagnitude(camera.position.x)
            val y = gridY + counter * calculateMagnitude(camera.position.y)

            val isCenterByX = x == 0.0f
            if (isCenterByX) updateColor(1)
            UtilsGraphics.drawLine(x, yMin, x, yMax, true)
            if (isCenterByX) updateColor(0)

            val isCenterByY = y == 0.0f
            if (isCenterByY) updateColor(-1)
            UtilsGraphics.drawLine(xMin, y, xMax, y, true)
            if (isCenterByY) updateColor(0)

            counter += step
        }
    }

    private fun removeRemainder(value: Float, remainder: Float): Float {
        return value - value % remainder
    }

    private fun calculateMagnitude(value: Float): Float {
        return if (value < 0) {
            -1.0f
        } else {
            +1.0f
        }
    }

    private fun updateColor(axis: Int) {
        val max = 1.0f
        val min = 0.4f

        when (axis) {
            +1 -> GL11.glColor3f(max, min, min)
            -1 -> GL11.glColor3f(min, min, max)
            else -> GL11.glColor3f(min, min, min)
        }
    }

}
