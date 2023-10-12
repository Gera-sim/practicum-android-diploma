package ru.practicum.android.diploma.search.data.network

import android.content.Context
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.search.data.dto.VacanciesResponse
import ru.practicum.android.diploma.search.data.dto.response_models.Salary
import ru.practicum.android.diploma.search.domain.models.ConvertedResponse
import ru.practicum.android.diploma.search.domain.models.Vacancy

class ModelConverter(private val context: Context) {
    fun convertVacanciesResponse(response: VacanciesResponse): ConvertedResponse {
        val vacancies = response.items.map { item ->
            Vacancy(
                id = item.id,
                logoUrl = item.employer?.logo_urls?.url240.toString(),
                title = item.name,
                company = item.employer?.name ?: "",
                salary = convertSalary(item.salary)
            )

        }
        return ConvertedResponse(vacancies)
    }

    fun convertSalary(salary: Salary?): String {
        if (salary == null) return context.getString(R.string.salary_not_specified)
        val result = StringBuilder()

        salary.from?.let {
            result.append(context.getString(R.string.from))
            result.append(" ")
            result.append(it)
            result.append(" ")
        }

        salary.to?.let {
            result.append(context.getString(R.string.to))
            result.append(" ")
            result.append(it)
        }

        val currencySymbol = when (salary.currency) {
            "AZN" -> "₼"
            "BYR" -> "Br"
            "EUR" -> "€"
            "GEL" -> "₾"
            "KGS" -> "сом"
            "KZT" -> "₸"
            "RUR" -> "₽"
            "UAH" -> "₴"
            "USD" -> "$"
            "UZS" -> "so'm"
            else -> salary.currency
        }
        result.append(" ")
        result.append(currencySymbol)

        return result.toString()
    }
}