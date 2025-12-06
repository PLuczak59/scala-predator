import scalafx.scene.shape.Circle
import scalafx.scene.paint.Color

case class Predator(size: Int, posX: Int, posY: Int) {
  def draw: Circle = {
    new Circle {
      centerX = posX
      centerY = posY
      radius = size / 2
      fill = Color.Red
    }
  }

  def move(direction: Direction): Predator = {
    val (nx, ny) = direction match {
      case Direction.UP    => (posX, posY - size)
      case Direction.DOWN  => (posX, posY + size)
      case Direction.LEFT  => (posX - size, posY)
      case Direction.RIGHT => (posX + size, posY)
    }
    copy(posX = nx, posY = ny)
  }
}
