package com.me.interview.application;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.io.Reader;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;

public class TransactionProcessor {
    final static Logger logger = Logger.getLogger(TransactionProcessor.class);



    public String processTransactionFile(String filePath, String fromDateString, String toDateString, String accountId)
            throws IOException, ParseException {



        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        Date fromDate = formatter.parse(fromDateString);
        Date toDate = formatter.parse(toDateString);


        Reader reader = Files.newBufferedReader(Paths.get(filePath));

        CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT
                .withHeader("transactionId", "fromAccountId", "toAccountId", "createdAt", "amount", "transactionType", "relatedTransaction")
                .withIgnoreHeaderCase().withSkipHeaderRecord()
                .withTrim());

        List reversalTransactions = new ArrayList<String>();

        Map<String, BigDecimal> relativeTransactions = new HashMap<>();

        csvParser.forEach(record -> {
            try {

                if (
                        !record.get("fromAccountId").equalsIgnoreCase(record.get("toAccountId")) // ignore records with  same from/to accounts
                                &&
                                (record.get("fromAccountId").equalsIgnoreCase(accountId) || record.get("toAccountId").equalsIgnoreCase(accountId))

                                &&
                                fromDate.compareTo(formatter.parse(record.get("createdAt"))) <= 0
                                &&
                                toDate.compareTo(formatter.parse(record.get("createdAt"))) >= 0
                                &&
                                !record.get("transactionType").equalsIgnoreCase("REVERSAL")
                        ) {
                    BigDecimal val;

                    if (record.get("fromAccountId").toString().equalsIgnoreCase(accountId)) {
                        val = new BigDecimal(record.get("amount")).negate();
                    } else {
                        val = new BigDecimal(record.get("amount"));
                    }
                    relativeTransactions.put(record.get("transactionId"), val);

                }


                if (record.get("transactionType").equalsIgnoreCase("REVERSAL")) {
                    reversalTransactions.add(record.get("relatedTransaction"));
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

        });


        Map<String, BigDecimal> reverseExcludedList = relativeTransactions.entrySet().parallelStream()
                .filter(tr -> !reversalTransactions.contains(tr.getKey())).collect(Collectors.toMap(z -> z.getKey(), z -> z.getValue()));


        BigDecimal total = new BigDecimal(0);

        if (!reverseExcludedList.isEmpty()) {
            total = reverseExcludedList.values().stream().reduce((x, y) -> x.add(y)).get();
        }

        return total.toString();
    }

    public static void main(String[] args) {
        logger.info("Starting the app>>>>>");


        TransactionProcessor ptf = new TransactionProcessor();

        try {

            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(System.in));

            System.out.println("enter csv file path: ");
            String filePath = reader.readLine();

            System.out.println("enter from Date: ");
            String fromDate = reader.readLine();

            System.out.println("enter to Date: ");
            String toDate = reader.readLine();

            System.out.println("enter account ID: ");
            String accountId = reader.readLine();

           String balance = ptf.processTransactionFile(filePath, fromDate, toDate, accountId);

           System.out.println("relative balance is: " + balance);

        } catch (IOException ex) {
            logger.info("Failed to read the csv file" );
            logger.debug(ex.getMessage());

        } catch (ParseException ex) {
            logger.info("Failed to parse the csv file");
            logger.debug(ex.getMessage());

        }

    }


}
