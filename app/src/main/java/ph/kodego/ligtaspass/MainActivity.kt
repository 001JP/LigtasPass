package ph.kodego.ligtaspass

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import ph.kodego.ligtaspass.databinding.ActivityMainBinding

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
}