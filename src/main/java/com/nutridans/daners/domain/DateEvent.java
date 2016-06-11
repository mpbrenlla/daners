package com.nutridans.daners.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DateEvent.
 */
@Entity
@Table(name = "date_event")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "dateevent")
public class DateEvent implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "date")
    private ZonedDateTime date;

    @Column(name = "breakfast")
    private String breakfast;

    @Column(name = "brunch")
    private String brunch;

    @Column(name = "lunch")
    private String lunch;

    @Column(name = "tea")
    private String tea;

    @Column(name = "dinner")
    private String dinner;

    @ManyToOne
    private User owner;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public String getBreakfast() {
        return breakfast;
    }

    public void setBreakfast(String breakfast) {
        this.breakfast = breakfast;
    }

    public String getBrunch() {
        return brunch;
    }

    public void setBrunch(String brunch) {
        this.brunch = brunch;
    }

    public String getLunch() {
        return lunch;
    }

    public void setLunch(String lunch) {
        this.lunch = lunch;
    }

    public String getTea() {
        return tea;
    }

    public void setTea(String tea) {
        this.tea = tea;
    }

    public String getDinner() {
        return dinner;
    }

    public void setDinner(String dinner) {
        this.dinner = dinner;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User user) {
        this.owner = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DateEvent dateEvent = (DateEvent) o;
        if(dateEvent.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, dateEvent.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "DateEvent{" +
            "id=" + id +
            ", date='" + date + "'" +
            ", breakfast='" + breakfast + "'" +
            ", brunch='" + brunch + "'" +
            ", lunch='" + lunch + "'" +
            ", tea='" + tea + "'" +
            ", dinner='" + dinner + "'" +
            '}';
    }
}
