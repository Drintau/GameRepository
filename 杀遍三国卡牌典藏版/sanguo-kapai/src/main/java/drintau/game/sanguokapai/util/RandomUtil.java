package drintau.game.sanguokapai.util;

import lombok.extern.slf4j.Slf4j;

import java.security.SecureRandom;

@Slf4j
public class RandomUtil {

    public static final int rate100 = 100;
    public static final int rate50 = 50;
    public static final int rate40 = 50;
    public static final int rate30 = 30;
    public static final int rate20 = 20;
    public static final int rate10 = 10;
    public static final int rate0 = 0;

    private static final SecureRandom secureRandom = new SecureRandom();

    private RandomUtil() {
        throw new UnsupportedOperationException("禁止实例化");
    }

    public static boolean roll(int successRate) {
        int randomInt = randomInt(rate100);
        log.info("随机数是：{}", randomInt);
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
