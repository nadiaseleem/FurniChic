package com.example.furnichic.util

import android.util.Patterns

 fun validateLastName(lastName:String): String? {
    if (lastName.isEmpty())
        return "Required"
    return null
}

 fun validateFistName(firstName:String): String? {
    if (firstName.isEmpty())
        return "Required"
    return null
}

fun validateEmail(email:String): String? {

    if (email.isEmpty())
        return "Required"
    if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        return "Invalid Email Address"

    return null
}

 fun validatePassword(password:String): String? {

    if (password.isEmpty())
        return "Required"
    if (password.length < 8)
        return "Minimum 8 characters password"
    if (!password.matches(".*[A-Z].*".toRegex()))
        return "Must Contain 1 Upper-cae Character"
    if (!password.matches(".*[a-z].*".toRegex()))
        return "Must Contain 1 Lower-cae Character"
    if(!password.matches(".*[@#\$%^&+=].*".toRegex()))
        return "Must Contain 1 Special Character (@#\$%^&+=)"
    if(!password.matches(".*[0-9].*".toRegex()))
        return "Must Contain 1 number"


    return null
}
