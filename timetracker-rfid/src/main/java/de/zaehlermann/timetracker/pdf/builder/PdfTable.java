package de.zaehlermann.timetracker.pdf.builder;

import org.openpdf.text.Element;
import org.openpdf.text.Phrase;
import org.openpdf.text.pdf.PdfPCell;
import org.openpdf.text.pdf.PdfPTable;

import javax.annotation.Nonnull;
import java.util.List;

import static de.zaehlermann.timetracker.pdf.builder.PdfBuilder.FONT_BODY;
import static java.util.Arrays.asList;

public class PdfTable {

  private final PdfPTable table;

  public PdfTable(final int columns) {
    this.table = new PdfPTable(columns);
    this.table.setWidthPercentage(100);
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
      final PdfPCell cell = new PdfPCell(new Phrase(header, FONT_BODY));
      cell.setHorizontalAlignment(Element.ALIGN_CENTER);
      cell.setPadding(5);
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
      final PdfPCell cell = new PdfPCell(new Phrase(cellText, FONT_BODY));
      cell.setPadding(5);
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
