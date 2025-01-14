package Demo;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * @Description
 * @Author dragonkeep
 * @Date 2025/1/5
 */
/*
* 使用Hessian进行序列化和反序列化，相比正常的ObjectInputStream的话序列化后的长度明显减少
* */
public class Main {
    public static void main(String[] args) throws Exception{
        Person person= new Person("dragonkeep",22);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Hessian2Output hessian2Output = new Hessian2Output(byteArrayOutputStream);
        hessian2Output.writeObject(person);
        hessian2Output.close();
        System.out.println(byteArrayOutputStream);

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        Hessian2Input hessian2Input=new Hessian2Input(byteArrayInputStream);
        Object person1 = hessian2Input.readObject();
        System.out.println(person1);
    }
}
