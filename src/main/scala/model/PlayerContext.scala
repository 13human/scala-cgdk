package model



final class PlayerContext(val cars: Vector[Car],
                          val world: World)

object PlayerContext extends CanBeEmpty[PlayerContext]