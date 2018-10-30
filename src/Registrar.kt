import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.stage.Modality
import javafx.stage.Stage
import javafx.scene.layout.*
import kotlin.system.exitProcess

// Create User Account
object Registrar {
	private var pendingUser = Account("", "", "", null)

	val stage = Stage()

	lateinit var status: RegistrationStatus
	enum class RegistrationStatus {
		SUCCESS, INVALID_EMAIL, USERNAME_TAKEN, PASSWORD_DIFF, CONNECTION_FAILED
	}

	val scene by lazy { scene() }

	private fun scene(): Scene {

		stage.initModality(Modality.APPLICATION_MODAL)

		val gridPane = grid()

		val text = Label("Create Account")
		text.font = GUIFont.heavy
		gridPane.add(text, 0, 0)

		//Email field
		val email = TextField()
		email.promptText = "Email"
		email.font = GUIFont.regular

		//Username field
		val username = TextField()
		username.promptText = "Username"
		username.font = GUIFont.regular

		//Password field
		val password = PasswordField()
		password.promptText = "Password"
		password.font = GUIFont.regular

		//Retype password
		val retypePassword = PasswordField()
		retypePassword.promptText = "Retype Password"
		retypePassword.font = GUIFont.regular

		//Add all fields to vBox
		val vBox = VBox(10.0)
		vBox.children.addAll(email, username, password, retypePassword)
		gridPane.add(vBox, 0, 1)

		//Register button
		val register = Button("Register")
		register.font = GUIFont.medium

		//On register click
		register.setOnAction {
			//Make sure passwords are the same
			if (password.text != retypePassword.text) {
				status = RegistrationStatus.PASSWORD_DIFF
			} else {
				//If data valid then set/store user data
				pendingUser.email = email.text
				pendingUser.username = username.text
				pendingUser.password = password.text

				registerUser()
			}

			// Display status message; if registration succeeded, exit.
			stage.scene = CreationMessage.scene
			stage.showAndWait()
			if (status == RegistrationStatus.SUCCESS) {
				Welcome.stage.close()
			}
		}

		//Back Button
		val back = Button("Go back")
		back.font = GUIFont.medium
		back.setOnAction { Welcome.stage.close() }

		//Horizontal Box
		val hBox = HBox(10.0)
		hBox.children.addAll(register, back)
		gridPane.add(hBox, 0, 2)

		return Scene(gridPane, 250.0, 225.0)
	}

	// Window shown when the Create button is pressed
	object CreationMessage {
		val scene by lazy { scene() }

		private fun scene(): Scene {
			//GridPane for creation message
			val gridPane = GridPane()
			gridPane.alignment = Pos.CENTER
			gridPane.hgap = 10.0
			gridPane.vgap = 10.0
			gridPane.padding = Insets(25.0, 25.0, 25.0, 25.0)

			//Choose message to display to user
			val message = when (status) {
				RegistrationStatus.SUCCESS -> Label("Registration complete")
				RegistrationStatus.INVALID_EMAIL -> Label("This is an invalid email address")
				RegistrationStatus.USERNAME_TAKEN -> Label("This username has already been taken")
				RegistrationStatus.PASSWORD_DIFF -> Label("The passwords don't match")
				RegistrationStatus.CONNECTION_FAILED -> Label("A connection could not be established to database")
			}

			//Left alignment
			val leftPane = StackPane(message)
			leftPane.alignment = Pos.CENTER_LEFT

			//Register button
			val register = Button("OK")
			register.font = GUIFont.medium
			register.setOnAction { Registrar.stage.close()	}

			//Right Pane alignment
			val rightPane = StackPane(register)
			rightPane.alignment = Pos.CENTER_RIGHT

			//Add both panes
			gridPane.add(leftPane, 0, 0)
			gridPane.add(rightPane, 0, 1)

			return Scene(gridPane, 200.0, 100.0)
		}
	}

	// Inserts the user into the database with a NULL phone number
	private fun registerUser() {

		//Create SQL Statements
		val userIDStatement = connection.createStatement()
		val usernameStatement = connection.createStatement()
		val emailStatement = connection.createStatement()
		val successStatement = connection.createStatement()

		//Execute UserID statement
		val userIDResult = userIDStatement.executeQuery(getMaxID())
		userIDResult.next()
		val maxID = userIDResult.getInt(1)

		//If taken then display error
		val usernameTakenResult = usernameStatement.executeQuery(checkForExistingUsername(pendingUser.username))
		usernameTakenResult.next()
		val usernameCount = usernameTakenResult.getInt(1)

		//CHECK IF EMAIL EXISTS
		val emailExistsResult = emailStatement.executeQuery(checkForExistingEmail(pendingUser.email))
		emailExistsResult.next()
		val emailCount = emailExistsResult.getInt(1)

		//Update the status to proper status
		when {
			usernameCount > 0 ->
				status = RegistrationStatus.USERNAME_TAKEN
			emailCount > 0 ->
				status = RegistrationStatus.INVALID_EMAIL
			else -> {
				status = RegistrationStatus.SUCCESS
				try {
					successStatement.executeUpdate(createAccount(maxID + 1, pendingUser))
					account = pendingUser
					Welcome.welcomeBanner.text = "Logged in as " + pendingUser.username
				} catch (e: Exception) {
					e.printStackTrace()
					println("Catastrophic failure!")
					exitProcess(-1)
				}
			}
		}
	}
}
