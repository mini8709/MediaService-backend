package com.mediaservice.infrastructure

import com.mediaservice.config.EmailProperties
import com.mediaservice.exception.ErrorCode
import com.mediaservice.exception.ServerUnavailableException
import org.springframework.mail.MailException
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.JavaMailSenderImpl
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Component
import java.util.Properties

@Component
class GoogleMailSender(val emailProperties: EmailProperties) {

    private val mailSender: JavaMailSender = JavaMailSenderImpl().apply {
        val properties = Properties()
        properties.put("mail.smtp.auth", true)
        properties.put("mail.smtp.starttls.enable", true)
        properties.put("mail.smtp.starttls.required", true)
        properties.put("mail.smtp.ssl.trust", emailProperties.host)

        host = emailProperties.host
        port = emailProperties.port
        username = emailProperties.username
        password = emailProperties.password
        javaMailProperties = properties
        protocol = "smtp"
        defaultEncoding = "UTF-8"
    }

    fun sendMailWithNewPassword(to: String, newPassword: String) {
        try {
            val message = this.mailSender.createMimeMessage()
            MimeMessageHelper(message, false, "UTF-8").apply {
                setFrom("cotlin.dev@gmail.com")
                setTo(to)
                setSubject("New password from Co-tlin")
                setText(
                    "<table class=\"m_-7550374159978672037content-shell-table\" width=\"500\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-spacing:0;background-color:rgb(255,255,255)\">\n" +
                        "    <tbody>\n" +
                        "    <tr>\n" +
                        "    <td class=\"m_-7550374159978672037pixel\">\n" +
                        "    <img src=\"https://ci5.googleusercontent.com/proxy/52Zuor4pycAFx0WgBXgsT8SeMRgeOYV_cWL6bVKLlBaLiBXGfdxmfrSnm4DmR7NVH8nP_BdMnZoxkQdemKmH-Q90hMz3DUFBmogBvY_1jvTX5rEnUxw5_HXkhYmXTZqdirmjUnLbcWuMzSpaAJ1hiX_rmPzzV08-0rr_uvA7t8NQ3rK3bBAoyOJj0aHLaN6Mpqx-9KEUg--l98Md5kj3B7VujZsPl-5fmrZw7eG53E3BFqDw_8clwSpGx5g0dLQutnur6EtcdOZY-228bAijeHUiaji0NO5kQN1cVf7IZEfsJ_qf-G8BXWbMfO7zh5QUlHwaKonrRjtkTcavjrewGybk_qxAcNavbpcSWw9UXskR8ORnZQAXqzi9boh4yKLJ6infwgCgCegwNONGC6l-9sHcowqrhk7OBUFa8eWkcDO6DGgnLBOWYDtwXZKz6WMPr4wecH1pQllGFkL-4U4_6WLSfg3heG1WRJeAerr_LYMTpGQUo2NttgXxad7na6gv1XA_-xlFCgf5B_Avyo4vP-CNRwp_9xCyfm26a15GxXEhyAxsx5mn_zFnak9OxaWPASVilu6moSFYhjFl3UEHHBHF8jQ2GFa4DGIfIlCL-Kk3A184qHvDiHjtrBE6OEDE9JoVG1df-VvC_VgewDCmD-udO1cPFb1lI0O9OkJkj50ZrmGkdAdqagq8dwrrMreugQxmG3wEiuwK1w=s0-d-e1-ft#https://beaconimages.netflix.net/img/BAQgBEAEa4AJ2THSsgosiINnMMOKPnivWRP4WrVdJ7UWnBo4owaKDYM1CR4YEpqW4L1bmZGiuYAvGvOHGLIws1FrWnwufeJO3_Tdnm3OChT6UvydWtQKBd72sR_JvA07f8l8hq7YqmCedUKDuaNeppYMgkQqMon8pdcqrEes4jpnpr48XT6idkOEDO6x0PuT2kgZ_od-KrZEtd9IDDX_Fd3k5IspgIqKqQgh-cbkIoKQ2gqxWC3DO7w1mtb6lUSkFXBpmSIkk9vKMgcGsBm1Du-IumNYKNMk6bykuYKQ5izpVHFHfxBuvVY2i12WcOSDO13pdUFzfbQZyY1nwzEzZNaivduuaVXlIih2HxT-pM0Bvqjq4EhqW8wMTQ6_DlnPO6hkypRjB8o8HYSH8AYA1q3G4Vn7PyVXAV51MY5iLJHNmMBKT1cmx7nBH6Tx8VDKrIhxV--Yhmx61gOYkOWaaZThbKOYVTb9y\" style=\"display:block;border:none;outline:none;border-collapse:collapse;overflow:hidden;height:1px;width:1px;border:0;margin:0;padding:0\" border=\"0\" class=\"CToWUd\"> </td>\n" +
                        "    </tr>\n" +
                        "\n" +
                        "    <tr>\n" +
                        "    <td class=\"m_-7550374159978672037content-shell\" align=\"center\">\n" +
                        "    <table class=\"m_-7550374159978672037gem-element-nflxLogo\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-spacing:0\">\n" +
                        "    <tbody>\n" +
                        "    <tr>\n" +
                        "    <td class=\"m_-7550374159978672037content-padding\" style=\"padding-top:20px;padding-left:40px;padding-right:40px\" align=\"left\"> <a href=\"https://www.netflix.com/browse?lnktrk=EMP&amp;g=13B325FFD3420EC433DBEB830C06D4DE645794CE&amp;lkid=URL_HOME\" style=\"color:inherit\" target=\"_blank\" data-saferedirecturl=\"https://www.google.com/url?q=https://www.netflix.com/browse?lnktrk%3DEMP%26g%3D13B325FFD3420EC433DBEB830C06D4DE645794CE%26lkid%3DURL_HOME&amp;source=gmail&amp;ust=1643953421427000&amp;usg=AOvVaw3v7MMcls6py7IMXuMGjUba\"> <img alt=\"Netflix\" src=\"https://ci3.googleusercontent.com/proxy/9A3a1TA6Bh0RAmVqtdE5P9Z8SdpMQsxFBej9freij52Jz3U4dG4idTHySBCwWROmaNIXVmM2bO1Ewgzn23JDu7yUnw=s0-d-e1-ft#https://assets.nflxext.com/us/email/gem/nflx.png\" width=\"24\" border=\"0\" style=\"display:block;border:none;outline:none;border-collapse:collapse;border-style:none\" class=\"CToWUd\"> </a> </td>\n" +
                        "    </tr>\n" +
                        "    </tbody>\n" +
                        "    </table>\n" +
                        "    <table align=\"left\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-spacing:0\">\n" +
                        "    <tbody>\n" +
                        "    <tr>\n" +
                        "    <td class=\"m_-7550374159978672037gem-copy m_-7550374159978672037content-padding m_-7550374159978672037gem-h1\" align=\"left\" style=\"padding-top:20px;color:#221f1f;font-family:NetflixSans-Bold,Helvetica,Roboto,Segoe UI,sans-serif;font-weight:700;padding-left:40px;padding-right:40px;font-size:36px;line-height:42px;letter-spacing:-1px\"> 비밀번호 재설정 </td>\n" +
                        "    </tr>\n" +
                        "    </tbody>\n" +
                        "    </table>\n" +
                        "    <table align=\"left\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-spacing:0\">\n" +
                        "    <tbody>\n" +
                        "    <tr>\n" +
                        "    <td class=\"m_-7550374159978672037gem-copy m_-7550374159978672037content-padding m_-7550374159978672037gem-p\" align=\"left\" style=\"padding-top:20px;color:#221f1f;padding-left:40px;padding-right:40px;font-family:NetflixSans-Light,Helvetica,Roboto,Segoe UI,sans-serif;font-weight:300;font-size:16px;line-height:21px;font-size:16px;line-height:21px\"> " + to + " 님, 안녕하세요. </td>\n" +
                        "    </tr>\n" +
                        "    </tbody>\n" +
                        "    </table>\n" +
                        "    <table align=\"left\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-spacing:0\">\n" +
                        "    <tbody>\n" +
                        "    <tr>\n" +
                        "    <td class=\"m_-7550374159978672037gem-copy m_-7550374159978672037content-padding m_-7550374159978672037gem-p\" align=\"left\" style=\"padding-top:20px;color:#221f1f;padding-left:40px;padding-right:40px;font-family:NetflixSans-Light,Helvetica,Roboto,Segoe UI,sans-serif;font-weight:300;font-size:16px;line-height:21px;font-size:16px;line-height:21px\"> Netflix의 동영상을 시청하시려면 비밀번호를 재설정해 주세요. </td>\n" +
                        "    </tr>\n" +
                        "    </tbody>\n" +
                        "    </table>\n" +
                        "    <table class=\"m_-7550374159978672037gem-single-button-shell m_-7550374159978672037button-mobile-flex\" width=\"100%\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-spacing:0\">\n" +
                        "    <tbody>\n" +
                        "    <tr>\n" +
                        "    <td class=\"m_-7550374159978672037gem-copy m_-7550374159978672037content-padding m_-7550374159978672037gem-h5\" align=\"left\" style=\"padding-top:20px;color:#221f1f;font-family:NetflixSans-Bold,Helvetica,Roboto,Segoe UI,sans-serif;font-weight:700;padding-left:40px;padding-right:40px;font-size:14px;line-height:17px;letter-spacing:-0.2px\"> 새 비밀번호 " + newPassword + " </td>\n" +
                        "    </tr>\n" +
                        "    </tbody>\n" +
                        "    </table>\n" +
                        "\n" +
                        "    <table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-spacing:0\">\n" +
                        "    <tbody>\n" +
                        "    <tr>\n" +
                        "    <td class=\"m_-7550374159978672037spacer\" style=\"padding:25px 0 0 0;font-size:0;line-height:0\"> &nbsp; </td>\n" +
                        "    </tr>\n" +
                        "    </tbody>\n" +
                        "    </table>\n" +
                        "\n" +
                        "    <table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-spacing:0\">\n" +
                        "    <tbody>\n" +
                        "    <tr>\n" +
                        "    <td class=\"m_-7550374159978672037content-padding\" style=\"padding-top:0;padding-left:40px;padding-right:40px\">\n" +
                        "    <table align=\"left\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-spacing:0\">\n" +
                        "    <tbody>\n" +
                        "    <tr>\n" +
                        "    <td class=\"m_-7550374159978672037empty\" style=\"border-top:2px solid #221f1f;font-size:0;line-height:0\"> &nbsp; </td>\n" +
                        "    </tr>\n" +
                        "    </tbody>\n" +
                        "    </table> </td>\n" +
                        "    </tr>\n" +
                        "    </tbody>\n" +
                        "    </table>\n" +
                        "    <table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-spacing:0\">\n" +
                        "    </table>\n" +
                        "    </td>\n" +
                        "    </tr>\n" +
                        "    </tbody>\n" +
                        "    </table>",
                    true
                )
            }

            this.mailSender.send(message)
        } catch (e: MailException) {
            throw ServerUnavailableException(ErrorCode.UNAVAILABLE_MAIL_SERVER, e.message!!)
        }
    }
}
