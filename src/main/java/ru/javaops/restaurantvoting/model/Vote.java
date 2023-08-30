package ru.javaops.restaurantvoting.model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(
        name = "vote",
        uniqueConstraints = @UniqueConstraint(columnNames = {"date", "user_id"}, name = "uk_date_user"))
@Getter
@Setter
public class Vote {

    @EmbeddedId
    private VoteId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lunch_id", nullable = false)
    private Lunch lunch;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Vote that)) {
            return false;
        }
        return that.id.equals(id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Embeddable
    @EqualsAndHashCode
    private static class VoteId implements Serializable {

        @Column(name = "date", nullable = false)
        @Temporal(TemporalType.DATE)
        private LocalDate date;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "user_id", nullable = false)
        private User user;
    }
}
