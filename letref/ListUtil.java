package letref;

import java.util.LinkedList;

public class ListUtil {
   public static <T> LinkedList<T> cons(T t, LinkedList<T> ts) {
      LinkedList<T> copy = copy(ts);
      copy.addFirst(t);
      return copy;
   }

   public static <T> LinkedList<T> append(LinkedList<T> ts, LinkedList<T> us) {
      LinkedList<T> copy = copy(ts);
      copy.addAll(us);
      return copy;
   }

   public static <T> LinkedList<T> copy(LinkedList<T> ts) {
      LinkedList<T> copy = new LinkedList<T>();
      copy.addAll(ts);
      return copy;
   }
}
