package combinedeventstreamtest;

import java.util.Objects;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class DataModel {
   private DoubleProperty a, b;
   
   public DataModel() {
      this.a = new SimpleDoubleProperty();
      this.b = new SimpleDoubleProperty();
   }
   
   public DoubleProperty aProperty() {
      return this.a;
   }
   
   public DoubleProperty bProperty() {
      return this.b;
   }
   
   public void setA(double a) {
      this.a.set(a);
   }
   
   public void setB(double b) {
      this.b.set(b);
   }

   @Override
   public String toString() {
      return this.a.doubleValue()+"\t"+this.b.doubleValue();
   }

   @Override
   public int hashCode() {
      int hash = 7;
      hash = 67 * hash + Objects.hashCode(this.a);
      hash = 67 * hash + Objects.hashCode(this.b);
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
      final DataModel other = (DataModel) obj;
      if (!Objects.equals(this.a, other.a)) {
         return false;
      }
      if (!Objects.equals(this.b, other.b)) {
         return false;
      }
      return true;
   }

}
