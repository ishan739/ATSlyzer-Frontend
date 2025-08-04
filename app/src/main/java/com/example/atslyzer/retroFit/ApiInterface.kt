package com.example.atslyzer.retroFit

import com.example.atslyzer.models.resumeResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiInterface {
    @Multipart
    @POST("/resume/upload")
    suspend fun analyzeResume(
        @Part file: MultipartBody.Part,
        @Part("jobRole") jobRole: RequestBody,
    ) : Response<resumeResponse>
}