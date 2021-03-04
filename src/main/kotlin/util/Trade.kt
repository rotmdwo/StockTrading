package util

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
import java.text.SimpleDateFormat
import kotlin.collections.ArrayList

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
    val asd = try {
        getPriceWithoutComma("")
    } catch (e: NumberFormatException) {
        2000
    }
    println(asd)
}

// 매수 가격 반환
fun buy(driver: ChromeDriver, stock: Stock, expectedPrice: Int, quantity: Int, balance: Int, user: String, pw: String, email: Email): Int {
    try {
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
        waitForDisplayingById(driver, "confirm")
        driver.findElementById("confirm").click()

        waitForDisplayingById(driver, "grid_diax0002")
        saveHtmlAsTxt(driver, "html_bought.txt")

    } catch (e: Exception) {
        email.notifyError("프로그램 중단됨.<br>확인 또는 작업 취소 후 재가동 해주세요.")
        //closeWindowThenReenterWTS(driver)
        do {
            println("프로그램 재개가 준비되었습니까? (y/n) : ")
        } while (BufferedReader(InputStreamReader(System.`in`)).readLine() != "y")

        return balance
    }

    val price = try {
        getPriceWithoutComma(driver.findElementById("grid_diax0002")
            .findElements(By.className("pq-grid-cell"))[5].text)

    } catch (e: NumberFormatException) {
        expectedPrice
    }

    val newBalance = updateDbAfterBuy(stock, quantity, price, balance, user, pw, email)

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
        email.notifyError("프로그램 중단됨.<br>확인 또는 작업 취소 후 재가동 해주세요.")
        //closeWindowThenReenterWTS(driver)
        do {
            println("프로그램 재개가 준비되었습니까? (y/n) : ")
        } while (BufferedReader(InputStreamReader(System.`in`)).readLine() != "y")
    }

    return newBalance
}

// 매도 가격 반환
fun sell(driver: ChromeDriver, stock: Stock, expectedPrice: Int, quantity: Int, balance: Int, user: String, pw: String, email: Email): Int {
    try {
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

        // 매도 확인
        waitForDisplayingById(driver, "confirm")
        driver.findElementById("confirm").click()

        waitForDisplayingById(driver, "grid_diax0002")
        saveHtmlAsTxt(driver, "html_sold.txt")

    } catch (e: Exception) {
        email.notifyError("프로그램 중단됨.<br>확인 또는 작업 취소 후 재가동 해주세요.")
        //closeWindowThenReenterWTS(driver)
        do {
            println("프로그램 재개가 준비되었습니까? (y/n) : ")
        } while (BufferedReader(InputStreamReader(System.`in`)).readLine() != "y")

        return balance
    }

    val price = try {
        getPriceWithoutComma(driver.findElementById("grid_diax0002")
            .findElements(By.className("pq-grid-cell"))[5].text)

    } catch (e: NumberFormatException) {
        expectedPrice
    }

    val newBalance = updateDbAfterSell(stock, quantity, price, balance, user, pw, email)

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
        email.notifyError("프로그램 중단됨.<br>확인 또는 작업 취소 후 재가동 해주세요.")
        //closeWindowThenReenterWTS(driver)
        do {
            println("프로그램 재개가 준비되었습니까? (y/n) : ")
        } while (BufferedReader(InputStreamReader(System.`in`)).readLine() != "y")
    }

    return newBalance
}

fun updateDbAfterBuy(stock: Stock, boughtQuantity: Int, boughtPrice: Int, balance: Int, user: String, pw: String, email: Email): Int {
    stock.averagePrice = (stock.averagePrice * stock.quantity + boughtPrice * boughtQuantity) / (stock.quantity + boughtQuantity)
    stock.quantity += boughtQuantity
    stock.lastSoldPoint = 0.0
    stock.lastTradingDate = System.currentTimeMillis()
    val newBalance = balance - boughtQuantity * boughtPrice

    updateStockQuantityAtDB(stock, user, pw)
    addLogToDB(stock, "buy", boughtPrice, boughtQuantity, null, user, pw)

    println("==============================")
    println("${stock.name}(${stock.code}) 개 당 ${boughtPrice}원에 ${boughtQuantity}주 매수 > 계좌 잔액 ${newBalance}원")
    email.notifyTrading("${stock.name}(${stock.code}) 개 당 ${boughtPrice}원에 ${boughtQuantity}주 매수 > 계좌 잔액 ${newBalance}원")
    println("==============================")

    return newBalance
}

