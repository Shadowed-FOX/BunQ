package com.blackfox.bunq.controllers;

import com.blackfox.bunq.Main;
import com.blackfox.bunq.database.Client;
import com.blackfox.bunq.database.ClientNotFoundException;
import com.blackfox.bunq.database.HibernateUtil;
import com.blackfox.bunq.database.Transaction;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.chart.PieChart;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.fxml.FXML;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

public class SummaryController {
    private Client currentClient;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox vBox;
    @FXML
    private Text income, outcome;
    @FXML
    private PieChart chart;


    public void postInitialize() {
        currentClient = HibernateUtil.getActiveClient();

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime before = now.minusDays(30);

        ZonedDateTime zonedNow = now.atZone(ZoneId.systemDefault());
        ZonedDateTime zonedBefore = before.atZone(ZoneId.systemDefault());

        Timestamp until = Timestamp.from(zonedNow.toInstant());
        Timestamp since = Timestamp.from(zonedBefore.toInstant());

        List<Transaction> outcomeTransactions = currentClient.getTransactions(Client.TransactionType.SENT, since, until);
        List<Transaction> incomeTransactions = currentClient.getTransactions(Client.TransactionType.RECEIVED, since, until);
        List<Transaction> allTransactions = currentClient.getTransactions(since, until);

        float sumIncome = sumTransactions(incomeTransactions);
        float sumOutcome = sumTransactions(outcomeTransactions) * -1;

        setChart(sumIncome, sumOutcome);
        setTransactions(allTransactions);

        income.setText("+" + sumIncome);
        outcome.setText(String.valueOf(sumOutcome));
    }

    private void setTransactions(List<Transaction> all) {
        for (Transaction transaction : all) {
            try {
                FXMLLoader loader = new FXMLLoader(Main.class.getResource("./view/last_transaction.fxml"));
                AnchorPane pane = loader.load();
                LastTransactionController controller = loader.getController();
                controller.setCurrentClient(currentClient);
                controller.getData(transaction);
                vBox.getChildren().add(pane);
            } catch (IOException | ClientNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        scrollPane.setContent(vBox);
    }

    private void setChart(float income, float outcome) {
        ObservableList<PieChart.Data> chartData = FXCollections.observableArrayList(
                new PieChart.Data("Wydatki", outcome),
                new PieChart.Data("Przychody", income)
        );
        chart.setData(chartData);
    }

    private float sumTransactions(List<Transaction> list) {
        float sum = 0;
        for (Transaction transaction : list) {
            sum += transaction.getAmount();
        }
        return sum;
    }
}