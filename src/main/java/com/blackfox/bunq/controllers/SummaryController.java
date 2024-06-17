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
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox vBox;
    @FXML
    private Text income, outcome;
    @FXML
    private PieChart chart;

    @FXML
    public void initialize() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime before = now.minusDays(30);

        ZonedDateTime zonedNow = now.atZone(ZoneId.systemDefault());
        ZonedDateTime zonedBefore = before.atZone(ZoneId.systemDefault());

        Timestamp until = Timestamp.from(zonedNow.toInstant());
        Timestamp since = Timestamp.from(zonedBefore.toInstant());

        Client client = HibernateUtil.getActiveClient();
        List<Transaction> outcomeTransactions = client.getTransactions(Client.TransactionType.SENT, since, until);
        List<Transaction> incomeTransactions = client.getTransactions(Client.TransactionType.RECEIVED, since, until);
        List<Transaction> allTransactions = client.getTransactions(since, until);

        float sumIncome = sumTransactions(incomeTransactions);
        float sumOutcome = -sumTransactions(outcomeTransactions);

        setChart(sumIncome, sumOutcome);
        setTransactions(allTransactions);

        income.setText("+" + sumIncome + " PLN");
        outcome.setText(String.valueOf(sumOutcome) + " PLN");
    }

    private void setTransactions(List<Transaction> all) {
        for (Transaction transaction : all) {
            try {
                FXMLLoader loader = new FXMLLoader(Main.class.getResource("./view/last_transaction.fxml"));
                AnchorPane pane = loader.load();
                LastTransactionController controller = loader.getController();
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
