import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.PasswordField
import javafx.scene.layout.GridPane
import javafx.scene.layout.HBox
import javafx.scene.layout.StackPane
import javafx.scene.layout.VBox
import javafx.stage.Modality
import javafx.stage.Stage

object PasswordChanger {
	private val stage = Stage()

	lateinit var status: PasswordStatus
	enum class PasswordStatus {
		INCORRECT, NO_MATCH
	}

	val scene by lazy {	scene()	}

	private fun scene(): Scene {

		stage.initModality(Modality.APPLICATION_MODAL)

		val gridPane = GridPane()
		gridPane.alignment = Pos.CENTER
		gridPane.hgap = 10.0
		gridPane.vgap = 10.0
		gridPane.padding = Insets(25.0, 25.0, 25.0, 25.0)

		val password = PasswordField()
		password.promptText = "Old Password"
		password.font = GUIFont.regular

		val newPassword = PasswordField()
		newPassword.promptText = "New Password"
		newPassword.font = GUIFont.regular

		val retypeNewPassword = PasswordField()
		retypeNewPassword.promptText = "Retype New Password"
		retypeNewPassword.font = GUIFont.regular

		val vbox = VBox(10.0)
		vbox.children.addAll(password, newPassword, retypeNewPassword)
		gridPane.add(vbox, 0, 0)

		val register = Button("Change Password")
		register.font = GUIFont.medium
		register.setOnAction {
			_ ->
			val correctPasswordStatement = connection.createStatement()
			val successStatement = connection.createStatement()

			val result = correctPasswordStatement.executeQuery(getMatchingRow(account.username, password.text))
			when {
				newPassword.text != retypeNewPassword.text -> {
					status = PasswordStatus.NO_MATCH
					stage.scene = BadPasswordMessage.scene
					stage.showAndWait()
				}
				account.username == "guest" -> {
					status = PasswordStatus.INCORRECT
					stage.scene = BadPasswordMessage.scene
					stage.showAndWait()
				}
				result.next() -> {
					successStatement.executeUpdate(changePassword(account.username, newPassword.text))
					account.password = newPassword.text
					Welcome.stage.close()
				}
				else -> {
					status = PasswordStatus.INCORRECT
					stage.scene = BadPasswordMessage.scene
					stage.showAndWait()
				}
			}
		}

		val back = Button("Cancel")
		back.font = GUIFont.medium
		back.setOnAction { _ -> Welcome.stage.close() }

		val hbox = HBox(10.0)
		hbox.children.addAll(register, back)
		gridPane.add(hbox, 0, 2)

		return Scene(gridPane, 250.0, 225.0)
	}

	// Window shown when Login has failed
	object BadPasswordMessage {
		val scene by lazy { scene() }

		private fun scene(): Scene {

			val gridPane = GridPane()
			gridPane.alignment = Pos.CENTER
			gridPane.hgap = 10.0
			gridPane.vgap = 10.0
			gridPane.padding = Insets(25.0, 25.0, 25.0, 25.0)

			// The SUCCESS option will never be initialized, but Kotlin enforces exhaustion of all enums
			val message = when (status) {
				PasswordStatus.INCORRECT -> Label("Incorrect password for old account")
				PasswordStatus.NO_MATCH -> Label("New passwords don't match")
			}
			val leftPane = StackPane(message)
			leftPane.alignment = Pos.CENTER_LEFT

			val register = Button("OK")
			register.font = GUIFont.medium
			register.setOnAction { _ ->	PasswordChanger.stage.close() }
			val rightPane = StackPane(register)
			rightPane.alignment = Pos.CENTER_RIGHT

			gridPane.add(leftPane, 0, 0)
			gridPane.add(rightPane, 0, 1)

			return Scene(gridPane, 250.0, 100.0)
		}
	}
}