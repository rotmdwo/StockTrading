package stock

data class Stock(val code: String, val name: String, var quantity: Int,
                 var averagePrice: Int, var lastSoldPoint: Double, var lastTradingDate: Long) {

}