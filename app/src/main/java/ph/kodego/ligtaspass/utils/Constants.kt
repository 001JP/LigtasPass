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
    fun encrypt(context: Context, message: String, uuid: String): String {

        val cryptoManager = CryptoManager()

        val bytes = message.encodeToByteArray()
        val file = File(context.filesDir, "$uuid.txt")
        if (!file.exists()){
            file.createNewFile()
        }
        val fos = FileOutputStream(file)

        return cryptoManager.encrypt(bytes, fos, uuid).decodeToString()
    }

    fun decrypt(context: Context, uuid: String): String{

        val cryptoManager = CryptoManager()

        val fileDecrypt = File(context.filesDir, "$uuid.txt")
        return cryptoManager.decrypt(
            FileInputStream(fileDecrypt),
            uuid
        ).decodeToString()
    }
}