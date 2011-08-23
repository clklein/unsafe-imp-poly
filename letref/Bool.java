package letref;

import java.util.HashSet;

public class Bool extends Expr {
   public boolean val;
   public Bool(boolean val) {
      this.val = val;
   }

   public ConstrainedType constrainedType(FinMap<Var, Schema> env,
    Constraints constrs) throws NoType {
      return new ConstrainedType(constrs, new BoolType());
   }

   public Val eval(FinMap<Var, Val> env) { return new BoolVal(val); }

   public boolean sharingOK(HashSet<Expr> seen) { return true; }

   public String toString() { return new Boolean(val).toString(); }
}
