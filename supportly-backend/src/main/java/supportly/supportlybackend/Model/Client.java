package supportly.supportlybackend.Model;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "CLIENT")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "FIRST_NAME")
    private String firstName;

    @Column(name = "SECOND_NAME")
    private String LastName;

    @Column(name="PHONE_NUMBER", nullable = false, length = 9)
    private String phoneNumber;

    @Column(name="EMAIL", nullable = false)
    @Email
    private String email;

    @OneToOne(cascade = CascadeType.ALL)
    private Company company;

    @Column(nullable = false)
    private String type;

}
