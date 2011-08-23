package letref;

import java.util.HashSet;

public class Int extends Expr {
   public int val;
   public Int(int val) {
      this.val = val;
   }

   public ConstrainedType constrainedType(FinMap<Var, Schema> env,
    Constraints constrs) throws NoType {
      return new ConstrainedType(constrs, new IntType());
   }

   public Val eval(FinMap<Var, Val> env) { return new IntVal(val); }

   public boolean sharingOK(HashSet<Expr> seen) { return true; }

   public String toString() { return new Integer(val).toString(); }
}
