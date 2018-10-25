import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.layout.GridPane
import javafx.scene.layout.VBox
import javafx.scene.text.Text
import javafx.stage.Modality
import javafx.stage.Stage
import java.sql.Connection

// Main menu
object Welcome {
	//data class User(val email: String?, val username: String, val password: String) {}
	lateinit var connection: Connection
	lateinit var account: Account

	var welcomeBanner = Text()
	val stage = Stage()

	// Specifies different types of stages to appear on top of main window
	private enum class StageType {
		CREATE_ACCOUNT, CHANGE_USERNAME, CHANGE_PASSWORD, MODIFY_ACCOUNT, MAKE_APPOINTMENT, CANCEL_APPOINTMENT, CHANGE_APPOINTMENT, SET_COLOR
	}

	enum class CalendarType {
		WEEK, MONTH, DAY
	}

	val scene by lazy {	scene()	}

	private fun scene(): Scene {

		stage.initModality(Modality.APPLICATION_MODAL)

		val vbox = VBox(10.0)
		vbox.alignment = Pos.TOP_LEFT

		val gridPane = GridPane()
		gridPane.alignment = Pos.CENTER
		gridPane.hgap = 10.0
		gridPane.vgap = 10.0
		gridPane.padding = Insets(25.0, 25.0, 25.0, 25.0)

		val menuBar = MenuBar()
		val menuAccount = Menu("Account")
		val menuAppointment = Menu("Appointment")
		val menuSettings = Menu("Settings")
		val menuHelp = Menu("Help")

		val createAccount = MenuItem("Create account")
		createAccount.setOnAction {
			_ ->
			changeStage(StageType.CREATE_ACCOUNT)
			stage.showAndWait()
		}

		val changeUsername = MenuItem("Change user name")
		changeUsername.setOnAction {
			_ ->
			changeStage(StageType.CHANGE_USERNAME)
			stage.showAndWait()
		}

		val changePassword = MenuItem("Change password")
		changePassword.setOnAction {
			_ ->
			// Change password associated with username
			changeStage(StageType.CHANGE_PASSWORD)
			stage.showAndWait()
		}

		val modifyAccount = MenuItem("Modify account")
		modifyAccount.setOnAction {
			_ ->
			// What kind of settings would we change?
			changeStage(StageType.MODIFY_ACCOUNT)
			stage.showAndWait()
		}

		menuAccount.items.addAll(createAccount, changeUsername, changePassword, modifyAccount)

		val setCalendarType = Menu("Set calendar type")
		val week = RadioMenuItem("Week")
		val month = RadioMenuItem("Month")
		val day = RadioMenuItem("Day")
		week.isSelected = true
		setCalendarType.items.addAll(week, month, day)
		menuSettings.items.addAll(setCalendarType)

		val toggleGroup = ToggleGroup()
		toggleGroup.toggles.addAll(week, month, day)

		menuBar.menus.addAll(menuAccount, menuAppointment, menuSettings, menuHelp)
		vbox.children.add(menuBar)

		// TODO: Create a calendar view in place of this, each entry in the calendar has a username
		// NOT NEEDED YET
		// TODO: Update text when a user creates a new account(switches account)
		welcomeBanner.text = "Logged in as " + account.username
		welcomeBanner.font = GUIFont.regularItalic
		gridPane.add(welcomeBanner, 0, 1)

		vbox.children.add(gridPane)

		return Scene(vbox, 300.0, 200.0)
	}

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
