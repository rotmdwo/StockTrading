package Kakao_2020_Blind

fun main() {
    val info = arrayOf("java backend junior pizza 150", "java backend junior pizza 150", "java backend junior pizza 150", "java backend junior pizza 150", "java backend junior pizza 150")
    val query = arrayOf("java and backend and junior and pizza 100","python and frontend and senior and chicken 200","cpp and - and senior and pizza 250","- and backend and senior and - 150","- and - and - and chicken 100","- and - and - and - 0")
    val array = solution3(info, query)
    for (i in 0 until array.size) println("${array[i]}")
}

fun solution3(info: Array<String>, query: Array<String>): IntArray {
    var answer = IntArray(query.size)
    val root = Root()

    for (i in 0 until info.size) {
        val list = info[i].split(" ")
        insertItemToTree(root, list)
    }

    root.cpp.backend.junior.chicken.sortWith(MyComparator)
    root.cpp.backend.junior.pizza.sortWith(MyComparator)
    root.cpp.backend.senior.chicken.sortWith(MyComparator)
    root.cpp.backend.senior.pizza.sortWith(MyComparator)
    root.cpp.frontend.junior.chicken.sortWith(MyComparator)
    root.cpp.frontend.junior.pizza.sortWith(MyComparator)
    root.cpp.frontend.senior.chicken.sortWith(MyComparator)
    root.cpp.frontend.senior.pizza.sortWith(MyComparator)
    root.jav.backend.junior.chicken.sortWith(MyComparator)
    root.jav.backend.junior.pizza.sortWith(MyComparator)
    root.jav.backend.senior.chicken.sortWith(MyComparator)
    root.jav.backend.senior.pizza.sortWith(MyComparator)
    root.jav.frontend.junior.chicken.sortWith(MyComparator)
    root.jav.frontend.junior.pizza.sortWith(MyComparator)
    root.jav.frontend.senior.chicken.sortWith(MyComparator)
    root.jav.frontend.senior.pizza.sortWith(MyComparator)
    root.python.backend.junior.chicken.sortWith(MyComparator)
    root.python.backend.junior.pizza.sortWith(MyComparator)
    root.python.backend.senior.chicken.sortWith(MyComparator)
    root.python.backend.senior.pizza.sortWith(MyComparator)
    root.python.frontend.junior.chicken.sortWith(MyComparator)
    root.python.frontend.junior.pizza.sortWith(MyComparator)
    root.python.frontend.senior.chicken.sortWith(MyComparator)
    root.python.frontend.senior.pizza.sortWith(MyComparator)

    for (i in 0 until query.size) {
        val list = query[i].split(" ")
        answer[i] = getNumber(root, list)
    }

    return answer
}

