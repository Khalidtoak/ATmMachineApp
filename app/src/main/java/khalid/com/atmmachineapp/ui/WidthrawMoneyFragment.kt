package khalid.com.atmmachineapp.ui

import android.content.Intent
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import com.google.android.material.snackbar.Snackbar
import khalid.com.atmmachineapp.R
import khalid.com.atmmachineapp.model.Card
import kotlinx.android.synthetic.main.widthraw_money_fragment.*
import java.util.*


class WidthrawMoneyFragment : FireBaseFragment(){
     private lateinit var card :Card
    private  var pinNoCount : Int  = 0
    private var  amtToText: String = ""

    companion object {
        fun newInstance() = WidthrawMoneyFragment()
    }

    private lateinit var viewModel: WithdrawMoneyViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.widthraw_money_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(WithdrawMoneyViewModel::class.java)
        // Use t5he ViewModel
        val name = name_text.text.toString()
        textView6.setOnClickListener {view ->
            findCard(name, view)
        }
        button.setOnClickListener {view ->
            handleWidthrawal(view)
        }
        textView7.setOnClickListener {
            AlertDialog.Builder(context!!.applicationContext).setTitle("Account balance").setMessage("Your account balance is ${card.balance}")
                .show()
        }
    }

    private fun handleWidthrawal(view: View) {
        amtToText = amt_text.text.toString()
        if (amtToText.isEmpty()) {
            amt_text.error = "please add the amount you want to widthraw"
            return
        }
        if (pin.text.toString().isEmpty()) {
            pin.error = "Please enter your atm card pin"
            return
        }
        if (amtToText.toInt() > 10000) {
            amt_text.error = "You cannot widthraw more than 10000 naira at once"
            return
        }
        // val document = firestore.collection("cards").document(textView4.text.toString())
        val balance = card.balance
        if (amtToText.toInt() > balance) {
            amt_text.error = "Insufficient funds!!"
            return
        }
        if (pin.text.toString() != card.pin) {
            pin.error = "Your pin is invalid"
            pinNoCount += 1
            return
        }
        if (pinNoCount == 3) {
            showSnackbar(view, "Card Blocked!! You entered the wrong pin")
            return
        }
        if (pinNoCount > 3) {
            showSnackbar(view, "You have to retrieve card from bank or create card to continue")
            return
        }
        if (amtToText.toInt() / 500 != 1) {
            showSnackbar(view, "This Atm machine can only dispense #500 and #1000")
            return
        }
        progressBar3.visibility = View.VISIBLE
        firestore.collection("cards").document(textView4.text.toString())
            .update("balance", balance - amtToText.toInt())
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    progressBar3.visibility = View.INVISIBLE
                    showSnackbar(view, "Transaction successful")
                    startActivity(Intent(activity, ReceiptActivity::class.java).apply {
                        putExtra("AmtWithdrawn", amtToText)
                        putExtra("balance", card.balance)
                        putExtra("time", Date())
                    })
                } else {
                    progressBar3.visibility = View.INVISIBLE
                    showSnackbar(view, "Transaction unsuccessful, please retry")
                }
            }
    }

    private fun findCard(name: String, view: View) {
        if (name_text.text.toString().isEmpty()) {
            name_text.error = "Name of card is needed to find card"
            return
        }
        progressBar2.visibility = View.VISIBLE
        val documentRefrence = firestore.collection("cards").document(name_text.text.toString())
        documentRefrence.addSnapshotListener { documentSnapshot, firebaseFirestoreException ->
            if (firebaseFirestoreException!=null){
                progressBar2.visibility = View.INVISIBLE
                showSnackbar(view, "Couldn't find card, please retry")
            }
            else{
                progressBar2.visibility = View.INVISIBLE
                card = documentSnapshot?.toObject(Card::class.java)!!
                textView5.text = card.cardNumber
                textView4.text = card.name
                button.isEnabled = true
            }
        }
    }
    private fun showSnackbar(view: View, text:String) {
        Snackbar.make(view, text , Snackbar.LENGTH_LONG).show()
    }

}
