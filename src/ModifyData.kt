import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox

// Modify data such as email address and phone
object ModifyData {
    val scene by lazy {	scene()	}

    private fun scene(): Scene {

        val gridPane = grid()

        val text = Label("Modify Account")
        text.font = GUIFont.heavy
        gridPane.add(text, 0, 0)

        val email = TextField()
        email.promptText = "New User Email"
        email.font = GUIFont.regular

        val phoneNumber = TextField()
        phoneNumber.promptText = "Phone Number"
        phoneNumber.font = GUIFont.regular

        val vBox = VBox(10.0)
        vBox.children.addAll(email, phoneNumber)
        gridPane.add(vBox, 0, 1)

        val register = Button("Update")
        register.font = GUIFont.medium
        register.setOnAction {
            val emailStatement = connection.createStatement()
            val successEmailStatement = connection.createStatement()
            val successPhoneStatement = connection.createStatement()

            val emailExistsResult = emailStatement.executeQuery(checkForExistingEmail(email.text))
            emailExistsResult.next()
            val emailCount = emailExistsResult.getInt(1)

            // No collisions with other email addresses exist
            if (emailCount == 0 && email.text != "") {
                successEmailStatement.executeUpdate(changeEmail(account.username, email.text))
                account.email = email.text

            }
            // The same phone number can apply to other users however
            if (phoneNumber.text != "") {
                successPhoneStatement.executeUpdate(changePhoneNumber(account.username, phoneNumber.text))
                account.phone = phoneNumber.text
            }
            Welcome.stage.close()
        }

        val back = Button("Go back")
        back.font = GUIFont.medium
        back.setOnAction { Welcome.stage.close() }

        val hBox = HBox(10.0)
        hBox.children.addAll(register, back)
        gridPane.add(hBox, 0, 2)

        return Scene(gridPane, 250.0, 225.0)
    }
}
