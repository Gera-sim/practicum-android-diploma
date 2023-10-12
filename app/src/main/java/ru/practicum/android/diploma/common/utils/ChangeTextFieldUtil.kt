package ru.practicum.android.diploma.common.utils

import android.annotation.SuppressLint
import android.content.Context
import android.widget.ImageButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import ru.practicum.android.diploma.R

object ChangeTextFieldUtil {
    @SuppressLint("UseCompatLoadingForDrawables")
    fun changeTextField(
        editText: TextInputEditText,
        textField: TextInputLayout,
        clearBtn: ImageButton,
        context: Context,
    ) {
        if (!editText.text.isNullOrEmpty()) {
            textField.setPadding(
                0, context.resources.getDimension(R.dimen.margin_14).toInt(),
                context.resources.getDimension(R.dimen.margin_24).toInt(), 0
            )
            textField.isEnabled = true
            textField.setHintTextAppearance(R.style.Text_Regular_12_400)
            clearBtn.setImageDrawable(context.getDrawable(R.drawable.ic_clear))
            clearBtn.isClickable = true
        } else {
            textField.setPadding(
                0, context.resources.getDimension(R.dimen.margin_4).toInt(),
                context.resources.getDimension(R.dimen.margin_24).toInt(), 0
            )
            textField.isEnabled = false
            textField.setHintTextAppearance(R.style.Text_Regular_16_400)
            clearBtn.setImageDrawable(context.getDrawable(R.drawable.arrow_forward))
            clearBtn.isClickable = false
        }
    }
}