import javafx.animation.AnimationTimer
import scalafx.application.JFXApp3
import scalafx.application.JFXApp3.PrimaryStage
import scalafx.beans.property.ObjectProperty
import scalafx.scene.Scene
import scalafx.scene.layout.Pane
import scalafx.scene.paint.Color
import scalafx.stage.Screen
import scala.util.Random

object Main extends JFXApp3 {

  private final val agentSize: Int      = 9
  private final val numberOfTunas: Int  = 500
  
  override def start(): Unit = {
    val (screenWidth, screenHeight) = (Screen.primary.visualBounds.width.toInt, Screen.primary.visualBounds.height.toInt)
    val (boardWidth, boardHeight)   = (screenWidth / agentSize, screenHeight / agentSize)

    stage = new PrimaryStage {
      title = "Predator"
      width = screenWidth
      height = screenHeight
      scene = new Scene {
        fill = Color.Black
        content = 
      }
    }
  }
}
