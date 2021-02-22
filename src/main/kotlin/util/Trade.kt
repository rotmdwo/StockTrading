package util

import org.openqa.selenium.By
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait
import stock.Stock
import java.io.FileWriter
import java.lang.Math.abs
import java.text.SimpleDateFormat
import kotlin.collections.ArrayList

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

fun test(driver: ChromeDriver) {
    // 매수 탭 클릭
    waitForDisplayingById(driver, "ui-id-21")
    driver.findElementById("ui-id-21").click()

    // 종목코드 입력
    //Thread.sleep(2000L)
    waitForDisplayingById(driver, "search_num")
    driver.findElementById("search_num").sendKeys("027740")

    // 시장가 선택
    driver.findElementsByName("orderOp_0100")[0].click()
    Thread.sleep(250L)
    //driver.findElementById("unitPrice_0100").sendKeys("0")
    driver.findElementsByName("orderOp_0100")[1].click()

    // 매수량 설정
    driver.findElementById("mesuQty_0100").sendKeys("1")

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
    //Thread.sleep(3000L)
    val buttons = driver.findElementsByTagName("button")
    for (button in buttons) {
        val id = button.getAttribute("id")

        if (id.contains("Ok")) {
            WebDriverWait(driver, 60).until(ExpectedConditions.elementToBeClickable(By.id(id)))
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
            waitForClickableById(driver, id)
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
            waitForClickableById(driver, id)
            button.click()
            break
        }
    }

    return price
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
        val lastDayMA20 = getMovingAverageOfLastDay10(stocks[i].code)
        val lastDayMA60 = getMovingAverageOfLastDay30(stocks[i].code)
        lastDayMA[i] = intArrayOf(lastDayMA20, lastDayMA60)
    }

    // 개장전 대기
    while (currentTime < openingTime) {
        Thread.sleep(500L)
        currentTime = format.format(System.currentTimeMillis())
    }

    // nested function
    fun updateDbAfterSell(stock: Stock, soldQuantity: Int, soldPrice: Int) {
        val profit = ((soldPrice - stock.averagePrice) * soldQuantity - soldPrice * soldQuantity * 0.0025).toInt()
        balance += profit

        stock.quantity -= soldQuantity
        if (stock.quantity == 0) stock.averagePrice = 0
        stock.lastTradingDate = System.currentTimeMillis()

        updateStockQuantityAtDB(stock, user, pw)
        addLogToDB(stock, "sell", soldPrice, soldQuantity, profit, user, pw)

        println("==============================")
        println("${stock.name}(${stock.code}) 개 당 ${soldPrice}원에 ${soldQuantity}주 매도 > 계좌 잔액 ${balance}원")
        println("==============================")
    }

    while (openingTime <= currentTime && currentTime < closingTime) {
        for (i in 0 until stocks.size) {
            val stock = stocks[i]
            val previous20 = lastDayMA[i][0]
            val previous60 = lastDayMA[i][1]
            val current20 = getMovingAverage10(stock.code)
            val current60 = getMovingAverage30(stock.code)
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

                println("==============================")
                println("${stock.name}(${stock.code}) 개 당 ${boughtPrice}원에 ${boughtQuantity}주 매수 > 계좌 잔액 ${balance}원")
                println("==============================")
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
            // sell : +4% 이익
            else if (stock.quantity > 0 && (price - stock.averagePrice) / stock.averagePrice.toDouble() > 0.04 &&
                    stock.lastSoldPoint < 4.0) {
                val soldQuantity = (stock.quantity * 1 / 7).coerceAtLeast(1)
                val soldPrice = sell(driver, stock.code, soldQuantity)
                stock.lastSoldPoint = 4.0
                updateDbAfterSell(stock, soldQuantity, soldPrice)
            }
            // sell : +6% 이익
            else if (stock.quantity > 0 && (price - stock.averagePrice) / stock.averagePrice.toDouble() > 0.06 &&
                    stock.lastSoldPoint < 6.0) {
                val soldQuantity = (stock.quantity * 1 / 6).coerceAtLeast(1)
                val soldPrice = sell(driver, stock.code, soldQuantity)
                stock.lastSoldPoint = 6.0
                updateDbAfterSell(stock, soldQuantity, soldPrice)
            }
            // sell : +8% 이익
            else if (stock.quantity > 0 && (price - stock.averagePrice) / stock.averagePrice.toDouble() > 0.08 &&
                    stock.lastSoldPoint < 8.0) {
                val soldQuantity = (stock.quantity * 1 / 5).coerceAtLeast(1)
                val soldPrice = sell(driver, stock.code, soldQuantity)
                stock.lastSoldPoint = 8.0
                updateDbAfterSell(stock, soldQuantity, soldPrice)
            }
            // sell : +10% 이익
            else if (stock.quantity > 0 && (price - stock.averagePrice) / stock.averagePrice.toDouble() > 0.1 &&
                    stock.lastSoldPoint < 10.0) {
                val soldQuantity = (stock.quantity * 1 / 4).coerceAtLeast(1)
                val soldPrice = sell(driver, stock.code, soldQuantity)
                stock.lastSoldPoint = 10.0
                updateDbAfterSell(stock, soldQuantity, soldPrice)
            }
            // sell : +12% 이익
            else if (stock.quantity > 0 && (price - stock.averagePrice) / stock.averagePrice.toDouble() > 0.12 &&
                    stock.lastSoldPoint < 12.0) {
                val soldQuantity = (stock.quantity * 1 / 3).coerceAtLeast(1)
                val soldPrice = sell(driver, stock.code, soldQuantity)
                stock.lastSoldPoint = 12.0
                updateDbAfterSell(stock, soldQuantity, soldPrice)
            }
            // sell : +14% 이익
            else if (stock.quantity > 0 && (price - stock.averagePrice) / stock.averagePrice.toDouble() > 0.14 &&
                    stock.lastSoldPoint < 14.0) {
                val soldQuantity = (stock.quantity * 1 / 2).coerceAtLeast(1)
                val soldPrice = sell(driver, stock.code, soldQuantity)
                stock.lastSoldPoint = 14.0
                updateDbAfterSell(stock, soldQuantity, soldPrice)
            }
            // sell : +16% 이익
            else if (stock.quantity > 0 && (price - stock.averagePrice) / stock.averagePrice.toDouble() > 0.16 &&
                    stock.lastSoldPoint < 16.0) {
                val soldQuantity = (stock.quantity * 1 / 1).coerceAtLeast(1)
                val soldPrice = sell(driver, stock.code, soldQuantity)
                stock.lastSoldPoint = 16.0
                updateDbAfterSell(stock, soldQuantity, soldPrice)
            }
            // stop loss : -8% 하락
            else if (stock.quantity > 0 && (price - stock.averagePrice) / stock.averagePrice.toDouble() < -0.08) {
                val soldQuantity = stock.quantity
                val soldPrice = sell(driver, stock.code, soldQuantity)
                stock.lastSoldPoint = -8.0
                updateDbAfterSell(stock, soldQuantity, soldPrice)
            }
            // sell : 오래된 주식들 매도
            else if (stock.quantity > 0 && (System.currentTimeMillis() - stock.lastTradingDate) / dayInMillisecond.toDouble() > 14.0) {

                if (stock.lastSoldPoint == 0.0 && (price - stock.averagePrice) / stock.averagePrice.toDouble() > 0.0225) {
                    val soldQuantity = (stock.quantity * 1 / 7).coerceAtLeast(1)
                    val soldPrice = sell(driver, stock.code, soldQuantity)
                    stock.lastSoldPoint = 4.0
                    updateDbAfterSell(stock, soldQuantity, soldPrice)
                } else if (stock.lastSoldPoint == 4.0 && (price - stock.averagePrice) / stock.averagePrice.toDouble() > 0.04) {
                    val soldQuantity = (stock.quantity * 1 / 6).coerceAtLeast(1)
                    val soldPrice = sell(driver, stock.code, soldQuantity)
                    stock.lastSoldPoint = 6.0
                    updateDbAfterSell(stock, soldQuantity, soldPrice)
                } else if (stock.lastSoldPoint == 6.0 && (price - stock.averagePrice) / stock.averagePrice.toDouble() > 0.04) {
                    val soldQuantity = (stock.quantity * 1 / 5).coerceAtLeast(1)
                    val soldPrice = sell(driver, stock.code, soldQuantity)
                    stock.lastSoldPoint = 8.0
                    updateDbAfterSell(stock, soldQuantity, soldPrice)
                } else if (stock.lastSoldPoint == 8.0 && (price - stock.averagePrice) / stock.averagePrice.toDouble() > 0.04) {
                    val soldQuantity = (stock.quantity * 1 / 4).coerceAtLeast(1)
                    val soldPrice = sell(driver, stock.code, soldQuantity)
                    stock.lastSoldPoint = 10.0
                    updateDbAfterSell(stock, soldQuantity, soldPrice)
                } else if (stock.lastSoldPoint == 10.0 && (price - stock.averagePrice) / stock.averagePrice.toDouble() > 0.04) {
                    val soldQuantity = (stock.quantity * 1 / 3).coerceAtLeast(1)
                    val soldPrice = sell(driver, stock.code, soldQuantity)
                    stock.lastSoldPoint = 12.0
                    updateDbAfterSell(stock, soldQuantity, soldPrice)
                } else if (stock.lastSoldPoint == 12.0 && (price - stock.averagePrice) / stock.averagePrice.toDouble() > 0.04) {
                    val soldQuantity = (stock.quantity * 1 / 2).coerceAtLeast(1)
                    val soldPrice = sell(driver, stock.code, soldQuantity)
                    stock.lastSoldPoint = 14.0
                    updateDbAfterSell(stock, soldQuantity, soldPrice)
                } else if (stock.lastSoldPoint == 14.0 && (price - stock.averagePrice) / stock.averagePrice.toDouble() > 0.04) {
                    val soldQuantity = (stock.quantity * 1 / 1).coerceAtLeast(1)
                    val soldPrice = sell(driver, stock.code, soldQuantity)
                    stock.lastSoldPoint = 16.0
                    updateDbAfterSell(stock, soldQuantity, soldPrice)
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