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
import javafx.scene.control.MenuButton
import javafx.scene.control.MenuItem

// Create User Account
@Deprecated("This has been easily implemented using a radio menu system in Welcome.kt")
object UpdateCal {
    val scene by lazy {	scene()	}

    private fun scene(): Scene {


        print("aaa");
        val gridPane = GridPane()
        gridPane.alignment = Pos.CENTER
        gridPane.hgap = 10.0
        gridPane.vgap = 10.0
        gridPane.padding = Insets(25.0, 25.0, 25.0, 25.0)

        val text = Label("Update Calendar")
        text.font = GUIFont.heavy
        gridPane.add(text, 0, 0)

        val menuItemMonth =   MenuItem("Month");
        val menuItemWeek =   MenuItem("Week");
        val menuItemDay =   MenuItem("Day");

        val menu = MenuButton("Choose Option" ,null, menuItemDay,menuItemMonth,menuItemWeek )
        menu.font = GUIFont.regular

        val vbox = VBox(10.0)
        vbox.children.addAll(menu)
        gridPane.add(vbox, 0, 1)

        val register = Button("Change")
        register.font = GUIFont.medium
        register.setOnAction {
            _ ->

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
