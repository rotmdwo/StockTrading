import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

fun main() {
    val runtime = Runtime.getRuntime()

    //process = runtime.exec("open /Users/sungjaelee/Documents/자소서/이승재5-16.jpg")
    //process = runtime.exec("open /Users/sungjaelee/Documents/자소서/라인_자소서.docx")
    val process = runtime.exec("python /Users/sungjaelee/Downloads/4주차_과제_1.py")

    val br = BufferedReader(InputStreamReader(process.inputStream))
    val bw = BufferedWriter(OutputStreamWriter(System.out))
    var textFromPython = br.readLine()
    while (textFromPython != null) {
        bw.write("${textFromPython}\n")
        textFromPython = br.readLine()
    }
    process.waitFor()
    process.destroy()


    bw.flush()
}