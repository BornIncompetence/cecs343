import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.control.Menu
import javafx.scene.control.MenuBar
import javafx.scene.control.MenuItem
import javafx.scene.layout.GridPane
import javafx.scene.layout.VBox
import javafx.scene.text.Text
import javafx.stage.Modality
import javafx.stage.Stage

// Main menu
object Welcome {
	data class User(val email: String?, val username: String, val password: String) {}
	lateinit var user: User
	val stage = Stage()

	// Specifies different types of stages to appear on top of main window
	private enum class StageType {
		CREATE_ACCOUNT, CHANGE_USERNAME, CHANGE_PASSWORD, MODIFY_ACCOUNT, SET_CALENDAR_TYPE
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

		val setCalendarType = MenuItem("Set calendar type")
		setCalendarType.setOnAction {
			_ ->
			// Change how calendar view is organized get either Week, Month, or Day
			changeStage(StageType.SET_CALENDAR_TYPE)
		}
		menuSettings.items.addAll(setCalendarType)

		menuBar.menus.addAll(menuAccount, menuAppointment, menuSettings, menuHelp)
		vbox.children.add(menuBar)

		// TODO: Create a calendar view in place of this, each entry in the calendar has a username
		val welcomeText = Text("Logged in as " + user.email)
		welcomeText.font = GUIFont.regularItalic
		gridPane.add(welcomeText, 0, 1)

		vbox.children.add(gridPane)

		return Scene(vbox, 300.0, 200.0)
	}

	private fun changeStage(stageType: StageType) {
		stage.scene = when (stageType) {
			Welcome.StageType.CREATE_ACCOUNT -> Registrar.scene
			Welcome.StageType.CHANGE_USERNAME -> TODO()
			Welcome.StageType.CHANGE_PASSWORD -> PasswordChanger.scene
			Welcome.StageType.MODIFY_ACCOUNT -> TODO()
			Welcome.StageType.SET_CALENDAR_TYPE -> TODO()
		}
	}
}