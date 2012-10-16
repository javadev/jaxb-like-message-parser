package edi.parser.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;

/**
 * This utility class helps when working with collection.
 * It heavily use interfaces Predicate and Function.
 *
 * There you can find methods for:
 * 1. create map and set
 * 2. get sub collection from one using Predicate
 * 3. Convert one collection to another using Function
 * 4. Find specific object in Collection using Predicate
 * 5. Answer to question isAll or isExsist some object in collection
 *
 */
public class CollectionUtil {

   public static <T> Set<T> asSet(T... obj) {
      Set<T> set = new HashSet<T>();
      for (T el : obj) {
         set.add(el);
      }
      return set;
   }


   public static <K, V> Map.Entry<K, V> entry(final K key, final V value) {
      return new Entry<K, V>(key, value);
   }

   public static <T> List<T> asList(T... array) {
      return new ArrayList<T>(Arrays.asList(array));
   }

   /**
    *
    * Map map = asMap(entry(1,"one"), entry(2,"Two"), entry(3,"There"))
    *
    */
   public static <K, V> Map<K, V> asMap(Map.Entry<K, V>... entries) {
      Map<K, V> map = new HashMap<K, V>();
      for (int i = 0; i < entries.length; ++i) {
         map.put(entries[i].getKey(), entries[i].getValue());
      }
      return map;
   }

   /**
    * Another way to build map
    * @param <K>
    * @param <V>
    * @param key
    * @param value
    * @return
    */
   public static <K, V> MapBuilder<K, V> asMap(K key, V value) {
      Map<K, V> map = new HashMap<K, V>();
      map.put(key, value);
      return new MapBuilder<K, V>(map);
   }

   /**
    *
    * @param <K>
    * @param <V>
    */
   public static class MapBuilder<K, V> {
      Map<K, V> map;

      public MapBuilder(Map<K, V> map) {
         super();
         this.map = map;
      }

      public MapBuilder<K, V> put(K key, V value) {
         map.put(key, value);
         return this;
      }

      public Map<K, V> map() {
         return map;
      }

   }

   /**
    * Find all object which satisfy Predicate
    */
   public static <Col extends List<T>, T> Col findAll(Col collection, Predicate<T> predicate) {
      Col result = (Col) new ArrayList<T>();
      for (T obj : collection) {
         if (predicate.evaluate(obj)) {
            result.add(obj);
         }
      }
      return result;
   }

   /**
    * Do operation described in predicate with every collection element. If
    * predicate return false stop iteration.
    */
   public static <Col extends Iterable<T>, T, Pred extends Predicate<T>> Pred doWithAll(Col collection, Pred operation) {
      for (T obj : collection) {
         if (!operation.evaluate(obj)) {
            return operation;
         }
      }
      return operation;
   }

   /**
    * Find first object in collection which satisfy
    * predicate. Order of elements inherited from Iterator
    */
   public static <Col extends Collection<T>, T> T findFirst(Col collection, Predicate<T> predicate) {
      for (T obj : collection) {
         if (predicate.evaluate(obj)) {
            return obj;
         }
      }
      return null;
   }

   /**
    * Return true if predicate return true for all elements
    */
   public static <Col extends Collection<T>, T> boolean isTrueForAll(Col collection, final Predicate<T> predicate) {
      return !isExist(collection, new Predicate<T>() {
         public boolean evaluate(T object) {
            return !predicate.evaluate(object);
         }
      });
   }

   /**
    * Return true if predicate return true for one elements.
    */
   public static <Col extends Collection<T>, T> boolean isExist(Col collection, Predicate<T> predicate) {
      return findFirst(collection, predicate) != null;
   }

   /**
    * Create new collection ColB (with same class. Collection class must have default constructor which is usally true)
    * And fill it with objects form ColA using function for converting element A to B.
    *
    * It is useful for convert object between layers or between services, or when creating Transfer objects.
    * Example in pricing services
    *
    *  CollectionUtil.convert(ticketPassenger.getAllCoupons(), new Function<PrototypeCouponPrice, Coupon>() {
         public PrototypeCouponPrice evaluate(Coupon coupon) {
            return new PrototypeCouponPriceImpl(coupon);
         }
      });
      return CouponPrices List after converting Coupon list
    */
   public static <B, ColA extends List<A>, A> List<B> convert(ColA collection, Function<B, A> function) {
      List<B> result;
      try {
         result = new ArrayList<B>();
         result.clear();
      } catch (Exception e) {
         throw new IllegalArgumentException("Can't create class=" + collection.getClass());
      }
      for (A obj : collection) {
         result.add(function.evaluate(obj));
      }
      return result;
   }

    /**
     * Create new array of type <code>Result</code>,
     * and fill it with objects form <code>Col</code> using function for converting element <code>Argument</code> to <code>Result</code>.
     * @param <Result>
     * @param <Col>
     * @param <Argument>
     * @param collection
     * @param function
     * @return
     */
    public static <Result, Col extends Collection<Argument>, Argument> Result[] convertToArray(Col collection,
            Function<Result, Argument> function) {
        int size = collection.size();
        if (size == 0) {
            return null;
        }
        Iterator<Argument> iterator = collection.iterator();
        Argument obj = iterator.next();
        Result fres = function.evaluate(obj);
        Result[] result = null;
        if (fres != null) {
            result = (Result[]) java.lang.reflect.Array.newInstance(fres.getClass(), size);
            result[0] = fres;

            for (int i = 1; i < size; i++) {
                obj = iterator.next();
                result[i] = function.evaluate(obj);
            }
        }
        return result;
    }

    /**
     * Return intersection of two collections
     *
     * @param <Col>
     * @param collection1
     * @param collection2
     * @return
     */
    public static <E, Col extends Collection<E>> Set<E> intersect(Col collection1, Col collection2) {
        return new HashSet<E>(CollectionUtils.intersection(collection1, collection2));
    }

   /**
    * Return last element and null if collection is null or empty
    */
   public static <Col extends List<T>, T> T getLast(Col c) {
      if (c == null || c.size() == 0) {
         return null;
      }
      return c.get(c.size() - 1);
   }

   /**
    * Return first element and null if collection is null or empty
    */
   public static <Col extends List<T>, T> T getFirst(Col c) {
      if (c == null || c.size() == 0) {
         return null;
      }
      return c.get(0);
   }

   private static final class Entry<K, V> implements Map.Entry<K, V> {
      private final K key;
      private final V value;

      private Entry(K key, V value) {
         this.key = key;
         this.value = value;
      }

      public K getKey() {
         return key;
      }

      public V getValue() {
         return value;
      }

      public V setValue(V value) {
         return null;
      }
   }

}
