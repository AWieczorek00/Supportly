package supportly.supportlybackend.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "EMPLOYEE")
public class Employee {

    @Id
    @Column(name = "ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long individualId;

    @Column(name = "FIRST_NAME", nullable = false)
    private String firstName;

    @Column(name = "SECOND_NAME")
    private String secondName;

    @Column(name="LAST_NAME", nullable = false)
    private String lastName;

    private String email;

    @Column(name="PHONE_NUMBER", nullable = false,length = 9)
    private Long phoneNumber;

    @Column(name="DATA_OF_CREATION", nullable = false)
    private LocalDate dateOfCreation;
}
