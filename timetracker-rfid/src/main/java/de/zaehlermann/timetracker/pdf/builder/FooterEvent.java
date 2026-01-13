package de.zaehlermann.timetracker.pdf.builder;

import org.openpdf.text.Document;
import org.openpdf.text.Element;
import org.openpdf.text.Font;
import org.openpdf.text.FontFactory;
import org.openpdf.text.Phrase;
import org.openpdf.text.pdf.ColumnText;
import org.openpdf.text.pdf.PdfContentByte;
import org.openpdf.text.pdf.PdfPageEventHelper;
import org.openpdf.text.pdf.PdfWriter;

import javax.annotation.Nonnull;
import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FooterEvent extends PdfPageEventHelper {

  private static final Font FONT = FontFactory.getFont(FontFactory.COURIER, 8, Color.DARK_GRAY);
  private final String dateString;

  public FooterEvent() {
    this.dateString = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());
  }

  @Override
  public void onEndPage(@Nonnull final PdfWriter writer, @Nonnull final Document document) {
    final PdfContentByte cb = writer.getDirectContent();
    final float left = document.left();
    final float right = document.right();
    final float bottom = document.bottom() - 20;

    // Date (left)
    ColumnText.showTextAligned(
      cb,
      Element.ALIGN_LEFT,
      new Phrase(dateString, FONT),
      left,
      bottom,
      0
    );

    // Page number (right)
    final String pageText = String.valueOf(writer.getPageNumber());
    ColumnText.showTextAligned(
      cb,
      Element.ALIGN_RIGHT,
      new Phrase(pageText, FONT),
      right,
      bottom,
      0
    );
  }
}
