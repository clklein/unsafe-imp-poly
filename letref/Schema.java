package letref;

public class Schema {
   public List<TypeVar> quantified;
   public Type body;
   public Schema(List<TypeVar> quantified, Type body) {
      this.quantified = quantified;
      this.body = body;
   }

   public Type freshInstance() {
      return instantiate(quantified, body);
   }

   public String toString() {
      String s = "forall ";
      List<TypeVar> xs = quantified;
      while (xs != null) {
         s += xs.first;
         if (xs.rest != null) s += ", ";
         xs = xs.rest;
      }
      return s + ". " + body;
   }

   private static Type instantiate(List<TypeVar> xs, Type t) {
      if (xs == null) {
         return t;
      } else {
         return instantiate(xs.rest, t.subst(xs.first, TypeVar.fresh()));
      }
   }
}
