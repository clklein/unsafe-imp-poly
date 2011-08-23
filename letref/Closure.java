package letref;

public class Closure implements Val {
   public FinMap<Var, Val> env;
   public Var param;
   public Expr body;
   public Closure(FinMap<Var, Val> env, Var param, Expr body) {
      this.env = env;
      this.param = param;
      this.body = body;
   }
   public String toString() { return "proc"; }
}
