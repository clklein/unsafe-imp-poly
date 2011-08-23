package letref;

public class BoolVal implements Val {
   public boolean val;
   public BoolVal(boolean val) {
      this.val = val;
   }
   public String toString() { return new Boolean(val).toString(); }
}
