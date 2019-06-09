package khalid.com.atmmachineapp.ui

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
        val collectionReference = firestore.collection("cards")
        val query = collectionReference.whereEqualTo("name", name_text.text.toString())
        query.get().addOnSuccessListener { querySnapshot ->
            if (
                querySnapshot.isEmpty) {
                collectionReference
                    .add(
                        Card(
                            textView2.text.toString(),
                            pin.text.toString(),
                            10000,
                            CreditCardNumberGenerator.generate("5399", 16)
                        )).addOnCompleteListener {
                        if (it.isSuccessful) {
                            progressBar.visibility = View.INVISIBLE
                            showSnackbar(view, "Card created and credited successfully")
                        } else {
                            progressBar.visibility = View.INVISIBLE
                            showSnackbar(view, "Could not create card")
                        }
                    }
            } else {
                showSnackbar(view, "card with that name already exists")
                name_text.error = "card with that name already exists"
                progressBar.visibility = View.INVISIBLE
            }
        }.addOnFailureListener {
            showSnackbar(view, "couldn't save card, please retry")
        }
    }

    private fun showSnackbar(view: View, text:String) {
        Snackbar.make(view, text , Snackbar.LENGTH_LONG).show()
    }
}