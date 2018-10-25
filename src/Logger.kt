import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.PasswordField
import javafx.scene.control.TextField
import javafx.scene.layout.GridPane
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
			// Check if username OR email address matches with password
			if (login(username.text, password.text)) {
				window.scene = Welcome.scene
			} else {
				stage.scene = LoginMessage.scene
				stage.showAndWait()
			}
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

	// Returns true if the user was able to login
	// The system needs to be able to login to the database
	// And find the matching user stored in the database
	private fun login(name: String, pass: String): Boolean {
		// Check connection
		try {
			val connection = DriverManager.getConnection(SQL.url + SQL.database, SQL.username, SQL.password)
			Welcome.connection = connection
		} catch (e: SQLException) {
			status = LoginStatus.CONNECTION_FAILED
			return false
		}

		val statement = Welcome.connection.createStatement()
		val result = statement.executeQuery(SQL.getMatchingRow(name, pass))
		return if (result.next()) {
			val email = result.getString("email")
			val username = result.getString("username")
			val password = result.getString("password")
			val phone = result.getString("phone")
			Welcome.account = Account(email, username, password, phone)
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

			val gridPane = GridPane()
			gridPane.alignment = Pos.CENTER
			gridPane.hgap = 10.0
			gridPane.vgap = 10.0
			gridPane.padding = Insets(25.0, 25.0, 25.0, 25.0)

			// The SUCCESS option will never be initialized, but Kotlin enforces exhaustion of all enums
			val message = when (status) {
				LoginStatus.SUCCESS -> Label("Login successful")
				LoginStatus.INCORRECT -> Label("Username or password incorrect")
				LoginStatus.CONNECTION_FAILED -> Label("A connection could not be established to database")
			}
			val leftPane = StackPane(message)
			leftPane.alignment = Pos.CENTER_LEFT

			val register = Button("OK")
			register.font = GUIFont.medium
			register.setOnAction { _ ->	Logger.stage.close() }
			val rightPane = StackPane(register)
			rightPane.alignment = Pos.CENTER_RIGHT

			gridPane.add(leftPane, 0, 0)
			gridPane.add(rightPane, 0, 1)

			return Scene(gridPane, 225.0, 100.0)
		}
	}
}
