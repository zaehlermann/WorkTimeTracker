package de.zaehlermann.timetracker.manager.ui.view;

import static java.util.Arrays.asList;

import java.io.File;
import java.io.FileInputStream;
import java.io.Serial;
import java.time.LocalDate;
import java.util.List;

import javax.annotation.Nonnull;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.streams.DownloadHandler;
import com.vaadin.flow.server.streams.DownloadResponse;
import com.vaadin.flow.server.streams.InputStreamDownloadCallback;

import de.zaehlermann.timetracker.base.ui.component.ViewToolbar;
import de.zaehlermann.timetracker.service.JournalService;
import jakarta.annotation.security.PermitAll;

@Route("time-journal")
@PageTitle("Time journal")
@Menu(order = 3, icon = "vaadin:clipboard-check", title = "Time journal")
@PermitAll // When security is enabled, allow all authenticated users
public class TimeJournalView extends Main {

  @Serial
  private static final long serialVersionUID = -7891828904175930796L;
  private static final JournalService JOURNAL_SERVICE = new JournalService();

  public TimeJournalView() {

    final List<String> allEmployeeNames = JOURNAL_SERVICE.getAllEmployeeNames();
    final Select<String> selectEmployee = new Select<>();
    selectEmployee.setLabel("Employee");
    selectEmployee.setItems(allEmployeeNames); // select from the employee file
    selectEmployee.setValue(allEmployeeNames.getFirst());

    final Select<Integer> selectYear = new Select<>();
    selectYear.setLabel("Year (Not implemented yet.)");
    selectYear.setItems(asList(2025, 2026, 2027, 2028, 2029, 2030)); // select from the employee file
    selectYear.setValue(LocalDate.now().getYear());

    final Select<Integer> selectMonth = new Select<>();
    selectMonth.setLabel("Month (Not implemented yet.)");
    selectMonth.setItems(asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12)); // select from the employee file
    selectMonth.setValue(LocalDate.now().getMonth().getValue());

    final TextArea textArea = new TextArea();
    textArea.setWidthFull();
    textArea.setLabel("Time Journal:");
    textArea.addClassName("journal-font");
    add(textArea);

    final Button btnShowJournal = new Button("Show Journal");
    btnShowJournal.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    btnShowJournal.addClickListener(clickEvent -> displayJournal(selectEmployee, selectYear, selectMonth, textArea));

    final Anchor downloadJournalTxt = new Anchor(downloadTxt(selectEmployee, selectYear, selectMonth), "Download Journal as TXT");
    final Anchor downloadJournalCsv = new Anchor(downloadCsv(selectEmployee, selectYear, selectMonth), "Download Journal as CSV");
    final Anchor downloadJournalBackup = new Anchor(downloadFullBackup(), "Download FullBackup");

    final FormLayout formLayout = new FormLayout(selectEmployee, selectYear, selectMonth, btnShowJournal);
    final FormLayout downloadLayout = new FormLayout(downloadJournalTxt, downloadJournalCsv, downloadJournalBackup);
    downloadLayout.setMaxColumns(3);
    downloadLayout.setWidthFull();

    final VerticalLayout verticalLayout = new VerticalLayout(formLayout, textArea, downloadLayout);
    verticalLayout.setSizeFull();

    setSizeFull();
    add(new ViewToolbar("Time journal"));
    add(verticalLayout);
  }

  @Nonnull
  private static DownloadHandler downloadTxt(@Nonnull final Select<String> selectEmployee,
                                             @Nonnull final Select<Integer> selectYear,
                                             @Nonnull final Select<Integer> selectMonth) {

    return DownloadHandler.fromInputStream((InputStreamDownloadCallback) downloadEvent -> {
      final File file = JOURNAL_SERVICE.downloadTxt(selectEmployee.getValue(), selectYear.getValue(), selectMonth.getValue());
      final FileInputStream fileInputStream = new FileInputStream(file);
      return new DownloadResponse(fileInputStream, file.getName(), "text/plain", file.length());
    });
  }

  @Nonnull
  private static DownloadHandler downloadCsv(@Nonnull final Select<String> selectEmployee,
                                             @Nonnull final Select<Integer> selectYear,
                                             @Nonnull final Select<Integer> selectMonth) {
    return DownloadHandler.fromInputStream((InputStreamDownloadCallback) downloadEvent -> {
      final File file = JOURNAL_SERVICE.downloadCsv(selectEmployee.getValue(), selectYear.getValue(), selectMonth.getValue());
      final FileInputStream fileInputStream = new FileInputStream(file);
      return new DownloadResponse(fileInputStream, file.getName(), "text/csv", file.length());
    });
  }

  @Nonnull
  private DownloadHandler downloadFullBackup() {
    return DownloadHandler.fromInputStream((InputStreamDownloadCallback) downloadEvent -> {
      final File file = JOURNAL_SERVICE.downloadBackup();
      final FileInputStream fileInputStream = new FileInputStream(file);
      return new DownloadResponse(fileInputStream, file.getName(), "application/zip", file.length());
    });
  }

  private static void displayJournal(@Nonnull final Select<String> selectEmployee,
                                     @Nonnull final Select<Integer> selectYear,
                                     @Nonnull final Select<Integer> selectMonth,
                                     @Nonnull final TextArea textArea) {
    final String journal = JOURNAL_SERVICE.createAndSaveJournalTxt(selectEmployee.getValue(), selectYear.getValue(), selectMonth.getValue());
    textArea.setValue(journal);
  }
}
