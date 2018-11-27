import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.PasswordField
import javafx.scene.control.TextField
import javafx.scene.layout.StackPane
import javafx.scene.layout.VBox
import javafx.stage.Modality
import javafx.stage.Stage
import java.sql.DriverManager
import java.sql.SQLException

// Login to database
object Logger {
	// Stage that appears when user cannot login
	private val stage = Stage()

	lateinit var status: LoginStatus
	enum class LoginStatus {
		SUCCESS, INCORRECT, CONNECTION_FAILED
	}

	val scene by lazy {	scene()	}

	private fun scene(): Scene {

		stage.initModality(Modality.APPLICATION_MODAL)

		val gridPane = grid()

		//Login Label
		val text = Label("Login")
		text.font = GUIFont.heavy
		gridPane.add(text, 0, 0)

		//Username field
		val username = TextField("guest")
		username.promptText = "Username"
		username.font = GUIFont.regular

		//Password Field
		val password = PasswordField()
		password.promptText = "Password"
		password.font = GUIFont.regular

		//Vertical View Box
		val vBox = VBox(10.0)
		vBox.children.addAll(username, password)
		gridPane.add(vBox, 0, 1)

		//Sign in Button
		val signIn = Button("Sign in")
		signIn.font = GUIFont.medium

		//Sign In button click
		signIn.setOnAction {
			// Check if username OR email address matches with password
			if (login(username.text, password.text)) {
				window.scene = Welcome.scene
			} else {
				stage.scene = LoginMessage.scene
				stage.showAndWait()
			}
		}

		// Adding register button into login page, deprecated use
		/*
		val signUp = Button("Sign up")
		signUp.font = GUIFont.medium
		signUp.setOnAction { _ -> window.scene = Registrar.scene }

		val hBox = HBox(10.0)
		hBox.children.add(signIn)
		hBox.children.add(signUp)
		gridPane.add(hBox, 0, 2)
		*/

		gridPane.add(signIn, 0, 2)

		return Scene(gridPane, 250.0, 150.0)
	}

	// Returns true if the user was able to login
	// The system needs to be able to login to the database
	// And find the matching user stored in the database
	private fun login(name: String, pass: String): Boolean {

		// Initialize connection
		try {
			val connect = DriverManager.getConnection(SQL_URL + SQLDatabase, SQLUsername, SQLPassword)
			connection = connect
		} catch (e: SQLException) {
			status = LoginStatus.CONNECTION_FAILED
			return false
		}

		val statement = connection.createStatement()
		val result = statement.executeQuery(getMatchingRow(name, pass))
		return if (result.next()) {
			val email = result.getString("email")
			val username = result.getString("username")
			val password = result.getString("password")
			val phone = result.getString("phone")
			//Get user ID for purpose of inserting into Appointment table
			val id = result.getString("user_id").toInt();
			account = Account(email, username, password, phone, id)
			status = LoginStatus.SUCCESS
			true
		} else {
			status = LoginStatus.INCORRECT
			false
		}
	}

	// Window shown when Login has failed
	object LoginMessage {
		val scene by lazy { scene() }

		private fun scene(): Scene {

			val gridPane = grid()

			// The SUCCESS option will never be initialized, but Kotlin enforces exhaustion of all enums
			val message = when (status) {
				LoginStatus.SUCCESS -> Label("Login successful")
				LoginStatus.INCORRECT -> Label("Username or password incorrect")
				LoginStatus.CONNECTION_FAILED -> Label("A connection could not be established to database")
			}

			//Left-aligned pane for message
			val leftPane = StackPane(message)
			leftPane.alignment = Pos.CENTER_LEFT

			//Ok Button
			val register = Button("OK")
			register.font = GUIFont.medium
			register.setOnAction { Logger.stage.close() }

			//Right-aligned pane for button
			val rightPane = StackPane(register)
			rightPane.alignment = Pos.CENTER_RIGHT

			gridPane.add(leftPane, 0, 0)
			gridPane.add(rightPane, 0, 1)

			return Scene(gridPane, 250.0, 100.0)
		}
	}
}
