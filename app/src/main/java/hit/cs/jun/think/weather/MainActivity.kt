package hit.cs.jun.think.weather

import android.content.Intent
import android.support.v7.widget.GridLayoutManager
import android.widget.TextView
import android.widget.Toast
import com.lljjcoder.citylist.CityListSelectActivity
import com.lljjcoder.citylist.bean.CityInfoBean
import hit.cs.jun.think.weather.Adapter.BaseAdapter
import hit.cs.jun.think.weather.Bean.City
import hit.cs.jun.think.weather.Bean.Weather
import hit.cs.jun.think.weather.Bean.cloud
import hit.cs.jun.think.weather.Bean.now
import hit.cs.jun.think.weather.HTTPAround.MyTemplateObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity(override val layoutId: Int = R.layout.activity_main) : BaseActivity() {

    private lateinit var weatherAdapter: BaseAdapter<Weather>
    private lateinit var now: City

    override fun init() {

        weatherAdapter = BaseAdapter(R.layout.recycler_weather) { view, item ->
            view.findViewById<TextView>(R.id.weather_data).text = item.data
            view.findViewById<TextView>(R.id.weather_item).text = item.item
        }

        recycler_view.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = weatherAdapter
        }

        location_name.setOnClickListener {
            val intent = Intent(this, CityListSelectActivity::class.java)
            startActivityForResult(intent, CityListSelectActivity.CITY_SELECT_RESULT_FRAG);
        }

        weather_share.setOnClickListener {
            shareToAnyWhere()
        }

        date_plan.setOnClickListener {
            startActivity(Intent(this, CalendarActivity::class.java))
        }

        now = (intent.getSerializableExtra("weatherData") as cloud).results[0]

        InitUI(now)
    }

    private fun shareToAnyWhere() {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_SUBJECT, "分享")
        intent.putExtra(Intent.EXTRA_TEXT, "${now.location.name} 温度:${now.now.temperature} 湿度:${now.now.humidity} 气压:${now.now.pressure} 风力等级:${now.now.wind_scale}")
        intent.putExtra(Intent.EXTRA_TITLE, "天气信息")
        startActivity(Intent.createChooser(intent, "请选择"))
    }

    private fun InitUI(city: City) {
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CityListSelectActivity.CITY_SELECT_RESULT_FRAG) {
            if (resultCode == RESULT_OK) {
                if (data == null) {
                    return;
                }

                val cityInfoBean = data.extras.getParcelable<CityInfoBean>("cityinfo") ?: return

                cloudAPI.getCities(location = cityInfoBean.name)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(object : MyTemplateObserver<cloud>() {

                            override fun onError(e: Throwable) {
                                Toast.makeText(baseContext, "API暂不支持此地区", Toast.LENGTH_SHORT).show()
                            }

                            override fun onNext(t: cloud) {
                                now = t.results[0]
                                InitUI(now)
                            }
                        })
            }
        }
    }
}
