package kidnapsteal.com

import android.Manifest
import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import androidx.test.InstrumentationRegistry.getInstrumentation
import androidx.test.InstrumentationRegistry.getTargetContext
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.anyIntent
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.GrantPermissionRule
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiSelector
import org.hamcrest.Matchers.allOf
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain
import java.util.*

class MainActivityTest {

    private val permissionRule =
        GrantPermissionRule.grant(Manifest.permission.READ_CONTACTS, Manifest.permission.READ_EXTERNAL_STORAGE)

    private val activityRule = IntentsTestRule<MainActivity>(MainActivity::class.java)

    @get:Rule
    val rule = RuleChain.outerRule(permissionRule).around(activityRule)

    @Test
    fun should_show_snackbar_greeting_when_login_activity_return_result_ok() {
        val user = UUID.randomUUID().toString()
        intending(anyIntent()).respondWith(
            Instrumentation.ActivityResult(
                Activity.RESULT_OK,
                Intent().putExtra("user", user)
            )
        )

        onView(withId(R.id.buttonLogin)).perform(click())

        onView(withText("Login Success, Welcome $user")).check(matches(isDisplayed()))

        intended(allOf(hasComponent(LoginActivity::class.java.canonicalName)))
    }

    @Test
    fun should_show_snackbar__permission_required_if_permission_dialog_denied() {
        activityRule.runOnUiThread {
            disableCameraPermission()
        }
        onView(withId(R.id.buttonRequestPermission)).perform(click())

        UiDevice.getInstance(getInstrumentation())
            .findObject(
                UiSelector().clickable(true).checkable(false).index(0)
            ).let {
                if (it.waitForExists(3000L)) {
                    it.click()
                    it.waitUntilGone(0L)
                }
            }

        onView(withText(MainPresenter.PermissionRequiredMessage.Camera.message)).check(matches(isDisplayed()))
    }

    @Test
    fun should_show_all_permission_granted__when_permission_is_granted() {
        activityRule.runOnUiThread {
            disableCameraPermission()
        }
        onView(withId(R.id.buttonRequestPermission)).perform(click())

        UiDevice.getInstance(getInstrumentation())
            .findObject(
                UiSelector().clickable(true).checkable(false).index(1)
            ).let {
                if (it.waitForExists(3000L)) {
                    it.click()
                    it.waitUntilGone(0L)
                }
            }

        onView(withText("All Permission Granted")).check(matches(isDisplayed()))
    }

    private fun disableCameraPermission() {
        InstrumentationRegistry.getInstrumentation().uiAutomation.revokeRuntimePermission(
            getTargetContext().packageName,
            Manifest.permission.CAMERA
        )
    }
}