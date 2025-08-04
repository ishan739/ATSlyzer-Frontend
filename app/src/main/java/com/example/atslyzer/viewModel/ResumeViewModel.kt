package com.example.atslyzer.viewModel

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.atslyzer.retroFit.ApiInterface
import com.example.atslyzer.models.ResumeAnalysis
import com.google.gson.Gson
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody

class ResumeViewModel(private val apiInterface: ApiInterface) : ViewModel() {

    var isLoading by mutableStateOf(false)
        private set

    var atsScore by mutableIntStateOf(0)
        private set

    var foundKeywords by mutableStateOf(emptyList<String>())
        private set

    var missingKeywords by mutableStateOf(emptyList<String>())
        private set

    var grammarMistakes by mutableStateOf(listOf<String>())
        private set

    var suggestions by mutableStateOf(listOf<String>())
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    fun analyzeResume(file: MultipartBody.Part, jobRole: RequestBody) {

        viewModelScope.launch {
            isLoading = true
            try {
                val response = apiInterface.analyzeResume(file, jobRole)

                if (response.isSuccessful) {
                    val rawText = response.body()
                        ?.candidates?.firstOrNull()
                        ?.content?.parts?.firstOrNull()?.text

                    rawText?.let {
                        val cleaned = it.replace(Regex("(?s)```json\\s*|```"), "").trim()
                        val parsed = Gson().fromJson(cleaned, ResumeAnalysis::class.java)

                        atsScore = parsed.atsScore
                        foundKeywords = parsed.foundKeywords
                        missingKeywords = parsed.missingKeywords
                        grammarMistakes = parsed.grammarMistakes
                        suggestions = parsed.suggestions
                        errorMessage = null
                    }
                        ?: run {
                            errorMessage = "Empty response from server."
                        }
                } else {
                    errorMessage = "API call failed: ${response.code()}"
                }

            } catch (e : Exception) {
                errorMessage = "Something went wrong: ${e.localizedMessage}"
            }

            isLoading = false
        }

    }

}