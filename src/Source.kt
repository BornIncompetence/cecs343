import javafx.application.Application
import javafx.scene.text.Font
import javafx.stage.Stage
import java.sql.DriverManager
import java.sql.SQLException

object GUIFont {
	val bold           = Font.loadFont("file:resources/fonts/SF-Pro-Text-Bold.otf", 20.0)!!
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

// This is the main window used between Logger and Welcome objects
// By reassigning window.scene, we can switch windows
// New windows are created by creating a new stage within other objects
// BTW, objects are effectively Singletons with syntactic sugar
lateinit var window: Stage

// By default, the first screen users will see is the login
// this is to make sure that the correct user is logged into the MYSQL database
// e.g. <Username>, <Password> = "java", "coffee"
// TODO: Enforce login credentials to match values in the database
// TODO: e.g. find entry that has username, then check if password matches
class GUI : Application() {

	// Entry-point for GUI app. Logger is first screen that users see
	override fun start(primaryStage: Stage) {
		window = primaryStage

		window.scene = Logger.scene
		window.title = "Vision"
		window.show()
	}

	// We'll nestle the main here so it doesn't conflict with other main
	// * unpacks the Array<String>
	companion object {
		@JvmStatic
		fun main(args: Array<String>) {
			launch(GUI::class.java, *args)
		}
	}
}

// TESTING QUERIES
fun main(args: Array<String>) {
	var pendingUser = Account("john@john.com", "john", "john", null)

	val connection = try {
		DriverManager.getConnection(SQL.url + SQL.database, SQL.username, SQL.password)
	} catch (e: SQLException) { null }
	val statement = connection!!.createStatement()
	println(SQL.createAccount(5, pendingUser))
	val userIDResult = statement.executeQuery(SQL.getMaxID())

	var maxID: Int
	if (userIDResult.next()) {
		maxID = userIDResult.getInt(1)
	}


	val result = statement.executeUpdate(SQL.createAccount(6, pendingUser))

	//println(SQL.change_user_password)
	//println(SQL.change_username)
	//println(SQL.change_password)
}