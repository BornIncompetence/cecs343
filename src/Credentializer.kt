import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.PasswordField
import javafx.scene.control.TextField
import javafx.scene.layout.GridPane
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox

@Deprecated("No suitable stage entry for this scene")
object Credentializer {
	data class User(val email: String, val password: String) {}
	lateinit var user: User

	val scene by lazy {	scene()	}

	private fun scene(): Scene {

		val gridPane = GridPane()
		gridPane.alignment = Pos.CENTER
		gridPane.hgap = 10.0
		gridPane.vgap = 10.0
		gridPane.padding = Insets(25.0, 25.0, 25.0, 25.0)

		val email = TextField()
		email.promptText = "Email"
		email.font = GUIFont.regular

		val password = PasswordField()
		password.promptText = "Password"
		password.font = GUIFont.regular

		val vbox = VBox(10.0)
		vbox.children.addAll(email, password)
		gridPane.add(vbox, 0, 0)

		val accept = Button("Accept")
		accept.font = GUIFont.medium
		accept.setOnAction {
			_ ->
			// Change user account credentials on the server, then close this
			Welcome.stage.close()
		}

		val cancel = Button("Cancel")
		cancel.font = GUIFont.medium
		cancel.setOnAction {
			_ ->
			Welcome.stage.close()
		}

		val hbox = HBox(10.0)
		hbox.children.addAll(accept, cancel)
		gridPane.add(hbox, 0, 1)

		return Scene(gridPane, 250.0, 150.0)
	}
}
