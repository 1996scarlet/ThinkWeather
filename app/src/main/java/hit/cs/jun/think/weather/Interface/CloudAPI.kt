package hit.cs.jun.think.weather.Interface

import hit.cs.jun.think.weather.Bean.cloud
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface CloudAPI {

    @GET("weather/now.json")
    fun getCities(@Query("key") key: String = "2ciyyaw7qladbkae",
                  @Query("location") location: String = "哈尔滨"): Observable<cloud>

    //------------------------------------------------------------------------------//
}