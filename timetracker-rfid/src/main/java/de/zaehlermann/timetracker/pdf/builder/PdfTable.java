package de.zaehlermann.timetracker.pdf.builder;

import org.openpdf.text.Element;
import org.openpdf.text.Phrase;
import org.openpdf.text.pdf.PdfPCell;
import org.openpdf.text.pdf.PdfPTable;

import javax.annotation.Nonnull;
import java.util.List;

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
    for (final String header : headers) {
      final PdfPCell cell = new PdfPCell(new Phrase(header));
      cell.setHorizontalAlignment(Element.ALIGN_CENTER);
      cell.setPadding(5);
      table.addCell(cell);
    }
    table.setHeaderRows(1);
    return this;
  }

  @Nonnull
  public PdfTable addRow(@Nonnull final String... cells) {
    for (final String cellText : cells) {
      final PdfPCell cell = new PdfPCell(new Phrase(cellText));
      cell.setPadding(5);
      table.addCell(cell);
    }
    return this;
  }

  @Nonnull
  public PdfTable addRows(@Nonnull final List<List<String>> rows) {
    for (final List<String> row : rows) {
      addRow(row.toArray(new String[0]));
    }
    return this;
  }

  public PdfPTable build() {
    return table;
  }
}
