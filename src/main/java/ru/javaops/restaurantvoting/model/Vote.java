package ru.javaops.restaurantvoting.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Persistable;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(
        name = "vote",
        uniqueConstraints = @UniqueConstraint(columnNames = {"date", "user_id"}, name = "uk_date_user"))
@IdClass(Vote.VoteId.class)
@Getter
@Setter
@NoArgsConstructor
public class Vote implements Persistable<Vote.VoteId> {

    @Id
    @Column(name = "date", nullable = false)
    @Temporal(TemporalType.DATE)
    private LocalDate date;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "user_id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "fk_user_vote",
                    foreignKeyDefinition = "FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE"
            ))
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "lunch_id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "fk_lunch_vote",
                    foreignKeyDefinition = "FOREIGN KEY(lunch_id) REFERENCES Lunch(id) ON DELETE CASCADE"
            ))
    private Lunch lunch;

    @Transient
    // For Hibernate to know that this Vote is not new.
    private boolean repeatedVote;

    public Vote(LocalDate date, User user, Lunch lunch) {
        this.date = date;
        this.user = user;
        this.lunch = lunch;
    }

    @Override
    public VoteId getId() {
        return new VoteId(date, user);
    }

    @Override
    public boolean isNew() {
        return !repeatedVote;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    public static class VoteId implements Serializable {

        private LocalDate date;

        private User user;

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof VoteId that)) {
                return false;
            }
            return date != null && user != null &&
                    date.equals(that.date) && user.equals(that.user);
        }

        @Override
        public int hashCode() {
            return Objects.hash(date, user);
        }

    }

}
