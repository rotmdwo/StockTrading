package util

import org.jsoup.Jsoup
import org.openqa.selenium.By
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait
import stock.Stock
import java.io.FileWriter
import java.lang.Math.abs
import java.lang.Math.max
import java.sql.DriverManager
import java.sql.Timestamp
import java.text.SimpleDateFormat
import kotlin.collections.ArrayList

fun getPriceWithoutComma(price: String): Int { // 숫자 사이에 ',' 들어 있는 String을 숫자로 만들어 줌
    return price.replace(",","").toInt()
}

fun getCurrentPrice(stockCode: String): Int {
    val url = "https://finance.naver.com/item/main.nhn?code=${stockCode}"
    val document = Jsoup.connect(url).get()
    val dds = document.getElementsByTag("dd")

    for (i in 0 until dds.size) {
        val elem = dds[i]
        val text = elem.text()

        if (text.substring(0, 3) == "현재가") {
            return getPriceWithoutComma(text.substring(4, text.indexOf(' ', 4)))
        }
    }

    return -1
}

fun getCodesTop50(): ArrayList<String> { // 시가총액 상위 50개 종목의 코드 반환
    val url = "https://finance.naver.com/sise/sise_market_sum.nhn"
    val document = Jsoup.connect(url).get()
    val tltles = document.getElementsByClass("tltle") // 종목정보 가진 클래스

    val arrayList = ArrayList<String>(50)

    for (i in 0 until tltles.size) {
        val elem = tltles[i]
        val company = elem.text()
        val href = elem.attr("href")
        val code = href.substring(href.indexOf('=') + 1)

        arrayList.add(code)
    }

    return arrayList
}

fun getCodesTopKospiKosdaq200(): ArrayList<Stock> {
    val baseUrl = "https://finance.naver.com/sise/sise_market_sum.nhn?"
    val arrayList = ArrayList<Stock>(400)

    for (sosok in 0 .. 1) { // KOSPI: sosok 0, KOSDAQ: sosok 1
        for (page in 1 .. 4) {
            val url = baseUrl + "sosok=$sosok&page=$page"
            val document = Jsoup.connect(url).get()
            val tltles = document.getElementsByClass("tltle") // 종목정보 가진 클래스

            for (i in 0 until tltles.size) {
                val elem = tltles[i]
                val company = elem.text()
                val href = elem.attr("href")
                val code = href.substring(href.indexOf('=') + 1)

                val stock = Stock(code, company, 0, 0, 0.0, System.currentTimeMillis())
                arrayList.add(stock)
            }
        }
    }

    return arrayList
}

fun getMovingAverage20(stockCode: String): Int {
    return getMovingAverage(stockCode, 2)
}

fun getMovingAverage60(stockCode: String): Int {
    return getMovingAverage(stockCode, 6)
}

fun getMovingAverage120(stockCode: String): Int {
    return getMovingAverage(stockCode, 12)
}

fun getMovingAverageOfLastDay20(stockCode: String): Int {
    return getMovingAverageOfLastDay(stockCode, 2)
}

fun getMovingAverageOfLastDay60(stockCode: String): Int {
    return getMovingAverageOfLastDay(stockCode, 6)
}

fun getMovingAverageOfLastDay120(stockCode: String): Int {
    return getMovingAverageOfLastDay(stockCode, 12)
}

fun getMovingAverage(stockCode: String, days10: Int): Int {
    var url = "https://finance.naver.com/item/sise_day.nhn?code=$stockCode&page=1"
    var sum = 0
    var num = 0

    for (i in 1 .. days10) {
        url = url.dropLast(url.substring(url.lastIndexOf('=') + 1).length) + i // url 구성
        val document = Jsoup.connect(url).get()
        val elems = document.getElementsByClass("tah p11") // 숫자들을 가진 태그

        var numOf0_bias = 0 // 전일비 차가 0이면 <img> 태그가 아닌 <span> 태그로 나와 계산 잘못 되는 것 방지

        for (j in 0 until elems.size) {
            if (elems[j].text() == "0") { // 전일비 0인 것 거름
                numOf0_bias++
                continue
            }

            if ((j - numOf0_bias) % 5 == 0) { // 종가만 추출
                sum += getPriceWithoutComma(elems[j].text())
                num++
            }
        }
    }

    return sum / num
}

