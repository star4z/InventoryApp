package edu.sunypoly.inventoryapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.vision.barcode.Barcode
import edu.sunypoly.inventoryapp.barcode_reader.BarcodeReaderActivity


class BarcodeActivity: AppCompatActivity(), View.OnClickListener {
    private val BARCODE_READER_ACTIVITY_REQUEST = 1208
    private var mTvResult: TextView? = null
    private var mTvResultHeader: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_barcode)
        findViewById<Button>(R.id.btn_activity).setOnClickListener(this)
        mTvResult = findViewById(R.id.tv_result)
        mTvResultHeader = findViewById(R.id.tv_result_head)
    }

    override fun onClick(v: View) {
        when (v.getId()) {
            R.id.btn_activity -> {
                launchBarCodeActivity()
            }
        }
    }


    private fun launchBarCodeActivity() {
        val launchIntent = BarcodeReaderActivity.getLaunchIntent(this, true, false)
        startActivityForResult(launchIntent, BARCODE_READER_ACTIVITY_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode != Activity.RESULT_OK) {
            Toast.makeText(this, "error in  scanning", Toast.LENGTH_SHORT).show()
            return
        }

        if (requestCode == BARCODE_READER_ACTIVITY_REQUEST && data != null) {
            val barcode = data.getParcelableExtra<Barcode>(BarcodeReaderActivity.KEY_CAPTURED_BARCODE)
            Toast.makeText(this, barcode!!.rawValue, Toast.LENGTH_SHORT).show()
            mTvResultHeader!!.text = "On Activity Result"
            mTvResult!!.setText(barcode.rawValue)
        }

    }
}