package model


/**
  * Предоставляет доступ к различным игровым константам.
  * @param randomSeed  Возвращает некоторое число, которое ваша стратегия может использовать для инициализации генератора
  * случайных чисел. Данное значение имеет рекомендательный характер, однако позволит более точно
  * воспроизводить прошедшие игры.
  * @param tickCount  Возвращает базовую длительность игры в тиках.
  *                   Реальная длительность может отличаться от этого значения в меньшую сторону.
  *                   Поле может быть определено как { @code game.initialFreezeDurationTicks + game.lapCount * game.lapTickCount}.
  *                   Значение поля не меняется в процессе игры. Эквивалентно { @code world.tickCount}.
  * @param worldWidth  Возвращает ширину игрового мира в тайлах.
  * @param worldHeight  Возвращает высоту игрового мира в тайлах.
  * @param trackTileSize  Возвращает размер (ширину и высоту) одного тайла.
  * @param trackTileMargin  Возвращает отступ от границы тайла до границы прямого участка трассы, проходящего через этот тайл.
  *                         Радиусы всех закруглённых сочленений участков трассы также равны этому значению.
  * @param lapCount  Возвращает количество кругов (циклов прохождения списка ключевых точек { @code world.waypoints}),
  *                  которое необходимо пройти для завершения трассы.
  * @param lapTickCount  Возвращает количество тиков, которое выделяется кодемобилям на прохождение одного круга.
  *                      Значение является составной частью выражения для нахождения базовой длительности игры ({ @code game.tickCount}) и
  *                      не используется в целях ограничения на прохождение одного отдельного круга.
  * @param initialFreezeDurationTicks  Возвращает количество тиков в начале игры, в течение которых кодемобиль не может изменять своё положение.
  *                                    Значение является составной частью выражения для нахождения базовой длительности игры ({ @code game.tickCount}).
  * @param burningTimeDurationFactor  Возвращает коэффициент, определяющий количество тиков до завершения игры после финиширования трассы
  *                                   очередным кодемобилем. Для получения более подробной информации смотрите документацию к
  *                                   { @code world.lastTickIndex}.
  * @param finishTrackScores  Возвращает 0-индексированный массив, содержащий количество баллов, зарабатываемых кодемобилями при
  *                           завершении трассы. Кодемобиль, финишировавший первым, приносит владельцу { @code finishTrackScores[0]} баллов,
  *                           вторым --- { @code finishTrackScores[1]} и так далее.
  * @param finishLapScore  Возвращает количество баллов, зарабатываемых кодемобилем при прохождении одного круга.
  *                        Баллы начисляются не единовременно, а постепенно, по мере прохождения ключевых точек.
  * @param lapWaypointsSummaryScoreFactor  Возвращает долю от баллов за круг ({ @code game.finishLapScore}), которую кодемобиль заработает при
  *                                        прохождении всех ключевых точек круга, кроме последней. Баллы равномерно распределены по ключевым точкам.
  * @param carDamageScoreFactor  Возвращает количество баллов, зарабатываемых кодемобилем при нанесении { @code 1.0} урона кодемобилю
  *                              другого игрока. При нанесении меньшего урона количество баллов пропорционально падает. Результат всегда
  *                              округляется в меньшую сторону.
  * @param carEliminationScore  Возвращает количество баллов, зарабатываемых кодемобилем при уничтожении кодемобиля другого игрока.
  * @param carWidth  Возвращает ширину кодемобиля.
  * @param carHeight  Возвращает высоту кодемобиля.
  * @param carEnginePowerChangePerTick  Возвращает максимальное значение, на которое может измениться относительная мощность двигателя кодемобиля
  *                                     ({ @code car.enginePower}) за один тик.
  * @param carWheelTurnChangePerTick  Возвращает максимальное значение, на которое может измениться относительный угол поворота колёс
  *                                   кодемобиля ({ @code car.wheelTurn}) за один тик.
  * @param carAngularSpeedFactor  Возвращает коэффициент, используемый для вычисления составляющей угловой скорости кодемобиля, порождаемой
  *                               движением кодемобиля при ненулевом относительном угле поворота колёс. Для получения более подробной информации
  *                               смотрите документацию к { @code move.wheelTurn}.
  * @param carMovementAirFrictionFactor  Возвращает относительную потерю модуля скорости кодемобиля за один тик.
  * @param carRotationAirFrictionFactor  Возвращает относительную потерю модуля угловой скорости кодемобиля за один тик. Относительная потеря
  *                                      применяется только к составляющей угловой скорости кодемобиля, не порождаемой движением кодемобиля при ненулевом
  *                                      относительном угле поворота колёс. Для получения более подробной информации смотрите документацию к
  *                                      { @code move.wheelTurn}.
  * @param carLengthwiseMovementFrictionFactor  Возвращает абсолютную потерю составляющей скорости кодемобиля, направленной вдоль продольной оси
  *                                             кодемобиля, за один тик.
  * @param carCrosswiseMovementFrictionFactor  Возвращает абсолютную потерю составляющей скорости кодемобиля, направленной вдоль поперечной оси
  *                                            кодемобиля, за один тик.
  * @param carRotationFrictionFactor  Возвращает абсолютную потерю модуля угловой скорости кодемобиля за один тик. Абсолютная потеря
  *                                   применяется только к составляющей угловой скорости кодемобиля, не порождаемой движением кодемобиля при ненулевом
  *                                   относительном угле поворота колёс. Для получения более подробной информации смотрите документацию к
  *                                   { @code move.wheelTurn}.
  * @param throwProjectileCooldownTicks  Возвращает длительность задержки в тиках, применяемой к кодемобилю после метания им снаряда.
  *                                      В течение этого времени кодемобиль не может метать новые снаряды.
  * @param useNitroCooldownTicks  Возвращает длительность задержки в тиках, применяемой к кодемобилю после использования им ускорения
  *                               <<нитро>>. В течение этого времени кодемобиль не может повторно использовать систему закиси азота.
  * @param spillOilCooldownTicks  Возвращает длительность задержки в тиках, применяемой к кодемобилю после использования им канистры с
  *                               мазутом. В течение этого времени кодемобиль не может разлить ещё одну канистру.
  * @param nitroEnginePowerFactor  Возвращает относительную мощность двигателя кодемобиля, мгновенно устанавливаемую при использовании
  *                                системы закиси азота для ускорения кодемобиля.
  * @param nitroDurationTicks  Возвращает длительность ускорения <<нитро>> в тиках.
  * @param carReactivationTimeTicks  Возвращает длительность интервала в тиках, по прошествии которого сильно повреждённый кодемобиль
  *                                  (значение { @code car.durability} равно нулю) будет восстановлен.
  * @param buggyMass  Возвращает массу кодемобиля типа багги ({ @code CarType.BUGGY}).
  * @param buggyEngineForwardPower  Возвращает максимальную мощность двигателя кодемобиля типа багги ({ @code CarType.BUGGY}) в направлении,
  *                                 совпадающем с направлением кодемобиля.
  * @param buggyEngineRearPower  Возвращает максимальную мощность двигателя кодемобиля типа багги ({ @code CarType.BUGGY}) в направлении,
  *                              противоположном направлению кодемобиля.
  * @param jeepMass  Возвращает массу кодемобиля типа джип ({ @code CarType.JEEP}).
  * @param jeepEngineForwardPower  Возвращает максимальную мощность двигателя кодемобиля типа джип ({ @code CarType.JEEP}) в направлении,
  *                                совпадающем с направлением кодемобиля.
  * @param jeepEngineRearPower  Возвращает максимальную мощность двигателя кодемобиля типа джип ({ @code CarType.JEEP}) в направлении,
  *                             противоположном направлению кодемобиля.
  * @param bonusSize  Возвращает размер (ширину и высоту) бонуса.
  * @param bonusMass  Возвращает массу бонуса.
  * @param pureScoreAmount  Возвращает количество баллов, мгновенно получаемых игроком, кодемобиль которого подобрал бонусные баллы
  *                         ({ @code BonusType.PURE_SCORE}).
  * @param washerRadius  Возвращает радиус шайбы ({ @code ProjectileType.WASHER}).
  * @param washerMass  Возвращает массу шайбы ({ @code ProjectileType.WASHER}).
  * @param washerInitialSpeed  Возвращает начальную скорость шайбы ({ @code ProjectileType.WASHER}).
  * @param washerDamage  Возвращает урон шайбы ({ @code ProjectileType.WASHER}).
  * @param sideWasherAngle  Возвращает модуль отклонения направления полёта двух шайб от направления кодемобиля.
  *                         Направление третьей шайбы совпадает с направлением кодемобиля.
  * @param tireRadius  Возвращает радиус шины ({ @code ProjectileType.TIRE}).
  * @param tireMass  Возвращает массу шины ({ @code ProjectileType.TIRE}).
  * @param tireInitialSpeed  Возвращает начальную скорость шины ({ @code ProjectileType.TIRE}).
  * @param tireDamageFactor  Возвращает количество урона, которое шина нанесёт неподвижно стоящему кодемобилю при попадании в него с
  *                          начальной скоростью ({ @code game.tireInitialSpeed}) и под прямым углом к поверхности кодемобиля. Движение
  *                          кодемобиля в направлении, совпадающем с направлением движения шины, уменьшает урон, движение в противоположном
  *                          направлении --- увеличивает.
  * @param tireDisappearSpeedFactor  Возвращает отношение текущей скорости шины к начальной ({ @code game.tireInitialSpeed}), при превышении
  *                                  которого в момент столкновения с другим объектом шина отскакивает и продолжает свой полёт. В противном случае
  *                                  шина убирается из игрового мира.
  * @param oilSlickInitialRange  Возвращает расстояние между ближайшими точками лужи мазута и кодемобиля при использовании канистры с
  *                              мазутом.
  * @param oilSlickRadius  Возвращает радиус лужи мазута.
  * @param oilSlickLifetime  Возвращает длительность высыхания лужи мазута в тиках.
  * @param maxOiledStateDurationTicks  Возвращает максимально возможную длительность высыхания кодемобиля, центр которого попал в лужу мазута.
  *                                    При этом, длительность высыхания лужа мазута сокращается на то же количество тиков. Таким образом, реальная
  *                                    длительность высыхания кодемобиля не может превышать оставшуюся длительность высыхания лужи.
  */
