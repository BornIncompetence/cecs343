import javafx.application.Application
import javafx.scene.text.Font
import javafx.stage.Stage

object GUIFont {
	val bold           =  Font.loadFont("file:resources/fonts/SF-Pro-Text-Bold.otf", 20.0)!!
	val boldItalic     = Font.loadFont("file:resources/fonts/SF-Pro-Text-BoldItalic.otf", 20.0)!!
	val heavy          = Font.loadFont("file:resources/fonts/SF-Pro-Text-Heavy.otf", 20.0)!!
	val heavyItalic    = Font.loadFont("file:resources/fonts/SF-Pro-Text-HeavyItalic.otf", 20.0)!!
	val light          = Font.loadFont("file:resources/fonts/SF-Pro-Text-Light.otf", 20.0)!!
	val lightItalic    = Font.loadFont("file:resources/fonts/SF-Pro-Text-LightItalic.otf", 20.0)!!
	val medium         = Font.loadFont("file:resources/fonts/SF-Pro-Text-Medium.otf", 12.0)!!
	val mediumItalic   = Font.loadFont("file:resources/fonts/SF-Pro-Text-MediumItalic.otf", 20.0)!!
	val regular        = Font.loadFont("file:resources/fonts/SF-Pro-Text-Regular.otf", 12.0)!!
	val regularItalic  = Font.loadFont("file:resources/fonts/SF-Pro-Text-RegularItalic.otf", 20.0)!!
	val semibold       = Font.loadFont("file:resources/fonts/SF-Pro-Text-Semibold.otf", 20.0)!!
	val semibolditalic = Font.loadFont("file:resources/fonts/SF-Pro-Text-SemiboldItalic.otf", 20.0)!!
}

lateinit var window: Stage

class GUI : Application() {

	val url = "jdbc:mysql://localhost:3306/"
	val database = "scheduler"

	override fun start(primaryStage: Stage) {
		window = primaryStage

		window.scene = Logger.scene
		window.title = "Vision"
		window.show()
	}

	companion object {
		@JvmStatic
		fun main(args: Array<String>) {
			launch(GUI::class.java)
		}
	}
}