package controllers

// import play.api._
import play.api.mvc._
import play.api.libs.json._
import play.api.libs.functional.syntax._

class Application extends Controller {

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

  def placeBuyOrder = Action { request =>
    val body: AnyContent = request.body
    val jsonBody: Option[JsValue] = body.asJson

    case class BuyOrder(amount: Double, currency: String, payment_method: String)
    implicit val BuyOrderReads: Reads[BuyOrder] = (
      (JsPath \ "amount").read[Double] and
        (JsPath \ "currency").read[String] and
        (JsPath \ "payment_method").read[String]
      )(BuyOrder.apply _)

    jsonBody.map { json =>
      print(json.validate[BuyOrder])
      Ok("ok")
    }.getOrElse {
      BadRequest("not ok")
    }
  }

}

