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
import javafx.stage.Modality
import javafx.stage.Stage

// Create User Account
object Registrar {
	val scene by lazy { scene() }
	lateinit var status: RegistrationStatus

	val stage = Stage()

	enum class RegistrationStatus {
		SUCCESS, INVALID_EMAIL, USERNAME_TAKEN, PASSWORD_DIFF
	}

	private fun scene(): Scene {

		stage.initModality(Modality.APPLICATION_MODAL)

		val gridPane = GridPane()
		gridPane.alignment = Pos.CENTER
		gridPane.hgap = 10.0
		gridPane.vgap = 10.0
		gridPane.padding = Insets(25.0, 25.0, 25.0, 25.0)

		val email = TextField()
		email.promptText = "Email"
		email.font = GUIFont.regular

		val username = TextField()
		username.promptText = "Username"
		username.font = GUIFont.regular

		val password = PasswordField()
		password.promptText = "Password"
		password.font = GUIFont.regular

		val retypePassword = PasswordField()
		retypePassword.promptText = "Retype Password"
		retypePassword.font = GUIFont.regular

		val vbox = VBox(10.0)
		vbox.children.addAll(email, username, password, retypePassword)
		gridPane.add(vbox, 0, 0)

		val register = Button("Register")
		register.font = GUIFont.medium
		register.setOnAction { _ ->
			// Check if email address or username already exists
			//     - Connect to database
			//     - Check database for instances of email-address or username
			//         - If found
			//         - Display warning if either already exists in the system
			//     - Check if password matches retype password
			//         - If matches
			//         - Display warning that passwords don't match
			// Else execute a query that adds email address and password to database
			status = RegistrationStatus.SUCCESS
			stage.scene = CreationMessage.scene
			stage.showAndWait()
			if (status == RegistrationStatus.SUCCESS) {
				Welcome.stage.close()
			}
		}

		val back = Button("Go back")
		back.font = GUIFont.medium
		back.setOnAction { _ -> Welcome.stage.close() }

		val hbox = HBox(10.0)
		hbox.children.addAll(register, back)
		gridPane.add(hbox, 0, 1)

		return Scene(gridPane, 250.0, 225.0)
	}

	// Window shown when the Create button is pressed
	object CreationMessage {
		val scene by lazy { scene() }

		private fun scene(): Scene {

			val gridPane = GridPane()
			gridPane.alignment = Pos.CENTER
			gridPane.hgap = 10.0
			gridPane.vgap = 10.0
			gridPane.padding = Insets(25.0, 25.0, 25.0, 25.0)

			val message = when (status) {
				RegistrationStatus.SUCCESS -> Label("Registration complete")
				RegistrationStatus.INVALID_EMAIL -> Label("This is an invalid email address")
				RegistrationStatus.USERNAME_TAKEN -> Label("This username has already been taken")
				RegistrationStatus.PASSWORD_DIFF -> Label("The passwords don't match")
			}
			gridPane.add(message, 0, 0)

			val register = Button("OK")
			register.font = GUIFont.medium
			register.setOnAction { _ ->	Registrar.stage.close()	}
			gridPane.add(register, 1, 1)

			return Scene(gridPane, 200.0, 100.0)
		}
	}
}
