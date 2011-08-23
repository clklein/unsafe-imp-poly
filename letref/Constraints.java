package letref;

public class Constraints {
   public List<Assignment> assignments;
   public Constraints(List<Assignment> assignments) {
      this.assignments = assignments;
   }

   public Constraints addEquation(Type t, Type u) throws NoType {
      return t.unify(u, Assignment.asEquations(assignments));
   }

   public Schema generalize(FinMap<Var, Schema> env, Type type) {
      Type subd = Assignment.apply(assignments, type);
      List<TypeVar> outer = null;
      for (Schema s : env.values()) {
         outer = List.append(Assignment.apply(assignments, s.body).typeVars(), 
          outer);
      }

      List<TypeVar> generalizable = List.removeAll(subd.typeVars(), outer);
      return new Schema(generalizable, subd);
   }

   public static Constraints unify(List<Equation> equations) throws NoType {
      if (equations == null) {
         return new Constraints(null);
      } else {
         return equations.first.lhs.unify(equations.first.rhs, equations.rest);
      }
   }

   public static List<Equation> subst(TypeVar x, Type t, List<Equation> es) {
      if (es == null) {
         return null;
      } else {
         Type lhs = es.first.lhs.subst(x, t);
         Type rhs = es.first.rhs.subst(x, t);
         return List.cons(new Equation(lhs, rhs), subst(x, t, es.rest));
      }
   }
}
