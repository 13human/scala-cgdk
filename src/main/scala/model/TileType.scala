package model

/**
  * Тип тайла.
  */
sealed trait TileType

object TileType {

  /**
    * Пустой тайл.
    */
  case object EMPTY extends TileType

  /**
    * Тайл с прямым вертикальным участком дороги.
    */
  case object VERTICAL extends TileType

  /**
    * Тайл с прямым горизонтальным участком дороги.
    */
  case object HORIZONTAL extends TileType

  /**
    * Тайл выполняющий роль сочленения двух других тайлов: справа и снизу от данного тайла.
    */
  case object LEFT_TOP_CORNER extends TileType

  /**
    * Тайл выполняющий роль сочленения двух других тайлов: слева и снизу от данного тайла.
    */
  case object RIGHT_TOP_CORNER extends TileType

  /**
    * Тайл выполняющий роль сочленения двух других тайлов: справа и сверху от данного тайла.
    */
  case object LEFT_BOTTOM_CORNER extends TileType

  /**
    * Тайл выполняющий роль сочленения двух других тайлов: слева и сверху от данного тайла.
    */
  case object RIGHT_BOTTOM_CORNER extends TileType

  /**
    * Тайл выполняющий роль сочленения трёх других тайлов: слева снизу и сверху от данного тайла.
    */
  case object LEFT_HEADED_T extends TileType

  /**
    * Тайл выполняющий роль сочленения трёх других тайлов: справа снизу и сверху от данного тайла.
    */
  case object RIGHT_HEADED_T extends TileType

  /**
    * Тайл выполняющий роль сочленения трёх других тайлов: слева справа и сверху от данного тайла.
    */
  case object TOP_HEADED_T extends TileType

  /**
    * Тайл выполняющий роль сочленения трёх других тайлов: слева справа и снизу от данного тайла.
    */
  case object BOTTOM_HEADED_T extends TileType

  /**
    * Тайл выполняющий роль сочленения четырёх других тайлов: со всех сторон от данного тайла.
    */
  case object CROSSROADS extends TileType

  /**
    * Тип тайла пока не известен.
    */
  case object UNKNOWN extends TileType
}
