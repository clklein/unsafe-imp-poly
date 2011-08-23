package letref;

import java.util.HashSet;

public class App extends Expr {
   public Expr rator;
   public Expr rand;
   public App(Expr rator, Expr rand) {
      this.rator = rator;
      this.rand = rand;
   }

   public ConstrainedType constrainedType(FinMap<Var, Schema> env,
    Constraints initConstrs) throws NoType {
      ConstrainedType ratorCT = rator.constrainedType(env, initConstrs);
      ConstrainedType randCT = rand.constrainedType(env, ratorCT.constraints);
      Type resultType = TypeVar.fresh();
      Constraints constrs = randCT.constraints.addEquation(ratorCT.type, 
       new FunType(randCT.type, resultType));
      return new ConstrainedType(constrs, resultType);
   }

   public Val eval(FinMap<Var, Val> env) {
      Closure clo = (Closure)rator.eval(env);
      return clo.body.eval(clo.env.extend(clo.param, rand.eval(env)));
   }

   public boolean sharingOK(HashSet<Expr> seen) {
      if (seen.contains(this)) {
         return false;
      } else {
         seen.add(this);
         return rator.sharingOK(seen) && rand.sharingOK(seen);
      }
   }

   public String toString() {
      return "(" + rator + " " + rand + ")";
   }
}
