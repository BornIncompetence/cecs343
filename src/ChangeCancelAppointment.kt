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
import java.sql.SQLException

// Change username of account
object ChangeCancelAppointment {
    // Stage that appears when user cannot change name
    private val stage = Stage()

    val scene by lazy {	scene()	}

    // The current apptID that we'll modify the database with
    private var apptID: Int = Int.MAX_VALUE

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
        val selectApp = ComboBox<String>()
        val appointments = mutableListOf<Appointment>()
        try {
            val successGetApptStatement = connection.createStatement()
            val apptResult = successGetApptStatement.executeQuery(getAppointments(account.username))

            while (apptResult.next()) {
                val title = apptResult.getString("title")
                val start = apptResult.getString("start_date")
                val end = apptResult.getString("end_date")
                val id = apptResult.getInt("appointment_id")

                appointments.add(Appointment(title, start, end, id))

                val listString = "NAME: $title DATES: ($start) - ($end) ID: $id"

                selectApp.items.add(listString)
            }

        } catch (e: Exception) {
            e.printStackTrace()
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

        selectApp.selectionModel.selectedItemProperty().addListener { _, _, newValue ->
            val appt = appointments.find {
                val foundID = newValue.extractID()
                it.id == foundID
            }
            if (appt != null) {
                apptID = appt.id
                newName.text = appt.title
                startDate.text = appt.startDate
                endDate.text = appt.endDate
            }
        }

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
        val modify = Button("Change")
        modify.font = GUIFont.medium

        //TODO: Update ComboBox to reflect changes in database, and edit weird behavior with datetime
        //Update button onClick action
        modify.setOnAction {
            val appointment = appointments.find { appt ->
                appt.id == apptID
            }

            if (appointment != null) {
                var success = true
                val titleStatement = connection.createStatement()
                val startStatement = connection.createStatement()
                val endStatement = connection.createStatement()

                println(newName.text)
                println(startDate.text)
                println(endDate.text)

                // If any of the fields fail to update, just keep going. The first try-catch probably isn't needed
                try {
                    titleStatement.executeUpdate(changeTitle(newName.text, apptID))
                } catch (e: SQLException) {
                    success = false
                }
                try {
                    startStatement.executeUpdate(changeStart(startDate.text, apptID))
                } catch (e: SQLException) {
                    success = false
                }
                try {
                    endStatement.executeUpdate(changeEnd(endDate.text, apptID))
                } catch(e: SQLException) {
                    success = false
                }

                if (success) {
                    AppointmentModify.label.text = "Successfully updated all fields"
                } else {
                    AppointmentModify.label.text = "Failed to update at least one field"
                }

            } else {
                AppointmentModify.label.text = "Invalid ID used, please select an appointment from the list"
            }

            stage.scene = AppointmentModify.scene
            stage.showAndWait()
        }

        //Back button
        val back = Button("Go back")
        back.font = GUIFont.medium
        back.setOnAction { Welcome.stage.close() }

        val cancel = Button("Cancel Appointment")
        cancel.font = GUIFont.medium
        cancel.setOnAction {
            val appointment = appointments.find { appt ->
                appt.id == apptID
            }

            if (appointment != null) {
                val deleteStatement = connection.createStatement()

                try {
                    deleteStatement.executeUpdate(removeAppointment(apptID))
                    AppointmentModify.label.text = "Removed ${appointment.title}"
                } catch (e: SQLException) {
                    AppointmentModify.label.text = "Fatal error! Unable to remove appointment!"
                }
            } else {
                AppointmentModify.label.text = "Invalid ID used, please select an appointment from the list"
            }

            stage.scene = AppointmentModify.scene
            stage.showAndWait()
        }

        val hBox = HBox(10.0)
        hBox.children.addAll(modify, back,cancel )
        gridPane.add(hBox, 0, 2)

        return Scene(gridPane, 250.0, 150.0)
    }

    // Window shown when changing Appointment Creation has failed
    object AppointmentModify {
        var label = Label()

        val scene by lazy { scene() }

        private fun scene(): Scene {

            val gridPane = grid()

            val leftPane = StackPane(label)
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

private fun String.extractID(): Int {
    val idx = this.indexOfLast {
        !it.isDigit()
    }

    return this.removeRange(0, idx + 1).toInt()
}

