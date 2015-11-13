import java.io.{IOException, ByteArrayOutputStream, Closeable, UnsupportedEncodingException}
import java.net.Socket
import java.nio.{ByteBuffer, ByteOrder}

import RemoteProcessClient._
import model.{Unit => _, _}

import scala.annotation.{tailrec, switch}

final class RemoteProcessClient(host: String, port: Int) extends Closeable {
   private val (socket, inputStream, outputStream) = {
        val socket = {
            val skt = new Socket(host, port)
            skt.setSendBufferSize(BufferSizeBytes)
            skt.setReceiveBufferSize(BufferSizeBytes)
            skt.setTcpNoDelay(true)
            skt
        }
        (socket, socket.getInputStream, socket.getOutputStream)
    }

    private val outputStreamBuffer = new ByteArrayOutputStream(BufferSizeBytes)

    lazy val mapName:String = readString()
    var tilesXY:Vector[Vector[TileType]] = Vector()
    def readTilesXY = {
      val newTilesXY = readEnumArray2D(tileTypeFromByte _)
      if (newTilesXY != null && newTilesXY.nonEmpty) {
        tilesXY = newTilesXY
      }
      tilesXY
    }
    lazy val waypoints:Vector[Vector[Int]] = readIntArray2D();
    lazy val startingDirection:Direction = directionFromByte(readByte())

    @scala.inline
    def writeToken(token: String): Unit = {
        writeByte(messageTypeToInt(MessageType.AuthenticationToken))
        writeString(token)
        flush()
    }

    @scala.inline
    def readTeamSize(): Int = {
        ensureMessageType(messageTypeFromByte(readByte()), MessageType.TeamSize)
        readInt()
    }

    def writeProtocolVersion(): Unit = {
        writeByte(messageTypeToInt(MessageType.ProtocolVersion))
        writeInt(2)
        flush()
    }

    def readGameContext(): Game = {
        ensureMessageType(messageTypeFromByte(readByte()), MessageType.GameContext)

        if (readBoolean()) {
            new Game(
                readLong(), readInt(), readInt(), readInt(), readDouble(), readDouble(), readInt(), readInt(),
                readInt(), readDouble(), readIntArray(), readInt(), readDouble(), readDouble(), readInt(), readDouble(),
                readDouble(), readDouble(), readDouble(), readDouble(), readDouble(), readDouble(), readDouble(),
                readDouble(), readDouble(), readInt(), readInt(), readInt(), readDouble(), readInt(), readInt(),
                readDouble(), readDouble(), readDouble(), readDouble(), readDouble(), readDouble(), readDouble(),
                readDouble(), readInt(), readDouble(), readDouble(), readDouble(), readDouble(), readDouble(),
                readDouble(), readDouble(), readDouble(), readDouble(), readDouble(), readDouble(), readDouble(),
                readInt(), readInt())
        } else { Game.empty }
    }

    def readPlayerContext(): PlayerContext = {
        messageTypeFromByte(readByte()) match {
            case MessageType.GameOver => PlayerContext.empty
            case MessageType.PlayerContext =>
                if (readBoolean()) { new PlayerContext(readCars(), readWorld()) }
                else { PlayerContext.empty }
            case msgType: Any => throw new IllegalArgumentException(s"Received wrong message: $msgType.")
        }
    }

    def writeMoves(moves: List[Move]): Unit = {
        writeByte(messageTypeToInt(MessageType.Moves))

        if (moves.isEmpty) {
            writeInt(-1)
        } else {
            writeInt(moves.length)

            moves.foreach { move =>
                writeBoolean(value = true)
                writeDouble(move.enginePower);
                writeBoolean(move.brake);
                writeDouble(move.wheelTurn);
                writeBoolean(move.throwProjectile);
                writeBoolean(move.useNitro);
                writeBoolean(move.spillOil);
            }
        }
        flush()
    }

    def close(): Unit = socket.close()

