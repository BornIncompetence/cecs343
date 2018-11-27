import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.layout.StackPane
import javafx.stage.Stage
import java.io.File
import java.sql.SQLException
import java.util.Random

object FileExplorer {
	val importStage = Stage()
	val exportStage = Stage()

	val scene by lazy { scene() }

	private fun scene(): Scene {

		val gridPane = grid()

		//TODO: Get the number of insertions and modifications to the database
		//Align message to the left
		val message = Label("99 entries inserted, 99 entries modified")
		val leftPane = StackPane(message)
		leftPane.alignment = Pos.CENTER_LEFT

		//Ok Button
		val register = Button("OK")
		register.font = GUIFont.medium
		register.setOnAction { Welcome.stage.close() }

		//Align Ok button to the right
		val rightPane = StackPane(register)
		rightPane.alignment = Pos.CENTER_RIGHT

		//Add to grid pane
		gridPane.add(leftPane, 0, 0)
		gridPane.add(rightPane, 0, 1)

		return Scene(gridPane, 250.0, 100.0)
	}

	fun overwrite(appointments: MutableList<Appointment>) {
		appointments.forEach {
			val idStatement = connection.createStatement()
			val idResult = idStatement.executeQuery(checkForExistingAppt(it.id))

			// Modify if the ID already exists, else create a new one
			if (idResult.next()) {
				val titleStatement = connection.createStatement()
				val startStatement = connection.createStatement()
				val endStatement = connection.createStatement()

				titleStatement.executeUpdate(changeTitle(it.title, it.id))
				startStatement.executeUpdate(changeStart(it.startDate, it.id))
				endStatement.executeUpdate(changeEnd(it.endDate, it.id))
			} else {
				val creationStatement = connection.createStatement()
				val aptID = it.title.hashCode() + (account.id + Random().nextInt(100))
				creationStatement.executeUpdate(createAppointment(it.title, it.startDate, it.endDate, account.id, aptID))
			}
		}
	}

	fun save(file: File) {
		try {
			val successGetAptStatement = connection.createStatement()
			val aptResult = successGetAptStatement.executeQuery(getAppointments(account.username))
			file.printWriter().use { out ->
				while (aptResult.next()) {
					val title = aptResult.getString("title")
					val start = aptResult.getString("start_date")
					val end = aptResult.getString("end_date")
					val id = aptResult.getInt("appointment_id")

					out.println("$title,$start,$end,$id")
				}
			}
		} catch (e: SQLException) {
			e.printStackTrace()
		}
	}

	fun open(file: File): MutableList<Appointment> {
		val appointments = mutableListOf<Appointment>()

		file.forEachLine { line ->
			val tokens = line.split(",")
			appointments.add(Appointment(tokens[0], tokens[1], tokens[2], tokens[3].toInt()))
		}

		return appointments
	}
}