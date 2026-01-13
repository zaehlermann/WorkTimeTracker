package de.zaehlermann.timetracker.pdf.builder;

import org.openpdf.text.Document;
import org.openpdf.text.DocumentException;
import org.openpdf.text.Font;
import org.openpdf.text.FontFactory;
import org.openpdf.text.Paragraph;
import org.openpdf.text.pdf.PdfWriter;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class PdfBuilder {

  static final Font FONT_TITLE = FontFactory.getFont(FontFactory.COURIER_BOLD, 20);
  public static final Font FONT_BODY = FontFactory.getFont(FontFactory.COURIER, 8);

  private final Document document;
  private final PdfWriter writer;

  public PdfBuilder(@Nonnull final String filePath) throws FileNotFoundException {
    this.document = new Document();
    this.writer = PdfWriter.getInstance(document, new FileOutputStream(filePath));
    this.writer.setPageEvent(new FooterEvent());
  }

  public PdfBuilder(@Nonnull final File file) throws FileNotFoundException {
    this.document = new Document();
    this.writer = PdfWriter.getInstance(document, new FileOutputStream(file));
    this.writer.setPageEvent(new FooterEvent());
  }

  @Nonnull
  public PdfBuilder withTitle(@Nonnull final String title) {
    document.addTitle(title);
    return this;
  }

  @Nonnull
  public PdfBuilder withAuthor(@Nonnull final String author) {
    document.addAuthor(author);
    return this;
  }

  @Nonnull
  public PdfBuilder open() {
    document.open();
    return this;
  }

  @Nonnull
  public PdfBuilder addParagraph(@Nonnull final String text) throws DocumentException {
    document.add(new Paragraph(text, FONT_BODY));
    return this;
  }

  @Nonnull
  public PdfBuilder addTable(@Nonnull final PdfTable pdfTable) throws DocumentException {
    document.add(pdfTable.build());
    return this;
  }

  public void build() {
    if (document.isOpen()) {
      document.close();
    }
    if (writer != null) {
      writer.close();
    }
  }
}
