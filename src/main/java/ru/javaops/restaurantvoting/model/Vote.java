package ru.javaops.restaurantvoting.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(
        name = "vote",
        uniqueConstraints = @UniqueConstraint(columnNames = {"date", "user_id"}, name = "uk_date_user"))
@Getter
@Setter
@NoArgsConstructor
public class Vote extends BaseEntity {

    @Column(name = "date", nullable = false)
    @Temporal(TemporalType.DATE)
    private LocalDate date;

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

    public Vote(Long id, LocalDate date, User user, Lunch lunch) {
        this.id = id;
        this.date = date;
        this.user = user;
        this.lunch = lunch;
    }

    public Vote(LocalDate date, User user, Lunch lunch) {
        this(null, date, user, lunch);
    }

}
