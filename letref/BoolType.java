package letref;

public class BoolType implements Type {
   public Type subst(TypeVar x, Type t) {
      return this;
   }

   public List<TypeVar> typeVars() {
      return null;
   }

   public Constraints unify(Type t, List<Equation> es) throws NoType {
      if (t.getClass().equals(BoolType.class)) {
         return Constraints.unify(es);
      } else {
         return t.unifyVar(this, es);
      }
   }

   public Constraints unifyVar(Type t, List<Equation> es) throws NoType {
      throw new NoType();
   }

   public String toString() {
      return "Bool";
   }
}
