package ph.kodego.ligtaspass

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import kotlinx.coroutines.launch
import ph.kodego.ligtaspass.database.PasswordApp
import ph.kodego.ligtaspass.database.PasswordDAO
import ph.kodego.ligtaspass.database.PasswordEntity

class SaveUpdatePasswordActivity : AppCompatActivity() {

    private lateinit var mPasswordDao: PasswordDAO
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_save_update_password)

        //Transition animation
        Animatoo.animateSlideLeft(this)

        mPasswordDao = (application as PasswordApp).db.passwordDao()

        val password = PasswordEntity(0, "Title", "test@email.com", "12345678")

       lifecycleScope.launch {
           mPasswordDao.insert(password)
       }
    }
}