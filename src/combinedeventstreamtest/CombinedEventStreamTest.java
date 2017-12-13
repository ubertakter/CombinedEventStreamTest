package combinedeventstreamtest;

import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
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
      //combine event streams
      EventStream<UndoChange> bothStream = EventStreams.merge(changeAStream, changeBStream);    
      
//      EventStream<UndoChange> bothStream = EventStreams.combine(changeAStream, changeBStream)
//              .hook(c -> System.out.println("Change in Both stream"))
//              .map(ChangeBoth::new);   
      
      //undo manager
      UndoManager<UndoChange> um = UndoManagerFactory.unlimitedHistoryUndoManager(
              bothStream, 
              c -> c.invert(), 
              c -> c.redo(),
              (c1, c2) -> c1.mergeWith(c2)
      );

      //generate new A
      Button aButton = new Button();
      aButton.setText("A change");
      aButton.setOnAction((ActionEvent event) -> {
         System.out.print(model+"\t->\t");
         model.setA(Math.random()*10.0);
         System.out.println(model);
      });
      
      //generate new B
      Button bButton = new Button();
      bButton.setText("B change");
      bButton.setOnAction((ActionEvent event) -> {
         System.out.print(model+"\t->\t");
         model.setB(Math.random()*10.0);
         System.out.println(model);
      });
      
      //generate new A and B
      Button bothButton = new Button("A and B change");
      bothButton.setOnAction(event -> {
         System.out.print(model+"\t->\t");
         model.setA(Math.random()*10.0);
         model.setB(Math.random()*10.0);
         System.out.println(model);         
      });

      //undo/redo buttons
      Button undoButton = new Button("Undo");
      Button redoButton = new Button("Redo");
      undoButton.disableProperty().bind(um.undoAvailableProperty().map(x -> !x));
      redoButton.disableProperty().bind(um.redoAvailableProperty().map(x -> !x));
      undoButton.setOnAction(event -> {
         System.out.print("undo "+model+"\t->\t");         
         um.undo();
         System.out.println(model);
      });
      redoButton.setOnAction(event -> {
         System.out.print("redo "+model+"\t->\t");                  
         um.redo();
         System.out.println(model);
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
