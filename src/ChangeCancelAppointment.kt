import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.ComboBox
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.layout.HBox
import javafx.scene.layout.StackPane
import javafx.scene.layout.VBox
import javafx.stage.Stage
import java.util.Random
import javax.swing.JList

// Change username of account
object ChangeCancelAppointment {
    // Stage that appears when user cannot change name
    private val stage = Stage()

    val scene by lazy {	scene()	}

    private fun scene(): Scene {

        val gridPane = grid()

        //Change Username label
        val text = Label("Edit Appointment")
        text.font = GUIFont.heavy
        gridPane.add(text, 0, 0)

        val selectAppLabel = Label("Select Appointment to Edit:")
        text.font = GUIFont.heavy
        gridPane.add(selectAppLabel, 0, 0)

        //Appointment Name Field
        val selectApp = ComboBox<String>();
        try{
            val successGetApptStatement = connection.createStatement()
            val apptResult = successGetApptStatement.executeQuery(getAppointments(account.username))

            while (apptResult.next()) {
                val title = apptResult.getString("title")
                val start = apptResult.getString("start_date")
                val end = apptResult.getString("end_date")
                val id = apptResult.getInt("appointment_id")

                val listString = "NAME: " + title +" DATES: (" + start + ") - (" + end +") ID: " +  id ;


                selectApp.items.add(listString)
            }

        }catch (e:Exception){

        }


        val newNamePrompt = Label("New Appointment Date")
        newNamePrompt.font = GUIFont.regular

        //Start Date
        val newName = TextField()
        newName.promptText = "New Name"
        newName.font = GUIFont.regular


        val startPrompt = Label("New Start Date")
        startPrompt.font = GUIFont.regular

        //Start Date
        val startDate = TextField()
        startDate.promptText = "YYYY-MM-DD HH:MM:SS"
        startDate.font = GUIFont.regular

        val endPrompt = Label("New  End Date")
        text.font = GUIFont.heavy

        //End Date
        val endDate = TextField()
        endDate.promptText = "YYYY-MM-DD HH:MM:SS"
        endDate.font = GUIFont.regular






        //Add to vBox
        val vBox = VBox(10.0)

        vBox.children.addAll(selectAppLabel)
        vBox.children.addAll(selectApp)

        vBox.children.addAll(newNamePrompt)
        vBox.children.addAll(newName)

        vBox.children.addAll(startPrompt)
        vBox.children.addAll(startDate)

        vBox.children.addAll(endPrompt)
        vBox.children.addAll(endDate)
        gridPane.add(vBox, 0, 1)

        //Update button
        val register = Button("Change")
        register.font = GUIFont.medium

        //Update button onClick action
        register.setOnAction {

            /*
            val createAppointmentStatement = connection.createStatement()

            //Appointment ID = aptName hashcode + account.id + random number to avoid any conflicts
            val apptID = aptName.text.hashCode() + (account.id + Random().nextInt(100))

            //Attempt to push new appt to DB. If error then that means this is a duplicate
            try{
                createAppointmentStatement.executeUpdate(createAppointment(aptName.text, startDate.text, endDate.text, account.id,  apptID))
                Welcome.stage.close();
            }catch (ex:Exception){
                stage.scene = AppointmentNameTaken.scene
                stage.showAndWait()
            }*/


            print(selectApp.items.lastIndex)


        }

        //Back button
        val back = Button("Go back")
        back.font = GUIFont.medium
        back.setOnAction { Welcome.stage.close() }

        val cancel = Button("Cancel Appointment")
        cancel.font = GUIFont.medium
        cancel.setOnAction { //Cancel Logic
            Welcome.stage.close()
        }

        val hBox = HBox(10.0)
        hBox.children.addAll(register, back,cancel )
        gridPane.add(hBox, 0, 2)

        return Scene(gridPane, 250.0, 150.0)
    }

    // Window shown when changing Appointment Creation has failed
    object AppointmentNameTaken {
        val scene by lazy { scene() }

        private fun scene(): Scene {

            val gridPane = grid()

            val message = Label("You can not have duplicate tasks")
            val leftPane = StackPane(message)
            leftPane.alignment = Pos.CENTER_LEFT

            //Ok Button
            val register = Button("OK")
            register.font = GUIFont.medium
            register.setOnAction { ChangeCancelAppointment.stage.close() }

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
