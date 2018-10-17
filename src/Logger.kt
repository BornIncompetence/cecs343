import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.PasswordField
import javafx.scene.control.TextField
import javafx.scene.layout.GridPane
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox

// Login to database
object Logger {
	val scene by lazy {	scene()	}

	private fun scene(): Scene {

		val gridPane = GridPane()
		gridPane.alignment = Pos.CENTER
		gridPane.hgap = 10.0
		gridPane.vgap = 10.0
		gridPane.padding = Insets(25.0, 25.0, 25.0, 25.0)

		val text = Label("Login")
		text.font = GUIFont.heavy
		gridPane.add(text, 0, 0)

		val username = TextField()
		username.promptText = "Username"
		username.font = GUIFont.regular

		val password = PasswordField()
		password.promptText = "Password"
		password.font = GUIFont.regular

		val vbox = VBox(10.0)
		vbox.children.addAll(username, password)
		gridPane.add(vbox, 0, 1)

		val signIn = Button("Sign in")
		signIn.font = GUIFont.medium
		signIn.setOnAction {
			_ ->
			Welcome.user = Welcome.User(username.text, password.text)
			window.scene = Welcome.scene
		}

		// val signUp = Button("Sign up")
		// signUp.font = GUIFont.medium
		// signUp.setOnAction { _ -> window.scene = Registrar.scene }

		// val hbox = HBox(10.0)
		// hbox.children.add(signIn)
		// hbox.children.add(signUp)
		// gridPane.add(hbox, 0, 2)

		gridPane.add(signIn, 0, 2)

		return Scene(gridPane, 250.0, 150.0)
	}
}