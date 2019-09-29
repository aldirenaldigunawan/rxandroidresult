package kidnapsteal.com

import android.content.Context
import android.view.View
import androidx.fragment.app.FragmentActivity
import com.google.android.material.snackbar.Snackbar

object Util {
    fun Context.message(message: String) {
        val snakeBar = Snackbar.make(
            (this as FragmentActivity).findViewById<View>(android.R.id.content),
            message,
            Snackbar.LENGTH_LONG
        )
        snakeBar.show()
    }


}