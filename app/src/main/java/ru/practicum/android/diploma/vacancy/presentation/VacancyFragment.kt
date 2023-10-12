package ru.practicum.android.diploma.vacancy.presentation

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.text.HtmlCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.common.utils.BindingFragment
import ru.practicum.android.diploma.common.utils.Constants.CLICK_DEBOUNCE_DELAY_MILLIS
import ru.practicum.android.diploma.common.utils.debounce
import ru.practicum.android.diploma.databinding.FragmentVacancyBinding

import ru.practicum.android.diploma.db.domain.models.Vacancy
import ru.practicum.android.diploma.search.data.dto.response_models.VacancyItem
import ru.practicum.android.diploma.vacancy.domain.models.VacancyDetails

class VacancyFragment : BindingFragment<FragmentVacancyBinding>() {
    private val viewModel by viewModel<VacancyViewModel>()
    private val currentVacancy by lazy { retrieveVacancy() }
    private lateinit var vacancy: Vacancy
    private lateinit var vacancyDetails: VacancyDetails
    private lateinit var confirmDialog: MaterialAlertDialogBuilder

    private var isClickAllowed = true

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentVacancyBinding {
        return FragmentVacancyBinding.inflate(inflater, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.icSharing.setOnClickListener {
            val share = Intent(Intent.ACTION_SEND)
            share.putExtra(Intent.EXTRA_TEXT, "ССЫЛКА")
            share.type = "text/plane"
            requireContext().startActivity(share)
        }
        binding.vacancyContactEmailValue.setOnClickListener {
            requireContext().startActivity(
                Intent(
                    Intent.ACTION_SENDTO,
                    Uri.parse("mailto:kalinroman034@gmail.com")
                )
            )
        }
        binding.vacancyContactPhoneValue.setOnClickListener {
            val call = Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", "8-800-555-35-35", null))
            requireContext().startActivity(call)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initBasicVacancyInfo()
        setConfirmDialog()

        viewModel.loadVacancyDetails(vacancy.id)

        viewModel.checkFavourite(vacancy)

        viewModel.stateFavouriteIconLiveData.observe(viewLifecycleOwner) {
            renderStateFavouriteIcon(it)
        }

        viewModel.stateVacancyInfoDb.observe(viewLifecycleOwner) { vacancyDetailsDb ->
            if (vacancyDetailsDb == null){
                return@observe
            }
            vacancyDetails = vacancyDetailsDb
            binding.detailsData.visibility = View.VISIBLE
            binding.icFavorites.isClickable = true
            binding.icSharing.isClickable = true
            initVacancyDetails()
        }

        viewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is VacancyState.Content -> {
                    binding.progressBar.visibility = View.GONE
                    vacancyDetails = state.vacancyDetails
                    binding.detailsData.visibility = View.VISIBLE
                    binding.similarVacanciesButton.visibility = View.VISIBLE
                    binding.icFavorites.isClickable = true
                    binding.icSharing.isClickable = true
                    initVacancyDetails()
                }
                is VacancyState.Error -> {
                    binding.progressBar.visibility = View.GONE
                    binding.detailsData.visibility = View.GONE
                    binding.similarVacanciesButton.visibility = View.GONE
                    binding.icFavorites.isClickable = false
                    binding.icSharing.isClickable = false
                    showToast(getString(R.string.error_no_internet))
                    viewModel.initVacancyDetailsInDb(vacancy)
                }

                VacancyState.Loading -> binding.progressBar.visibility = View.VISIBLE
            }
        }

        initClickListeners()
    }

    private fun initBasicVacancyInfo(){

        //проверить использовние KEY_VACANCY
        val jsonVacancy = requireArguments().getString(KEY_VACANCY)
        vacancy = Gson().fromJson(jsonVacancy, Vacancy::class.java)
        binding.vacancyName.text = vacancy.name
        initSalary()
        binding.employerName.width = (resources.displayMetrics.widthPixels*0.7).toInt()
        binding.city.width = (resources.displayMetrics.widthPixels*0.7).toInt()
        binding.employerName.text = vacancy.employerName
        binding.city.text = vacancy.city
    }

    private fun setConfirmDialog(){
        confirmDialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle(requireActivity().getString(R.string.write_to_email))
            .setNegativeButton(requireActivity().getString(R.string.no)) { _, _ ->

            }.setPositiveButton(requireActivity().getString(R.string.yes)) { _, _ ->
                viewModel.shareEmail(vacancyDetails.contacts?.email!!)
            }
    }

    private fun initSalary(){
        var salaryText = ""
        if(vacancy.salaryFrom != null) salaryText = "от ${vacancy.salaryFrom} "
        if(vacancy.salaryTo != null) salaryText += "до ${vacancy.salaryTo}"
        if(vacancy.salaryCurrency != null) salaryText += " ${vacancy.salaryCurrency}"
        if(vacancy.salaryFrom == null && vacancy.salaryTo == null && vacancy.salaryCurrency == null) salaryText = getString(
            R.string.salary_not_specified)
        binding.salary.text = salaryText
    }

