package letref;

import java.util.HashSet;

public class Var extends Expr implements Comparable<Var> {
   public String name;
   public Var(String name) {
      this.name = name;
   }

   public ConstrainedType constrainedType(FinMap<Var, Schema> env,
                                          Constraints constrs) {
      if (env.maps(this)) {
         Type instantiatedType = env.lookup(this).freshInstance();
         return new ConstrainedType(constrs, instantiatedType);
      } else {
         return null;
      }
   }

   public int compareTo(Var another) {
      return name.compareTo(another.name);
   }

   public Val eval(FinMap<Var, Val> env) {
      return env.lookup(this);
   }

   public boolean sharingOK(HashSet<Expr> seen) { return true; }

   public String toString() { return name; }
}
