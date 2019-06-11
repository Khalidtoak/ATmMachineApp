package khalid.com.atmmachineapp.ui

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.snackbar.Snackbar
import khalid.com.atmmachineapp.R
import khalid.com.atmmachineapp.model.Card
import kotlinx.android.synthetic.main.widthraw_money_fragment.*


class WidthrawMoneyFragment : FireBaseFragment(){
     private lateinit var card :Card
    private  var pinNoCount : Int  = 0

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
            if (amt_text.text.toString().isEmpty()){
                amt_text.error = "please add the amount you want to widthraw"
                return@setOnClickListener
            }
            if (pin.text.toString().isEmpty()){
                pin.error = "Please enter your atm card pin"
                return@setOnClickListener
            }
            if (amt_text.text.toString().toInt() > 10000){
                amt_text.error = "You cannot widthraw more than 10000 naira at once"
                return@setOnClickListener
            }
           // val document = firestore.collection("cards").document(textView4.text.toString())
            val balance  = card.balance
            if (amt_text.text.toString().toInt() > balance) {
                amt_text.error = "Insufficient funds!!"
                return@setOnClickListener
            }
            if (pin.text.toString()!=card.pin){
                pin.error = "Your pin is invalid"
                pinNoCount +=1
                return@setOnClickListener
            }
            if (pinNoCount == 3){
                showSnackbar(view,  "Card Blocked!! You entered the wrong pin")
                return@setOnClickListener
            }
            if (pinNoCount>3){
                showSnackbar(view , "You have to retrieve card from bank or create card to continue")
                return@setOnClickListener
            }
            if (amt_text.text.toString().toInt()/500 !=1){
                showSnackbar(view , "This Atm machine can only dispense #500 and #1000")
                return@setOnClickListener
            }
            progressBar3.visibility = View.VISIBLE
            firestore.collection("cards").document(textView4.text.toString())
                .update("balance", balance - amt_text.text.toString().toInt())
                .addOnCompleteListener {task->
                    if (task.isSuccessful){
                        progressBar3.visibility = View.INVISIBLE
                        showSnackbar(view, "Transaction successful")

                    }
                    else{
                        progressBar3.visibility = View.INVISIBLE
                        showSnackbar(view, "Transaction unsuccessful, please retry")
                    }
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
