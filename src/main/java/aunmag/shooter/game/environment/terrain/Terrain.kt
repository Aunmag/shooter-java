package aunmag.shooter.game.environment.terrain

import aunmag.shooter.core.Application
import aunmag.shooter.core.graphics.Graphics
import aunmag.shooter.core.structures.Texture
import aunmag.shooter.game.client.Context
import aunmag.shooter.game.client.player.Player
import org.lwjgl.opengl.GL11

class Terrain {

    private val blockSize = 4
    private val shader = ShaderTerrain()
    private val texture: Texture
    private val textureQuantity: Int

    init {
        textureQuantity = Math.ceil(
                (Player.SCALE_MAX + blockSize) / blockSize.toDouble()
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
        if (Context.main.isDebug) {
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
        val projection = camera.toViewProjection(x, y, 0.0f)

        shader.bind()
        shader.setUniformProjection(projection)
        shader.setUniformQuantity(textureQuantity)
        texture.bind()
        texture.render()
        Application.getShader().bind()
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
        val center = removeRemainder(camera.scale, step * 2.0f) / 2.0f

        val xMin = camera.position.x - center
        val xMax = camera.position.x + center
        val yMin = camera.position.y - center
        val yMax = camera.position.y + center
        val gridX = removeRemainder(camera.position.x, step)
        val gridY = removeRemainder(camera.position.y, step)

        updateColor(0)

        var counter = -center + step
        while (counter <= center) {
            val x = gridX + counter * calculateMagnitude(camera.position.x)
            val y = gridY + counter * calculateMagnitude(camera.position.y)

            val isCenterByX = x == 0.0f
            if (isCenterByX) updateColor(1)
            Graphics.draw.line(x, yMin, x, yMax, camera::project)
            if (isCenterByX) updateColor(0)

            val isCenterByY = y == 0.0f
            if (isCenterByY) updateColor(-1)
            Graphics.draw.line(xMin, y, xMax, y, camera::project)
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
