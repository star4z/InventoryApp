package edu.sunypoly.inventoryapp;

import java.util.ArrayList;
import java.util.Objects;

/**
 * A special return type used by Authenticator. Used for passing data.
 */
public class AuthenticatorStatus {

    String message;
    private long statusId; //The status id needs to be the same for two Statuses to be considered the same status.

    public AuthenticatorStatus(String message) {
        this.message = message;
        statusId = System.currentTimeMillis();
    }

    protected AuthenticatorStatus(String message, long id){
        this.message = message;
        statusId = id;
    }

    public static AuthenticatorStatus GotItems = new AuthenticatorStatus("Items retrieved successfully.", 1);

    public static AuthenticatorStatus ServerError = new AuthenticatorStatus("Could not connect to server.", 2);

    public static AuthenticatorStatus NoItems = new AuthenticatorStatus("Did not receive any items.", 3);

    public static AuthenticatorStatus AddedItem = new AuthenticatorStatus("Added item successfully.", 4);

    public static AuthenticatorStatus DeletedItem = new AuthenticatorStatus("Deleted item successfully.", 5);

    public static AuthenticatorStatus AuthError = new AuthenticatorStatus("User not logged in.", 6);

    /**
     * Used to pass list of items in addition to message.
     */
    static class ListStatus extends AuthenticatorStatus{
        ArrayList<InventoryItem> data;

        public ListStatus(String message, ArrayList<InventoryItem> data) {

            super(message, GotItems.statusId); //Uses the id from "GotItems" to make status checking easier
            this.data = data;
        }
    }


    /**
     * Accepts sub-classes and only compared statusId's
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AuthenticatorStatus)) return false;
        AuthenticatorStatus status = (AuthenticatorStatus) o;
        return statusId == status.statusId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(statusId);
    }
}
