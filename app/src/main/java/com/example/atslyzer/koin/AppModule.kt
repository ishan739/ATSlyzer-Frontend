package com.example.atslyzer.koin

import com.example.atslyzer.retroFit.RetrofitInstance
import com.example.atslyzer.viewModel.ResumeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { RetrofitInstance.apiInterface }
    viewModel { ResumeViewModel(get()) }
}