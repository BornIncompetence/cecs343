import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.PasswordField
import javafx.scene.control.TextField
import javafx.scene.layout.GridPane
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox

// Create User Account
object ChangeName {
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
            //TODO insert functionallity
            println("Username Updated")
            Welcome.stage.close()
        }

        val back = Button("Go back")
        back.font = GUIFont.medium
        back.setOnAction { _ -> Welcome.stage.close() }

        val hbox = HBox(10.0)
        hbox.children.addAll(register, back)
        gridPane.add(hbox, 0, 2)

        return Scene(gridPane, 250.0, 225.0)
    }
}
