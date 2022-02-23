package com.mediaservice

import com.mediaservice.application.UserService
import com.mediaservice.application.dto.user.PasswordFindRequestDto
import com.mediaservice.application.dto.user.PasswordUpdateRequestDto
import com.mediaservice.application.dto.user.SignInRequestDto
import com.mediaservice.application.dto.user.SignUpRequestDto
import com.mediaservice.application.dto.user.SignUpVerifyAuthRequestDto
import com.mediaservice.application.dto.user.SignUpVerifyMailRequestDto
import com.mediaservice.application.dto.user.UserResponseDto
import com.mediaservice.config.JwtTokenProvider
import com.mediaservice.domain.Profile
import com.mediaservice.domain.RefreshToken
import com.mediaservice.domain.Role
import com.mediaservice.domain.User
import com.mediaservice.domain.repository.ProfileRepository
import com.mediaservice.domain.repository.RefreshTokenRepository
import com.mediaservice.domain.repository.UserRepository
import com.mediaservice.exception.BadRequestException
import com.mediaservice.exception.ErrorCode
import com.mediaservice.exception.ServerUnavailableException
import com.mediaservice.infrastructure.Authentication
import com.mediaservice.infrastructure.GoogleMailSender
import com.mediaservice.infrastructure.RedisUtil
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.Properties
import java.util.UUID
import javax.mail.Session
import javax.mail.internet.MimeMessage
import kotlin.test.assertEquals

class UserServiceTest {
    private var userRepository = mockk<UserRepository>()
    private var profileRepository = mockk<ProfileRepository>()
    private var tokenProvider = mockk<JwtTokenProvider>()
    private var refreshTokenRepository = mockk<RefreshTokenRepository>()
    private val mailSender = mockk<GoogleMailSender>()
    private val redisUtil = mockk<RedisUtil>()
    private val authentication = mockk<Authentication>()
    private var userService: UserService = UserService(
        this.userRepository,
        this.profileRepository,
        this.redisUtil,
        this.refreshTokenRepository,
        this.tokenProvider,
        this.mailSender,
        this.authentication
    )

    private lateinit var user: User
    private lateinit var profile: Profile
    private lateinit var pwUpdatedUser: User
    private lateinit var id: UUID
    private lateinit var profileId: UUID
    private lateinit var email: String
    private lateinit var prop: Properties
    private lateinit var session: Session
    private lateinit var message: MimeMessage
    private lateinit var refreshToken: RefreshToken
    private lateinit var signUpKey: String

    @BeforeEach
    fun setup() {
        clearAllMocks()
        this.id = UUID.randomUUID()
        this.profileId = UUID.randomUUID()
        this.email = "test@gmail.com"
        this.user = User(id, email, "1234", Role.USER)
        this.profile = Profile(profileId, user, "action", "19+", "image_url", false)
        this.pwUpdatedUser = User(id, email, "test123!!", Role.USER)
        this.prop = Properties()
        this.session = Session.getDefaultInstance(prop)
        this.message = MimeMessage(session)
        this.refreshToken = RefreshToken("refresh_token")
        this.signUpKey = "sign-up key"
    }

    @Test
    fun successFindById() {
        // given
        every { userRepository.findById(id) } returns this.user

        // when
        val userResponseDto: UserResponseDto = this.userService.findById(this.id)

        // then
        assertEquals(this.user.email, userResponseDto.email)
    }

    @Test
    fun failFindById() {
        val exception = assertThrows(BadRequestException::class.java) {
            // given
            every { userRepository.findById(id) } returns null

            // when
            this.userService.findById(this.id)
        }

        // then
        assertEquals(ErrorCode.ROW_DOES_NOT_EXIST, exception.errorCode)
    }

    @Test
    fun successSignUpVerifyMail() {
        // given
        val signUpVerifyMailRequestDto = SignUpVerifyMailRequestDto("test@gmail.com")

        every { userRepository.findByEmail(signUpVerifyMailRequestDto.email) } returns null
        every { authentication.createSignUpKey() } returns this.signUpKey
        every { redisUtil.setDataExpire(signUpVerifyMailRequestDto.email, signUpKey, 180) } returns this.signUpKey
        every { mailSender.sendMailWithSignUpKey(signUpVerifyMailRequestDto.email, signUpKey) } returns Unit

        // when
        val email = this.userService.signUpVerifyMail(signUpVerifyMailRequestDto)

        // then
        assertEquals(email, "test@gmail.com")
    }

