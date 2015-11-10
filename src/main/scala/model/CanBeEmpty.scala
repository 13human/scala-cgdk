package model

trait CanBeEmpty[T <: AnyRef] {
  // scalastyle:off null
  final val empty: T = null.asInstanceOf[T]
  // scalastyle:on null
}

object CanBeEmpty {
  implicit class CanBeEmptyOps[T <: AnyRef](val underline: T) extends AnyVal {
    // scalastyle:off null
    def isEmpty: Boolean = underline eq null

    def isDefined: Boolean = underline ne null
    // scalastyle:on null
  }
}