fun getNumber(root: Root, list: List<String>): Int {
    val lang = list[0]
    val end = list[2]
    val career = list[4]
    val food = list[6]
    val score = list[7].toInt()

    if (lang == "cpp") {
        if (end == "backend") {
            if (career == "junior") {
                if (food == "chicken") {
                    return binarySearch(root.cpp.backend.junior.chicken, score)
                } else if (food == "pizza") {
                    return binarySearch(root.cpp.backend.junior.pizza, score)
                } else {
                    return binarySearch(root.cpp.backend.junior.chicken, score) + binarySearch(root.cpp.backend.junior.pizza, score)
                }
            } else if (career == "senior") {
                if (food == "chicken") {
                    return binarySearch(root.cpp.backend.senior.chicken, score)
                } else if (food == "pizza") {
                    return binarySearch(root.cpp.backend.senior.pizza, score)
                } else {
                    return binarySearch(root.cpp.backend.senior.chicken, score) + binarySearch(root.cpp.backend.senior.pizza, score)
                }
            } else {
                if (food == "chicken") {
                    return binarySearch(root.cpp.backend.junior.chicken, score) + binarySearch(root.cpp.backend.senior.chicken, score)
                } else if (food == "pizza") {
                    return binarySearch(root.cpp.backend.junior.pizza, score) + binarySearch(root.cpp.backend.senior.pizza, score)
                } else {
                    return binarySearch(root.cpp.backend.junior.chicken, score) + binarySearch(root.cpp.backend.senior.chicken, score) + binarySearch(root.cpp.backend.junior.pizza, score) + binarySearch(root.cpp.backend.senior.pizza, score)
                }
            }
        } else if (end == "frontend") {
            if (career == "junior") {
                if (food == "chicken") {
                    return binarySearch(root.cpp.frontend.junior.chicken, score)
                } else if (food == "pizza") {
                    return binarySearch(root.cpp.frontend.junior.pizza, score)
                } else {
                    return binarySearch(root.cpp.frontend.junior.chicken, score) + binarySearch(root.cpp.frontend.junior.pizza, score)
                }
            } else if (career == "senior") {
                if (food == "chicken") {
                    return binarySearch(root.cpp.frontend.senior.chicken, score)
                } else if (food == "pizza") {
                    return binarySearch(root.cpp.frontend.senior.pizza, score)
                } else {
                    return binarySearch(root.cpp.frontend.senior.chicken, score) + binarySearch(root.cpp.frontend.senior.pizza, score)
                }
            } else {
                if (food == "chicken") {
                    return binarySearch(root.cpp.frontend.junior.chicken, score) + binarySearch(root.cpp.frontend.senior.chicken, score)
                } else if (food == "pizza") {
                    return binarySearch(root.cpp.frontend.junior.pizza, score) + binarySearch(root.cpp.frontend.senior.pizza, score)
                } else {
                    return binarySearch(root.cpp.frontend.junior.chicken, score) + binarySearch(root.cpp.frontend.senior.chicken, score) + binarySearch(root.cpp.frontend.junior.pizza, score) + binarySearch(root.cpp.frontend.senior.pizza, score)
                }
            }
        } else { // 프,백
            if (career == "junior") {
                if (food == "chicken") {
                    return binarySearch(root.cpp.frontend.junior.chicken, score) + binarySearch(root.cpp.backend.junior.chicken, score)
                } else if (food == "pizza") {
                    return binarySearch(root.cpp.frontend.junior.pizza, score) + binarySearch(root.cpp.backend.junior.pizza, score)
                } else {
                    return binarySearch(root.cpp.frontend.junior.chicken, score) + binarySearch(root.cpp.backend.junior.chicken, score) + binarySearch(root.cpp.frontend.junior.pizza, score) + binarySearch(root.cpp.backend.junior.pizza, score)
                }
            } else if (career == "senior") {
                if (food == "chicken") {
                    return binarySearch(root.cpp.frontend.senior.chicken, score) + binarySearch(root.cpp.backend.senior.chicken, score)
                } else if (food == "pizza") {
                    return binarySearch(root.cpp.frontend.senior.pizza, score) + binarySearch(root.cpp.backend.senior.pizza, score)
                } else {
                    return binarySearch(root.cpp.frontend.senior.chicken, score) + binarySearch(root.cpp.backend.senior.chicken, score) + binarySearch(root.cpp.frontend.senior.pizza, score) + binarySearch(root.cpp.backend.senior.pizza, score)
                }
            } else { // 프백, 시주
                if (food == "chicken") {
                    return binarySearch(root.cpp.frontend.senior.chicken, score) + binarySearch(root.cpp.backend.senior.chicken, score) + binarySearch(root.cpp.frontend.junior.chicken, score) + binarySearch(root.cpp.backend.junior.chicken, score)
                } else if (food == "pizza") {
                    return binarySearch(root.cpp.frontend.senior.pizza, score) + binarySearch(root.cpp.backend.senior.pizza, score) + binarySearch(root.cpp.frontend.junior.pizza, score) + binarySearch(root.cpp.backend.junior.pizza, score)
                } else {
                    return binarySearch(root.cpp.frontend.senior.chicken, score) + binarySearch(root.cpp.backend.senior.chicken, score) + binarySearch(root.cpp.frontend.junior.chicken, score) + binarySearch(root.cpp.backend.junior.chicken, score) + binarySearch(root.cpp.frontend.senior.pizza, score) + binarySearch(root.cpp.backend.senior.pizza, score) + binarySearch(root.cpp.frontend.junior.pizza, score) + binarySearch(root.cpp.backend.junior.pizza, score)
                }
            }
        }
    } else if (lang == "java") {
        if (end == "backend") {
            if (career == "junior") {
                if (food == "chicken") {
                    return binarySearch(root.jav.backend.junior.chicken, score)
                } else if (food == "pizza") {
                    return binarySearch(root.jav.backend.junior.pizza, score)
                } else {
                    return binarySearch(root.jav.backend.junior.chicken, score) + binarySearch(root.jav.backend.junior.pizza, score)
                }
            } else if (career == "senior") {
                if (food == "chicken") {
                    return binarySearch(root.jav.backend.senior.chicken, score)
                } else if (food == "pizza") {
                    return binarySearch(root.jav.backend.senior.pizza, score)
                } else {
                    return binarySearch(root.jav.backend.senior.chicken, score) + binarySearch(root.jav.backend.senior.pizza, score)
                }
            } else {
                if (food == "chicken") {
                    return binarySearch(root.jav.backend.junior.chicken, score) + binarySearch(root.jav.backend.senior.chicken, score)
                } else if (food == "pizza") {
                    return binarySearch(root.jav.backend.junior.pizza, score) + binarySearch(root.jav.backend.senior.pizza, score)
                } else {
                    return binarySearch(root.jav.backend.junior.chicken, score) + binarySearch(root.jav.backend.senior.chicken, score) + binarySearch(root.jav.backend.junior.pizza, score) + binarySearch(root.jav.backend.senior.pizza, score)
                }
            }
        } else if (end == "frontend") {
            if (career == "junior") {
                if (food == "chicken") {
                    return binarySearch(root.jav.frontend.junior.chicken, score)
                } else if (food == "pizza") {
                    return binarySearch(root.jav.frontend.junior.pizza, score)
                } else {
                    return binarySearch(root.jav.frontend.junior.chicken, score) + binarySearch(root.jav.frontend.junior.pizza, score)
                }
            } else if (career == "senior") {
                if (food == "chicken") {
                    return binarySearch(root.jav.frontend.senior.chicken, score)
                } else if (food == "pizza") {
                    return binarySearch(root.jav.frontend.senior.pizza, score)
                } else {
                    return binarySearch(root.jav.frontend.senior.chicken, score) + binarySearch(root.jav.frontend.senior.pizza, score)
                }
            } else {
                if (food == "chicken") {
                    return binarySearch(root.jav.frontend.junior.chicken, score) + binarySearch(root.jav.frontend.senior.chicken, score)
                } else if (food == "pizza") {
                    return binarySearch(root.jav.frontend.junior.pizza, score) + binarySearch(root.jav.frontend.senior.pizza, score)
                } else {
                    return binarySearch(root.jav.frontend.junior.chicken, score) + binarySearch(root.jav.frontend.senior.chicken, score) + binarySearch(root.jav.frontend.junior.pizza, score) + binarySearch(root.jav.frontend.senior.pizza, score)
                }
            }
        } else { // 프,백
            if (career == "junior") {
                if (food == "chicken") {
                    return binarySearch(root.jav.frontend.junior.chicken, score) + binarySearch(root.jav.backend.junior.chicken, score)
                } else if (food == "pizza") {
                    return binarySearch(root.jav.frontend.junior.pizza, score) + binarySearch(root.jav.backend.junior.pizza, score)
                } else {
                    return binarySearch(root.jav.frontend.junior.chicken, score) + binarySearch(root.jav.backend.junior.chicken, score) + binarySearch(root.jav.frontend.junior.pizza, score) + binarySearch(root.jav.backend.junior.pizza, score)
                }
            } else if (career == "senior") {
                if (food == "chicken") {
                    return binarySearch(root.jav.frontend.senior.chicken, score) + binarySearch(root.jav.backend.senior.chicken, score)
                } else if (food == "pizza") {
                    return binarySearch(root.jav.frontend.senior.pizza, score) + binarySearch(root.jav.backend.senior.pizza, score)
                } else {
                    return binarySearch(root.jav.frontend.senior.chicken, score) + binarySearch(root.jav.backend.senior.chicken, score) + binarySearch(root.jav.frontend.senior.pizza, score) + binarySearch(root.jav.backend.senior.pizza, score)
                }
            } else { // 프백, 시주
                if (food == "chicken") {
                    return binarySearch(root.jav.frontend.senior.chicken, score) + binarySearch(root.jav.backend.senior.chicken, score) + binarySearch(root.jav.frontend.junior.chicken, score) + binarySearch(root.jav.backend.junior.chicken, score)
                } else if (food == "pizza") {
                    return binarySearch(root.jav.frontend.senior.pizza, score) + binarySearch(root.jav.backend.senior.pizza, score) + binarySearch(root.jav.frontend.junior.pizza, score) + binarySearch(root.jav.backend.junior.pizza, score)
                } else {
                    return binarySearch(root.jav.frontend.senior.chicken, score) + binarySearch(root.jav.backend.senior.chicken, score) + binarySearch(root.jav.frontend.junior.chicken, score) + binarySearch(root.jav.backend.junior.chicken, score) + binarySearch(root.jav.frontend.senior.pizza, score) + binarySearch(root.jav.backend.senior.pizza, score) + binarySearch(root.jav.frontend.junior.pizza, score) + binarySearch(root.jav.backend.junior.pizza, score)
                }
            }
        }
    } else if (lang == "python") {
        if (end == "backend") {
            if (career == "junior") {
                if (food == "chicken") {
                    return binarySearch(root.python.backend.junior.chicken, score)
                } else if (food == "pizza") {
                    return binarySearch(root.python.backend.junior.pizza, score)
                } else {
                    return binarySearch(root.python.backend.junior.chicken, score) + binarySearch(root.python.backend.junior.pizza, score)
                }
            } else if (career == "senior") {
                if (food == "chicken") {
                    return binarySearch(root.python.backend.senior.chicken, score)
                } else if (food == "pizza") {
                    return binarySearch(root.python.backend.senior.pizza, score)
                } else {
                    return binarySearch(root.python.backend.senior.chicken, score) + binarySearch(root.python.backend.senior.pizza, score)
                }
            } else {
                if (food == "chicken") {
                    return binarySearch(root.python.backend.junior.chicken, score) + binarySearch(root.python.backend.senior.chicken, score)
                } else if (food == "pizza") {
                    return binarySearch(root.python.backend.junior.pizza, score) + binarySearch(root.python.backend.senior.pizza, score)
                } else {
                    return binarySearch(root.python.backend.junior.chicken, score) + binarySearch(root.python.backend.senior.chicken, score) + binarySearch(root.python.backend.junior.pizza, score) + binarySearch(root.python.backend.senior.pizza, score)
                }
            }
        } else if (end == "frontend") {
            if (career == "junior") {
                if (food == "chicken") {
                    return binarySearch(root.python.frontend.junior.chicken, score)
                } else if (food == "pizza") {
                    return binarySearch(root.python.frontend.junior.pizza, score)
                } else {
                    return binarySearch(root.python.frontend.junior.chicken, score) + binarySearch(root.python.frontend.junior.pizza, score)
                }
            } else if (career == "senior") {
                if (food == "chicken") {
                    return binarySearch(root.python.frontend.senior.chicken, score)
                } else if (food == "pizza") {
                    return binarySearch(root.python.frontend.senior.pizza, score)
                } else {
                    return binarySearch(root.python.frontend.senior.chicken, score) + binarySearch(root.python.frontend.senior.pizza, score)
                }
            } else {
                if (food == "chicken") {
                    return binarySearch(root.python.frontend.junior.chicken, score) + binarySearch(root.python.frontend.senior.chicken, score)
                } else if (food == "pizza") {
                    return binarySearch(root.python.frontend.junior.pizza, score) + binarySearch(root.python.frontend.senior.pizza, score)
                } else {
                    return binarySearch(root.python.frontend.junior.chicken, score) + binarySearch(root.python.frontend.senior.chicken, score) + binarySearch(root.python.frontend.junior.pizza, score) + binarySearch(root.python.frontend.senior.pizza, score)
                }
            }
        } else { // 프,백
            if (career == "junior") {
                if (food == "chicken") {
                    return binarySearch(root.python.frontend.junior.chicken, score) + binarySearch(root.python.backend.junior.chicken, score)
                } else if (food == "pizza") {
                    return binarySearch(root.python.frontend.junior.pizza, score) + binarySearch(root.python.backend.junior.pizza, score)
                } else {
                    return binarySearch(root.python.frontend.junior.chicken, score) + binarySearch(root.python.backend.junior.chicken, score) + binarySearch(root.python.frontend.junior.pizza, score) + binarySearch(root.python.backend.junior.pizza, score)
                }
            } else if (career == "senior") {
                if (food == "chicken") {
                    return binarySearch(root.python.frontend.senior.chicken, score) + binarySearch(root.python.backend.senior.chicken, score)
                } else if (food == "pizza") {
                    return binarySearch(root.python.frontend.senior.pizza, score) + binarySearch(root.python.backend.senior.pizza, score)
                } else {
                    return binarySearch(root.python.frontend.senior.chicken, score) + binarySearch(root.python.backend.senior.chicken, score) + binarySearch(root.python.frontend.senior.pizza, score) + binarySearch(root.python.backend.senior.pizza, score)
                }
            } else { // 프백, 시주
                if (food == "chicken") {
                    return binarySearch(root.python.frontend.senior.chicken, score) + binarySearch(root.python.backend.senior.chicken, score) + binarySearch(root.python.frontend.junior.chicken, score) + binarySearch(root.python.backend.junior.chicken, score)
                } else if (food == "pizza") {
                    return binarySearch(root.python.frontend.senior.pizza, score) + binarySearch(root.python.backend.senior.pizza, score) + binarySearch(root.python.frontend.junior.pizza, score) + binarySearch(root.python.backend.junior.pizza, score)
                } else {
                    return binarySearch(root.python.frontend.senior.chicken, score) + binarySearch(root.python.backend.senior.chicken, score) + binarySearch(root.python.frontend.junior.chicken, score) + binarySearch(root.python.backend.junior.chicken, score) + binarySearch(root.python.frontend.senior.pizza, score) + binarySearch(root.python.backend.senior.pizza, score) + binarySearch(root.python.frontend.junior.pizza, score) + binarySearch(root.python.backend.junior.pizza, score)
                }
            }
        }
    } else {
        if (end == "backend") {
            if (career == "junior") {
                if (food == "chicken") {
                    return binarySearch(root.python.backend.junior.chicken, score) + binarySearch(root.jav.backend.junior.chicken, score) + binarySearch(root.cpp.backend.junior.chicken, score)
                } else if (food == "pizza") {
                    return binarySearch(root.python.backend.junior.pizza, score) + binarySearch(root.jav.backend.junior.pizza, score) + binarySearch(root.cpp.backend.junior.pizza, score)
                } else {
                    return binarySearch(root.python.backend.junior.chicken, score) + binarySearch(root.python.backend.junior.pizza, score) + binarySearch(root.jav.backend.junior.chicken, score) + binarySearch(root.jav.backend.junior.pizza, score) + binarySearch(root.cpp.backend.junior.chicken, score) + binarySearch(root.cpp.backend.junior.pizza, score)
                }
            } else if (career == "senior") {
                if (food == "chicken") {
                    return (binarySearch(root.python.backend.senior.chicken, score)
                    + binarySearch(root.jav.backend.senior.chicken, score)
                    + binarySearch(root.cpp.backend.senior.chicken, score))
                } else if (food == "pizza") {
                    return (binarySearch(root.python.backend.senior.pizza, score)
                            + binarySearch(root.jav.backend.senior.pizza, score)
                            + binarySearch(root.cpp.backend.senior.pizza, score))
                } else {
                    return (binarySearch(root.python.backend.senior.chicken, score) + binarySearch(root.python.backend.senior.pizza, score)
                            + binarySearch(root.jav.backend.senior.chicken, score) + binarySearch(root.jav.backend.senior.pizza, score)
                            + binarySearch(root.cpp.backend.senior.chicken, score) + binarySearch(root.cpp.backend.senior.pizza, score))
                }
            } else {
                if (food == "chicken") {
                    return (binarySearch(root.python.backend.junior.chicken, score) + binarySearch(root.python.backend.senior.chicken, score)
                            + binarySearch(root.jav.backend.junior.chicken, score) + binarySearch(root.jav.backend.senior.chicken, score)
                            + binarySearch(root.cpp.backend.junior.chicken, score) + binarySearch(root.cpp.backend.senior.chicken, score))

                } else if (food == "pizza") {
                    return (binarySearch(root.python.backend.junior.pizza, score) + binarySearch(root.python.backend.senior.pizza, score)
                            + binarySearch(root.jav.backend.junior.pizza, score) + binarySearch(root.jav.backend.senior.pizza, score)
                            + binarySearch(root.cpp.backend.junior.pizza, score) + binarySearch(root.cpp.backend.senior.pizza, score))
                } else {
                    return (binarySearch(root.python.backend.junior.chicken, score) + binarySearch(root.python.backend.senior.chicken, score) + binarySearch(root.python.backend.junior.pizza, score) + binarySearch(root.python.backend.senior.pizza, score)
                            + binarySearch(root.jav.backend.junior.chicken, score) + binarySearch(root.jav.backend.senior.chicken, score) + binarySearch(root.jav.backend.junior.pizza, score) + binarySearch(root.jav.backend.senior.pizza, score)
                            + binarySearch(root.cpp.backend.junior.chicken, score) + binarySearch(root.cpp.backend.senior.chicken, score) + binarySearch(root.cpp.backend.junior.pizza, score) + binarySearch(root.cpp.backend.senior.pizza, score))
                }
            }
        } else if (end == "frontend") {
            if (career == "junior") {
                if (food == "chicken") {
                    return (binarySearch(root.python.frontend.junior.chicken, score)
                            + binarySearch(root.jav.frontend.junior.chicken, score)
                            + binarySearch(root.cpp.frontend.junior.chicken, score))
                } else if (food == "pizza") {
                    return (binarySearch(root.python.frontend.junior.pizza, score)
                            + binarySearch(root.jav.frontend.junior.pizza, score)
                            + binarySearch(root.cpp.frontend.junior.pizza, score))
                } else {
                    return (binarySearch(root.python.frontend.junior.chicken, score) + binarySearch(root.python.frontend.junior.pizza, score)
                            + binarySearch(root.jav.frontend.junior.chicken, score) + binarySearch(root.jav.frontend.junior.pizza, score)
                            + binarySearch(root.cpp.frontend.junior.chicken, score) + binarySearch(root.cpp.frontend.junior.pizza, score))
                }
            } else if (career == "senior") {
                if (food == "chicken") {
                    return (binarySearch(root.python.frontend.senior.chicken, score)
                            + binarySearch(root.jav.frontend.senior.chicken, score)
                            + binarySearch(root.cpp.frontend.senior.chicken, score))
                } else if (food == "pizza") {
                    return (binarySearch(root.python.frontend.senior.pizza, score)
                            + binarySearch(root.jav.frontend.senior.pizza, score)
                            + binarySearch(root.cpp.frontend.senior.pizza, score))
                } else {
                    return (binarySearch(root.python.frontend.senior.chicken, score) + binarySearch(root.python.frontend.senior.pizza, score)
                            + binarySearch(root.jav.frontend.senior.chicken, score) + binarySearch(root.jav.frontend.senior.pizza, score)
                            + binarySearch(root.cpp.frontend.senior.chicken, score) + binarySearch(root.cpp.frontend.senior.pizza, score))
                }
            } else {
                if (food == "chicken") {
                    return (binarySearch(root.python.frontend.junior.chicken, score) + binarySearch(root.python.frontend.senior.chicken, score)
                            + binarySearch(root.jav.frontend.junior.chicken, score) + binarySearch(root.jav.frontend.senior.chicken, score)
                            + binarySearch(root.cpp.frontend.junior.chicken, score) + binarySearch(root.cpp.frontend.senior.chicken, score))
                } else if (food == "pizza") {
                    return (binarySearch(root.python.frontend.junior.pizza, score) + binarySearch(root.python.frontend.senior.pizza, score)
                            + binarySearch(root.jav.frontend.junior.pizza, score) + binarySearch(root.jav.frontend.senior.pizza, score)
                            + binarySearch(root.cpp.frontend.junior.pizza, score) + binarySearch(root.cpp.frontend.senior.pizza, score))
                } else {
                    return (binarySearch(root.python.frontend.junior.chicken, score) + binarySearch(root.python.frontend.senior.chicken, score) + binarySearch(root.python.frontend.junior.pizza, score) + binarySearch(root.python.frontend.senior.pizza, score)
                            + binarySearch(root.jav.frontend.junior.chicken, score) + binarySearch(root.jav.frontend.senior.chicken, score) + binarySearch(root.jav.frontend.junior.pizza, score) + binarySearch(root.jav.frontend.senior.pizza, score)
                            + binarySearch(root.cpp.frontend.junior.chicken, score) + binarySearch(root.cpp.frontend.senior.chicken, score) + binarySearch(root.cpp.frontend.junior.pizza, score) + binarySearch(root.cpp.frontend.senior.pizza, score))
                }
            }
        } else { // 프,백
            if (career == "junior") {
                if (food == "chicken") {
                    return (binarySearch(root.python.frontend.junior.chicken, score) + binarySearch(root.python.backend.junior.chicken, score)
                            + binarySearch(root.jav.frontend.junior.chicken, score) + binarySearch(root.jav.backend.junior.chicken, score)
                            + binarySearch(root.cpp.frontend.junior.chicken, score) + binarySearch(root.cpp.backend.junior.chicken, score))
                } else if (food == "pizza") {
                    return (binarySearch(root.python.frontend.junior.pizza, score) + binarySearch(root.python.backend.junior.pizza, score)
                            + binarySearch(root.jav.frontend.junior.pizza, score) + binarySearch(root.jav.backend.junior.pizza, score)
                            + binarySearch(root.cpp.frontend.junior.pizza, score) + binarySearch(root.cpp.backend.junior.pizza, score))
                } else {
                    return (binarySearch(root.python.frontend.junior.chicken, score) + binarySearch(root.python.backend.junior.chicken, score) + binarySearch(root.python.frontend.junior.pizza, score) + binarySearch(root.python.backend.junior.pizza, score)
                            + binarySearch(root.jav.frontend.junior.chicken, score) + binarySearch(root.jav.backend.junior.chicken, score) + binarySearch(root.jav.frontend.junior.pizza, score) + binarySearch(root.jav.backend.junior.pizza, score)
                            + binarySearch(root.cpp.frontend.junior.chicken, score) + binarySearch(root.cpp.backend.junior.chicken, score) + binarySearch(root.cpp.frontend.junior.pizza, score) + binarySearch(root.cpp.backend.junior.pizza, score))
                }
            } else if (career == "senior") {
                if (food == "chicken") {
                    return (binarySearch(root.python.frontend.senior.chicken, score) + binarySearch(root.python.backend.senior.chicken, score)
                            + binarySearch(root.jav.frontend.senior.chicken, score) + binarySearch(root.jav.backend.senior.chicken, score)
                            + binarySearch(root.cpp.frontend.senior.chicken, score) + binarySearch(root.cpp.backend.senior.chicken, score))
                } else if (food == "pizza") {
                    return (binarySearch(root.python.frontend.senior.pizza, score) + binarySearch(root.python.backend.senior.pizza, score)
                            + binarySearch(root.jav.frontend.senior.pizza, score) + binarySearch(root.jav.backend.senior.pizza, score)
                            + binarySearch(root.cpp.frontend.senior.pizza, score) + binarySearch(root.cpp.backend.senior.pizza, score))
                } else {
                    return (binarySearch(root.python.frontend.senior.chicken, score) + binarySearch(root.python.backend.senior.chicken, score) + binarySearch(root.python.frontend.senior.pizza, score) + binarySearch(root.python.backend.senior.pizza, score)
                            + binarySearch(root.jav.frontend.senior.chicken, score) + binarySearch(root.jav.backend.senior.chicken, score) + binarySearch(root.jav.frontend.senior.pizza, score) + binarySearch(root.jav.backend.senior.pizza, score)
                            + binarySearch(root.cpp.frontend.senior.chicken, score) + binarySearch(root.cpp.backend.senior.chicken, score) + binarySearch(root.cpp.frontend.senior.pizza, score) + binarySearch(root.cpp.backend.senior.pizza, score))
                }
            } else { // 프백, 시주
                if (food == "chicken") {
                    return (binarySearch(root.python.frontend.senior.chicken, score) + binarySearch(root.python.backend.senior.chicken, score) + binarySearch(root.python.frontend.junior.chicken, score) + binarySearch(root.python.backend.junior.chicken, score)
                            + binarySearch(root.jav.frontend.senior.chicken, score) + binarySearch(root.jav.backend.senior.chicken, score) + binarySearch(root.jav.frontend.junior.chicken, score) + binarySearch(root.jav.backend.junior.chicken, score)
                            + binarySearch(root.cpp.frontend.senior.chicken, score) + binarySearch(root.cpp.backend.senior.chicken, score) + binarySearch(root.cpp.frontend.junior.chicken, score) + binarySearch(root.cpp.backend.junior.chicken, score))
                } else if (food == "pizza") {
                    return (binarySearch(root.python.frontend.senior.pizza, score) + binarySearch(root.python.backend.senior.pizza, score) + binarySearch(root.python.frontend.junior.pizza, score) + binarySearch(root.python.backend.junior.pizza, score)
                            + binarySearch(root.jav.frontend.senior.pizza, score) + binarySearch(root.jav.backend.senior.pizza, score) + binarySearch(root.jav.frontend.junior.pizza, score) + binarySearch(root.jav.backend.junior.pizza, score)
                            + binarySearch(root.cpp.frontend.senior.pizza, score) + binarySearch(root.cpp.backend.senior.pizza, score) + binarySearch(root.cpp.frontend.junior.pizza, score) + binarySearch(root.cpp.backend.junior.pizza, score))
                } else {
                    return (binarySearch(root.python.frontend.senior.chicken, score) + binarySearch(root.python.backend.senior.chicken, score) + binarySearch(root.python.frontend.junior.chicken, score) + binarySearch(root.python.backend.junior.chicken, score) + binarySearch(root.python.frontend.senior.pizza, score) + binarySearch(root.python.backend.senior.pizza, score) + binarySearch(root.python.frontend.junior.pizza, score) + binarySearch(root.python.backend.junior.pizza, score)
                            + binarySearch(root.jav.frontend.senior.chicken, score) + binarySearch(root.jav.backend.senior.chicken, score) + binarySearch(root.jav.frontend.junior.chicken, score) + binarySearch(root.jav.backend.junior.chicken, score) + binarySearch(root.jav.frontend.senior.pizza, score) + binarySearch(root.jav.backend.senior.pizza, score) + binarySearch(root.jav.frontend.junior.pizza, score) + binarySearch(root.jav.backend.junior.pizza, score)
                            + binarySearch(root.cpp.frontend.senior.chicken, score) + binarySearch(root.cpp.backend.senior.chicken, score) + binarySearch(root.cpp.frontend.junior.chicken, score) + binarySearch(root.cpp.backend.junior.chicken, score) + binarySearch(root.cpp.frontend.senior.pizza, score) + binarySearch(root.cpp.backend.senior.pizza, score) + binarySearch(root.cpp.frontend.junior.pizza, score) + binarySearch(root.cpp.backend.junior.pizza, score))
                }
            }
        }
    }
}

