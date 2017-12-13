/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package combinedeventstreamtest;

import java.util.Optional;

/**
 *
 * @author bwhitworth
 */
public interface UndoChange {
   public void redo();
   public UndoChange invert();
   public Optional<UndoChange> mergeWith(UndoChange other);
   
}
