package controllers

// import play.api._
import play.api.mvc._
import play.api.libs.json._
import play.api.libs.functional.syntax._

class Application extends Controller {

  object Database {

    val price : Float = 0;
    val buys : Seq[BuyOrder] = Seq()
    val sells : Seq[SellOrder] = Seq()

    def buy(order: BuyOrder) : Unit = buys :+ order

    def sell(order: SellOrder) : Unit = sells :+ order
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

  case class BuyOrderResponse(time: Long, price: Double, status: String)
  implicit val BuyOrderResponseWrites = new Writes[BuyOrderResponse] {
    def writes(buyOrderResponse: BuyOrderResponse) = Json.obj(
      "time" -> buyOrderResponse.time,
      "price" -> buyOrderResponse.price,
      "status" -> buyOrderResponse.status
    )
  }

  def index = Action {
    val json = Json.toJson(Health("ok"))
    Ok(json)
  }

  def getPrice = Action {
    val json = Json.toJson(Price(10, 10))
    Ok(json)
  }

  case class BuyOrder(amount: Float, currency: String, payment_method: String)
  case class SellOrder(amount: Float, currency: String, payment_method: String)

  def placeBuyOrder = Action { request =>
    val body : AnyContent = request.body
    val jsonBody : Option[JsValue] = body.asJson

    case class BuyOrderRaw(amount: String, currency: String, payment_method: String)

    implicit val BuyOrderReads: Reads[BuyOrderRaw] = (
      (JsPath \ "amount").read[String] and
        (JsPath \ "currency").read[String] and
        (JsPath \ "payment_method").read[String]
      )(BuyOrderRaw.apply _)

    jsonBody.map { json =>
      println(json.validate[BuyOrderRaw])
      val amount: Float = ((json \ "amount").as[String]).toFloat
      val currency: String = (json \ "currency").as[String]
      val paymentMethod: String = (json \ "payment_method").as[String]
      Database.buy(BuyOrder(amount, currency, paymentMethod))
      Ok("ok")
    }.getOrElse {
      BadRequest("not ok")
    }
  }

  def placeSellOrder = Action { request =>
    val body : AnyContent = request.body
    val jsonBody : Option[JsValue] = body.asJson

    case class SellOrderRaw(amount: String, currency: String, payment_method: String)

    implicit val SellOrderReads: Reads[SellOrderRaw] = (
      (JsPath \ "amount").read[String] and
        (JsPath \ "currency").read[String] and
        (JsPath \ "payment_method").read[String]
      )(SellOrderRaw.apply _)

    jsonBody.map { json =>
      println(json.validate[SellOrderRaw])
      val amount: Float = ((json \ "amount").as[String]).toFloat
      val currency: String = (json \ "currency").as[String]
      val paymentMethod: String = (json \ "payment_method").as[String]
      Database.sell(SellOrder(amount, currency, paymentMethod))
      Ok("ok")
    }.getOrElse {
      BadRequest("not ok")
    }
  }

  def getBuysHistory = Action { request =>
    Ok("0")
  }

  def getSellsHistory = Action { request =>
    Ok("0")
  }
}

