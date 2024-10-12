package api.service;

import api.model.Habit;
import api.model.User;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.List;
import java.util.Properties;

public class NotificationService {

    public void sendNotificationConsole(List<Habit> habits) {
        System.out.println(message(habits));
    }

    public void sendNotificationEmail(User user, List<Habit> habits) {
        try {
            Properties props = new Properties();
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.ssl.trust", "*");
            Session session = Session.getInstance(props);
            MimeMessage message = new MimeMessage(session);

            message.setFrom(new InternetAddress("myEmail@gmail.com"));// Замените на ваш адрес электронной почты
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(user.getEmail()));
            //Тема
            message.setSubject("HabitTracking");
            //Текст
            message.setText(message(habits));

            Authenticator auth = new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication("sender@example.com", "password");
                }
            };

            Transport transport = session.getTransport("smtp");
            transport.connect("smtp.gmail.com", 587, "sender@example.com", "password");
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendNotificationWeb(User user, List<Habit> habits) {
        try {
            HttpPost httpPost = new HttpPost("http://localhost:9090/sendNotification"); // Замените на ваш URL
            httpPost.setHeader("Content-Type", "application/json");
            String jsonMessage = "{message: " + message(habits) + "}"; //изменить json под вашу сущность
            httpPost.setEntity(new StringEntity(jsonMessage));
            CloseableHttpResponse httpResponse = HttpClientBuilder.create().build().execute(httpPost);
            String status = httpResponse.getStatusLine().toString();
            String body = EntityUtils.toString(httpResponse.getEntity());
            if (status.equals("HTTP/1.1 200 ")) {
                System.out.println("Уведомление успешно отправлено");
            } else {
                System.out.println("Ошибка" + status + body);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String message(List<Habit> habits) {
        List<Habit> habitMisc = habits.stream().filter(habit -> habit.completed() && habit.checkMissedDays() > 0).toList();
        if (habitMisc == null || habitMisc.isEmpty()) return "";
        StringBuilder message = new StringBuilder("Вам нужно выполнить эти привычки: ");
        for (Habit habit : habitMisc) {
            message.append(habit.getName())
                    .append(": дата выполнения: ")
                    .append(habit.getNextExecutionDate())
                    .append(". Просрочено на ")
                    .append(habit.checkMissedDays())
                    .append(" дней. ");
        }
        return message.toString();
    }
}
