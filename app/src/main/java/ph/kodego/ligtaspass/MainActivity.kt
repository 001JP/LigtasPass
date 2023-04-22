package ph.kodego.ligtaspass

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import ph.kodego.ligtaspass.databinding.ActivityMainBinding
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

            //Generate random number from 0..3 + 1
            when (secureRandom.nextInt(4)+1) {
                1 -> generatedPassword += smallLetters[secureRandom.nextInt(smallLetters.size-1)]
                2 -> generatedPassword += capitalLetters[secureRandom.nextInt(capitalLetters.size-1)]
                3 -> generatedPassword += numbers[secureRandom.nextInt(numbers.size-1)]
                4 -> generatedPassword += symbols[secureRandom.nextInt(symbols.size-1)]
            }

        }while (generatedPassword.length <= passwordLength)

        return generatedPassword
    }
}