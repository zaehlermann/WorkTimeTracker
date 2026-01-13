package de.zaehlermann.timetracker.pdf.builder;

import org.openpdf.text.Element;
import org.openpdf.text.Font;
import org.openpdf.text.FontFactory;
import org.openpdf.text.Phrase;
import org.openpdf.text.pdf.PdfPCell;
import org.openpdf.text.pdf.PdfPTable;

import javax.annotation.Nonnull;
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

  @Nonnull
  public PdfTable setColumnWidths(@Nonnull final float... widths) {
    try {
      table.setWidths(widths);
    } catch (final Exception ignored) {
    }
    return this;
  }

  @Nonnull
  public PdfTable addHeader(@Nonnull final String... headers) {
    return addHeader(asList(headers));
  }

  @Nonnull
  public PdfTable addHeader(@Nonnull final List<String> headers) {
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

  @Nonnull
  public PdfTable addRow(@Nonnull final String... cells) {
    return addRow(asList(cells));
  }

  @Nonnull
  public PdfTable addRow(@Nonnull final List<String> cells) {
    for (final String cellText : cells) {
      final PdfPCell cell = new PdfPCell(new Phrase(cellText, FONT_TABLE_BODY));
      //cell.setPadding(5);
      cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
      table.addCell(cell);
    }
    return this;
  }

  @Nonnull
  public PdfTable addRows(@Nonnull final List<List<String>> rows) {
    rows.forEach(this::addRow);
    return this;
  }

  public PdfPTable build() {
    return table;
  }

}
