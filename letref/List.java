package letref;

public class List<T> {
   public final T first;
   public final List<T> rest;
   public List(T first, List<T> rest) {
      this.first = first;
      this.rest = rest;
   }
   public String toString() {
      String s = "[";
      List<T> p = this;
      while (p != null) {
         s += p.first;
         if (p.rest != null) s += ", ";
         p = p.rest;
      }
      return s + "]";
   }

   public static <T> List<T> cons(T t, List<T> ts) {
      return new List<T>(t, ts);
   }

   public static <T> List<T> append(List<T> ts, List<T> us) {
      if (ts == null) {
         return us;
      } else {
         return cons(ts.first, append(ts.rest, us));
      }
   }

   public static <T> boolean member(T t, List<T> ts) {
      if (ts == null) {
         return false;
      } else {
         return ts.first.equals(t) || member(t, ts.rest);
      }
   }

   public static <T> List<T> removeAll(List<T> ts, List<T> excl) {
      if (ts == null) {
         return null;
      } else {
         List<T> removed = removeAll(ts.rest, excl);
         if (member(ts.first, excl)) {
            return removed;
         } else {
            return cons(ts.first, removed);
         }
      }
   }
}
