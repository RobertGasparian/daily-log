package com.robgasp.dailylog.core.provider

// TODO: Handle localisation
interface LabelProvider : Provider<String, LabelProvider.Label> {
    override fun get(param: Label?): String {
        return when (param) {
            Label.TODAY -> "Today"
            Label.YESTERDAY -> "Yesterday"
            null -> ""
        }
    }
    enum class Label {
        TODAY,
        YESTERDAY
    }
}
