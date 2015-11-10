package model

/**
  * Направление.
  */
sealed trait Direction

object Direction {

  /**
    * Налево/слева.
    */
  case object LEFT extends Direction

  /**
    * Направо/справа.
    */
  case object RIGHT extends Direction

  /**
    * Вверх/сверху.
    */
  case object UP extends Direction

  /**
    * Вниз/снизу.
    */
  case object DOWN extends Direction

}
