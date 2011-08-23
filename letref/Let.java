package letref;

import java.util.HashSet;

public class Let extends Expr {
   public Var lhs;
   public Expr rhs;
   public Expr body;
   public Let(Var lhs, Expr rhs, Expr body) {
      this.lhs = lhs;
      this.rhs = rhs;
      this.body = body;
   }

   public ConstrainedType constrainedType(FinMap<Var, Schema> env,
    Constraints initConstrs) throws NoType {
      ConstrainedType rhsCT = rhs.constrainedType(env, initConstrs);
      Schema rhsSchema = rhsCT.constraints.generalize(env, rhsCT.type);
      return body.constrainedType(env.extend(lhs, rhsSchema), 
       rhsCT.constraints);
   }

   public Val eval(FinMap<Var, Val> env) {
      return body.eval(env.extend(lhs, rhs.eval(env)));
   }

   public boolean sharingOK(HashSet<Expr> seen) {
      if (seen.contains(this)) {
         return false;
      } else {
         seen.add(this);
         return rhs.sharingOK(seen) && body.sharingOK(seen);
      }
   }

   public String toString() {
      return "(let (" + lhs + " " + rhs + ")" + body + ")";
   }
}
