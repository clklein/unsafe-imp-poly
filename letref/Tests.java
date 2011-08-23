package letref;

public class Tests {
   public static void main(String[] args) {
      testTyping();
      testEvaluation();
   }

   public static Expr id = new Lam(new Var("x"), new Var("x"));

   public static Expr unsafeExpr =
    new Let(new Var("x"),
            new Ref(id),
            new Seq(new Set(new Var("x"),
                            new Lam(new Var("x"), new Bool(true))),
                    new Incr(new App(new Deref(new Var("x")),
                                     new Int(2)))));

   public static void testTyping() {
      testType(new Int(7), "forall . Int");
      testNoType(new Incr(id));

      testType(new Bool(true), "forall . Bool");

      testType(new Ref(new Int(7)), "forall . Ref(Int)");
      testType(new Incr(new Deref(new Ref(new Int(7)))), "forall . Int");
      testType(new Set(new Ref(new Int(7)), new Int(8)), "forall . Int");
      testNoType(new Set(new Ref(new Int(7)), id));

      testNoType(new Var("x"));

      testType(id, "forall x0. (x0 -> x0)");
      testType(new Lam(new Var("x"), new App(id, new Var("x"))),
               "forall x0. (x0 -> x0)");
      testNoType(new App(new Lam(new Var("x"), 
                                 new App(new Int(6), new Int(7))),
                         new Int(8)));
      testNoType(new App(id, new App(new Int(6), new Int(7))));
      testNoType(new Lam(new Var("f"),
                         new App(new Lam(new Var("_"),
                                         new App(new Var("f"), id)),
                                 new App(new Var("f"), new Int(7)))));

      testType(new Let(new Var("x"), new Int(7), 
                       new Incr(new Var("x"))),
               "forall . Int");
      testNoType(new Let(new Var("x"), new App(new Int(7), new Int(8)),
                         new Var("x")));
      testNoType(new Set(new Ref(new Int(0)),
                         new Let(new Var("T"), new App(new Int(2), new Int(3)),
                                 new Int(3))));
      testNoType(new Let(new Var("x"), new Int(7),
                         new App(new Var("x"), new Int(8))));
      testType(new Let(new Var("f"), id,
                       new App(new Lam(new Var("_"),
                                       new App(new Var("f"), new Int(7))),
                               new App(new Var("f"), new Var("f"))
                               )),
               "forall . Int");
      testNoType(new Lam(new Var("x"), new App(new Var("x"), new Var("x"))));
      testNoType(new Lam(new Var("x"),
                         new Let(new Var("f"),  
                                 new App(new Var("x"), new Int(0)),
                                 new Incr(new Var("x")))));
      testNoType(new App(new App(new Lam(new Var("f"),
                                         new Lam(new Var("x"),
                                             new Let(new Var("g"),
                                                     new Var("f"),
                                                     new App(new Var("g"), 
                                                             id)))),
                                 new Lam(new Var("x"),
                                         new Incr(new Var("x")))),
                         new Int(0)));
      testType(new App(new App(new Lam(new Var("f"),
                                       new Lam(new Var("x"),
                                           new Let(new Var("g"),
                                                   new Var("f"),
                                                   new App(new Var("g"), 
                                                           new Var("x"))))),
                               new Lam(new Var("x"),
                                       new Incr(new Var("x")))),
                       new Int(0)),
                "forall . Int");                        
      testType(new Lam(new Var("r"),
                       new Lam(new Var("x"),
                               new Set(new Var("r"), new Var("x")))),
               "forall x1. (Ref(x1) -> (x1 -> Int))");
      testType(new Lam(new Var("r"),
                       new Deref(new Var("r"))),
               "forall x1. (Ref(x1) -> x1)");
      testNoType(new Let(new Var("c"), id,
                         new Let(new Var("c"), new Int(1),
                                 new App(new Var("c"), new Int(2)))));
      testType(new Lam(new Var("f"),
                       new Let(new Var("x"), 
                               new App(new Var("f"), new Int(3)),
                               new Var("x"))),
               "forall x1. ((Int -> x1) -> x1)");

      testType(new Lam(new Var("x"), new Lam(new Var("x"), new Var("x"))),
               "forall x0, x1. (x0 -> (x1 -> x1))");
      testType(new Lam(new Var("x"), new Incr(new Var("x"))),
               "forall . (Int -> Int)");
      testType(new Lam(new Var("x"), 
                       new Lam(new Var("y"), 
                               new App(new Var("x"), new Var("y")))),
               "forall x1, x2. ((x1 -> x2) -> (x1 -> x2))");
      testNoType(new Lam(new Var("x"), 
                         new Lam(new Var("y"), 
                                 new App(new Incr(new Var("x")), 
                                         new Var("y")))));
      testNoType(new Lam(new Var("x"), 
                         new App(new Var("x"), 
                                 new Var("x"))));
      testType(new Deref(new Ref(new Int(0))), 
               "forall . Int");

      testType(new Seq(id, new Int(0)), 
               "forall . Int");
      testNoType(new Seq(new App(new Int(0), id), new Int(0)));
      testType(new Lam(new Var("f"),
                       new Seq(new App(new Var("f"), new Int(0)),
                               new Var("f"))),
               "forall x1. ((Int -> x1) -> (Int -> x1))");

      // Intentional bug
      testType(unsafeExpr, "forall . Int");
   }

   public static void testEvaluation() {
      testEval(new Incr(new Int(0)), "1");
      testEval(new Incr(new Incr(new Int(0))), "2");

      testEval(new App(new App(new Lam(new Var("x"),
                                       new Lam(new Var("y"),
                                               new Var("x"))),
                               new Incr(new Int(0))),
                       new Int(0)),
               "1");

      testEval(new Let(new Var("x"), new Incr(new Int(0)),
                       new Incr(new Var("x"))),
               "2");

      testEval(new Let(new Var("x"), new Ref(new Int(0)),
                       new Let(new Var("_"),
                               new Ref(new Int(1)),
                               new Let(new Var("_"),
                                       new Set(new Var("x"),
                                               new Incr(new Deref(
                                                new Var("x")))),
                                       new Deref(new Var("x"))))),
               "1");

      testStuck(unsafeExpr); 
   }
  
   public static void testStuck(Expr expr) {
      try {
         expr.valueOf();
         fail("not stuck");
      } catch (ClassCastException e) {}
   }

   public static void testType(Expr expr, String expectedType) {
      Schema schema = expr.typeOf();
      if (schema == null || !schema.toString().equals(expectedType)) {
         fail(schema == null ? null : schema.toString());
      }
   }

   public static void testNoType(Expr expr) {
      Schema schema = expr.typeOf();
      if (schema != null) fail(schema.toString());
   }

   public static void testEval(Expr expr, String expectedVal) {
      if (expr.typeOf() == null) {
         fail("no type");
      } else {
         Val val = expr.valueOf();
         if (!expectedVal.equals(val.toString())) {
            fail(expectedVal);
         }
      }
   }

   public static void fail(String msg) {
      throw new RuntimeException("test failure: " + msg);
   }
}
