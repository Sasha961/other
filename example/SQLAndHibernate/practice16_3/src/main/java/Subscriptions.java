import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Getter
@Setter

@Entity
@Table(name = "Subscriptions")
public class Subscriptions {

    @EmbeddedId
    private Key id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "student_id", insertable = false, updatable = false)
    private Students students;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "course_id", insertable = false, updatable = false)
    private Courses courses;

    @Column(name = "student_id", insertable = false, updatable = false)
    private Integer studentId;

    @Column(name = "course_id", insertable = false, updatable = false)
    private Integer courseId;

    @Column(name = "subscription_date")
    private Date subscriptionsDate;
}
