package Kakao_2020_Blind

fun main() {
    print(solution1("...!@BaT#*..y.abcdefghijklm"))
}

fun solution1(new_id: String): String {
    var answer: String = new_id

    answer = answer.toLowerCase()

    var index = 0
    while (index < answer.length) {
        val char = answer[index]
        if (char == '-' || char == '_' || char == '.') index++
        else if (char.toInt() in 48..57) index++
        else if (char.toInt() in 97..122) index++
        else answer = answer.removeRange(index..index)
    }

    index = 0
    while (index < answer.length - 1) {
        if (answer.substring(index, index + 2) == "..") answer = answer.removeRange(index..index)
        else index++
    }

    if (answer.lastIndex >= 0 && answer[0] == '.') answer = answer.removeRange(0..0)
    if (answer.lastIndex >= 0 && answer[answer.lastIndex] == '.') answer = answer.removeRange(answer.lastIndex..answer.lastIndex)

    if (answer.isEmpty()) answer = "a"

    if (answer.length > 15) {
        answer = answer.substring(0, 15)
        if (answer[answer.lastIndex] == '.') answer = answer.substring(0, 14)
    }

    if (answer.length <= 2) {
        val lastChar = answer[answer.lastIndex]

        while (answer.length < 3) answer += lastChar
    }

    return answer
}