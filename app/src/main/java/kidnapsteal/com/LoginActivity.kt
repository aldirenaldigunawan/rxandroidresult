package kidnapsteal.com

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)


        done.setOnClickListener {
            val user = editText.text.toString()
            finishWithUser(user)
        }
    }

    private fun finishWithUser(user: String) {
        val data = Intent().putExtra("user", user)
        setResult(Activity.RESULT_OK, data)
        finish()
    }

    override fun onBackPressed() {
        setResult(Activity.RESULT_CANCELED, null)
        super.onBackPressed()
    }
}