package model

/**
  * Класс, определяющий кодемобиль. Содержит также все свойства прямоугольного юнита.
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
  * @param width Возвращает ширину объекта.
  * @param height Возвращает высоту объекта.
  * @param playerId Возвращает идентификатор игрока, которому принадлежит кодемобиль.
  * @param teammateIndex Возвращает 0-индексированный номер кодемобиля среди юнитов одного игрока.
  * @param teammate Возвращает { @code true}, если и только если данный кодемобиль принадлежит вам.
  * @param `type` Возвращает тип кодемобиля.
  * @param projectileCount Возвращает количество метательных снарядов.
  * @param nitroChargeCount Возвращает количество зарядов для системы закиси азота.
  * @param oilCanisterCount Возвращает количество канистр с мазутом.
  * @param remainingProjectileCooldownTicks Возвращает количество тиков, по прошествии которого кодемобиль может запустить очередной снаряд,
  *                                         или { @code 0}, если кодемобиль может совершить данное действие в текущий тик.
  * @param remainingNitroCooldownTicks Возвращает количество тиков, по прошествии которого кодемобиль может использовать очередной заряд системы
  *                                    закиси азота, или { @code 0}, если кодемобиль может совершить данное действие в текущий тик.
  * @param remainingOilCooldownTicks Возвращает количество тиков, по прошествии которого кодемобиль может разлить очередную лужу мазута,
  *                                  или { @code 0}, если кодемобиль может совершить данное действие в текущий тик.
  * @param remainingNitroTicks Возвращает количество оставшихся тиков действия системы закиси азота.
  * @param remainingOiledTicks Возвращает количество тиков, оставшихся до полного высыхания кодемобиля, попавшего в лужу мазута.
  * @param durability Возвращает текущую прочность кодемобиля в интервале [{ @code 0.0}, { @code 1.0}].
  * @param enginePower Возвращает относительную мощность двигателя кодемобиля. Значение находится в интервале
  *                    [{ @code -1.0}, { @code 1.0}] кроме случаев, когда кодемобиль использует ускорение <<нитро>>.
  * @param wheelTurn Возвращает относительный угол поворота колёс (или руля, что эквивалентно) кодемобиля в интервале  [{ @code -1.0}, { @code 1.0}].
  * @param nextWaypointIndex Возвращает индекс следующего ключевого тайла в массиве { @code world.waypoints}.
  * @param nextWaypointX Возвращает компоненту X позиции следующего ключевого тайла.
  *                      Конвертировать позицию в точные координаты можно, используя значение { @code game.trackTileSize}.
  * @param nextWaypointY Возвращает компоненту Y позиции следующего ключевого тайла.
  *                      Конвертировать позицию в точные координаты можно, используя значение { @code game.trackTileSize}.
  * @param finishedTrack Возвращает { @code true}, если и только если данный кодемобиль финишировал. Финишировавший кодемобиль
  *                      перестаёт управляться игроком, а также участвовать в столкновениях с другими юнитами.
  */
class Car(id: Long,
          mass: Double,
          x: Double,
          y: Double,
          speedX: Double,
          speedY: Double,
          angle: Double,
          angularSpeed: Double,
          width: Double,
          height: Double,
          val playerId: Long,
          val teammateIndex: Int,
          val teammate: Boolean,
          val `type`: CarType,
          val projectileCount: Int,
          val nitroChargeCount: Int,
          val oilCanisterCount: Int,
          val remainingProjectileCooldownTicks: Int,
          val remainingNitroCooldownTicks: Int,
          val remainingOilCooldownTicks: Int,
          val remainingNitroTicks: Int,
          val remainingOiledTicks: Int,
          val durability: Double,
          val enginePower: Double,
          val wheelTurn: Double,
          val nextWaypointIndex: Int,
          val nextWaypointX: Int,
          val nextWaypointY: Int,
          val finishedTrack: Boolean) extends RectangularUnit(id, mass, x, y, speedX, speedY, angle, angularSpeed, width, height)

object Car extends CanBeEmpty[Car]


