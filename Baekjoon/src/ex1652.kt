import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.util.*

fun main() {
    val br = BufferedReader(InputStreamReader(System.`in`))
    val bw = BufferedWriter(OutputStreamWriter(System.out))

    val N = StringTokenizer(br.readLine()).nextToken().toInt()
    val mat = Array<String>(N, {""})

    var answerHorizontal = 0
    var answerVertical = 0

    for (i in 0 until N) {
        mat[i] = StringTokenizer(br.readLine()).nextToken()
    }

    for (i in 0 until N) {
        var series = 0
        for (j in 0 until N) {
            if (mat[i][j] == '.') series++
            else {
                if (series > 1) answerHorizontal++

                series = 0
            }
        }

        if (series > 1) answerHorizontal++
    }

    for (j in 0 until N) {
        var series = 0
        for (i in 0 until N) {
            if (mat[i][j] == '.') series++
            else {
                if (series > 1) answerVertical++

                series = 0
            }
        }

        if (series > 1) answerVertical++
    }

    bw.write("${answerHorizontal} ${answerVertical}")
    bw.flush()
}