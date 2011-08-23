package letref;

import java.util.HashSet;

public class Seq extends Expr {
   public Expr first;
   public Expr second;
   public Seq(Expr first, Expr second) {
      this.first = first;
      this.second = second;
   }

   public ConstrainedType constrainedType(FinMap<Var, Schema> env,
                                          Constraints initConstrs) {
      ConstrainedType firstCT = first.constrainedType(env, initConstrs);
      if (firstCT != null) {
         return second.constrainedType(env, firstCT.constraints);
      } else {
         return null;
      }
   }

   public Val eval(FinMap<Var, Val> env) {
      first.eval(env);
      return second.eval(env);
   }

   public boolean sharingOK(HashSet<Expr> seen) {
      if (seen.contains(this)) {
         return false;
      } else {
         seen.add(this);
         return first.sharingOK(seen) && second.sharingOK(seen);
      }
   }

   public String toString() {
      return "(seq " + first + " " + second + ")";
   }
}
