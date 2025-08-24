package supportly.supportlybackend;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import supportly.supportlybackend.Model.Activities;
import supportly.supportlybackend.Model.Order;
import supportly.supportlybackend.Model.Part;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDate;

import static com.itextpdf.text.Element.ALIGN_CENTER;
import static com.itextpdf.text.Font.BOLD;
import static com.itextpdf.text.Rectangle.NO_BORDER;

public class PdfGeneration {

    private static final DecimalFormat df = new DecimalFormat("0.00");


    private final String fontUrl = "src/main/java/pl/backendbscthesis/Font/FreeSans-LrmZ.ttf";

    private final BaseFont polishCharacter = BaseFont.createFont(fontUrl, BaseFont.CP1250, BaseFont.EMBEDDED);
    private final Font titleOfTableFont = new Font(polishCharacter, 16, BOLD);
    private final Font elementsOfTableFont = new Font(polishCharacter, 12);
    private final Font employeeFont = new Font(polishCharacter, 12);

    public PdfGeneration() throws DocumentException, IOException, IOException {
        employeeFont.setColor(BaseColor.RED);

    }

    public ByteArrayInputStream createProtocol(Order order) {

        Document document = new Document(PageSize.A4);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            PdfWriter.getInstance(document,out);
            document.open();


            PdfPTable titleTable = new PdfPTable(1);
            titleTable.setHorizontalAlignment(ALIGN_CENTER);
//            titleTable.addCell(new PdfPCell(new Paragraph("PROTOKÓŁ Z WYKONANYCH CZYNOŚCI SERWISOWYCH NR "+numberProtocol(order.getDateOfExecution(),order.getClient().getName()) , titleOfTableFont))).setHorizontalAlignment(ALIGN_CENTER);
            titleTable.setSpacingBefore(10f);
            titleTable.setSpacingAfter(12.5f);

            PdfPTable tableWithDataClient = new PdfPTable(2);
            tableWithDataClient.setWidths(new float[]{40, 60});
            tableWithDataClient.setHorizontalAlignment(ALIGN_CENTER);
            tableWithDataClient.addCell(new PdfPCell(new Paragraph("Klient: "))).setHorizontalAlignment(Element.ALIGN_RIGHT);
//            tableWithDataClient.addCell(new PdfPCell(new Paragraph(order.getClient().getName()))).setHorizontalAlignment(ALIGN_CENTER);
            tableWithDataClient.addCell(new PdfPCell(new Paragraph("Adres/Dane do faktury:"))).setHorizontalAlignment(Element.ALIGN_RIGHT);
            PdfPCell pdfPCell = new PdfPCell();
            pdfPCell.setHorizontalAlignment(ALIGN_CENTER);
//            pdfPCell.addElement(new Paragraph(order.getClient().getAddress() + " " + order.getClient().getStreetNumber() + "/" + order.getClient().getApartmentNumber()));
//            pdfPCell.addElement(new Paragraph(order.getClient().getZipcode() + " " + order.getClient().getCity()));
            pdfPCell.setColspan(2);
            tableWithDataClient.addCell(pdfPCell).setHorizontalAlignment(ALIGN_CENTER);
            tableWithDataClient.setSpacingBefore(20f);
            tableWithDataClient.setSpacingAfter(12.5f);

            PdfPTable tableWithDataEmployee = new PdfPTable(2);
            tableWithDataEmployee.setWidths(new float[]{40, 60});
            tableWithDataEmployee.addCell(new PdfPCell(new Paragraph("Rodzaj usługi: ", elementsOfTableFont))).setHorizontalAlignment(Element.ALIGN_RIGHT);
            tableWithDataEmployee.addCell(new PdfPCell(new Paragraph(order.getPeriod(), elementsOfTableFont))).setHorizontalAlignment(ALIGN_CENTER);
            tableWithDataEmployee.addCell(new PdfPCell(new Paragraph("Serwisant:", elementsOfTableFont))).setHorizontalAlignment(Element.ALIGN_RIGHT);
            tableWithDataEmployee.addCell(new PdfPCell(new Paragraph(order.getEmployeeList().get(0).getFirstName() + " " + order.getEmployeeList().get(0).getLastName(), employeeFont))).setHorizontalAlignment(ALIGN_CENTER);
            tableWithDataEmployee.addCell(new PdfPCell(new Paragraph("Data wykonania:", elementsOfTableFont))).setHorizontalAlignment(Element.ALIGN_RIGHT);
            tableWithDataEmployee.addCell(new PdfPCell(new Paragraph(order.getDateOfExecution().toString(), elementsOfTableFont))).setHorizontalAlignment(ALIGN_CENTER);
            tableWithDataEmployee.setSpacingBefore(10);
            tableWithDataEmployee.setSpacingAfter(12.5f);


            PdfPTable titleOfActivities = new PdfPTable(1);
            titleOfActivities.addCell(new PdfPCell(new Paragraph("LISTA CZYNOŚCI", titleOfTableFont))).setHorizontalAlignment(ALIGN_CENTER);
            titleOfActivities.setSpacingBefore(10);
            PdfPTable tableWithActivitie = new PdfPTable(2);
            tableWithActivitie.addCell(new PdfPCell(new Paragraph("Opis Czyności", elementsOfTableFont))).setHorizontalAlignment(ALIGN_CENTER);
            tableWithActivitie.addCell(new PdfPCell(new Paragraph("Informacje o wykonaniu", elementsOfTableFont))).setHorizontalAlignment(ALIGN_CENTER);

            tableWithActivitie.setSpacingAfter(12.5f);


            PdfPTable titleOfPartsAndMaterials = new PdfPTable(1);
            titleOfPartsAndMaterials.setSpacingBefore(10f);
            titleOfPartsAndMaterials.addCell(new PdfPCell(new Paragraph("ZAINSTALOWANE CZĘŚCI/MATERIAŁY", titleOfTableFont))).setHorizontalAlignment(ALIGN_CENTER);
            titleOfPartsAndMaterials.addCell(new PdfPCell(new Paragraph("Nazwa części,materiałów/Ilość", elementsOfTableFont))).setHorizontalAlignment(ALIGN_CENTER);
            PdfPTable tableWithPartsAndMaterials = new PdfPTable(2);
//            if (!order.getPartList().isEmpty()) {
//                for (Part parts : order.getPartList()) {
//                    tableWithPartsAndMaterials.addCell(new PdfPCell(new Paragraph(parts.getName(), elementsOfTableFont)));
////                    tableWithPartsAndMaterials.addCell(new PdfPCell(new Paragraph(String.valueOf(parts.getAmount()), elementsOfTableFont)));
//                }
//            } else {
//                tableWithPartsAndMaterials.addCell(new PdfPCell(new Paragraph("Brak", elementsOfTableFont))).setHorizontalAlignment(ALIGN_CENTER);
//            }
            tableWithPartsAndMaterials.setSpacingAfter(12.5f);

            PdfPTable tableWithComments = new PdfPTable(1);
            tableWithComments.addCell(new PdfPCell(new Paragraph("UWAGI- WNIOSKI-ZALECENIA", titleOfTableFont))).setHorizontalAlignment(ALIGN_CENTER);
            tableWithComments.addCell(new PdfPCell(new Paragraph(order.getNote(), elementsOfTableFont)));

            tableWithComments.setSpacingBefore(10f);
            tableWithComments.setSpacingAfter(12.5f);

            PdfPTable titleSettlement = new PdfPTable(1);
            titleSettlement.setSpacingBefore(10f);
            titleSettlement.addCell(new PdfPCell(new Paragraph("ROZLICZENIE USŁUGI", titleOfTableFont))).setHorizontalAlignment(ALIGN_CENTER);
            PdfPTable tableWithSettlement = new PdfPTable(2);
            tableWithSettlement.addCell(new PdfPCell(new Paragraph("Robocizna", elementsOfTableFont)));
            tableWithSettlement.addCell(new PdfPCell(new Paragraph(order.getManHour() + " godz", elementsOfTableFont)));
            tableWithSettlement.addCell(new PdfPCell(new Paragraph("Droga", elementsOfTableFont)));
            tableWithSettlement.addCell(new PdfPCell(new Paragraph(order.getDistance() + " km", elementsOfTableFont)));
            tableWithSettlement.setSpacingAfter(12.5f);

            PdfPTable table = new PdfPTable(2);
            table.setWidths(new float[]{55f, 45f});
            PdfPCell pdfPCell1 = new PdfPCell(new Paragraph("USŁUGĘ WYKONAŁ",titleOfTableFont));
            pdfPCell1.setBorder(NO_BORDER);
            PdfPCell pdfPCell2 = new PdfPCell(new Paragraph("POTWIERDZAM WYKONANIE",titleOfTableFont));
            pdfPCell2.setVerticalAlignment(Element.ALIGN_RIGHT);
            pdfPCell2.setBorder(NO_BORDER);
            table.addCell(pdfPCell1);
            table.addCell(pdfPCell2);

            table.setSpacingBefore(37.5f);


            document.add(titleTable);
            document.add(tableWithDataClient);
            document.add(tableWithDataEmployee);
            document.add(titleOfActivities);
            document.add(tableWithActivitie);
            document.add(titleOfPartsAndMaterials);
            document.add(tableWithPartsAndMaterials);
            document.add(tableWithComments);
            document.add(titleSettlement);
            document.add(tableWithSettlement);
            document.add(table);

            document.close();

        } catch (DocumentException e) {
            e.printStackTrace();
        }

