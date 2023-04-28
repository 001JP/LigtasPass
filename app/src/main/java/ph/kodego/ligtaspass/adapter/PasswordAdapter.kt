package ph.kodego.ligtaspass.adapter

import android.app.Dialog
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import ph.kodego.ligtaspass.MainActivity
import ph.kodego.ligtaspass.database.PasswordEntity
import ph.kodego.ligtaspass.databinding.DialogModifyPasswordBinding
import ph.kodego.ligtaspass.databinding.DialogViewPasswordBinding
import ph.kodego.ligtaspass.databinding.SavedItemsBinding
import java.text.SimpleDateFormat
import java.util.Date


class PasswordAdapter (var activity: MainActivity)
    : RecyclerView.Adapter<PasswordAdapter.SavingsViewHolder>() {

    private var passwords: ArrayList<PasswordEntity> = arrayListOf()

    fun addSamples(password: PasswordEntity){
        passwords.add(0,password)
        notifyItemInserted(0)
    }

    override fun getItemCount(): Int {
        return  passwords.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavingsViewHolder {
        val itemBinding = SavedItemsBinding //ItemAccountBinding
            .inflate(
                LayoutInflater.from(parent.context),
                parent, false)
        return SavingsViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: PasswordAdapter.SavingsViewHolder, //ano yung laman ng bawat layout na gagamitin
                                  position: Int) {
        holder.bindStudent(passwords[position])
    }

    inner class SavingsViewHolder(private val itemBinding: SavedItemsBinding) // responsible sa each row na data and behavior ng bawat row
        : RecyclerView.ViewHolder(itemBinding.root), View.OnClickListener{

        var password = PasswordEntity()
        init {
            itemView.setOnClickListener(this)
        }
        fun bindStudent(passwords: PasswordEntity){
            this.password = passwords
            itemBinding.passwordName.text ="${passwords.title}"
            itemBinding.lastUpdate.text ="${passwords.lastUpdate}"
        }

        override fun onClick(v: View?) {
            showCustomDialogue().show()
        }



        private fun showCustomDialogue(): Dialog {
            return activity?.let {
                val builder = AlertDialog.Builder(it)
                val dialogViewPasswordBinding: DialogViewPasswordBinding =
                    DialogViewPasswordBinding.inflate(it.layoutInflater)

                with(dialogViewPasswordBinding) {
                    passwordTitle.setText(password.title)
                    passwordPassword.setText(activity.decryptPassword(password))
                    passwordUsernameEmail.setText(password.emailUsername)
                    lastUpdate.text = password.lastUpdate
                }

                with(builder) {
                    setNegativeButton("Update") {dialogInterface, which ->

                        val date: String = SimpleDateFormat("MM-dd-yyyy HH:mm:ss").format(Date())

                        val updatedPasswordEntity = PasswordEntity(
                            password.id,
                            password.uuid,
                            dialogViewPasswordBinding.passwordTitle.text.toString(),
                            dialogViewPasswordBinding.passwordUsernameEmail.text.toString(),
                            activity.encrypt(dialogViewPasswordBinding.passwordPassword.text.toString(), password.uuid).toString(),
                            date
                        )
                        activity.updatePassword(updatedPasswordEntity)
                        dialogInterface.dismiss()
                    }
                    setNeutralButton("Delete") {dialogInterface, which ->
                        activity.deletePassword(password)
                        dialogInterface.dismiss()
                    }
                    setPositiveButton("Cancel",
                        DialogInterface.OnClickListener{ dialog, which ->}
                    )
                    setView(dialogViewPasswordBinding.root)
                    create()
                }
            }?: throw IllegalStateException("Activity cannot be null")
        }
        private fun toast(text: String) = Toast.makeText(activity.applicationContext,text, Toast.LENGTH_SHORT).show()
    }

    fun passwordList(list: ArrayList<PasswordEntity>){
        passwords = list
        notifyDataSetChanged()
    }
}