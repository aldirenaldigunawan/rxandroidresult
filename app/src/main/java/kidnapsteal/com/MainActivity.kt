package kidnapsteal.com

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kidnapsteal.RxPermissionImpl
import io.reactivex.android.schedulers.AndroidSchedulers
import kidnapsteal.com.Util.message
import kotlinx.android.synthetic.main.activity_sample.*


class MainActivity : AppCompatActivity(), MainView {

    private val navigator by lazy { NavigatorImpl(this) }
    private val presenter by lazy {
        MainPresenter(
            this,
            navigator,
            RxPermissionImpl(this),
            AndroidSchedulers.mainThread()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sample)

        buttonLogin.setOnClickListener { presenter.onLoginClick() }
        buttonRequestPermission.setOnClickListener { presenter.requestPermission() }
    }

    override fun greetingUser(userName: String) {
        message("Login Success, Welcome $userName")
        userView.text = userName
    }

    override fun showRequirePermission(message: String) {
        message(message)
    }

    override fun showAllComplete() {
        message("All Permission Granted")
    }
}


