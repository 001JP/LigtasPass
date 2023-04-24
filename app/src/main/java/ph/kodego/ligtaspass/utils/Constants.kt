package ph.kodego.ligtaspass.utils

import android.content.Context
import ph.kodego.ligtaspass.encryption.CryptoManager
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

object Constants {
    const val INCLUDE_SYMBOLS = "include_symbols"
    const val INCLUDE_NUMBERS = "include_numbers"
    const val INCLUDE_LOWERCASE = "include_lowercase"
    const val INCLUDE_UPPERCASE = "include_uppercase"
    const val PASSWORD_LENGTH = "password_length"

    private const val ENCRYPTION_FILE = "secret.txt"
    fun encrypt(context: Context, message: String): String {

        val cryptoManager = CryptoManager()

        val bytes = message.encodeToByteArray()
        val file = File(context.filesDir, ENCRYPTION_FILE)
        if (!file.exists()){
            file.createNewFile()
        }
        val fos = FileOutputStream(file)

        return cryptoManager.encrypt(bytes, fos).decodeToString()
    }

    fun decrypt(context: Context, message: String): String{

        val cryptoManager = CryptoManager()

        val fileDecrypt = File(context.filesDir, ENCRYPTION_FILE)
        return cryptoManager.decrypt(
            inputStream = FileInputStream(fileDecrypt)
        ).decodeToString()
    }
}