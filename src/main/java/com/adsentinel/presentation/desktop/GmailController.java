package com.adsentinel.presentation.desktop;

import com.adsentinel.application.dto.CreateGmailAccountCommand;
import com.adsentinel.application.dto.GmailAccountDTO;
import com.adsentinel.application.service.GmailService;
import com.adsentinel.domain.model.GmailAccount;
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
public class GmailController {

    private final GmailService gmailService;

    @FXML
    private TableView<GmailAccountDTO> gmailTable;
    @FXML
    private TableColumn<GmailAccountDTO, Long> idColumn;
    @FXML
    private TableColumn<GmailAccountDTO, String> emailColumn;
    @FXML
    private TableColumn<GmailAccountDTO, String> statusColumn;
    @FXML
    private TableColumn<GmailAccountDTO, Integer> warmingColumn;
    @FXML
    private TableColumn<GmailAccountDTO, String> notesColumn;
    @FXML
    private TableColumn<GmailAccountDTO, Void> actionColumn;

    public GmailController(GmailService gmailService) {
        this.gmailService = gmailService;
    }

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        warmingColumn.setCellValueFactory(new PropertyValueFactory<>("warmingDays"));
        notesColumn.setCellValueFactory(new PropertyValueFactory<>("notes"));

        addButtonToTable();
        loadData();
    }

    private void loadData() {
        ObservableList<GmailAccountDTO> data = FXCollections.observableArrayList(gmailService.findAll());
        gmailTable.setItems(data);
    }

    @FXML
    public void handleAddAccount() {
        Dialog<GmailAccountDTO> dialog = new Dialog<>();
        dialog.setTitle("Adicionar Conta Gmail");
        dialog.setHeaderText("Cadastrar nova conta");

        ButtonType loginButtonType = new ButtonType("Salvar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField email = new TextField();
        email.setPromptText("email@gmail.com");
        TextField notes = new TextField();
        notes.setPromptText("Observações");

        grid.add(new Label("Email:"), 0, 0);
        grid.add(email, 1, 0);
        grid.add(new Label("Obs:"), 0, 1);
        grid.add(notes, 1, 1);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                CreateGmailAccountCommand command = new CreateGmailAccountCommand();
                command.setEmail(email.getText());
                command.setNotes(notes.getText());
                command.setOperatorName("OPERADOR_DESKTOP");
                
                try {
                    return gmailService.createAccount(command);
                } catch (Exception e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Erro");
                    alert.setHeaderText("Erro ao criar conta");
                    alert.setContentText(e.getMessage());
                    alert.showAndWait();
                    return null;
                }
            }
            return null;
        });

        Optional<GmailAccountDTO> result = dialog.showAndWait();
        result.ifPresent(account -> loadData());
    }

    private void addButtonToTable() {
        Callback<TableColumn<GmailAccountDTO, Void>, TableCell<GmailAccountDTO, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<GmailAccountDTO, Void> call(final TableColumn<GmailAccountDTO, Void> param) {
                return new TableCell<>() {
                    private final Button btn = new Button("Ação");

                    {
                        btn.setOnAction(event -> {
                            GmailAccountDTO account = getTableView().getItems().get(getIndex());
                            // Handle action logic (e.g., promote/suspend via service)
                            if (account.getStatus() == GmailAccount.AccountStatus.WARMING) {
                                try {
                                    gmailService.promoteToActive(account.getId(), "OPERADOR_DESKTOP");
                                    loadData();
                                } catch (Exception e) {
                                    Alert alert = new Alert(Alert.AlertType.ERROR);
                                    alert.setContentText(e.getMessage());
                                    alert.show();
                                }
                            }
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            GmailAccountDTO account = getTableView().getItems().get(getIndex());
                            if (account.getStatus() == GmailAccount.AccountStatus.WARMING) {
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
