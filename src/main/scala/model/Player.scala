package model

/**
  * Содержит данные о текущем состоянии игрока.
  * @param id  Возвращает уникальный идентификатор игрока.
  * @param me  Возвращает { @code true} в том и только в том случае, если этот игрок ваш.
  * @param name  Возвращает имя игрока.
  * @param strategyCrashed  Возвращает специальный флаг --- показатель того, что стратегия игрока <<упала>>.
  *         Более подробную информацию можно найти в документации к игре.
  * @param score  Возвращает количество баллов, набранное игроком.
  */
class Player(val id: Long,
             val me: Boolean,
             val name: String,
             val strategyCrashed: Boolean,
             val score: Int)

object Player extends CanBeEmpty[Player]