    private fun initVacancyDetails() {
        val radius = resources.getDimensionPixelSize(R.dimen.margin_12)
        Glide.with(requireContext())
            .load(vacancyDetails.employer?.logoUrls?.original)
            .placeholder(R.drawable.vacancy_placeholder)
            .transform(RoundedCorners(radius))
            .into(binding.placeholder)

        val keySkills = vacancyDetails.keySkills
        val contacts = vacancyDetails.contacts
        val nameContact = vacancyDetails.contacts?.name
        val emailContact = vacancyDetails.contacts?.email
        val phoneContactList = vacancyDetails.contacts?.phones
        val firstPhoneContact = phoneContactList?.getOrNull(0)
        val phoneComment = if (firstPhoneContact != null) firstPhoneContact.comment else null
        val formattedPhoneContact =
            if (firstPhoneContact != null) "+${firstPhoneContact.country}(${firstPhoneContact.city})${
                firstPhoneContact.number.dropLast(4)
            }-${
                firstPhoneContact.number.drop(3).dropLast(2)
            }-${firstPhoneContact.number.drop(5)}" else null
        val noData = getString(R.string.no_data)

        binding.city.text = if (vacancy.city.isEmpty()) vacancyDetails.area.name else vacancy.city
        binding.requiredExperienceValue.text = vacancyDetails.experience?.name ?: noData
        binding.scheduleValue.text = vacancyDetails.schedule?.name ?: noData
        binding.vacancyDescriptionValue.text = HtmlCompat.fromHtml(
            vacancyDetails.description,
            HtmlCompat.FROM_HTML_MODE_COMPACT
        )

        if (keySkills.isNotEmpty()) {
            binding.keySkillsContainer.visibility = View.VISIBLE
            binding.vacancyKeySkillsValue.text = keySkills.joinToString { it.name }
        } else
            binding.keySkillsContainer.visibility = View.GONE

        if (contacts != null) {
            binding.contactsContainer.visibility = View.VISIBLE
            binding.vacancyContactPersonValue.text =
                if (nameContact != null) nameContact else noData
            binding.vacancyContactEmailValue.text =
                if (emailContact != null) emailContact else noData
            binding.vacancyContactPhoneValue.text =
                if (formattedPhoneContact != null) formattedPhoneContact else noData
            binding.vacancyPhoneCommentValue.text =
                if (phoneComment != null) phoneComment else noData
        } else
            binding.contactsContainer.visibility = View.GONE
    }

    private fun showToast(message: String){
        Toast.makeText(requireActivity().applicationContext, message, Toast.LENGTH_LONG)
            .show()
    }

    private fun renderStateFavouriteIcon(isFavourite: Boolean?){
        when (isFavourite) {
            true -> binding.icFavorites.setImageResource(R.drawable.ic_favorites_on)
            else -> binding.icFavorites.setImageResource(R.drawable.ic_favorites_off)
        }
    }
    val debouncedOnClick = debounce<Unit>(
        delayMillis = CLICK_DEBOUNCE_DELAY_MILLIS,
        coroutineScope = viewLifecycleOwner.lifecycleScope,
        useLastParam = true,  // or false, depending on your requirements
        action = {
            findNavController().navigate(
                R.id.action_vacancyFragment_to_similarVacancyFragment,
                SimilarVacancyFragment.createArgs(vacancy.id)
            )
        }
    )

    private val phoneClickDebounce = debounce<String>(
        delayMillis = CLICK_DEBOUNCE_DELAY_MILLIS,
        coroutineScope = viewLifecycleOwner.lifecycleScope,
        useLastParam = false
    ) { phone ->
        if (vacancyDetails.contacts?.phones != null) {
            viewModel.sharePhone(phone)
        }
    }

    private val emailClickDebounce = debounce<Unit>(
        delayMillis = CLICK_DEBOUNCE_DELAY_MILLIS,
        coroutineScope = viewLifecycleOwner.lifecycleScope,
        useLastParam = false
    ) {
        if (vacancyDetails.contacts?.email != null) {
            confirmDialog.show()
        }
    }
    private fun initClickListeners(){
        binding.similarVacanciesButton.setOnClickListener {
            debouncedOnClick(Unit)
        }

        binding.backBtn.setOnClickListener{
            findNavController().navigateUp()
        }

        binding.icFavorites.setOnClickListener{
            viewModel.clickOnFavoriteIcon(vacancy, vacancyDetails)
        }

        binding.icSharing.setOnClickListener {
            viewModel.shareVacancyUrl(vacancyDetails.alternateUrl)
        }

        binding.vacancyContactPhoneValue.setOnClickListener {
            phoneClickDebounce(binding.vacancyContactPhoneValue.text.toString())
        }

        binding.vacancyContactEmailValue.setOnClickListener {
            emailClickDebounce(Unit)
        }

    }

    fun retrieveVacancy(): VacancyItemDto {
        val jsonVacancy = requireArguments().getString(KEY_VACANCY)
        return Gson().fromJson(jsonVacancy, VacancyItemDto::class.java)
    }

    companion object {
        const val KEY_VACANCY = "vacancy"

        fun createArgs(vacancy: Vacancy): Bundle {
            val gson = Gson()
            val jsonVacancy = gson.toJson(vacancy)
            val bundle = Bundle()
            bundle.putString(KEY_VACANCY, jsonVacancy)
            return bundle
        }
    }
}