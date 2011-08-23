package letref;

import java.util.HashSet;

public class Deref extends Expr {
   public Expr body;
   public Deref(Expr body) {
      this.body = body;
   }

   public ConstrainedType constrainedType(FinMap<Var, Schema> env,
    Constraints initConstrs) throws NoType {
      ConstrainedType bodyCT = body.constrainedType(env, initConstrs);
      Type containedType = TypeVar.fresh();
      Constraints constrs = bodyCT.constraints.addEquation(bodyCT.type,
       new RefType(containedType));
      return new ConstrainedType(constrs, containedType);
   }

   public Val eval(FinMap<Var, Val> env) {
      return ((RefVal)body.eval(env)).cell.contents;
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
      return "(! " + body + ")";
   }
}
