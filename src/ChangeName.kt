import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.PasswordField
import javafx.scene.control.TextField
import javafx.scene.layout.GridPane
import javafx.scene.layout.HBox
import javafx.scene.layout.StackPane
import javafx.scene.layout.VBox
import javafx.stage.Stage
import java.sql.DriverManager
import java.sql.SQLException

// Change username of account
object ChangeName {
    // Stage that appears when user cannot change name
    private val stage = Stage()

    val scene by lazy {	scene()	}

    private fun scene(): Scene {

        val gridPane = GridPane()
        gridPane.alignment = Pos.CENTER
        gridPane.hgap = 10.0
        gridPane.vgap = 10.0
        gridPane.padding = Insets(25.0, 25.0, 25.0, 25.0)

        val text = Label("Change Username")
        text.font = GUIFont.heavy
        gridPane.add(text, 0, 0)

        val username = TextField()
        username.promptText = "Username"
        username.font = GUIFont.regular

        val vbox = VBox(10.0)
        vbox.children.addAll(username)
        gridPane.add(vbox, 0, 1)

        val register = Button("Update")
        register.font = GUIFont.medium
        register.setOnAction {
            _ ->
            val usernameStatement = Welcome.connection.createStatement()
            val successStatement = Welcome.connection.createStatement()

            val usernameTakenResult = usernameStatement.executeQuery(SQL.checkForExistingUsername(username.text))
            usernameTakenResult.next()
            val usernameCount = usernameTakenResult.getInt(1)

            // Makes sure username doesn't already exist
            if (usernameCount > 0 || Welcome.account.username == "guest") {
                stage.scene = NameSwapMessage.scene
                stage.showAndWait()
            } else {
                successStatement.executeUpdate(SQL.changeUsername(Welcome.account.username, username.text))
                Welcome.account.username = username.text
                Welcome.welcomeBanner.text = username.text
                Welcome.stage.close()
            }
        }

        val back = Button("Go back")
        back.font = GUIFont.medium
        back.setOnAction { _ -> Welcome.stage.close() }

        val hbox = HBox(10.0)
        hbox.children.addAll(register, back)
        gridPane.add(hbox, 0, 2)

        return Scene(gridPane, 250.0, 225.0)
    }

    // Window shown when changing username has failed
    object NameSwapMessage {
        val scene by lazy { scene() }

        private fun scene(): Scene {

            val gridPane = GridPane()
            gridPane.alignment = Pos.CENTER
            gridPane.hgap = 10.0
            gridPane.vgap = 10.0
            gridPane.padding = Insets(25.0, 25.0, 25.0, 25.0)

            // The SUCCESS option will never be initialized, but Kotlin enforces exhaustion of all enums
            val message = Label("Username taken")
            val leftPane = StackPane(message)
            leftPane.alignment = Pos.CENTER_LEFT

            val register = Button("OK")
            register.font = GUIFont.medium
            register.setOnAction { _ ->	ChangeName.stage.close() }
            val rightPane = StackPane(register)
            rightPane.alignment = Pos.CENTER_RIGHT

            gridPane.add(leftPane, 0, 0)
            gridPane.add(rightPane, 0, 1)

            return Scene(gridPane, 225.0, 100.0)
        }
    }
}