fun getMovingAverageOfLastDay(stockCode: String, days10: Int): Int {
    var url = "https://finance.naver.com/item/sise_day.nhn?code=$stockCode&page=1"
    var sum = 0
    var num = 0

    for (i in 1 .. days10) {
        url = url.dropLast(url.substring(url.lastIndexOf('=') + 1).length) + i // url 구성
        val document = Jsoup.connect(url).get()
        val elems = document.getElementsByClass("tah p11") // 숫자들을 가진 태그

        var numOf0_bias = 0 // 전일비 차가 0이면 <img> 태그가 아닌 <span> 태그로 나와 계산 잘못 되는 것 방지

        for (j in 0 until elems.size) {
            if (elems[j].text() == "0") { // 전일비 0인 것 거름
                numOf0_bias++
                continue
            }

            if ((j - numOf0_bias) % 5 == 0 && !(i == 1 && j == 0)) { // 종가만 추출, 오늘 가격 제외
                sum += getPriceWithoutComma(elems[j].text())
                num++
            }
        }
    }

    // 마지막 날 가격 추가
    url = url.dropLast(url.substring(url.lastIndexOf('=') + 1).length) + (days10 + 1)
    val document = Jsoup.connect(url).get()
    val elems = document.getElementsByClass("tah p11") // 숫자들을 가진 태그
    sum += getPriceWithoutComma(elems[0].text())
    num++

    return sum / num
}

fun accessToMiraeWTS(): ChromeDriver {
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

    // QR 로그인 선택
    driver.findElementByClassName("menu_02").click()

    // 자동 로그아웃 시간 7시간으로 설정
    Thread.sleep(4000L)
    driver.findElementByClassName("select_val").click()
    driver.findElementById("420").click()

    // QR 인증 11초 대기
    Thread.sleep(11000L)

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
    Thread.sleep(3000L)
    //waitForDisplayingById(driver, "mdiMenu_mdi0100")
    driver.findElementById("mdiMenu_mdi0100").click()

    // 로딩 대기 후 매수 탭 클릭
    //Thread.sleep(1000L)
    waitForDisplayingById(driver, "ui-id-21")
    driver.findElementById("ui-id-21").click()

    println("매수탭과 매도탭의 계좌를 선택하고 비밀번호를 입력해주세요.")

    return driver
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
    System.setProperty("webdriver.chrome.driver", "/Users/sungjaelee/Downloads/chromedriver")
    val options = ChromeOptions()
    options.setCapability("ignoreProtectedModeSettings", true)
    val driver = ChromeDriver(options)
    val url = "https://www.naver.com/"

    // 메인 페이지 접속
    driver.get(url)

    //Thread.sleep(3000L)
    //driver.findElementByXPath("//button[ends-with(@id, 'cn_btn')]").click()
    val buttons = driver.findElementsByTagName("button")
    for (button in buttons) {
        val id = button.getAttribute("id")

        if (id.contains("ch_btn")) {
            button.click()
            break
        }
    }
}

// 매수 가격 반환
fun buy(driver: ChromeDriver, stockCode: String, quantity: Int): Int {
    // 매수 탭 클릭
    waitForDisplayingById(driver, "ui-id-21")
    driver.findElementById("ui-id-21").click()

    // 종목코드 입력
    //Thread.sleep(2000L)
    waitForDisplayingById(driver, "search_num")
    driver.findElementById("search_num").sendKeys(stockCode)

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

    //waitForDisplayingById(driver, "messageBox_1002Ok")
    //driver.findElementById("messageBox_1002Ok").click()
    // 확인버튼의 랜덤 id 해결책
    val buttons = driver.findElementsByTagName("button")
    for (button in buttons) {
        val id = button.getAttribute("id")

        if (id.contains("Ok")) {
            button.click()
            break
        }
    }

    return price
}

// 매도 가격 반환
fun sell(driver: ChromeDriver, stockCode: String, quantity: Int): Int {
    // 매도 탭 클릭
    waitForDisplayingById(driver, "ui-id-22")
    driver.findElementById("ui-id-22").click()

    // 종목코드 입력
    //Thread.sleep(2000L)
    waitForDisplayingById(driver, "search_num")
    driver.findElementsById("search_num")[1].sendKeys(stockCode)

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

    //waitForDisplayingById(driver, "messageBox_1002Ok")
    //driver.findElementById("messageBox_1002Ok").click()
    val buttons = driver.findElementsByTagName("button")
    for (button in buttons) {
        val id = button.getAttribute("id")

        if (id.contains("Ok")) {
            button.click()
            break
        }
    }

    return price
}

fun getStockQuantityFromDB(stockCode: String, user: String, pw: String): Int {
    Class.forName("com.mysql.cj.jdbc.Driver")
    val conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/STOCK_TRADING", user, pw)

    val sql = "SELECT * FROM TB_STOCK WHERE CODE = ?"
    val stmt = conn.prepareStatement(sql)
    stmt.setString(1, stockCode)
    val rs = stmt.executeQuery()

    val quantity =  if (rs.next()) rs.getInt("QUANTITY") else -1

    conn.close()

    return quantity
}

