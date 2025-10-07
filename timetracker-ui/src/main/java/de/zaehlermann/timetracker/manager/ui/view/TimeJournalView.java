package de.zaehlermann.timetracker.manager.ui.view;

import static java.util.Arrays.asList;

import java.io.File;
import java.io.FileInputStream;
import java.io.Serial;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

import javax.annotation.Nonnull;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.streams.DownloadHandler;
import com.vaadin.flow.server.streams.DownloadResponse;
import com.vaadin.flow.server.streams.InputStreamDownloadCallback;

import de.zaehlermann.timetracker.base.ui.component.ViewToolbar;
import de.zaehlermann.timetracker.model.Journal;
import de.zaehlermann.timetracker.model.JournalSummaryItem;
import de.zaehlermann.timetracker.model.Workday;
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
    selectYear.setLabel("Year");
    selectYear.setItems(asList(2025, 2026, 2027, 2028, 2029, 2030)); // select from the employee file
    selectYear.setValue(LocalDate.now().getYear());

    final Select<Integer> selectMonth = new Select<>();
    selectMonth.setLabel("Month");
    selectMonth.setItems(asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12)); // select from the employee file
    selectMonth.setValue(LocalDate.now().getMonth().getValue());

    final Grid<JournalSummaryItem> journalSummaryGrid = new Grid<>(JournalSummaryItem.class, false);
    journalSummaryGrid.addColumn(JournalSummaryItem::getKey).setHeader("Description").setResizable(true).setAutoWidth(true);
    journalSummaryGrid.addColumn(JournalSummaryItem::getValue).setHeader("Value").setResizable(true).setAutoWidth(true);
    journalSummaryGrid.setAllRowsVisible(true);
    journalSummaryGrid.addThemeVariants(GridVariant.LUMO_COMPACT);
    journalSummaryGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
    journalSummaryGrid.addThemeVariants(GridVariant.LUMO_COLUMN_BORDERS);

    final Details summaryDetails = new Details("Time Journal Summary", journalSummaryGrid);
    summaryDetails.setOpened(true);

    final Grid<Workday> workdayGrid = new Grid<>(Workday.class, false);
    workdayGrid.setColumnReorderingAllowed(true);
    workdayGrid.addColumn(Workday::getDay).setHeader("Date");
    workdayGrid.addColumn(Workday::getWeekDayName).setHeader("Weekday").setComparator(Comparator.comparing(Workday::getWeekDayValue));
    workdayGrid.addColumn(Workday::getAbsenceType).setHeader("Absence");
    workdayGrid.addColumn(Workday::getLogin).setHeader("Login");
    workdayGrid.addColumn(Workday::getLogout).setHeader("Logout");
    workdayGrid.addColumn(workday -> workday.isCorrected() ? "X" : "").setHeader("Corrected");
    workdayGrid.addColumn(Workday::getHoursDayInPlaceFormatted).setHeader("Hours").setTextAlign(ColumnTextAlign.END);
    workdayGrid.addColumn(Workday::getSaldo).setHeader("Saldo").setTextAlign(ColumnTextAlign.END);
    workdayGrid.getColumns().forEach(column -> {
      column.setSortable(true);
      column.setResizable(true);
    });
    workdayGrid.setWidthFull();
    workdayGrid.setAllRowsVisible(true);
    workdayGrid.setMultiSort(true, Grid.MultiSortPriority.APPEND);
    workdayGrid.addThemeVariants(GridVariant.LUMO_COMPACT);
    workdayGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
    workdayGrid.addThemeVariants(GridVariant.LUMO_COLUMN_BORDERS);

    final Details workdayGridDetails = new Details("Time Journal Details", workdayGrid);
    workdayGridDetails.setWidthFull();
    workdayGridDetails.setOpened(true);

    final Button btnShowJournal = new Button("Show Journal");
    btnShowJournal.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    btnShowJournal.addClickListener(clickEvent -> displayJournal(selectEmployee, selectYear, selectMonth, journalSummaryGrid, workdayGrid));

    final Anchor downloadJournalTxt = new Anchor(downloadTxt(selectEmployee, selectYear, selectMonth), "Download Journal as TXT");
    final Anchor downloadJournalCsv = new Anchor(downloadCsv(selectEmployee, selectYear, selectMonth), "Download Journal as CSV");
    final Anchor downloadJournalBackup = new Anchor(downloadFullBackup(), "Download FullBackup");

    final FormLayout formLayout = new FormLayout(selectEmployee, selectYear, selectMonth, btnShowJournal);
    final Details formDetails = new Details("Select Journal", formLayout);
    formDetails.setOpened(true);

    final FormLayout downloadLayout = new FormLayout(downloadJournalTxt, downloadJournalCsv, downloadJournalBackup);
    downloadLayout.setMaxColumns(3);
    downloadLayout.setWidthFull();

    final Details downloadDetails = new Details("Download Journal", downloadLayout);

    final VerticalLayout verticalLayout = new VerticalLayout(formDetails, summaryDetails, workdayGridDetails, downloadDetails);
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
                                     @Nonnull final Grid<JournalSummaryItem> journalSummaryGrid,
                                     @Nonnull final Grid<Workday> workdayGrid) {

    final Journal journal = JOURNAL_SERVICE.createJournal(selectEmployee.getValue(), selectYear.getValue(), selectMonth.getValue());
    journalSummaryGrid.setItems(journal.getJournalSummaryItems());
    journalSummaryGrid.recalculateColumnWidths();
    workdayGrid.setItems(journal.getWorkdays());
  }
}
