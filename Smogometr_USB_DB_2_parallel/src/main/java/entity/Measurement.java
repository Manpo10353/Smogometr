package entity;

import jakarta.persistence.*;

@Entity
@Table(name = "measurements", schema = "smogometr", catalog = "")
public class Measurement {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "measurement_id")
    private int idsmogometrDb;
    @Basic
    @Column(name = "value")
    private double value;

    public Measurement(double value) {
        this.value = value;
    }

    public int getIdsmogometrDb() {
        return idsmogometrDb;
    }

    public void setIdsmogometrDb(int idsmogometrDb) {
        this.idsmogometrDb = idsmogometrDb;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Measurement that = (Measurement) o;

        if (idsmogometrDb != that.idsmogometrDb) return false;
        if (Double.compare(that.value, value) != 0) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = idsmogometrDb;
        temp = Double.doubleToLongBits(value);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