fun binarySearch(foodList: ArrayList<Food>, score: Int): Int {
    var left = 0
    var right = foodList.size - 1
    var mid = (left + right) / 2

    while (left <= right) {
        if (foodList[mid].score < score) left = mid + 1
        else right = mid - 1
        mid = (left + right) / 2
    }

    if (foodList.size > mid && foodList[mid].score >= score) return foodList.size - mid
    else if (foodList.size - 1 > mid && foodList[mid].score < score) return foodList.size - mid - 1
    else return 0
}

object MyComparator: Comparator<Food> {
    override fun compare(o1: Food, o2: Food): Int {
        if (o1.score < o2.score) return -1
        else if (o1.score > o2.score) return 1
        else return 0
    }

}
fun insertItemToTree(root: Root, list: List<String>) {
    val lang = list[0]
    val end = list[1]
    val career = list[2]
    val food = list[3]
    val score = list[4].toInt()

    if (lang == "cpp") {
        if (end == "backend") {
            if (career == "junior") {
                if (food == "chicken") {
                    root.cpp.backend.junior.chicken.add(Food(score))
                } else { // pizza
                    root.cpp.backend.junior.pizza.add(Food(score))
                }
            } else { // senior
                if (food == "chicken") {
                    root.cpp.backend.senior.chicken.add(Food(score))
                } else { // pizza
                    root.cpp.backend.senior.pizza.add(Food(score))
                }
            }
        } else { //frontend
            if (career == "junior") {
                if (food == "chicken") {
                    root.cpp.frontend.junior.chicken.add(Food(score))
                } else { // pizza
                    root.cpp.frontend.junior.pizza.add(Food(score))
                }
            } else { // senior
                if (food == "chicken") {
                    root.cpp.frontend.senior.chicken.add(Food(score))
                } else { // pizza
                    root.cpp.frontend.senior.pizza.add(Food(score))
                }
            }
        }
    } else if (lang == "java") {
        if (end == "backend") {
            if (career == "junior") {
                if (food == "chicken") {
                    root.jav.backend.junior.chicken.add(Food(score))
                } else { // pizza
                    root.jav.backend.junior.pizza.add(Food(score))
                }
            } else { // senior
                if (food == "chicken") {
                    root.jav.backend.senior.chicken.add(Food(score))
                } else { // pizza
                    root.jav.backend.senior.pizza.add(Food(score))
                }
            }
        } else { //frontend
            if (career == "junior") {
                if (food == "chicken") {
                    root.jav.frontend.junior.chicken.add(Food(score))
                } else { // pizza
                    root.jav.frontend.junior.pizza.add(Food(score))
                }
            } else { // senior
                if (food == "chicken") {
                    root.jav.frontend.senior.chicken.add(Food(score))
                } else { // pizza
                    root.jav.frontend.senior.pizza.add(Food(score))
                }
            }
        }
    } else { //python
        if (end == "backend") {
            if (career == "junior") {
                if (food == "chicken") {
                    root.python.backend.junior.chicken.add(Food(score))
                } else { // pizza
                    root.python.backend.junior.pizza.add(Food(score))
                }
            } else { // senior
                if (food == "chicken") {
                    root.python.backend.senior.chicken.add(Food(score))
                } else { // pizza
                    root.python.backend.senior.pizza.add(Food(score))
                }
            }
        } else { //frontend
            if (career == "junior") {
                if (food == "chicken") {
                    root.python.frontend.junior.chicken.add(Food(score))
                } else { // pizza
                    root.python.frontend.junior.pizza.add(Food(score))
                }
            } else { // senior
                if (food == "chicken") {
                    root.python.frontend.senior.chicken.add(Food(score))
                } else { // pizza
                    root.python.frontend.senior.pizza.add(Food(score))
                }
            }
        }
    }
}

class Root {
    val cpp = Language()
    val jav = Language()
    val python = Language()
}
class Language {
    val backend = End()
    val frontend = End()
}
class End {
    val junior = Career()
    val senior = Career()
}
class Career {
    val chicken = ArrayList<Food>()
    val pizza = ArrayList<Food>()
}

data class Food(val score: Int)

