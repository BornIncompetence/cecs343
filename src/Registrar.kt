import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.stage.Modality
import javafx.stage.Stage
import java.sql.DriverManager
import java.sql.SQLException
import javafx.scene.layout.*
import kotlin.system.exitProcess


// Create User Account
object Registrar {
	private var pendingUser = Account("", "", "", null)

	//stage var
	val stage = Stage()

	lateinit var status: RegistrationStatus
	enum class RegistrationStatus {
		SUCCESS, INVALID_EMAIL, USERNAME_TAKEN, PASSWORD_DIFF, CONNECTION_FAILED
	}


	val scene by lazy { scene() }

	private fun scene(): Scene {

		stage.initModality(Modality.APPLICATION_MODAL)

		//Create gridpane
		val gridPane = GridPane()
		gridPane.alignment = Pos.CENTER
		gridPane.hgap = 10.0
		gridPane.vgap = 10.0
		gridPane.padding = Insets(25.0, 25.0, 25.0, 25.0)

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

		//Add all fields to vbox
		val vbox = VBox(10.0)
		vbox.children.addAll(email, username, password, retypePassword)
		gridPane.add(vbox, 0, 0)

		//Register button
		val register = Button("Register")
		register.font = GUIFont.medium

		//On register click
		register.setOnAction { _ ->
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

			// Display status message. If Registration succeeded, exit.
			// TODO: Switch account in session to newly created one
			stage.scene = CreationMessage.scene
			stage.showAndWait()
			if (status == RegistrationStatus.SUCCESS) {
				Welcome.stage.close()
			}
		}

		//Back Button
		val back = Button("Go back")
		back.font = GUIFont.medium
		back.setOnAction { _ -> Welcome.stage.close() }


		val hbox = HBox(10.0)
		hbox.children.addAll(register, back)
		gridPane.add(hbox, 0, 1)

		//Return the scene
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

			//Left allignment
			val leftPane = StackPane(message)
			leftPane.alignment = Pos.CENTER_LEFT

			//Register button
			val register = Button("OK")
			register.font = GUIFont.medium
			register.setOnAction { _ ->	Registrar.stage.close()	}

			//Right Pane allignment
			val rightPane = StackPane(register)
			rightPane.alignment = Pos.CENTER_RIGHT

			//Add both panes
			gridPane.add(leftPane, 0, 0)
			gridPane.add(rightPane, 0, 1)

			//Return scene
			return Scene(gridPane, 200.0, 100.0)
		}
	}

	// Inserts the user into the database with a NULL phone number
	private fun registerUser() {

		//Create SQL Statements
		val userIDStatement = Welcome.connection.createStatement()
		val usernameStatement = Welcome.connection.createStatement()
		val emailStatement = Welcome.connection.createStatement()
		val successStatement = Welcome.connection.createStatement()

		//Excute UserID statement
		val userIDResult = userIDStatement.executeQuery(SQL.getMaxID())
		userIDResult.next()
		val maxID = userIDResult.getInt(1)

		//If taken then display error
		val usernameTakenResult = usernameStatement.executeQuery(SQL.checkForExistingUsername(pendingUser.username))
		usernameTakenResult.next()
		val usernameCount = usernameTakenResult.getInt(1)

		//CHECK IF EMAIL EXISTS
		val emailExistsResult = emailStatement.executeQuery(SQL.checkForExistingEmail(pendingUser.email))
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
					successStatement.executeUpdate(SQL.createAccount(maxID + 1, pendingUser))
					Welcome.account = pendingUser
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
