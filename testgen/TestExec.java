package testgen;

import letref.*;
import korat.testing.*;

public class TestExec implements ITestCaseListener {
   public void notifyNewTestCase(Object testInput) {
      Expr expr = ((TestInput)testInput).expr;
      try {
         expr.valueOf();
      } catch (RuntimeException e) {
         System.out.println(expr + " raises an exception");
         throw e;
      }
   }

   public void notifyTestFinished(long expored, long generated) {}
}
