package com.kidnapsteal

import android.app.Activity
import android.content.Intent

data class ActivityResult(
        val requestCode: Int, val intent: Intent,
        val resultCode: Int = -1
) {
    fun getResultStatus(): ResultStatus {
        return when (resultCode) {
            Activity.RESULT_OK -> ResultStatus.Ok
            else -> ResultStatus.Cancel
        }
    }

    enum class ResultStatus {
        Ok, Cancel,
    }
}