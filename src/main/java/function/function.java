package function;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.border.DashedBorder;
import com.itextpdf.layout.border.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.util.Arrays;

public class function {
    private static final String PREFIX = "INV";
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
    private static final int ITERATIONS = 100000;
    private static final int KEY_LENGTH = 256;
    private static final String ALGORITHM = "PBKDF2WithHmacSHA256";

    public static String generateInvoiceNumber() {
        // Generate a timestamp string
        String timestamp = dateFormat.format(new Date());

        // Generate a random 4-digit number
        Random random = new Random();
        int randomNumber = random.nextInt(9000) + 1000; // ensures a 4-digit number
        String randomPart = String.valueOf(randomNumber);

        // Concatenate the prefix, timestamp, and random number
        String invoiceNumber = PREFIX + "-" + timestamp + "-" + randomPart;

        return invoiceNumber;
    }
    public static String generateRandomNumber() {
        Random random = new Random();
        StringBuilder number = new StringBuilder();

        for (int i = 0; i < 6; i++) {
            int digit = random.nextInt(10); // Generates random digit (0-9)
            number.append(digit);
        }

        return number.toString();
    }
    public static String hashPassword(String password) {
        try {
            // Create a MessageDigest instance for SHA-256
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            // Perform the hashing
            byte[] hashedBytes = digest.digest(password.getBytes());

            // Convert bytes to hexadecimal representation
            StringBuilder hexString = new StringBuilder();
            for (byte hashedByte : hashedBytes) {
                String hex = Integer.toHexString(0xff & hashedByte);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            // Return hashed password in hexadecimal format
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            // Handle exception if algorithm is not available
            e.printStackTrace();
            return null;
        }
    }
    public static <Date> String Dateformat(String date){
        SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");

        try {
            // Parse the input date string
            Date inputDate = (Date) inputFormat.parse(date);

            // Format the date into the desired format
            String outputDateStr = outputFormat.format(inputDate);

            // Print the formatted date
            System.out.println("Formatted date: " + outputDateStr);
            return outputDateStr;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }
    public static void generatePdf(String filePath, String invoiceNumber,String logoPath,String navPath,String s_name,String r_name,String date,String s_account,String s_phone,String r_account,String r_phone,String amount_d,String fee,String r) throws FileNotFoundException, MalformedURLException {

        PdfWriter pdfWriter = new PdfWriter(filePath);
        PdfDocument pdfDocument = new PdfDocument(pdfWriter);
        pdfDocument.setDefaultPageSize(PageSize.A4);

        Document document = new Document(pdfDocument);

        // Logo at the back of the PDF
        ImageData logoData = ImageDataFactory.create(logoPath);
        Image logo = new Image(logoData);

        float x = pdfDocument.getDefaultPageSize().getWidth() / 2;
        float y = pdfDocument.getDefaultPageSize().getHeight() / 2;
        logo.setFixedPosition(x - 170, y - 150);
        logo.setOpacity(0.1f);
        document.add(logo);

        // Logo at the back of the PDF
        ImageData imageData = ImageDataFactory.create(navPath);
        Image image = new Image(imageData);

        float a = pdfDocument.getDefaultPageSize().getWidth() / 2;
        float b = pdfDocument.getDefaultPageSize().getHeight() / 2;
        image.setFixedPosition(a - 280, b + 340);
        image.setOpacity(1f);
        document.add(image);


        float threecol = 190f;
        float twocol = 285f;
        float twocol150 = twocol + 150;
        float twocolWidth[] = {twocol150,twocol};
        float twocolWidth2[] = {twocol,twocol};
        float oneColumnWidth[] = {twocol150};
        float fullwidth[] = {threecol * 3};
        Paragraph onesp = new Paragraph("\n");

        Table table = new  Table(twocolWidth2);
        table.addCell(new Cell().add(" ").setBorder(Border.NO_BORDER));
        Table nestedtable = new Table(new float[]{twocol/2,800f});
        nestedtable.addCell(getHeaderTextCell("Address :"));
        nestedtable.addCell(getHeaderTextCellValue("Kinabalu Residential College, \n50603 Kuala Lumpur, \nFederal Territory of Kuala Lumpur"));

        table.addCell(new Cell().add(nestedtable).setBorder(Border.NO_BORDER));

        Border grayborder = new SolidBorder(Color.GRAY, 2f);
        Table dividingLine = new Table(fullwidth);
        dividingLine.setBorder(grayborder);

        document.add(table);
        document.add(onesp);
        document.add(dividingLine);

        Table table2 = new  Table(twocolWidth);
        table2.addCell(new Cell().add("Transaction Receipt").setFontSize(20f).setBorder(Border.NO_BORDER).setBold());
        Table nestedtable2 = new Table(new float[]{twocol/2,twocol/2});
        nestedtable2.addCell(getHeaderTextCell("Invoice No.  :"));
        nestedtable2.addCell(getHeaderTextCellValue(invoiceNumber));
        nestedtable2.addCell(getHeaderTextCell("Invoice Date :"));
        nestedtable2.addCell(getHeaderTextCellValue(date));

        table2.addCell(new Cell().add(nestedtable2).setBorder(Border.NO_BORDER));

        document.add(table2);
        document.add(dividingLine);
        document.add(onesp);

        Table twoColTable = new Table(twocolWidth);
        twoColTable.addCell(getSendandReceiveCell("From: "));
        twoColTable.addCell(getSendandReceiveCell("To: "));
        document.add(twoColTable.setMarginBottom(12f));

        Table twoColTable2 = new Table(twocolWidth);
        twoColTable2.addCell(getCell10fLeft("Name",true));
        twoColTable2.addCell(getCell10fLeft("Name",true));
        twoColTable2.addCell(getCell10fLeft(s_name,false));
        twoColTable2.addCell(getCell10fLeft(r_name,false));
        document.add(twoColTable2);

        Table twoColTable3 = new Table(twocolWidth);
        twoColTable3.addCell(getCell10fLeft("Account",true));
        twoColTable3.addCell(getCell10fLeft("Account",true));
        twoColTable3.addCell(getCell10fLeft(s_account,false));
        twoColTable3.addCell(getCell10fLeft(r_account,false));
        document.add(twoColTable3);

        Table twoColTable4 = new Table(twocolWidth);
        twoColTable4.addCell(getCell10fLeft("Phone No.",true));
        twoColTable4.addCell(getCell10fLeft("Phone No.",true));
        twoColTable4.addCell(getCell10fLeft(s_phone,false));
        twoColTable4.addCell(getCell10fLeft(r_phone,false));
        document.add(twoColTable4.setMarginBottom(10f));

        Table tableDivider = new Table(fullwidth);
        Border dgb = new DashedBorder(Color.GRAY, 0.5f);
        document.add(tableDivider.setBorder(dgb));

        Paragraph productPara = new Paragraph("Transaction Details");
        document.add(productPara.setBold());

        Table twoColTable5 = new Table(twocolWidth);
        twoColTable5.addCell(getCell10fLeft("Amount deducted: ",true));
        twoColTable5.addCell(getCell10fLeft("Amount received: ",true));
        twoColTable5.addCell(getCell10fLeft(amount_d,false));
        twoColTable5.addCell(getCell10fLeft(r,false));
        document.add(twoColTable5);

        Table oneColTable = new Table(oneColumnWidth);
        oneColTable.addCell(getCell10fLeft("Processing Fee: ",true));
        oneColTable.addCell(getCell10fLeft(fee,false));
        document.add(oneColTable.setMarginBottom(10f));

        document.add(dividingLine.setBorder(new SolidBorder(Color.GRAY, 1)).setMarginBottom(15f));

        Table thankYou = new Table(fullwidth);
        thankYou.addCell(new Cell().add("""
                Thank you for entrusting your gold and treasures to our mystical vaults! \
                Your patronage fuels the magic that keeps our wizarding bank thriving. \
                May your fortunes multiply like potions brewing in a cauldron, \
                and may your journeys always lead you back to our enchanted halls. \
                Until next we meet, may your coins jingle with the melody of prosperity!

                For any inquiries or further assistance, send an owl to us at wizzzz@gmail.com.""").setBorder(Border.NO_BORDER));
        document.add(thankYou.setMarginBottom(10f));

        document.add(dividingLine.setBorder(new SolidBorder(Color.GRAY, 1)).setMarginBottom(15f));

        Table tb = new Table(fullwidth);
        tb.addCell(new Cell().add("TERMS AND CONDITIONS\n").setBold().setBorder(Border.NO_BORDER));
        List<String> TncList = new ArrayList<>();
        TncList.add("1. The receipt is generated with the user's knowledge and serves as proof of the transaction.");
        TncList.add("2. Wizard Bank is not responsible for any loss incurred after the transaction is complete.");
        TncList.add("3. By completing this transaction, you consent to the collection and use of your information.");

        for(String tnc:TncList){
            tb.addCell(new Cell().add(tnc).setBorder(Border.NO_BORDER));
        }
        document.add(tb);

        document.close();

        System.out.println("PDF is generated.");
    }
    static Cell getHeaderTextCell(String textValue){
        return new Cell().add(textValue).setBold().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.RIGHT);
    }

    static Cell getHeaderTextCellValue(String textValue){
        return new Cell().add(textValue).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.RIGHT);
    }

    static Cell getSendandReceiveCell(String textValue){
        return new Cell().add(textValue).setFontSize(12f).setBold().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT);
    }

    static Cell getCell10fLeft(String textValue, Boolean isBold){
        Cell myCell = new Cell().add(textValue).setFontSize(10f).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT);
        return isBold ?myCell.setBold():myCell;
    }
    public static byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return salt;
    }
    public static byte[] hashPassword(char[] password, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        PBEKeySpec spec = new PBEKeySpec(password, salt, ITERATIONS, KEY_LENGTH);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
        return keyFactory.generateSecret(spec).getEncoded();
    }
    public static boolean verifyPassword(char[] password, byte[] salt, byte[] expectedHash) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] hash = hashPassword(password, salt);
        return Arrays.equals(hash, expectedHash);
    }
}
