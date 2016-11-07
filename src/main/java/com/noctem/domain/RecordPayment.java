package com.noctem.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A RecordPayment.
 */
@Entity
@Table(name = "record_payment")
public class RecordPayment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "pay", nullable = false)
    private Double pay;

    @NotNull
    @Column(name = "accept_payment", nullable = false)
    private Boolean acceptPayment;

    @ManyToOne
    private Card card;

    @ManyToOne
    private Record record;

    @ManyToOne
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getPay() {
        return pay;
    }

    public void setPay(Double pay) {
        this.pay = pay;
    }

    public Boolean isAcceptPayment() {
        return acceptPayment;
    }

    public void setAcceptPayment(Boolean acceptPayment) {
        this.acceptPayment = acceptPayment;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public Record getRecord() {
        return record;
    }

    public void setRecord(Record record) {
        this.record = record;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RecordPayment recordPayment = (RecordPayment) o;
        if(recordPayment.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, recordPayment.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "RecordPayment{" +
            "id=" + id +
            ", pay='" + pay + "'" +
            ", acceptPayment='" + acceptPayment + "'" +
            '}';
    }
}
