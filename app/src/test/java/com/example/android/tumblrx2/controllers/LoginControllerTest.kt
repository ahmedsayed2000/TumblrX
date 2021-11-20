package com.example.android.tumblrx2.controllers

import com.google.common.truth.Truth.assertThat

import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class LoginControllerTest {
    
    @Test
    fun correctEmailAndPassword() {
        val result: String = LoginController.onLogin("admin@admin", "admin")
        assertThat(result).isEqualTo("Success")
    }

    @Test
    fun incorrectEmailAndPassword() {
        val result: String = LoginController.onLogin("admin@admin", "a")
        assertThat(result).isEqualTo("Email and Password do not match")
    }

    @Test
    fun incorrectEmailFormat() {
        val result: String = LoginController.onLogin("admin", "admin")
        assertThat(result).isEqualTo("Please enter a valid email")
    }

    @Test
    fun missingEmailAndCorrectPassword() {
        val result: String = LoginController.onLogin("", "admin")
        assertThat(result).isEqualTo("Please enter an email")
    }

    @Test
    fun correctEmailAndMissingPassword() {
        val result: String = LoginController.onLogin("admin@admin", "")
        assertThat(result).isEqualTo("Please enter a password")
    }

}