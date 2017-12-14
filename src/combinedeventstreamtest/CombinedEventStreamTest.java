package combinedeventstreamtest;

import java.util.function.BinaryOperator;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import org.fxmisc.undo.UndoManager;
import org.fxmisc.undo.UndoManagerFactory;
import org.reactfx.Change;
import org.reactfx.EventStream;
import org.reactfx.EventStreams;
import org.reactfx.SuspendableEventStream;

public class CombinedEventStreamTest extends Application {
   
   @Override
   public void start(Stage primaryStage) {
      //a data model
      DataModel model = new DataModel();
      //event streams of property changes
      EventStream<UndoChange> changeAStream = EventStreams.changesOf(model.aProperty())
              .hook(c -> System.out.println("Change in A stream"))
              .map(c -> new ChangeA(model, (Change<Number>)c));
      EventStream<UndoChange> changeBStream = EventStreams.changesOf(model.bProperty())
              .hook(c -> System.out.println("Change in B stream"))
              .map(c -> new ChangeB(model, (Change<Number>)c));
      BinaryOperator<UndoChange> abOp = (c1, c2) -> {
         ChangeA ca = null;
         if(c1 instanceof ChangeA) {
            ca = (ChangeA)c1;
         }
         ChangeB cb = null;
         if(c2 instanceof ChangeB) {
            cb = (ChangeB)c2;
         }
         return new ChangeBoth(ca, cb);
      };
      //combine event streams
      SuspendableEventStream<UndoChange> bothStream = EventStreams.merge(changeAStream, changeBStream).reducible(abOp);    
      
      //undo manager
      UndoManager<UndoChange> um = UndoManagerFactory.unlimitedHistoryUndoManager(
              bothStream, 
              c -> c.invert(), 
              c -> bothStream.suspendWhile(c::redo),
              (c1, c2) -> c1.mergeWith(c2)
      );

      //generate new A
      Button aButton = new Button();
      aButton.setText("A change");
      aButton.setOnAction((ActionEvent event) -> {
         System.out.println("A change button action: "+model);
         model.setA(Math.random()*10.0);
      });
      
      //generate new B
      Button bButton = new Button();
      bButton.setText("B change");
      bButton.setOnAction((ActionEvent event) -> {
         System.out.println("B change button action: "+model);
         model.setB(Math.random()*10.0);
      });
      
      //generate new A and B
      Button bothButton = new Button("A and B change");
//      bothButton.setOnAction(event -> {
//         System.out.println("A+B change button action: "+model);
//      });
      EventStreams.eventsOf(bothButton, ActionEvent.ACTION)
              .suspenderOf(bothStream)
              .subscribe((ActionEvent event) ->{
                 System.out.println("A+B Button Action in event stream");
                  model.setA(Math.random()*10.0);
                  model.setB(Math.random()*10.0);                 
              });
              
      //undo/redo buttons
      Button undoButton = new Button("Undo");
      Button redoButton = new Button("Redo");
      undoButton.disableProperty().bind(um.undoAvailableProperty().map(x -> !x));
      redoButton.disableProperty().bind(um.redoAvailableProperty().map(x -> !x));
      undoButton.setOnAction(event -> {
         System.out.println("undo "+model);          
         um.undo();
      });
      redoButton.setOnAction(event -> {
         System.out.println("redo "+model);                  
         um.redo();
      });
      
      FlowPane root = new FlowPane();
      root.setHgap(5);
      root.setVgap(5);
      root.getChildren().addAll(aButton, bButton, bothButton, undoButton, redoButton);
      
      Scene scene = new Scene(root, 300, 250);
      
      primaryStage.setTitle("Combined stream test");
      primaryStage.setScene(scene);
      primaryStage.show();
   }

   /**
    * @param args the command line arguments
    */
   public static void main(String[] args) {
      launch(args);
   }
   
}
