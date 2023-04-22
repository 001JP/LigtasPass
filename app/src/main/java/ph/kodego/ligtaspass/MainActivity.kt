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
            val generatedPassword = generatePassword(8)
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

    private fun generatePassword(passwordLength: Int): String{

        //TODO: Generate password with settings requirement

        var generatedPassword = ""
        val secureRandom = SecureRandom()


        val smallLetters = ('a'..'z').toList().shuffled()
        val capitalLetters = ('A'..'Z').toList().shuffled()
        val numbers = (0..9).toList().shuffled()
        val symbols = listOf("~", "!", "@", "#", "$", "%", "^", "&", "*", "(", ")", "-", "+", "=", "[", "]", "{", "}", ";", ":", "'", "\"", "<", ">", ".", ",", "/", "?", "|", "\\").shuffled()

        do {

            //Generate secure random number from 0..3 + 1
            when (secureRandom.nextInt(4)+1) {
                1 -> generatedPassword += smallLetters[secureRandom.nextInt(smallLetters.size-1)]
                2 -> generatedPassword += capitalLetters[secureRandom.nextInt(capitalLetters.size-1)]
                3 -> generatedPassword += numbers[secureRandom.nextInt(numbers.size-1)]
                4 -> generatedPassword += symbols[secureRandom.nextInt(symbols.size-1)]
            }

        }while (generatedPassword.length <= passwordLength)

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

        settingsDialogBinding.saveSettingsButton.setOnClickListener {
            Toast.makeText(this, "Settings saved.", Toast.LENGTH_LONG).show()
            dialog.dismiss()
        }


        dialog.show()
    }
}