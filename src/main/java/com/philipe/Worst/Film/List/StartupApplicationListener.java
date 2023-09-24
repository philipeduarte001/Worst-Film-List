package com.philipe.Worst.Film.List;

import com.philipe.Worst.Film.List.service.WorstFilmsService;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class StartupApplicationListener implements ApplicationListener<ApplicationReadyEvent> {

    private final WorstFilmsService worstFilmsService;

    public StartupApplicationListener(WorstFilmsService worstFilmsService) {
        this.worstFilmsService = worstFilmsService;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        worstFilmsService.loadAndPersistCsvFile("src/main/resources/movielist.csv");
    }

}
