import com.me.interview.application.TransactionProcessor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;



public class TransactionProcessUnitTests {

    private final static String reversalAfterTimeFramePath = "/tmp/reversalAfterTimeFrame.csv";
    private final static String reversalBeforeTimeFramePath = "/tmp/reversalBeforeTimeFrame.csv";
    private final static String reversalWithinTimeFramePath = "/tmp/reversalWithinTimeFrame.csv";
    private final static String reversalWithinTimeFrameForTransactionOutsideTimeFramePath = "/tmp/reversalWithinTimeFrameForTransactionOutsideTimeFrame.csv";


    final static Logger logger = Logger.getLogger(TransactionProcessUnitTests.class);

    @Test
    public void testProcessReversalAfterTimeFrame() {
        TransactionProcessor testClass = new TransactionProcessor();
        try {
            String res = testClass.processTransactionFile(reversalAfterTimeFramePath,"20/10/2018 12:00:00",
                    "20/10/2018 19:00:00", "ACC334455");
            assertEquals("10.00", res);
        } catch (IOException ex) {
            logger.info("failed to read input file");


        } catch (ParseException ex) {
            logger.info("failed to parse input csv file");
        }

    }

    @Test
    public void testProcessReversalBeforeTimeFrame() {
        TransactionProcessor testClass = new TransactionProcessor();
        try {
            String res = testClass.processTransactionFile(reversalBeforeTimeFramePath,"20/10/2018 12:00:00",
                    "20/10/2018 19:00:00", "ACC334455");
            assertEquals("-5.00", res);
        } catch (IOException ex) {
            logger.info("failed to read input file");


        } catch (ParseException ex) {
            logger.info("failed to parse input csv file");
        }

    }

    @Test
    public void testProcessReversalWithinTimeFrame() {
        TransactionProcessor testClass = new TransactionProcessor();
        try {
            String res = testClass.processTransactionFile(reversalWithinTimeFramePath,"20/10/2018 12:00:00",
                    "20/10/2018 19:00:00", "ACC334455");
            assertEquals("10.00", res);
        } catch (IOException ex) {
            logger.info("failed to read input file");


        } catch (ParseException ex) {
            logger.info("failed to parse input csv file");
        }

    }

    @Test
    public void testReversalWithinTimeFrameForTransactionOutsideTimeFrame() {
        TransactionProcessor testClass = new TransactionProcessor();
        try {
            String res = testClass.processTransactionFile(reversalWithinTimeFrameForTransactionOutsideTimeFramePath,"20/10/2018 12:00:00",
                    "20/10/2018 19:00:00", "ACC334455");
            assertEquals("-15.00", res);
        } catch (IOException ex) {
            logger.info("failed to read input file");


        } catch (ParseException ex) {
            logger.info("failed to parse input csv file");
        }

    }


    @Before
    public void prepareFiles() throws IOException{

        BufferedWriter writer = Files.newBufferedWriter(Paths.get(reversalAfterTimeFramePath));
        CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
                .withHeader("transactionId", "fromAccountId", "toAccountId", "createdAt", "amount", "transactionType", "relatedTransaction"));

        csvPrinter.printRecord("TX10001", "ACC334455", "ACC778899", "20/10/2018 12:47:55", "10.00", "PAYMENT","");
        csvPrinter.printRecord("TX10002", "ACC778879", "ACC334455", "20/10/2018 14:47:55", "10.00", "PAYMENT","");
        csvPrinter.printRecord("TX10003", "ACC334455", "ACC778879", "20/10/2018 15:47:55", "15.00", "PAYMENT","");
        csvPrinter.printRecord("TX10004", "ACC778879", "ACC334455", "20/10/2018 16:47:55", "10.00", "PAYMENT","");
        csvPrinter.printRecord("TX10005", "ACC334455", "ACC778879", "20/10/2018 20:47:55", "15.00", "REVERSAL","TX10003");
        csvPrinter.flush();

/////////////////////////////////////////////////

        writer = Files.newBufferedWriter(Paths.get(reversalBeforeTimeFramePath));
        csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
                .withHeader("transactionId", "fromAccountId", "toAccountId", "createdAt", "amount", "transactionType", "relatedTransaction"));

        csvPrinter.printRecord("TX10000", "ACC334455", "ACC778879", "20/10/2018 11:47:55", "10.00", "REVERSAL","TX10001");
        csvPrinter.printRecord("TX10001", "ACC334455", "ACC778899", "20/10/2018 12:47:55", "10.00", "PAYMENT","");
        csvPrinter.printRecord("TX10002", "ACC778879", "ACC334455", "20/10/2018 14:47:55", "10.00", "PAYMENT","");
        csvPrinter.printRecord("TX10003", "ACC334455", "ACC778879", "20/10/2018 15:47:55", "25.00", "PAYMENT","");
        csvPrinter.printRecord("TX10004", "ACC778879", "ACC334455", "20/10/2018 16:47:55", "10.00", "PAYMENT","");
        csvPrinter.flush();
/////////////////////////////////////////////////

        writer = Files.newBufferedWriter(Paths.get(reversalWithinTimeFramePath));
        csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
                .withHeader("transactionId", "fromAccountId", "toAccountId", "createdAt", "amount", "transactionType", "relatedTransaction"));

        csvPrinter.printRecord("TX10001", "ACC334455", "ACC778899", "20/10/2018 12:47:55", "10.00", "PAYMENT","");
        csvPrinter.printRecord("TX10002", "ACC778879", "ACC334455", "20/10/2018 14:47:55", "10.00", "PAYMENT","");
        csvPrinter.printRecord("TX10003", "ACC334455", "ACC778879", "20/10/2018 15:47:55", "15.00", "PAYMENT","");
        csvPrinter.printRecord("TX10004", "ACC778879", "ACC334455", "20/10/2018 16:47:55", "10.00", "PAYMENT","");
        csvPrinter.printRecord("TX10005", "ACC334455", "ACC778879", "20/10/2018 18:47:55", "15.00", "REVERSAL","TX10003");

        csvPrinter.flush();

//////////////////////////////////////////////////
        writer = Files.newBufferedWriter(Paths.get(reversalWithinTimeFrameForTransactionOutsideTimeFramePath));
        csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
                .withHeader("transactionId", "fromAccountId", "toAccountId", "createdAt", "amount", "transactionType", "relatedTransaction"));

        csvPrinter.printRecord("TX10000", "ACC334455", "ACC778899", "20/10/2018 11:47:55", "10.00", "PAYMENT","");
        csvPrinter.printRecord("TX10001", "ACC334455", "ACC778899", "20/10/2018 12:47:55", "10.00", "PAYMENT","");
        csvPrinter.printRecord("TX10002", "ACC778879", "ACC334455", "20/10/2018 14:47:55", "10.00", "PAYMENT","");
        csvPrinter.printRecord("TX10003", "ACC334455", "ACC778879", "20/10/2018 15:47:55", "15.00", "PAYMENT","");
        csvPrinter.printRecord("TX10004", "ACC334455", "ACC778899", "20/10/2018 16:47:55", "10.00", "REVERSAL","TX10000");

        csvPrinter.flush();


    }

    @After
    public void destroyFiles() throws IOException{

        File file = new File(reversalAfterTimeFramePath);
        file.delete();

        file = new File(reversalBeforeTimeFramePath);
        file.delete();

        file = new File(reversalWithinTimeFramePath);
        file.delete();

        file = new File(reversalWithinTimeFrameForTransactionOutsideTimeFramePath);
        file.delete();


    }

}


