package supportly.supportlybackend.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import supportly.supportlybackend.Dto.EmailDto;



@Service
public class MailService {

    @Autowired
    private JavaMailSender javaMailSender;



    public void sendMail(String email ) throws MessagingException {
        EmailDto emailDto = new EmailDto(email, "Dostałeś/aś nowe zadanie",
                "<p style=\"text - align:center;\"><strong>Do twojego konta zostało dodane nowe zadanie.</strong></p>" +
                        "\n" +
                        "<p style=\" text - align:center;\"><strong>Sprawdzi je u siebie</strong></p>", true);


        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
        mimeMessageHelper.setTo(emailDto.getEmail());
        mimeMessageHelper.setSubject(emailDto.getSubject());
        mimeMessageHelper.setText(emailDto.getText(), emailDto.getIsHtmlContent());
        javaMailSender.send(mimeMessage);
    }
}
