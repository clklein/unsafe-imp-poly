package letref;

public class FunType implements Type {
   public Type domain;
   public Type codomain;
   public FunType(Type domain, Type codomain) {
      this.domain = domain;
      this.codomain = codomain;
   }

   public Type subst(TypeVar x, Type t) {
      return new FunType(domain.subst(x, t), codomain.subst(x, t));
   };

   public List<TypeVar> typeVars() {
      List<TypeVar> domTVs = domain.typeVars();
      List<TypeVar> codomTVs = codomain.typeVars();
      return List.append(domain.typeVars(), List.removeAll(codomTVs, domTVs));
   }

   public Constraints unify(Type t, List<Equation> es) throws NoType {
      if (t.getClass().equals(FunType.class)) {
         FunType f = (FunType)t;
         Constraints unifiedDom = domain.unify(f.domain, es);
         return codomain.unify(f.codomain, 
          Assignment.asEquations(unifiedDom.assignments));
      } else {
         return t.unifyVar(this, es);
      }
   }

   public Constraints unifyVar(Type t, List<Equation> es) throws NoType {
      throw new NoType();
   }

   public String toString() {
      return "(" + domain + " -> " + codomain + ")";
   }
}
