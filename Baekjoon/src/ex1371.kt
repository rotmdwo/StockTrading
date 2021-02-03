import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.util.*

fun main() {
    // 문자열 문제
    // 인풋스트림에 읽을 문자가 있는 지 확인하기 위하여 BufferedReader.ready()를 사용
    // ascii 숫자를 구하기 위하여 string[i].toInt()를 사용
    val br = BufferedReader(InputStreamReader(System.`in`))
    val bw = BufferedWriter(OutputStreamWriter(System.out))
    val arr = IntArray(26, {0})

    var st = StringTokenizer(br.readLine())
    while (st.hasMoreTokens()) {
        countLetters(arr, st.nextToken())
    }

    while (br.ready()) {
        st = StringTokenizer(br.readLine())
        while (st.hasMoreTokens()) {
            countLetters(arr, st.nextToken())
        }
    }

    var max = arr[0]
    val maxLetterList = ArrayList<Int>()
    for (i in 1..25) {
        if (max < arr[i]) max = arr[i]
    }

    for (i in 0..25) {
        if (max == arr[i]) maxLetterList.add(i)
    }

    for (i in 0 until maxLetterList.size) {
        bw.write("${maxLetterList[i].plus('a'.toInt()).toChar()}")
    }

    bw.flush()
}

fun countLetters(arr: IntArray, string: String) {
    for (i in string.indices) {
        arr[string[i].toInt().minus('a'.toInt())]++
    }
}