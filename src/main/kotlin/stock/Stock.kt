package stock

data class Stock(val code: String, val name: String, val quantity: Int,
                 val averagePrice: Int, val lastTradingDate: Long) {

}