package util

import org.jsoup.Jsoup
import org.openqa.selenium.By
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import java.io.FileWriter

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

fun buy(driver: ChromeDriver, stockCode: String, quantity: Int) {
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
    driver.findElementById("confirm").click()
}

fun sell(driver: ChromeDriver, stockCode: String, quantity: Int) {
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
    driver.findElementById("confirm").click()
}