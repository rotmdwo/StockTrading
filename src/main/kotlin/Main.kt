import org.openqa.selenium.chrome.ChromeDriver
import stock.Stock
import util.*
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.Exception
import java.sql.*
import java.util.*
import kotlin.system.exitProcess

lateinit var stocks: ArrayList<Stock>
var driver: ChromeDriver? = null

fun main() {
    val br = BufferedReader(InputStreamReader(System.`in`))

    print("DB ID를 입력해주세요: ")
    val id = br.readLine()
    print("DB PW를 입력해주세요: ")
    val pw = br.readLine()
    print("Email PW를 입력해주세요: ")
    val emailPassword = br.readLine()
    val email = Email(emailPassword)
    println()

    try {
        stocks = getAllStocksInfoFromDB(id, pw)
    } catch (e: SQLException) {
        println("DB ID 또는 PW를 잘못 입력하였습니다. 프로그램을 종료합니다.")
        exitProcess(-1)
    }

    //print("계좌 잔액을 입력해주세요: ")
    //val balance = br.readLine().toInt()
    var balance = 0
    print("원하는 명령어를 입력해주세요: ")
    var command = br.readLine()

    try {
        while (command != "quit") {
            when (command) {
                "test" -> {
                    isKospiDown()
                }
                "add new stocks" -> {
                    print("코스닥, 코스피 상위 몇 종목까지?: ")
                    val num = br.readLine().toInt()
                    addNewStocksInfoToDB(num, id, pw)
                }
                "rebalancing" -> {
                    rebalanceStocksInDB(id, pw)
                }
                "access" -> {
                    driver = accessToMiraeWTS(false)
                    balance = getBalance(driver!!)
                    println("현재 계좌잔액은 ${balance}원 입니다.")
                }
                "remote access" -> {
                    driver = accessToMiraeWTS(true)
                    balance = getBalance(driver!!)
                    println("현재 계좌잔액은 ${balance}원 입니다.")
                }
                "trade" -> {
                    driver?.let {// if not null
                        println("자동매매 시작")
                        startAutoTrading(it, stocks, balance, id, pw, email)
                    } ?: run { // if null
                        println("먼저 access 명령어를 통해 WTS에 접속해주세요.")
                    }
                }
                "email" -> {
                    Email(emailPassword)
                        .create()
                        .from("rotmdehf@naver.com", "이승재")
                        .to("rotmdehf@gmail.com")
                        .content("테스트")
                        .attachFile("/Users/sungjaelee/Desktop/Kotlin Practice/StockTrading/build.gradle")
                        .send()
                    //Email(emailPassword).sendEmailWithFile("test", "test!", "/Users/sungjaelee/Desktop/Kotlin Practice/StockTrading/build.gradle")
                }
                //"buy" -> println(buy(driver!!, "027740", 1))
                //"sell" -> println(sell(driver!!, "027740", 1))
                else -> println("지원하지 않는 명령어입니다.")
            }

            print("\n원하는 명령어를 입력해주세요: ")
            command = br.readLine()
        }
    } catch (e: Exception) {
        e.printStackTrace()

        email.notifyError("프로그램 종료됨")
    }


    println("프로그램 종료")
}