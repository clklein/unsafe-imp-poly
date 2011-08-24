package letref;

public class Tests {
   public static void main(String[] args) {
      testTyping();
      testEvaluation();
   }

   public static Expr id = Lam(Var("x"), Var("x"));

   public static Expr unsafeExpr =
    Let(Var("x"),
        Ref(id),
        Seq(Set(Var("x"),
                Lam(Var("x"), Bool(true))),
                Incr(App(Deref(Var("x")), Int(2)))));

   public static void testTyping() {
      testType(Int(7), "forall . Int");
      testNoType(Incr(id));

      testType(Bool(true), "forall . Bool");

      testType(Ref(Int(7)), "forall . Ref(Int)");
      testType(Incr(Deref(Ref(Int(7)))), "forall . Int");
      testType(Set(Ref(Int(7)), Int(8)), "forall . Int");
      testNoType(Set(Ref(Int(7)), id));

      testNoType(Var("x"));

      testType(id, "forall x0. (x0 -> x0)");
      testType(Lam(Var("x"), App(id, Var("x"))),
               "forall x0. (x0 -> x0)");
      testNoType(App(Lam(Var("x"), 
                         App(Int(6), Int(7))),
                     Int(8)));
      testNoType(App(id, App(Int(6), Int(7))));
      testNoType(Lam(Var("f"),
                     App(Lam(Var("_"),
                             App(Var("f"), id)),
                         App(Var("f"), Int(7)))));

      testType(Let(Var("x"), Int(7), 
                   Incr(Var("x"))),
               "forall . Int");
      testNoType(Let(Var("x"), App(Int(7), Int(8)),
                     Var("x")));
      testNoType(Set(Ref(Int(0)),
                         Let(Var("T"), App(Int(2), Int(3)),
                             Int(3))));
      testNoType(Let(Var("x"), Int(7),
                     App(Var("x"), Int(8))));
      testType(Let(Var("f"), id,
                   App(Lam(Var("_"),
                           App(Var("f"), Int(7))),
                       App(Var("f"), Var("f"))
                               )),
               "forall . Int");
      testNoType(Lam(Var("x"), App(Var("x"), Var("x"))));
      testNoType(Lam(Var("x"),
                     Let(Var("f"),  
                         App(Var("x"), Int(0)),
                         Incr(Var("x")))));
      testNoType(App(App(Lam(Var("f"),
                             Lam(Var("x"),
                                 Let(Var("g"),
                                     Var("f"),
                                     App(Var("g"),id)))),
                         Lam(Var("x"),
                             Incr(Var("x")))),
                     Int(0)));
      testType(App(App(Lam(Var("f"),
                           Lam(Var("x"),
                               Let(Var("g"),
                                   Var("f"),
                                   App(Var("g"), 
                                       Var("x"))))),
                       Lam(Var("x"),
                           Incr(Var("x")))),
                   Int(0)),
                "forall . Int");                        
      testType(Lam(Var("r"),
                   Lam(Var("x"),
                       Set(Var("r"), Var("x")))),
               "forall x1. (Ref(x1) -> (x1 -> Int))");
      testType(Lam(Var("r"),
                   Deref(Var("r"))),
               "forall x1. (Ref(x1) -> x1)");
      testNoType(Let(Var("c"), id,
                     Let(Var("c"), Int(1),
                     App(Var("c"), Int(2)))));
      testType(Lam(Var("f"),
                   Let(Var("x"), 
                       App(Var("f"), Int(3)),
                       Var("x"))),
               "forall x1. ((Int -> x1) -> x1)");

      testType(Lam(Var("x"), Lam(Var("x"), Var("x"))),
               "forall x0, x1. (x0 -> (x1 -> x1))");
      testType(Lam(Var("x"), Incr(Var("x"))),
               "forall . (Int -> Int)");
      testType(Lam(Var("x"), 
                   Lam(Var("y"), 
                       App(Var("x"), Var("y")))),
               "forall x1, x2. ((x1 -> x2) -> (x1 -> x2))");
      testNoType(Lam(Var("x"), 
                     Lam(Var("y"), 
                         App(Incr(Var("x")), 
                             Var("y")))));
      testNoType(Lam(Var("x"), 
                     App(Var("x"), 
                         Var("x"))));
      testType(Deref(Ref(Int(0))), 
               "forall . Int");

      testType(Seq(id, Int(0)), 
               "forall . Int");
      testNoType(Seq(App(Int(0), id), Int(0)));
      testType(Lam(Var("f"),
                   Seq(App(Var("f"), Int(0)),
                           Var("f"))),
               "forall x1. ((Int -> x1) -> (Int -> x1))");

      // Intentional bug
      testType(unsafeExpr, "forall . Int");
   }

   public static void testEvaluation() {
      testEval(Incr(Int(0)), "1");
      testEval(Incr(Incr(Int(0))), "2");

      testEval(App(App(Lam(Var("x"),
                           Lam(Var("y"),
                               Var("x"))),
                       Incr(Int(0))),
                   Int(0)),
               "1");

      testEval(Let(Var("x"), Incr(Int(0)),
                   Incr(Var("x"))),
               "2");

      testEval(Let(Var("x"), Ref(Int(0)),
                   Let(Var("_"),
                       Ref(Int(1)),
                       Let(Var("_"),
                           Set(Var("x"),
                               Incr(Deref(Var("x")))),
                           Deref(Var("x"))))),
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

   public static Int Int(int val) {
      return new Int(val);
   }

   public static Deref Deref(Expr body) {
      return new Deref(body);
   }

   public static App App(Expr rator, Expr rand) {
      return new App(rator, rand);
   }

   public static Incr Incr(Expr body) {
      return new Incr(body);
   }

   public static Bool Bool(boolean val) {
      return new Bool(val);
   }

   public static Lam Lam(Var param, Expr body) {
      return new Lam(param, body);
   }

   public static Set Set(Expr lhs, Expr rhs) {
      return new Set(lhs, rhs);
   }

   public static Seq Seq(Expr first, Expr second) {
      return new Seq(first, second);
   }

   public static Ref Ref(Expr body) {
      return new Ref(body);
   }

   public static Var Var(String name) {
      return new Var(name);
   }

   public static Let Let(Var lhs, Expr rhs, Expr body) {
      return new Let(lhs, rhs, body);
   }
}
