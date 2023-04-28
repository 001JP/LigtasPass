package ph.kodego.ligtaspass

import android.app.Dialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import ph.kodego.ligtaspass.adapter.PasswordAdapter
import ph.kodego.ligtaspass.database.PasswordApp
import ph.kodego.ligtaspass.database.PasswordDAO
import ph.kodego.ligtaspass.database.PasswordEntity
import ph.kodego.ligtaspass.databinding.ActivityMainBinding
import ph.kodego.ligtaspass.databinding.DialogGeneratorSettingsBinding
import ph.kodego.ligtaspass.databinding.DialogViewPasswordBinding
import ph.kodego.ligtaspass.generator_settings.GeneratorSettings
import ph.kodego.ligtaspass.utils.Constants
import ph.kodego.ligtaspass.utils.PreferenceUtility
import java.security.SecureRandom

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private lateinit var passwordsAdapter: PasswordAdapter

    private lateinit var mPasswordDao: PasswordDAO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Transition Animation
        Animatoo.animateSlideUp(this)
        mPasswordDao = (application as PasswordApp).db.passwordDao()

        lifecycleScope.launch{

            mPasswordDao.fetchAllRecords().collect(){
                if (it.isNotEmpty()){
                    val passwords = ArrayList(it)
                    //Adapter set up
                    passwordsAdapter = PasswordAdapter(this@MainActivity)
                    passwordsAdapter.passwordList(passwords)
                    binding.list.layoutManager = LinearLayoutManager(applicationContext)
                    binding.list.adapter = passwordsAdapter

                    binding.noPasswordSaveImageView.visibility = View.GONE
                    binding.noPasswordSaveTextView.visibility = View.GONE
                    binding.list.visibility = View.VISIBLE
                } else {
                    binding.noPasswordSaveImageView.visibility = View.VISIBLE
                    binding.noPasswordSaveTextView.visibility = View.VISIBLE
                    binding.list.visibility = View.GONE
                }

            }
        }

        binding.saveButton.setOnClickListener {
            val intent = Intent(this, SaveUpdatePasswordActivity::class.java)
            intent.putExtra(SaveUpdatePasswordActivity.PASSWORD, binding.generatedPasswordEditText.text.toString())
            startActivity(intent)
        }

        binding.generateButton.setOnClickListener {

            //Close keyboard
            binding.generatedPasswordEditText.clearFocus()
            binding.searchBox.clearFocus()
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(binding.root.windowToken, 0)

            val generatedPassword = generatePassword()
            binding.generatedPasswordEditText.setText(generatedPassword)

            //Play cat
            binding.catLottie.playAnimation()
        }

       /* binding.history.setOnClickListener{
            showCustomDialogue().show()
        }*/

        binding.copyPassword.setOnClickListener{
            copyTextToClipboard()
        }

        binding.settingsTextView.setOnClickListener {
            showSettingsDialog()
        }

        binding.imageView.setOnClickListener{
            var testing : PasswordEntity = PasswordEntity()

            testing.id = 0
            testing.title = "myPassword"
            testing.password = "&vo,;M1oZqJl"
            testing.emailUsername = "jason@yahoo.com"
            var save = PreferenceUtility(this)
            save.saveStringPreferences("tryKey","tryValue")
            Toast.makeText(this, "Password length ${save.getStringPreferences("tryKey")}", Toast.LENGTH_LONG).show()
        }

        val extras = intent.extras
        var user: String? = ""
        if (extras != null) {
            user = extras.getString("Save")
            if(user == "Done"){
                Snackbar.make(binding.root,
                    "Saved Successfully.",
                    Snackbar.LENGTH_LONG).show()
            }
        }
    }

    private fun generatePassword(): String{

        //Generate password with settings requirement
        val preferenceUtility = PreferenceUtility(this)
        var generatorSettings = GeneratorSettings(
            preferenceUtility.getBooleanPreferences(Constants.INCLUDE_SYMBOLS),
            preferenceUtility.getBooleanPreferences(Constants.INCLUDE_NUMBERS),
            preferenceUtility.getBooleanPreferences(Constants.INCLUDE_LOWERCASE),
            preferenceUtility.getBooleanPreferences(Constants.INCLUDE_UPPERCASE),
            preferenceUtility.getIntPreferences(Constants.PASSWORD_LENGTH)
        )

        //If no settings is selected, select to default
        if (
            !generatorSettings.includeSymbols &&
            !generatorSettings.includeNumbers &&
            !generatorSettings.includeLowercase &&
            !generatorSettings.includeUppercase){
            generatorSettings = GeneratorSettings()
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

    fun copyTextToClipboard() {
        val textToCopy =  binding.generatedPasswordEditText.text
        val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("text", textToCopy)
        clipboardManager.setPrimaryClip(clipData)
        Toast.makeText(this, "Text copied to clipboard", Toast.LENGTH_LONG).show()
    }

    private fun showSettingsDialog() {
        val dialog = Dialog(this)
        val settingsDialogBinding: DialogGeneratorSettingsBinding =
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

        if (generatorSettings.includeSymbols) settingsDialogBinding.includeSymbolsCheckbox.isChecked =
            true
        if (generatorSettings.includeNumbers) settingsDialogBinding.includeNumbersCheckbox.isChecked =
            true
        if (generatorSettings.includeLowercase) settingsDialogBinding.includeLowercaseCheckbox.isChecked =
            true
        if (generatorSettings.includeUppercase) settingsDialogBinding.includeUppercaseCheckbox.isChecked =
            true
        settingsDialogBinding.passwordLengthEditText.setText(
            preferenceUtility.getIntPreferences(
                Constants.PASSWORD_LENGTH
            ).toString()
        )

        settingsDialogBinding.saveSettingsButton.setOnClickListener {

            if (settingsDialogBinding.includeSymbolsCheckbox.isChecked) {
                preferenceUtility.saveBooleanPreferences(Constants.INCLUDE_SYMBOLS, true)
            } else {
                preferenceUtility.saveBooleanPreferences(Constants.INCLUDE_SYMBOLS, false)
            }

            if (settingsDialogBinding.includeNumbersCheckbox.isChecked) {
                preferenceUtility.saveBooleanPreferences(Constants.INCLUDE_NUMBERS, true)
            } else {
                preferenceUtility.saveBooleanPreferences(Constants.INCLUDE_NUMBERS, false)
            }

            if (settingsDialogBinding.includeLowercaseCheckbox.isChecked) {
                preferenceUtility.saveBooleanPreferences(Constants.INCLUDE_LOWERCASE, true)
            } else {
                preferenceUtility.saveBooleanPreferences(Constants.INCLUDE_LOWERCASE, false)
            }

            if (settingsDialogBinding.includeUppercaseCheckbox.isChecked) {
                preferenceUtility.saveBooleanPreferences(Constants.INCLUDE_UPPERCASE, true)
            } else {
                preferenceUtility.saveBooleanPreferences(Constants.INCLUDE_UPPERCASE, false)
            }

            val passwordLength =
                settingsDialogBinding.passwordLengthEditText.text.toString().toIntOrNull()

            if (passwordLength != null && passwordLength > 0) {
                preferenceUtility.saveIntPreferences(Constants.PASSWORD_LENGTH, passwordLength)
                Toast.makeText(this, "Settings saved.", Toast.LENGTH_LONG).show()
                dialog.dismiss()
            } else {
                Toast.makeText(this, "Password length should be greater than 0.", Toast.LENGTH_LONG)
                    .show()
            }

        }
        dialog.show()
    }

    fun decryptPassword(password: PasswordEntity): String{
        return Constants.decrypt(this, password.uuid)
    }

    fun encrypt(password: String, uuid: String){
        Constants.encrypt(this, password, uuid)
    }

    fun updatePassword(password: PasswordEntity){

        lifecycleScope.launch{
            mPasswordDao.update(password)

            mPasswordDao.fetchAllRecords().collect(){
                if (it.isNotEmpty()){
                    val passwords = ArrayList(it)
                    passwordsAdapter.passwordList(passwords)

                    binding.noPasswordSaveImageView.visibility = View.GONE
                    binding.noPasswordSaveTextView.visibility = View.GONE
                    binding.list.visibility = View.VISIBLE
                } else {
                    binding.noPasswordSaveImageView.visibility = View.VISIBLE
                    binding.noPasswordSaveTextView.visibility = View.VISIBLE
                    binding.list.visibility = View.GONE
                }
            }
        }

        Snackbar.make(binding.root, "Password updated.", Toast.LENGTH_SHORT).show()
    }

    fun deletePassword(password: PasswordEntity){
        lifecycleScope.launch{
            mPasswordDao.delete(password)

            mPasswordDao.fetchAllRecords().collect(){
                if (it.isNotEmpty()){
                    val passwords = ArrayList(it)
                    passwordsAdapter.passwordList(passwords)

                    binding.noPasswordSaveImageView.visibility = View.GONE
                    binding.noPasswordSaveTextView.visibility = View.GONE
                    binding.list.visibility = View.VISIBLE
                } else {
                    binding.noPasswordSaveImageView.visibility = View.VISIBLE
                    binding.noPasswordSaveTextView.visibility = View.VISIBLE
                    binding.list.visibility = View.GONE
                }
            }
        }

        Snackbar.make(binding.root, "Password updated.", Toast.LENGTH_SHORT).show()
    }

}