package mu.rekolt.service;

import mu.rekolt.model.Delivery;
import org.apache.poi.xwpf.usermodel.*;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class ReportGenerator {
    public static void generateReport(List<Delivery> deliveries) {
        try {
            XWPFDocument document = new XWPFDocument();

            // Print the title
            XWPFParagraph title = document.createParagraph();
            XWPFRun titleRun = title.createRun();
            titleRun.setText("REKOLT Season Report");
            titleRun.setBold(true);

            double seasonTotal = 0;
            List<String> printedMembers = new ArrayList<>();

            // Add a boolean to track if this is the very first member
            boolean isFirstMember = true;

            for (Delivery d : deliveries) {
                if (!printedMembers.contains(d.getMemberId())) {
                    printedMembers.add(d.getMemberId());

                    // Only add a page break if it is NOT the first member
                    if (!isFirstMember) {
                        document.createParagraph().setPageBreak(true);
                    }
                    isFirstMember = false;

                    XWPFParagraph header = document.createParagraph();
                    XWPFRun run = header.createRun();
                    run.setText("Member: " + d.getMemberId() + " - " + d.getMemberName());
                    run.setBold(true);

                    double memberTotal = 0;
                    for (Delivery memberDelivery : deliveries) {
                        if (memberDelivery.getMemberId().equals(d.getMemberId())) {
                            XWPFParagraph line = document.createParagraph();
                            line.createRun().setText(memberDelivery.reportSummary());
                            memberTotal += memberDelivery.netPayable();
                            seasonTotal += memberDelivery.netPayable();
                        }
                    }
                    XWPFParagraph totalLine = document.createParagraph();
                    XWPFRun totalRun = totalLine.createRun();
                    totalRun.setText("MEMBER TOTAL: " + memberTotal + " MUR");
                    totalRun.setBold(true);
                }
            }

            XWPFParagraph seasonPara = document.createParagraph();
            XWPFRun sRun = seasonPara.createRun();
            sRun.setText("SEASON TOTAL PAYABLE: " + seasonTotal + " MUR");
            sRun.setBold(true);

            FileOutputStream out = new FileOutputStream("output/season-report.docx");
            document.write(out);
            out.flush();
            out.close();
            document.close();
            System.out.println("Season report generated!");

        } catch (Exception e) {
            System.out.println("Error writing report: " + e.getMessage());
        }
    }
}