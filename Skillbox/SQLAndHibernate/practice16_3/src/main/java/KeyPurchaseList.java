import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Embeddable
public class KeyPurchaseList implements Serializable {

    @Column(name = "student_name")
    private String studentName;

    @Column(name = "course_name")
    private String courseName;
}
