package de.moritzruth.rhomes.messages

import org.bukkit.ChatColor

object GermanMessages : Messages {
    override val noPermissionUseOwn: String = "${ChatColor.RED}Du hast keine Berechtigung, zu deinem Home zu gehen."
    override val noPermissionUseGuest: String =
        "${ChatColor.RED}Du hast keine Berechtigung, zum Gäste-Home eines Spielers zu gehen."

    override val noPermissionSetGuest: String = "${ChatColor.RED}Du hast keine Berechtigung, dein Gäste-Home zu setzen."
    override val noPermissionSetOwn: String = "${ChatColor.RED}Du hast keine Berechtigung, dein Home zu setzen."

    override val noOwnHomeSet: String =
        "${ChatColor.RED}Du musst dein Home erst mit ${ChatColor.WHITE}/h set ${ChatColor.RED}setzen, bevor du" +
                "diesen Befehl benutzen kannst."

    override val ownHomeSet: String = "${ChatColor.GREEN}Dein Home wurde gesetzt."
    override val guestHomeSet: String = "${ChatColor.GREEN}Dein Gäste-Home wurde gesetzt."
    override val cancelledMoved: String = "${ChatColor.RED}Teleportierung wurde abgebrochen, da du dich bewegt hast."
    override val cancelledDamage: String =
        "${ChatColor.RED}Teleportierung wurde abgebrochen, da du Schaden erlitten hast."

    override fun willTeleportIn(seconds: Int): String = "${ChatColor.GREEN}Du wirst in " +
            "${ChatColor.WHITE}${Messages.simplePluralize(seconds, "Sekunde", "Sekunden")} " +
            "${ChatColor.GREEN}teleportiert."

    override fun noGuestHomeSetOrUnknownPlayer(target: String): String =
        "${ChatColor.WHITE}$target ${ChatColor.RED}hat diesen Server noch nie betreten oder noch kein Gäste-Home gesetzt."
}
