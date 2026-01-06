package com.adsentinel.presentation.desktop;

import com.adsentinel.application.service.DashboardService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class DashboardController {

    private final DashboardService dashboardService;

    @FXML
    private Label totalGmailLabel;
    
    @FXML
    private Label warmingGmailLabel;

    @FXML
    private Label totalAdsLabel;
    
    @FXML
    private Label activeAdsLabel;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @FXML
    public void initialize() {
        refreshStats();
    }

    public void refreshStats() {
        Map<String, Long> gmailStats = dashboardService.getGmailStats();
        Map<String, Long> adsStats = dashboardService.getAdsStats();

        totalGmailLabel.setText(String.valueOf(gmailStats.getOrDefault("TOTAL", 0L)));
        warmingGmailLabel.setText(String.valueOf(gmailStats.getOrDefault("WARMING", 0L)));
        
        totalAdsLabel.setText(String.valueOf(adsStats.getOrDefault("TOTAL", 0L)));
        activeAdsLabel.setText(String.valueOf(adsStats.getOrDefault("ACTIVE", 0L)));
    }
}
