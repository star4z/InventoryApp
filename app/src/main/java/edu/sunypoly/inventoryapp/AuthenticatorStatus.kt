package edu.sunypoly.inventoryapp

import java.util.ArrayList
import java.util.Objects

/**
 * A special return type used by Authenticator. Used for passing data.
 */
open class AuthenticatorStatus {

    internal var message: String
    private var statusId: Long = 0 //The status id needs to be the same for two Statuses to be considered the same status.

    @Suppress("unused")
    constructor(message: String) {
        this.message = message
        statusId = System.currentTimeMillis()
    }

    protected constructor(message: String, id: Long) {
        this.message = message
        statusId = id
    }

    /**
     * Used to pass list of items in addition to message.
     */
    internal class ListStatus(message: String, var data: ArrayList<InventoryItem>)//Uses the id from "GotItems" to make status checking easier
        : AuthenticatorStatus(message, GotItems.statusId)


    /**
     * Accepts sub-classes and only compared statusId's
     * @param other
     * @return
     */
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is AuthenticatorStatus) return false
        val status = other as AuthenticatorStatus?
        return statusId == status!!.statusId
    }

    override fun hashCode(): Int {
        return Objects.hash(statusId)
    }

    companion object {

        var GotItems = AuthenticatorStatus("Items retrieved successfully.", 1)

        var ServerError = AuthenticatorStatus("Could not connect to server.", 2)

        var NoItems = AuthenticatorStatus("Did not receive any items.", 3)

        var AddedItem = AuthenticatorStatus("Added item successfully.", 4)

        var DeletedItem = AuthenticatorStatus("Deleted item successfully.", 5)

        var AuthError = AuthenticatorStatus("User not logged in.", 6)
    }
}
