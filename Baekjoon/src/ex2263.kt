import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

fun main() {
    // inorder, postorder tree traversal 순서 주어졌을 때 preorder traversal 순서 찾는 문제
    // preorder: 자기 자신 프린트 - 왼쪽 노드 방문 - 오른쪽 노드 방문
    // inorder: 왼쪽 노드 방문 - 자기 자신 프린트 - 오른쪽 노드 방문
    // postorder: 왼쪽 노드 방문 - 오른쪽 노드 방문 - 자기 자신 프린트
    // 문제 푼 방식: inorder를 순회하며 어떤 수가 inorder에서의 인덱스보다 postorder에서의 인덱스가 더 크면
    // 어떤 수의 inorder에서의 인덱스를 postorder의 인덱스로 옮기고 한 칸 씩 땡김
    // 그리고 postorder의 인덱스에 있던 inorder의 수를 기존 inorder에서의 어떤 수의 right child로 만듬
    // 이 과정에서 parent, child 업데이트

    // ArrayList vs LinkedList: ArrayList는 인덱싱 할 때 O(1)이지만 remove나 add를 할 때 O(N)
    // LinkedList는 인덱싱 할 때 O(N)이지만 remove나 add를 할 때 O(1)
    // 배열의 인덱싱을 O(N)이므로, 순서가 바뀌지 않는 배열의 인덱싱이 자주 필요하면 HashMap을 쓰면 좋다
    val bw = BufferedWriter(OutputStreamWriter(System.out))
    val br = BufferedReader(InputStreamReader(System.`in`))
    val N = StringTokenizer(br.readLine()).nextToken().toInt()
    var st = StringTokenizer(br.readLine())
    val inOrderArr = ArrayList<Int>()
    val postOrderArr = IntArray(N + 1)
    val tree = Array(N + 1, {Node(it)})
    val hashMap = HashMap<Int, Int>()

    // dummy adding
    inOrderArr.add(0)

    for (i in 1..N) inOrderArr.add(st.nextToken().toInt())
    st = StringTokenizer(br.readLine())
    for (i in 1..N) {
        postOrderArr[i] = st.nextToken().toInt()
        hashMap[postOrderArr[i]] = i
    }

    var rootNode = tree[inOrderArr[N]]

    for (i in 2..N) {
        tree[inOrderArr[i]].left = tree[inOrderArr[i - 1]]
        tree[inOrderArr[i - 1]].parent = tree[inOrderArr[i]]
    }

    var index = 1
    while (index <= N) {
        val key = inOrderArr[index]
        val postIndex = hashMap[key]!!

        // inOrderArr의 수가 postOrderArr의 수보다 앞에 있을 때 한 칸씩 당김
        if (index < postIndex) {
            // 루트노드 재설정
            if (inOrderArr[postIndex] == rootNode.key) rootNode = tree[key]

            tree[inOrderArr[index + 1]].left = null
            tree[key].right = tree[inOrderArr[postIndex]]

            tree[inOrderArr[postIndex]].parent?.let {
                if (it.right == tree[inOrderArr[postIndex]]) it.right = tree[key]
                else it.left = tree[key]
            }
            tree[inOrderArr[postIndex]].parent = tree[key]

            inOrderArr.removeAt(index)
            inOrderArr.add(postIndex, key)
        } else index += 1
    }

    traverseTreeByPreorder(rootNode, bw)
    bw.flush()
}

fun traverseTreeByPreorder(node: Node, bw: BufferedWriter) {
    bw.write("${node.key} ")

    node.left?.let { traverseTreeByPreorder(it, bw) }
    node.right?.let { traverseTreeByPreorder(it, bw) }
}

data class Node(val key: Int) {
    var parent: Node? = null
    var left: Node? = null
    var right: Node? = null
}