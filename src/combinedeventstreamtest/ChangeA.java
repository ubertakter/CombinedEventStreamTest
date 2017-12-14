package combinedeventstreamtest;

import java.util.Optional;
import org.reactfx.Change;

public class ChangeA extends ChangeBase<Double> {

   public ChangeA(DataModel model, double oldVal, double newVal) {
      super(model, oldVal, newVal);
   }
   
   public ChangeA(DataModel model, Change<Number> c) {      
      super(model, (Double)c.getOldValue(), (Double)c.getNewValue());
   }
   
   @Override
   public ChangeA invert() {
      System.out.println("ChangeA invert "+this);
      return new ChangeA(this.model, this.newValue, this.oldValue);
   }

   @Override
   public void redo() {
      System.out.println("ChangeA redo "+this);
      this.model.setA(this.newValue);
   }

   @Override
   public Optional<UndoChange> mergeWith(UndoChange other) {
      System.out.print("ChangeA attempting merge with "+other+"... ");
      if(other instanceof ChangeA) {
         System.out.println("merged");
         return Optional.of(new ChangeA(this.model, this.oldValue, ((ChangeA) other).newValue));
      }
      System.out.println("did not merge");
      return Optional.empty();
   }      
}
