package khalid.com.atmmachineapp.ui

import android.content.Intent
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.SetOptions
import khalid.com.atmmachineapp.*
import khalid.com.atmmachineapp.model.Card
import khalid.com.atmmachineapp.utils.CreditCardNumberGenerator
import kotlinx.android.synthetic.main.create_card_fragment.*
import kotlinx.android.synthetic.main.create_card_fragment.name_text
import kotlinx.android.synthetic.main.create_card_fragment.pin
import kotlinx.android.synthetic.main.widthraw_money_fragment.*


class CreateCardFragment : FireBaseFragment() {

    companion object {
        fun newInstance() = CreateCardFragment()
    }

    private lateinit var viewModel: CreateCardViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.create_card_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(CreateCardViewModel::class.java)
        name_text.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                textView2.text = p0.toString()
            }
        })
        textView3.setOnClickListener { view ->
            saveCard(view)
        }
    }

    private fun saveCard(view: View) {
        if (name_text.text.toString().isEmpty()) {
            name_text.error = "name cannot be empty"
            return
        }
        if (pin.text.toString().isEmpty()) {
            pin.error = "pin cannot be empty"
            return
        }
        textView.text = CreditCardNumberGenerator.generate("5399", 16)

        progressBar.visibility = View.VISIBLE
        val doocumentReference = firestore.collection("cards").document(name_text.text.toString())
        doocumentReference.get().addOnSuccessListener {document->
            if (document.exists()){
                showSnackbar(view , "Card with that name already exists")
            }
            else{
                doocumentReference.set(Card(
                    textView2.text.toString(),
                    pin.text.toString(),
                    10000,
                    CreditCardNumberGenerator.generate("5399", 16)
                ), SetOptions.merge()).addOnCompleteListener { task ->
                    if (task.isSuccessful){
                        progressBar.visibility = View.INVISIBLE
                        showSnackbar(view, "Card created and credited successfully")
                    }
                    else {
                        progressBar.visibility = View.INVISIBLE
                        showSnackbar(view, "Could not create card")
                    }
                }
            }
        }.addOnFailureListener {
            showSnackbar(view, "couldn't save card,please retry")
        }
    }

    private fun showSnackbar(view: View, text:String) {
        Snackbar.make(view, text , Snackbar.LENGTH_LONG).show()
    }
}
