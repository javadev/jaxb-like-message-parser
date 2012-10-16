package edi.parser.util;

/**
*
*
* @param <ObjectType>
* @param <PropertType>
*/
public interface PropertyHolder<ObjectType, PropertType> {
   void set(ObjectType obj, PropertType object);

   PropertType get(ObjectType obj);
}
