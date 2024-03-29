package letref;

public class TypeVar implements Type {
   public String name;
   public TypeVar(String name) {
      this.name = name;
   }

   public Type subst(TypeVar x, Type t) {
      if (name.equals(x.name)) {
         return t;
      } else {
         return this;
      }
   }

   public List<TypeVar> typeVars() {
      return List.cons(this, null);
   }

   public Constraints unify(Type t, List<Equation> es) throws NoType {
      if (List.member(this, t.typeVars())) {
         throw new NoType();
      } else {
         Constraints rest = Constraints.unify(
          Constraints.subst(this, t, es));
         return new Constraints(List.cons(new Assignment(this, t), 
          rest.assignments));
      }
   }

   public Constraints unifyVar(Type t, List<Equation> es) throws NoType {
      return unify(t, es);
   }

   public String toString() {
      return name;
   }

   private static int firstStamp = 0;
   private static int nextStamp = firstStamp;
   public static TypeVar fresh() {
      return new TypeVar("x" + nextStamp++);
   }
   public static void reset() {
      nextStamp = firstStamp;
   }
}
