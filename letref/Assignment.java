package letref;

public class Assignment {
   public TypeVar lhs;
   public Type rhs;
   public Assignment(TypeVar lhs, Type rhs) {
      this.lhs = lhs;
      this.rhs = rhs;
   }
   public String toString() {
      return asEquation().toString();
   }

   public Equation asEquation() {
      return new Equation(lhs, rhs);
   }
   public static List<Equation> asEquations(List<Assignment> assignments) {
      if (assignments == null) {
         return null;
      } else {
         return List.cons(assignments.first.asEquation(),
          asEquations(assignments.rest));
      }
   }

   public static Type apply(List<Assignment> assignments, Type t) {
      if (assignments == null) {
         return t;
      } else {
         return apply(assignments.rest, 
          t.subst(assignments.first.lhs, assignments.first.rhs));
      }
   }
}
