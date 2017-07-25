package hit.cs.jun.think.weather.Bean

import io.realm.RealmObject
import java.util.*

/**
 * Project ThinkWeather.
 * Created by 旭 on 2017/7/25.
 */
open class Plan(var id: Long = 0, var date: Date? = null, var things: String = "") : RealmObject()