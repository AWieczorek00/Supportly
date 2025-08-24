package supportly.supportlybackend.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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

    @Column(name = "LAST_NAME", nullable = false)
    private String lastName;

    //todo dodac aby był uniklany
    @Column(name = "PHONE_NUMBER", nullable = false, length = 9, unique = true)
    private String phoneNumber;

    @ManyToMany(mappedBy = "employees")
    private List<Task> tasks = new ArrayList<>();

    @Column(name = "DATA_OF_CREATION", nullable = false)
    @CreationTimestamp
    private LocalDate dateOfCreation;
}
