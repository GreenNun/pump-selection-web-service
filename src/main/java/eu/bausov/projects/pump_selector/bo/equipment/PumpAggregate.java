package eu.bausov.projects.pump_selector.bo.equipment;

import eu.bausov.projects.pump_selector.bo.Parameters;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;

@Entity
@Table(name = "TB_PUMP_AGGREGATES")
@XmlRootElement
public class PumpAggregate extends Equipment {

    private String parameters;

    private Pump pump;
    private Seal seal;
    private Reducer reducer;
    private Motor motor;
    private Coupling coupling;

    @Basic
    public String getParameters() {
        return parameters;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters;
    }

    @ManyToOne(optional = false)
    public Pump getPump() {
        return pump;
    }

    public void setPump(Pump pump) {
        this.pump = pump;
    }

    @ManyToOne(optional = false)
    public Seal getSeal() {
        return seal;
    }

    public void setSeal(Seal seal) {
        this.seal = seal;
    }

    @ManyToOne(optional = false)
    public Reducer getReducer() {
        return reducer;
    }

    public void setReducer(Reducer reducer) {
        this.reducer = reducer;
    }

    @ManyToOne(optional = false)
    public Motor getMotor() {
        return motor;
    }

    public void setMotor(Motor motor) {
        this.motor = motor;
    }

    @ManyToOne(optional = false)
    public Coupling getCoupling() {
        return coupling;
    }

    public void setCoupling(Coupling coupling) {
        this.coupling = coupling;
    }

    @Transient
    public BigDecimal getTotalPrice() {
        BigDecimal totalPrice = new BigDecimal(BigDecimal.ROUND_UNNECESSARY);
        totalPrice = totalPrice.add(pump.getPrice());
        totalPrice = totalPrice.add(reducer.getPrice());
        totalPrice = totalPrice.add(motor.getPrice());
        totalPrice = totalPrice.add(coupling.getPrice());

        // Price * 2
        return totalPrice.multiply(new BigDecimal(2));
    }
}