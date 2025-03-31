package supportly.supportlybackend.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Data;
import supportly.supportlybackend.Model.Address;

@Entity
@Data
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "NIP", length = 13,unique = true)
    private String nip;

    @OneToOne(cascade = CascadeType.ALL) // Dodaj kaskadowość
    @JoinColumn(name = "ADDRESS_ID")
    private Address address;

    @Column(name = "PHONE_NUMBER", nullable = false, length = 9)
    private String phoneNumber;

    @Column(name = "EMAIL", nullable = false)
    @Email
    private String email;

    @Column(name="REGON", nullable = false, unique = true)
    private String regon;

}
