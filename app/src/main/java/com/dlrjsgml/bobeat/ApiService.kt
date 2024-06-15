package com.dlrjsgml.bobeat

import retrofit2.Call
import com.dlrjsgml.bobeat.data.MealResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("mealServiceDietInfo")
    fun getMealInfo(
        @Query("Key") key : String,
        @Query("Type") type: String,
        @Query("pIndex") pIndex: Int,
        @Query("pSize") pSize: Int,
        @Query("ATPT_OFCDC_SC_CODE") officeCode: String,
        @Query("SD_SCHUL_CODE") schoolCode: String,
        @Query("MLSV_YMD") mlsvYMD : String
    ): Call<MealResponse>
}