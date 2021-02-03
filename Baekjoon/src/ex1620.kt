import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.util.*
import kotlin.collections.HashMap

fun main() {
    // 해시맵 문제
    val br = BufferedReader(InputStreamReader(System.`in`))
    val bw = BufferedWriter(OutputStreamWriter(System.out))
    val st = StringTokenizer(br.readLine())
    val N = st.nextToken().toInt()
    val M = st.nextToken().toInt()
    val map = HashMap<String, String>(2 * N)

    for (i in 1 .. N) {
        val name = StringTokenizer(br.readLine()).nextToken()

        map.put(i.toString(), name)
        map.put(name, i.toString())
    }

    for (i in 1 .. M) {
        val string = StringTokenizer(br.readLine()).nextToken()
        bw.write("${map[string]}\n")
    }

    bw.flush()
}