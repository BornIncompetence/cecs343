import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.layout.GridPane
import javafx.scene.text.Text

// Main menu:
//     Modify user account data
//     Menu bar include account, appointment, setting, and help
object Welcome {
	data class User(val email: String, val password: String) {}
	lateinit var user: User

	val scene by lazy {	scene()	}

	private fun scene(): Scene {

		val gridPane = GridPane()
		gridPane.alignment = Pos.CENTER
		gridPane.hgap = 10.0
		gridPane.vgap = 10.0
		gridPane.padding = Insets(25.0, 25.0, 25.0, 25.0)

		val email = Text(user.email)
		email.font = GUIFont.bold
		gridPane.add(email, 0, 0)

		val password = Text(user.password)
		password.font = GUIFont.bold
		gridPane.add(password, 1, 0)

		return Scene(gridPane, 150.0, 50.0)
	}
}