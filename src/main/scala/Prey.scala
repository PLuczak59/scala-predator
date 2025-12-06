import scalafx.scene.shape.Rectangle
import scalafx.scene.paint.Color

case class Prey(size: Int, posX: Int, posY: Int) {
  def draw: Rectangle = {
    new Rectangle {
      x = posX
      y = posY
      width = size
      height = size
      fill = Color.Blue
    }
  }

  def move(direction: Direction): Prey = {
    val (nx, ny) = direction match {
      case Direction.UP    => (posX, posY - size)
      case Direction.DOWN  => (posX, posY + size)
      case Direction.LEFT  => (posX - size, posY)
      case Direction.RIGHT => (posX + size, posY)
    }
    copy(posX = nx, posY = ny)
  }
}

