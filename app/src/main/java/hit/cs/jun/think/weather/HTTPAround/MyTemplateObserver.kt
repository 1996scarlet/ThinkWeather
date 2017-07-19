package hit.cs.jun.think.weather.HTTPAround

import android.util.Log

import io.reactivex.Observer
import io.reactivex.disposables.Disposable

open class MyTemplateObserver<T> : Observer<T> {

    override fun onSubscribe(d: Disposable) {}

    override fun onNext(t: T) {}

    override fun onError(e: Throwable) {
        Log.e(TAG, "onError: ", e)
    }

    override fun onComplete() {}

    companion object {
        private val TAG = "MyTemplateObserver"
    }
}
