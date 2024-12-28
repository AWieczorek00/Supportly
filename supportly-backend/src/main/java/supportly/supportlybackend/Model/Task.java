package supportly.supportlybackend.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.time.LocalDate;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name="TASK")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name="NAME", nullable = false)
    private String name;

    @Column(name="EXECUTION_TIME")
    private LocalDate executionTime;

    @Column(name="DONE")
    private Boolean done;

    @OneToOne
    @JoinColumn(name = "EMPLOYEE_ID")
    private Employee employee;
}
