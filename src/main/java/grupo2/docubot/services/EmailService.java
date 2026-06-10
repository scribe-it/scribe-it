package grupo2.docubot.services;

import jakarta.annotation.Resource;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Service;

// Service creado para posteriorment mandar un email al analista notificando la creacion de lo CU

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendNotification(String destinationEmail) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(destinationEmail);
            helper.setSubject("🤖 DocuBot: Tus Casos de Uso están listos");

            StringBuilder htmlBuilder = new StringBuilder();
            htmlBuilder.append("<div style='font-family: Arial, sans-serif; max-width: 500px; margin: 20px auto; border: 1px solid #e0e0e0; border-radius: 8px; overflow: hidden; box-shadow: 0 4px 10px rgba(0,0,0,0.05);'>");

            // Encabezado con el Logo
            htmlBuilder.append("  <div style='background-color: #f8f9fa; padding: 30px; text-align: center; border-bottom: 1px solid #e0e0e0;'>");
            htmlBuilder.append("    <img src='cid:logoDocubot' alt='DocuBot Logo' style='width: 130px; height: auto;' />");
            htmlBuilder.append("  </div>");

            // Cuerpo
            htmlBuilder.append("  <div style='padding: 30px; text-align: center; background-color: #ffffff;'>");
            htmlBuilder.append("    <h2 style='color: #2c3e50; margin-top: 0;'>¡Procesamiento Finalizado!</h2>");
            htmlBuilder.append("    <p style='color: #555555; font-size: 15px; line-height: 1.6;'>Docubot terminó de analizar tu chat y generó los nuevos casos de uso con éxito.</p>");

            htmlBuilder.append("    <br/><br/>");

            // Botón de acceso directo
            htmlBuilder.append("    <a href='http://localhost:8080/api/v1/auth/login' style='background-color: #4f46e5; color: #ffffff; text-decoration: none; padding: 12px 28px; font-weight: bold; border-radius: 6px; display: inline-block; transition: background-color 0.2s;'>");
            htmlBuilder.append("      Ver en la Plataforma");
            htmlBuilder.append("    </a>");
            htmlBuilder.append("  </div>");

            // Pie de página
            htmlBuilder.append("  <div style='background-color: #f8f9fa; padding: 15px; text-align: center; font-size: 11px; color: #999999; border-top: 1px solid #e0e0e0;'>");
            htmlBuilder.append("    Este es un aviso automático de DocuBot. Por favor no respondas a este correo.");
            htmlBuilder.append("  </div>");
            htmlBuilder.append("</div>");

            helper.setText(htmlBuilder.toString(), true);

            mailSender.send(message);
            System.out.println("📧 Correo de aviso minimalista enviado a: " + destinationEmail);

        } catch (MessagingException | jakarta.mail.MessagingException e) {
            System.err.println("❌ Error al enviar el aviso minimalista: " + e.getMessage());
        }
    }
}
