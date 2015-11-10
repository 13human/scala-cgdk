package model

/**
  * Тип бонуса.
  */
sealed trait BonusType

object BonusType {
  /**
    * Ремкомплект. Полностью устраняет все повреждения кодемобиля при подборе.
    */
  case object REPAIR_KIT extends BonusType

  /**
    * Ящик с метательными снарядами. При подборе пополняет запас снарядов кодемобиля на единицу. Тип снарядов
    * соответствует типу кодемобиля.
    */
  case object AMMO_CRATE extends BonusType

  /**
    * Топливо для системы закиси азота. При использовании мгновенно устанавливает мощность двигателя кодемобиля равной
    * значению {@code game.nitroEnginePowerFactor} и не даёт изменять её в течение {@code game.useNitroCooldownTicks}
    * тиков.
    */
  case object NITRO_BOOST extends BonusType

  /**
    * Канистра с мазутом. При использовании на {@code game.oilSlickLifetime} тиков создаёт позади кодемобиля скользкую
    * круглую область. Радиус области равен {@code game.oilSlickRadius}, центр находится на продольной оси кодемобиля,
    * а расстояние между ближайшими точками области и кодемобиля составляет {@code game.oilSlickInitialRange}.
    */
  case object OIL_CANISTER extends BonusType

  /**
    * Баллы в чистом виде. При подборе данного бонуса игроку мгновенно начисляется {@code game.pureScoreAmount} баллов.
    */
  case object PURE_SCORE extends BonusType

}

