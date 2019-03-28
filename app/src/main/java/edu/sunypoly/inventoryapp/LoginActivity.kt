package edu.sunypoly.inventoryapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast

class LoginActivity : AppCompatActivity() {

    val TAG = this.javaClass.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    fun login(view: View) {
        val u = findViewById<EditText>(R.id.username_field)
        val p = findViewById<EditText>(R.id.password_field)

        val usr = u.text.toString()
        val pwd = p.text.toString()

        val authenticator = Authenticator.getInstance()
        val loginSuccessful = authenticator.login(usr, pwd)

        Log.d(TAG, "Login attempted. Result: $loginSuccessful")

        if (loginSuccessful) {
            setResult(RESULT_OK)
            finish()
        } else {
            Toast.makeText(this, "Login failed.", Toast.LENGTH_LONG).show()
        }
    }
}