fun updateDbAfterSell(stock: Stock, soldQuantity: Int, soldPrice: Int, balance: Int, user: String, pw: String, email: Email): Int {
    val profit = ((soldPrice - stock.averagePrice) * soldQuantity - soldPrice * soldQuantity * 0.0025).toInt()
    val newBalance = balance + (soldPrice * soldQuantity * 0.9975).toInt()

    stock.quantity -= soldQuantity
    if (stock.quantity == 0) stock.averagePrice = 0
    stock.lastTradingDate = System.currentTimeMillis()

    updateStockQuantityAtDB(stock, user, pw)
    addLogToDB(stock, "sell", soldPrice, soldQuantity, profit, user, pw)

    println("==============================")
    println("${stock.name}(${stock.code}) 개 당 ${soldPrice}원에 ${soldQuantity}주 매도 > 계좌 잔액 ${newBalance}원")
    email.notifyTrading("${stock.name}(${stock.code}) 개 당 ${soldPrice}원에 ${soldQuantity}주 매도 > 계좌 잔액 ${newBalance}원")
    println("==============================")

    return newBalance
}

fun startAutoTrading(driver: ChromeDriver, stocks: ArrayList<Stock>, bal: Int, user: String, pw: String, email: Email) {
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

            val diff = (current5 - current20)/current20.toDouble()
            if (-0.005 < diff && diff < 0.0) {
                println("${stock.name} 단기MA: ${current5} 중기MA: ${current20}")
            }

            val currentProfit: Double = (price - stock.averagePrice) / stock.averagePrice.toDouble()

            // buy - 골든크로스
            if (stock.quantity == 0 && dayBeforeLastDay5 < dayBeforeLastDay20 && lastDay5 < lastDay20 &&
                    dayBeforeLastDay5 < lastDay5 && lastDay5 < current5 && current20 < current5 &&
                    (System.currentTimeMillis() - stock.lastTradingDate) / dayInMillisecond.toDouble() > 1.0 &&
                    price < balance && 500000 <= balance) {
                val boughtQuantity = if (price > 500000) 1 else 500000 / price
                balance = buy(driver, stock, price, boughtQuantity, balance, user, pw, email)
            }
            // sell - 데드크로스
            else if (stock.quantity > 0 && lastDay20 < lastDay5 && current5 < current20 &&
                (System.currentTimeMillis() - stock.lastTradingDate) / dayInMillisecond.toDouble() > 1.0 &&
                currentProfit > 0.005) {
                val soldQuantity = stock.quantity
                stock.lastSoldPoint = currentProfit * 100
                balance = sell(driver, stock, price, soldQuantity, balance, user, pw, email)
            }
            // sell : +16% 이익
            else if (stock.quantity > 0 && currentProfit > 0.16 && stock.lastSoldPoint < 16.0) {
                // lastSoldPoint와 현재수익률 간의 갭이 있을 때 한 번에 팔기 위한 방법
                val bias = when (stock.lastSoldPoint) {
                    0.0 -> 6
                    4.0 -> 5
                    6.0 -> 4
                    8.0 -> 3
                    10.0 -> 2
                    12.0 -> 1
                    else -> 0
                }
                val soldQuantity = (stock.quantity * (1 + bias)/ (1 + bias)).coerceAtLeast(1)
                stock.lastSoldPoint = 16.0
                balance = sell(driver, stock, price, soldQuantity, balance, user, pw, email)
            }
            // sell : +14% 이익
            else if (stock.quantity > 0 && currentProfit > 0.14 && stock.lastSoldPoint < 14.0) {
                val bias = when (stock.lastSoldPoint) {
                    0.0 -> 5
                    4.0 -> 4
                    6.0 -> 3
                    8.0 -> 2
                    10.0 -> 1
                    else -> 0
                }
                val soldQuantity = (stock.quantity * (1 + bias) / (2 + bias)).coerceAtLeast(1)
                stock.lastSoldPoint = 14.0
                balance = sell(driver, stock, price, soldQuantity, balance, user, pw, email)
            }
            // sell : +12% 이익
            else if (stock.quantity > 0 && currentProfit > 0.12 && stock.lastSoldPoint < 12.0) {
                val bias = when (stock.lastSoldPoint) {
                    0.0 -> 4
                    4.0 -> 3
                    6.0 -> 2
                    8.0 -> 1
                    else -> 0
                }
                val soldQuantity = (stock.quantity * (1 + bias) / (3 + bias)).coerceAtLeast(1)
                stock.lastSoldPoint = 12.0
                balance = sell(driver, stock, price, soldQuantity, balance, user, pw, email)
            }
            // sell : +10% 이익
            else if (stock.quantity > 0 && currentProfit > 0.10 && stock.lastSoldPoint < 10.0) {
                val bias = when (stock.lastSoldPoint) {
                    0.0 -> 3
                    4.0 -> 2
                    6.0 -> 1
                    else -> 0
                }
                val soldQuantity = (stock.quantity * (1 + bias) / (4 + bias)).coerceAtLeast(1)
                stock.lastSoldPoint = 10.0
                balance = sell(driver, stock, price, soldQuantity, balance, user, pw, email)
            }
            // sell : +8% 이익
            else if (stock.quantity > 0 && currentProfit > 0.08 && stock.lastSoldPoint < 8.0) {
                val bias = when (stock.lastSoldPoint) {
                    0.0 -> 2
                    4.0 -> 1
                    else -> 0
                }
                val soldQuantity = (stock.quantity * (1 + bias) / (5 + bias)).coerceAtLeast(1)
                stock.lastSoldPoint = 8.0
                balance = sell(driver, stock, price, soldQuantity, balance, user, pw, email)
            }
            // sell : +6% 이익
            else if (stock.quantity > 0 && currentProfit > 0.06 && stock.lastSoldPoint < 6.0) {
                val bias = when (stock.lastSoldPoint) {
                    0.0 -> 1
                    else -> 0
                }
                val soldQuantity = (stock.quantity * (1 + bias) / (6 + bias)).coerceAtLeast(1)
                stock.lastSoldPoint = 6.0
                balance = sell(driver, stock, price, soldQuantity, balance, user, pw, email)
            }
            // sell : +4% 이익
            else if (stock.quantity > 0 && currentProfit > 0.04 && stock.lastSoldPoint < 4.0) {
                val soldQuantity = (stock.quantity * 1 / 7).coerceAtLeast(1)
                stock.lastSoldPoint = 4.0
                balance = sell(driver, stock, price, soldQuantity, balance, user, pw, email)
            }
                /*
            // stop loss : -8% 하락
            else if (stock.quantity > 0 && (price - stock.averagePrice) / stock.averagePrice.toDouble() < -0.08) {
                val soldQuantity = stock.quantity
                stock.lastSoldPoint = -8.0
                balance = sell(driver, stock, soldQuantity, balance, user, pw, email)
            }
                 */
                // TODO: 물타기 기능 추가 -> 매수평균가보다 현재가가 10% 이상 낮으면서 이평선 상향돌파 할 때 추가매수.
            // sell : 오래된 주식들 매도
            else if (stock.quantity > 0 && (System.currentTimeMillis() - stock.lastTradingDate) / dayInMillisecond.toDouble() > 7.0) {

                if (stock.lastSoldPoint == 0.0 && currentProfit > 0.0225) {
                    val soldQuantity = (stock.quantity * 1 / 7).coerceAtLeast(1)
                    stock.lastSoldPoint = 4.0
                    balance = sell(driver, stock, price, soldQuantity, balance, user, pw, email)
                } else if (stock.lastSoldPoint == 4.0 && currentProfit > 0.04) {
                    val soldQuantity = (stock.quantity * 1 / 6).coerceAtLeast(1)
                    stock.lastSoldPoint = 6.0
                    balance = sell(driver, stock, price, soldQuantity, balance, user, pw, email)
                } else if (stock.lastSoldPoint == 6.0 && currentProfit > 0.04) {
                    val soldQuantity = (stock.quantity * 1 / 5).coerceAtLeast(1)
                    stock.lastSoldPoint = 8.0
                    balance = sell(driver, stock, price, soldQuantity, balance, user, pw, email)
                } else if (stock.lastSoldPoint == 8.0 && currentProfit > 0.04) {
                    val soldQuantity = (stock.quantity * 1 / 4).coerceAtLeast(1)
                    stock.lastSoldPoint = 10.0
                    balance = sell(driver, stock, price, soldQuantity, balance, user, pw, email)
                } else if (stock.lastSoldPoint == 10.0 && currentProfit > 0.04) {
                    val soldQuantity = (stock.quantity * 1 / 3).coerceAtLeast(1)
                    stock.lastSoldPoint = 12.0
                    balance = sell(driver, stock, price, soldQuantity, balance, user, pw, email)
                } else if (stock.lastSoldPoint == 12.0 && currentProfit > 0.04) {
                    val soldQuantity = (stock.quantity * 1 / 2).coerceAtLeast(1)
                    stock.lastSoldPoint = 14.0
                    balance = sell(driver, stock, price, soldQuantity, balance, user, pw, email)
                } else if (stock.lastSoldPoint == 14.0 && currentProfit > 0.04) {
                    val soldQuantity = (stock.quantity * 1 / 1).coerceAtLeast(1)
                    stock.lastSoldPoint = 16.0
                    balance = sell(driver, stock, price, soldQuantity, balance, user, pw, email)
                }
            }
        }

        currentTime = format.format(System.currentTimeMillis())
        println()
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