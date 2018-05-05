package co.edu.itm.clinicaldata.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import co.edu.itm.clinicaldata.model.ProcessingRequest;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class GeneratePdfReport {

    private static final Logger LOGGER = Logger.getLogger(GeneratePdfReport.class.getName());

    private static final int FONT_SIZE_BODY = 8;
    private static final int FONT_SIZE_HEAD = 10;
    private static final Font HEAD_FONT = FontFactory.getFont(FontFactory.HELVETICA_BOLD, FONT_SIZE_HEAD);
    private static final Font BODY_FONT = FontFactory.getFont(FontFactory.HELVETICA, FONT_SIZE_BODY);

    public static ByteArrayInputStream processResult(ProcessingRequest processingRequest) {
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100);
            table.setWidths(new int[]{1, 4, 2, 2, 1});

            table.addCell(headCell("Id"));
            table.addCell(headCell("Identifier"));
            table.addCell(headCell("Creation Date"));
            table.addCell(headCell("Filename"));
            table.addCell(headCell("State"));

            List<ProcessingRequest> listProcessingRequest = new ArrayList<>();
            listProcessingRequest.add(processingRequest);

            for (ProcessingRequest request : listProcessingRequest) {
                table.addCell(bodyCell(request.getId().toString()));
                table.addCell(bodyCell(request.getIdentifier()));
                table.addCell(bodyCell(DateUtilities.timestampToString(request.getCreationDate())));
                table.addCell(bodyCell(request.getFileName()));
                table.addCell(bodyCell(request.getState()));
            }

            PdfWriter.getInstance(document, out);
            document.open();
            document.add(table);

            document.close();
        } catch (DocumentException ex) {
            LOGGER.error(String
                    .format("Ocurrió un error en la generación del PDF, para la solicitud <%s>",
                            processingRequest.getIdentifier()), ex);
        }
        return new ByteArrayInputStream(out.toByteArray());
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