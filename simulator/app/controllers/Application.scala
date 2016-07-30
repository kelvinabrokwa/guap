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
    request.body.asJson.map { json =>
      json.validate[(String)].map{
        case "hello" => Ok(Json.toJson(BuyOrderResponse(10, 20, "great work!")))
      }.recoverTotal{
        e => BadRequest("nah b")
      }
    }.getOrElse {
      BadRequest("expecting JSON")
    }
  }

}

