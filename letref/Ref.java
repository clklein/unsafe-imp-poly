package letref;

import java.util.HashSet;

public class Ref extends Expr {
   public Expr body;
   public Ref(Expr body) {
      this.body = body;
   }

   public ConstrainedType constrainedType(FinMap<Var, Schema> env,
    Constraints initConstrs) throws NoType {
      ConstrainedType bodyCT = body.constrainedType(env, initConstrs);
      return new ConstrainedType(bodyCT.constraints, new RefType(bodyCT.type));
   }

   public Val eval(FinMap<Var, Val> env) {
      return new RefVal(body.eval(env));
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
      return "(ref " + body + ")";
   }
}
