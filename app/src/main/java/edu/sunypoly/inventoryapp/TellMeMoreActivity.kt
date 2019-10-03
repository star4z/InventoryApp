package edu.sunypoly.inventoryapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

import java.io.IOException
import java.util.*

/**
 * Lists all the fields of an InventoryItem
 */
class TellMeMoreActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tell_me_more)

        val extras = intent.extras
        if (extras != null) {
            var item: InventoryItem? = null
            try {
                item = InventoryItem.fromByteArray(extras.getByteArray("item"))
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: ClassNotFoundException) {
                e.printStackTrace()
            }

            val textView = findViewById<TextView>(R.id.root_view)
            assert(item != null)
            val hashMap = item!!.fields
            val stringBuilder = StringBuilder()
            for (key in hashMap.keys) {
                stringBuilder.append(key[0])
                stringBuilder.append(key.substring(1).toLowerCase(Locale.getDefault()))
                stringBuilder.append(": ")
                stringBuilder.append(hashMap[key])
                stringBuilder.append("\n")
            }
            textView.text = stringBuilder.toString()
        }
    }
}
