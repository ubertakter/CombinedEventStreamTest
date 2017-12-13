package combinedeventstreamtest;

import java.util.Objects;
import java.util.Optional;
import org.reactfx.util.Tuple2;

public class ChangeBoth implements UndoChange {

   private final ChangeA aChange;
   private final ChangeB bChange;
   
   public ChangeBoth(ChangeA ac, ChangeB bc) {
      this.aChange = ac;
      this.bChange = bc;
   }
   
   public ChangeBoth(Tuple2<UndoChange, UndoChange> tuple) {
      this.aChange = ((ChangeBoth)tuple.get1()).aChange;
      this.bChange = ((ChangeBoth)tuple.get2()).bChange;
   }
   
   @Override
   public UndoChange invert() {
      System.out.println("ChangeBoth invert "+this);
      return new ChangeBoth(new ChangeA(this.aChange.model, this.aChange.newValue, this.aChange.oldValue), 
                            new ChangeB(this.bChange.model, this.bChange.newValue, this.bChange.oldValue));
   }

   @Override
   public void redo() {
      System.out.println("ChangeBoth redo "+this);
      DataModel model = this.aChange.model;
      model.setA(this.aChange.newValue);
      model.setB(this.bChange.newValue);
   }

   @Override
   public Optional<UndoChange> mergeWith(UndoChange other) {
      System.out.print("ChangeBoth attempting merge with "+other+"... ");
      if(other instanceof ChangeBoth) {
         System.out.println("merged");
         ChangeBoth cb = (ChangeBoth)other;
         ChangeA ac = (cb.aChange == null) ? this.aChange : cb.aChange;
         ChangeB bc = (cb.bChange == null) ? this.bChange : cb.bChange;
         return Optional.of(
                 new ChangeBoth(
                         new ChangeA(this.aChange.model, this.aChange.oldValue, ac.newValue),
                         new ChangeB(this.bChange.model, this.bChange.oldValue, bc.newValue)
                 )
         );
      }
      System.out.println("did not merge");
      return Optional.empty();
   }   

   @Override
   public int hashCode() {
      int hash = 7;
      hash = 17 * hash + Objects.hashCode(this.aChange);
      hash = 17 * hash + Objects.hashCode(this.bChange);
      return hash;
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      }
      if (obj == null) {
         return false;
      }
      if (getClass() != obj.getClass()) {
         return false;
      }
      final ChangeBoth other = (ChangeBoth) obj;
      if (!Objects.equals(this.aChange, other.aChange)) {
         return false;
      }
      if (!Objects.equals(this.bChange, other.bChange)) {
         return false;
      }
      return true;
   }   
}
