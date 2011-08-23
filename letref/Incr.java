package letref;

import java.util.HashSet;

public class Incr extends Expr {
   public Expr body;
   public Incr(Expr body) {
      this.body = body;
   }

   public ConstrainedType constrainedType(FinMap<Var, Schema> env,
                                          Constraints initConstrs) {
      ConstrainedType bodyCT = body.constrainedType(env, initConstrs);
      if (bodyCT != null) {
         Constraints constrs = bodyCT.constraints.addEquation(new IntType(),
          bodyCT.type);
         if (constrs != null) {
            return new ConstrainedType(constrs, new IntType());
         } else {
            return null;
         }
      } else {
         return null;
      }
   }

   public Val eval(FinMap<Var, Val> env) {
      return new IntVal(1 + ((IntVal)body.eval(env)).val);
   }

   public boolean sharingOK(HashSet<Expr> seen) {
      if (seen.contains(this)) {
         return false;
      } else {
         seen.add(this);
         return body.sharingOK(seen);
      }
   }

   public String toString() {
      return "(+1 " + body + ")";
   }
}
