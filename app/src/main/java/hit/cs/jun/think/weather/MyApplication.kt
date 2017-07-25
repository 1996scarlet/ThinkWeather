package hit.cs.jun.think.weather

import android.app.Application
import io.realm.Realm

/**
 * Project ThinkWeather.
 * Created by 旭 on 2017/7/25.
 */
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
    }
}