fun updateStockQuantityAtDB(stock: Stock, user: String, pw: String): Int {
    Class.forName("com.mysql.cj.jdbc.Driver")
    val conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/STOCK_TRADING", user, pw)

    val sql = "INSERT INTO TB_STOCK VALUES(?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE " +
            "QUANTITY = ?, AVERAGE_PRICE = ?, LAST_SOLD_POINT = ?, LAST_TRADING_DATE = ?"
    val stmt = conn.prepareStatement(sql)
    stmt.setString(1, stock.code)
    stmt.setString(2, stock.name)
    stmt.setInt(3, stock.quantity)
    stmt.setInt(4, stock.averagePrice)
    stmt.setDouble(5, stock.lastSoldPoint)
    stmt.setTimestamp(6, Timestamp(stock.lastTradingDate))

    stmt.setInt(7, stock.quantity)
    stmt.setInt(8, stock.averagePrice)
    stmt.setDouble(9, stock.lastSoldPoint)
    stmt.setTimestamp(10, Timestamp(stock.lastTradingDate))
    val count = stmt.executeUpdate()

    //if (count == 1) conn.commit() // auto-commit이라 제거
    conn.close()

    return count
}

fun getAllStocksInfoFromDB(user: String, pw: String): ArrayList<Stock> {
    Class.forName("com.mysql.cj.jdbc.Driver")
    val conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/STOCK_TRADING", user, pw)

    val sql = "SELECT * FROM TB_STOCK"
    val stmt = conn.prepareStatement(sql)
    val rs = stmt.executeQuery()

    val arrayList = ArrayList<Stock>()

    while (rs.next()) {
        arrayList.add(Stock(rs.getString("CODE"), rs.getString("NAME"),
            rs.getInt("QUANTITY"), rs.getInt("AVERAGE_PRICE"),
                rs.getDouble("LAST_SOLD_POINT"), rs.getTimestamp("LAST_TRADING_DATE").time))
    }

    conn.close()

    return arrayList
}

fun addFirstStocksInfoToDB(user: String, pw: String) {
    val stocks = getCodesTopKospiKosdaq200()
    var index = 0

    while (index < stocks.size) {
        val count = updateStockQuantityAtDB(stocks[index], user, pw)

        if (count == 1) index++
    }
}

