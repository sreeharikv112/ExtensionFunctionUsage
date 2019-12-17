package com.dev.extensionfunctionusage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.widget.EditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.activity_main.*
import java.util.regex.Pattern

class MainActivity : AppCompatActivity() {

    //Min Max values
    private val mGeneralMinChar = 1
    private val mGeneralMaxChar = 30

    //Differentiation for field validation
    private val mDefaultInputType = -1
    private val mPasswordInputType = 1

    //Email validator helper
    val mEmailRegEx = "[a-zA-Z0-9._-]+@[a-z]+.[a-z]+"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        attachTextChangeListeners()
    }

    /**
     * Attaches Text Change Listeners
     */
    private fun attachTextChangeListeners() {

        txt_person_name.onTextChanged(
            txt_person_email,
            InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS,
            txt_person_name_error_layout,
            getString(R.string.name_error_msg)
            , mGeneralMaxChar, false
        )

        txt_person_email.onEmailTextChanged(
            null,
            mDefaultInputType,
            txt_email_error_layout,
            getString(R.string.email_error_msg)
        )

        txt_person_email.onEmailTextChanged(
            txt_person_designation,
            mDefaultInputType,
            txt_email_error_layout,
            getString(R.string.email_error_msg)
        )

        txt_person_designation.onTextChanged(
            null,
            InputType.TYPE_CLASS_PHONE,
            txt_designation_error_layout,
            getString(R.string.designation_error_msg)
            , mGeneralMaxChar, false
        )
    }


    /**
     * Common entry field listener.
     * Propagates corresponding next entry field navigation and enabling/disabling
     */
    private fun EditText.onTextChanged(
        targetEdt: EditText?,
        inputType: Int,
        error_layout: TextInputLayout,
        errorMsg: String,
        maxCharactersAllowed: Int,
        isMinMaxStrictValidation: Boolean
    ) {
        this.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(edt: Editable?) {
                if (isMinMaxStrictValidation) {
                    if (edt?.length != maxCharactersAllowed) {
                        setErrorCondition(targetEdt, error_layout, errorMsg)
                    } else {
                        setNonErrorCondition(targetEdt, error_layout, inputType)
                    }
                } else {
                    if (edt!!.length < mGeneralMinChar || edt.length > maxCharactersAllowed) {
                        setErrorCondition(targetEdt, error_layout, errorMsg)
                    } else {
                        setNonErrorCondition(targetEdt, error_layout, inputType)
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })
    }

    /**
     * Specific validation for email entry field.
     */
    private fun EditText.onEmailTextChanged(
        targetEdt: EditText?,
        inputType: Int,
        error_layout: TextInputLayout,
        errorMsg: String
    ) {
        this.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(edt: Editable?) {

                if (text != null && !(Pattern.compile(
                        mEmailRegEx,
                        Pattern.CASE_INSENSITIVE
                    ).matcher(text)).matches()
                ) {
                    setErrorCondition(targetEdt, error_layout, errorMsg)
                } else {
                    setNonErrorCondition(targetEdt, error_layout, inputType)
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
    }

    /**
     * Common error condition for entry fields. Disables next entry field.
     */
    fun setErrorCondition(targetEdt: EditText?, error_layout: TextInputLayout?, errorMsg: String) {
        error_layout?.error = errorMsg
        targetEdt?.setDisabled()
    }

    /**
     * Common non error condition for entry fields. Enables the next entry field.
     */
    fun setNonErrorCondition(targetEdt: EditText?, error_layout: TextInputLayout?, inputType: Int) {
        error_layout?.error = null
        targetEdt?.setEnabled(inputType)
    }

    /**
     * Common function for disabling entry fields.
     */
    private fun EditText.setDisabled() {
        isFocusable = false
        isFocusableInTouchMode = false
        this.inputType = InputType.TYPE_NULL
    }

    /**
     * Common function for enabling and setting input field according to type of targeted entry.
     */
    private fun EditText.setEnabled(inputTypeNew: Int) {
        isFocusable = true
        isFocusableInTouchMode = true
        when (inputTypeNew) {
            mDefaultInputType ->
                this.inputType = InputType.TYPE_CLASS_TEXT
            mPasswordInputType ->
                this.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            else ->
                this.inputType = inputTypeNew
        }
    }
}
