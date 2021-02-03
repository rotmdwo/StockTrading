import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.util.*
import kotlin.math.roundToInt
import kotlin.math.roundToLong

fun main() {
    val MILLION = 1000

    val br = BufferedReader(InputStreamReader(System.`in`))
    val bw = BufferedWriter(OutputStreamWriter(System.out))
    var st = StringTokenizer(br.readLine())

    val n = st.nextToken().toInt()
    var m = st.nextToken().toInt()

    if (n / 2 < m) m = n - m

    var answer1: Int = 0
    var answer2: Int = 0
    var answer3: Int = 0
    var answer4: Int = 0
    var answer5: Int = 0
    var answer6: Int = 0
    var answer7: Int = 0
    var answer8: Int = 0
    var answer9: Int = 0
    var answer10: Int = 1

    for (i in 1..m) {
        var temp10 = answer10 * (n - i + 1)
        answer10 = temp10 % MILLION
        val carry9 = temp10 / MILLION

        var temp9 = answer9 * (n - i + 1) + carry9
        answer9 = temp9 % MILLION
        val carry8 = temp9 / MILLION

        var temp8 = answer8 * (n - i + 1) + carry8
        answer8 = temp8 % MILLION
        val carry7 = temp8 / MILLION

        var temp7 = answer7 * (n - i + 1) + carry7
        answer7 = temp7 % MILLION
        val carry6 = temp7 / MILLION

        var temp6 = answer6 * (n - i + 1) + carry6
        answer6 = temp6 % MILLION
        val carry5 = temp6 / MILLION

        var temp5 = answer5 * (n - i + 1) + carry5
        answer5 = temp5 % MILLION
        val carry4 = temp5 / MILLION

        var temp4 = answer4 * (n - i + 1) + carry4
        answer4 = temp4 % MILLION
        val carry3 = temp4 / MILLION

        var temp3 = answer3 * (n - i + 1) + carry3
        answer3 = temp3 % MILLION
        val carry2 = temp3 / MILLION

        var temp2 = answer2 * (n - i + 1) + carry2
        answer2 = temp2 % MILLION
        val carry1 = temp2 / MILLION

        var temp1 = answer1 * (n - i + 1) + carry1
        answer1 = temp1 % MILLION

        val t1 = answer1.toDouble() / i
        answer1 = t1.toInt()

        val t2 = (t1 - t1.toLong()) * MILLION + answer2.toDouble() / i
        answer2 = t2.toInt()

        val t3 = (t2 - t2.toLong()) * MILLION + answer3.toDouble() / i
        answer3 = t3.toInt()

        val t4 = (t3 - t3.toLong()) * MILLION + answer4.toDouble() / i
        answer4 = t4.toInt()

        val t5 = (t4 - t4.toLong()) * MILLION + answer5.toDouble() / i
        answer5 = t5.toInt()

        val t6 = (t5 - t5.toLong()) * MILLION + answer6.toDouble() / i
        answer6 = t6.toInt()

        val t7 = (t6 - t6.toLong()) * MILLION + answer7.toDouble() / i
        answer7 = t7.toInt()

        val t8 = (t7 - t7.toLong()) * MILLION + answer8.toDouble() / i
        answer8 = t8.toInt()

        val t9 = (t8 - t8.toLong()) * MILLION + answer9.toDouble() / i
        answer9 = t9.toInt()

        val t10 = (t9 - t9.toLong()) * MILLION + answer10.toDouble() / i
        answer10 = t10.roundToInt()



        lateinit var formattedString: String
        if (answer1 != 0) {
            formattedString = String.format("%d%03d%03d%03d%03d%03d%03d%03d%03d%03d",
                    answer1, answer2, answer3, answer4, answer5, answer6, answer7, answer8,
            answer9, answer10)
        } else if (answer2 != 0) {
            formattedString = String.format("%d%03d%03d%03d%03d%03d%03d%03d%03d",
                    answer2, answer3, answer4, answer5, answer6, answer7, answer8,
                    answer9, answer10)
        } else if (answer3 != 0) {
            formattedString = String.format("%d%03d%03d%03d%03d%03d%03d%03d",
                    answer3, answer4, answer5, answer6, answer7, answer8,
                    answer9, answer10)
        } else if (answer4 != 0) {
            formattedString = String.format("%d%03d%03d%03d%03d%03d%03d",
                    answer4, answer5, answer6, answer7, answer8,
                    answer9, answer10)
        } else if (answer5 != 0){
            formattedString = String.format("%d%03d%03d%03d%03d%03d",
                    answer5, answer6, answer7, answer8, answer9, answer10)
        } else if (answer6 != 0){
            formattedString = String.format("%d%03d%03d%03d%03d",
                    answer6, answer7, answer8, answer9, answer10)
        } else if (answer7 != 0){
            formattedString = String.format("%d%03d%03d%03d",
                    answer7, answer8, answer9, answer10)
        } else if (answer8 != 0){
            formattedString = String.format("%d%03d%03d", answer8, answer9, answer10)
        } else if (answer9 != 0){
            formattedString = String.format("%d%03d", answer9, answer10)
        } else {
            formattedString = String.format("%d", answer10)
        }

        bw.write("${t10} ${formattedString}\n")
    }


    bw.flush()
}
