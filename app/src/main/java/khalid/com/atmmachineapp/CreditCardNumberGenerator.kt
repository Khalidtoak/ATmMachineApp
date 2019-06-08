package khalid.com.atmmachineapp

/**
 * Created by  on 6/8/2019.
 */
import java.util.Random

/**
 * A credit card number generator.
 *
 * @author Josef Galea
 */
object CreditCardNumberGenerator {

    private val random = Random(System.currentTimeMillis())

    /**
     * Generates a random valid credit card number. For more information about
     * the credit card number generation algorithms and credit card numbers
     * refer to [Everything
 * you ever wanted to know about CC's](http://euro.ecom.cmu.edu/resources/elibrary/everycc.htm), [Graham King's blog](http://www.darkcoding.net/credit-card/), and
     * [This is How Credit Card Numbers Are Generated](http://codytaylor.org/2009/11/this-is-how-credit-card-numbers-are-generated.html)
     *
     * @param bin
     * The bank identification number, a set digits at the start of the credit card
     * number, used to identify the bank that is issuing the credit card.
     * @param length
     * The total length (i.e. including the BIN) of the credit card number.
     * @return
     * A randomly generated, valid, credit card number.
     */
    fun generate(bin: String, length: Int): String {

        // The number of random digits that we need to generate is equal to the
        // total length of the card number minus the start digits given by the
        // user, minus the check digit at the end.
        val randomNumberLength = length - (bin.length + 1)

        val builder = StringBuilder(bin)
        for (i in 0 until randomNumberLength) {
            val digit = this.random.nextInt(10)
            builder.append(digit)
        }

        // Do the Luhn algorithm to generate the check digit.
        val checkDigit = this.getCheckDigit(builder.toString())
        builder.append(checkDigit)

        return builder.toString()
    }

    /**
     * Generates the check digit required to make the given credit card number
     * valid (i.e. pass the Luhn check)
     *
     * @param number
     * The credit card number for which to generate the check digit.
     * @return The check digit required to make the given credit card number
     * valid.
     */
    private fun getCheckDigit(number: String): Int {

        // Get the sum of all the digits, however we need to replace the value
        // of the first digit, and every other digit, with the same digit
        // multiplied by 2. If this multiplication yields a number greater
        // than 9, then add the two digits together to get a single digit
        // number.
        //
        // The digits we need to replace will be those in an even position for
        // card numbers whose length is an even number, or those is an odd
        // position for card numbers whose length is an odd number. This is
        // because the Luhn algorithm reverses the card number, and doubles
        // every other number starting from the second number from the last
        // position.
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

        // The check digit is the number required to make the sum a multiple of
        // 10.
        val mod = sum % 10
        return if (mod == 0) 0 else 10 - mod
    }
}
