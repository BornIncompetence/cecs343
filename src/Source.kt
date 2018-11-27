import javafx.application.Application
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.layout.GridPane
import javafx.scene.text.Font
import javafx.stage.Stage
import java.sql.Connection

object GUIFont {
	// val bold           = Font.loadFont("file:resources/fonts/SF-Pro-Text-Bold.otf", 20.0)!!
	// val boldItalic     = Font.loadFont("file:resources/fonts/SF-Pro-Text-BoldItalic.otf", 20.0)!!
	val heavy          = Font.loadFont("file:resources/fonts/SF-Pro-Text-Heavy.otf", 20.0)!!
	// val heavyItalic    = Font.loadFont("file:resources/fonts/SF-Pro-Text-HeavyItalic.otf", 20.0)!!
	// val light          = Font.loadFont("file:resources/fonts/SF-Pro-Text-Light.otf", 20.0)!!
	// val lightItalic    = Font.loadFont("file:resources/fonts/SF-Pro-Text-LightItalic.otf", 20.0)!!
	val medium         = Font.loadFont("file:resources/fonts/SF-Pro-Text-Medium.otf", 12.0)!!
	// val mediumItalic   = Font.loadFont("file:resources/fonts/SF-Pro-Text-MediumItalic.otf", 20.0)!!
	val regular        = Font.loadFont("file:resources/fonts/SF-Pro-Text-Regular.otf", 12.0)!!
	val regularItalic  = Font.loadFont("file:resources/fonts/SF-Pro-Text-RegularItalic.otf", 12.0)!!
	// val semibold       = Font.loadFont("file:resources/fonts/SF-Pro-Text-Semibold.otf", 20.0)!!
	// val semibolditalic = Font.loadFont("file:resources/fonts/SF-Pro-Text-SemiboldItalic.otf", 20.0)!!
}

// Account data class used for storing current user credentials during the session
data class Account(var email: String, var username: String, var password: String, var phone: String?, var id: Int)

// Appointment data class used for storing one or more appointments
data class Appointment(var title: String, var startDate: String, var endDate: String, var id: Int)

// This is the main window used between Logger and Welcome objects
// By reassigning window.scene, we can switch windows
// New windows are created by creating a new stage within other objects
// BTW, objects are effectively Singletons with syntactic sugar
lateinit var window: Stage

// This is the connection that all windows use to create statements that execute SQL queries
// It only needs to be initialized once
lateinit var connection: Connection

// This is the account that is logged into the account
lateinit var account: Account

// This is the gridPane that every window uses
fun grid(): GridPane {
	val gridPane = GridPane()
	gridPane.alignment = Pos.CENTER
	gridPane.hgap = 10.0
	gridPane.vgap = 10.0
	gridPane.padding = Insets(25.0, 25.0, 25.0, 25.0)
	return gridPane
}

// By default, the first screen users will see is the login
// this is to make sure that the correct user is logged into the MYSQL database
// e.g. <Username>, <Password> = "java", "coffee"
class GUI : Application() {

	// Entry-point for GUI app. Logger is first screen that users see
	override fun start(primaryStage: Stage) {
		window = primaryStage

		window.scene = Logger.scene
		window.title = "Vision"
		window.show()
	}
}

fun main(args: Array<String>) {
	Application.launch(GUI::class.java, *args)
}