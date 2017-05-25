package future

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation._
import org.slf4j.MDC

trait MdcContextAwareRunnable {
  var contextMap: java.util.Map[String, String]
}

class DefaultMdcContextAwareRunnable(
    var contextMap: java.util.Map[String, String])
    extends MdcContextAwareRunnable

@Aspect
class FutureInstrumentation {

  @DeclareMixin("scala.concurrent.impl.CallbackRunnable")
  def mixinContextToFutureRelatedRunnable: MdcContextAwareRunnable =
    new DefaultMdcContextAwareRunnable(null)

  @Pointcut(
    "execution((scala.concurrent.impl.CallbackRunnable).new(..)) && this(runnable)")
  def futureRelatedRunnableCreation(runnable: MdcContextAwareRunnable): Unit = {}

  @After("futureRelatedRunnableCreation(runnable)")
  def afterCreation(runnable: MdcContextAwareRunnable): Unit = {
    runnable.contextMap = MDC.getCopyOfContextMap
  }

  @Pointcut(
    "execution(* (scala.concurrent.impl.CallbackRunnable).run()) && this(runnable)")
  def futureRelatedRunnableExecution(runnable: MdcContextAwareRunnable): Unit = {}

  @Around("futureRelatedRunnableExecution(runnable)")
  def aroundExecution(pjp: ProceedingJoinPoint,
                      runnable: MdcContextAwareRunnable): Any = {
    try {
      if (runnable.contextMap != null) {
        MDC.setContextMap(runnable.contextMap)
      }
      pjp.proceed()
    } finally {
      MDC.clear()
    }
  }

}
