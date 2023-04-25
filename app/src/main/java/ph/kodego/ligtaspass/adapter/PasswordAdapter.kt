package ph.kodego.ligtaspass.adapter

import android.app.Activity
import android.app.Dialog
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import ph.kodego.ligtaspass.database.PasswordDAO
import ph.kodego.ligtaspass.database.PasswordEntity
import ph.kodego.ligtaspass.databinding.DialogModifyPasswordBinding
import ph.kodego.ligtaspass.databinding.DialogViewPasswordBinding
import ph.kodego.ligtaspass.databinding.SavedItemsBinding
import ph.kodego.ligtaspass.utils.Constants


class PasswordAdapter ( var passwords: ArrayList<PasswordEntity>, var activity: Activity)
    : RecyclerView.Adapter<PasswordAdapter.SavingsViewHolder>() {

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
                    passwordPassword.setText(Constants.decrypt(activity, password.password))
                    lastUpdate.text = password.lastUpdate

                    dialogViewPasswordBinding.btnModify.setOnClickListener{
                        showModifyDialog().show()
                    }
                }

                with(builder) {
                    setNegativeButton("Cancel",
                        DialogInterface.OnClickListener { dialog, id ->
                        })
                    setView(dialogViewPasswordBinding.root)
                    create()
                }
            }?: throw IllegalStateException("Activity cannot be null")
        }

        private fun showModifyDialog(): Dialog {
            return activity?.let {
                val builder = AlertDialog.Builder(it)
                val dialogModifyPasswordBinding: DialogModifyPasswordBinding =
                    DialogModifyPasswordBinding.inflate(it.layoutInflater)

                with(dialogModifyPasswordBinding) {

                    passwordTitle.setText(password.title)
                    email.setText(password.emailUsername)
                    passwordPassword.setText(Constants.decrypt(activity, password.password))
                    lastUpdate.text = password.lastUpdate
                }
                with(builder) {
                    setPositiveButton("Cancel",
                        DialogInterface.OnClickListener { dialog, id ->
                        })
                    setNegativeButton("Update",
                        DialogInterface.OnClickListener { dialog, id ->
                            //Update here
                        })
                    setView(dialogModifyPasswordBinding.root)
                    create()
                }
            }?: throw IllegalStateException("Activity cannot be null")
        }

        private fun toast(text: String) = Toast.makeText(activity.applicationContext,text, Toast.LENGTH_SHORT).show()
    }
}