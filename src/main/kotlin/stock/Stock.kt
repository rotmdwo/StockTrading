package stock

data class Stock(val code: String, val name: String, var quantity: Int,
                 var averagePrice: Int, var lastTradingDate: Long) {

}