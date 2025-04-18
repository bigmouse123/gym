package com.jiankun.gym.controller;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.jiankun.gym.util.Result;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author OfferKing
 * @version 1.0
 * @date 2025/4/18 10:56
 */
@RestController
@Slf4j
public class CaptchaController {
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private DefaultKaptcha defaultKaptcha;

    //图片以json形式返回
    @GetMapping("/captcha")
    public Result captcha(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("KaptchaController.kaptcha");
        response.setHeader("Cache-Control", "no-store, no-cache");
        response.setContentType("image/jpeg");
        // 生成文字验证码
        String captcha = defaultKaptcha.createText();
        log.info("图形验证码:{}", captcha);
        String uuid = UUID.randomUUID().toString().replace("-", "");
        //redis存储
        redisTemplate.opsForValue().set("captcha:" + uuid, captcha, 30, TimeUnit.SECONDS);
        // 生成图片验证码
        BufferedImage image = defaultKaptcha.createImage(captcha);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", out);
        String base64Code = Base64.encodeBase64String(out.toByteArray());
        Map<String, Object> map = new HashMap<>();
        map.put("captcha", "data:image/png;base64," + base64Code);
        map.put("uuid", uuid);

        return Result.ok("", map);
    }

    //直接返回图片
    @GetMapping("/captcha1")
    public void captcha1(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("KaptchaController.kaptcha");
        response.setHeader("Cache-Control", "no-store, no-cache");
        response.setContentType("image/jpeg");
        // 生成文字验证码
        String captcha = defaultKaptcha.createText();
        log.info("图形验证码:{}", captcha);
        String uuid = UUID.randomUUID().toString().replace("-", "");
        //redis存储
        redisTemplate.opsForValue().set(uuid, captcha, 30, TimeUnit.SECONDS);
        // 生成图片验证码
        BufferedImage image = defaultKaptcha.createImage(captcha);
        ServletOutputStream out = response.getOutputStream();
        ImageIO.write(image, "jpg", out);
        out.flush();
        out.close();
    }

}
