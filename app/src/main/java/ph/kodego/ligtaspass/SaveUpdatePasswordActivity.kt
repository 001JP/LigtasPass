package ph.kodego.ligtaspass

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import kotlinx.coroutines.launch
import ph.kodego.ligtaspass.database.PasswordApp
import ph.kodego.ligtaspass.database.PasswordDAO
import ph.kodego.ligtaspass.database.PasswordEntity
import ph.kodego.ligtaspass.databinding.ActivitySaveUpdatePasswordBinding

class SaveUpdatePasswordActivity : AppCompatActivity() {

    private lateinit var mPasswordDao: PasswordDAO
    private lateinit var binding: ActivitySaveUpdatePasswordBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySaveUpdatePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Transition animation
        Animatoo.animateSlideLeft(this)

        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        mPasswordDao = (application as PasswordApp).db.passwordDao()

        //Sample
        val password = PasswordEntity(0, "Title", "test@email.com", "12345678")
        //Sample
       lifecycleScope.launch {
           mPasswordDao.insert(password)
       }
    }
}