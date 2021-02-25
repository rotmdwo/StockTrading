package util

import org.jsoup.Jsoup
import stock.Stock

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

fun getMovingAverage10(stockCode: String): Int {
    return getMovingAverage(stockCode, 1)
}

fun getMovingAverage20(stockCode: String): Int {
    return getMovingAverage(stockCode, 2)
}

fun getMovingAverage30(stockCode: String): Int {
    return getMovingAverage(stockCode, 3)
}

fun getMovingAverage60(stockCode: String): Int {
    return getMovingAverage(stockCode, 6)
}

fun getMovingAverage120(stockCode: String): Int {
    return getMovingAverage(stockCode, 12)
}

fun getMovingAverageOfLastDay10(stockCode: String): Int {
    return getMovingAverageOfLastDay(stockCode, 1)
}

fun getMovingAverageOfLastDay20(stockCode: String): Int {
    return getMovingAverageOfLastDay(stockCode, 2)
}

fun getMovingAverageOfLastDay30(stockCode: String): Int {
    return getMovingAverageOfLastDay(stockCode, 3)
}

fun getMovingAverageOfLastDay60(stockCode: String): Int {
    return getMovingAverageOfLastDay(stockCode, 6)
}

fun getMovingAverageOfLastDay120(stockCode: String): Int {
    return getMovingAverageOfLastDay(stockCode, 12)
}

fun getMovingAverageOfDayBeforeLastDay10(stockCode: String): Int {
    return getMovingAverageOfDayBeforeLastDay(stockCode, 1)
}

fun getMovingAverageOfDayBeforeLastDay20(stockCode: String): Int {
    return getMovingAverageOfDayBeforeLastDay(stockCode, 2)
}

fun getMovingAverageOfDayBeforeLastDay30(stockCode: String): Int {
    return getMovingAverageOfDayBeforeLastDay(stockCode, 3)
}

fun getMovingAverage5(stockCode: String): Int {
    val url = "https://finance.naver.com/item/sise_day.nhn?code=$stockCode&page=1"
    var sum = 0
    var num = 0
    val document = Jsoup.connect(url).get()
    val elems = document.getElementsByClass("tah p11") // 숫자들을 가진 태그

    var numOf0_bias = 0 // 전일비 차가 0이면 <img> 태그가 아닌 <span> 태그로 나와 계산 잘못 되는 것 방지

    var index = 0
    while (num < 5) {
        if (elems[index].text() == "0") { // 전일비 0인 것 거름
            numOf0_bias++
            index++
            continue
        }

        if ((index - numOf0_bias) % 5 == 0) { // 종가만 추출
            sum += getPriceWithoutComma(elems[index].text())
            num++
        }

        index++
    }

    return sum / num
}

fun getMovingAverageOfLastDay5(stockCode: String): Int {
    val url = "https://finance.naver.com/item/sise_day.nhn?code=$stockCode&page=1"
    var sum = 0
    var num = 0
    val document = Jsoup.connect(url).get()
    val elems = document.getElementsByClass("tah p11") // 숫자들을 가진 태그

    var numOf0_bias = 0 // 전일비 차가 0이면 <img> 태그가 아닌 <span> 태그로 나와 계산 잘못 되는 것 방지

    var index = 0
    while (num < 5) {
        if (elems[index].text() == "0") { // 전일비 0인 것 거름
            numOf0_bias++
            index++
            continue
        }

        if ((index - numOf0_bias) % 5 == 0 && index != 0) { // 종가만 추출
            sum += getPriceWithoutComma(elems[index].text())
            num++
        }

        index++
    }

    return sum / num
}

fun getMovingAverageOfDayBeforeLastDay5(stockCode: String): Int {
    val url = "https://finance.naver.com/item/sise_day.nhn?code=$stockCode&page=1"
    var sum = 0
    var num = 0
    val document = Jsoup.connect(url).get()
    val elems = document.getElementsByClass("tah p11") // 숫자들을 가진 태그

    var numOf0_bias = 0 // 전일비 차가 0이면 <img> 태그가 아닌 <span> 태그로 나와 계산 잘못 되는 것 방지

    var index = 0
    while (num < 5) {
        if (elems[index].text() == "0") { // 전일비 0인 것 거름
            numOf0_bias++
            index++
            continue
        }

        if ((index - numOf0_bias) % 5 == 0 && index > 6) { // 종가만 추출
            sum += getPriceWithoutComma(elems[index].text())
            num++
        }

        index++
    }

    return sum / num
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

fun getMovingAverageOfDayBeforeLastDay(stockCode: String, days10: Int): Int {
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

            if ((j - numOf0_bias) % 5 == 0 && !(i == 1 && j <= 6)) { // 종가만 추출, 오늘,어제 가격 제외
                sum += getPriceWithoutComma(elems[j].text())
                num++
            }
        }
    }

    // 마지막 이틀 가격 추가
    url = url.dropLast(url.substring(url.lastIndexOf('=') + 1).length) + (days10 + 1)
    val document = Jsoup.connect(url).get()
    val elems = document.getElementsByClass("tah p11") // 숫자들을 가진 태그
    sum += getPriceWithoutComma(elems[0].text())
    num++

    if (getPriceWithoutComma(elems[1].text()) != 0) sum += getPriceWithoutComma(elems[5].text())
    else sum += getPriceWithoutComma(elems[6].text())
    num++

    return sum / num
}