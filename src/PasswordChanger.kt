import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.PasswordField
import javafx.scene.layout.HBox
import javafx.scene.layout.StackPane
import javafx.scene.layout.VBox
import javafx.stage.Modality
import javafx.stage.Stage

// Change password of account
object PasswordChanger {
	// Stage that appears when user cannot change password
	private val stage = Stage()

	lateinit var status: PasswordStatus
	enum class PasswordStatus {
		INCORRECT, NO_MATCH
	}

	val scene by lazy {	scene()	}

	private fun scene(): Scene {

		stage.initModality(Modality.APPLICATION_MODAL)

		val gridPane = grid()

		val password = PasswordField()
		password.promptText = "Old Password"
		password.font = GUIFont.regular

		val newPassword = PasswordField()
		newPassword.promptText = "New Password"
		newPassword.font = GUIFont.regular

		val retypeNewPassword = PasswordField()
		retypeNewPassword.promptText = "Retype New Password"
		retypeNewPassword.font = GUIFont.regular

		val vBox = VBox(10.0)
		vBox.children.addAll(password, newPassword, retypeNewPassword)
		gridPane.add(vBox, 0, 0)

		val register = Button("Change Password")
		register.font = GUIFont.medium
		register.setOnAction {
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
		back.setOnAction { Welcome.stage.close() }

		val hBox = HBox(10.0)
		hBox.children.addAll(register, back)
		gridPane.add(hBox, 0, 2)

		return Scene(gridPane, 250.0, 225.0)
	}

	// Window shown when Login has failed
	object BadPasswordMessage {
		val scene by lazy { scene() }

		private fun scene(): Scene {

			val gridPane = grid()

			// The SUCCESS option will never be initialized, but Kotlin enforces exhaustion of all enums
			val message = when (status) {
				PasswordStatus.INCORRECT -> Label("Incorrect password for old account")
				PasswordStatus.NO_MATCH -> Label("New passwords don't match")
			}
			val leftPane = StackPane(message)
			leftPane.alignment = Pos.CENTER_LEFT

			val register = Button("OK")
			register.font = GUIFont.medium
			register.setOnAction { PasswordChanger.stage.close() }
			val rightPane = StackPane(register)
			rightPane.alignment = Pos.CENTER_RIGHT

			gridPane.add(leftPane, 0, 0)
			gridPane.add(rightPane, 0, 1)

			return Scene(gridPane, 250.0, 100.0)
		}
	}
}