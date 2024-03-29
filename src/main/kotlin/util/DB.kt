package util

import stock.Stock
import java.sql.DriverManager
import java.sql.Timestamp

fun addFirstStocksInfoToDB(user: String, pw: String) {
    val stocks = getCodesTopKospiKosdaq200()
    var index = 0

    while (index < stocks.size) {
        val count = updateStockQuantityAtDB(stocks[index], user, pw)

        if (count == 1) index++
    }
}

fun rebalanceStocksInDB(user: String, pw: String) {
    val newStocks = getCodesTopKospiKosdaq200()
    val oldStocks = getAllStocksInfoFromDB(user, pw)

    // 먼저 200위 밖으로 나간 거 제거
    for (i in 0 until oldStocks.size) {
        val oldStock = oldStocks[i]
        var mustKeep = false

        // 수량 남아있으면 제외
        if (oldStock.quantity != 0) continue

        for (j in 0 until newStocks.size) {
            val newStock = newStocks[j]
            if (oldStock.code == newStock.code) {
                mustKeep = true
                break
            }
        }

        if (!mustKeep) {
            deleteStockInfoFromDB(oldStock, user, pw)
            println("리밸런싱: 종목 제거 - ${oldStock.code} ${oldStock.name}")
        }
    }

    // 새로 200위 안에 들어온 거 추가
    for (i in 0 until newStocks.size) {
        val newStock = newStocks[i]
        var alreadyExist = false

        for (j in 0 until oldStocks.size) {
            val oldStock = oldStocks[j]
            if (oldStock.code == newStock.code) {
                alreadyExist = true
                break
            }
        }

        if (!alreadyExist) {
            updateStockQuantityAtDB(newStock, user, pw)
            println("리밸런싱: 종목 추가 - ${newStock.code} ${newStock.name}")
        }
    }
}

fun addNewStocksInfoToDB(num: Int, user: String, pw: String) {
    if (num % 50 != 0) {
        println("50 단위로 num을 설정해주세요.")
        return
    }

    val stocks = getCodesTopKospiKosdaq(num)

    for (i in 0 until stocks.size) {
        addStockInfoToDB(stocks[i], user, pw)
    }
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

fun addStockInfoToDB(stock: Stock, user: String, pw: String) {
    Class.forName("com.mysql.cj.jdbc.Driver")
    val conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/STOCK_TRADING", user, pw)

    val sql = "INSERT INTO TB_STOCK (CODE, NAME, QUANTITY, AVERAGE_PRICE, LAST_SOLD_POINT, LAST_TRADING_DATE) " +
            "SELECT ?, ?, ?, ?, ?, ? FROM DUAL WHERE NOT EXISTS (SELECT * FROM TB_STOCK WHERE CODE = ?)"
    val stmt = conn.prepareStatement(sql)
    stmt.setString(1, stock.code)
    stmt.setString(2, stock.name)
    stmt.setInt(3, stock.quantity)
    stmt.setInt(4, stock.averagePrice)
    stmt.setDouble(5, stock.lastSoldPoint)
    stmt.setTimestamp(6, Timestamp(stock.lastTradingDate))
    stmt.setString(7, stock.code)

    stmt.executeUpdate()

    conn.close()
}

fun deleteStockInfoFromDB(stock: Stock, user: String, pw: String) {
    Class.forName("com.mysql.cj.jdbc.Driver")
    val conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/STOCK_TRADING", user, pw)

    val sql = "DELETE FROM TB_STOCK WHERE CODE = ?"
    val stmt = conn.prepareStatement(sql)
    stmt.setString(1, stock.code)

    stmt.executeUpdate()

    conn.close()
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