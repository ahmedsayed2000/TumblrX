package com.example.android.tumblrx2.login

data class User(val email: String, val password: String) {
//object User(val email: String, val password: String) {

    /**
     * Checks that the a user's email and password are entered correctly to be sent to the login controller.
     *
     * @param email the string entered in the email editText
     * @param password the password entered in the password editText
     * @return an integer that determines the string the controller will send back to the login activity
     */
    fun validateUser(email: String, password: String): Int {
        if (email.isEmpty()) return 1
//        if (EMAIL_ADDRESS.matcher(email).matches()) return 2
        if (email.length < 6) return 2
        if (password.isEmpty()) return 3
        return 0
    }
}