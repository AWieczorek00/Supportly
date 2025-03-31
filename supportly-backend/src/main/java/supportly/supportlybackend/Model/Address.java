package supportly.supportlybackend.Model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "CITY", nullable = false)
    private String city;

    @Column(name = "ZIP_CODE", nullable = false)
    private String zipCode;

    @Column(name = "STREET", nullable = false)
    private String street;

    @Column(name = "APARTMENT_NUMBER")
    private String apartmentNumber;

    @Column(name = "STREET_NUMBER", nullable = false)
    private int streetNumber;




}
