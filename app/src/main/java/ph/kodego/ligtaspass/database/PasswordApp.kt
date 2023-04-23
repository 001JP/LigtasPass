package ph.kodego.ligtaspass.database

import android.app.Application

class PasswordApp: Application() {
    val db by lazy {
        PasswordDatabase.getInstance(this)
    }
}