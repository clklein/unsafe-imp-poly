package letref;

public interface Type {
   public Type subst(TypeVar x, Type t);
   public List<TypeVar> typeVars();
   public Constraints unify(Type t, List<Equation> es);
   public Constraints unifyVar(Type t, List<Equation> es);
   public String toString();
}