    @Test
    fun failSignUpVerifyMailDuplicatedEmail() {
        val exception = assertThrows(BadRequestException::class.java) {
            // given
            val signUpVerifyMailRequestDto = SignUpVerifyMailRequestDto("test@gmail.com")

            every { userRepository.findByEmail(signUpVerifyMailRequestDto.email) } returns this.user

            // when
            this.userService.signUpVerifyMail(signUpVerifyMailRequestDto)
        }

        // then
        assertEquals(ErrorCode.ROW_ALREADY_EXIST, exception.errorCode)
    }

    @Test
    fun failSignUpVerifyAuth_ValidTimeOut() {
        val exception = assertThrows(BadRequestException::class.java) {
            // given
            val signUpVerifyAuthRequestDto = SignUpVerifyAuthRequestDto("test@gmail.com", "1234")

            every { redisUtil.getData(signUpVerifyAuthRequestDto.email) } returns null

            // when
            this.userService.signUpVerifyAuth(signUpVerifyAuthRequestDto)
        }

        // then
        assertEquals(ErrorCode.NOT_ACCESSIBLE, exception.errorCode)
    }

    @Test
    fun successSignUp() {
        // given
        mockkObject(User)
        val signUpRequestDto = SignUpRequestDto("test@gmail.com", "1234")

        every { User.of(signUpRequestDto.email, signUpRequestDto.password, Role.USER) } returns this.user
        every { userRepository.findByEmail(signUpRequestDto.email) } returns null
        every { redisUtil.getData(signUpRequestDto.email) } returns this.signUpKey
        every { userRepository.save(user) } returns this.user

        // when
        val userResponseDto: UserResponseDto = this.userService.signUp(signUpRequestDto)

        // then
        assertEquals(this.user.email, userResponseDto.email)
    }

    @Test
    fun successIsDuplicatedByEmail() {
        // given
        every { userRepository.findByEmail(email) } returns null

        // when
        val isDuplicated = this.userService.isDuplicatedByEmail(email)

        // then
        assertEquals(isDuplicated, false)
    }

    @Test
    fun successSignIn() {
        // given
        val signInRequestDto = SignInRequestDto("test@gmail.com", "1234")

        every { userRepository.findByEmail(signInRequestDto.email) } returns this.user
        every { tokenProvider.createAccessToken(user.id!!, user.role) } returns "valid token"
        every { tokenProvider.createRefreshToken() } returns this.refreshToken
        every { refreshTokenRepository.save(refreshToken) } returns this.refreshToken
        every { profileRepository.findByUserId(id) } returns listOf(this.profile)

        // when
        val signInResponseDto = this.userService.signIn(signInRequestDto)

        // then
        assertEquals(signInResponseDto.accessToken, "valid token")
    }

    @Test
    fun failSignIn_CannotFindUser() {
        val exception = assertThrows(BadRequestException::class.java) {
            // given
            val signInRequestDto = SignInRequestDto("test@gmail.com", "1234")

            every { userRepository.findByEmail(signInRequestDto.email) } returns null

            // when
            this.userService.signIn(signInRequestDto)
        }

        // then
        assertEquals(ErrorCode.INVALID_SIGN_IN, exception.errorCode)
    }

    @Test
    fun failSignIn_IncorrectPassword() {
        val exception = assertThrows(BadRequestException::class.java) {
            // given
            val signInRequestDto = SignInRequestDto("test@gmail.com", "12345")

            every { userRepository.findByEmail(signInRequestDto.email) } returns this.user

            // when
            this.userService.signIn(signInRequestDto)
        }

        // then
        assertEquals(ErrorCode.INVALID_SIGN_IN, exception.errorCode)
    }

    @Test
    fun successUpdatePassword() {
        // given
        val passwordUpdateRequestDto = PasswordUpdateRequestDto("1234", "test123!!")

        every { userRepository.findById(id) } returns this.user
        every { userRepository.update(id, user) } returns this.pwUpdatedUser

        // when
        val userResponseDto = this.userService.updatePassword(id, passwordUpdateRequestDto)

        // then
        assertEquals(this.pwUpdatedUser.id, userResponseDto.id)
    }

    @Test
    fun failUpdatePassword_CannotFindUser() {
        val exception = assertThrows(BadRequestException::class.java) {
            // given
            val passwordUpdateRequestDto = PasswordUpdateRequestDto("1234", "test123!!")
            every { userRepository.findById(id) } returns null

            // when
            this.userService.updatePassword(id, passwordUpdateRequestDto)
        }
        // then
        assertEquals(ErrorCode.ROW_DOES_NOT_EXIST, exception.errorCode)
    }

