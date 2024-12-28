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

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "NIP", length = 13)
    private String nip;

    @Column(name = "ADDRESS", nullable = false)
    private String address;

    @Column(name = "CITY", nullable = false)
    private String city;

    @Column(name = "ZIP_CODE", nullable = false)
    private String zipcode;

    @Column(name = "STREET_NUMBER", nullable = false)
    private String streetNumber;

    @Column(name = "APARTMENT_NUMBER")
    private String apartmentNumber;

    @Column(name="PHONE_NUMBER", nullable = false, length = 9)
    private String phoneNumber;

    @Column(name="EMAIL", nullable = false)
    @Email
    private String email;

    @Column(nullable = false)
    private String type;

}
