import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.layout.GridPane
import javafx.scene.layout.VBox
import javafx.scene.text.Text
import javafx.stage.Modality
import javafx.stage.Stage

// Main menu
object Welcome {


	var welcomeBanner = Text()
	//Get the stage var
	val stage = Stage()

	// Specifies different types of stages to appear on top of main window
	private enum class StageType {
		CREATE_ACCOUNT, CHANGE_USERNAME, CHANGE_PASSWORD, MODIFY_ACCOUNT, MAKE_APPOINTMENT, CANCEL_APPOINTMENT, CHANGE_APPOINTMENT, SET_COLOR
	}

	//Class to hold type of Calender
	enum class CalendarType {
		//Possible choices
		WEEK, MONTH, DAY
	}

	val scene by lazy {	scene()	}

	private fun scene(): Scene {

		stage.initModality(Modality.APPLICATION_MODAL)

		val vbox = VBox(10.0)
		vbox.alignment = Pos.TOP_LEFT

		//Create a grid pane to organize view
		val gridPane = GridPane()
		gridPane.alignment = Pos.CENTER
		gridPane.hgap = 10.0
		gridPane.vgap = 10.0
		gridPane.padding = Insets(25.0, 25.0, 25.0, 25.0)

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
			_ ->
			changeStage(StageType.CREATE_ACCOUNT)
			stage.showAndWait()
		}

		//Open ChangeUsername window on menu click
		val changeUsername = MenuItem("Change user name")
		changeUsername.setOnAction {
			_ ->
			changeStage(StageType.CHANGE_USERNAME)
			stage.showAndWait()
		}

		//Open ChangePassword window on menu click
		val changePassword = MenuItem("Change password")
		changePassword.setOnAction {
			_ ->
			changeStage(StageType.CHANGE_PASSWORD)
			stage.showAndWait()
		}

		//Open modifyAccount window on menu click
		val modifyAccount = MenuItem("Modify account")
		modifyAccount.setOnAction {
			_ ->
			changeStage(StageType.MODIFY_ACCOUNT)
			stage.showAndWait()
		}

		//Add all menue items to the menu
		menuAccount.items.addAll(createAccount, changeUsername, changePassword, modifyAccount)

		//Create the calendar type menu to the view
		val setCalendarType = Menu("Set calendar type")
		val week = RadioMenuItem("Week")
		val month = RadioMenuItem("Month")
		val day = RadioMenuItem("Day")

		//Start off by selecting the week calander view
		week.isSelected = true
		setCalendarType.items.addAll(week, month, day)
		menuSettings.items.addAll(setCalendarType)

		//Add the toggleview with all the options
		val toggleGroup = ToggleGroup()
		toggleGroup.toggles.addAll(week, month, day)

		//Add all menus to the menubar
		menuBar.menus.addAll(menuAccount, menuAppointment, menuSettings, menuHelp)

		//Add all menuBar to the view
		vbox.children.add(menuBar)

		// TODO: Create a calendar view in place of welcome banner, each entry in the calendar has a username
		// NOT NEEDED YET

		//Set welcome banner to username
		welcomeBanner.text = "Logged in as " + account.username
		welcomeBanner.font = GUIFont.regularItalic
		gridPane.add(welcomeBanner, 0, 1)

		//Add grid to vbox
		vbox.children.add(gridPane)

		return Scene(vbox, 300.0, 200.0)
	}

	//This function takes a stagetype and excutes its corresponing window
	private fun changeStage(stageType: StageType) {
		stage.scene = when (stageType) {
			Welcome.StageType.CREATE_ACCOUNT -> Registrar.scene
			Welcome.StageType.CHANGE_USERNAME -> ChangeName.scene
			Welcome.StageType.CHANGE_PASSWORD -> PasswordChanger.scene
			Welcome.StageType.MODIFY_ACCOUNT -> ModifyData.scene
			Welcome.StageType.MAKE_APPOINTMENT -> TODO() // NOT NEEDED YET
			Welcome.StageType.CANCEL_APPOINTMENT -> TODO() // NOT NEEDED YET
			Welcome.StageType.CHANGE_APPOINTMENT -> TODO() // NOT NEEDED YET
			Welcome.StageType.SET_COLOR -> TODO() // NOT NEEDED YET, Probably doesn't need to have its own stage
		}
	}
}
