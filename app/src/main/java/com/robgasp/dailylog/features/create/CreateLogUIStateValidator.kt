package com.robgasp.dailylog.features.create

import com.robgasp.dailylog.core.misc.Validator
import jakarta.inject.Inject
import jakarta.inject.Singleton

@Singleton
class CreateLogUIStateValidator @Inject constructor() : Validator<CreateViewModel.UIState> {
    override fun isValid(value: CreateViewModel.UIState): Validator.Report {
        val reportsList = mutableListOf<String>()
        if (value.title.isEmpty()) {
            reportsList.add("Empty title")
        }
        if (value.description.isEmpty()) {
            reportsList.add("Empty log")
        }
        return if (reportsList.isEmpty()) {
            Validator.Valid
        } else {
            Validator.Invalid(reportsList.toList())
        }
    }
}
