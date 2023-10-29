package com.example.weather_app
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SearchView
import com.example.weather_app.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

//api key e37b98e3a86353309288d5ea7497c99e
//https://api.openweathermap.org/data/2.5/weather?q=ahmedabad&appid=e37b98e3a86353309288d5ea7497c99e
class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private val TAG = "MainActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        fetchWeatherInfo("Ahmedabad")
        val searchView = binding.searchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                // task HERE
                if (!query.isNullOrBlank()) {
                    fetchWeatherInfo(query)
                }
                return true
            }
            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
    }
    private fun fetchWeatherInfo(cityname :String){
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .build().create(ApiInterface::class.java)
        val response = retrofit.getWeatherData("$cityname","e37b98e3a86353309288d5ea7497c99e","metric")
        response.enqueue(object : Callback<WeatherApp>{
            override fun onResponse(call: Call<WeatherApp>, response: Response<WeatherApp>) {
                val responseBody  = response.body()
                if(response.isSuccessful && responseBody != null){
                    val temperature  =responseBody.main.temp.toString()
                    val humidity = responseBody.main.humidity
                    val windspeed = responseBody.wind.speed
                    val sunrise = responseBody.sys.sunrise.toLong()
                    val sunset  = responseBody.sys.sunset.toLong()
                    val sealevel = responseBody.main.pressure
                    val conditon  =responseBody.weather.firstOrNull()?.main?: "unkown"
                    val maxtemp  =responseBody.main.temp_max
                    val mintemp = responseBody.main.temp_min

                    binding.weather.text = conditon
                    binding.maxTemp.text = "Max Temp : $maxtemp °C"
                    binding.minTemp.text = "MinTemp : $mintemp °C"
                    binding.temp.text= "$temperature °C"
                    binding.humidity.text = "$humidity %"
                    binding.windspeed.text = "$windspeed m/s"
                    binding.sunrise.text = "${time(sunrise)}"
                    binding.sunset.text = "${time(sunset)}"
                    binding.sea.text = "$sealevel hPa"
                    binding.condition.text = conditon
                    binding.day.text = dayName(System.currentTimeMillis())
                    binding.date.text = date()
                    binding.cityName.text = "$cityname"
                    //Log.d("TAG", "onResponse: $temperature")\

                    ChangeImgToWeatherbyCondition(conditon)
                }
            }

            override fun onFailure(call: Call<WeatherApp>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }
    private fun ChangeImgToWeatherbyCondition(conditions : String) {
        when (conditions){
            "Clear Sky", "Sunny", "Clear" ->{
                binding.root.setBackgroundResource(R.drawable.sunny_background)
                binding.lottieAnimationView.setAnimation(R.raw.sunny)
            }
            "Clouds", "Party Clouds", "Overcast", "Haze" ->{
                binding.root.setBackgroundResource(R.drawable.colud_background)
                binding.lottieAnimationView.setAnimation(R.raw.cloud)
            }

            "Rain","RAIN","Light Rain", "Drizzle", "Moderate Rain", "Showers", "Heavy Rain" ->{
                binding.root.setBackgroundResource(R.drawable.rain_background)
                binding.lottieAnimationView.setAnimation(R.raw.animation_lobi464z)
            }

           "Snows","Snow", "Light Snow", "Moderate Snow", "Heavy Snow", "Blizzard"->{
                binding.root.setBackgroundResource(R.drawable.snow_background)
                binding.lottieAnimationView.setAnimation(R.raw.snow)
            }
            else->{
                binding.root.setBackgroundResource(R.drawable.sunny_background)
                binding.lottieAnimationView.setAnimation(R.raw.sunny)
            }


        }
        binding.lottieAnimationView.playAnimation()
    }



}
fun time(timestamp: Long): String {
    val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
    return  sdf.format((Date(timestamp*1000)))
}
fun dayName(timestamp: Long):String{
    val sdf = SimpleDateFormat("EEE", Locale.getDefault())
    return  sdf.format((Date()))
}
fun date(): String {
    val sdf = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    return  sdf.format((Date()))
}
