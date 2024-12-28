package supportly.supportlybackend.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Proxy;
import org.springframework.context.annotation.Lazy;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ORDER")
@Proxy(lazy = false)
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @OneToOne()
    @JoinColumn(name = "CLIENT_ID")
    private Client client;

    @ManyToMany
    @JoinTable(
            name = "ORDER_EMPLOYEE",
            joinColumns = @JoinColumn(name = "ORDER_ID"),
            inverseJoinColumns = @JoinColumn(name = "EMPLOYEE_ID"))

    private List<Employee> employeeList;

    @OneToMany()
    @JoinColumn(name = "ACTIVITIES_ID")
    private List<Activities> activitiesList;

    @OneToMany()
    @JoinColumn(name = "PART_ID")
    private List<Part> partList;

    @Column(name="DATE_OF_ADMISSION")
    private LocalDate dateOfAdmission;

    @Column(name = "DATE_OF_EXECUTION")
    private LocalDate dateOfExecution;

    @Column(name="MAN_HOUR")
    private float manHour;

    @Column(name="DISTANCE")
    private float distance;

    @Column(name="PRIORITY")
    private String priority;

    @Column(name="STATUS")
    private String status;

    @Column(name="PERIOD")
    private String period;

    @Column(name="NOTE")
    private String note;
}
