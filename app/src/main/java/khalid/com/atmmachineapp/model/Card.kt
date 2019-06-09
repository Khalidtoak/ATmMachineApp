package khalid.com.atmmachineapp.model

/**
 * Created by  on 6/8/2019.
 */
data class Card(
    val  name :String,
    val pin : String,
    var balance: Int,
    val cardNumber : String
)