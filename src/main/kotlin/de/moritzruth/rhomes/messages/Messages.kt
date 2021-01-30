package de.moritzruth.rhomes.messages

interface Messages {
    val noPermissionUseOwn: String
    val noPermissionUseGuest: String
    val noPermissionSetGuest: String
    val noPermissionSetOwn: String
    val noOwnHomeSet: String
    val ownHomeSet: String
    val guestHomeSet: String
    val cancelledMoved: String
    val cancelledDamage: String

    fun willTeleportIn(seconds: Int): String
    fun noGuestHomeSetOrUnknownPlayer(target: String): String

    companion object {
        fun simplePluralize(
            amount: Int,
            singular: String,
            plural: String = singular + "s",
            prependAmount: Boolean = true
        ): String {
            val word = if (amount == 1) singular else plural

            return if (prependAmount) "$amount $word" else word
        }
    }
}
