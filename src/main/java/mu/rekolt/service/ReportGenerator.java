package mu.rekolt.service;

import mu.rekolt.model.Delivery;
import org.apache.poi.xwpf.usermodel.*; // Library for creating Word documents
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class ReportGenerator {
    public static void generateReport(List<Delivery> deliveries) {
        try {
            XWPFDocument document = new XWPFDocument(); // Create a new Word document

            // Print the main title
            XWPFParagraph title = document.createParagraph();
            XWPFRun titleRun = title.createRun();
            titleRun.setText("REKOLT Season Report");
            titleRun.setBold(true);
            titleRun.setFontSize(16);

            double seasonTotal = 0; // Variable to track total for the whole season
            List<String> printedMembers = new ArrayList<>(); // Track which members we have already printed

            boolean isFirstMember = true; // We don't want a blank page before the first member

            // Loop through every delivery to find unique members
            for (Delivery d : deliveries) {

                // If we haven't printed this member yet
                if (!printedMembers.contains(d.getMemberId())) {
                    printedMembers.add(d.getMemberId()); // Add them to the "already printed" list

                    // Only add a page break if it is NOT the first member
                    if (!isFirstMember) {
                        document.createParagraph().setPageBreak(true);
                    }
                    isFirstMember = false; // Set to false after the first member

                    // 1. Print the Member Header (Bold)
                    XWPFParagraph header = document.createParagraph();
                    XWPFRun run = header.createRun();
                    run.setText("Member: " + d.getMemberId() + " - " + d.getMemberName());
                    run.setBold(true);
                    run.setFontSize(14);

                    // 2. Create the Table for this member (1 row for headers, 3 columns)
                    XWPFTable table = document.createTable(1, 3);

                    // Set the HEADER ROW for the table
                    table.getRow(0).getCell(0).setText("Produce");
                    table.getRow(0).getCell(1).setText("Mass (kg)");
                    table.getRow(0).getCell(2).setText("Net Payable (MUR)");

                    double memberTotal = 0; // Variable to track this member's total

                    // Loop AGAIN to find this specific member's deliveries
                    for (Delivery memberDelivery : deliveries) {
                        if (memberDelivery.getMemberId().equals(d.getMemberId())) {

                            // 3. Create a new row for each delivery
                            XWPFTableRow row = table.createRow();
                            row.getCell(0).setText(memberDelivery.getProduce().getCode());
                            row.getCell(1).setText(String.valueOf(memberDelivery.getMass()));
                            row.getCell(2).setText(String.valueOf(memberDelivery.netPayable()));

                            // Add to this member's total
                            memberTotal += memberDelivery.netPayable();
                            seasonTotal += memberDelivery.netPayable();
                        }
                    }

                    // 4. Add a final row for the member's total
                    XWPFTableRow totalRow = table.createRow();
                    totalRow.getCell(0).setText("Member Total");
                    totalRow.getCell(1).setText("");
                    totalRow.getCell(2).setText(String.valueOf(memberTotal));

                    // Make the Total row bold
                    totalRow.getCell(0).getParagraphs().get(0).getRuns().get(0).setBold(true);
                    totalRow.getCell(2).getParagraphs().get(0).getRuns().get(0).setBold(true);
                }
            }

            // 5. Print the overall Season Total at the end
            XWPFParagraph seasonPara = document.createParagraph();
            XWPFRun sRun = seasonPara.createRun();
            sRun.setText("SEASON TOTAL PAYABLE: " + seasonTotal + " MUR");
            sRun.setBold(true);
            sRun.setFontSize(14);

            // 6. Save the file to the "output" folder
            FileOutputStream out = new FileOutputStream("output/season-report.docx");
            document.write(out);
            out.flush(); // Force the file to save completely
            out.close();
            document.close();
            System.out.println("Season report generated!");

        } catch (Exception e) {
            System.out.println("Error writing report: " + e.getMessage());
        }
    }
}