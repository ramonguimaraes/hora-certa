package com.ramonguimaraes.horacerta.utils

import android.util.Log
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resumeWithException

@OptIn(ExperimentalCoroutinesApi::class)
suspend fun <T> Task<T>.myAwait(): T {
    return suspendCancellableCoroutine { cont ->
        addOnCompleteListener {
            if (it.exception != null) {
                Log.e("TESTE", "ERROU")
                cont.resumeWithException(it.exception!!)
            } else {
                Log.e("TESTE", "ELSE")
                cont.resume(it.result, null)
            }
        }
    }
}
