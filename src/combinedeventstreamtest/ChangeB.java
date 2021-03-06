package combinedeventstreamtest;

import java.util.Optional;
import org.reactfx.Change;

public class ChangeB extends ChangeBase<Double> {

   public ChangeB(DataModel model, double oldVal, double newVal) {
      super(model, oldVal, newVal);
   }
   
   public ChangeB(DataModel model, Change<Number> c) {      
      super(model, (Double)c.getOldValue(), (Double)c.getNewValue());
   }
   
   @Override
   public ChangeB invert() {
      System.out.println("ChangeB invert "+this);
      return new ChangeB(this.model, this.newValue, this.oldValue);
   }

   @Override
   public void redo() {
      System.out.println("ChangeB redo "+this);
      this.model.setB(this.newValue);
   }

   @Override
   public Optional<UndoChange> mergeWith(UndoChange other) {
      System.out.print("ChangeB attempting merge with "+other+"... ");
      if(other instanceof ChangeB) {
         System.out.println("merged");
         return Optional.of(new ChangeB(this.model, this.oldValue, ((ChangeB) other).newValue));
      }
      System.out.println("did not merge");
      return Optional.empty();
   }      
   
}
