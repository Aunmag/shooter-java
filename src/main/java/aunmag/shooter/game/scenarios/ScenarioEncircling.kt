package aunmag.shooter.game.scenarios

import aunmag.shooter.core.gui.Button
import aunmag.shooter.core.gui.Label
import aunmag.shooter.core.gui.Notification
import aunmag.shooter.core.gui.Page
import aunmag.shooter.core.gui.font.FontStyle
import aunmag.shooter.core.structures.Texture
import aunmag.shooter.core.utilities.Timer
import aunmag.shooter.core.utilities.UtilsGraphics
import aunmag.shooter.core.utilities.UtilsMath
import aunmag.shooter.core.utilities.UtilsMath.limitNumber
import aunmag.shooter.game.ai.Ai
import aunmag.shooter.game.client.Context
import aunmag.shooter.game.client.Player
import aunmag.shooter.game.data.soundGameOver
import aunmag.shooter.game.environment.World
import aunmag.shooter.game.environment.actor.Actor
import aunmag.shooter.game.environment.actor.ActorType
import aunmag.shooter.game.environment.decorations.Decoration
import aunmag.shooter.game.environment.decorations.DecorationType
import aunmag.shooter.game.environment.weapon.Weapon
import aunmag.shooter.game.environment.weapon.WeaponType
import aunmag.shooter.game.items.ItemWeapon
import org.lwjgl.opengl.GL11

class ScenarioEncircling(world: World) : Scenario(world) {

    private val bordersDistance = 32f
    private var wave = 0
    private val waveFinal = 6
    private val zombiesQuantityInitial = 5
    private var zombiesQuantityToSpawn = 0
    private val zombiesSpawnTimer = Timer(world.time, 0.5)
    private var zombie = ActorType.zombie
    private var zombieAgile = ActorType.zombieAgile
    private var zombieHeavy = ActorType.zombieHeavy
    private var bonusDropChance = 0f
    private var bonusDropLaserGun = 0.00005f
    private val difficulty = 1.1f

    init {
        initializeBluffs()
        startNextWave()
    }

    private fun initializeBluffs() {
        val ground = world.ground.all
        val quantity = bordersDistance / 2 + 1
        val step = 4
        val length = step * quantity
        val first = length / -2f + step / 2f
        val last = first + length - step

        val a = Math.PI.toFloat()
        val b = 0f
        val c = UtilsMath.PIx0_5.toFloat()
        val d = UtilsMath.PIx1_5.toFloat()

        var i = first + step
        while (i <= last - step) {
            ground.add(Decoration(DecorationType.bluff, i, first, a))
            ground.add(Decoration(DecorationType.bluff, i, last, b))
            ground.add(Decoration(DecorationType.bluff, first, i, c))
            ground.add(Decoration(DecorationType.bluff, last, i, d))
            i += step.toFloat()
        }

        ground.add(Decoration(DecorationType.bluffCorner, first, first, a))
        ground.add(Decoration(DecorationType.bluffCorner, last, last, b))
        ground.add(Decoration(DecorationType.bluffCorner, first, last, c))
        ground.add(Decoration(DecorationType.bluffCorner, last, first, d))
    }

    override fun update() {
        if (Context.main.playerActor?.isAlive != true) {
            gameOver(false)
            return
        }

        confinePlayerPosition()

        val areAllZombiesSpawned = zombiesQuantityToSpawn == 0
        if (areAllZombiesSpawned && world.actors.all.size == 1) {
            startNextWave()
        } else if (!areAllZombiesSpawned && zombiesSpawnTimer.isDone) {
            spawnZombie()
            zombiesSpawnTimer.next()
        }
    }

    override fun render() {
        if (Context.main.isDebug) {
            renderBorders()
        }
    }

    private fun renderBorders() {
        val n = bordersDistance
        GL11.glLineWidth(2f)
        GL11.glColor3f(1f, 0f, 0f)
        UtilsGraphics.drawLine(-n, -n, +n, -n, true)
        UtilsGraphics.drawLine(+n, -n, +n, +n, true)
        UtilsGraphics.drawLine(+n, +n, -n, +n, true)
        UtilsGraphics.drawLine(-n, +n, -n, -n, true)
        GL11.glLineWidth(1f)
    }

