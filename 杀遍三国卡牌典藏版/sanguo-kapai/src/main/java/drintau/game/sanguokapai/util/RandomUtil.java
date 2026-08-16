package drintau.game.sanguokapai.util;

import lombok.extern.slf4j.Slf4j;

import java.security.SecureRandom;

@Slf4j
public class RandomUtil {

    // 随机数最大值，给生成随机数用
    public static final int maxRandomInt100 = 100;
    public static final int maxRandomInt50 = 50;
    public static final int maxRandomInt30 = 30;
    public static final int maxRandomInt20 = 20;

    // 随机成功概率，给生成随机结果用
    public static final int rate80 = 80;
    public static final int rate50 = 50;
    public static final int rate20 = 20;
    public static final int rate10 = 10;
    public static final int rate0 = 0;

    private static final SecureRandom secureRandom = new SecureRandom();

    private RandomUtil() {
        throw new UnsupportedOperationException("禁止实例化");
    }

    public static boolean roll(int successRate) {
        int randomInt = randomInt(maxRandomInt100);
        log.info("概率阈值（不包含）：{}，随机数是：{}", successRate, randomInt);
        return randomInt < successRate;
    }

    /**
     * 生成 [0, maxInt) 之间的随机整数
     * 0 <= x < maxInt
     */
    public static int randomInt(int maxInt) {
        return secureRandom.nextInt(maxInt);
    }

}
