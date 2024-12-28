package supportly.supportlybackend.Model.Template;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "PARTS_TEMPLATE")
public class PartsTemplate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "NAME", nullable = false)
    private String name;


    @Column(name = "PRICE", nullable = false)
    private float price;

    @Column(name = "TAX")
    private float tax;
}
