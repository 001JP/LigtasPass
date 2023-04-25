package ph.kodego.ligtaspass

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import kotlinx.coroutines.launch
import ph.kodego.ligtaspass.database.PasswordApp
import ph.kodego.ligtaspass.database.PasswordDAO
import ph.kodego.ligtaspass.database.PasswordEntity
import ph.kodego.ligtaspass.databinding.ActivitySaveUpdatePasswordBinding
import ph.kodego.ligtaspass.utils.Constants
import java.text.SimpleDateFormat
import java.util.*

class SaveUpdatePasswordActivity : AppCompatActivity() {

    private lateinit var mPasswordDao: PasswordDAO
    private lateinit var binding: ActivitySaveUpdatePasswordBinding
    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySaveUpdatePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Transition animation
        Animatoo.animateSlideLeft(this)

        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        binding.btnSave.setOnClickListener{
            mPasswordDao = (application as PasswordApp).db.passwordDao()

            //Sample Encryption and Decryption
            val encryptedMessage = Constants.encrypt(this, "hello world")
            val decryptedMessage = Constants.decrypt(this, encryptedMessage)
            val date: String = SimpleDateFormat("MM-dd-yyyy HH:mm:ss").format(Date())
            val title = binding.txtTitle.text

            Log.i("SavePasswordActivity", "Encrypted: $encryptedMessage")
            Log.i("SavePasswordActivity", "Decrypted: $decryptedMessage")

            //Sample
            val password = PasswordEntity(
                0, "$title",
                "${binding.emailUsernameEdiText.text}",
                Constants.encrypt(this, "${binding.passwordEdiText.text}"),
                date)
            //Sample
            lifecycleScope.launch {
                mPasswordDao.insert(password)
            }

            var intent = Intent( this, MainActivity::class.java)
            var bundle = Bundle()
            intent.putExtras(bundle)
            intent.putExtra("Save","Done")
            startActivity(intent)
            finish()
           // onBackPressed()
        }

        val passwordGenerated = intent.getStringExtra(PASSWORD)
        binding.passwordEdiText.setText(passwordGenerated)
    }

    companion object{
        const val PASSWORD = "password"
    }
    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        finish()
    }
}