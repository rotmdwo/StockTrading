package Coupang

class Coupang_2020_FALL_4 {
    fun main() {
        val roads = arrayOf(arrayOf("ULSAN", "BUSAN"),arrayOf("DAEJEON","ULSAN"),
                arrayOf("DAEJEON","GWANGJU"),arrayOf("SEOUL", "DAEJEON"),
                arrayOf("SEOUL","ULSAN"),arrayOf("DAEJEON","DAEGU"),
                arrayOf("GWANGJU","BUSAN"),arrayOf("DAEGU","GWANGJU"),
                arrayOf("DAEGU","BUSAN"),arrayOf("ULSAN","DAEGU"),
                arrayOf("GWANGJU", "YEOSU"),arrayOf("BUSAN","YEOSU"))
        print(solution4("SEOUL", "DAEGU", "YEOSU",roads = roads))
    }
    val answer = intArrayOf(0, 0)
    fun solution4(depar: String, hub:String, dest : String, roads: Array<Array<String>>): Int {

        var hashMap = HashMap<String, Node1>()

        for (i in 0 until roads.size) {
            var src = roads[i][0]
            var dst = roads[i][1]

            var nodeSrc = hashMap[src]
            var nodeDst = hashMap[dst]



            if(nodeSrc != null) {
                if (nodeDst != null) {
                    nodeSrc.dst.add(nodeDst)
                    if (src == "ULSAN") {
                        println("ASD")
                    }

                } else {
                    var newDst = Node1(dest)
                    nodeSrc.dst.add(newDst)
                    hashMap[dst] = newDst
                }
            } else {
                if (nodeDst !=null) {
                    var newSrc = Node1(src)
                    newSrc.dst.add(nodeDst)
                    hashMap[src] = newSrc

                } else {
                    var newSrc = Node1(src)
                    var newDst = Node1(dst)
                    newSrc.dst.add(newDst)
                    hashMap[src] = newSrc
                    hashMap[dst] = newDst
                }
            }

        }

        for (i in 0 until hashMap["ULSAN"]!!.dst.size) {
            println("ULSAN -> ${hashMap["ULSAN"]!!.dst[i].key}")
        }
        var startNode = hashMap[depar]
        val hubNode = hashMap[hub]
        if (startNode == null || hubNode == null) return 0
        else {
            dps(hashMap, depar, hub, 0)
            //dps(hashMap, hub, dest, 1)
        }


        return answer[0]
    }
    class Node1(var key: String) {
        var dst = ArrayList<Node1>()
    }

    fun dps(hashMap: HashMap<String, Node1>, depar: String, dest: String, stage: Int) {
        //println(depar)
        if (depar == dest) {
            //println()
            answer[stage]++
            return
        }

        var startNode = hashMap[depar]

        if (startNode == null){
            //println("null")
            return
        }
        for (i in 0 until startNode.dst.size) {
            //println("${depar} -> ${startNode.dst[i].key}")
        }

        for (i in 0 until startNode.dst.size) {
            dps(hashMap, startNode.dst[i].key, dest, stage)
        }
    }
}