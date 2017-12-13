package combinedeventstreamtest;

import java.util.Optional;

public interface UndoChange {
   public void redo();
   public UndoChange invert();
   public Optional<UndoChange> mergeWith(UndoChange other);
   
}
