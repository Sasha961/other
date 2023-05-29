import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter

@Entity
@Table(name = "PurchaseList")
public class PurchaseList {

    @EmbeddedId
    private KeyPurchaseList id;

    @Column(name = "student_name", insertable = false, updatable = false)
    private String studentName;

    @Column(name = "course_name", insertable = false, updatable = false)
    private String courseName;


    private int price;

    @Column(name = "subscription_date")
    private Date subscriptionDate;
}
