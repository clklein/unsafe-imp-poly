package letref;

public class ConstrainedType {
   public Constraints constraints;
   public Type type;
   public ConstrainedType(Constraints constraints, Type type) {
      this.constraints = constraints;
      this.type = type;
   }
}
