package mirror;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;

public final class RefClass {

    private static HashMap<Class<?>,Constructor<?>> REF_TYPES = new HashMap<Class<?>, Constructor<?>>();
    static {
        try {
            REF_TYPES.put(RefObject.class, RefObject.class.getConstructor(Class.class, Field.class));
            REF_TYPES.put(RefMethod.class, RefMethod.class.getConstructor(Class.class, Field.class));
            REF_TYPES.put(RefInt.class, RefInt.class.getConstructor(Class.class, Field.class));
            REF_TYPES.put(RefLong.class, RefLong.class.getConstructor(Class.class, Field.class));
            REF_TYPES.put(RefFloat.class, RefFloat.class.getConstructor(Class.class, Field.class));
            REF_TYPES.put(RefDouble.class, RefDouble.class.getConstructor(Class.class, Field.class));
            REF_TYPES.put(RefBoolean.class, RefBoolean.class.getConstructor(Class.class, Field.class));
            REF_TYPES.put(RefStaticObject.class, RefStaticObject.class.getConstructor(Class.class, Field.class));
            REF_TYPES.put(RefStaticInt.class, RefStaticInt.class.getConstructor(Class.class, Field.class));
            REF_TYPES.put(RefStaticMethod.class, RefStaticMethod.class.getConstructor(Class.class, Field.class));
            REF_TYPES.put(RefConstructor.class, RefConstructor.class.getConstructor(Class.class, Field.class));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Class<?> load(Class<?> mappingClass, String className) {
        try {
            return load(mappingClass, Class.forName(className));
        } catch (Exception e) {
            return null;
        }
    }


    public static Class load(Class mappingClass, Class<?> realClass) {
        Field[] fields = mappingClass.getDeclaredFields();
        //初始化class 中所有的成员变量
        for (Field field : fields) {
            try {
                if (Modifier.isStatic(field.getModifiers())) {//静态方法~
                    //获取反射需要的类型 -- 每个类型对应一个构造函数,且都在 Ref对象中有初始化
                    Constructor<?> constructor = REF_TYPES.get(field.getType());
                    if (constructor != null) {
                        //用构造函数初始化所有的静态成员变量
                        field.set(null, constructor.newInstance(realClass, field));
                    }
                }
            }
            catch (Exception e) {
                // Ignore
            }
        }
        return realClass;
    }

}