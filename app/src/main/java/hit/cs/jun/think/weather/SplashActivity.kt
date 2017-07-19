package hit.cs.jun.think.weather

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import hit.cs.jun.think.weather.Bean.cloud
import hit.cs.jun.think.weather.HTTPAround.MyTemplateObserver
import hit.cs.jun.think.weather.Interface.CloudAPI
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class SplashActivity : AppCompatActivity() {

    protected val CLOUD_BASE_URL = "https://api.seniverse.com/v3/"
    protected var cloudAPI: CloudAPI = Retrofit.Builder()
            .baseUrl(CLOUD_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build().create(CloudAPI::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        cloudAPI.getCities()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : MyTemplateObserver<cloud>() {
                    override fun onNext(t: cloud) {
                        if (t.results.isEmpty()) return

                        startActivity(Intent(baseContext, MainActivity::class.java).putExtra("weatherData", t))
                        finish()
                    }
                })
    }
}
