package khalid.com.atmmachineapp

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
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
            if (name_text.text.toString().isEmpty()) {
                name_text.error = "name cannot be empty"
                return@setOnClickListener
            }
            if (pin.text.toString().isEmpty()) {
                pin.error = "pin cannot be empty"
                return@setOnClickListener
            }
            textView.text = CreditCardNumberGenerator.generate("5399", 16)

            progressBar.visibility = View.VISIBLE
            firestore.collection("cards").document(textView2.text.toString())
                .set(
                    Card(
                        textView2.text.toString(),
                        pin.text.toString(),
                        10000,
                        CreditCardNumberGenerator.generate("5399", 16)
                    ), SetOptions.merge()
                )
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        progressBar.visibility = View.INVISIBLE
                        Snackbar.make(view, "Card created and credited successfully", Snackbar.LENGTH_LONG).show()
                    } else {
                        progressBar.visibility = View.INVISIBLE
                        Snackbar.make(view, "Could not create card", Snackbar.LENGTH_LONG).show()
                    }
                }
        }
    }

}
