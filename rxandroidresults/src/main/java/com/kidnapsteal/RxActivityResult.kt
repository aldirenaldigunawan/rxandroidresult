package com.kidnapsteal


import android.content.Intent
import io.reactivex.Observable

interface RxActivityResult {
    fun startActivityForResult(intent: Intent, requestCode: Int): Observable<ActivityResult>
    fun startActivity(intent: Intent, requestCode: Int): Observable<ActivityResult>
    fun listenToResult(requestCode: Int): Observable<ActivityResult>
}