package khalid.com.atmmachineapp.utils

/**
 * Created by khalid on 6/8/2019.
 */
import java.util.Random

object CreditCardNumberGenerator {

    private val random = Random(System.currentTimeMillis())

    fun generate(bin: String, length: Int): String {

        val randomNumberLength = length - (bin.length + 1)

        val builder = StringBuilder(bin)
        for (i in 0 until randomNumberLength) {
            val digit = random.nextInt(10)
            builder.append(digit)
        }

        val checkDigit = getCheckDigit(builder.toString())
        builder.append(checkDigit)

        return builder.toString()
    }

    private fun getCheckDigit(number: String): Int {
        var sum = 0
        for (i in 0 until number.length) {

            // Get the digit at the current position.
            var digit = Integer.parseInt(number.substring(i, i + 1))

            if (i % 2 == 0) {
                digit *= 2
                if (digit > 9) {
                    digit = digit / 10 + digit % 10
                }
            }
            sum += digit
        }
        val mod = sum % 10
        return if (mod == 0) 0 else 10 - mod
    }
}
