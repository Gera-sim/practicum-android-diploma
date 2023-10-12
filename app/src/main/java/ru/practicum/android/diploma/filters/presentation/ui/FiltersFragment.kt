package ru.practicum.android.diploma.filters.presentation.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.common.utils.ChangeTextFieldUtil
import ru.practicum.android.diploma.databinding.FragmentFilterBinding
import ru.practicum.android.diploma.filters.presentation.ui.ChoosePlaceWorkFragment.Companion.AREA_ID
import ru.practicum.android.diploma.filters.presentation.ui.ChoosePlaceWorkFragment.Companion.KEY_CHOOSE
import ru.practicum.android.diploma.filters.presentation.ui.ChoosePlaceWorkFragment.Companion.PLACE_WORK
import ru.practicum.android.diploma.filters.presentation.view_model.FiltersViewModel

class FiltersFragment : Fragment() {
    private lateinit var binding: FragmentFilterBinding
    private var isChecked = false
    private var areaId = ""
    private val vm by viewModel<FiltersViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentFilterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        setPlaceWork()
        showRemoveBtn()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.btnLocation.setOnClickListener {
            findNavController().navigate(R.id.action_filtersFragment_to_choosePlaceWorkFragment)
        }
        binding.locationClearBtn.setOnClickListener {
            binding.locationEditText.text?.clear()
            vm.getLocation(null)
            changeLocationField()
        }
        binding.btnIndustry.setOnClickListener {
            findNavController().navigate(R.id.action_filtersFragment_to_chooseIndustry)
        }
        binding.industryClearBtn.setOnClickListener {
            binding.industryEditText.text?.clear()
            changeIndustryField()
        }
        binding.salaryEditText.addTextChangedListener {
            if (binding.salaryEditText.text.isEmpty()) {
                binding.textExpectedSalary.setTextColor(R.attr.text_hint_search)
                binding.salaryClearBtn.isVisible = false
            } else {
                binding.textExpectedSalary.setTextColor(requireContext().getColor(R.color.blue))
                binding.salaryClearBtn.isVisible = true
            }
            showRemoveBtn()
        }
        binding.salaryClearBtn.setOnClickListener {
            binding.salaryEditText.text.clear()
        }
        binding.noSalaryLayout.setOnClickListener {
            isChecked = !isChecked
            changeCheckBox()
        }
        binding.btnChoose.setOnClickListener {
            vm.saveFilters(
                binding.locationEditText.text.toString(),
                binding.industryEditText.text.toString(),
                binding.salaryEditText.text.toString(),
                isChecked,
                areaId,
            )
        }

        binding.btnRemove.setOnClickListener {
            vm.removeFilters()
            binding.locationEditText.text?.clear()
        }

        vm.observeState().observe(viewLifecycleOwner) {
            binding.locationEditText.setText(it.location)
            binding.industryEditText.setText(it.industry)
            binding.salaryEditText.setText(it.lowestSalary)
            isChecked = it.removeNoSalary
            changeCheckBox()
            changeLocationField()
            changeIndustryField()
        }
    }

    private fun changeLocationField() {
        ChangeTextFieldUtil.changeTextField(
            binding.locationEditText,
            binding.locationTextField,
            binding.locationClearBtn,
            requireContext(),
        )
        showRemoveBtn()
    }

    private fun changeIndustryField() {
        ChangeTextFieldUtil.changeTextField(
            binding.industryEditText,
            binding.industryTextField,
            binding.industryClearBtn,
            requireContext(),
        )
        showRemoveBtn()
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun changeCheckBox() {
        if (isChecked) {
            binding.noSalaryCheckbox.background =
                requireContext().getDrawable(R.drawable.pressed_check_box)
        } else {
            binding.noSalaryCheckbox.background =
                requireContext().getDrawable(R.drawable.empty_check_box)
        }
        showRemoveBtn()
    }

    private fun showRemoveBtn() {
        if (binding.locationEditText.text.toString()
                .isNotEmpty() || binding.industryEditText.text.toString().isNotEmpty()
            || binding.salaryEditText.text.isNotEmpty() || isChecked
        ) {
            binding.btnRemove.isVisible = true
            binding.btnChoose.text = requireContext().getString(R.string.apply)
        } else {
            binding.btnRemove.isVisible = false
            binding.btnChoose.text = requireContext().getString(R.string.select)
        }
    }

    private fun setPlaceWork() {
        setFragmentResultListener(KEY_CHOOSE)
        { _, bundle ->
            if (!bundle.getString(PLACE_WORK).isNullOrEmpty()) {
                vm.getLocation(bundle.getString(PLACE_WORK))
                areaId = bundle.getString(AREA_ID) ?: ""
            }
        }
    }
}