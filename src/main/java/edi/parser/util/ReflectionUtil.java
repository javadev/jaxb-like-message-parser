package edi.parser.util;

import org.apache.commons.beanutils.PropertyUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Helper for usefull reflection operations
 */
public class ReflectionUtil {

   /**
    * Get all fields from class including super class fields
    */
   public static List<Field> getAllFields(Class cls) {
      List<Field> fields = new ArrayList<Field>();

      // add fields from supper to child
      Class iterCls = cls;
      while (iterCls != null && !iterCls.equals(Object.class)) {
         fields.addAll(0, Arrays.asList(iterCls.getDeclaredFields()));
         iterCls = iterCls.getSuperclass();
      }

      return fields;
   }

   /**
    * Get all fields from class including super class fields
    * which contain annotation annotationCls
    * @param cls
    * @param annotationCls
    * @return
    */
   public static List<Field> getAllFieldsWithAnnotation(Class cls, final Class annotationCls) {
      List<Field> fields = getAllFields(cls);
      return CollectionUtil.findAll(fields, new Predicate<Field>() {
         public boolean evaluate(Field object) {
            return object.getAnnotation(annotationCls) != null;
         }
      });
   }

   /**
    * Extract ParameterType from field if it's declared
    * like this GenericType<ParameterType>
    * @param field
    * @return
    */
   public static Class typeExtractor(Field field) {
      Class result;
      Type type = field.getGenericType();
      if (type instanceof ParameterizedType) {
         ParameterizedType parameterizedType = (ParameterizedType) field.getGenericType();
         result = (Class) parameterizedType.getActualTypeArguments()[0];
      } else {
         result = field.getType();
      }
      return result;
   }

   /**
    * TODO:
    * @param obj
    * @return
    */
   public static List<Class> getGenericTypes(Object obj) {
      assert obj == null : "object can't be null";
      // TypeVariable<Class>[] pars = obj.getClass().getTypeParameters();
      return null;
   }

   /**
    * Create new instance.
    * Hides checked reflection exception with SystemException
    */
   public static <T> T newInstance(Class<T> cls) {
      try {
         //if(args == null || args.length == 0) {
            return cls.newInstance();
        // } else {
           //TODO:
        // }
      } catch (Exception e) {
         throw new SystemException(String.format("Can't instanitate %s. Check that class have public no arg constructor.", cls), e);
      }
   }



   /**
    * Get class form name.
    * Hides checked reflection exception
    * with SystemException
    */
   public static <T> Class<T> classForName(String name) {
      try {
         return (Class<T>) Class.forName(name);
      } catch (ClassNotFoundException e) {
         throw new SystemException(e);
      }
   }

   /**
    * Set property to object by name.
    * Hides checked reflection exception
    * with SystemException
    */
   public static void setProperty(Object obj, String name, Object value) {
      try {
          PropertyUtils.setProperty(obj, name, value);
      } catch (IllegalAccessException e) {
         throw new SystemException(e);
      } catch (InvocationTargetException e) {
         throw new SystemException(e.getTargetException());
      } catch (NoSuchMethodException e) {
          throw new SystemException(e);
      }
   }

   public static Object getProperty(Object obj, String name) {
       try {
          return PropertyUtils.getProperty(obj, name);
       } catch (IllegalAccessException e) {
          throw new SystemException(e);
       } catch (InvocationTargetException e) {
          throw new SystemException(e.getTargetException());
       } catch (NoSuchMethodException e) {
           throw new SystemException(e);
       }
    }

   /**
    * Extract generic parameter from super class
    * @param class1
    * @param number
    * @return
    */
   public static Class getGenericParameter(Type class1, int number) {
//      assertArg("Not ParameterizedType", class1 instanceof ParameterizedType);
      ParameterizedType type = (ParameterizedType) class1;
      Type arg = type.getActualTypeArguments()[number];
      if (arg instanceof Class) {
          return (Class) arg;
      } else if (arg instanceof ParameterizedType) {
          return (Class) ((ParameterizedType) arg).getRawType();
      } else {
          throw new UnsupportedOperationException();
      }

   }

   public static List<Class> getLinksClasses(Class cls, Predicate<Class> isLinkPredicate) {
        List<Class> classes = new ArrayList<Class>();
        if (isLinkPredicate.evaluate(cls)) {
            classes.add(cls);
        }
        for (PropertyDescriptor desc : PropertyUtils.getPropertyDescriptors(cls)) {
            Class type = desc.getPropertyType();
            if (List.class.isAssignableFrom(type)) {
                try {
                    Field fld = cls.getDeclaredField(desc.getName());
                    type = typeExtractor(fld);
                } catch (Exception e) {
//                    AssertUtil.fail("No field");
                    int temp = 6;
                }
            }
            if (isLinkPredicate.evaluate(type)) {
                classes.addAll(getLinksClasses(type, isLinkPredicate));
            }
        }
        return classes;
    }

   public static <ObjectType> PropertyHolder<ObjectType, Object> build(final String fieldName, final Class<ObjectType> objClass) {
        class ReflectionPropertyHolder implements PropertyHolder<ObjectType, Object> {
            public void set(ObjectType obj, Object object) {
                setProperty(obj, fieldName, object);
            }

            public Object get(ObjectType obj) {
                return getProperty(obj, fieldName);
            }

            @Override
            public String toString() {
                return "PropertyHolder[" + fieldName + "]";
            }
        }
        return new ReflectionPropertyHolder();
    }
}
