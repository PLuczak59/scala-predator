import scala.collection.immutable.Queue
import scala.annotation.tailrec
import scala.util.Random

object PathFinding {

  private val random = new Random()

  private def isPreyVisible(
                             predatorX: Int,
                             predatorY: Int,
                             preyX: Int,
                             preyY: Int,
                             boardWidth: Int,
                             boardHeight: Int
                           ): Boolean = {
    val dx = math.abs(preyX - predatorX)
    val dy = math.abs(preyY - predatorY)
    
    val directDistanceX = dx
    val wrappedDistanceX = boardWidth - dx
    val directDistanceY = dy
    val wrappedDistanceY = boardHeight - dy
    
    directDistanceX <= wrappedDistanceX && directDistanceY <= wrappedDistanceY
  }

  private def randomDirection(): Direction = {
    val directions = List(Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT)
    directions(random.nextInt(directions.length))
  }

  def findNextDirection(
                         predatorX: Int,
                         predatorY: Int,
                         preyX: Int,
                         preyY: Int,
                         boardWidth: Int,
                         boardHeight: Int,
                         cellSize: Int
                       ): Direction = {

    if (!isPreyVisible(predatorX, predatorY, preyX, preyY, boardWidth, boardHeight)) {
      return randomDirection()
    }

    val startX = predatorX / cellSize
    val startY = predatorY / cellSize
    val targetX = preyX / cellSize
    val targetY = preyY / cellSize

    val gridWidth = boardWidth / cellSize
    val gridHeight = boardHeight / cellSize

    if (startX == targetX && startY == targetY) {
      return Direction.UP
    }

    case class State(x: Int, y: Int, path: List[Direction])

    def getNeighbors(x: Int, y: Int): List[(Int, Int, Direction)] = {
      List(
        (x, y - 1, Direction.UP),
        (x, y + 1, Direction.DOWN),
        (x - 1, y, Direction.LEFT),
        (x + 1, y, Direction.RIGHT)
      ).filter { case (nx, ny, _) =>
        nx >= 0 && nx < gridWidth && ny >= 0 && ny < gridHeight
      }
    }

    @scala.annotation.tailrec
    @tailrec
    def bfs(queue: Queue[State], visited: Set[(Int, Int)]): Option[Direction] = {
      if (queue.isEmpty) None
      else {
        val (current, rest) = queue.dequeue

        if (current.x == targetX && current.y == targetY) {
          current.path.headOption
        } else if (visited.contains(current.x, current.y)) {
          bfs(rest, visited)
        } else {
          val neighbors = getNeighbors(current.x, current.y)
            .filterNot { case (nx, ny, _) => visited.contains(nx, ny) }
            .map { case (nx, ny, dir) => State(nx, ny, current.path :+ dir) }

          bfs(rest.enqueueAll(neighbors), visited + ((current.x, current.y)))
        }
      }
    }

    bfs(Queue(State(startX, startY, List.empty)), Set.empty)
      .getOrElse(Direction.UP)
  }
}
