package ru.practicum.android.diploma.common.utils

import ru.practicum.android.diploma.search.data.dto.response_models.Salary

class SalaryConverter {
    fun convertSalary(salary: Salary?): String? {
        if (salary == null) return "ЗП не указана"
        val result = StringBuilder()

        salary.from?.let {
            result.append("от")
            result.append(" ")
            result.append(it)
            result.append(" ")
        }

        salary.to?.let {
            result.append("до")
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