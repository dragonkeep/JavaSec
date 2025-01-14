package Demo;

import com.thoughtworks.xstream.XStream;

/**
 * @Description
 * @Author dragonkeep
 * @Date 2025/1/10
 */
public class Test {
    public static void main(String[] args) {
        XStream xStream = new XStream();
        Person person = new Person("dragonkeep", 22, new Company("chenze", "xiamen"));
        String xml = xStream.toXML(person);
        System.out.println(xml);
    }
}
