import model.Car;
import model.Game;
import model.Move;
import model.World;

public final class MyStrategy implements Strategy {
    @Override
    public void move(Car self, World world, Game game, Move move) {
        move.setEnginePower(1.0D);
        move.setThrowProjectile(true);
        move.setSpillOil(true);

        if (world.getTick() > game.getInitialFreezeDurationTicks()) {
            move.setUseNitro(true);
        }
    }
}
