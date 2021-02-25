package util

import javafx.concurrent.Task
import org.jsoup.Jsoup
import org.openqa.selenium.By
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.interactions.Actions
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait
import stock.Stock
import java.io.BufferedReader
import java.io.FileWriter
import java.io.InputStreamReader
import java.lang.Math.abs
import java.text.SimpleDateFormat
import kotlin.collections.ArrayList
import java.util.concurrent.*

fun accessToMiraeWTS(isRemote: Boolean): ChromeDriver {
    System.setProperty("webdriver.chrome.driver", "/Users/sungjaelee/Downloads/chromedriver")
    val options = ChromeOptions()
    options.setCapability("ignoreProtectedModeSettings", true)
    val driver = ChromeDriver(options)
    val url = "https://www.miraeassetdaewoo.com/"

    // 메인 페이지 접속
    driver.get(url)

    // 프레임 선택
    driver.switchTo().frame(driver.findElementById("contentframe"))
    // 로그인 클릭
    val login_box = driver.findElementByClassName("login_box")
    login_box.findElement(By.tagName("a")).click()

    if (!isRemote) { // 원격접속 - 간편인증 사용
        // QR 로그인 선택
        driver.findElementByClassName("menu_02").click()

        // 자동 로그아웃 시간 7시간으로 설정
        Thread.sleep(4000L)
        driver.findElementByClassName("select_val").click()
        driver.findElementById("420").click()

        // QR 인증 11초 대기
        Thread.sleep(11000L)
    } else { // 로컬접속 - QR인증 사용
        // 간편인증 로그인 선택
        driver.findElementByClassName("menu_01").click()

        // 자동 로그아웃 시간 7시간으로 설정
        Thread.sleep(4000L)
        driver.findElementByClassName("select_val").click()
        driver.findElementById("420").click()

        // 간편인증 120초 대기
        Thread.sleep(120000L)
    }

    // 트레이딩 탭 클릭
    val quick_menu = driver.findElementByClassName("quick_menu")
    val links = quick_menu.findElements(By.tagName("a"))
    links[3].click()

    // 새로 띄어지는 창으로 focus 옮김
    val windows = driver.windowHandles
    driver.switchTo().window(windows.last())

    // frame 선택
    driver.switchTo().frame(driver.findElementById("contentframe"))
    // 로딩 대기 후 주식종합 탭 클릭
    Thread.sleep(4000L)
    //waitForDisplayingById(driver, "mdiMenu_mdi0100")
    driver.findElementById("mdiMenu_mdi0100").click()

    // 로딩 대기 후 매수 탭 클릭
    //Thread.sleep(1000L)
    waitForDisplayingById(driver, "ui-id-21")
    driver.findElementById("ui-id-21").click()

    println("매수탭과 매도탭의 계좌를 선택하고 비밀번호를 입력해주세요.")

    return driver
}

fun closeWindowThenReenterWTS(driver: ChromeDriver) {
    driver.close()
    var windows = driver.windowHandles
    driver.switchTo().window(windows.last())

    // frame 선택
    driver.switchTo().frame(driver.findElementById("contentframe"))

    // 트레이딩 탭 클릭
    val quick_menu = driver.findElementByClassName("quick_menu")
    val links = quick_menu.findElements(By.tagName("a"))
    links[3].click()

    // 새로 띄어지는 창으로 focus 옮김
    windows = driver.windowHandles
    driver.switchTo().window(windows.last())

    // frame 선택
    driver.switchTo().frame(driver.findElementById("contentframe"))
    // 로딩 대기 후 주식종합 탭 클릭
    Thread.sleep(4000L)
    //waitForDisplayingById(driver, "mdiMenu_mdi0100")
    driver.findElementById("mdiMenu_mdi0100").click()

    // 로딩 대기 후 매수 탭 클릭
    //Thread.sleep(1000L)
    waitForDisplayingById(driver, "ui-id-21")
    driver.findElementById("ui-id-21").click()

    println("오류발생 후 재접속 완료. 매수탭과 매도탭의 계좌를 선택하고 비밀번호를 입력해주세요.")
}

fun getBalance(driver: ChromeDriver): Int {
    Thread.sleep(2000L)
    driver.findElementById("ui-id-11").click()
    Thread.sleep(3000L)
    saveHtmlAsTxt(driver, "deposit_tab.txt")

    return getPriceWithoutComma(driver.findElementsByTagName("td")[158].text)
}

// HTML을 텍스트파일로 저장
fun saveHtmlAsTxt(driver: ChromeDriver, path: String) {
    // ex) path = "html.txt"
    val fWriter = FileWriter(path)
    val str = driver.executeScript("return document.documentElement.innerHTML") as String
    fWriter.write(str)
    fWriter.close()
}

