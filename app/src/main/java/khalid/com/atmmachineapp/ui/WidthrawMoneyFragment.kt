package khalid.com.atmmachineapp.ui

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.snackbar.Snackbar
import khalid.com.atmmachineapp.R
import khalid.com.atmmachineapp.model.Card
import kotlinx.android.synthetic.main.widthraw_money_fragment.*


class WidthrawMoneyFragment : FireBaseFragment(){

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
        // Use the ViewModel
        val name = name_text.text.toString()
        textView6.setOnClickListener {view ->
            if(name_text.text.toString().isEmpty()){
                name_text.error = "Name of card is needed to find card"
                return@setOnClickListener
            }
            progressBar2.visibility = View.VISIBLE
            val query = firestore.collection("cards").whereEqualTo("name", name)
            query.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                if (firebaseFirestoreException!=null){
                    Snackbar.make(
                        view, "Something went wrong, please try withdrawing again", Snackbar.LENGTH_LONG
                    )
                    progressBar2.visibility= View.INVISIBLE
                }
                else{
                    querySnapshot!!.forEach {
                        val cardInfo = it.toObject(Card::class.java)
                        textView4.text = cardInfo.name
                        textView5.text = cardInfo.cardNumber
                        button.isEnabled =true
                    }
                }




            }
        }
    }

}
