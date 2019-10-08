package edu.sunypoly.inventoryapp

import android.os.Bundle

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.Serializable
import java.util.HashMap

/**
 * Stores inventory item data
 * Used by GSON to convert between JSONs and Objects.
 */
class InventoryItem(//        fieldsMap.put(ID, "" + id);
        var id: Int,//        fieldsMap.put(BARCODE, "" + barcode);
        var barcode: Int,//        fieldsMap.put(QR, qr);
        var qr: String,//        fieldsMap.put(NAME, name);
        var name: String,//        fieldsMap.put(TYPE, type);
        var type: String,//        fieldsMap.put(SERIAL, "" + serial);
        var serial: String,//        fieldsMap.put(ROOM, room);
        var room: String,//        fieldsMap.put(BRAND, brand);
        var brand: String,//        fieldsMap.put(ACQUIRED, acquired);
        var acquired: String) : Serializable {

    /**
     * Returns a HashMap of all the id keys
     * @return
     */
    internal val fields: HashMap<String, String>
        get() = object : HashMap<String, String>() {
            init {
                put(ID, "" + id)
                put(BARCODE, "" + barcode)
                put(QR, qr)
                put(NAME, name)
                put(TYPE, type)
                put(SERIAL, "" + serial)
                put(ROOM, room)
                put(BRAND, brand)
                put(ACQUIRED, acquired)
            }
        }

    override fun toString(): String {
        return "InventoryItem{" +
                "id=" + id +
                ", barcode=" + barcode +
                ", qr='" + qr + '\''.toString() +
                ", name='" + name + '\''.toString() +
                ", type='" + type + '\''.toString() +
                ", serial=" + serial +
                ", room='" + room + '\''.toString() +
                ", brand='" + brand + '\''.toString() +
                ", acquired='" + acquired + '\''.toString() +
                '}'.toString()
    }

    /**
     * Returns the item as a Bundle for passing it through Intents, etc
     * (Alternate method to the byte[] stuff below)
     * @return Bundle
     */
    fun toBundle(): Bundle {
        val bundle = Bundle()

        bundle.putInt(ID, id)
        bundle.putInt(BARCODE, barcode)
        bundle.putString(QR, qr)
        bundle.putString(NAME, name)
        bundle.putString(TYPE, type)
        bundle.putString(SERIAL, serial)
        bundle.putString(ROOM, room)
        bundle.putString(BRAND, brand)
        bundle.putString(ACQUIRED, acquired)

        return bundle
    }

    /**
     * Used to convert the object into a form that can be passed through an Intent
     * @return
     * @throws IOException
     */
    @Throws(IOException::class)
    internal fun toByteArray(): ByteArray {
        ByteArrayOutputStream().use { bos ->
            ObjectOutputStream(bos).use { out ->
                out.writeObject(this)
                return bos.toByteArray()
            }
        }
    }

    /**
     * Used by SearchActivity to nicely check if this item contains the specified query.
     * @param s
     * @return
     */
    internal operator fun contains(s: String): Boolean {
        return (id.toString() + "" + barcode + qr + name + type + serial + room + brand + acquired).contains(s)
    }

    companion object {

        const val ID = "id"
        const val BARCODE = "barcode"
        const val QR = "qr"
        const val NAME = "name"
        const val TYPE = "type"
        const val SERIAL = "serial"
        const val ROOM = "room"
        const val BRAND = "brand"
        const val ACQUIRED = "acquired"

        /**
         * Used to convert the item back from a byte[] after being received from an intent
         * @param bytes
         * @return
         * @throws IOException
         * @throws ClassNotFoundException
         */
        @Throws(IOException::class, ClassNotFoundException::class)
        internal fun fromByteArray(bytes: ByteArray): InventoryItem {
            ByteArrayInputStream(bytes).use { bis -> ObjectInputStream(bis).use { `in` -> return `in`.readObject() as InventoryItem } }
        }
    }
}
