// We will be using the user's locally installed database for the purposes
// of this assignment. The user should already have a MYSQL database
// created with the name "scheduler"
const val SQL_URL = "jdbc:mysql://localhost:3306/"
const val SQLDatabase = "scheduler?useLegacyDatetimeCode=false&serverTimezone=UTC&useSSL=false"
const val SQLUsername = "root"
const val SQLPassword = "password"

// Query that gets the number of entries that match account's username
fun checkForExistingUsername(name: String): String {
	return "SELECT COUNT(*) FROM Users WHERE username = '$name';"
}

// Update query that replaces a user's email address
fun changeEmail(name: String, address: String): String {
	return "UPDATE Users SET email = '$address' WHERE username = '$name';"
}

// Update query that replaces a user's phone number
fun changePhoneNumber(name: String, number: String): String {
	return "UPDATE Users SET phone = '$number' WHERE username = '$name';"
}

// Query that gets the number of entries that match account's email
fun checkForExistingEmail(email: String): String {
	return "SELECT COUNT(*) FROM Users WHERE email = '$email';"
}

// Update query that inserts an account with the given ID number
fun createAccount(id: Int, account: Account): String {
	return "INSERT INTO scheduler.users (user_id, username, password, email, phone) VALUES(\n" +
			"$id, " +
			"'${account.username}', " +
			"'${account.password}', " +
			"'${account.email}', " +
			"${if (account.phone == null) "NULL" else account.phone}" +
			");"
}

// Query that gets the largest ID number for the purposes of insertion
fun getMaxID(): String {
	return "SELECT MAX(user_id) FROM Users;"
}

// Update query that changes a username
// Because the user is bound with a userID, this won't affect appointments later down the line
fun changeUsername(old: String, new: String): String {
	return "UPDATE Users SET username = '$new' WHERE username = '$old';"
}

// Update query that changes the password of a given user
fun changePassword(user: String, pass: String): String {
	return "UPDATE Users SET password = '$pass' WHERE username = '$user';"
}

// Get row that matches the username and password combo that's asked for
fun getMatchingRow(name: String, pass: String): String {
	return "SELECT * FROM Users WHERE username LIKE '$name' AND password = '$pass';"
}

// Create Appointment
fun createAppointment(name: String, startDate: String, endDate: String, userID: Int, appID: Int): String {
	return "INSERT INTO scheduler.appointments(appointment_id, user_id , title, start_date, end_date) VALUES( '$appID', '$userID', '$name' , '$startDate', '$endDate') "
}

// Get all Appointments belonging to username
fun getAppointments(username: String): String {
	return "SELECT a.title, a.start_date, a.end_date, a.appointment_id " +
			"FROM Appointments a " +
			"INNER JOIN Users u ON a.user_id = u.user_id " +
			"WHERE username LIKE '$username';"
}

fun checkForExistingAppt(appID: Int): String {
	return "SELECT COUNT(*) FROM Appointments WHERE appointment_id = $appID;"
}

fun changeTitle(title: String, appID: Int): String {
	return "UPDATE Appointments SET title = '$title' WHERE appointment_id = $appID;"
}

fun changeStart(startDate: String, appID: Int): String {
	return "UPDATE Appointments SET start_date = '$startDate' WHERE appointment_id = $appID;"
}

fun changeEnd(endDate: String, appID: Int): String {
	return "UPDATE Appointments SET end_date = '$endDate' WHERE appointment_id = $appID;"
}
