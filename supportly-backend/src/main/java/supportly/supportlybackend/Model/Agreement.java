package supportly.supportlybackend.Model;

import jakarta.persistence.*;
import lombok.Data;
import supportly.supportlybackend.Enum.Period;

import java.time.LocalDate;

@Entity
@Data
public class Agreement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @OneToOne
    @JoinColumn(name = "COMPANY_ID")
    private Company company;

    @Column(name = "AGREEMENT_NUMBER", nullable = false)
    private String agreementNumber;

    @Column(name = "SIGNED_DATE", nullable = false)
    private LocalDate signedDate;

    @Column(name = "PERIOD", nullable = false)
    @Enumerated(EnumType.STRING)
    private Period period;
}
