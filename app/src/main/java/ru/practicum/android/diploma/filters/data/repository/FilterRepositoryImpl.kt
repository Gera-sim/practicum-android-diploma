package ru.practicum.android.diploma.filters.data.repository

import android.content.SharedPreferences
import ru.practicum.android.diploma.filters.domain.api.FilterRepository

class FilterRepositoryImpl(
    private val sharedPrefs: SharedPreferences,
) : FilterRepository {
    override fun saveFilters(
        location: String?,
        industry: String?,
        expectedSalary: String?,
        removeNoSalary: Boolean,
    ) {
        val country: String?
        var region: String? = null
        if (location.toString().contains(",")) {
            country = location?.split(",")?.first()
            region = location?.split(",")?.last()
        } else {
            country = location
        }
        sharedPrefs.edit()
            .putString(COUNTRY_KEY, country)
            .putString(REGION_KEY, region)
            .putString(INDUSTRY_KEY, industry)
            .putString(EXPECTED_SALARY_KEY, expectedSalary)
            .putBoolean(NO_SALARY_KEY, removeNoSalary)
            .apply()
    }

    override fun getLocation(): String {
        val location: String = if (sharedPrefs.getString(REGION_KEY, null) !== null) {
            sharedPrefs.getString(COUNTRY_KEY, null) + "," + sharedPrefs.getString(REGION_KEY, null)
        } else {
            sharedPrefs.getString(COUNTRY_KEY, null) ?: ""
        }
        return location
    }

    override fun getIndustry(): String? {
        return sharedPrefs.getString(INDUSTRY_KEY, null)
    }

    override fun getExpectedSalary(): String? {
        return sharedPrefs.getString(EXPECTED_SALARY_KEY, null)
    }

    override fun getRemoveNoSalary(): Boolean {
        return sharedPrefs.getBoolean(NO_SALARY_KEY, false)
    }

    override fun removeFilters() {
        sharedPrefs.edit().clear().apply()
    }

    companion object {
        const val COUNTRY_KEY = "COUNTRY_KEY"
        const val REGION_KEY = "REGION_KEY"
        const val INDUSTRY_KEY = "INDUSTRY_KEY"
        const val EXPECTED_SALARY_KEY = "EXPECTED_SALARY_KEY"
        const val NO_SALARY_KEY = "NO_SALARY_KEY"
    }
}