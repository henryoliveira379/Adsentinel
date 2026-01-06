package com.adsentinel.presentation.desktop;

import com.adsentinel.application.dto.AdsAccountDTO;
import com.adsentinel.application.dto.CreateAdsAccountCommand;
import com.adsentinel.application.dto.GmailAccountDTO;
import com.adsentinel.application.service.AdsService;
import com.adsentinel.application.service.GmailService;
import com.adsentinel.domain.model.AdsAccount;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AdsController {

    private final AdsService adsService;
    private final GmailService gmailService;

    @FXML
    private TableView<AdsAccountDTO> adsTable;
    @FXML
    private TableColumn<AdsAccountDTO, String> accountIdColumn;
    @FXML
    private TableColumn<AdsAccountDTO, String> gmailColumn;
    @FXML
    private TableColumn<AdsAccountDTO, String> statusColumn;
    @FXML
    private TableColumn<AdsAccountDTO, String> phaseColumn;
    @FXML
    private TableColumn<AdsAccountDTO, Boolean> checklistColumn;
    @FXML
    private TableColumn<AdsAccountDTO, Void> actionColumn;

    public AdsController(AdsService adsService, GmailService gmailService) {
        this.adsService = adsService;
        this.gmailService = gmailService;
    }

    @FXML
    public void initialize() {
        accountIdColumn.setCellValueFactory(new PropertyValueFactory<>("accountId"));
        gmailColumn.setCellValueFactory(new PropertyValueFactory<>("gmailEmail"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        phaseColumn.setCellValueFactory(new PropertyValueFactory<>("phase"));
        checklistColumn.setCellValueFactory(new PropertyValueFactory<>("checklistCompleted"));

        addActionsColumn();
        loadData();
    }

    private void loadData() {
        ObservableList<AdsAccountDTO> data = FXCollections.observableArrayList(adsService.findAll());
        adsTable.setItems(data);
    }

    @FXML
    public void handleAddAccount() {
        Dialog<AdsAccountDTO> dialog = new Dialog<>();
        dialog.setTitle("Nova Conta Google Ads");
        dialog.setHeaderText("Vincular conta Ads a um Gmail");

        ButtonType saveButtonType = new ButtonType("Salvar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField accountId = new TextField();
        accountId.setPromptText("123-456-7890");
        
        ComboBox<GmailAccountDTO> gmailCombo = new ComboBox<>();
        gmailCombo.setItems(FXCollections.observableArrayList(gmailService.findAll()));
        // Show email in combo
        gmailCombo.setConverter(new javafx.util.StringConverter<GmailAccountDTO>() {
            @Override
            public String toString(GmailAccountDTO object) {
                return object == null ? "" : object.getEmail();
            }
            @Override
            public GmailAccountDTO fromString(String string) {
                return null;
            }
        });

        grid.add(new Label("ID da Conta:"), 0, 0);
        grid.add(accountId, 1, 0);
        grid.add(new Label("Gmail Vinculado:"), 0, 1);
        grid.add(gmailCombo, 1, 1);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                if (gmailCombo.getValue() == null) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Selecione um Gmail!");
                    alert.show();
                    return null;
                }
                
                CreateAdsAccountCommand command = new CreateAdsAccountCommand();
                command.setAccountId(accountId.getText());
                command.setGmailAccountId(gmailCombo.getValue().getId());
                command.setOperatorName("OPERADOR_DESKTOP");
                
                try {
                    return adsService.createAccount(command);
                } catch (Exception e) {
                     Alert alert = new Alert(Alert.AlertType.ERROR);
                     alert.setTitle("Erro");
                     alert.setContentText(e.getMessage());
                     alert.showAndWait();
                     return null;
                }
            }
            return null;
        });

        Optional<AdsAccountDTO> result = dialog.showAndWait();
        result.ifPresent(account -> loadData());
    }

    private void addActionsColumn() {
        Callback<TableColumn<AdsAccountDTO, Void>, TableCell<AdsAccountDTO, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<AdsAccountDTO, Void> call(final TableColumn<AdsAccountDTO, Void> param) {
                return new TableCell<>() {
                    private final Button btn = new Button("Ação");

                    {
                        btn.setOnAction(event -> {
                            AdsAccountDTO account = getTableView().getItems().get(getIndex());
                            try {
                                if (!account.isChecklistCompleted()) {
                                    adsService.completeChecklist(account.getId(), "OPERADOR_DESKTOP");
                                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Checklist completado!");
                                    alert.showAndWait();
                                    loadData();
                                } else if (account.getStatus() == AdsAccount.AdsStatus.CREATED) {
                                    adsService.promoteToActive(account.getId(), "OPERADOR_DESKTOP");
                                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Conta Ativada!");
                                    alert.showAndWait();
                                    loadData();
                                }
                            } catch (Exception e) {
                                Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
                                alert.showAndWait();
                            }
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            AdsAccountDTO account = getTableView().getItems().get(getIndex());
                            if (!account.isChecklistCompleted()) {
                                btn.setText("Checklist");
                                setGraphic(btn);
                            } else if (account.getStatus() == AdsAccount.AdsStatus.CREATED) {
                                btn.setText("Ativar");
                                setGraphic(btn);
                            } else {
                                setGraphic(null);
                            }
                        }
                    }
                };
            }
        };

        actionColumn.setCellFactory(cellFactory);
    }
}
