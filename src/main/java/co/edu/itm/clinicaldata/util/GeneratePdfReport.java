package co.edu.itm.clinicaldata.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import org.apache.log4j.Logger;

import co.edu.itm.clinicaldata.model.Investigator;
import co.edu.itm.clinicaldata.model.ProcessResource;
import co.edu.itm.clinicaldata.model.ProcessingRequest;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class GeneratePdfReport {

    private static final String SPACE = " ";
    private static final int TABLE_100_PERCENT = 100;
    private static final String ITM_LOGO = "http://clusteri.itm.edu.co/wiki/img/tiki/Tiki_WCG.png";
    private static final int FONT_SIZE_BODY = 8;
    private static final int FONT_SIZE_HEAD = 10;
    private static final Font HEAD_FONT = FontFactory.getFont(FontFactory.HELVETICA_BOLD, FONT_SIZE_HEAD);
    private static final Font BODY_FONT = FontFactory.getFont(FontFactory.HELVETICA, FONT_SIZE_BODY);

    private static final Logger LOGGER = Logger.getLogger(GeneratePdfReport.class.getName());

    public static ByteArrayInputStream processRequest(Investigator investigator,
            List<ProcessingRequest> listProcessingRequest,
            List<ProcessResource> listProcessResource) {
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(TABLE_100_PERCENT);
            table.setWidths(new int[]{1, 4, 2, 2, 1});

            table.addCell(headCell("Id"));
            table.addCell(headCell("Identificador"));
            table.addCell(headCell("F. creación"));
            table.addCell(headCell("Archivo"));
            table.addCell(headCell("Estado"));

            for (ProcessingRequest request : listProcessingRequest) {
                table.addCell(bodyCell(request.getId().toString()));
                table.addCell(bodyCell(request.getIdentifier()));
                table.addCell(bodyCell(DateUtilities.timestampToString(request.getCreationDate())));
                table.addCell(bodyCell(request.getFileName()));
                table.addCell(bodyCell(request.getState()));

                addResources(table, listProcessResource);
                addResult(table, request);
            }

            PdfWriter.getInstance(document, out);
            document.open();

            document.add(createHeader(investigator, "REPORTE DE PROCESAMIENTO DE SOLICITUD"));
            document.add(table);
            document.close();
        } catch (DocumentException ex) {
            LOGGER.error(String
                    .format("Ocurrió un error en la generación del PDF, para el investigador <%d>",
                            investigator.getId()), ex);
        }
        return new ByteArrayInputStream(out.toByteArray());
    }

    private static void addResult(PdfPTable table, ProcessingRequest request) {
        if(!Validations.field(request.getResult())){
            PdfPCell cell = bodyCell("Resultado: " + request.getResult());
            cell.setColspan(5);
            table.addCell(cell);
        }
    }

    private static void addResources(PdfPTable table, List<ProcessResource> listProcessResource) {
        if(!Validations.field(listProcessResource)){
            StringBuilder sb = new StringBuilder();
            String separator = "";
            for (ProcessResource processResource : listProcessResource) {
                sb.append(separator);
                sb.append(processResource.getName());
                if(!Validations.field(processResource.getVersion())){
                    sb.append(SPACE);
                    sb.append("V:");
                    sb.append(processResource.getVersion());
                    sb.append(SPACE);
                    separator = ", ";
                }
            }

            PdfPCell cell = bodyCell("Recursos adicionales: " + sb.toString());
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setColspan(5);
            table.addCell(cell);
        }
    }

    private static PdfPCell getImageHeaderCell() {
        Image image = null;
        try {
            image = Image.getInstance(new URL(ITM_LOGO));
            image.scalePercent(40);
        } catch (IOException | BadElementException e) {
            LOGGER.info("Ocurrió un error obteniendo el logo para el PDF.", e);
        }
        PdfPCell cell = new PdfPCell(image);
        alignCellToCenter(cell);
        return cell;
    }

    private static void alignCellToCenter(PdfPCell cell) {
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorderWidth(1);
    }

    private static PdfPCell getNameHeaderCell(String title) {
        PdfPCell cell = new PdfPCell(new Paragraph(title, HEAD_FONT));
        alignCellToCenter(cell);
        return cell;
    }

    private static PdfPCell headerCell(String name){
        return new PdfPCell(new Paragraph(name, BODY_FONT));
    }

    private static PdfPCell getDataHeaderCell(Investigator investigator){
        PdfPTable table = new PdfPTable(1);

        table.addCell(headerCell("F. de generación: " + DateUtilities.getActualDate()));
        table.addCell(headerCell("Investigador: " + investigator.getName()));
        table.addCell(headerCell("Email: " + investigator.getEmail()));
        table.addCell(headerCell(""));

        PdfPCell cell = new PdfPCell(table);
        cell.setBorderWidth(1);

        return cell;
    }

    private static PdfPTable createHeader(Investigator investigator, String reportName) {
        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(TABLE_100_PERCENT);

        table.addCell(getImageHeaderCell());
        table.addCell(getNameHeaderCell(reportName));
        table.addCell(getDataHeaderCell(investigator));
        table.setSpacingAfter(10);

        return table;
    }

    private static PdfPCell headCell(String name){
        PdfPCell hcell = new PdfPCell(new Phrase(name, HEAD_FONT));
        hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
        return hcell;
    }

    private static PdfPCell bodyCell(String attribute){
        PdfPCell cell = new PdfPCell(new Phrase(attribute, BODY_FONT));
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        return cell;
    }
}