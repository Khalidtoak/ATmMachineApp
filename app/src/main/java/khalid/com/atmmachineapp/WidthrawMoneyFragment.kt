package khalid.com.atmmachineapp

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


class WidthrawMoneyFragment : Fragment() {

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
        // TODO: Use the ViewModel
    }

}
