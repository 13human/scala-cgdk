import model.{Car, Game, Move, World}

final class MyStrategy extends Strategy {
  override def move(self: Car, world: World, game: Game, move: Move) {
    move.enginePower = 1.0D
    move.throwProjectile = true
    move.spillOil = true

    if (world.tick > game.initialFreezeDurationTicks) {
      move.useNitro = true
    }
  }
}
