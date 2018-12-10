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
    // ComboBox that holds appointments
    private val selectApp = ComboBox<String>()
    private val appointments = mutableListOf<Appointment>()

    // Stage that appears when user cannot change name
    private val stage = Stage()

    val scene by lazy {	scene()	}

    // The current aptID that we'll modify the database with
    private var aptID: Int = Int.MAX_VALUE

    private fun scene(): Scene {

        val gridPane = grid()

        //Change Username label
        val text = Label("Edit Appointment")
        text.font = GUIFont.heavy
        gridPane.add(text, 0, 0)

        val selectAppLabel = Label("Select Appointment to Edit:")
        text.font = GUIFont.heavy
        gridPane.add(selectAppLabel, 0, 0)

        //Dropdown menu
        updateComboBox()

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

        val endPrompt = Label("New End Date")
        text.font = GUIFont.regular

        //End Date
        val endDate = TextField()
        endDate.promptText = "YYYY-MM-DD HH:MM:SS"
        endDate.font = GUIFont.regular

        val remindPrompt = Label("Edit reminder")
        remindPrompt.font = GUIFont.regular

        val remind = ComboBox<String>()
        remind.promptText = "Remind me in X minutes"
        remind.items.setAll("Never", "15 minutes", "30 minutes", "1 hour", "2 hours", "4 hours", "1 day")

        selectApp.selectionModel.selectedItemProperty().addListener {
            _, _, newValue ->
            val apt = appointments.find {
                val foundID = newValue.extractID()
                it.id == foundID
            }
            if (apt != null) {
                aptID = apt.id
                newName.text = apt.title
                startDate.text = apt.startDate
                endDate.text = apt.endDate
                remind.value = when (apt.reminder) {
                    0 -> "Never"
                    15 -> "15 minutes"
                    30 -> "30 minutes"
                    60 -> "1 hour"
                    120 -> "2 hours"
                    240 -> "4 hours"
                    3600 -> "1 day"
                    else -> {
                        null
                    }
                }
            }
        }

        //Add to vBox
        val vBox = VBox(10.0)
        vBox.children.addAll(
                selectAppLabel,
                selectApp,
                newNamePrompt,
                newName,
                startPrompt,
                startDate,
                endPrompt,
                endDate,
                remindPrompt,
                remind
        )
        gridPane.add(vBox, 0, 1)

        //Update button
        val modify = Button("Change")
        modify.font = GUIFont.medium

        //Update button onClick action
        modify.setOnAction {
            val appointment = appointments.find { apt ->
                apt.id == aptID
            }

            if (appointment != null) {
                var success = true
                val titleStatement = connection.createStatement()
                val startStatement = connection.createStatement()
                val endStatement = connection.createStatement()
                val remindStatement = connection.createStatement()

                println(newName.text)
                println(startDate.text)
                println(endDate.text)

                // If any of the fields fail to update, just keep going. The first try-catch probably isn't needed
                try {
                    titleStatement.executeUpdate(changeTitle(newName.text, aptID))
                } catch (e: SQLException) {
                    success = false
                }
                try {
                    startStatement.executeUpdate(changeStart(startDate.text, aptID))
                } catch (e: SQLException) {
                    success = false
                }
                try {
                    endStatement.executeUpdate(changeEnd(endDate.text, aptID))
                } catch(e: SQLException) {
                    success = false
                }
                try {
                    val remindInt = when (remind.value) {
                        "Never" -> null
                        "15 minutes" -> 15
                        "30 minutes" -> 30
                        "1 hour" -> 60
                        "2 hours" -> 120
                        "4 hours" -> 240
                        "1 day" -> 3600
                        else -> {
                            null
                        }
                    }
                    remindStatement.executeUpdate(changeReminder(remindInt, aptID))
                } catch(e: SQLException) {
                    success = false
                }

                if (success) {
                    AppointmentModify.label.text = "Successfully updated all fields"
                } else {
                    AppointmentModify.label.text = "Failed to update at least one field"
                }

                updateComboBox()
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

        //Cancel appointment, depending on last appointment selected from dropdown
        val cancel = Button("Cancel Appointment")
        cancel.font = GUIFont.medium
        cancel.setOnAction {
            val appointment = appointments.find { apt ->
                apt.id == aptID
            }

            if (appointment != null) {
                val deleteStatement = connection.createStatement()

                try {
                    deleteStatement.executeUpdate(removeAppointment(aptID))
                    AppointmentModify.label.text = "Removed ${appointment.title}"
                    updateComboBox()
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

        return Scene(gridPane, 350.0, 325.0)
    }

    // Window shown verifying changes to Appointment
    private object AppointmentModify {
        // The message shown changes, depending on status of appointment editing
        var label = Label()

        val scene by lazy { scene() }

        private fun scene(): Scene {

            val gridPane = grid()

            //Align message left
            val leftPane = StackPane(label)
            leftPane.alignment = Pos.CENTER_LEFT

            //Ok Button
            val register = Button("OK")
            register.font = GUIFont.medium
            register.setOnAction { ChangeCancelAppointment.stage.close() }

            //Alignment Ok button to the right
            val rightPane = StackPane(register)
            rightPane.alignment = Pos.CENTER_RIGHT

            //Add to grid pane
            gridPane.add(leftPane, 0, 0)
            gridPane.add(rightPane, 0, 1)

            return Scene(gridPane, 300.0, 100.0)
        }
    }

    // Extracts the ID from the very end of the string
    private fun String.extractID(): Int {
        val idx = this.indexOfLast {
            !it.isDigit()
        }

        return this.removeRange(0, idx + 1).toInt()
    }

    // Update the list of appointments when an appointment is created, changed, or deleted
    fun updateComboBox() {
        try {
            appointments.clear()
            selectApp.items.clear()

            val successGetAptStatement = connection.createStatement()
            val aptResult = successGetAptStatement.executeQuery(getAppointments(account.username))

            while (aptResult.next()) {
                val title = aptResult.getString("title")
                val start = aptResult.getString("start_date")
                val end = aptResult.getString("end_date")
                val id = aptResult.getInt("appointment_id")
                val reminder = aptResult.getInt("reminder")

                appointments.add(Appointment(title, start, end, id, reminder))

                val listString = "NAME: $title DATES: ($start) - ($end) ID: $id"

                selectApp.items.add(listString)
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
