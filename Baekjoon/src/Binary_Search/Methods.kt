package Binary_Search

// Long을 사용하는 바이너리 서치. O(logN)
fun binarySearch(N: Long): Long {
    var left = 1L
    var right = N
    var mid = (left + right) / 2

    while (left <= right) {
        if (isXPossible(N, mid)) left = mid + 1
        else right = mid - 1
        mid = (left + right) / 2
    }

    return mid
}


fun isXPossible(N: Long, X: Long): Boolean {
    //TODO: binarySearch 메서드에서 mid값이 가능한 지 확인하는 메서드 구현필요
    return true
}

// 오름차순으로 정렬된 arrayList의 적절한 위치에 num을 넣어 정렬상태를 유지시키는 메소드
fun insertNumIntoSortedArrayList(arrayList: ArrayList<Int>, num: Int) {
    // 바이너리 서치 사용, O(logN)
    var left = 0
    var right = arrayList.size - 1
    var mid = (left + right) / 2

    while (left <= right) {
        if (arrayList[mid] < num) left = mid + 1
        else right = mid - 1
        mid = (left + right) / 2
    }

    arrayList.add(left, num)
}