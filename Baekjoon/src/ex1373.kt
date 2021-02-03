import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.lang.StringBuilder
import java.util.*

fun main() {
    // 2진법 -> 8진법으로 변경 문제
    // 2진법 3자리 마다 8진법 한 자리로 바꾸되,
    // 먼저 2진법 수의 길이를 파악해 맨 앞에 0 ~ 2자리를 8진법으로 한 자리로 변경
    // 문자열의 변경이 잦기 때문에 String이 아닌 StringBuilder를 사용하는 게 관건
    val br = BufferedReader(InputStreamReader(System.`in`))
    val bw = BufferedWriter(OutputStreamWriter(System.out))

    var binaryStr = StringTokenizer(br.readLine()).nextToken()
    val binaryLength = binaryStr.length
    val quotient = binaryLength / 3
    val remainder = binaryLength % 3
    // 이어 붙이는 + 연산이 많은 String은 StringBuilder를 사용해야 메모리, 시간에 효율적
    var answer = StringBuilder("")

    if (remainder == 1) {
        val octal = binaryStr[0].minus('0')
        answer.append(octal)
        binaryStr = binaryStr.substring(1, binaryStr.length)
    } else if (remainder == 2){
        val octal = 2 * binaryStr[0].minus('0') + binaryStr[1].minus('0')
        answer.append(octal)
        binaryStr = binaryStr.substring(2, binaryStr.length)
    }
    for (i in 0 until quotient) {
        val octal = 4 * binaryStr[3 * i].minus('0') +
                2 * binaryStr[3 * i + 1].minus('0') +
                binaryStr[3 * i + 2].minus('0')

        answer.append(octal)
    }

    bw.write("${answer}")
    bw.flush()
}