package letref;

public class RefType implements Type {
   public Type contents;
   public RefType(Type contents) {
      this.contents = contents;
   }

   public Type subst(TypeVar x, Type t) {
      return new RefType(contents.subst(x, t));
   }

   public List<TypeVar> typeVars() {
      return contents.typeVars();
   }

   public Constraints unify(Type t, List<Equation> es) {
      if (t.getClass().equals(RefType.class)) {
         return contents.unify(((RefType) t).contents, es);
      } else {
         return t.unifyVar(this, es);
      }
   }

   public Constraints unifyVar(Type t, List<Equation> es) {
      return null;
   }

   public String toString() {
      return "Ref(" + contents + ")";
   }
}
