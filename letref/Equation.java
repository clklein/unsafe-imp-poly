package letref;

public class Equation {
   public Type lhs;
   public Type rhs;
   public Equation(Type lhs, Type rhs) {
      this.lhs = lhs;
      this.rhs = rhs;
   }
   public String toString() {
      return lhs + " = " + rhs;
   }
}
