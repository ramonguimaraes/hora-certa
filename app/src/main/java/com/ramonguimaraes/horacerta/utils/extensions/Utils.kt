package com.ramonguimaraes.horacerta.ui

import android.widget.EditText

/**
 * Retorna [true] caso o valor do editText não seja vazio e se variavel de controle [isValid]
 * seja [true]
 *
 * @param error: String
 * @param showError: Boolean
 * @param isValid: Boolean
 *
 * @return Boolean
 */
fun EditText.validate(error: String = "Campo obrigatório", showError: Boolean = true, isValid: Boolean): Boolean {
    val fieldValid = this.text.isNotBlank()
    if (!fieldValid) {
        if (showError) {
            this.error = error
        }
    }
    return isValid && fieldValid
}
