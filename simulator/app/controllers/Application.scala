package controllers

// import play.api._
import play.api.mvc._
import play.api.libs.json._
// import play.api.libs.functional.syntax._

class Application extends Controller {

  object Market {
    val r = scala.util.Random
    def price : Price = Price(java.lang.System.currentTimeMillis, r.nextInt.toDouble)
  }

  case class BuyOrder(price: Price,
                      status: String,
                      request: BuyOrderRequest)

  case class SellOrder(price: Price,
                       status: String,
                       request: SellOrderRequest)

  object Database {
    var buys : Seq[BuyOrder] = Seq()
    var sells : Seq[SellOrder] = Seq()

    def buy(orderReq: BuyOrderRequest) : BuyOrder = {
      val order: BuyOrder = BuyOrder(Market.price, "", orderReq)
      buys = buys :+ order
      order
    }

    def sell(orderReq: SellOrderRequest) : SellOrder = {
      val order = SellOrder(Market.price, "", orderReq)
      sells = sells :+ order
      order
    }
  }

  case class Health(status: String)
  implicit val healthWrites = new Writes[Health] {
    def writes(health: Health) = Json.obj(
      "status" -> health.status
    )
  }

  case class Price(time: Long, price: Double)
  implicit val priceWrites = new Writes[Price] {
    def writes(price: Price) = Json.obj(
      "time" -> price.time,
      "price" -> price.price
    )
  }

  case class BuyOrderResponse(price: Price, status: String)
  implicit val BuyOrderResponseWrites = new Writes[BuyOrderResponse] {
    def writes(buyOrderResponse: BuyOrderResponse) = Json.obj(
      "time" -> buyOrderResponse.price.time,
      "price" -> buyOrderResponse.price.price,
      "status" -> buyOrderResponse.status
    )
  }

  case class SellOrderResponse(price: Price, status: String)
  implicit val SellOrderResponseWrites = new Writes[SellOrderResponse] {
    def writes(sellOrderResponse: SellOrderResponse) = Json.obj(
      "time" -> sellOrderResponse.price.time,
      "price" -> sellOrderResponse.price.price,
      "status" -> sellOrderResponse.status
    )
  }

  def index = Action {
    Ok(Json.toJson(Health("ok")))
  }

  def getPrice = Action {
    val json = Json.toJson(Market.price)
    Ok(json)
  }

  case class BuyOrderRequest(amount: Double, currency: String, payment_method: String)
  case class SellOrderRequest(amount: Double, currency: String, payment_method: String)

  def placeBuyOrder = Action { request =>
    val body: AnyContent = request.body
    val jsonBody: Option[JsValue] = body.asJson

    jsonBody.map { json =>
      val amount: Double = (json \ "amount").as[String].toDouble
      val currency: String = (json \ "currency").as[String]
      val paymentMethod: String = (json \ "payment_method").as[String]
      val order: BuyOrder = Database.buy(BuyOrderRequest(amount, currency, paymentMethod))
      val res = BuyOrderResponse(order.price, order.status)
      Ok(Json.toJson(res))
    }.getOrElse {
      BadRequest(Json.toJson(Json.obj(
        "error" -> "bad request"
      )))
    }
  }

  def placeSellOrder = Action { request =>
    val body : AnyContent = request.body
    val jsonBody : Option[JsValue] = body.asJson

    jsonBody.map { json =>
      val amount: Double = (json \ "amount").as[String].toDouble
      val currency: String = (json \ "currency").as[String]
      val paymentMethod: String = (json \ "payment_method").as[String]
      val order: SellOrder = Database.sell(SellOrderRequest(amount, currency, paymentMethod))
      val res = SellOrderResponse(order.price, order.status)
      Ok(Json.toJson(res))
    }.getOrElse {
      BadRequest(Json.toJson(Json.obj(
        "error" -> "bad request"
      )))
    }
  }

  implicit val buyOrderRequestWrites = new Writes[BuyOrderRequest] {
    def writes(buyOrderRequest: BuyOrderRequest) = Json.obj(
      "amount" -> buyOrderRequest.amount,
      "currency" -> buyOrderRequest.currency,
      "payment_method" -> buyOrderRequest.payment_method
    )
  }

  implicit val sellOrderRequestWrites = new Writes[SellOrderRequest] {
    def writes(sellOrderRequest: SellOrderRequest) = Json.obj(
      "amount" -> sellOrderRequest.amount,
      "currency" -> sellOrderRequest.currency,
      "payment_method" -> sellOrderRequest.payment_method
    )
  }

  implicit val buysOrderWrites = new Writes[BuyOrder] {
    def writes(buyOrder: BuyOrder) = Json.obj(
      "time" -> JsNumber(buyOrder.price.time),
      "price" -> JsNumber(buyOrder.price.price),
      "request" -> Json.toJson(buyOrder.request)
    )
  }

  implicit val sellsOrderWrites = new Writes[SellOrder] {
    def writes(sellOrder: SellOrder) = Json.obj(
      "time" -> JsNumber(sellOrder.price.time),
      "price" -> JsNumber(sellOrder.price.price),
      "request" -> Json.toJson(sellOrder.request)
    )
  }

  implicit val buysHistoryResponseWrites = new Writes[Seq[BuyOrder]] {
    def writes(buyHistory: Seq[BuyOrder]) = Json.arr(buyHistory.map{ buyOrder =>
      Json.toJson(buyOrder)
    })
  }

  implicit val sellsHistoryResponseWrites = new Writes[Seq[SellOrder]] {
    def writes(sellHistory: Seq[SellOrder]) = Json.arr(sellHistory.map{ sellOrder =>
      Json.toJson(sellOrder)
    })
  }

  def getBuysHistory = Action { request =>
    Ok(Json.toJson(Database.buys))
  }

  def getSellsHistory = Action { request =>
    Ok(Json.toJson(Database.sells))
  }

  class Wallet {
    val balance: Double = 0;
  }

  object Wallets {
    val wallets: Seq[Wallet] = Seq()
  }

  def getWallet = Action { request =>
    Ok("mulah")
  }
}
