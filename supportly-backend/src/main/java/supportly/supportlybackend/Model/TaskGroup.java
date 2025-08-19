package supportly.supportlybackend.Model;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "TASK_GROUP")
@NoArgsConstructor
public class TaskGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name="TASK_ID",nullable = false)
    private Long taskId;

    public TaskGroup(Long taskId) {
        this.taskId = taskId;
    }
}

