enum class Squeal {
	CREATE_ACCOUNT, CHANGE_USERNAME, CHANGE_PASSWORD, MODIFY_ACCOUNT
}


object SQL {
	lateinit var old_username: String
	lateinit var new_username: String
	lateinit var new_password: String
	lateinit var old_password: String
	lateinit var email: String

	val create_account by lazy {
		"SELECT * FROM Users\n" +
		"WHERE username = '$old_username' OR email = '$email';"
	}

	val change_user_password by lazy {
		"UPDATE Users\n" +
		"SET username = '$new_username', password = '$new_password'\n" +
		"WHERE username = '$old_username';"
	}

	val change_password by lazy {
		"UPDATE Users\n" +
		"SET password = '$new_password'\n" +
		"WHERE password = '$old_password';"
	}


	val change_username by lazy {
		"UPDATE Users\n" +
		"SET username = '$new_username'\n" +
		"WHERE username = '$old_username';".format(new_username, old_username)
	}
}

