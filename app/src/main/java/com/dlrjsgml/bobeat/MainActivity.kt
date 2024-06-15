package com.dlrjsgml.bobeat

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.dlrjsgml.bobeat.data.MealResponse
import com.dlrjsgml.bobeat.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.letsgo.setOnClickListener {
            if(binding.writens.text.isNotEmpty()){
                try {
                    getMealInfo("${binding.writens.text}")
                    binding.whatday.text = "${binding.writens.text.toString().substring(0 until 4)}년${binding.writens.text.toString().substring(4 until 6)}월${binding.writens.text.toString().substring(6 until 8)}일 의 급식은?"
                }
                catch(e : Exception){
                    binding.whatday.text = "입력 형식이 틀렸습니다."
                }
            }
        }

    }

    private fun getMealInfo(whens : String) {
        val call = RetrofitInstance.api.getMealInfo(
            key =  "07712bb365424a558eab0a7ece4638c1",
            type = "json",
            pIndex = 1,
            pSize = 100,
            officeCode = "D10",
            schoolCode = "7240454",
            mlsvYMD = whens
        )

        call.enqueue(object : Callback<MealResponse> {
            override fun onResponse(call: Call<MealResponse>, response: Response<MealResponse>) {
                if (response.isSuccessful) {
                    val mealResponse = response.body()
                    mealResponse?.let {
                        val menuContents = mealResponse.toString()
                        // Handle the response here
                        try{
                            binding.first.text = menuContents.split(",")[1]
                        }
                        catch (e:Exception){
                            binding.first.text = "오늘 아침이 없습니다."
                        }
                        try{
                            binding.sec.text = menuContents.split(",")[2]
                        }
                        catch (e:Exception){
                            binding.sec.text = "오늘 점심이 없습니다."
                        }
                        try{
                            binding.third.text = menuContents.split(",")[3]
                        }
                        catch (e:Exception){
                            binding.third.text = "오늘 저녁이 없습니다."
                        }

                    }
                } else {
                    Log.e("MainActivity", "Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<MealResponse>, t: Throwable) {
                Log.e("MainActivity", "Failure: ${t.message}")
            }
        })
    }
}
