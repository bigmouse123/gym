package com.jiankun.gym.config;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * @author OfferKing
 * @version 1.0
 * @date 2025/4/18 10:55
 */
@Configuration
public class CaptchaConfig {

    @Bean
    public DefaultKaptcha getDefaultKaptcha() {
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        Properties properties = new Properties();
        // 验证码是否带边框 No
        properties.setProperty("kaptcha.border", "no");
        // 验证码字体颜色
        properties.setProperty("kaptcha.textproducer.font.color", "blue");
        // 验证码整体宽度
        properties.setProperty("kaptcha.image.width", "300");
        // 验证码整体高度
        properties.setProperty("kaptcha.image.height", "75");
        // 文字个数
        properties.setProperty("kaptcha.textproducer.char.length", "4");
        // 文字大小
        properties.setProperty("kaptcha.textproducer.font.size", "50");
        // 文字随机字体
        properties.setProperty("kaptcha.textproducer.font.names", "宋体,楷体,微软雅黑");
        // 文字距离
        properties.setProperty("kaptcha.textproducer.char.space", "16");
        // 干扰线颜色
        properties.setProperty("kaptcha.noise.color", "blue");
        Config config = new Config(properties);
        defaultKaptcha.setConfig(config);

        return defaultKaptcha;
    }
}
