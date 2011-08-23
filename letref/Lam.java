package letref;

import java.util.HashSet;

public class Lam extends Expr {
   public Var param;
   public Expr body;
   public Lam(Var param, Expr body) {
      this.param = param;
      this.body = body;
   }

   public ConstrainedType constrainedType(FinMap<Var, Schema> env,
    Constraints constrs) throws NoType {
      Type domTy = TypeVar.fresh();
      Schema paramSchema = new Schema(null, domTy);
      ConstrainedType CT = body.constrainedType(env.extend(param, paramSchema),
       constrs);
      return new ConstrainedType(CT.constraints, new FunType(domTy, CT.type));
   }

   public Val eval(FinMap<Var, Val> env) {
      return new Closure(env, param, body);
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
      return "(lam (" + param + ") " + body + ")";
   }
}
