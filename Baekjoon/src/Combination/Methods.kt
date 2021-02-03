package Combination

import java.math.BigInteger

// originalArray로 주어진 n개의 문자들 중 r개를 골라 오름차순으로 만든 String을 result에 넣어주는 메서드
// 처음 부를 때 tempArray는 originalArray와 같은 순서지만 다른 변수참조로 전달
// 처음 부를 때 r과 q는 같은 값으로 전달
fun combination(originalArray: CharArray, tempArray: CharArray, result: ArrayList<String>,
                n: Int, r: Int, q: Int) {
    if (r == 0) {
        val combinedArray = tempArray.sliceArray(0 until q)
        combinedArray.sort()
        var string = ""
        for (i in 0 until combinedArray.size) string += combinedArray[i]
        result.add(string)
    } else if (n < r) {
        return
    } else {
        tempArray[r - 1] = originalArray[n - 1]
        combination(originalArray, tempArray, result, n - 1, r - 1, q)
        combination(originalArray, tempArray, result, n - 1, r, q)
    }
}

// 조합의 개수 구하는 메서드
private val combinationDP = Array(201, { LongArray(201, { -1 }) })
fun calculateNCr(n: Int, r: Int): Long {
    if (n == r) return 1
    else if (r == 1) return n.toLong()
    else if (combinationDP[n][r] != (-1).toLong()) return combinationDP[n][r]
    else {
        combinationDP[n][r] = calculateNCr(n - 1, r - 1) + calculateNCr(n - 1, r)
        return combinationDP[n][r]
    }
}

// 조합의 개수 구하는 메서드 BigInteger 버전
private val dpBigInteger = Array(201, { Array(201, { BigInteger.ZERO }) })
fun calculateNCrBigInteger(n: Int, r: Int): BigInteger {
    if (n == r) return BigInteger.valueOf(1)
    else if (r == 1) return BigInteger.valueOf(n.toLong())
    else if (dpBigInteger[n][r] != BigInteger.ZERO) return dpBigInteger[n][r]
    else {
        dpBigInteger[n][r] = calculateNCrBigInteger(n - 1, r - 1) + calculateNCrBigInteger(n - 1, r)
        return dpBigInteger[n][r]
    }
}