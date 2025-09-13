package supportly.supportlybackend.Model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "TASK")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "EXECUTION_TIME")
    private LocalDate executionTime;

    @OneToOne
    @JoinColumn(name = "ORDER_ID")
    @JsonManagedReference
    private Order order;

    @ManyToMany
    @JoinTable(
            name = "TASK_EMPLOYEE", // tabela łącznikowa
            joinColumns = @JoinColumn(name = "TASK_ID"), // kolumna wskazująca na Task
            inverseJoinColumns = @JoinColumn(name = "EMPLOYEE_ID") // kolumna wskazująca na Employee
    )
    private List<Employee> employees = new ArrayList<>();

    @Column(name = "DONE")
    private Boolean done;
}
