package letref;

import java.util.HashSet;

public class Set extends Expr {
   public Expr lhs;
   public Expr rhs;
   public Set(Expr lhs, Expr rhs) {
      this.lhs = lhs;
      this.rhs = rhs;
   }

   public ConstrainedType constrainedType(FinMap<Var, Schema> env,
    Constraints initConstrs) throws NoType {
      ConstrainedType lhsCT = lhs.constrainedType(env, initConstrs);
      ConstrainedType rhsCT = rhs.constrainedType(env, lhsCT.constraints);
      Constraints constrs = rhsCT.constraints.addEquation(lhsCT.type,
       new RefType(rhsCT.type));
      return new ConstrainedType(constrs, new IntType());
   }

   public Val eval(FinMap<Var, Val> env) {
      ((RefVal)lhs.eval(env)).cell.contents = rhs.eval(env);
      return new IntVal(0);
   }

   public boolean sharingOK(HashSet<Expr> seen) {
      if (seen.contains(this)) {
         return false;
      } else {
         seen.add(this);
         return lhs.sharingOK(seen) && rhs.sharingOK(seen);
      }
   }

   public String toString() {
      return "(" + lhs + " := " + rhs + ")";
   }
}
