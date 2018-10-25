import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.PasswordField
import javafx.scene.layout.GridPane
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox

object PasswordChanger {
	val scene by lazy {	scene()	}

	private fun scene(): Scene {

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
			// Find field with username
			//     - Connect to database
			//     - Check database for instances of username
			//         - If not found
			//         - Show warning that username doesn't exist
			//     - Check if old password matches password in database
			//         - If old password doesn't match
			//         - Show warning that old password doesn't match
			//     - Check if password matches retype password
			//         - If doesn't match
			//         - Display warning that passwords don't match
			// Else execute a query that edits the old password field with the new password
			Welcome.stage.close()
		}

		val back = Button("Cancel")
		back.font = GUIFont.medium
		back.setOnAction { _ -> Welcome.stage.close() }

		val hbox = HBox(10.0)
		hbox.children.addAll(register, back)
		gridPane.add(hbox, 0, 2)

		return Scene(gridPane, 250.0, 225.0)
	}
}