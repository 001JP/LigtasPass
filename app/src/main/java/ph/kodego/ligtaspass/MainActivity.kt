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
import ph.kodego.ligtaspass.databinding.DialogViewPasswordBinding

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
    }

    private fun generatePassword(passwordLength: Int): String{

        //TODO: Generate password with settings requirement

        var generatedPassword = ""

        val smallLetters = ('a'..'z').toList()
        val capitalLetters = ('A'..'Z').toList()
        val numbers = (0..9).toList()
        val symbols = listOf("~", "!", "@", "#", "$", "%", "^", "&", "*", "(", ")", "-", "+", "=", "[", "]", "{", "}", ";", ":", "'", "\"", "<", ">", ".", ",", "/", "?", "|", "\\")

        do {

            var characterPicker = 0

            when ((1..4).random()) {
                1 -> {
                    characterPicker = (smallLetters.indices).random()
                    generatedPassword += smallLetters[characterPicker]
                }
                2 -> {
                    characterPicker = (capitalLetters.indices).random()
                    generatedPassword += capitalLetters[characterPicker]
                }
                3 -> {
                    characterPicker = (numbers.indices).random()
                    generatedPassword += numbers[characterPicker]
                }
                4 -> {
                    characterPicker = (symbols.indices).random()
                    generatedPassword += symbols[characterPicker]
                }
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
}