import model.CanBeEmpty.CanBeEmptyOps
import model.{Move, PlayerContext}

import scala.annotation.tailrec


final class Runner(args: Array[String]) {
    private val remoteProcessClient = new RemoteProcessClient(args(0), Integer.parseInt(args(1)))
    private val token: String = args(2)

    def run():Unit = {
        try {
            remoteProcessClient.writeToken(token)
            val teamSize = remoteProcessClient.readTeamSize()
            remoteProcessClient.writeProtocolVersion()
            val game = {
                val g = remoteProcessClient.readGameContext()
                if (g.isEmpty) { throw new NullPointerException(s"game: $g") }
                g
            }

            val strategies = Array.fill(teamSize) { new MyStrategy() }

            @tailrec
            def iteratePlayerContext(playerContext: PlayerContext): Unit = if (playerContext.isDefined) {
                val playerCars = playerContext.cars

                if (playerCars.length == teamSize) {
                    val moves = List.fill(teamSize) { new Move() }
                    playerCars.zip(moves).foreach {
                        case (car, move) if car.isDefined &&
                          playerContext.world.isDefined =>
                            strategies(car.teammateIndex).move(car, playerContext.world, game, move)
                        case _ =>
                    }
                    remoteProcessClient.writeMoves(moves)
                }
                iteratePlayerContext(remoteProcessClient.readPlayerContext())
            }
            iteratePlayerContext(remoteProcessClient.readPlayerContext())
        } finally {
            remoteProcessClient.close()
        }
    }
}
object Runner {
    def main(args: Array[String]): Unit = {
        val params =
            if (args.length == 3) { args }
            else { Array("127.0.0.1", "31001", "0000000000000000") }
        new Runner(params).run()
    }
}
