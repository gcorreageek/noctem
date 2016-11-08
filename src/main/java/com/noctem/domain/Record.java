package com.noctem.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Record.
 */
@Entity
@Table(name = "record")
public class Record implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "date", nullable = false)
    private ZonedDateTime date;

    @NotNull
    @Column(name = "total", nullable = false)
    private Double total;

    @OneToMany(mappedBy = "record", cascade = {CascadeType.ALL}, orphanRemoval = true)
    @JsonIgnoreProperties({"record"})
    private Set<RecordItem> recordItems = new HashSet<>();

    @OneToMany(mappedBy = "record")
    @JsonIgnore
    private Set<RecordPayment> recordPayments = new HashSet<>();

    @ManyToOne
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Set<RecordItem> getRecordItems() {
        return recordItems;
    }

    public void setRecordItems(Set<RecordItem> recordItems) {
        this.recordItems = recordItems;
    }

    public Set<RecordPayment> getRecordPayments() {
        return recordPayments;
    }

    public void setRecordPayments(Set<RecordPayment> recordPayments) {
        this.recordPayments = recordPayments;
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
        Record record = (Record) o;
        if(record.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, record.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Record{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", date='" + date + "'" +
            ", total='" + total + "'" +
            '}';
    }
}
