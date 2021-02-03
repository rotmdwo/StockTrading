package Naver_2020_FALL

fun main() {
    println(solution1("kkaxbycyz", "abc"))
}

fun solution1(m:String, k: String): String {
    var answer = StringBuilder(m)
    var index = 0

    for (i in 0 until k.length){
        var letter = k[i]

        while (index < m.length){
            if(answer[index] ==letter){
                println(letter)
                println("${answer.substring(0, index)} ${answer.substring(index + 1, answer.lastIndex + 1)}")
                answer = java.lang.StringBuilder(answer.substring(0, index) + answer.substring(index + 1, answer.lastIndex + 1))
                break
            }  else index++
        }

    }
    return answer.toString()
}


