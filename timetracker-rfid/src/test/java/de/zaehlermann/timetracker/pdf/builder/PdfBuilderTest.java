package de.zaehlermann.timetracker.pdf.builder;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openpdf.text.DocumentException;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

import static de.zaehlermann.timetracker.pdf.builder.PdfBuilder.BODY_FONT;
import static de.zaehlermann.timetracker.pdf.builder.PdfBuilder.TITLE_FONT;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PdfBuilderTest {

  @BeforeEach
  void setUp() {
    final File file = new File("target/PdfBuilderTest/");
    FileUtils.deleteQuietly(file);
    assertTrue(file.mkdirs());
  }

  @Test
  void build() throws IOException, DocumentException {

    final PdfTable table = new PdfTable(3)
      .addHeader("ID", "Name", "Email")
      .addRow("1", "Alice", "alice@example.com")
      .addRow("2", "Bob", "bob@example.com")
      .addRow("3", "Charlie", "charlie@example.com");

    new PdfBuilder("target/pdfbuildertest/example.pdf")
      .withTitle("My PDF Document")
      .withAuthor("John Doe")
      .open()
      .addParagraph("Test equal space 0815!!", TITLE_FONT)
      .addParagraph("Test equal space 0815!!", BODY_FONT)
      .addParagraph(LocalDateTime.now().toString(), BODY_FONT)
      .addTable(table)
      .build();

    System.out.println("PDF created successfully.");
    assertTrue(new File("target/pdfbuildertest/example.pdf").exists());

  }

}