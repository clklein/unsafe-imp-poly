package letref;

public class IntVal implements Val {
   public int val;
   public IntVal(int val) {
      this.val = val;
   }
   public String toString() { return new Integer(val).toString(); }
}
