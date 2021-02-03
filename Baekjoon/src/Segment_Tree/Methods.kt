package Segment_Tree

// 구간 합 세그먼트 트리를 LongArray의 형태로 반환, 처음 시작 key는 1
fun makeSumSegmentTree(arr: LongArray, N: Int): LongArray {
    val tree = LongArray(4 * N, { 0L })

    initSumSegmentTree(arr, tree, 0, N - 1, 1)

    return tree
}

// 구간 합 세그먼트 트리에 값 할당
fun initSumSegmentTree(arr: LongArray, tree: LongArray, start: Int, end: Int, key: Int): Long {
    if (start == end) {
        tree[key] = arr[start]

        return tree[key]
    }

    val mid = (start + end) / 2

    val leftSum = initSumSegmentTree(arr, tree, start, mid, key * 2)
    val rightSum = initSumSegmentTree(arr, tree, mid + 1, end, key * 2 + 1)

    tree[key] = leftSum + rightSum

    return tree[key]
}

// Segment Tree의 구간 합 찾는 메서드
// start, end: 데이터의 시작과 끝 0, N - 1
// key: 방문 노드의 key, 처음 부를 때는 루트노드를 의미하는 1을 전달
// left, right: 찾으려는 데이터 구간
fun findSequenceSum(tree: LongArray, start: Int, end: Int, key: Int, left: Int, right: Int): Long{
    if (left > end || right < start) return 0L

    if (left <= start && end <= right) return tree[key]

    val mid = (start + end) / 2

    val leftSum = findSequenceSum(tree, start, mid, key * 2, left, right)
    val rightSum = findSequenceSum(tree, mid + 1, end, key * 2 + 1, left, right)

    return leftSum + rightSum
}

// Segment Tree의 구간합 업데이트하는 메서드
// start, end: 데이터의 시작과 끝 0, N - 1
// key: 방문 노드의 key, 처음 부를 때는 루트노드를 의미하는 1을 전달
// index: 값이 바뀐 노드의 인덱스
// diff: 값이 바뀐 노드의 바뀐 전과 후의 값의 차이 (바뀌기 전 - 바뀐 후)
fun updateSum(tree: LongArray, start: Int, end: Int, key: Int, index: Int, diff: Long) {
    if (index < start || end < index) return

    tree[key] += diff

    if (start == end) return

    val mid = (start + end) / 2

    updateSum(tree, start, mid, key * 2, index, diff)
    updateSum(tree, mid + 1, end, key * 2 + 1, index, diff)
}