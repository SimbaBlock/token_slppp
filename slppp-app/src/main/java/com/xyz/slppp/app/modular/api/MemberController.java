package com.xyz.slppp.app.modular.api;

import com.xyz.slppp.app.core.mail.MailService;
import com.xyz.slppp.app.core.util.JsonResult;
import com.xyz.slppp.app.modular.system.model.Member;
import com.xyz.slppp.app.modular.system.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletResponse;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.TimeUnit;


@Controller
@RequestMapping("/rest/member")
public class MemberController {

    @Autowired
    private MemberService memberService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private MailService mailService;


    /**
     * 用户注册
     * @param
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("/register")
    public JsonResult register(String username, String password, String email, String privateKey, String code) {

        String emailCode = (String)redisTemplate.opsForValue().get(email);

        if (!emailCode.equals(code))
            System.out.println("验证码错误");

        Member member = memberService.findByUserName(username);

        if (member != null)
            System.out.println("当前用户已存在");

        Member Member = new Member();
        Member.setEmail(email);
        Member.setPassword(password);
        Member.setPrivateKey(privateKey);
        Member.setUsername(username);
        memberService.insert(Member);

        return new JsonResult();

    }


    /**
     * 发送邮箱
     * @param
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("/sendEmail")
    public JsonResult sendEmail(String email) throws MessagingException {

        int radomInt = new Random().nextInt(999999);

        redisTemplate.opsForValue().set(email, radomInt, 1000, TimeUnit.SECONDS);

        String subject = "upay注册";
        StringBuffer sb = new StringBuffer();
        sb.append("<p>你好，感谢您注册upay，您的验证码是：</p>" + radomInt);
        String text = sb.toString();



            JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
            javaMailSender.setHost("smtp.qq.com");
            javaMailSender.setPort(465);
            javaMailSender.setUsername("574590203@qq.com");
            javaMailSender.setPassword("hpucnishmgcrbfdc");
            Properties properties = new Properties();
            properties.setProperty("mail.host", "smtp.qq.com");
            properties.setProperty("mail.transport.protocol", "smtp");
            properties.setProperty("mail.smtp.auth", "true");
            properties.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            properties.setProperty("mail.smtp.port", "465");
            properties.setProperty("mail.smtp.socketFactory.port", "465");

            javaMailSender.setJavaMailProperties(properties);

        MimeMessage mailMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mailMessage, true, "utf-8");
        helper.setFrom("574590203@qq.com");// 设置发件人
        helper.setTo(email);// 设置收件人
        helper.setSubject("upay注册");// 设置主题
        helper.setText(text);// 邮件体
        javaMailSender.send(mailMessage);// 发送邮件



//        mailService.send(email, subject, text);

        return new JsonResult();

    }


    /**
     * 用户登录
     * @param
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("/login")
    public JsonResult login(String username, String password, HttpServletResponse response) {
        return memberService.login(username, password, response);
    }


}
