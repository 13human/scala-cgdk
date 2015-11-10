package model



final class PlayerContext(val cars: Array[Car],
                          val world: World)

object PlayerContext extends CanBeEmpty[PlayerContext]