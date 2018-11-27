import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.layout.HBox
import javafx.scene.layout.StackPane
import javafx.scene.layout.VBox
import javafx.stage.Stage
import java.util.Random

// Change username of account
object MakeAppointment {
    // Stage that appears when user cannot change name
    private val stage = Stage()

    val scene by lazy {	scene()	}

    private fun scene(): Scene {

        val gridPane = grid()

        //Change Username label
        val text = Label("Create a New Appointment")
        text.font = GUIFont.heavy
        gridPane.add(text, 0, 0)

        //Appointment Name Field
        val aptName = TextField()
        aptName.promptText = "Appointment Name"
        aptName.font = GUIFont.regular

        val startPrompt = Label("Start Date")
        startPrompt.font = GUIFont.regular

        //Start Date
        val startDate = TextField()
        startDate.promptText = "YYYY-MM-DD HH:MM:SS"
        startDate.font = GUIFont.regular

        val endPrompt = Label("End Date")
        text.font = GUIFont.heavy

        //End Date
        val endDate = TextField()
        endDate.promptText = "YYYY-MM-DD HH:MM:SS"
        endDate.font = GUIFont.regular

        //Add to vBox
        val vBox = VBox(10.0)
        vBox.children.addAll(aptName)
        vBox.children.addAll(startPrompt)
        vBox.children.addAll(startDate)
        vBox.children.addAll(endPrompt)
        vBox.children.addAll(endDate)
        gridPane.add(vBox, 0, 1)

        //Update button
        val register = Button("Create")
        register.font = GUIFont.medium

        //Update button onClick action
        register.setOnAction {

            val createAppointmentStatement = connection.createStatement()

            //Appointment ID = aptName hashcode + account.id + random number to avoid any conflicts
            val apptID = aptName.text.hashCode() + (account.id + Random().nextInt(100))

            //Atempt to push new appt to DB. If error then that means this is a duplicate
            try{
                val result = createAppointmentStatement.executeUpdate(createAppointment(aptName.text, startDate.text, endDate.text, account.id,  apptID))
                //Welcome.stage.close();
            }catch (ex:Exception){
                stage.scene = AppointmentNameTaken.scene
                stage.showAndWait()
            }

        }

        //Back button
        val back = Button("Go back")
        back.font = GUIFont.medium
        back.setOnAction { Welcome.stage.close() }

        val hBox = HBox(10.0)
        hBox.children.addAll(register, back)
        gridPane.add(hBox, 0, 2)

        return Scene(gridPane, 250.0, 150.0)
    }

    // Window shown when changing Appointment Creation has failed
    object AppointmentNameTaken {
        val scene by lazy { scene() }

        private fun scene(): Scene {

            val gridPane = grid()

            // The SUCCESS option will never be initialized, but Kotlin enforces exhaustion of all enums
            val message = Label("You can not have duplicate tasks")
            val leftPane = StackPane(message)
            leftPane.alignment = Pos.CENTER_LEFT

            //Ok Button
            val register = Button("OK")
            register.font = GUIFont.medium
            register.setOnAction { MakeAppointment.stage.close() }

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
