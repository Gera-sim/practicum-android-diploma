package ru.practicum.android.diploma.vacancy.data

import com.google.gson.annotations.SerializedName



data class VacancyDetailsResponse (
    val contacts: Contacts?,
    val description: String,
    @SerializedName("alternate_url")
    val alternateUrl: String,
    val area: Area,
    val employer: Employer?,
    val experience: Experience?,
    @SerializedName("key_skills")
    val keySkills: List<KeySkill>,
    val schedule: Schedule?,
): Response()