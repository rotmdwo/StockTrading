import stock.Stock
import util.*
import java.io.BufferedReader
import java.io.InputStreamReader
import java.sql.*
import java.util.*
import kotlin.system.exitProcess

lateinit var stocks: ArrayList<Stock>

fun main() {
    val br = BufferedReader(InputStreamReader(System.`in`))

    print("DB ID를 입력해주세요: ")
    val id = br.readLine()
    print("DB PW를 입력해주세요: ")
    val pw = br.readLine()
    println()

    try {
        stocks = getAllStocksInfoFromDB(id, pw)
    } catch (e: SQLException) {
        println("DB ID 또는 PW를 잘못 입력하였습니다. 프로그램을 종료합니다.")
        exitProcess(-1)
    }


    print("원하는 명령어를 입력해주세요: ")
    var command = br.readLine()

    while (command != "quit") {
        when (command) {
            "access" -> accessToMiraeWTS()
            "begin algorithm" -> startAutoTrading(stocks)
            else -> println("지원하지 않는 명령어입니다.")
        }

        print("\n원하는 명령어를 입력해주세요: ")
        command = br.readLine()
    }

    println("프로그램 종료")
}