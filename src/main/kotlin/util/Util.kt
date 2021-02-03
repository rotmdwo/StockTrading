package util

import org.jsoup.Jsoup

fun getCurrentPrice(stockCode: String): Int {
    val url = "https://finance.naver.com/item/main.nhn?code=${stockCode}"
    val document = Jsoup.connect(url).get()

    val dds = document.getElementsByTag("dd")
    for (i in 0 until dds.size) {
        val elem = dds[i]
        val text = elem.text()

        if (text.substring(0, 3) == "현재가") {
            return Integer.parseInt(text.substring(4, text.indexOf(' ', 4)).replace(",",""))
        }

    }

    return -1
}