class Game(val randomSeed: Long,
           val tickCount: Int,
           val worldWidth: Int,
           val worldHeight: Int,
           val trackTileSize: Double,
           val trackTileMargin: Double,
           val lapCount: Int,
           val lapTickCount: Int,
           val initialFreezeDurationTicks: Int,
           val burningTimeDurationFactor: Double,
           val finishTrackScores: Vector[Int],
           val finishLapScore: Int,
           val lapWaypointsSummaryScoreFactor: Double,
           val carDamageScoreFactor: Double,
           val carEliminationScore: Int,
           val carWidth: Double,
           val carHeight: Double,
           val carEnginePowerChangePerTick: Double,
           val carWheelTurnChangePerTick: Double,
           val carAngularSpeedFactor: Double,
           val carMovementAirFrictionFactor: Double,
           val carRotationAirFrictionFactor: Double,
           val carLengthwiseMovementFrictionFactor: Double,
           val carCrosswiseMovementFrictionFactor: Double,
           val carRotationFrictionFactor: Double,
           val throwProjectileCooldownTicks: Int,
           val useNitroCooldownTicks: Int,
           val spillOilCooldownTicks: Int,
           val nitroEnginePowerFactor: Double,
           val nitroDurationTicks: Int,
           val carReactivationTimeTicks: Int,
           val buggyMass: Double,
           val buggyEngineForwardPower: Double,
           val buggyEngineRearPower: Double,
           val jeepMass: Double,
           val jeepEngineForwardPower: Double,
           val jeepEngineRearPower: Double,
           val bonusSize: Double,
           val bonusMass: Double,
           val pureScoreAmount: Int,
           val washerRadius: Double,
           val washerMass: Double,
           val washerInitialSpeed: Double,
           val washerDamage: Double,
           val sideWasherAngle: Double,
           val tireRadius: Double,
           val tireMass: Double,
           val tireInitialSpeed: Double,
           val tireDamageFactor: Double,
           val tireDisappearSpeedFactor: Double,
           val oilSlickInitialRange: Double,
           val oilSlickRadius: Double,
           val oilSlickLifetime: Int,
           val maxOiledStateDurationTicks: Int)

object Game extends CanBeEmpty[Game]
