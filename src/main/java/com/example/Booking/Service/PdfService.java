package com.example.Booking.Service;

import com.example.Booking.Model.Booking;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;

@Service
public class PdfService {

    public byte[] generateTicketPdf(Booking booking) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);

            // Set font
            PdfFont font = PdfFontFactory.createFont();

            // Title
            Paragraph title = new Paragraph("GENDA TICKETS - BOOKING CONFIRMATION")
                    .setFont(font)
                    .setFontSize(18)
                    .setBold();
            document.add(title);

            document.add(new Paragraph("\n"));

            // Booking details table
            Table table = new Table(2);
            table.setWidth(500);

            // Add booking information
            addTableRow(table, "Booking ID:", booking.getBookingId());
            addTableRow(table, "Passenger Name:", booking.getPassengerName());
            addTableRow(table, "Email:", booking.getPassengerEmail());
            addTableRow(table, "Phone:", booking.getPassengerPhone());
            addTableRow(table, "Number of Passengers:", booking.getNumberOfPassengers().toString());
            
            document.add(new Paragraph("BOOKING DETAILS").setBold().setFontSize(14));
            document.add(table);

            document.add(new Paragraph("\n"));

            // Journey details table
            Table journeyTable = new Table(2);
            journeyTable.setWidth(500);

            addTableRow(journeyTable, "From:", booking.getDepartureProvince() + " - " + booking.getDepartureDistrict());
            addTableRow(journeyTable, "To:", booking.getDestinationProvince() + " - " + booking.getDestinationDistrict());
            addTableRow(journeyTable, "Travel Date:", booking.getTravelDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
            addTableRow(journeyTable, "Booking Date:", booking.getBookingDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
            addTableRow(journeyTable, "Status:", booking.getStatus().toString());
            addTableRow(journeyTable, "Total Price:", "$" + booking.getPrice().toString());

            if (booking.getSeatNumbers() != null && !booking.getSeatNumbers().trim().isEmpty()) {
                addTableRow(journeyTable, "Seat Numbers:", booking.getSeatNumbers());
            }

            if (booking.getVehicleInfo() != null && !booking.getVehicleInfo().trim().isEmpty()) {
                addTableRow(journeyTable, "Vehicle Info:", booking.getVehicleInfo());
            }

            document.add(new Paragraph("JOURNEY DETAILS").setBold().setFontSize(14));
            document.add(journeyTable);

            document.add(new Paragraph("\n"));

            // Footer
            Paragraph footer = new Paragraph("Thank you for choosing Genda Tickets!\n" +
                    "Please present this ticket at the boarding point.\n" +
                    "For any queries, please contact our customer service.")
                    .setFontSize(10);
            document.add(footer);

            document.close();
            return baos.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Error generating PDF: " + e.getMessage(), e);
        }
    }

    private void addTableRow(Table table, String label, String value) {
        table.addCell(new Cell().add(new Paragraph(label).setBold()));
        table.addCell(new Cell().add(new Paragraph(value != null ? value : "N/A")));
    }
}