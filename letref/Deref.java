package letref;

import java.util.HashSet;

public class Deref extends Expr {
   public Expr body;
   public Deref(Expr body) {
      this.body = body;
   }

   public ConstrainedType constrainedType(FinMap<Var, Schema> env,
                                          Constraints initConstrs) {
      ConstrainedType bodyCT = body.constrainedType(env, initConstrs);
      if (bodyCT != null) {
         Type containedType = TypeVar.fresh();
         Constraints constrs = bodyCT.constraints.addEquation(bodyCT.type,
          new RefType(containedType));
         if (constrs != null) {
            return new ConstrainedType(constrs, containedType);
         } else {
            return null;
         }
      } else {
         return null;
      }
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
