package testgen;

import letref.*;
import korat.finitization.*;
import korat.finitization.impl.FinitizationFactory;
import java.util.HashSet;

public class TestInput {
   public Expr expr;

   public boolean repOK() {
      return expr.sharingOK(new HashSet<Expr>()) && expr.typeOf() != null;
   }

   public static IFinitization finTestInput(int numVar, int numInt, 
    String numOthers) {
      ObjectBound objBound = parseObjectBound(numOthers);
      IFinitization fin = FinitizationFactory.create(TestInput.class);

      IClassDomain varClsDom = varClassDomain(fin, numVar);
      IFieldDomain varFldDom = fin.createObjSet(varClsDom);
      ((IObjSet)varFldDom).setNullAllowed(false); // work around Korat bug

      IObjSet exprs = fin.createObjSet(Expr.class);
      exprs.setNullAllowed(false); // work around same Korat bug
      fin.set("expr", exprs);

      // integer
      exprs.addClassDomain(intClassDomain(fin, numInt));

      // boolean
      exprs.addClassDomain(boolClassDomain(fin));

      // variable
      exprs.addClassDomain(varClsDom);

      // application
      exprs.addClassDomain(fin.createClassDomain(App.class,
       objBound.bound(App.class)));
      fin.set("letref.App.rator", exprs);
      fin.set("letref.App.rand", exprs);

      // deref
      exprs.addClassDomain(fin.createClassDomain(Deref.class, 
       objBound.bound(Deref.class)));
      fin.set("letref.Deref.body", exprs);

      // increment
      exprs.addClassDomain(fin.createClassDomain(Incr.class, 
       objBound.bound(Incr.class)));
      fin.set("letref.Incr.body", exprs);

      // lambda
      exprs.addClassDomain(fin.createClassDomain(Lam.class, 
       objBound.bound(Lam.class)));
      fin.set("letref.Lam.param", varFldDom);
      fin.set("letref.Lam.body", exprs);

      // let
      exprs.addClassDomain(fin.createClassDomain(Let.class, 
       objBound.bound(Let.class)));
      fin.set("letref.Let.lhs", varFldDom);
      fin.set("letref.Let.rhs", exprs);
      fin.set("letref.Let.body", exprs);

      // ret
      exprs.addClassDomain(fin.createClassDomain(Ref.class, 
       objBound.bound(Ref.class)));
      fin.set("letref.Ref.body", exprs);

      // assignment
      exprs.addClassDomain(fin.createClassDomain(Set.class, 
       objBound.bound(Set.class)));
      fin.set("letref.Set.lhs", exprs);
      fin.set("letref.Set.rhs", exprs);

      // sequencing
      exprs.addClassDomain(fin.createClassDomain(Seq.class, 
       objBound.bound(Seq.class)));
      fin.set("letref.Seq.first", exprs);
      fin.set("letref.Seq.second", exprs);

      return fin;
   }

   public static IClassDomain varClassDomain(IFinitization fin, int numVar) {
      IClassDomain dom = fin.createClassDomain(Var.class);
      Var[] vars = new Var[numVar];
      for (int i = 0; i < numVar; i++) {
         vars[i] = new Var("x" + i);
      }
      dom.addObjects(vars);
      return dom;
   }

   public static IClassDomain intClassDomain(IFinitization fin, int numInt) {
      IClassDomain dom = fin.createClassDomain(Int.class);
      Int[] ints = new Int[numInt];
      for (int i = 0; i < numInt; i++) {
         ints[i] = new Int(i);
      }
      dom.addObjects(ints);
      return dom;
   }

   public static IClassDomain boolClassDomain(IFinitization fin) {
      IClassDomain dom = fin.createClassDomain(Bool.class);
      Bool[] bools = {new Bool(true), new Bool(false)};
      dom.addObjects(bools);
      return dom;
   }

   public static ObjectBound parseObjectBound(String arg) {
      try {
         return new UniformBound(Integer.parseInt(arg));
      } catch (NumberFormatException nfe) {
         try {
            String[] pieces = arg.split(":"); 
            if ((pieces.length) != 3) {
               throw new IllegalArgumentException();
            }
            return new DistinguishedBound(Class.forName(pieces[0]),
             Integer.parseInt(pieces[1]),
             Integer.parseInt(pieces[2]));
         } catch (NumberFormatException dnfe) {
            throw new IllegalArgumentException();
         } catch (ClassNotFoundException cnfe) {
            throw new IllegalArgumentException();
         }
      }
   }

   public static interface ObjectBound {
      public int bound(Class c);
   }

   public static class UniformBound implements ObjectBound {
      private int bound;
      public UniformBound(int bound) { this.bound = bound; }
      public int bound(Class c) { return bound; }
   }

   public static class DistinguishedBound implements ObjectBound {
      private Class distinguished;
      private int distBound;
      private int undistBound;
      public DistinguishedBound(Class distinguished, int distBound,
       int undistBound) {
         this.distinguished = distinguished;
         this.distBound = distBound;
         this.undistBound = undistBound;
      }
      public int bound(Class c) {
         return c.equals(distinguished) ? distBound : undistBound;
      }
   }
}
