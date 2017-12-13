/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package combinedeventstreamtest;

import java.util.Objects;
import java.util.Optional;

/**
 *
 * @author bwhitworth
 */
public abstract class ChangeBase<T> implements UndoChange {
   protected final T oldValue, newValue;
   protected final DataModel model;
   
   protected ChangeBase(DataModel model, T oldValue, T newValue) {
      this.model = model;
      this.oldValue = oldValue;
      this.newValue = newValue;
   }
   
   public abstract ChangeBase<T> invert();
   public abstract void redo();
   
   public Optional<ChangeBase<?>> mergeWith(ChangeBase<?> other) {
      return Optional.empty();
   }

   @Override
   public int hashCode() {
      return Objects.hash(this.oldValue, this.newValue);
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
      final ChangeBase<?> other = (ChangeBase<?>) obj;
      if (!Objects.equals(this.oldValue, other.oldValue)) {
         return false;
      }
      if (!Objects.equals(this.newValue, other.newValue)) {
         return false;
      }
      if (!Objects.equals(this.model, other.model)) {
         return false;
      }
      return true;
   }

}
