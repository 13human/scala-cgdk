package model

/**
  * Класс, определяющий метательный снаряд. Содержит также все свойства круглого юнита.
  * @param id Возвращает уникальный идентификатор объекта.
  * @param mass Возвращает массу объекта в единицах массы.
  * @param x Возвращает X-координату центра объекта. Ось абсцисс направлена слева направо.
  * @param y Возвращает Y-координату центра объекта. Ось ординат направлена свеху вниз.
  * @param speedX Возвращает X-составляющую скорости объекта. Ось абсцисс направлена слева направо.
  * @param speedY Возвращает Y-составляющую скорости объекта. Ось ординат направлена свеху вниз.
  * @param angle Возвращает угол поворота объекта в радианах. Нулевой угол соответствует направлению оси абсцисс.
  *              Положительные значения соответствуют повороту по часовой стрелке.
  * @param angularSpeed Возвращает скорость вращения объекта.
  *                     Положительные значения соответствуют вращению по часовой стрелке.
  * @param radius Возвращает радиус объекта.
  * @param carId Возвращает идентификатор кодемобиля, выпустившего данный снаряд.
  * @param playerId Возвращает идентификатор игрока, кодемобиль которого выпустил данный снаряд.
  * @param  `type` Возвращает тип метательного снаряда.
  */
class Projectile(id: Long,
                 mass: Double,
                 x: Double,
                 y: Double,
                 speedX: Double,
                 speedY: Double,
                 angle: Double,
                 angularSpeed: Double,
                 radius: Double,
                 val carId: Long,
                 val playerId: Long,
                 val `type`: ProjectileType) extends CircularUnit(id, mass, x, y, speedX, speedY, angle, angularSpeed, radius)

object Projectile extends CanBeEmpty[Projectile]