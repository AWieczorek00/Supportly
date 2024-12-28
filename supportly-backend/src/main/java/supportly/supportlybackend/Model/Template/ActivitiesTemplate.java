package supportly.supportlybackend.Model.Template;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@NoArgsConstructor
@Data
@AllArgsConstructor
@Table(name = "ACTIVITIES_TEMPLATE")
public class ActivitiesTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name="NAME", nullable = false)
    private String name;

    public ActivitiesTemplate(String name) {
        this.name = name;
    }
}