    private fun startNextWave() {
        if (wave == waveFinal) {
            gameOver(true)
            return
        }

        wave++
        zombiesQuantityToSpawn = wave * wave * zombiesQuantityInitial
        bonusDropChance = wave * 0.8f / zombiesQuantityToSpawn

        updateZombiesTypes()

        world.notifications.add(Notification(
                world.time,
                "Wave $wave/$waveFinal",
                "Kill $zombiesQuantityToSpawn zombies"
        ))
    }

    private fun updateZombiesTypes() {
        val skillFactor = (difficulty - 1) * (wave - 1) + 1
        zombie = creteZombieType(ActorType.zombie, skillFactor)
        zombieAgile = creteZombieType(ActorType.zombieAgile, skillFactor)
        zombieHeavy = creteZombieType(ActorType.zombieHeavy, skillFactor)
    }

    private fun creteZombieType(type: ActorType, skillFactor: Float): ActorType {
        return ActorType(
                type.name,
                type.radius,
                type.weight,
                skillFactor * type.strength,
                skillFactor * type.velocity,
                type.velocityFactorSprint,
                type.velocityRotation,
                type.damage,
                type.reaction
        )
    }

    private fun confinePlayerPosition() {
        val position = Context.main.playerActor?.body?.position ?: return
        position.x = limitNumber(position.x, -bordersDistance, bordersDistance)
        position.y = limitNumber(position.y, -bordersDistance, bordersDistance)
    }

    private fun spawnZombie() {
        val type = when (UtilsMath.randomizeBetween(1, 4)) {
            1 -> zombieAgile
            2 -> zombieHeavy
            else -> zombie
        }

        val distance = Player.SCALE_MAX / 2f
        val direction = UtilsMath.randomizeBetween(0f, UtilsMath.PIx2.toFloat())

        val centerX = Context.main.playerActor?.body?.position?.x ?: 0f
        val centerY = Context.main.playerActor?.body?.position?.y ?: 0f
        val x = centerX - distance * Math.cos(direction.toDouble()).toFloat()
        val y = centerY - distance * Math.sin(direction.toDouble()).toFloat()

        val zombie = Actor(type, world, x, y, -direction)
        world.actors.all.add(zombie)
        world.ais.all.add(Ai(zombie))

        val randomFloat = UtilsMath.random.nextFloat()
        if (randomFloat <= bonusDropLaserGun) {
            createWeaponBonus(zombie, WeaponType.laserGun)
        } else if (randomFloat <= bonusDropChance) {
            createWeaponBonus(zombie, selectRandomWeaponType())
        }

        zombiesQuantityToSpawn--
    }

    private fun selectRandomWeaponType(): WeaponType {
        return when (UtilsMath.randomizeBetween(1, 2 * wave)) {
            1 -> WeaponType.pm
            2 -> WeaponType.tt
            3 -> WeaponType.mp43sawedOff
            4 -> WeaponType.mp27
            5 -> WeaponType.pp91kedr
            6 -> WeaponType.pp19bizon
            7 -> WeaponType.aks74u
            8 -> WeaponType.ak74m
            9 -> WeaponType.rpk74
            10 -> WeaponType.saiga12k
            11 -> WeaponType.pkm
            12 -> WeaponType.pkpPecheneg
            else -> WeaponType.laserGun
        }
    }

    private fun createWeaponBonus(giver: Actor, weaponType: WeaponType) {
        val weapon = Weapon(world, weaponType)
        val bonus = ItemWeapon(giver, weapon)
        world.itemsWeapon.all.add(bonus)
    }

    private fun gameOver(isVictory: Boolean) {
        createGameOverPage(isVictory)
        Context.main.application.endGame()

        if (!isVictory) {
            soundGameOver.play()
        }
    }

    private fun createGameOverPage(isVictory: Boolean) {
        val wallpaperName = if (isVictory) "victory" else "death"
        val wallpaper = Texture.getOrCreate(
                "images/wallpapers/$wallpaperName", Texture.Type.WALLPAPER
        )
        val page = Page(wallpaper)

        val title = if (isVictory) "Well done!" else "You have died"
        val kills = Context.main.playerActor?.kills ?: 0
        val wavesSurvived = if (isVictory) wave else wave - 1
        val score = "You killed $kills zombies and survived $wavesSurvived/$waveFinal waves."

        page.add(Label(4, 3, 4, 1, title))
        page.add(Label(4, 4, 4, 1, score, FontStyle.LABEL_LIGHT))
        page.add(Button(4, 9, 4, 1, "Back to main menu", Button.ACTION_BACK))

        page.open()
        Context.main.application.isPause = true
    }

}
