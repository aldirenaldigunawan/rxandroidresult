package kidnapsteal.com

import com.kidnapsteal.ActivityResult
import com.kidnapsteal.Permission
import com.kidnapsteal.PermissionResult
import com.kidnapsteal.RxPermission
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

class MainPresenter(
    private val view: MainView,
    private val navigator: Navigator,
    private val rxPermission: RxPermission,
    private val uiScheduler: Scheduler
) {

    private val disposer = CompositeDisposable()

    fun onLoginClick() {
        navigator.openLoginScreen(LOGIN_REQUEST_CODE)
            .filter { it.getResultStatus() == ActivityResult.ResultStatus.Ok }
            .observeOn(uiScheduler)
            .subscribe(::onLoginResult) {
                println("error :${it.message}")
            }
            .addToDisposer()
    }

    fun requestPermission() {
        rxPermission.checkAndRequest(
            Permission.Camera,
            Permission.ReadContact,
            Permission.ReadStorage
        ).subscribe(::handlePermissionResult)
            .addToDisposer()
    }

    private fun handlePermissionResult(permissionResult: List<PermissionResult>) {
        val denied = permissionResult.find { it is PermissionResult.Denied }
        if (denied != null && denied.permissions.isNotEmpty()) {
            val requiredPermission = PermissionRequiredMessage.from(denied.permissions.first())
            view.showRequirePermission(requiredPermission.message)
        } else {
            view.showAllComplete()
        }
    }

    private fun onLoginResult(result: ActivityResult) {
        val user = result.intent.getStringExtra("user")
        user?.let { view.greetingUser(user) }
    }

    private fun Disposable.addToDisposer() {
        disposer.add(this)
    }

    companion object {
        const val LOGIN_REQUEST_CODE = 0x99
    }

    sealed class PermissionRequiredMessage(val message: String) {
        object Camera : PermissionRequiredMessage("Need to access camera")
        object ReadStorage : PermissionRequiredMessage("Need to access storage")
        object ReadContact : PermissionRequiredMessage("Need to access contact")
        object Location : PermissionRequiredMessage("Need to access location")

        companion object {
            fun from(permission: Permission): PermissionRequiredMessage {
                return when (permission) {
                    is Permission.Camera -> Camera
                    is Permission.ReadStorage -> ReadStorage
                    is Permission.ReadContact -> ReadContact
                    is Permission.Location -> Location
                    else -> throw IllegalArgumentException("Unsupported Permission")
                }
            }
        }
    }
}