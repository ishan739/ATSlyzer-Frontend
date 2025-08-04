package com.example.atslyzer.models

data class ResumeAnalysis(
    val atsScore: Int,
    val foundKeywords: List<String>,
    val missingKeywords: List<String>,
    val grammarMistakes: List<String>,
    val suggestions: List<String>
)
