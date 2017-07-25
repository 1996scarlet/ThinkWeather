package hit.cs.jun.think.weather

import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.widget.EditText
import android.widget.TextView
import com.vicpin.krealmextensions.create
import com.vicpin.krealmextensions.delete
import com.vicpin.krealmextensions.query
import com.vicpin.krealmextensions.save
import hit.cs.jun.think.weather.Adapter.BaseAdapter
import hit.cs.jun.think.weather.Bean.Plan
import kotlinx.android.synthetic.main.activity_calendar.*
import java.util.*

class CalendarActivity(override val layoutId: Int = R.layout.activity_calendar) : BaseActivity() {

    private lateinit var planAdapter: BaseAdapter<Plan>
    private lateinit var pick: Date

    override fun init() {

        planAdapter = BaseAdapter(R.layout.recycler_weather) { view, item ->
            view.findViewById<TextView>(R.id.weather_data).text = item.things
            view.findViewById<TextView>(R.id.weather_item).text = "创建时间:${Date(item.id)}"
            view.setOnLongClickListener {

                Plan().delete { it.equalTo("id", item.id) }

                reQuery()
                true
            }

            view.setOnClickListener { showEditDialog(item) }
        }

        recycler_view.apply {
            layoutManager = LinearLayoutManager(this@CalendarActivity)
            adapter = planAdapter
        }

        val current = Date(System.currentTimeMillis())
        pick = Date(current.year + 1900, current.month, current.date)
        reQuery()

        calendar.setOnDateChangeListener { _, y, m, d ->
            pick = Date(y, m, d)
            reQuery()
        }

        add.setOnClickListener { showAddDialog() }
    }

    fun reQuery() {
        planAdapter.resetItems(Plan().query {
            it.equalTo("date", pick)
        }.toMutableList())
    }

    private fun showAddDialog() {
        val editText = EditText(this)
        AlertDialog.Builder(this).setPositiveButton("Add", { _, _ ->
            val find = editText.text
            if (!find.isNullOrBlank()) {
                Plan(System.currentTimeMillis(), pick, find.toString()).save()
                reQuery()
            }
        }).setView(editText).setTitle("Add Your Plan").show()
    }

    private fun showEditDialog(item: Plan) {
        val editText = EditText(this)
        AlertDialog.Builder(this).setPositiveButton("Edit", { _, _ ->
            val find = editText.text
            if (!find.isNullOrBlank()) {
                Plan().delete { it.equalTo("id", item.id) }
                item.things = find.toString()
                item.create()
                reQuery()
            }
        }).setView(editText).setTitle("Edit Your Plan").show()
    }

}