    private def readWorld(): World = {
        if (readBoolean()) {
            new  World(
                readInt(), readInt(), readInt(), readInt(), readInt(), readPlayers(), readCars(), readProjectiles(),
                readBonuses(), readOilSlicks(), mapName, readTilesXY, waypoints, startingDirection
            )
        } else { World.empty }
    }

    private def readPlayers(): Vector[Player] = {
        val playerCount: Int = readInt()

        Vector.fill(playerCount) {
            if (readBoolean()) {
                new Player(readLong(), readBoolean(), readString(), readBoolean(), readInt())
            } else { Player.empty }
        }
    }

    private def readCars(): Vector[Car] = {
        val carCount: Int = readInt()
        Vector.fill(carCount) { readCar() }
    }

    private def readCar(): Car = {
        if (readBoolean()) {
            new Car(
                readLong(), readDouble(), readDouble(), readDouble(), readDouble(), readDouble(), readDouble(),
                readDouble(), readDouble(), readDouble(), readLong(), readInt(), readBoolean(), carTypeFromByte(readByte()),
            readInt(), readInt(), readInt(), readInt(), readInt(), readInt(), readInt(), readInt(), readDouble(),
            readDouble(), readDouble(), readInt(), readInt(), readInt(), readBoolean()
            )
        } else { Car.empty }
    }

  private def readProjectiles(): Vector[Projectile] = {
    val projectileCount = readInt()
    Vector.fill(projectileCount) { readProjectile() }
  }

  private def readProjectile(): Projectile = {
    if(readBoolean()) {
      new Projectile(
        readLong(), readDouble(), readDouble(), readDouble(), readDouble(), readDouble(), readDouble(),
        readDouble(), readDouble(), readLong(), readLong(), projectileTypeFromByte(readByte())
      )
    } else Projectile.empty
  }

  private def readBonuses(): Vector[Bonus] = {
    val bonusCount = readInt()
    Vector.fill(bonusCount) { readBonus() }
  }

  private def readBonus(): Bonus = {
    if(readBoolean()) {
      new Bonus(
        readLong(), readDouble(), readDouble(), readDouble(), readDouble(), readDouble(), readDouble(),
        readDouble(), readDouble(), readDouble(), bonusTypeFromByte(readByte())
      )
    } else Bonus.empty
  }

  private def readOilSlicks(): Vector[OilSlick] = {
    val oilSlickCount = readInt()
    Vector.fill(oilSlickCount) { readOilSlick() }
  }

  private def readOilSlick(): OilSlick = {
    if(readBoolean()) {
      new OilSlick(
        readLong(), readDouble(), readDouble(), readDouble(), readDouble(), readDouble(), readDouble(),
        readDouble(), readDouble(), readInt()
      )
    } else OilSlick.empty
  }

  private def readEnumArray[T](count:Int)(readTfromByte:Byte => T): Vector[T] = {
    readBytes(count).toVector.map(readTfromByte)
  }

  private def readEnumArray[T](readTfromByte:Byte => T): Vector[T] = {
    readEnumArray(readInt())(readTfromByte)
  }

  private def readEnumArray2D[T](readTfromByte:Byte => T): Vector[Vector[T]] = {
    val count = readInt()
    Vector.fill(count) {readEnumArray[T](readTfromByte)}
  }

    private def readString(): String = {
        try {
            val length: Int = readInt()
            if (length == -1) { "" }
            else { new String(readBytes(length), "UTF-8") }
        } catch {
            case e: UnsupportedEncodingException =>
                throw new IllegalArgumentException("UTF-8 is unsupported.", e)
        }
    }

    private def writeString(value: String): Unit = {
        try {
            val bytes = value.getBytes("UTF-8")
            writeInt(bytes.length)
            writeBytes(bytes)
        } catch {
            case e: UnsupportedEncodingException =>
                throw new IllegalArgumentException("UTF-8 is unsupported.", e)
        }
    }

    @scala.inline
    private def readBoolean(): Boolean = readByte() != 0

    @scala.inline
    private def readBooleanArray(count: Int): Vector[Boolean] = {
        val bytes: Vector[Byte] = readBytes(count).toVector
        bytes.map(0 != _)
    }

