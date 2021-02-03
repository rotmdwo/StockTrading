package Trie

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