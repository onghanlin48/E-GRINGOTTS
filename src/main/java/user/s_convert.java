package user;

import ajax.check_amount;
import data.Account;
import data.resend;
import db.DBConnection;
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
import function.JavaMail;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import javax.mail.MessagingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;

import static function.function.generateInvoiceNumber;

@WebServlet("/s_convert")
public class s_convert extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        String code = req.getParameter("code");
        resend send = (resend) session.getAttribute("send");
        Account account = (Account) session.getAttribute("account");
        int attempt = send.getResend();
        System.out.println(attempt);
        String code_c = account.getCode();
        if(attempt != 0){
            if(code.equals(code_c)){
                check_amount check  = (check_amount) session.getAttribute("check_amount");
                int id = account.getUserId();
                int from = check.getFrom();
                int get = check.getTo();
                double amount = check.getAmount();
                double t = check.getT();
                double fee = check.getFee();
                double converting = check.getConverting();
                String invoiceNumber = generateInvoiceNumber();
                LocalDate currentDate = LocalDate.now();
                String s = check.getS();
                String g = check.getGet();

                try(Connection connection = DBConnection.getConnection()){
                    String sql = "update amount set amount=? where userid=? AND cid=?";
                    String addsql = "update amount set amount= amount + ? where userid=? AND cid=?";
                    String insertSql = "INSERT INTO history (invoice, form, receive, amount, currency, date, category, reference, status, user) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                    try(PreparedStatement ps = connection.prepareStatement(sql)){
                        ps.setDouble(1, amount);
                        ps.setInt(2, id);
                        ps.setInt(3, from);
                        int row =  ps.executeUpdate();

                        if(row>0){
                           try(PreparedStatement ps2 = connection.prepareStatement(addsql)){
                               ps2.setDouble(1, converting);
                               ps2.setInt(2, id);
                               ps2.setInt(3, get);

                               row =  ps2.executeUpdate();
                               if(row>0){
                                  try(PreparedStatement insertStatement = connection.prepareStatement(insertSql)){
                                      insertStatement.setString(1, invoiceNumber);
                                      insertStatement.setInt(2, id);
                                      insertStatement.setInt(3, id);
                                      insertStatement.setDouble(4, converting);
                                      insertStatement.setInt(5, get);
                                      insertStatement.setDate(6, Date.valueOf(currentDate));
                                      insertStatement.setString(7, "Convert Currency");
                                      insertStatement.setString(8, "Convert Currency");
                                      insertStatement.setInt(9, 1);
                                      insertStatement.setString(10, "Online");
                                      insertStatement.executeUpdate();

                                      insertStatement.setString(1, invoiceNumber);
                                      insertStatement.setInt(2, id);
                                      insertStatement.setInt(3, 1);
                                      insertStatement.setDouble(4, fee);
                                      insertStatement.setInt(5, from);
                                      insertStatement.setDate(6, Date.valueOf(currentDate));
                                      insertStatement.setString(7, "Fee");
                                      insertStatement.setString(8, "Processing Fee");
                                      insertStatement.setInt(9, 2);
                                      insertStatement.setString(10, "Online");
                                      insertStatement.executeUpdate();

                                      insertStatement.setString(1, invoiceNumber);
                                      insertStatement.setInt(2, id);
                                      insertStatement.setInt(3, id);
                                      insertStatement.setDouble(4, t);
                                      insertStatement.setInt(5, from);
                                      insertStatement.setDate(6, Date.valueOf(currentDate));
                                      insertStatement.setString(7, "Convert Currency");
                                      insertStatement.setString(8, "Convert Currency");
                                      insertStatement.setInt(9, 2);
                                      insertStatement.setString(10, "Online");
                                      insertStatement.executeUpdate();

                                      String filePath = getServletContext().getRealPath("/invoice/" + invoiceNumber + ".pdf");
                                      File invoiceDir = new File("invoice");
                                      if (!invoiceDir.exists()) {
                                          invoiceDir.mkdirs(); // Create directory if it doesn't exist
                                      }
                                      PdfWriter pdfWriter = new PdfWriter(filePath);
                                      PdfDocument pdfDocument = new PdfDocument(pdfWriter);
                                      pdfDocument.setDefaultPageSize(PageSize.A4);

                                      Document document = new Document(pdfDocument);

                                      // Logo at the back of the PDF
                                      ImageData logoData = ImageDataFactory.create(getServletContext().getRealPath("/Image/logo.png"));
                                      Image logo = new Image(logoData);

                                      float x = pdfDocument.getDefaultPageSize().getWidth() / 2;
                                      float y = pdfDocument.getDefaultPageSize().getHeight() / 2;
                                      logo.setFixedPosition(x - 170, y - 150);
                                      logo.setOpacity(0.1f);
                                      document.add(logo);

                                      // Logo at the back of the PDF
                                      ImageData imageData = ImageDataFactory.create(getServletContext().getRealPath("/Image/Navbar.png"));
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
                                      float threecolWidth[] = {threecol,threecol,threecol};
                                      float onetwo[] = {threecol + 125f, threecol *2};
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
                                      table2.addCell(new Cell().add("Currency Exchange Receipt").setFontSize(20f).setBorder(Border.NO_BORDER).setBold());
                                      Table nestedtable2 = new Table(new float[]{twocol/2,twocol/2});
                                      nestedtable2.addCell(getHeaderTextCell("Invoice No.  :"));
                                      nestedtable2.addCell(getHeaderTextCellValue(invoiceNumber));
                                      nestedtable2.addCell(getHeaderTextCell("Invoice Date :"));
                                      nestedtable2.addCell(getHeaderTextCellValue(String.valueOf(currentDate)));

                                      table2.addCell(new Cell().add(nestedtable2).setBorder(Border.NO_BORDER));

                                      document.add(table2);
                                      document.add(dividingLine);
                                      document.add(onesp);

                                      Table tableDivider = new Table(fullwidth);
                                      Border dgb = new DashedBorder(Color.GRAY, 0.5f);
                                      document.add(tableDivider.setBorder(dgb));

                                      Paragraph productPara = new Paragraph("Conversion Details");
                                      document.add(productPara.setBold());

                                      Table Col2Table = new Table(twocolWidth);
                                      Col2Table.setBackgroundColor(Color.BLACK, 0.7f);
                                      Col2Table.addCell(new Cell().add("Description").setBold().setFontColor(Color.WHITE).setBorder(Border.NO_BORDER));
                                      Col2Table.addCell(new Cell().add("Amount").setBold().setFontColor(Color.WHITE).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER));
                                      document.add(Col2Table);

                                      // Amount purely for currency change
                                      Table twoColTable = new Table(twocolWidth);
                                      twoColTable.addCell(new Cell().add("Amount to be converted").setMarginLeft(10f).setBorder(Border.NO_BORDER));
                                      twoColTable.addCell(new Cell().add(String.valueOf(t)+ " " + s).setTextAlignment(TextAlignment.RIGHT).setMarginRight(15f).setBorder(Border.NO_BORDER));
                                      document.add(twoColTable);

                                      // Processing Fee
                                      Table twoColTable2 = new Table(twocolWidth);
                                      twoColTable2.addCell(new Cell().add("Processing Fee").setMarginLeft(10f).setBorder(Border.NO_BORDER));
                                      twoColTable2.addCell(new Cell().add(String.valueOf(fee)+ " " + s).setTextAlignment(TextAlignment.RIGHT).setMarginRight(15f).setBorder(Border.NO_BORDER));
                                      document.add(twoColTable2);

                                      // Amount to be Deducted from Account
                                      Table twoColTable3 = new Table(twocolWidth);
                                      twoColTable3.addCell(new Cell().add("Amount Deducted").setMarginLeft(10f).setBorder(Border.NO_BORDER));
                                      twoColTable3.addCell(new Cell().add(String.valueOf(t + fee) + " " + s).setTextAlignment(TextAlignment.RIGHT).setMarginRight(15f).setBorder(Border.NO_BORDER));
                                      document.add(twoColTable3);

                                      // Conversion Rate
                                      Table twoColTable4 = new Table(twocolWidth);
                                      twoColTable4.addCell(new Cell().add("Conversion Rate").setMarginLeft(10f).setBorder(Border.NO_BORDER));
                                      twoColTable4.addCell(new Cell().add(check.getRate()).setTextAlignment(TextAlignment.RIGHT).setMarginRight(15f).setBorder(Border.NO_BORDER));
                                      document.add(twoColTable4);

                                      Table threeColTable4 = new Table(onetwo);
                                      threeColTable4.addCell(new Cell().add("").setBorder(Border.NO_BORDER));
                                      threeColTable4.addCell(new Cell().add(tableDivider).setBorder(Border.NO_BORDER));
                                      document.add(threeColTable4);

                                      //Amount Converted
                                      Table threeColTable3 = new Table(threecolWidth);
                                      threeColTable3.addCell(new Cell().add("").setBorder(Border.NO_BORDER)).setMarginLeft(10f);
                                      threeColTable3.addCell(new Cell().add("Amount Converted").setBorder(Border.NO_BORDER)).setBold().setTextAlignment(TextAlignment.CENTER);
                                      threeColTable3.addCell(new Cell().add(String.valueOf(converting) + " " + g).setBorder(Border.NO_BORDER)).setMarginRight(15f).setTextAlignment(TextAlignment.RIGHT);
                                      document.add(threeColTable3);

                                      document.add(tableDivider.setMarginBottom(15f));

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

                                      try {
                                          JavaMail.sendmail(account.getEmail(), "Successfully Convert!","Invoice Convert",filePath);
                                      } catch (MessagingException e) {
                                          throw new RuntimeException(e);
                                      }
                                      check.clear();
                                      req.setAttribute("des", "Convert Successfully!");
                                      req.setAttribute("page", "Home");
                                      RequestDispatcher rd = req.getRequestDispatcher("Success.jsp");
                                      rd.forward(req, resp);
                                  }
                               }
                           }
                        }
                        connection.close();
                    }

                } catch (SQLException e) {
                    check.clear();
                    e.printStackTrace();
                    System.out.println("Error connecting to the database: " + e.getMessage());
                    req.setAttribute("des", "Please contact admin!");
                    req.setAttribute("page", "Home");
                    RequestDispatcher rd = req.getRequestDispatcher("Failed.jsp");
                    rd.forward(req, resp);

                } catch (ClassNotFoundException e) {
                    check.clear();
                    req.setAttribute("des", "Please contact admin!");
                    req.setAttribute("page", "Home");
                    RequestDispatcher rd = req.getRequestDispatcher("Failed.jsp");
                    rd.forward(req, resp);
                    throw new RuntimeException(e);

                }

            }else{
                attempt = attempt - 1;
                send.setResend(attempt);
                session.setAttribute("send",send);
                req.setAttribute("account", account);
                req.setAttribute("resend", send);
                RequestDispatcher rd = req.getRequestDispatcher("verify.jsp");
                rd.forward(req, resp);
            }
        }else{
            req.setAttribute("account", account);
            req.setAttribute("resend", send);
            RequestDispatcher rd = req.getRequestDispatcher("verify.jsp");
            rd.forward(req, resp);
        }
    }
    static Cell getHeaderTextCell(String textValue){
        return new Cell().add(textValue).setBold().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.RIGHT);
    }

    static Cell getHeaderTextCellValue(String textValue){
        return new Cell().add(textValue).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.RIGHT);
    }
}