    @scala.inline
    private def readBooleanArray(): Vector[Boolean] = {
      readBooleanArray(readInt())
    }

    @scala.inline
    private def readBooleanArray2D(): Vector[Vector[Boolean]] = {
      val count = readInt()
      Vector.fill(count) {readBooleanArray()}
    }

    @scala.inline
    private def writeBoolean(value: Boolean): Unit = {
      writeBytes(Array[Byte](if (value) 1 else 0))
    }

    @scala.inline
    private def readInt(): Int = {
      ByteBuffer.wrap(readBytes(IntegerSizeBytes)).order(ProtocolByteOrder).getInt
    }

  @scala.inline
  private def readIntArray(count: Int): Vector[Int] = {
    val bytes: Array[Byte] = readBytes(count * IntegerSizeBytes)
    val ints = 0 until count map (i => ByteBuffer.wrap(
      bytes, i * IntegerSizeBytes, IntegerSizeBytes
    ).order(ProtocolByteOrder).getInt())
    ints.toVector
  }

  @scala.inline
  private def readIntArray(): Vector[Int] = {
    readIntArray(readInt())
  }

  @scala.inline
  private def readIntArray2D(): Vector[Vector[Int]] = {
    val count = readInt()
    Vector.fill(count) {readIntArray()}
  }


  @scala.inline
  private def writeInt(value: Int): Unit = {
    writeBytes(ByteBuffer.allocate(IntegerSizeBytes).order(ProtocolByteOrder).putInt(value).array)
  }

  @scala.inline
  private def readLong(): Long = {
    ByteBuffer.wrap(readBytes(LongSizeBytes)).order(ProtocolByteOrder).getLong
  }

  @scala.inline
  private def writeLong(value: Long): Unit = {
    writeBytes(ByteBuffer.allocate(LongSizeBytes).order(ProtocolByteOrder).putLong(value).array)
  }

  @scala.inline
  private def readDouble(): Double = {
    java.lang.Double.longBitsToDouble(readLong())
  }

  @scala.inline
  private def writeDouble(value: Double): Unit = {
    writeLong(java.lang.Double.doubleToLongBits(value))
  }

  private def readBytes(byteCount: Int): Array[Byte] = {
    def result(bytes: Array[Byte], offset: Int): Array[Byte] = {
      if (offset == byteCount) { bytes }
      else { throw new IOException(s"Can't read $byteCount bytes from input stream.") }
    }

    @tailrec
    def rb(offset: Int, bytes: Array[Byte]): Array[Byte] = {
      if (offset < byteCount) {
        val readByteCount = inputStream.read(bytes, offset, byteCount - offset)
        if (readByteCount != -1) {
          rb(offset + readByteCount, bytes)
        } else { result(bytes, offset) }
      } else { result(bytes, offset) }
    }
    rb(0, new Array[Byte](byteCount))
  }

  private def readByte(): Byte = {
    val byte = inputStream.read()
    if (byte == -1) { throw new IOException(s"Can't read 1 bytes from input stream.") }
    else { byte.asInstanceOf[Byte] }
  }

  @scala.inline
  private def writeBytes(bytes: Array[Byte]): Unit = outputStreamBuffer.write(bytes)

  @scala.inline
  private def writeByte(byte: Int): Unit = outputStreamBuffer.write(byte)

  private def flush(): Unit = {
    outputStream.write(outputStreamBuffer.toByteArray)
    outputStreamBuffer.reset()
    outputStream.flush()
  }
}

object RemoteProcessClient {
    private[RemoteProcessClient] val BufferSizeBytes: Int = 1 << 20
    private[RemoteProcessClient] val ProtocolByteOrder: ByteOrder = ByteOrder.LITTLE_ENDIAN
    private[RemoteProcessClient] val IntegerSizeBytes: Int = Integer.SIZE / java.lang.Byte.SIZE
    private[RemoteProcessClient] val LongSizeBytes: Int = java.lang.Long.SIZE / java.lang.Byte.SIZE

