package ru.practicum.android.diploma.db.data.converter

import android.annotation.SuppressLint
import ru.practicum.android.diploma.db.data.entity.VacancyEntity
import ru.practicum.android.diploma.db.domain.models.Vacancy
import ru.practicum.android.diploma.search.data.dto.response_models.Salary
import ru.practicum.android.diploma.search.data.dto.response_models.VacancyItem
import ru.practicum.android.diploma.vacancy.domain.models.*
import java.text.*
import java.util.Date
import java.util.Locale

class VacancyDbConverter {
    fun map(vacancyEntity: VacancyEntity): VacancyItem {
        return VacancyItem(
            id = vacancyEntity.id,
            area = Area(name = vacancyEntity.area),
            name = vacancyEntity.name,
            city = vacancyEntity.city,
            salary = Salary(
                currency = vacancyEntity.salaryCurrency,
                from = vacancyEntity.salaryFrom,
                to = vacancyEntity.salaryTo
            ),
            employer = Employer(
                name = vacancyEntity.employerName,
                logoUrls = LogoUrls(
                    original = vacancyEntity.originalLogo
                )
            ),
            employerName = vacancyEntity.employerName,
            found = 0,
            employerLogoUrl = vacancyEntity.employerLogoUrl,
            pages = 0,
        )
    }

       private fun getContacts(vacancyEntity: VacancyEntity): Contacts? {
        if (vacancyEntity.contactEmail.isNullOrEmpty() && vacancyEntity.contactName.isNullOrEmpty() && getPhonesList(
                vacancyEntity
            ).isNullOrEmpty()
        ) return null

        return Contacts(
            vacancyEntity.contactEmail, vacancyEntity.contactName, getPhonesList(vacancyEntity)
        )
    }

    fun mapDetail(vacancyEntity: VacancyEntity): VacancyDetails {
        return VacancyDetails(
            contacts = getContacts(vacancyEntity),
            description = vacancyEntity.description,
            alternateUrl = vacancyEntity.alternateUrl,
            area = Area(name = vacancyEntity.area),
            employer = getEmployer(vacancyEntity),
            experience = getExperience(vacancyEntity),
            keySkills = getKeySkillsList(vacancyEntity),
            schedule = getSchedule(vacancyEntity)
        )
    }

    fun map(vacancy: Vacancy, vacancyDetails: VacancyDetails): VacancyEntity {
        return VacancyEntity(
            id = vacancy.id,
            name = vacancy.name,
            city = vacancy.city,
            employerName = vacancy.employerName,
            employerLogoUrl = vacancy.employerLogoUrl,
            salaryCurrency = vacancy.salaryCurrency,
            salaryFrom = vacancy.salaryFrom?.filterNot { it.isWhitespace() }?.toInt(),
            salaryTo = vacancy.salaryTo?.filterNot { it.isWhitespace() }?.toInt(),
            getCurrentDate(),

            contactEmail = vacancyDetails.contacts?.email,
            contactName = vacancyDetails.contacts?.name,
            contactPhones = getContactPhone(vacancyDetails.contacts?.phones),
            contactComment = getContactComment(vacancyDetails.contacts?.phones),
            description = vacancyDetails.description,
            alternateUrl = vacancyDetails.alternateUrl,
            area = vacancyDetails.area.name,
            originalLogo = vacancyDetails.employer?.logoUrls?.original ?: "",
            experience = vacancyDetails.experience?.name,
            keySkillsList = getKeySkillsStr(vacancyDetails.keySkills),
            schedule = vacancyDetails.schedule?.name
        )
    }

    //Форматтеры:
    private fun getContactPhone(phones: List<Phone>?): String {
        if (phones.isNullOrEmpty()) return ""
        val firstPhoneContact = phones[0]
        val phone =
            "+${firstPhoneContact.country}" + "(${firstPhoneContact.city})" + firstPhoneContact.number.dropLast(
                4
            ) + "-${
                firstPhoneContact.number.drop(3).dropLast(2)
            }" + "-${firstPhoneContact.number.drop(5)}"
        return phone
    }

    private fun getContactComment(phones: List<Phone>?): String? {
        if (phones.isNullOrEmpty()) return ""
        return phones[0].comment
    }

    private fun getKeySkillsStr(keySkills: List<KeySkill>): String {
        var keySkillStr = ""
        keySkills.forEach { keySkill ->
            if (!keySkill.name.isNullOrEmpty()) keySkillStr += keySkill.name + ", "
        }

        return keySkillStr.dropLast(2)
    }

    private fun getPhonesList(vacancyEntity: VacancyEntity): List<Phone>? {
        if (vacancyEntity.contactPhones.isNullOrEmpty()) return null
        val phone = vacancyEntity.contactPhones
        val country = phone[1].toString()
        val city = phone.substring(3, 6)
        val number = phone.substring(7, 10) + phone.substring(11, 13) + phone.substring(14, 16)
        val comment = vacancyEntity.contactComment
        val phonesList = arrayListOf<Phone>()
        phonesList.add(Phone(city, country, number, comment))
        return phonesList.toList()
    }

    private fun getSchedule(vacancyEntity: VacancyEntity): Schedule? {
        return if (vacancyEntity.schedule.isNullOrEmpty()) null
        else return Schedule(name = vacancyEntity.schedule)
    }

    private fun getKeySkillsList(vacancyEntity: VacancyEntity): List<KeySkill> {
        if (vacancyEntity.keySkillsList.isNullOrEmpty()) return emptyList()
        val keySkillsNameList = vacancyEntity.keySkillsList.split(",").toList()
        return keySkillsNameList.map { KeySkill(name = it.trim()) }
    }

    private fun getExperience(vacancyEntity: VacancyEntity): Experience? {
        return if (vacancyEntity.experience.isNullOrEmpty()) null
        else Experience(name = vacancyEntity.experience)
    }

    private fun getEmployer(vacancyEntity: VacancyEntity): Employer? {
        return if (vacancyEntity.originalLogo.isNullOrEmpty()) null
        else Employer(
            LogoUrls(
                v90 = "", v240 = "", original = vacancyEntity.originalLogo
            )
        )
    }

    @SuppressLint("SimpleDateFormat")
    private fun getCurrentDate(): String {
        val sdf = SimpleDateFormat("dd/M/yyyy HH:mm:ss", Locale("ru", "RU"))
        return sdf.format(Date())
    }

    private fun createValue(salary: Int?): String? {
        if (salary == null) {
            return null
        }
        val formatter: DecimalFormat = NumberFormat.getInstance(Locale("ru", "RU")) as DecimalFormat
        return formatter.format(salary)
    }
}