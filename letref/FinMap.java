package letref;

import java.util.*;

public class FinMap<X, Y> {
   private TreeMap<X, Y> map;
   public FinMap() { this.map = new TreeMap<X, Y>(); }
   public FinMap(TreeMap<X, Y> map) { this.map = map; }
   public FinMap<X, Y> extend(X x, Y y) {
      TreeMap<X, Y> extended = new TreeMap<X, Y>(map);
      extended.put(x, y);
      return new FinMap<X, Y>(extended);
   }
   public Y lookup(X x) {
      if (!map.containsKey(x))
         throw new RuntimeException(x + " unmapped");
      return map.get(x);
   }
   public boolean maps(X x) { return map.containsKey(x); }
   public Collection<Y> values() { return map.values(); }
}
