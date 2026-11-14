package de.zaehlermann.timetracker.pdf.builder;

import org.eclipse.jdt.annotation.NonNull;
import org.openpdf.text.Element;
import org.openpdf.text.Font;
import org.openpdf.text.FontFactory;
import org.openpdf.text.Phrase;
import org.openpdf.text.pdf.PdfPCell;
import org.openpdf.text.pdf.PdfPTable;

import java.awt.Color;
import java.util.List;

import static java.util.Arrays.asList;

public class PdfTable {

  private static final Font FONT_TABLE_HEADER = FontFactory.getFont(FontFactory.COURIER_BOLD, 8);
  private static final Font FONT_TABLE_BODY = FontFactory.getFont(FontFactory.COURIER, 8);
  private final PdfPTable table;

  public PdfTable(final int columns) {
    this.table = new PdfPTable(columns);
    this.table.setWidthPercentage(100);
    this.table.setSpacingBefore(5);
    this.table.getDefaultCell().setBorderWidth(0.1f);
  }

  @NonNull
  public PdfTable addHeader(@NonNull final String... headers) {
    return addHeader(asList(headers));
  }

  @NonNull
  public PdfTable addHeader(@NonNull final List<String> headers) {
    for (final String header : headers) {
      final PdfPCell cell = new PdfPCell(new Phrase(header, FONT_TABLE_HEADER));
      cell.setHorizontalAlignment(Element.ALIGN_CENTER);
      cell.setPadding(5);
      cell.setBackgroundColor(Color.LIGHT_GRAY);
      table.addCell(cell);
    }
    table.setHeaderRows(1);
    return this;
  }

  @NonNull
  public PdfTable addRow(@NonNull final String... cells) {
    return addRow(asList(cells));
  }

  @NonNull
  public PdfTable addRow(@NonNull final List<String> cells) {
    for (final String cellText : cells) {
      final PdfPCell cell = new PdfPCell(new Phrase(cellText, FONT_TABLE_BODY));
      //cell.setPadding(5);
      cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
      table.addCell(cell);
    }
    return this;
  }

  @NonNull
  public PdfTable addRows(@NonNull final List<List<String>> rows) {
    rows.forEach(this::addRow);
    return this;
  }

  public PdfPTable build() {
    return table;
  }

}
