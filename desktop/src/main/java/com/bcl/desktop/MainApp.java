package com.bcl.desktop;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.core.type.TypeReference;

import java.net.http.*;
import java.net.URI;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.*;
import java.io.IOException;

public class MainApp extends Application {
    private final String API_BASE = "http://localhost:8080/api";
    private final HttpClient http = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    private final String AUTH_HEADER = "Basic " + java.util.Base64.getEncoder().encodeToString("staff:BCLyon2024".getBytes());

    private TableView<Map<String,Object>> table = new TableView<>();

    public static void main(String[] args){ launch(); }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("BCL - Products Manager (desktop)");
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        // top: buttons
        HBox top = new HBox(8);
        Button refreshBtn = new Button("Refresh");
        Button addBtn = new Button("Add Product");
        top.getChildren().addAll(refreshBtn, addBtn);

        // table columns
        TableColumn<Map<String,Object>, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(cd -> new javafx.beans.property.SimpleStringProperty(
                String.valueOf(cd.getValue().getOrDefault("productId", "")))
        );

        TableColumn<Map<String,Object>, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(cd -> new javafx.beans.property.SimpleStringProperty(
                String.valueOf(cd.getValue().getOrDefault("productName", "")))
        );

        TableColumn<Map<String,Object>, String> catCol = new TableColumn<>("Category");
        catCol.setCellValueFactory(cd -> new javafx.beans.property.SimpleStringProperty(
                String.valueOf(cd.getValue().getOrDefault("category", "")))
        );

        TableColumn<Map<String,Object>, String> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(cd -> new javafx.beans.property.SimpleStringProperty(
                String.valueOf(cd.getValue().getOrDefault("price", "")))
        );

        TableColumn<Map<String,Object>, String> costCol = new TableColumn<>("Cost");
        costCol.setCellValueFactory(cd -> new javafx.beans.property.SimpleStringProperty(
                String.valueOf(cd.getValue().getOrDefault("cost", "")))
        );

        table.getColumns().addAll(idCol, nameCol, catCol, priceCol, costCol);
        root.setTop(top);
        root.setCenter(table);

        refreshBtn.setOnAction(e -> { try { loadProducts(); } catch (Exception ex){ showError(ex); } });
        addBtn.setOnAction(e -> showAddDialog());

        Scene scene = new Scene(root, 800, 400);
        primaryStage.setScene(scene);
        primaryStage.show();

        // initial load
        try { loadProducts(); } catch (Exception ex){ showError(ex); }
    }

    private void loadProducts() throws Exception {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(API_BASE + "/products"))
                .header("Accept", "application/json")
                .header("Authorization", AUTH_HEADER)
                .GET()
                .build();

        var resp = http.send(req, BodyHandlers.ofString());

        if(resp.statusCode() == 401){
            showAlert("Unauthorized","API requires Basic Auth (staff / BCLyon2024)", Alert.AlertType.ERROR);
            return;
        }
        if(resp.statusCode() != 200){
            showAlert("Error","Failed to load products: " + resp.statusCode(), Alert.AlertType.ERROR);
            return;
        }

        List<Map<String,Object>> list = mapper.readValue(resp.body(), new TypeReference<List<Map<String,Object>>>(){});
        table.getItems().clear();
        table.getItems().addAll(list);
    }

    private void showAddDialog(){
        Dialog<Map<String,String>> dlg = new Dialog<>();
        dlg.setTitle("Add Product");
        ButtonType saveBtn = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dlg.getDialogPane().getButtonTypes().addAll(saveBtn, ButtonType.CANCEL);
        GridPane gp = new GridPane();
        gp.setHgap(8); gp.setVgap(8);
        TextField nameF = new TextField();
        TextField catF = new TextField();
        TextField priceF = new TextField();
        TextField costF = new TextField();
        gp.addRow(0, new Label("Name:"), nameF);
        gp.addRow(1, new Label("Category:"), catF);
        gp.addRow(2, new Label("Price:"), priceF);
        gp.addRow(3, new Label("Cost:"), costF);
        dlg.getDialogPane().setContent(gp);
        dlg.setResultConverter(btn -> {
            if(btn == saveBtn){
                Map<String,String> m = new HashMap<>();
                m.put("productName", nameF.getText());
                m.put("category", catF.getText());
                m.put("price", priceF.getText());
                m.put("cost", costF.getText());
                return m;
            }
            return null;
        });
        var res = dlg.showAndWait();
        res.ifPresent(map -> {
            try {
                createProduct(map.get("productName"), map.get("category"), map.get("price"), map.get("cost"));
                loadProducts();
            } catch (Exception ex){ showError(ex); }
        });
    }

    private void createProduct(String name, String category, String price, String cost) throws IOException, InterruptedException {
        Map<String,Object> payload = new HashMap<>();
        payload.put("productName", name);
        payload.put("category", category);
        payload.put("price", Double.parseDouble(price));
        payload.put("cost", Double.parseDouble(cost));
        String body = mapper.writeValueAsString(payload);

        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(API_BASE + "/products"))
                .header("Content-Type", "application/json")
                .header("Authorization", AUTH_HEADER)
                .POST(BodyPublishers.ofString(body))
                .build();

        var resp = http.send(req, BodyHandlers.ofString());
        if(resp.statusCode() >= 400){
            showAlert("Error","Create failed: " + resp.statusCode() + "\n" + resp.body(), Alert.AlertType.ERROR);
        } else {
            showAlert("Success","Product created", Alert.AlertType.INFORMATION);
        }
    }

    private void showError(Throwable t){
        t.printStackTrace();
        showAlert("Error", t.getMessage(), Alert.AlertType.ERROR);
    }

    private void showAlert(String title, String msg, Alert.AlertType type){
        Alert a = new Alert(type);
        a.setTitle(title);
        a.setContentText(msg);
        a.showAndWait();
    }
}
