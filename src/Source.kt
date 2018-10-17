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
	val regularItalic  = Font.loadFont("file:resources/fonts/SF-Pro-Text-RegularItalic.otf", 12.0)!!
	val semibold       = Font.loadFont("file:resources/fonts/SF-Pro-Text-Semibold.otf", 20.0)!!
	val semibolditalic = Font.loadFont("file:resources/fonts/SF-Pro-Text-SemiboldItalic.otf", 20.0)!!
}

// This is the main window used by all scene objects to initialize one-another
lateinit var window: Stage

// By default, the first screen users will see is the login, this is to make
// sure that the correct user is logged into the MYSQL database
// e.g. <Username>, <Password> = "root", "password"
class GUI : Application() {

	// We will be using the user's locally installed database for the purposes
	// of this assignment. The user should already have a MYSQL database
	// created with the name "scheduler"
	val url = "jdbc:mysql://localhost:3306/"
	val database = "scheduler"

	// Entry-point for GUI app.
	override fun start(primaryStage: Stage) {
		window = primaryStage

		window.scene = Logger.scene
		window.title = "Vision"
		window.show()
	}

	// Kotlin needs this for some reason
	companion object {
		@JvmStatic
		fun main(args: Array<String>) {
			launch(GUI::class.java)
		}
	}
}