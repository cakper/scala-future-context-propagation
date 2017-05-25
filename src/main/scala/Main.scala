import org.slf4j.{LoggerFactory, MDC}

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

object Main extends App {

  val logger = LoggerFactory.getLogger(getClass.getName.stripSuffix("$"))

  withMdc(Map("test" -> "abc")) {
    logger.info("My test message")

    Await.ready(Future(logger.info("2nd message")), 5.seconds)
  }

  def withMdc[A](mdc: Map[String, String])(func: => A): A = {
    val oldContextMap = MDC.getCopyOfContextMap
    mdc.foreach { case (key, value) => MDC.put(key, value) }
    val result = func
    MDC.setContextMap(oldContextMap)
    result
  }
}
