import scala.util.Random

case class GameState(
                      prey: Prey,
                      predators: List[Predator],
                      currentDirection: Option[Direction],
                      elapsedTime: Long,
                      bestTime: Option[Long],
                      isGameOver: Boolean,
                      boardWidth: Int,
                      boardHeight: Int,
                      cellSize: Int = 20
                    ) {

  private def wrapCoordinates(x: Int, y: Int): (Int, Int) = {
    val wrappedX = ((x % boardWidth) + boardWidth) % boardWidth
    val wrappedY = ((y % boardHeight) + boardHeight) % boardHeight
    (wrappedX, wrappedY)
  }

  private def movePreyByDirection(): GameState = {
    currentDirection match {
      case Some(dir) =>
        val movedPrey = prey.move(dir)
        val (wrappedX, wrappedY) = wrapCoordinates(movedPrey.posX, movedPrey.posY)
        copy(prey = movedPrey.copy(posX = wrappedX, posY = wrappedY))
      case None => this
    }
  }

  private def movePredatorsTowardsPrey(): GameState = {
    val movedPredators = predators.map { predator =>
      val direction = PathFinding.findNextDirection(
        predator.posX,
        predator.posY,
        prey.posX,
        prey.posY,
        boardWidth,
        boardHeight,
        cellSize
      )
      val moved = predator.move(direction)
      
      val bounceX = if (moved.posX < 0) {
        moved.copy(posX = 0)
      } else if (moved.posX >= boardWidth) {
        moved.copy(posX = boardWidth - cellSize)
      } else moved
      
      val bounceY = if (bounceX.posY < 0) {
        bounceX.copy(posY = 0)
      } else if (bounceX.posY >= boardHeight) {
        bounceX.copy(posY = boardHeight - cellSize)
      } else bounceX
      
      bounceY
    }
    copy(predators = movedPredators)
  }

  private def checkCollisions(): GameState = {
    val collisionRadius = cellSize * 2
    val hasCollision = predators.exists { predator =>
      val dx = predator.posX - prey.posX
      val dy = predator.posY - prey.posY
      val distance = math.sqrt(dx * dx + dy * dy)
      distance < collisionRadius
    }
    if (hasCollision) copy(isGameOver = true) else this
  }

  private def incrementTimer(deltaMs: Long): GameState = {
    copy(elapsedTime = elapsedTime + deltaMs)
  }

  def nextCycle(deltaMs: Long): GameState = {
    if (isGameOver || currentDirection.isEmpty) this
    else {
      this
        .movePreyByDirection()
        .movePredatorsTowardsPrey()
        .checkCollisions()
        .incrementTimer(deltaMs)
    }
  }

  def reset(newPrey: Prey, newPredators: List[Predator]): GameState = {
    val newBestTime = if (isGameOver) {
      bestTime match {
        case Some(best) => Some(math.min(best, elapsedTime))
        case None => Some(elapsedTime)
      }
    } else bestTime

    copy(
      prey = newPrey,
      predators = newPredators,
      currentDirection = None,
      elapsedTime = 0,
      bestTime = newBestTime,
      isGameOver = false
    )
  }

  def formatTime(timeMs: Long): String = {
    val totalSeconds = timeMs / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    f"$minutes%02d:$seconds%02d"
  }
}

object GameState {
  def initial(boardWidth: Int, boardHeight: Int, numberOfPredators: Int, cellSize: Int = 20): GameState = {
    val random = new Random()

    val preyX = (boardWidth / 2 / cellSize) * cellSize
    val preyY = (boardHeight / 2 / cellSize) * cellSize
    val prey = Prey(cellSize, preyX, preyY)

    val minDistance = boardWidth / 4

    @scala.annotation.tailrec
    def generatePredatorPosition(): (Int, Int) = {
      val x = random.nextInt(boardWidth / cellSize) * cellSize
      val y = random.nextInt(boardHeight / cellSize) * cellSize
      val dx = x - preyX
      val dy = y - preyY
      val distance = math.sqrt(dx * dx + dy * dy)

      if (distance >= minDistance) (x, y)
      else generatePredatorPosition()
    }

    val predators = List.fill(numberOfPredators) {
      val (x, y) = generatePredatorPosition()
      Predator(cellSize, x, y)
    }

    GameState(
      prey = prey,
      predators = predators,
      currentDirection = None,
      elapsedTime = 0,
      bestTime = None,
      isGameOver = false,
      boardWidth = boardWidth,
      boardHeight = boardHeight,
      cellSize = cellSize
    )
  }
}
