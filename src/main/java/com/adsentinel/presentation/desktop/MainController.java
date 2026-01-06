package com.adsentinel.presentation.desktop;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;

@Component
public class MainController {

    @FXML
    private BorderPane mainLayout;

    private final ApplicationContext applicationContext;
    
    @Value("classpath:/fxml/dashboard.fxml")
    private Resource dashboardFxml;

    @Value("classpath:/fxml/gmail_accounts.fxml")
    private Resource gmailFxml;
    
    @Value("classpath:/fxml/ads_accounts.fxml")
    private Resource adsFxml;

    public MainController(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @FXML
    public void initialize() {
        showDashboard();
    }

    @FXML
    public void showDashboard() {
        loadView(dashboardFxml);
    }

    @FXML
    public void showGmailAccounts() {
        loadView(gmailFxml);
    }

    @FXML
    public void showAdsAccounts() {
        loadView(adsFxml);
    }

    private void loadView(Resource fxml) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(fxml.getURL());
            fxmlLoader.setControllerFactory(applicationContext::getBean);
            Parent view = fxmlLoader.load();
            mainLayout.setCenter(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
