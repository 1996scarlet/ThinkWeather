package hit.cs.jun.think.weather.Bean

import java.io.Serializable

class now(var text: String,
          var code: String,
          var temperature: String,
          var feels_like: String,
          var pressure: String,
          var humidity: String,
          var visibility: String,
          var wind_direction: String,
          var wind_direction_degree: String,
          var wind_speed: String,
          var wind_scale: String,
          var clouds: String,
          var dew_point: String) : Serializable