fun test() {
    val executor = Executors.newSingleThreadExecutor()
    val future = executor.submit(Callable {
        val br = BufferedReader(InputStreamReader(System.`in`))
        println("인풋: ")
        br.readLine()
    })

    var result: String? = null

    while (true) {
        try {
            println("1")
            result = future.get(3, TimeUnit.SECONDS)
            println("2")
            if (result != null) println(result)
            println("3")
        } catch (e: Exception) {
            println("시간초과")
        }
    }


}

// 매수 가격 반환
fun buy(driver: ChromeDriver, stock: Stock, quantity: Int, balance: Int, user: String, pw: String): Int {
    // 매수 탭 클릭
    waitForDisplayingById(driver, "ui-id-21")
    driver.findElementById("ui-id-21").click()

    // 종목코드 입력
    //Thread.sleep(2000L)
    waitForDisplayingById(driver, "search_num")
    driver.findElementById("search_num").sendKeys(stock.code)

    // 시장가 선택
    driver.findElementsByName("orderOp_0100")[0].click()
    Thread.sleep(250L)
    //driver.findElementById("unitPrice_0100").sendKeys("0")
    driver.findElementsByName("orderOp_0100")[1].click()

    // 매수량 설정
    driver.findElementById("mesuQty_0100").sendKeys("$quantity")

    // 매수 버튼 클릭
    driver.findElementById("mesu_0100").click()

    // 매수 확인
    //Thread.sleep(500L)
    waitForDisplayingById(driver, "confirm")
    driver.findElementById("confirm").click()

    //Thread.sleep(500L)
    waitForDisplayingById(driver, "grid_diax0002")
    saveHtmlAsTxt(driver, "html_bought.txt")
    //println(driver.findElementById("grid_diax0002").text)
    val price = getPriceWithoutComma(driver.findElementById("grid_diax0002")
            .findElements(By.className("pq-grid-cell"))[5].text)
            //.findElements(By.xpath("//td[contains(@class, 'pq-grid-cell') and contains(@class, 'pq-align-right') and contains(@class, 'low')]"))[28].text)

    val newBalance = updateDbAfterBuy(stock, quantity, price, balance, user, pw)

    //waitForDisplayingById(driver, "messageBox_1002Ok")
    //driver.findElementById("messageBox_1002Ok").click()
    // 확인버튼의 랜덤 id 해결책
    try {
        val buttons = driver.findElementsByTagName("button")
        for (button in buttons) {
            val id = button.getAttribute("id")

            if (id.contains("Ok")) {
                //waitForClickableById(driver, id)
                //button.click()
                Actions(driver).moveToElement(button).click(button).perform()
                break
            }
        }
    } catch (e: Exception) {
        closeWindowThenReenterWTS(driver)
        do {
            println("계좌 비밀번호를 입력하셨습니까? (y/n) : ")
        } while (BufferedReader(InputStreamReader(System.`in`)).readLine() != "y")
    }

    return newBalance
}

// 매도 가격 반환
fun sell(driver: ChromeDriver, stock: Stock, quantity: Int, balance: Int, user: String, pw: String): Int {
    // 매도 탭 클릭
    waitForDisplayingById(driver, "ui-id-22")
    driver.findElementById("ui-id-22").click()

    // 종목코드 입력
    //Thread.sleep(2000L)
    waitForDisplayingById(driver, "search_num")
    driver.findElementsById("search_num")[1].sendKeys(stock.code)

    // 시장가 선택
    driver.findElementsByName("orderOp1_0100")[0].click()
    Thread.sleep(250L)
    //driver.findElementById("unitPrice_0100").sendKeys("0")
    driver.findElementsByName("orderOp1_0100")[1].click()

    // 매도량 설정
    driver.findElementById("qtyMedo_0100").sendKeys("$quantity")

    // 매도 버튼 클릭
    driver.findElementById("medo_0100").click()
    //Thread.sleep(500L)
    waitForDisplayingById(driver, "confirm")
    driver.findElementById("confirm").click()

    //Thread.sleep(500L)
    waitForDisplayingById(driver, "grid_diax0002")
    saveHtmlAsTxt(driver, "html_sold.txt")
    val price = getPriceWithoutComma(driver.findElementById("grid_diax0002")
            .findElements(By.className("pq-grid-cell"))[5].text)
            //.findElements(By.xpath("//td[contains(@class, 'pq-grid-cell') and contains(@class, 'pq-align-right') and contains(@class, 'low')]"))[28].text)

    val newBalance = updateDbAfterSell(stock, quantity, price, balance, user, pw)

    //waitForDisplayingById(driver, "messageBox_1002Ok")
    //driver.findElementById("messageBox_1002Ok").click()
    try {
        val buttons = driver.findElementsByTagName("button")
        for (button in buttons) {
            val id = button.getAttribute("id")

            if (id.contains("Ok")) {
                //waitForClickableById(driver, id)
                //button.click()
                Actions(driver).moveToElement(button).click(button).perform()
                break
            }
        }
    } catch (e: Exception) {
        closeWindowThenReenterWTS(driver)
        do {
            println("계좌 비밀번호를 입력하셨습니까? (y/n) : ")
        } while (BufferedReader(InputStreamReader(System.`in`)).readLine() != "y")
    }

    return newBalance
}

