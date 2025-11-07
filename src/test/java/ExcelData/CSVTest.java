package ExcelData;

import Payloads.Payload;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.testng.annotations.Test;
import java.io.IOException;


import static UtilityPackage.Utility.csvReadUtility;

public class CSVTest {

    @Test
    public void getDataFromCSVFile() throws IOException {

        String csvFilePath = System.getProperty("user.dir") + "/src/main/java/Payloads/TestData.csv";
        CSVParser csvParserObj = csvReadUtility(csvFilePath);

        //Get headers of CSV file
        System.out.println("Header are " + csvParserObj.getHeaderNames());

        /*
        List<CSVRecord> records = csvParserObj.getRecords();
        System.out.println("First data = "+records.get(0));
        System.out.print("Second data = "+records.get(1));*/

        //Iterate through records
        for (CSVRecord csvrecord : csvParserObj) {
            String name = csvrecord.get("Name");
            String address = csvrecord.get("Address");
            System.out.println("Name from CSV file = " + name);
            System.out.println("Address from CSV file = " + address);

        }



    }

}