package kidnapsteal.com

import android.content.Intent
import androidx.fragment.app.FragmentActivity
import com.kidnapsteal.ActivityResult
import com.kidnapsteal.RxActivityResult
import com.kidnapsteal.RxActivityResultImpl
import io.reactivex.Observable

interface Navigator {
    fun openLoginScreen(requestCode: Int): Observable<ActivityResult>
    fun listenTo(requestCode: Int): Observable<ActivityResult>
}

class NavigatorImpl(
    private val activity: FragmentActivity,
    private val rxActivityResult: RxActivityResult = RxActivityResultImpl(activity)
) : Navigator {

    override fun openLoginScreen(requestCode: Int): Observable<ActivityResult> {
        val intent = Intent(activity, LoginActivity::class.java)
        return rxActivityResult.startActivity(intent, requestCode)
    }

    override fun listenTo(requestCode: Int): Observable<ActivityResult> {
        return rxActivityResult.listenToResult(requestCode)
    }

}