    sealed trait MessageType

    object MessageType {
        case object Unknown extends MessageType
        case object GameOver extends MessageType
        case object AuthenticationToken extends MessageType
        case object TeamSize extends MessageType
        case object ProtocolVersion extends MessageType
        case object GameContext extends MessageType
        case object PlayerContext extends MessageType
        case object Moves extends MessageType
    }

    @scala.inline
    private[RemoteProcessClient] def ensureMessageType(actualType: MessageType, expectedType: MessageType): Boolean =
        if (actualType != expectedType) {
            throw new IllegalArgumentException(s"Received wrong message [actual=$actualType, expected=$expectedType].")
        } else {
            true
        }

    // scalastyle:off magic.number
    def messageTypeToInt(value: MessageType): Int = value match {
        case MessageType.Unknown             => 0
        case MessageType.GameOver            => 1
        case MessageType.AuthenticationToken => 2
        case MessageType.TeamSize            => 3
        case MessageType.ProtocolVersion     => 4
        case MessageType.GameContext         => 5
        case MessageType.PlayerContext       => 6
        case MessageType.Moves               => 7
        case _                               => -1
    }

    def messageTypeFromByte(value: Byte): MessageType = (value: @switch) match {
        case 0 => MessageType.Unknown
        case 1 => MessageType.GameOver
        case 2 => MessageType.AuthenticationToken
        case 3 => MessageType.TeamSize
        case 4 => MessageType.ProtocolVersion
        case 5 => MessageType.GameContext
        case 6 => MessageType.PlayerContext
        case 7 => MessageType.Moves
        case _ => throw new IllegalArgumentException("messageTypeFromByte: " + value)
    }
    // scalastyle:on magic.number



    private[RemoteProcessClient] def carTypeFromByte(value: Byte): CarType = (value: @switch) match {
        case 0 => CarType.BUGGY
        case 1 => CarType.JEEP
        case _ => throw new IllegalArgumentException("carTypeFromByte: " + value)
    }

    private[RemoteProcessClient] def projectileTypeFromByte(value: Byte): ProjectileType = (value: @switch) match {
        case 0 => ProjectileType.WASHER
        case 1 => ProjectileType.TIRE
        case _ => throw new IllegalArgumentException("projectileTypeFromByte: " + value)
    }

    private[RemoteProcessClient] def bonusTypeFromByte(value: Byte): BonusType = (value: @switch) match {
        case 0 => BonusType.REPAIR_KIT
        case 1 => BonusType.AMMO_CRATE
        case 2 => BonusType.NITRO_BOOST
        case 3 => BonusType.OIL_CANISTER
        case 4 => BonusType.PURE_SCORE
        case _ => throw new IllegalArgumentException("bonusTypeFromByte: " + value)
    }

    private[RemoteProcessClient] def directionFromByte(value: Byte): Direction = (value: @switch) match {
        case 0 => Direction.LEFT
        case 1 => Direction.RIGHT
        case 2 => Direction.UP
        case 3 => Direction.DOWN
        case _ => throw new IllegalArgumentException("directionFromByte: " + value)
    }

    private[RemoteProcessClient] def tileTypeFromByte(value: Byte): TileType = (value: @switch) match {
      case 0 => TileType.EMPTY
      case 1 => TileType.VERTICAL
      case 2 => TileType.HORIZONTAL
      case 3 => TileType.LEFT_TOP_CORNER
      case 4 => TileType.RIGHT_TOP_CORNER
      case 5 => TileType.LEFT_BOTTOM_CORNER
      case 6 => TileType.RIGHT_BOTTOM_CORNER
      case 7 => TileType.LEFT_HEADED_T
      case 8 => TileType.RIGHT_HEADED_T
      case 9 => TileType.TOP_HEADED_T
      case 10 => TileType.BOTTOM_HEADED_T
      case 11 => TileType.CROSSROADS
      case 12 => TileType.UNKNOWN
      case _ => throw new IllegalArgumentException("tileTypeFromByte: " + value)
    }
    // scalastyle:on magic.number
}