        return new ByteArrayInputStream(out.toByteArray());
    }

    public ByteArrayInputStream createInvoice(Order order) {
        float summaryParts = 0;

        Document document = new Document(PageSize.A4);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            PdfWriter.getInstance(document,out);
            document.open();


            PdfPTable titleTable = new PdfPTable(1);
            titleTable.setHorizontalAlignment(ALIGN_CENTER);
//            titleTable.addCell(new PdfPCell(new Paragraph("Faktura "+numberInvoice(order.getDateOfExecution(),order.getClient().getName()) , titleOfTableFont))).setHorizontalAlignment(ALIGN_CENTER);
            titleTable.setSpacingBefore(10f);
            titleTable.setSpacingAfter(12.5f);

            PdfPTable tableWithDataClient = new PdfPTable(2);
            tableWithDataClient.setWidths(new float[]{40, 60});
            tableWithDataClient.setHorizontalAlignment(ALIGN_CENTER);
            tableWithDataClient.addCell(new PdfPCell(new Paragraph("Klient: "))).setHorizontalAlignment(Element.ALIGN_RIGHT);
//            tableWithDataClient.addCell(new PdfPCell(new Paragraph(order.getClient().getName()))).setHorizontalAlignment(ALIGN_CENTER);
            tableWithDataClient.addCell(new PdfPCell(new Paragraph("Adres/Dane do faktury:"))).setHorizontalAlignment(Element.ALIGN_RIGHT);
            PdfPCell pdfPCell = new PdfPCell();
            pdfPCell.setHorizontalAlignment(ALIGN_CENTER);
//            pdfPCell.addElement(new Paragraph(order.getClient().getAddress() + " " + order.getClient().getStreetNumber() + "/" + order.getClient().getApartmentNumber()));
//            pdfPCell.addElement(new Paragraph(order.getClient().getZipcode() + " " + order.getClient().getCity()));
            pdfPCell.setColspan(2);
            tableWithDataClient.addCell(pdfPCell).setHorizontalAlignment(ALIGN_CENTER);
            tableWithDataClient.setSpacingBefore(20f);
            tableWithDataClient.setSpacingAfter(12.5f);

            PdfPTable tableWithDataEmployee = new PdfPTable(2);
            tableWithDataEmployee.setWidths(new float[]{40, 60});
            tableWithDataEmployee.addCell(new PdfPCell(new Paragraph("Rodzaj usługi: ", elementsOfTableFont))).setHorizontalAlignment(Element.ALIGN_RIGHT);
            tableWithDataEmployee.addCell(new PdfPCell(new Paragraph(order.getPeriod(), elementsOfTableFont))).setHorizontalAlignment(ALIGN_CENTER);
            tableWithDataEmployee.addCell(new PdfPCell(new Paragraph("Serwisant:", elementsOfTableFont))).setHorizontalAlignment(Element.ALIGN_RIGHT);
            tableWithDataEmployee.addCell(new PdfPCell(new Paragraph(order.getEmployeeList().get(0).getFirstName() + " " + order.getEmployeeList().get(0).getLastName(), employeeFont))).setHorizontalAlignment(ALIGN_CENTER);
            tableWithDataEmployee.addCell(new PdfPCell(new Paragraph("Data wykonania:", elementsOfTableFont))).setHorizontalAlignment(Element.ALIGN_RIGHT);
            tableWithDataEmployee.addCell(new PdfPCell(new Paragraph(order.getDateOfExecution().toString(), elementsOfTableFont))).setHorizontalAlignment(ALIGN_CENTER);
            tableWithDataEmployee.setSpacingBefore(10);
            tableWithDataEmployee.setSpacingAfter(12.5f);


            PdfPTable titleOfActivities = new PdfPTable(1);
            titleOfActivities.addCell(new PdfPCell(new Paragraph("LISTA CZYNOŚCI", titleOfTableFont))).setHorizontalAlignment(ALIGN_CENTER);
            titleOfActivities.setSpacingBefore(10);
            PdfPTable tableWithActivitie = new PdfPTable(2);
            tableWithActivitie.addCell(new PdfPCell(new Paragraph("Opis Czyności", elementsOfTableFont))).setHorizontalAlignment(ALIGN_CENTER);
            tableWithActivitie.addCell(new PdfPCell(new Paragraph("Informacje o wykonaniu", elementsOfTableFont))).setHorizontalAlignment(ALIGN_CENTER);
            tableWithActivitie.setSpacingAfter(12.5f);


            PdfPTable titleOfPartsAndMaterials = new PdfPTable(1);
            titleOfPartsAndMaterials.setSpacingBefore(10f);
            titleOfPartsAndMaterials.addCell(new PdfPCell(new Paragraph("ZAINSTALOWANE CZĘŚCI/MATERIAŁY", titleOfTableFont))).setHorizontalAlignment(ALIGN_CENTER);
            titleOfPartsAndMaterials.addCell(new PdfPCell(new Paragraph("Nazwa części,materiałów/Ilość/Kwota", elementsOfTableFont))).setHorizontalAlignment(ALIGN_CENTER);
            PdfPTable tableWithPartsAndMaterials = new PdfPTable(5);
//            if (!order.getPartList().isEmpty()) {
//                for (Part parts : order.getPartList()) {
//                    tableWithPartsAndMaterials.addCell(new PdfPCell(new Paragraph(parts.getName(), elementsOfTableFont)));
////                    tableWithPartsAndMaterials.addCell(new PdfPCell(new Paragraph(String.valueOf(parts.getAmount()), elementsOfTableFont)));
//                    tableWithPartsAndMaterials.addCell(new PdfPCell(new Paragraph(parts.getPrice()+" zł", elementsOfTableFont)));
//                    tableWithPartsAndMaterials.addCell(new PdfPCell(new Paragraph(parts.getTax() + "%", elementsOfTableFont)));
//                    tableWithPartsAndMaterials.addCell(new PdfPCell(new Paragraph(df.format(parts.getTax()*parts.getPrice()*10), elementsOfTableFont)));
////                    summaryParts += ((parts.getTax()*parts.getPrice())*parts.getAmount())*10;
//                }
//            } else {
//                tableWithPartsAndMaterials.addCell(new PdfPCell(new Paragraph("Brak", elementsOfTableFont))).setHorizontalAlignment(ALIGN_CENTER);
//            }
            tableWithPartsAndMaterials.setSpacingAfter(12.5f);

            PdfPTable tableWithComments = new PdfPTable(1);
            tableWithComments.addCell(new PdfPCell(new Paragraph("UWAGI- WNIOSKI-ZALECENIA", titleOfTableFont))).setHorizontalAlignment(ALIGN_CENTER);
            tableWithComments.addCell(new PdfPCell(new Paragraph(order.getNote(), elementsOfTableFont)));

            tableWithComments.setSpacingBefore(10f);
            tableWithComments.setSpacingAfter(12.5f);

            PdfPTable titleSettlement = new PdfPTable(1);
            titleSettlement.setSpacingBefore(10f);
            titleSettlement.addCell(new PdfPCell(new Paragraph("ROZLICZENIE USŁUGI", titleOfTableFont))).setHorizontalAlignment(ALIGN_CENTER);
            PdfPTable tableWithSettlement = new PdfPTable(4);
            tableWithSettlement.addCell(new PdfPCell(new Paragraph("Robocizna", elementsOfTableFont)));
            tableWithSettlement.addCell(new PdfPCell(new Paragraph(order.getManHour() + " godz", elementsOfTableFont)));
            tableWithSettlement.addCell(new PdfPCell(new Paragraph(50 + " zł", elementsOfTableFont)));
            tableWithSettlement.addCell(new PdfPCell(new Paragraph(order.getManHour()*50 + " zł", elementsOfTableFont)));
            tableWithSettlement.addCell(new PdfPCell(new Paragraph("Droga", elementsOfTableFont)));
            tableWithSettlement.addCell(new PdfPCell(new Paragraph(order.getDistance() + " km", elementsOfTableFont)));
            tableWithSettlement.addCell(new PdfPCell(new Paragraph(2 + " zł", elementsOfTableFont)));
            tableWithSettlement.addCell(new PdfPCell(new Paragraph(order.getDistance()*2 + " zł", elementsOfTableFont)));
            tableWithSettlement.setSpacingAfter(12.5f);

            PdfPTable summary = new PdfPTable(1);
            summary.setSpacingBefore(10f);
            summary.addCell(new PdfPCell(new Paragraph("PODSUMOWANIE ROZLICZENIA", titleOfTableFont))).setHorizontalAlignment(ALIGN_CENTER);
            String summaryValue = df.format((order.getManHour()*50)+(order.getDistance()*2) + summaryParts);
            summary.addCell(new PdfPCell(new Paragraph( "Do zapłaty jest: "+ summaryValue + " zł",elementsOfTableFont)));

            PdfPTable table = new PdfPTable(2);
            table.setWidths(new float[]{55f, 45f});
            PdfPCell pdfPCell1 = new PdfPCell(new Paragraph("USŁUGĘ WYKONAŁ",titleOfTableFont));
            pdfPCell1.setBorder(NO_BORDER);
            PdfPCell pdfPCell2 = new PdfPCell(new Paragraph("POTWIERDZAM WYKONANIE",titleOfTableFont));
            pdfPCell2.setVerticalAlignment(Element.ALIGN_RIGHT);
            pdfPCell2.setBorder(NO_BORDER);
            table.addCell(pdfPCell1);
            table.addCell(pdfPCell2);

            table.setSpacingBefore(37.5f);


            document.add(titleTable);
            document.add(tableWithDataClient);
            document.add(tableWithDataEmployee);
            document.add(titleOfActivities);
            document.add(tableWithActivitie);
            document.add(titleOfPartsAndMaterials);
            document.add(tableWithPartsAndMaterials);
            document.add(tableWithComments);
            document.add(titleSettlement);
            document.add(tableWithSettlement);
            document.add(summary);
            document.add(table);
            document.close();

        } catch (DocumentException e) {
            e.printStackTrace();
        }

        return new ByteArrayInputStream(out.toByteArray());
    }

    private String numberProtocol(LocalDate dateOfExecution, String name){
        return "PROT/"+name+"/"+dateOfExecution.toString();
    }

    private String numberInvoice(LocalDate dateOfExecution, String name){
        return "Faktura/"+name+"/"+dateOfExecution.toString();
    }






}
