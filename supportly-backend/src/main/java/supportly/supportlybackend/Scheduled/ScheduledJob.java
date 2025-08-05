package supportly.supportlybackend.Scheduled;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import supportly.supportlybackend.Service.OrderService;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class ScheduledJob {

    private final OrderService orderService;
    private LocalDate lastRun = null;

    // Automatycznie wywoływane codziennie o 8:00, jeśli aplikacja działa
    @Scheduled(cron = "0 0 8 * * *")
    public void scheduledRun() {
        runJob();
    }

    // Wywoływane po uruchomieniu aplikacji
    @EventListener(ApplicationReadyEvent.class)
    public void catchUpOnStartup() {
        if (lastRun == null || !lastRun.isEqual(LocalDate.now())) {
            System.out.println("Job was missed — running after startup");
            runJob();
        }
    }

    private void runJob() {
        orderService.createOrderFromSchedule();
        System.out.println("Uruchomiono job: " + LocalDateTime.now());
        lastRun = LocalDate.now();
    }

}
