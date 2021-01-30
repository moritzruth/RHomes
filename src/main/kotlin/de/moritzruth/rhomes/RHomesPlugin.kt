package de.moritzruth.rhomes

import de.moritzruth.rhomes.messages.EnglishMessages
import de.moritzruth.rhomes.messages.GermanMessages
import de.moritzruth.rhomes.messages.Messages
import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.Sound
import org.bukkit.SoundCategory
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitTask

class RHomesPlugin : JavaPlugin(), Listener {
    companion object {
        lateinit var instance: RHomesPlugin
    }

    init {
        instance = this
    }

    private lateinit var messages: Messages
    private lateinit var homesFile: ConfigFile

    override fun onEnable() {
        messages = when (val language = config.getString("language", "en")) {
            "en" -> EnglishMessages
            "de" -> GermanMessages
            else -> throw Exception("Unsupported language specified: $language")
        }

        homesFile = ConfigFile.createOrLoad("homes")
        server.pluginManager.registerEvents(this, this)
    }

    private fun CommandSender.sendPrefixedMessage(message: String) =
        sendMessage("${ChatColor.GRAY}[${ChatColor.GOLD}Homes${ChatColor.GRAY}] ${ChatColor.RESET}$message")

    private val teleportTasks = HashMap<Player, BukkitTask>()

    private fun Player.scheduleTeleport(location: Location, delay: Int = config.getInt("teleportDelay", 3)) {
        if (delay == 0) {
            teleport(location)
            playSound(this.location, Sound.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 1f, 1f)
            teleportTasks.remove(this)
        } else {
            sendPrefixedMessage(messages.willTeleportIn(delay))
            playSound(this.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.MASTER, 1f, 1f)

            teleportTasks[this] = server.scheduler.runTaskLater(this@RHomesPlugin, Runnable {
                scheduleTeleport(location, delay - 1)
            }, 20)
        }
    }

    private fun useSelf(player: Player) {
        if (!player.hasPermission("rhomes.use.own")) player.sendPrefixedMessage(messages.noPermissionUseOwn)
        else {
            val home = homesFile.getLocation("${player.uniqueId}.own")

            if (home == null) player.sendPrefixedMessage(messages.noOwnHomeSet)
            else player.scheduleTeleport(home)
        }
    }

    private fun useGuest(player: Player, targetName: String) {
        if (!player.hasPermission("rhomes.use.guest")) player.sendPrefixedMessage(messages.noPermissionUseGuest)
        else {
            val home = homesFile.getKeys(false)
                .find {
                    homesFile.getString("$it.lastKnownName").equals(targetName, true)
                }
                ?.let { homesFile.getLocation("$it.guest") }

            if (home == null) player.sendPrefixedMessage(messages.noGuestHomeSetOrUnknownPlayer(targetName))
            else player.scheduleTeleport(home)
        }
    }

    private fun setHome(player: Player, guest: Boolean) {
        if (guest) {
            if (!player.hasPermission("rhomes.set.guest")) player.sendPrefixedMessage(messages.noPermissionSetGuest)
            homesFile.set("${player.uniqueId}.guest", player.location)
            player.sendPrefixedMessage(messages.guestHomeSet)
        } else {
            if (!player.hasPermission("rhomes.set.own")) player.sendPrefixedMessage(messages.noPermissionSetOwn)
            homesFile.set("${player.uniqueId}.own", player.location)
            player.sendPrefixedMessage(messages.ownHomeSet)
        }

        homesFile.save()
    }

    private fun cancelTeleport(player: Player) {
        val task = teleportTasks[player] ?: return
        task.cancel()
        teleportTasks.remove(player)
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        val player = sender as? Player ?: run {
            sender.sendPrefixedMessage("${ChatColor.RED}Only players may use this command.")
            return true
        }

        when (args.size) {
            0 -> useSelf(player)
            1 -> when (val value = args[0]) {
                "set" -> setHome(player, false)
                "setguest" -> setHome(player, true)
                else -> useGuest(player, value)
            }
            else -> return false
        }

        return true
    }

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        alias: String,
        args: Array<out String>
    ): MutableList<String> {
        if (sender !is Player || args.size != 1) return mutableListOf()

        val possibilities = homesFile.getKeys(false).mapNotNull { homesFile.getString("$it.lastKnownName") }
            .plus("set").plus("setguest")

        return possibilities.filter { it.startsWith(args[0]) }.toMutableList()
    }

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        homesFile.set("${event.player.uniqueId}.lastKnownName", event.player.name)
        homesFile.save()
    }

    @EventHandler
    fun onPlayerQuit(event: PlayerQuitEvent) {
        cancelTeleport(event.player)
    }

    @EventHandler
    fun onPlayerMove(event: PlayerMoveEvent) {
        val to = event.to
        if (to != null) {
            val delta = event.from.subtract(to)

            if (
                delta.x == 0.0 &&
                delta.y == 0.0 &&
                delta.z == 0.0
            ) return
        }

        if (teleportTasks.containsKey(event.player)) {
            cancelTeleport(event.player)
            event.player.sendPrefixedMessage(messages.cancelledMoved)
        }
    }

    @EventHandler
    fun onPlayerDamage(event: EntityDamageEvent) {
        val player = event.entity
        if (player is Player && teleportTasks.containsKey(player)) {
            cancelTeleport(player)
            player.sendPrefixedMessage(messages.cancelledDamage)
        }
    }
}

val plugin: RHomesPlugin get() = RHomesPlugin.instance
