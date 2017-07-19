package hit.cs.jun.think.weather

import android.support.v7.widget.GridLayoutManager
import android.widget.TextView
import hit.cs.jun.think.weather.Adapter.BaseAdapter
import hit.cs.jun.think.weather.Bean.City
import hit.cs.jun.think.weather.Bean.Weather
import hit.cs.jun.think.weather.Bean.cloud
import hit.cs.jun.think.weather.Bean.now
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity(override val layoutId: Int = R.layout.activity_main) : BaseActivity() {

    private lateinit var weatherAdapter: BaseAdapter<Weather>

    override fun init() {

        weatherAdapter = BaseAdapter(R.layout.recycler_weather) { view, item ->
            view.findViewById<TextView>(R.id.weather_data).text = item.data
            view.findViewById<TextView>(R.id.weather_item).text = item.item
        }

        recycler_view.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = weatherAdapter
        }


        val city: City = (intent.getSerializableExtra("weatherData") as cloud).results[0]
        location_name.text = city.location.name
        city_last_update.text = "最后更新\n${city.last_update}"

        val now: now = city.now
        now_temperature.text = " ${now.temperature}°"

        val mData = mutableListOf<Weather>(
                Weather(0, now.wind_direction, "风向"),
                Weather(0, now.wind_scale, "风力等级"),
                Weather(0, now.wind_direction_degree, "风向角度"),
                Weather(0, now.wind_speed, "风速"),
                Weather(0, now.pressure, "气压"),
                Weather(0, now.humidity, "相对湿度"),
                Weather(0, now.visibility, "能见度"),
                Weather(0, now.feels_like, "体感温度")
        )

        weatherAdapter.resetItems(mData)

        text_left.text = "${now.humidity}%"
        text_middle.text = "较舒适"
        text_right.text = "68/良"
    }
}
