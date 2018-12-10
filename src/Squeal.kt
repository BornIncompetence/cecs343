// We will be using the user's locally installed database for the purposes
// of this assignment. The user should already have a MYSQL database
// created with the name "scheduler"
const val SQL_URL = "jdbc:mysql://localhost:3306/"
const val SQLDatabase = "scheduler?useLegacyDatetimeCode=false&serverTimezone=UTC&useSSL=false"
const val SQLUsername = "java"
const val SQLPassword = "coffee"

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
fun createAppointment(name: String, startDate: String, endDate: String, userID: Int, appID: Int, reminder: Int?): String {
	return "INSERT INTO scheduler.appointments(appointment_id, user_id , title, start_date, end_date, reminder) " +
			"VALUES( '$appID', '$userID', '$name' , '$startDate', '$endDate', ${reminder ?: "NULL"});"
}

// Get all Appointments belonging to username
fun getAppointments(username: String): String {
	return "SELECT a.title, a.start_date, a.end_date, a.appointment_id, a.reminder " +
			"FROM Appointments a " +
			"INNER JOIN Users u ON a.user_id = u.user_id " +
			"WHERE username LIKE '$username';"
}

// Use the AppointmentID to check if an appointment with appID exists in database
fun checkForExistingAppt(appID: Int): String {
	return "SELECT COUNT(*) FROM Appointments WHERE appointment_id = $appID;"
}

// Change the title of the appointment using the appID
fun changeTitle(title: String, appID: Int): String {
	return "UPDATE Appointments SET title = '$title' WHERE appointment_id = $appID;"
}

// Change the start date of the appointment using the appID
fun changeStart(startDate: String, appID: Int): String {
	return "UPDATE Appointments SET start_date = '$startDate' WHERE appointment_id = $appID;"
}

// Change the end date of the appointment using the appID
fun changeEnd(endDate: String, appID: Int): String {
	return "UPDATE Appointments SET end_date = '$endDate' WHERE appointment_id = $appID;"
}

// Remove appointment from database using appID
fun removeAppointment(appID: Int): String {
	return "DELETE FROM Appointments WHERE appointment_id = $appID;"
}

//Sends email to address
//Returns true for email sent
//Returns false for email not sent
fun sendEmail(emailAddress: String, appointmentName: String, startDate: String, username: String): Boolean{

	val emailSent = emailAddress == ""//Set to true if email is sent
	val emailMSG = " Hello, $username \nYou have a/an $appointmentName @ $startDate "//Message String
	println(emailMSG)

	//TODO actually send message emailMSG and update emailSent



	return emailSent
}

// Change the reminder for this appointment
fun changeReminder(reminder: Int?, appID: Int) : String {
	return "UPDATE Appointments SET reminder = ${reminder ?: "NULL"} WHERE appointment_id = $appID;"
}