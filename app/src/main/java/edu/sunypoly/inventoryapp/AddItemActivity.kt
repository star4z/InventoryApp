@file:Suppress("DEPRECATION")

package edu.sunypoly.inventoryapp

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.vision.barcode.Barcode
import edu.sunypoly.inventoryapp.barcode_reader.BarcodeReaderActivity
import java.text.SimpleDateFormat
import java.time.Year
import java.util.*
import java.util.Calendar.*

/**
 * Handles adding and updating items in the server
 */
class AddItemActivity : AppCompatActivity() {

    private val tag = "AddItemActivity"

    //References to all the views where the fields are updated
    private lateinit var barcodeView: TextView
    private lateinit var qrCodeView: TextView
    private lateinit var nameView: EditText
    private lateinit var typeView: EditText
    private lateinit var serialView: EditText
    private lateinit var roomView: EditText
    private lateinit var brandView: EditText
    private lateinit var acquiredView: EditText

    private var authenticator: Authenticator? = null

    internal var id = -1 //Sending an item with id -1 to the server will prompt it to generate a new item

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_item)

        //Connects view data members with their corresponding views in the xml
        barcodeView = findViewById(R.id.barcode_display)
        qrCodeView = findViewById(R.id.qr_display)
        nameView = findViewById(R.id.editText)
        typeView = findViewById(R.id.editText2)
        serialView = findViewById(R.id.editText3)
        roomView = findViewById(R.id.editText4)
        brandView = findViewById(R.id.editText5)
        acquiredView = findViewById(R.id.editText6)

        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val calendar = GregorianCalendar()

        //If this activity is being used to update, rather than add an item, this stuff updates the fields with the appropriate values
        if (intent != null) {

            val extras = intent.extras
            if (extras != null) {

                Log.v(javaClass.simpleName, intent.extras!!.toString())
                id = extras.getInt(InventoryItem.ID)
                barcodeView.text = extras.getInt(InventoryItem.BARCODE).toString()
                qrCodeView.text = extras.getString(InventoryItem.QR)
                nameView.setText(extras.getString(InventoryItem.NAME))
                typeView.setText(extras.getString(InventoryItem.TYPE))
                serialView.setText(extras.getInt(InventoryItem.SERIAL).toString())
                roomView.setText(extras.getString(InventoryItem.ROOM))
                brandView.setText(extras.getString(InventoryItem.BRAND))
                acquiredView.setText(extras.getString(InventoryItem.ACQUIRED))
            } else {
                acquiredView.setText(dateFormat.format(calendar.time))
            }
        } else {
            acquiredView.setText(dateFormat.format(calendar.time))
        }

        authenticator = Authenticator.instance

        acquiredView.setOnClickListener {
            val date = GregorianCalendar()
            val onDateSetListener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                date.set(year, month, dayOfMonth)
                acquiredView.setText(dateFormat.format(date.time))
            }
            DatePickerDialog(this, onDateSetListener, date.get(YEAR), date.get(MONTH), date.get(DATE)).show()
        }
    }

    /**
     * Called when add button is pressed
     * @param view required parameter for methods which are referenced in xml
     */
    @Suppress("UNUSED_PARAMETER")
    fun onAdd(view: View) {

        //Error checking
        if (nameView.text.toString().isEmpty()) {
            Toast.makeText(this, "Item must at least have a name", Toast.LENGTH_LONG)
                    .show()
            return
        }

        //Get values from views
        val barcodeStr = barcodeView.text.toString()
        val qr = qrCodeView.text.toString()
        val name = nameView.text.toString()
        val type = typeView.text.toString()
        val serialStr = serialView.text.toString()
        val room = roomView.text.toString()
        val brand = brandView.text.toString()
        val acquired = acquiredView.text.toString()


        //parsing view values to the correct data type
        var barcode = -1
//        var serial = -1
        try {
            if (barcodeStr.isNotEmpty())
                barcode = Integer.valueOf(barcodeStr)
//            if (serialStr.isNotEmpty())
//                serial = Integer.valueOf(serialStr)
        } catch (e: NumberFormatException) {
            Toast.makeText(this, "Encountered a number field with a string in it.",
                    Toast.LENGTH_SHORT).show()
        }

        val item = InventoryItem(id, barcode, qr, name, type, serialStr, room, brand,
                acquired)


        //This doesn't usually show, but if things are slow, this shows up
        val dialog = ProgressDialog.show(this, "Contacting server...", "Adding item")

        authenticator!!.login("", "")

        val status = authenticator!!.addItem(item)

        dialog.dismiss()


        when (status) {
            AuthenticatorStatus.AddedItem -> Toast.makeText(this, "Added item.", Toast.LENGTH_LONG).show()
            AuthenticatorStatus.ServerError -> Toast.makeText(this, status.message, Toast.LENGTH_LONG).show()
            else -> Toast.makeText(this, "Could not add item.", Toast.LENGTH_LONG).show()
        }
    }

    private fun containsIllegalCharacter(item: InventoryItem): Boolean {
        val map = item.fields

        val ILLEGAL_CHARACTERS = charArrayOf('{', '}', '[', ']', '/', '\\', '#', ':', ',', '?', '&', '=', '<', '>', '(', ')', '*', '^', '!', '~', '-', '|', ';', '%')

        for (key in map.keys) {
            if (key != InventoryItem.ID && key != InventoryItem.BARCODE &&
                    key != InventoryItem.SERIAL) {
                for (c in map[key]!!.toCharArray()) {
                    for (d in ILLEGAL_CHARACTERS) {
                        if (c == d) {
                            Toast.makeText(this, "Field " + key + " cannot contain " +
                                    d, Toast.LENGTH_LONG).show()
                            Log.v(javaClass.simpleName, "Field " + key + " cannot contain " +
                                    d + "")
                            return true
                        }
                    }
                }
            }
        }

        return false
    }

    val qr_request = 1201
    val barcode_request = 1202

    fun getQRCode(v: View) {
        launchBarcodeActivity(v, qr_request)
    }

    fun getBarcode(v: View) {
        launchBarcodeActivity(v, barcode_request)
    }

    @Suppress("UNUSED_PARAMETER")
    fun launchBarcodeActivity(v: View, requestCode: Int) {
        val launchIntent = BarcodeReaderActivity.getLaunchIntent(this, true, false)
        startActivityForResult(launchIntent, requestCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode != Activity.RESULT_OK) {
            Toast.makeText(this, "error in  scanning", Toast.LENGTH_SHORT).show()
            return
        }

        if (data != null) {
            val barcode = data.getParcelableExtra<Barcode>(BarcodeReaderActivity.KEY_CAPTURED_BARCODE)
            val out = barcode?.rawValue
            Log.d(tag, "barcode=${out}")
            when(requestCode) {
                qr_request -> {
                    qrCodeView.text = out
                }
                barcode_request -> {
                    barcodeView.text = out
                }
            }
        }
    }
}
