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
                getMealInfo("${binding.writens.text}")
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
                        // Handle the response here
                        binding.good.text = mealResponse.toString()

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
