package letref;

import java.util.HashSet;

public abstract class Expr {
   public abstract ConstrainedType constrainedType(FinMap<Var, Schema> env,
    Constraints constrs) throws NoType;
   public abstract Val eval(FinMap<Var, Val> env);
   public abstract boolean sharingOK(HashSet<Expr> seen);
   public abstract String toString();

   public Schema typeOf() {
      TypeVar.reset();
      FinMap<Var, Schema> topLevEnv = new FinMap<Var, Schema>();
      try {
         ConstrainedType CT = constrainedType(topLevEnv, new Constraints(null));
         return CT.constraints.generalize(topLevEnv, CT.type);
      } catch (NoType exn) {
         return null;
      }
   }

   public Val valueOf() {
      return eval(new FinMap<Var, Val>());
   }
}
