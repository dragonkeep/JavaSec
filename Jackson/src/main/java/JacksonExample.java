import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @Description
 * @Author dragonkeep
 * @Date 2024/12/31
 */
public class JacksonExample {
    public static void serializeWithObjectMapper() throws Exception{
        String json="{\"name\":\"dragonkeep\",\"age\":20}";
        ObjectMapper objectMapper = new ObjectMapper();
        Person person = objectMapper.readValue(json,Person.class);
        System.out.println(person.getName());
        System.out.println(person.getAge());
    }
    public static  void unserializeWithObjectMapper() throws Exception{
        Person person = new Person("dragonkeep",20);
        ObjectMapper  objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(person);
        System.out.println(json);
    }
    public static void serializeWithJsonParser() throws Exception{
        String json="{\"name\":\"dragonkeep\",\"age\":20}";
        JsonFactory jsonFactory = new JsonFactory();
        JsonParser parser = jsonFactory.createParser(json);
        while(!parser.isClosed()){
            System.out.println(parser.getCurrentName());
        }
    }
    public static void main(String[] args) throws  Exception{
        //serializeWithObjectMapper();
        //unserializeWithObjectMapper();
        serializeWithJsonParser();
    }
}
