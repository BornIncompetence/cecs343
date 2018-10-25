import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.layout.GridPane
import javafx.scene.layout.HBox
import javafx.scene.layout.StackPane
import javafx.scene.layout.VBox
import javafx.stage.Stage

// Change username of account
object ChangeName {
    // Stage that appears when user cannot change name
    private val stage = Stage()

    val scene by lazy {	scene()	}

    private fun scene(): Scene {

        //GridPane for scene
        val gridPane = GridPane()
        gridPane.alignment = Pos.CENTER
        gridPane.hgap = 10.0
        gridPane.vgap = 10.0
        gridPane.padding = Insets(25.0, 25.0, 25.0, 25.0)

        //Change Username label
        val text = Label("Change Username")
        text.font = GUIFont.heavy
        gridPane.add(text, 0, 0)

        //Username field
        val username = TextField()
        username.promptText = "Username"
        username.font = GUIFont.regular

        //Add to vbox
        val vbox = VBox(10.0)
        vbox.children.addAll(username)
        gridPane.add(vbox, 0, 1)

        //Update button
        val register = Button("Update")
        register.font = GUIFont.medium

        //Update button onClick action
        register.setOnAction {
            _ ->
			// SQL Statements
            val usernameStatement = connection.createStatement()
            val successStatement = connection.createStatement()

			// Username is taken result
            val usernameTakenResult = usernameStatement.executeQuery(checkForExistingUsername(username.text))
            usernameTakenResult.next()
            val usernameCount = usernameTakenResult.getInt(1)

            // Makes sure username doesn't already exist
            if (usernameCount > 0 || account.username == "guest") {
                stage.scene = NameSwapMessage.scene
                stage.showAndWait()
            } else {
                successStatement.executeUpdate(changeUsername(account.username, username.text))
                account.username = username.text
                Welcome.welcomeBanner.text = "Logged in as " + username.text
                Welcome.stage.close()
            }
        }

        //Back button
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

            //Ok Button
            val register = Button("OK")
            register.font = GUIFont.medium
            register.setOnAction { _ ->	ChangeName.stage.close() }

            //Alignment var
            val rightPane = StackPane(register)
            rightPane.alignment = Pos.CENTER_RIGHT

            //Add to grid pane
            gridPane.add(leftPane, 0, 0)
            gridPane.add(rightPane, 0, 1)

            return Scene(gridPane, 250.0, 100.0)
        }
    }
}
