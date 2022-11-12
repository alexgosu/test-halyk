package kz.example.halykfinance.scheduler;

import kz.example.halykfinance.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ScheduledTasks {

    private final MessageService messageService;

    @Scheduled(cron = "${application.msg-processor-cron}")
    @SchedulerLock(name = "ScheduledTasks_processMessages")
    public void processMessages() {
        log.info("processMessages::start");
        try {
            messageService.processAllMessages();
        } catch (Exception e) {
            log.error("Unhandled exception", e);
        }
        log.info("processMessages::end");
    }
}
