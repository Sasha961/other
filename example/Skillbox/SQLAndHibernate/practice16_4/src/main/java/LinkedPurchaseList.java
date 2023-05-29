import jakarta.persistence.*;
import lombok.Data;

@Data

@Entity
public class LinkedPurchaseList {

    @EmbeddedId
    private KeyLinkedPurchaseList id;

    @Column(name = "student_id", insertable = false, updatable = false)
    private Integer studentId;

    @Column(name = "course_id", insertable = false, updatable = false)
    private Integer courseId;
}
