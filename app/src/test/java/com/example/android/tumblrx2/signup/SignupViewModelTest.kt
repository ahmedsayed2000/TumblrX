package com.example.android.tumblrx2.signup


import com.example.android.tumblrx2.responses.LoginResponse
import com.example.android.tumblrx2.responses.RegisterResponse
import com.google.common.truth.Truth.assertThat
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class SignupViewModelTest {

    private lateinit var viewModel: SignupViewModel

    @Before
    fun setUp() {
        viewModel = SignupViewModel()
    }

    @Test
    fun validateInputEmptyEmail() {
        val result = viewModel.validateInput("","132456","gamal","21")
        assertThat(result).isEqualTo(-1)
    }

    @Test
    fun validateInputEmptyPassword() {
        val result = viewModel.validateInput("gamal@gmail.com","","gamal","21")
        assertThat(result).isEqualTo(-1)
    }

    @Test
    fun validateInputEmptyUsername() {
        val result = viewModel.validateInput("gamal@gmail.com","132456","","21")
        assertThat(result).isEqualTo(-1)
    }


    @Test
    fun validateInputEmptyAge() {
        val result = viewModel.validateInput("gamal@gmail.com","132456","gamal","")
        assertThat(result).isEqualTo(-1)
    }

    @Test
    fun validateInputInvalidEmail() {
        val result = viewModel.validateInput("gamal","132456","gamal","21")
        assertThat(result).isEqualTo(1)
    }

    @Test
    fun validateInputInvalidPassword() {
        val result = viewModel.validateInput("gamal@gmail.com","132","gamal","21")
        assertThat(result).isEqualTo(2)
    }

    @Test
    fun validateInputInvalidUsername() {
        val result = viewModel.validateInput("gamal@gmail.com","132456","gamal_","21")
        assertThat(result).isEqualTo(3)
    }

    @Test
        fun validateInputInvalidAge13() {
        val result = viewModel.validateInput("gamal@gmail.com","132456","gamal","12")
        assertThat(result).isEqualTo(4)
    }

    @Test
        fun validateInputInvalidAge130() {
        val result = viewModel.validateInput("gamal@gmail.com","132456","gamal","131")
        assertThat(result).isEqualTo(4)
    }

    @Test
    fun chooseErrMsgEmptyField() {
        val result = viewModel.chooseErrMsg(-1)
        assertThat(result).isEqualTo("Make sure all fields are filled")
    }

    @Test
    fun chooseErrMsgInvalidEmail() {
        val result = viewModel.chooseErrMsg(1)
        assertThat(result).isEqualTo("Enter a correct email")
    }

    @Test
    fun chooseErrMsgInvalidPassword() {
        val result = viewModel.chooseErrMsg(2)
        assertThat(result).isEqualTo("Password is too short")
    }

    @Test
    fun chooseErrMsgInvalidUsername() {
        val result = viewModel.chooseErrMsg(3)
        assertThat(result).isEqualTo("Username is invalid, make sure it is less than 32 characters, and only contains letter, numbers, or dashes (-)")
    }

    @Test
    fun chooseErrMsgInvalidAge() {
        val result = viewModel.chooseErrMsg(4)
        assertThat(result).isEqualTo("Enter a valid age between 13 and 130")
    }

    @Test
    fun testSignupSuccessResponse(){
        val response : Response<RegisterResponse> = Response.success(RegisterResponse("success","5","mockToken"))
        assertThat(response.isSuccessful).isTrue()
    }

    @Test
    fun testSignupFailureResponse() {
        val errorResponse =
            "{\n" +
                    "  \"type\": \"error\",\n" +
                    "  \"message\": \"Something went wrong.\"\n" + "}"
        val errorResponseBody = errorResponse.toResponseBody("application/json".toMediaTypeOrNull())
        val response : Response<RegisterResponse> = Response.error(400,errorResponseBody)
        assertThat(response.isSuccessful).isFalse()
    }
}