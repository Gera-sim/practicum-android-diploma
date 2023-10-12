package ru.practicum.android.diploma.search.data.dto.response_models

import kotlinx.serialization.Serializable

@Serializable
data class Area(
    val id: String? = "",
    val name: String? = "",
    val parentId: String? = "",
) {
    companion object {
        val emptyArea = Area(name = "")
    }
}
