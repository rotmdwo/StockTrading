package Trie

import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.util.*
class ex14425 {
    fun main() {
        // 트라이 문제
        // 트라이: String을 트리의 형태로 한 노드에 한 문자 씩 저장, 문자열을 읽을 때 속도: O(M), M은 String 길이
        val br = BufferedReader(InputStreamReader(System.`in`))
        val bw = BufferedWriter(OutputStreamWriter(System.out))
        val st = StringTokenizer(br.readLine())
        val n = st.nextToken().toInt()
        val m = st.nextToken().toInt()
        val root = TrieNode()
        var answer = 0

        for (i in 0 until n) {
            TrieNode.insertWordToTrie(root, br.readLine())
        }

        for (i in 0 until m) {
            if (TrieNode.checkIfTrieHasWord(root, br.readLine())) answer++
        }

        bw.write("${answer}")
        bw.flush()
    }

    // 알파벳 소문자 26개를 표현하는 트라이 자료구조
    class TrieNode() {
        var isEndOfTheWord = false
        val children = Array<TrieNode?>(26, {null})

        companion object {
            // 트라이에 26개 소문자로 이루어진 String 삽입
            fun insertWordToTrie(root: TrieNode, word: String) {
                var node = root

                for (i in 0 until word.length) {
                    val tempNode = node.children[word[i].toInt() - 97]

                    if (tempNode != null) node = tempNode
                    else {
                        val newNode = TrieNode()
                        node.children[word[i].toInt() - 97] = newNode
                        node = newNode
                    }
                }

                node.isEndOfTheWord = true
            }

            // 트라이에서 26개 소문자로 이루어진 String이 존재하는지 확인
            fun checkIfTrieHasWord(root: TrieNode, word: String): Boolean {
                var node = root

                for (i in 0 until word.length) {
                    val tempNode = node.children[word[i].toInt() - 97]
                    if (tempNode != null) node = tempNode
                    else return false
                }

                if (node.isEndOfTheWord) return true
                else return false
            }
        }
    }
}