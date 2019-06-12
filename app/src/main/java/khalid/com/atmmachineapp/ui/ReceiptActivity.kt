package khalid.com.atmmachineapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import khalid.com.atmmachineapp.R
import kotlinx.android.synthetic.main.activity_receipt.*

class ReceiptActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_receipt)
        val intent = intent
        textView11.text = intent.getStringExtra("AmtWithdrawn")
        textView13.text = intent.getStringExtra("time")
        textView15.text = intent.getStringExtra("balance")

    }
}
