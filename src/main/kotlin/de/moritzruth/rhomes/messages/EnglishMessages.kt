package de.moritzruth.rhomes.messages

import org.bukkit.ChatColor

object EnglishMessages: Messages {
    override val noPermissionUseOwn: String = "${ChatColor.RED}You do not have the permission to go to your home."
    override val noPermissionUseGuest: String =
        "${ChatColor.RED}You do not have the permission to go to the home of another player."

    override val noPermissionSetGuest: String = "${ChatColor.RED}You do not have the permission to set your home."
    override val noPermissionSetOwn: String = "${ChatColor.RED}You do not have the permission to set your guest home."

    override val noOwnHomeSet: String =
        "${ChatColor.RED}You must set your own home using ${ChatColor.WHITE}/h set ${ChatColor.RED}before you can use" +
                "this command."

    override val ownHomeSet: String = "${ChatColor.GREEN}Your home was set."
    override val guestHomeSet: String = "${ChatColor.GREEN}Your guest home was set."
    override val cancelledMoved: String = "${ChatColor.RED}Teleportation was cancelled because you moved."
    override val cancelledDamage: String = "${ChatColor.RED}Teleportation was cancelled because you took damage."

    override fun willTeleportIn(seconds: Int): String =
        "${ChatColor.GREEN}You will be teleported in " +
                "${ChatColor.WHITE}${Messages.simplePluralize(seconds, "second")}${ChatColor.WHITE}."

    override fun noGuestHomeSetOrUnknownPlayer(target: String): String =
        "${ChatColor.WHITE}$target ${ChatColor.RED}never joined this server before or has not set a guest home yet."
}
