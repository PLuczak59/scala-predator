import scalafx.Includes._
import scalafx.animation.{KeyFrame, Timeline}
import scalafx.application.JFXApp3
import scalafx.application.JFXApp3.PrimaryStage
import scalafx.beans.property.ObjectProperty
import scalafx.scene.Scene
import scalafx.scene.layout.Pane
import scalafx.scene.text.{Text, Font}
import scalafx.scene.control.Button
import scalafx.scene.input.KeyCode
import scalafx.scene.paint.Color
import scalafx.stage.Screen
import scalafx.util.Duration

object Main extends JFXApp3 {

  private final val cycleTime: Int = 70
  private final val numberOfPredator: Int = 3
  private final val cellSize: Int = 20

  override def start(): Unit = {
    val (boardWidth, boardHeight): (Int, Int) = (
      Screen.primary.visualBounds.width.intValue,
      Screen.primary.visualBounds.height.intValue
    )

    val gameState = ObjectProperty(
      GameState.initial(boardWidth, boardHeight, numberOfPredator, cellSize)
    )

    val gamePane = new Pane {
      prefWidth = boardWidth
      prefHeight = boardHeight
      style = "-fx-background-color: linear-gradient(from 0% 0% to 0% 100%, #0B3D0B 0%, #013220 100%);"
    }

    val timerText = new Text {
      text <== gameState.map(gs => s"Time: ${gs.formatTime(gs.elapsedTime)}")
      fill = Color.White
      font = Font.font(20)
      x = boardWidth - 150
      y = 30
    }

    val bestTimeText = new Text {
      text <== gameState.map(gs =>
        gs.bestTime.map(bt => s"Best: ${gs.formatTime(bt)}").getOrElse("Best: --:--")
      )
      fill = Color.Yellow
      font = Font.font(20)
      x = boardWidth - 150
      y = 60
    }

    val replayButton = new Button("Rejouer") {
      layoutX = boardWidth / 2 - 50
      layoutY = boardHeight / 2
      visible = false
      onAction = _ => {
        gameState.value = gameState.value.reset(
          GameState.initial(boardWidth, boardHeight, numberOfPredator, cellSize).prey,
          GameState.initial(boardWidth, boardHeight, numberOfPredator, cellSize).predators
        )
      }
    }

    gameState.onChange { (_, _, newState) =>
      replayButton.visible = newState.isGameOver
      gamePane.children.clear()
      gamePane.children.addAll(
        newState.prey.draw,
        timerText,
        bestTimeText,
        replayButton
      )
      newState.predators.foreach(p => gamePane.children.add(p.draw))
    }

    gamePane.children.clear()
    gamePane.children.addAll(
      gameState.value.prey.draw,
      timerText,
      bestTimeText,
      replayButton
    )
    gameState.value.predators.foreach(p => gamePane.children.add(p.draw))

    stage = new PrimaryStage {
      title = "Predator-Prey Game"
      width = boardWidth
      height = boardHeight
      scene = new Scene {
        content = gamePane

        onKeyPressed = event => {
          val newDirection = event.code match {
            case KeyCode.Up    => Some(Direction.UP)
            case KeyCode.Down  => Some(Direction.DOWN)
            case KeyCode.Left  => Some(Direction.LEFT)
            case KeyCode.Right => Some(Direction.RIGHT)
            case _ => gameState.value.currentDirection
          }

          if (newDirection.isDefined && !gameState.value.isGameOver) {
            gameState.value = gameState.value.copy(currentDirection = newDirection)
          }
        }
      }
    }

    new Timeline {
      keyFrames = List(
        KeyFrame(
          time = Duration(cycleTime),
          onFinished = _ => {
            if (!gameState.value.isGameOver && gameState.value.currentDirection.isDefined) {
              gameState.value = gameState.value.nextCycle(cycleTime)
            }
          }
        )
      )
      cycleCount = Timeline.Indefinite
    }.play()
  }
}
