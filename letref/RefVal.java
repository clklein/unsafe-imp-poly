package letref;

public class RefVal implements Val {
   public final Cell cell;
   public RefVal(Val init) {
      this.cell = new Cell(init);
   }

   public String toString() { return "ref"; }

   public static class Cell {
      public Val contents;
      public Cell(Val contents) {
         this.contents = contents;
      }
   }
}
