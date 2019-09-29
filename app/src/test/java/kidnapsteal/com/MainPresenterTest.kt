package kidnapsteal.com

import android.content.Intent
import com.kidnapsteal.ActivityResult
import com.kidnapsteal.Permission
import com.kidnapsteal.PermissionResult
import com.kidnapsteal.RxPermission
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyZeroInteractions
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import kidnapsteal.com.MainPresenter.Companion.LOGIN_REQUEST_CODE
import org.junit.Before
import org.junit.Test
import java.util.*

class MainPresenterTest {

    private lateinit var view: MainView
    private lateinit var presenter: MainPresenter
    private lateinit var navigator: Navigator
    private lateinit var permission: RxPermission

    @Before
    fun setup() {
        view = mock()
        navigator = mock()
        permission = mock()
        presenter = MainPresenter(view, navigator, permission, Schedulers.trampoline())
    }

    @Test
    fun onLoginClick__redirect_user_to_login_screen() {
        val result = ActivityResult(LOGIN_REQUEST_CODE, getIntentResult(null))
        val user = UUID.randomUUID().toString()

        val noUserResult = Observable.just(result)
        val cancelResult = Observable.just(result.copy(resultCode = 0))
        val okResultWithData = Observable.just(result.copy(intent = getIntentResult(user)))

        whenever(navigator.openLoginScreen(LOGIN_REQUEST_CODE)).thenReturn(noUserResult, cancelResult, okResultWithData)


        // given result is Ok but no userName should not trigger greetingUser
        presenter.onLoginClick()

        verify(navigator).openLoginScreen(LOGIN_REQUEST_CODE)
        verifyZeroInteractions(view)

        //given result cancel, should do nothing
        presenter.onLoginClick()
        verifyZeroInteractions(view)

        //given result ok with userName, should greetingUser
        presenter.onLoginClick()
        verify(view).greetingUser(user)
    }

    @Test
    fun given_all_permissin_granted__should_show_all_complete() {
        val permissionsRequest = listOf(Permission.Camera, Permission.ReadContact, Permission.ReadStorage)

        val grantedPermission: List<PermissionResult> = listOf(PermissionResult.Granted(permissionsRequest))

        val allPermissionGranted = Observable.just(grantedPermission)

        whenever(permission.checkAndRequest(*permissionsRequest.toTypedArray())).thenReturn(allPermissionGranted)

        presenter.requestPermission()

        verify(view).showAllComplete()
    }

    @Test
    fun given_no_permission_granted__should_show_message_for_first_denied_permission() {
        val permissionsRequest = listOf(Permission.Camera, Permission.ReadContact, Permission.ReadStorage)

        val deniedPermission: List<PermissionResult> = listOf(PermissionResult.Denied(permissionsRequest))

        val noPermissionGranted = Observable.just(deniedPermission)

        whenever(permission.checkAndRequest(*permissionsRequest.toTypedArray())).thenReturn(noPermissionGranted)

        presenter.requestPermission()

        val messageCameraPermission = MainPresenter.PermissionRequiredMessage.from(Permission.Camera).message
        verify(view).showRequirePermission(messageCameraPermission)
    }

    @Test
    fun given_some_permission_denied__should_show_message_for_first_denied_permission() {
        val permissionsRequest = listOf(Permission.Camera, Permission.ReadContact, Permission.ReadStorage)

        val grantedPermission: List<PermissionResult> = listOf(PermissionResult.Granted(permissionsRequest))
        val deniedPermission: List<PermissionResult> = listOf(PermissionResult.Denied(permissionsRequest))

        val permissionResultListForSomeGranted = grantedPermission.subList(0, 1) + deniedPermission.last()

        val somePermissionDenied = Observable.just(permissionResultListForSomeGranted)


        whenever(permission.checkAndRequest(*permissionsRequest.toTypedArray())).thenReturn(somePermissionDenied)

        presenter.requestPermission()

        val firstDeniedPermission =
            permissionResultListForSomeGranted.first { it is PermissionResult.Denied }.permissions.first()
        val messageForSomePermissionGranted =
            MainPresenter.PermissionRequiredMessage.from(firstDeniedPermission).message

        verify(view).showRequirePermission(messageForSomePermissionGranted)
    }

    private fun getIntentResult(withUser: String? = null): Intent {
        return mock {
            on { getStringExtra("user") }.thenReturn(withUser)
        }
    }

}