fun updateDbAfterBuy(stock: Stock, boughtQuantity: Int, boughtPrice: Int, balance: Int, user: String, pw: String): Int {
    stock.averagePrice = (stock.averagePrice * stock.quantity + boughtPrice * boughtQuantity) / (stock.quantity + boughtQuantity)
    stock.quantity += boughtQuantity
    stock.lastSoldPoint = 0.0
    stock.lastTradingDate = System.currentTimeMillis()
    val newBalance = balance - boughtQuantity * boughtPrice

    updateStockQuantityAtDB(stock, user, pw)
    addLogToDB(stock, "buy", boughtPrice, boughtQuantity, null, user, pw)

    println("==============================")
    println("${stock.name}(${stock.code}) 개 당 ${boughtPrice}원에 ${boughtQuantity}주 매수 > 계좌 잔액 ${newBalance}원")
    println("==============================")

    return newBalance
}

fun updateDbAfterSell(stock: Stock, soldQuantity: Int, soldPrice: Int, balance: Int, user: String, pw: String): Int {
    val profit = ((soldPrice - stock.averagePrice) * soldQuantity - soldPrice * soldQuantity * 0.0025).toInt()
    val newBalance = balance + (soldPrice * soldQuantity * 0.9975).toInt()

    stock.quantity -= soldQuantity
    if (stock.quantity == 0) stock.averagePrice = 0
    stock.lastTradingDate = System.currentTimeMillis()

    updateStockQuantityAtDB(stock, user, pw)
    addLogToDB(stock, "sell", soldPrice, soldQuantity, profit, user, pw)

    println("==============================")
    println("${stock.name}(${stock.code}) 개 당 ${soldPrice}원에 ${soldQuantity}주 매도 > 계좌 잔액 ${newBalance}원")
    println("==============================")

    return newBalance
}

