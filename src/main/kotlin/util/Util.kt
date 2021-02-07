package util

import org.jsoup.Jsoup
import org.openqa.selenium.By
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import stock.Stock
import java.io.FileWriter
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

                val stock = Stock(code, company, 0, 0, System.currentTimeMillis())
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

            if ((j - numOf0_bias) % 5 == 0) { // 종가만 거르기
                sum += getPriceWithoutComma(elems[j].text())
                num++
            }
        }
    }

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
    driver.findElementById("mdiMenu_mdi0100").click()

    // 로딩 대기 후 매수 탭 클릭
    Thread.sleep(1000L)
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

// 매수 가격 반환
fun buy(driver: ChromeDriver, stockCode: String, quantity: Int): Int {
    // 매수 탭 클릭
    driver.findElementById("ui-id-21").click()

    // 종목코드 입력
    Thread.sleep(2000L)
    driver.findElementById("search_num").sendKeys(stockCode)

    // 시장가 선택
    driver.findElementsByName("orderOp_0100")[1].click()

    // 매수량 설정
    driver.findElementById("mesuQty_0100").sendKeys("$quantity")

    // 매수 버튼 클릭
    driver.findElementById("mesu_0100").click()

    // 매수 확인
    Thread.sleep(1000L)
    val price = getPriceWithoutComma(driver.findElementById("price").text)
    driver.findElementById("confirm").click()

    return price
}

// 매도 가격 반환
fun sell(driver: ChromeDriver, stockCode: String, quantity: Int): Int {
    // 매도 탭 클릭
    driver.findElementById("ui-id-22").click()

    // 종목코드 입력
    Thread.sleep(2000L)
    driver.findElementsById("search_num")[1].sendKeys(stockCode)

    // 시장가 선택
    driver.findElementsByName("orderOp1_0100")[1].click()

    // 매도량 설정
    driver.findElementById("qtyMedo_0100").sendKeys("$quantity")

    // 매도 버튼 클릭
    driver.findElementById("medo_0100").click()

    Thread.sleep(1000L)
    val price = getPriceWithoutComma(driver.findElementById("price").text)
    driver.findElementById("confirm").click()

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

    val sql = "INSERT INTO TB_STOCK VALUES(?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE " +
            "QUANTITY = ?, AVERAGE_PRICE = ?, LAST_TRADING_DATE = ?"
    val stmt = conn.prepareStatement(sql)
    stmt.setString(1, stock.code)
    stmt.setString(2, stock.name)
    stmt.setInt(3, stock.quantity)
    stmt.setInt(4, stock.averagePrice)
    stmt.setTimestamp(5, Timestamp(stock.lastTradingDate))

    stmt.setInt(6, stock.quantity)
    stmt.setInt(7, stock.averagePrice)
    stmt.setTimestamp(8, Timestamp(stock.lastTradingDate))
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
            rs.getInt("QUANTITY"), rs.getInt("AVERAGE_PRICE"), rs.getTimestamp("LAST_TRADING_DATE").time))
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

fun startAutoTrading(driver: ChromeDriver, stocks: ArrayList<Stock>, user: String, pw: String) {
    val dayInMillisecond = 1000 * 60 * 60 * 24L
    val format = SimpleDateFormat("HH:mm:ss")
    val openingTime = "09:00:00"
    val closingTime = "15:20:00"
    var currentTIme = format.format(System.currentTimeMillis())

    val previousMA = Array(stocks.size, {IntArray(2)})

    for (i in 0 until stocks.size) {
        val ma20 = getMovingAverage20(stocks[i].code)
        val ma60 = getMovingAverage60(stocks[i].code)
        previousMA[i] = intArrayOf(ma20, ma60)
    }

    while (openingTime <= currentTIme && currentTIme < closingTime) {
        for (i in 0 until stocks.size) {
            val stock = stocks[i]
            val previous20 = previousMA[i][0]
            val previous60 = previousMA[i][1]
            val current20 = getMovingAverage20(stock.code)
            val current60 = getMovingAverage60(stock.code)
            val price = getCurrentPrice(stock.code)

            // buy
            if (stock.quantity == 0 && previous20 < previous60 && current60 < current20 &&
                (System.currentTimeMillis() - stock.lastTradingDate) / dayInMillisecond.toDouble() > 1.0 && price < 100000) {
                val boughtQuantity = 1
                val boughtPrice = buy(driver, stock.code, boughtQuantity)
                stock.averagePrice = (stock.averagePrice * stock.quantity + boughtPrice * boughtQuantity) / (stock.quantity + boughtQuantity)
                stock.quantity += boughtQuantity
                stock.lastTradingDate = System.currentTimeMillis()

                updateStockQuantityAtDB(stock, user, pw)
                addLogToDB(stock, "buy", boughtPrice, boughtQuantity, null, user, pw)
            }

            // sell
            if (stock.quantity == 1 && previous60 < previous20 && current20 < current60 &&
                (System.currentTimeMillis() - stock.lastTradingDate) / dayInMillisecond.toDouble() > 1.0 &&
                (price - stock.averagePrice) / stock.averagePrice.toDouble() > 0.02) {
                val soldQuantity = 1
                val soldPrice = sell(driver, stock.code, soldQuantity)
                stock.quantity -= soldQuantity
                if (stock.quantity == 0) stock.averagePrice = 0
                stock.lastTradingDate = System.currentTimeMillis()

                updateStockQuantityAtDB(stock, user, pw)

                val profit = ((soldPrice - stock.averagePrice) * soldQuantity - soldPrice * soldQuantity * 0.0025).toInt()
                addLogToDB(stock, "sell", soldPrice, soldQuantity, profit, user, pw)
            }

            // stop loss
            if (stock.quantity == 1 && (stock.averagePrice - price) / stock.averagePrice.toDouble() < -0.08) {
                val soldQuantity = 1
                val soldPrice = sell(driver, stock.code, soldQuantity)
                stock.quantity -= soldQuantity
                if (stock.quantity == 0) stock.averagePrice = 0
                stock.lastTradingDate = System.currentTimeMillis()

                updateStockQuantityAtDB(stock, user, pw)

                val profit = ((soldPrice - stock.averagePrice) * soldQuantity - soldPrice * soldQuantity * 0.0025).toInt()
                addLogToDB(stock, "sell", soldPrice, soldQuantity, profit, user, pw)
            }

            previousMA[i] = intArrayOf(current20, current60)
        }

        currentTIme = format.format(System.currentTimeMillis())
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
