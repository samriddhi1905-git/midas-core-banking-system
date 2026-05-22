package com.jpmc.midascore.component;

import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Incentive;
import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.repository.TransactionRepository;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class KafkaConsumer {

    private final DatabaseConduit databaseConduit;
    private final TransactionRepository transactionRepository;

    private final RestTemplate restTemplate = new RestTemplate();

    public KafkaConsumer(DatabaseConduit databaseConduit,
                         TransactionRepository transactionRepository) {
        this.databaseConduit = databaseConduit;
        this.transactionRepository = transactionRepository;
    }

    @KafkaListener(
            topics = "${general.kafka-topic}",
            groupId = "midas-group"
    )
    public void listen(Transaction transaction) {

        UserRecord sender =
                databaseConduit.findById(transaction.getSenderId());

        UserRecord recipient =
                databaseConduit.findById(transaction.getRecipientId());

        if (sender == null || recipient == null) {
            return;
        }

        if (sender.getBalance() < transaction.getAmount()) {
            return;
        }

        Incentive incentive =
                restTemplate.postForObject(
                        "http://localhost:8080/incentive",
                        transaction,
                        Incentive.class
                );

        float incentiveAmount = incentive.getAmount();

        sender.setBalance(
                sender.getBalance() - transaction.getAmount());

        recipient.setBalance(
                recipient.getBalance()
                        + transaction.getAmount()
                        + incentiveAmount);

        databaseConduit.save(sender);
        databaseConduit.save(recipient);

        TransactionRecord record =
                new TransactionRecord(
                        sender,
                        recipient,
                        transaction.getAmount(),
                        incentiveAmount);

        transactionRepository.save(record);
    }
}