fun startAutoTrading(driver: ChromeDriver, stocks: ArrayList<Stock>, bal: Int, user: String, pw: String) {
    var balance = bal
    val dayInMillisecond = 1000 * 60 * 60 * 24L
    val format = SimpleDateFormat("HH:mm:ss")
    val openingTime = "09:00:00"
    val closingTime = "15:20:00"
    var currentTime = format.format(System.currentTimeMillis())

    val lastDayMA = Array(stocks.size, {IntArray(2)})
    val dayBeforeLastDayMA = Array(stocks.size, {IntArray(2)})
    val isNewStockArray = BooleanArray(stocks.size, {false})

    for (i in 0 until stocks.size) {
        isNewStockArray[i] = isNewStock(stocks[i].code)
    }

    // 전날 기준 이동평균값 가져옴
    for (i in 0 until stocks.size) {
        if (isNewStockArray[i]) continue
        val lastDayMA5 = getMovingAverageOfLastDay5(stocks[i].code)
        val lastDayMA20 = getMovingAverageOfLastDay20(stocks[i].code)
        lastDayMA[i] = intArrayOf(lastDayMA5, lastDayMA20)
    }

    // 이틀전 기준 이동평균값 가져옴
    for (i in 0 until stocks.size) {
        if (isNewStockArray[i]) continue
        val dayBeforeLastDayMA5 = getMovingAverageOfDayBeforeLastDay5(stocks[i].code)
        val dayBeforeLastDayMA20 = getMovingAverageOfDayBeforeLastDay20(stocks[i].code)
        dayBeforeLastDayMA[i] = intArrayOf(dayBeforeLastDayMA5, dayBeforeLastDayMA20)
    }

    // 개장전 대기
    while (currentTime < openingTime) {
        Thread.sleep(500L)
        currentTime = format.format(System.currentTimeMillis())
    }

    while (openingTime <= currentTime && currentTime < closingTime) {
        for (i in 0 until stocks.size) {
            if (isNewStockArray[i]) continue

            val stock = stocks[i]
            val dayBeforeLastDay5 = dayBeforeLastDayMA[i][0]
            val dayBeforeLastDay20 = dayBeforeLastDayMA[i][1]
            val lastDay5 = lastDayMA[i][0]
            val lastDay20 = lastDayMA[i][1]
            val current5 = getMovingAverage5(stock.code)
            val current20 = getMovingAverage20(stock.code)
            val price = getCurrentPrice(stock.code)

            if (abs((current5 - current20)/current20.toDouble()) < 0.01) {
                println("${stock.name} 단기MA: ${current5} 중기MA: ${current20}")
            }

            // buy - 골든크로스
            if (stock.quantity == 0 && dayBeforeLastDay5 < dayBeforeLastDay20 && lastDay5 < lastDay20 &&
                    dayBeforeLastDay5 < lastDay5 && lastDay5 < current5 && current20 < current5 &&
                    (System.currentTimeMillis() - stock.lastTradingDate) / dayInMillisecond.toDouble() > 1.0 &&
                    price < balance && 500000 <= balance) {
                val boughtQuantity = if (price > 500000) 1 else 500000 / price
                balance = buy(driver, stock, boughtQuantity, balance, user, pw)
            }
            // sell - 데드크로스
            else if (stock.quantity > 0 && lastDay20 < lastDay5 && current5 < current20 &&
                (System.currentTimeMillis() - stock.lastTradingDate) / dayInMillisecond.toDouble() > 1.0 &&
                (price - stock.averagePrice) / stock.averagePrice.toDouble() > 0.005) {
                val soldQuantity = stock.quantity
                stock.lastSoldPoint = (price - stock.averagePrice) / stock.averagePrice.toDouble() * 100
                balance = sell(driver, stock, soldQuantity, balance, user, pw)
            }
            // sell : +4% 이익
            else if (stock.quantity > 0 && (price - stock.averagePrice) / stock.averagePrice.toDouble() > 0.04 &&
                    stock.lastSoldPoint < 4.0) {
                val soldQuantity = (stock.quantity * 1 / 7).coerceAtLeast(1)
                stock.lastSoldPoint = 4.0
                balance = sell(driver, stock, soldQuantity, balance, user, pw)
            }
            // sell : +6% 이익
            else if (stock.quantity > 0 && (price - stock.averagePrice) / stock.averagePrice.toDouble() > 0.06 &&
                    stock.lastSoldPoint < 6.0) {
                val soldQuantity = (stock.quantity * 1 / 6).coerceAtLeast(1)
                stock.lastSoldPoint = 6.0
                balance = sell(driver, stock, soldQuantity, balance, user, pw)
            }
            // sell : +8% 이익
            else if (stock.quantity > 0 && (price - stock.averagePrice) / stock.averagePrice.toDouble() > 0.08 &&
                    stock.lastSoldPoint < 8.0) {
                val soldQuantity = (stock.quantity * 1 / 5).coerceAtLeast(1)
                stock.lastSoldPoint = 8.0
                balance = sell(driver, stock, soldQuantity, balance, user, pw)
            }
            // sell : +10% 이익
            else if (stock.quantity > 0 && (price - stock.averagePrice) / stock.averagePrice.toDouble() > 0.1 &&
                    stock.lastSoldPoint < 10.0) {
                val soldQuantity = (stock.quantity * 1 / 4).coerceAtLeast(1)
                stock.lastSoldPoint = 10.0
                balance = sell(driver, stock, soldQuantity, balance, user, pw)
            }
            // sell : +12% 이익
            else if (stock.quantity > 0 && (price - stock.averagePrice) / stock.averagePrice.toDouble() > 0.12 &&
                    stock.lastSoldPoint < 12.0) {
                val soldQuantity = (stock.quantity * 1 / 3).coerceAtLeast(1)
                stock.lastSoldPoint = 12.0
                balance = sell(driver, stock, soldQuantity, balance, user, pw)
            }
            // sell : +14% 이익
            else if (stock.quantity > 0 && (price - stock.averagePrice) / stock.averagePrice.toDouble() > 0.14 &&
                    stock.lastSoldPoint < 14.0) {
                val soldQuantity = (stock.quantity * 1 / 2).coerceAtLeast(1)
                stock.lastSoldPoint = 14.0
                balance = sell(driver, stock, soldQuantity, balance, user, pw)
            }
            // sell : +16% 이익
            else if (stock.quantity > 0 && (price - stock.averagePrice) / stock.averagePrice.toDouble() > 0.16 &&
                    stock.lastSoldPoint < 16.0) {
                val soldQuantity = (stock.quantity * 1 / 1).coerceAtLeast(1)
                stock.lastSoldPoint = 16.0
                balance = sell(driver, stock, soldQuantity, balance, user, pw)
            }
            // stop loss : -8% 하락
            else if (stock.quantity > 0 && (price - stock.averagePrice) / stock.averagePrice.toDouble() < -0.08) {
                val soldQuantity = stock.quantity
                stock.lastSoldPoint = -8.0
                balance = sell(driver, stock, soldQuantity, balance, user, pw)
            }
            // sell : 오래된 주식들 매도
            else if (stock.quantity > 0 && (System.currentTimeMillis() - stock.lastTradingDate) / dayInMillisecond.toDouble() > 14.0) {

                if (stock.lastSoldPoint == 0.0 && (price - stock.averagePrice) / stock.averagePrice.toDouble() > 0.0225) {
                    val soldQuantity = (stock.quantity * 1 / 7).coerceAtLeast(1)
                    stock.lastSoldPoint = 4.0
                    balance = sell(driver, stock, soldQuantity, balance, user, pw)
                } else if (stock.lastSoldPoint == 4.0 && (price - stock.averagePrice) / stock.averagePrice.toDouble() > 0.04) {
                    val soldQuantity = (stock.quantity * 1 / 6).coerceAtLeast(1)
                    stock.lastSoldPoint = 6.0
                    balance = sell(driver, stock, soldQuantity, balance, user, pw)
                } else if (stock.lastSoldPoint == 6.0 && (price - stock.averagePrice) / stock.averagePrice.toDouble() > 0.04) {
                    val soldQuantity = (stock.quantity * 1 / 5).coerceAtLeast(1)
                    stock.lastSoldPoint = 8.0
                    balance = sell(driver, stock, soldQuantity, balance, user, pw)
                } else if (stock.lastSoldPoint == 8.0 && (price - stock.averagePrice) / stock.averagePrice.toDouble() > 0.04) {
                    val soldQuantity = (stock.quantity * 1 / 4).coerceAtLeast(1)
                    stock.lastSoldPoint = 10.0
                    balance = sell(driver, stock, soldQuantity, balance, user, pw)
                } else if (stock.lastSoldPoint == 10.0 && (price - stock.averagePrice) / stock.averagePrice.toDouble() > 0.04) {
                    val soldQuantity = (stock.quantity * 1 / 3).coerceAtLeast(1)
                    stock.lastSoldPoint = 12.0
                    balance = sell(driver, stock, soldQuantity, balance, user, pw)
                } else if (stock.lastSoldPoint == 12.0 && (price - stock.averagePrice) / stock.averagePrice.toDouble() > 0.04) {
                    val soldQuantity = (stock.quantity * 1 / 2).coerceAtLeast(1)
                    stock.lastSoldPoint = 14.0
                    balance = sell(driver, stock, soldQuantity, balance, user, pw)
                } else if (stock.lastSoldPoint == 14.0 && (price - stock.averagePrice) / stock.averagePrice.toDouble() > 0.04) {
                    val soldQuantity = (stock.quantity * 1 / 1).coerceAtLeast(1)
                    stock.lastSoldPoint = 16.0
                    balance = sell(driver, stock, soldQuantity, balance, user, pw)
                }
            }
        }

        currentTime = format.format(System.currentTimeMillis())
    }

    println("장이 종료되었습니다.")
}

fun waitForDisplayingById(driver: ChromeDriver, id: String) {
    WebDriverWait(driver, 60).until(ExpectedConditions.presenceOfElementLocated(By.id(id)))
}

fun waitForClickableById(driver: ChromeDriver, id: String) {
    WebDriverWait(driver, 60).until(ExpectedConditions.elementToBeClickable(By.id(id)))
}

fun isNewStock(stockCode: String): Boolean {
    val url = "https://finance.naver.com/item/sise_day.nhn?code=$stockCode&page=1"
    var document = Jsoup.connect(url).get()
    var elems = document.getElementsByTag("table")
    val nav = elems[1]

    // 신규상장 종목 거름
    val pageNum = nav.getElementsByTag("tr")[0].getElementsByTag("td").size - 1
    if (pageNum <= 3) return true

    // 거래정지 종목 거름
    document = Jsoup.connect(url).get()
    elems = document.getElementsByClass("tah p11") // 숫자들을 가진 태그

    for (i in 0 until 60) {
        if (i % 6 == 1 && elems[i].text() != "0") {
            return false
        }
    }

    return true
}