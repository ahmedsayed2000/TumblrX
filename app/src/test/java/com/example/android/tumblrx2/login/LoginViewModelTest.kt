package com.example.android.tumblrx2.login


import com.example.android.tumblrx2.responses.LoginError
import com.example.android.tumblrx2.responses.LoginResponse
import com.google.common.truth.Truth.assertThat
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import retrofit2.Response
import retrofit2.Retrofit

class LoginViewModelTest {

    private lateinit var viewModel: LoginViewModel

    @Before
    fun setUp() {
        viewModel = LoginViewModel()
    }

    @Test
    fun `enter an empty email return -1`() {
        val result = viewModel.loginValidateInput("","123456")
        assertThat(result).isEqualTo(-1)
    }

    @Test
    fun `enter an empty password return -1`() {
        val result = viewModel.loginValidateInput("gamal@gmail.com","")
        assertThat(result).isEqualTo(-1)
    }


    @Test
    fun `enter an invalid email return 1`(){
        val result = viewModel.loginValidateInput("Gamal","123456")
        assertThat(result).isEqualTo(1)
//        val invalidEmail: String = "gamal132"
//        val result:Int
//        if (!(android.util.Patterns.EMAIL_ADDRESS.matcher(invalidEmail).matches())) result = 1
//        else result = 0

    }

    @Test
    fun `enter an invalid password return 2`(){
        val result = viewModel.loginValidateInput("gamal@gmail.com","123")
        assertThat(result).isEqualTo(2)
    }

    @Test
    fun `enter valid inputs return 0`(){
        val result = viewModel.loginValidateInput("gamal@gmail.com","123456")
        assertThat(result).isEqualTo(0)
    }

    @Test
    fun testLoginErrMsgEmptyFields(){
        val result = viewModel.loginChooseErrMsg(-1)
        assertThat(result).isEqualTo("Make sure all fields are filled")
    }

    @Test
    fun testLoginErrMsgInvalidEmail(){
        val result = viewModel.loginChooseErrMsg(1)
        assertThat(result).isEqualTo("Enter a valid email")
    }

    @Test
    fun testLoginErrMsgInvalidPass(){
        val result = viewModel.loginChooseErrMsg(2)
        assertThat(result).isEqualTo("Password is too short")
    }

    @Test
    fun testLoginSuccessResponse() {
        val response : Response<LoginResponse> = Response.success(LoginResponse("success","mockToken"))
        assertThat(response.isSuccessful).isTrue()
    }

    @Test
    fun testLoginFailureResponse() {
        val errorResponse =
            "{\n" +
                    "  \"type\": \"error\",\n" +
                    "  \"message\": \"Inavlid email and passwrod.\"\n" + "}"
        val errorResponseBody = errorResponse.toResponseBody("application/json".toMediaTypeOrNull())
        val response : Response<LoginResponse> = Response.error(400,errorResponseBody)
        assertThat(response.isSuccessful).isFalse()
    }

}