fun startAutoTrading(driver: ChromeDriver, stocks: ArrayList<Stock>, bal: Int, user: String, pw: String) {
    var balance = bal
    val dayInMillisecond = 1000 * 60 * 60 * 24L
    val format = SimpleDateFormat("HH:mm:ss")
    val openingTime = "09:00:00"
    val closingTime = "15:20:00"
    var currentTime = format.format(System.currentTimeMillis())

    val lastDayMA = Array(stocks.size, {IntArray(2)})

    for (i in 0 until stocks.size) {
        val lastDayMA20 = getMovingAverageOfLastDay20(stocks[i].code)
        val lastDayMA60 = getMovingAverageOfLastDay60(stocks[i].code)
        lastDayMA[i] = intArrayOf(lastDayMA20, lastDayMA60)
    }

    // 개장전 대기
    while (currentTime < openingTime) {
        Thread.sleep(500L)
        currentTime = format.format(System.currentTimeMillis())
    }

    // nested function
    fun updateDbAfterSell(stock: Stock, soldQuantity: Int, soldPrice: Int) {
        stock.quantity -= soldQuantity
        if (stock.quantity == 0) stock.averagePrice = 0
        stock.lastTradingDate = System.currentTimeMillis()

        updateStockQuantityAtDB(stock, user, pw)

        val profit = ((soldPrice - stock.averagePrice) * soldQuantity - soldPrice * soldQuantity * 0.0025).toInt()
        balance += profit
        addLogToDB(stock, "sell", soldPrice, soldQuantity, profit, user, pw)

        println("${stock.name}(${stock.code}) 개 당 ${soldPrice}원에 ${soldQuantity}주 매도 > 계좌 잔액 ${balance}원")
    }

    while (openingTime <= currentTime && currentTime < closingTime) {
        for (i in 0 until stocks.size) {
            val stock = stocks[i]
            val previous20 = lastDayMA[i][0]
            val previous60 = lastDayMA[i][1]
            val current20 = getMovingAverage20(stock.code)
            val current60 = getMovingAverage60(stock.code)
            val price = getCurrentPrice(stock.code)

            if (abs((current20 - current60)/current60.toDouble()) < 0.01) {
                println("${stock.name} 단기MA: ${current20} 중기MA: ${current60}")
            }

            // buy - 골든크로스
            if (stock.quantity == 0 && previous20 < previous60 && current60 < current20 &&
                (System.currentTimeMillis() - stock.lastTradingDate) / dayInMillisecond.toDouble() > 1.0
                    && price < balance && 500000 <= balance) {
                val boughtQuantity = if (price > 500000) 1 else 500000 / price
                val boughtPrice = buy(driver, stock.code, boughtQuantity)
                stock.averagePrice = (stock.averagePrice * stock.quantity + boughtPrice * boughtQuantity) / (stock.quantity + boughtQuantity)
                stock.quantity += boughtQuantity
                stock.lastSoldPoint = 0.0
                stock.lastTradingDate = System.currentTimeMillis()
                balance -= boughtQuantity * boughtPrice

                updateStockQuantityAtDB(stock, user, pw)
                addLogToDB(stock, "buy", boughtPrice, boughtQuantity, null, user, pw)

                println("${stock.name}(${stock.code}) 개 당 ${boughtPrice}원에 ${boughtQuantity}주 매수 > 계좌 잔액 ${balance}원")
            }
            // sell - 데드크로스
            else if (stock.quantity > 0 && previous60 < previous20 && current20 < current60 &&
                (System.currentTimeMillis() - stock.lastTradingDate) / dayInMillisecond.toDouble() > 1.0 &&
                (price - stock.averagePrice) / stock.averagePrice.toDouble() > 0.0125) {
                val soldQuantity = stock.quantity
                val soldPrice = sell(driver, stock.code, soldQuantity)
                stock.lastSoldPoint = (price - stock.averagePrice) / stock.averagePrice.toDouble() * 100
                updateDbAfterSell(stock, soldQuantity, soldPrice)
            }
            // sell : +4.5% 이익
            else if (stock.quantity > 0 && (price - stock.averagePrice) / stock.averagePrice.toDouble() > 0.045 &&
                    stock.lastSoldPoint < 4.5) {
                val soldQuantity = (stock.quantity * 1 / 3).coerceAtLeast(1)
                val soldPrice = sell(driver, stock.code, soldQuantity)
                stock.lastSoldPoint = 4.5
                updateDbAfterSell(stock, soldQuantity, soldPrice)
            }
            // sell : +9.0% 이익
            else if (stock.quantity > 0 && (price - stock.averagePrice) / stock.averagePrice.toDouble() > 0.09 &&
                    stock.lastSoldPoint < 9.0) {
                val soldQuantity = (stock.quantity * 1 / 2).coerceAtLeast(1)
                val soldPrice = sell(driver, stock.code, soldQuantity)
                stock.lastSoldPoint = 9.0
                updateDbAfterSell(stock, soldQuantity, soldPrice)
            }
            // sell : +13.5% 이익
            else if (stock.quantity > 0 && (price - stock.averagePrice) / stock.averagePrice.toDouble() > 0.135 &&
                    stock.lastSoldPoint < 13.5) {
                val soldQuantity = stock.quantity * 1
                val soldPrice = sell(driver, stock.code, soldQuantity)
                stock.lastSoldPoint = 13.5
                updateDbAfterSell(stock, soldQuantity, soldPrice)
            }
            // stop loss : -8% 하락
            else if (stock.quantity > 0 && (stock.averagePrice - price) / stock.averagePrice.toDouble() < -0.08) {
                val soldQuantity = stock.quantity
                val soldPrice = sell(driver, stock.code, soldQuantity)
                stock.lastSoldPoint = -8.0
                updateDbAfterSell(stock, soldQuantity, soldPrice)
            }
        }

        currentTime = format.format(System.currentTimeMillis())
    }

    println("장이 종료되었습니다.")
}

fun addLogToDB(stock: Stock, type: String, price: Int, quantity: Int, profit: Int?, user: String, pw: String): Int {
    Class.forName("com.mysql.cj.jdbc.Driver")
    val conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/STOCK_TRADING", user, pw)

    val sql = "INSERT INTO TB_LOG VALUES(?, ?, ?, ?, ?, ?, ?)"
    val stmt = conn.prepareStatement(sql)
    stmt.setString(1, stock.code)
    stmt.setString(2, stock.name)
    stmt.setString(3, type)
    stmt.setInt(4, price)
    stmt.setInt(5, quantity)
    if (profit != null) stmt.setInt(6, profit)
    else stmt.setNull(6, java.sql.Types.INTEGER)
    stmt.setTimestamp(7, Timestamp(stock.lastTradingDate))

    val count = stmt.executeUpdate()

    //if (count == 1) conn.commit() // auto-commit이라 제거
    conn.close()

    return count
}

fun waitForDisplayingById(driver: ChromeDriver, id: String) {
    WebDriverWait(driver, 60).until(ExpectedConditions.presenceOfElementLocated(By.id(id)))
}