    @Test
    fun failUpdatePassword_InvalidPassword() {
        val exception = assertThrows(BadRequestException::class.java) {
            // given
            val passwordUpdateRequestDto = PasswordUpdateRequestDto("1233", "test123!!")
            every { userRepository.findById(id) } returns this.user

            // when
            this.userService.updatePassword(id, passwordUpdateRequestDto)
        }
        // then
        assertEquals(ErrorCode.INVALID_SIGN_IN, exception.errorCode)
    }

    @Test
    fun failUpdatePassword_InvalidPasswordFormat() {
        val exception = assertThrows(BadRequestException::class.java) {
            // given
            val passwordUpdateRequestDto = PasswordUpdateRequestDto("1234", "test1231")
            every { userRepository.findById(id) } returns this.user

            // when
            this.userService.updatePassword(id, passwordUpdateRequestDto)
        }
        // then
        assertEquals(ErrorCode.INVALID_FORMAT, exception.errorCode)
    }

    @Test
    fun failUpdatePassword_CannotFindUpdatedUser() {
        val exception = assertThrows(BadRequestException::class.java) {
            // given
            val passwordUpdateRequestDto = PasswordUpdateRequestDto("1234", "test123!!")
            every { userRepository.findById(id) } returns this.user
            every { userRepository.update(id, user) } returns null

            // when
            this.userService.updatePassword(id, passwordUpdateRequestDto)
        }
        // then
        assertEquals(ErrorCode.ROW_DOES_NOT_EXIST, exception.errorCode)
    }

    @Test
    fun successFindPassword() {
        // given
        val passwordFindRequestDto = PasswordFindRequestDto(email)
        every { userRepository.findByEmail(email) } returns this.user
        every { userRepository.update(id, user) } returns this.pwUpdatedUser
        every { mailSender.sendMailWithNewPassword(email, any()) } returns Unit

        // when
        val userResponseDto = userService.findPassword(passwordFindRequestDto)

        // then
        assertEquals(this.pwUpdatedUser.id, userResponseDto.id)
    }

    @Test
    fun failFindPassword_CannotFindUser() {
        val exception = assertThrows(BadRequestException::class.java) {
            // given
            val passwordFindRequestDto = PasswordFindRequestDto(email)
            every { userRepository.findByEmail(email) } returns null

            // when
            this.userService.findPassword(passwordFindRequestDto)
        }
        // then
        assertEquals(ErrorCode.ROW_DOES_NOT_EXIST, exception.errorCode)
    }

    @Test
    fun failFindPassword_CannotFindUpdateUser() {
        val exception = assertThrows(BadRequestException::class.java) {
            // given
            val passwordFindRequestDto = PasswordFindRequestDto(email)
            every { userRepository.findByEmail(email) } returns user
            every { mailSender.sendMailWithNewPassword(email, any()) } returns Unit
            every { userRepository.update(id, user) } returns null

            // when
            this.userService.findPassword(passwordFindRequestDto)
        }
        // then
        assertEquals(ErrorCode.ROW_DOES_NOT_EXIST, exception.errorCode)
    }

    @Test
    fun failFindPassword_CannotSendMail() {
        val exception = assertThrows(ServerUnavailableException::class.java) {
            // given
            val passwordFindRequestDto = PasswordFindRequestDto(email)
            every { userRepository.findByEmail(email) } returns this.user
            every { userRepository.update(id, user) } returns this.pwUpdatedUser
            every { mailSender.sendMailWithNewPassword(email, any()) } throws ServerUnavailableException(ErrorCode.UNAVAILABLE_MAIL_SERVER, "test")

            // when
            this.userService.findPassword(passwordFindRequestDto)
        }
        // then
        assertEquals(ErrorCode.UNAVAILABLE_MAIL_SERVER, exception.errorCode)
    }

    @Test
    fun successFindProfiles() {
        // given
        every { userRepository.findById(id) } returns this.user
        every { profileRepository.findByUserId(id) } returns listOf(this.profile)

        // when
        val profileResponseDto = userService.findProfiles(id)

        // then
        assertEquals(profileResponseDto[0].name, "action")
    }

    @Test
    fun failFindProfiles_CannotFindUser() {
        val exception = assertThrows(BadRequestException::class.java) {
            // given
            every { userRepository.findById(id) } returns null

            // when
            this.userService.findProfiles(id)
        }
        // then
        assertEquals(ErrorCode.ROW_DOES_NOT_EXIST, exception.errorCode)
    }
}
