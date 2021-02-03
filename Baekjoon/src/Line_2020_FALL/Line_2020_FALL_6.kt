package Line_2020_FALL

fun main() {

}

fun solution6(companies: Array<String>, applicants: Array<String>): Array<String> {
    var answer = Array<String>(companies.size, {""})
    val rejectedList = ArrayList<Char>()
    val numOfApplied = IntArray(applicants.size, {0})
    val numOfAccepted = IntArray(companies.size, {0})
    var tempList = ArrayList<String>()

    for (i in 0 until companies.size) answer[i] = companies[i][0].toString() + "_"
    for (i in 0 until applicants.size) rejectedList.add(applicants[i][0])

    while (rejectedList.isNotEmpty()) {
        val appliedSet = Array<HashSet<Char>>(companies.size, { HashSet() })

        for (i in 0 until rejectedList.size) {
            val applicant = rejectedList[i]
            var index = 0

            for (j in 0 until applicants.size) {
                if (applicants[j][0] == applicant) {
                    index = j
                    break
                }
            }


            if (applicants[index][applicants[index].lastIndex].toInt() > numOfApplied[index]) {
                // 더 입사지원
                val companyToApply = applicants[index][2 + numOfApplied[index]]

                for (j in 0 until companies.size) {
                    if (companies[j][0] == companyToApply) {
                        appliedSet[j].add(applicant)
                        numOfApplied[index] ++
                        break
                    }
                }

            } else { // 입사지원 초과
                rejectedList.removeAt(i)
            }

        }

        for (i in 0 until companies.size) {
            for (j in 2..companies[i][companies[i].lastIndex - 2].toInt()) {
                if (numOfAccepted[i] < companies[i][companies[i].lastIndex].toInt()) {
                    if (appliedSet[i].contains(companies[i][j])) {
                        rejectedList.remove(companies[i][j])
                        numOfApplied[i]++
                        var string = "${answer[i]}" + companies[i][j]
                        tempList.add(string)
                    }
                } else break

            }
        }
    }

    answer.sort()


    return answer
}