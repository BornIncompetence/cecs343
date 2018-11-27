import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.layout.VBox
import javafx.scene.text.Text
import javafx.stage.FileChooser
import javafx.stage.Modality
import javafx.stage.Stage

// Main menu
object Welcome {
	var welcomeBanner = Text()
	// Stage for many of the menu bar options available
	val stage = Stage()

	// Specifies different types of stages to appear on top of main window
	private enum class StageType {
		CREATE_ACCOUNT, CHANGE_USERNAME, CHANGE_PASSWORD, MODIFY_ACCOUNT, MAKE_APPOINTMENT, CANCEL_APPOINTMENT, CHANGE_APPOINTMENT, SET_COLOR, IMPORT_FILE
	}

	//Class to hold type of Calender
	enum class CalendarType {
		//Possible choices
		WEEK, MONTH, DAY
	}

	val scene by lazy {	scene()	}

	private fun scene(): Scene {

		stage.initModality(Modality.APPLICATION_MODAL)

		val gridPane = grid()

		//Create the menu bar
		val menuBar = MenuBar()
		val menuAccount = Menu("Account")
		val menuAppointment = Menu("Appointment")
		val menuSettings = Menu("Settings")
		val menuHelp = Menu("Help")

		//Add Create account action
		val createAccount = MenuItem("Create account")

		//Open CreateAccount window on menu click
		createAccount.setOnAction {
			changeStage(StageType.CREATE_ACCOUNT)
			stage.showAndWait()
		}

		//Open ChangeUsername window on menu click
		val changeUsername = MenuItem("Change user name")
		changeUsername.setOnAction {
			changeStage(StageType.CHANGE_USERNAME)
			stage.showAndWait()
		}

		//Open ChangePassword window on menu click
		val changePassword = MenuItem("Change password")
		changePassword.setOnAction {
			changeStage(StageType.CHANGE_PASSWORD)
			stage.showAndWait()
		}

		//Open modifyAccount window on menu click
		val modifyAccount = MenuItem("Modify account")
		modifyAccount.setOnAction {
			changeStage(StageType.MODIFY_ACCOUNT)
			stage.showAndWait()
		}

		//Add all menu items to the menu
		menuAccount.items.addAll(createAccount, changeUsername, changePassword, modifyAccount)

		//Open modifyAccount window on menu click
		val createAppt  = MenuItem("Create Appointment")
		createAppt.setOnAction {
			changeStage(StageType.MAKE_APPOINTMENT)
			stage.showAndWait()
		}
		menuAppointment.items.addAll(createAppt)

		val changeAppt  = MenuItem("Change / Cancel Appointment")
		changeAppt.setOnAction {
			changeStage(StageType.CHANGE_APPOINTMENT)
			stage.showAndWait()
		}
		menuAppointment.items.addAll(changeAppt)

		//Create the calendar type menu to the view
		val setCalendarType = Menu("Set calendar type")
		val week = RadioMenuItem("Week")
		val month = RadioMenuItem("Month")
		val day = RadioMenuItem("Day")

		//Start off by selecting the week calender view
		week.isSelected = true
		setCalendarType.items.addAll(week, month, day)
		menuSettings.items.addAll(setCalendarType)

		//Add the for calendar view type
		val toggleGroup = ToggleGroup()
		toggleGroup.toggles.addAll(week, month, day)

		//Add the import file setting
		val importer = MenuItem("Import schedule")
		importer.setOnAction {
			val fileChooser = FileChooser()
			fileChooser.extensionFilters.add(FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv"))

			val file = fileChooser.showOpenDialog(FileExplorer.importStage)

			val appointments = FileExplorer.open(file)
			FileExplorer.overwrite(appointments)

			changeStage(StageType.IMPORT_FILE)
			stage.showAndWait()
		}

		//Add the export file setting
		val exporter = MenuItem("Export schedule")
		exporter.setOnAction {
			val fileChooser = FileChooser()
			fileChooser.extensionFilters.add(FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv"))
			val file = fileChooser.showSaveDialog(FileExplorer.exportStage)
			FileExplorer.save(file)
		}

		//Add file import/export to the settings menu bar
		menuSettings.items.addAll(importer, exporter)

		//Add all menus to the menu bar
		menuBar.menus.addAll(menuAccount, menuAppointment, menuSettings, menuHelp)

		// TODO: Create a calendar view in place of welcome banner, each entry in the calendar has a username
		// NOT NEEDED YET

		//Set welcome banner to username
		welcomeBanner.text = "Logged in as " + account.username
		welcomeBanner.font = GUIFont.regularItalic
		gridPane.add(welcomeBanner, 0, 1)

		//Add grid to vBox
		val vBox = VBox(10.0)
		vBox.alignment = Pos.TOP_LEFT
		vBox.children.addAll(menuBar, gridPane)

		return Scene(vBox, 300.0, 200.0)
	}

	//This function takes a stage type and executes its corresponding window
	private fun changeStage(stageType: StageType) {
		stage.scene = when (stageType) {
			Welcome.StageType.CREATE_ACCOUNT -> Registrar.scene
			Welcome.StageType.CHANGE_USERNAME -> ChangeName.scene
			Welcome.StageType.CHANGE_PASSWORD -> PasswordChanger.scene
			Welcome.StageType.MODIFY_ACCOUNT -> ModifyData.scene
			Welcome.StageType.MAKE_APPOINTMENT ->  MakeAppointment.scene
			Welcome.StageType.IMPORT_FILE -> FileExplorer.scene
			Welcome.StageType.CANCEL_APPOINTMENT -> TODO()
			Welcome.StageType.CHANGE_APPOINTMENT -> ChangeCancelAppointment.scene
			Welcome.StageType.SET_COLOR -> TODO() // NOT NEEDED YET, Probably doesn't need to have its own stage
		}
	}
}
