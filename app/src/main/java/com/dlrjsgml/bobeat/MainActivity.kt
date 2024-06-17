package com.dlrjsgml.bobeat

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.dlrjsgml.bobeat.data.MealResponse
import com.dlrjsgml.bobeat.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.log

class MainActivity : AppCompatActivity() {

    lateinit var binding : ActivityMainBinding

    @RequiresApi(Build.VERSION_CODES.O)
    var mutableDate : LocalDateTime = LocalDateTime.now()
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        changeDay(0)
        binding.yesterday.setOnClickListener {
            changeDay(-1)
        }
        binding.tommorow.setOnClickListener {
            changeDay(1)
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun changeDay(good : Long){
        getMealInfo(mutableDate!!.plusDays(good).format(DateTimeFormatter.ofPattern("yyyyMMdd")))
        mutableDate = mutableDate!!.plusDays(good)
        val datetoStringMan = mutableDate!!.format(DateTimeFormatter.ofPattern("yyyyMMdd"))
        val monthman =datetoStringMan.substring(4, 6).toInt().toString()
        val dayman = datetoStringMan.substring(6, 8).toInt().toString()
        binding.whatday.text = "${datetoStringMan.substring(0,4)}년 ${ monthman}월 ${ dayman}일의 급식은?"
    }
    private fun getMealInfo(whens: String) {
        val call = RetrofitInstance.api.getMealInfo(
            key = "07712bb365424a558eab0a7ece4638c1",
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
                        try {
                            binding.first.text = menuContents.split(",")[1].replace("<br/>","\n").substring(39,)
                        } catch (e: Exception) {
                            binding.first.text = "오늘 아침이 없습니다."
                        }
                        try {
                            binding.sec.text = menuContents.split(",")[2].replace("<br/>","\n").substring(14,)
                        } catch (e: Exception) {
                            binding.sec.text = "오늘 점심이 없습니다."
                        }
                        try {
                            binding.third.text = menuContents.split(",")[3].replace("<br/>","\n").substring(14,)
                        } catch (e: Exception) {
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
