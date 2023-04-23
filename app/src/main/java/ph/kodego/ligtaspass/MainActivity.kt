package ph.kodego.ligtaspass

import android.R
import android.app.Dialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import ph.kodego.ligtaspass.databinding.ActivityMainBinding
import ph.kodego.ligtaspass.databinding.DialogGeneratorSettingsBinding
import ph.kodego.ligtaspass.databinding.DialogViewPasswordBinding
import ph.kodego.ligtaspass.generator_settings.GeneratorSettings
import ph.kodego.ligtaspass.utils.Constants
import ph.kodego.ligtaspass.utils.PreferenceUtility
import java.security.SecureRandom

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Transition Animation
        Animatoo.animateSlideUp(this)

        binding.saveButton.setOnClickListener {
            val intent = Intent(this, SaveUpdatePasswordActivity::class.java)
            startActivity(intent)
        }

        binding.generateButton.setOnClickListener {
            val generatedPassword = generatePassword()
            binding.generatedPasswordEditText.setText(generatedPassword)
            Log.d("MainActivity", "Generated Password: $generatedPassword")

            //Play cat
            binding.catLottie.playAnimation()
        }

        binding.historyButton.setOnClickListener{
            showCustomDialogue().show()
        }

        binding.copyPassword.setOnClickListener{
            copyTextToClipboard()
        }

        binding.settingsTextView.setOnClickListener {
            showSettingsDialog()
        }
    }

    private fun generatePassword(): String{

        //Generate password with settings requirement
        val preferenceUtility = PreferenceUtility(this)
        val generatorSettings = GeneratorSettings(
            preferenceUtility.getBooleanPreferences(Constants.INCLUDE_SYMBOLS),
            preferenceUtility.getBooleanPreferences(Constants.INCLUDE_NUMBERS),
            preferenceUtility.getBooleanPreferences(Constants.INCLUDE_LOWERCASE),
            preferenceUtility.getBooleanPreferences(Constants.INCLUDE_UPPERCASE),
            preferenceUtility.getIntPreferences(Constants.PASSWORD_LENGTH)
        )

        //If nothing is selected, revert to default
        if (!generatorSettings.includeSymbols
            && !generatorSettings.includeNumbers
            && !generatorSettings.includeLowercase
            && !generatorSettings.includeUppercase){
            generatorSettings.includeSymbols = true
            generatorSettings.includeNumbers = true
            generatorSettings.includeLowercase = true
            generatorSettings.includeUppercase = true
        }

        var generatedPassword = ""
        val secureRandom = SecureRandom()


        val smallLetters = ('a'..'z').toList().shuffled()
        val capitalLetters = ('A'..'Z').toList().shuffled()
        val numbers = (0..9).toList().shuffled()
        val symbols = listOf("~", "!", "@", "#", "$", "%", "^", "&", "*", "(", ")", "-", "+", "=", "[", "]", "{", "}", ";", ":", "'", "\"", "<", ">", ".", ",", "/", "?", "|", "\\").shuffled()

        do {

            //Generate secure random number from 0..3 + 1
            when (secureRandom.nextInt(4)+1) {
                1 -> {
                    if (generatorSettings.includeSymbols){
                        generatedPassword += symbols[secureRandom.nextInt(symbols.size-1)]
                    } else continue
                }
                2 -> {
                    if (generatorSettings.includeNumbers){
                        generatedPassword += numbers[secureRandom.nextInt(numbers.size-1)]
                    } else continue
                }
                3 -> {
                    if (generatorSettings.includeLowercase){
                        generatedPassword += smallLetters[secureRandom.nextInt(smallLetters.size-1)]
                    } else continue
                }
                4 -> {
                    if (generatorSettings.includeUppercase){
                        generatedPassword += capitalLetters[secureRandom.nextInt(capitalLetters.size-1)]
                    } else continue
                }
            }

        }while (generatedPassword.length <= generatorSettings.passwordLength-1)

        return generatedPassword
    }

    private fun showCustomDialogue(): Dialog {
        return this?.let {
            val builder = AlertDialog.Builder(this)
            val dialogViewPasswordBinding : DialogViewPasswordBinding =
                DialogViewPasswordBinding.inflate(this.layoutInflater)

            with(builder) {
                setView(dialogViewPasswordBinding.root)
                create()
            }
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun copyTextToClipboard() {
        val textToCopy =  binding.generatedPasswordEditText.text
        val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("text", textToCopy)
        clipboardManager.setPrimaryClip(clipData)
        Toast.makeText(this, "Text copied to clipboard", Toast.LENGTH_LONG).show()
    }

    private fun showSettingsDialog(){
        val dialog = Dialog(this)
        val settingsDialogBinding : DialogGeneratorSettingsBinding =
            DialogGeneratorSettingsBinding.inflate(this.layoutInflater)
        dialog.setContentView(settingsDialogBinding.root)
        dialog.setCancelable(true)

        val preferenceUtility = PreferenceUtility(this)

        val generatorSettings = GeneratorSettings(
            preferenceUtility.getBooleanPreferences(Constants.INCLUDE_SYMBOLS),
            preferenceUtility.getBooleanPreferences(Constants.INCLUDE_NUMBERS),
            preferenceUtility.getBooleanPreferences(Constants.INCLUDE_LOWERCASE),
            preferenceUtility.getBooleanPreferences(Constants.INCLUDE_UPPERCASE),
            preferenceUtility.getIntPreferences(Constants.PASSWORD_LENGTH)
        )

        if (generatorSettings.includeSymbols) settingsDialogBinding.includeSymbolsCheckbox.isChecked = true
        if (generatorSettings.includeNumbers) settingsDialogBinding.includeNumbersCheckbox.isChecked = true
        if (generatorSettings.includeLowercase) settingsDialogBinding.includeLowercaseCheckbox.isChecked = true
        if (generatorSettings.includeUppercase) settingsDialogBinding.includeUppercaseCheckbox.isChecked = true
        settingsDialogBinding.passwordLengthEditText.setText(preferenceUtility.getIntPreferences(Constants.PASSWORD_LENGTH).toString())

        settingsDialogBinding.saveSettingsButton.setOnClickListener {

            if (settingsDialogBinding.includeSymbolsCheckbox.isChecked){
                preferenceUtility.saveBooleanPreferences(Constants.INCLUDE_SYMBOLS, true)
            } else {
                preferenceUtility.saveBooleanPreferences(Constants.INCLUDE_SYMBOLS, false)
            }

            if (settingsDialogBinding.includeNumbersCheckbox.isChecked){
                preferenceUtility.saveBooleanPreferences(Constants.INCLUDE_NUMBERS, true)
            } else {
                preferenceUtility.saveBooleanPreferences(Constants.INCLUDE_NUMBERS, false)
            }

            if (settingsDialogBinding.includeLowercaseCheckbox.isChecked){
                preferenceUtility.saveBooleanPreferences(Constants.INCLUDE_LOWERCASE, true)
            } else {
                preferenceUtility.saveBooleanPreferences(Constants.INCLUDE_LOWERCASE, false)
            }

            if (settingsDialogBinding.includeUppercaseCheckbox.isChecked){
                preferenceUtility.saveBooleanPreferences(Constants.INCLUDE_UPPERCASE, true)
            } else {
                preferenceUtility.saveBooleanPreferences(Constants.INCLUDE_UPPERCASE, false)
            }

            val passwordLength = settingsDialogBinding.passwordLengthEditText.text.toString().toIntOrNull()

            if (passwordLength != null && passwordLength > 0){
                preferenceUtility.saveIntPreferences(Constants.PASSWORD_LENGTH, passwordLength)
                Toast.makeText(this, "Settings saved.", Toast.LENGTH_LONG).show()
                dialog.dismiss()
            } else {
                Toast.makeText(this, "Password length should be greater than 0.", Toast.LENGTH_LONG).show()
            }

        }


        dialog.show()
    }
}