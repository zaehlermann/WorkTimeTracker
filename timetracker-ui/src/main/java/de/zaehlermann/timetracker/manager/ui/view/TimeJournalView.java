package de.zaehlermann.timetracker.manager.ui.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.streams.DownloadHandler;
import com.vaadin.flow.server.streams.DownloadResponse;
import com.vaadin.flow.server.streams.InputStreamDownloadCallback;
import de.zaehlermann.timetracker.base.ui.component.ViewToolbar;
import de.zaehlermann.timetracker.i18n.MessageKeys;
import de.zaehlermann.timetracker.i18n.Messages;
import de.zaehlermann.timetracker.i18n.SupportedLocales;
import de.zaehlermann.timetracker.model.Journal;
import de.zaehlermann.timetracker.model.JournalSummaryItem;
import de.zaehlermann.timetracker.model.Workday;
import de.zaehlermann.timetracker.service.JournalService;
import de.zaehlermann.timetracker.validate.ValidateUtils;
import jakarta.annotation.security.PermitAll;

import jakarta.annotation.Nonnull;
import java.io.File;
import java.io.FileInputStream;
import java.io.Serial;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.Comparator;
import java.util.List;

import static java.util.Arrays.asList;

@Route("time-journal")
@PageTitle("Time journal")
@Menu(order = 4, icon = "vaadin:chart", title = "Time journal")
@PermitAll // When security is enabled, allow all authenticated users
public class TimeJournalView extends Main {

  @Serial
  private static final long serialVersionUID = -7891828904175930796L;
  private static final JournalService JOURNAL_SERVICE = new JournalService();
  private static final String COLUMN_DATE = "Date";
  public static final String COLUMN_HOURS = "Hours";
  public static final String COLUMN_SALDO = "Saldo";
  public static final String COLUMN_ABSENCE = "Absence";
  public static final String COLUMN_CORRECTED = "Corrected";
  public static final String COLUMN_WEEKDAY = "Weekday";
  public static final String COLUMN_LOGIN = "Login";
  public static final String COLUMN_LOGOUT = "Logout";
  public static final String TOTAL = "Total: ";

  final Select<String> selectEmployee = new Select<>();

  public TimeJournalView() {

    final List<String> allEmployeeNames = JOURNAL_SERVICE.getAllEmployeeNames();
    selectEmployee.setLabel(MessageKeys.EMPLOYEE_ID.getTranslation());
    selectEmployee.setItems(allEmployeeNames); // select from the employee file
    selectEmployee.setValue(allEmployeeNames.isEmpty() ? null : allEmployeeNames.getFirst());

    final Select<Integer> selectYear = new Select<>();
    selectYear.setLabel(Messages.get(MessageKeys.TIME_JOURNAL_YEAR));
    selectYear.setItems(asList(2025, 2026, 2027, 2028, 2029, 2030)); // select from the employee file
    selectYear.setValue(LocalDate.now().getYear());

    final Select<Integer> selectMonth = new Select<>();
    selectMonth.setLabel(MessageKeys.TIME_JOURNAL_MONTH.getTranslation());
    selectMonth.setEmptySelectionAllowed(true);
    selectMonth.setEmptySelectionCaption("All");
    selectMonth.setItemLabelGenerator(item -> item == null ? "All" : Month.of(item).getDisplayName(TextStyle.FULL, SupportedLocales.getDefault()));
    selectMonth.setItems(asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12)); // select from the employee file
    selectMonth.setValue(LocalDate.now().getMonth().getValue());

    final Grid<JournalSummaryItem> employeeSummaryGrid = new Grid<>(JournalSummaryItem.class, false);
    employeeSummaryGrid.addColumn(JournalSummaryItem::getKey).setHeader("Description").setResizable(true).setAutoWidth(true);
    employeeSummaryGrid.addColumn(JournalSummaryItem::getValue).setHeader("Value").setResizable(true).setAutoWidth(true);
    employeeSummaryGrid.setAllRowsVisible(true);
    employeeSummaryGrid.addThemeVariants(GridVariant.LUMO_COMPACT);
    employeeSummaryGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
    employeeSummaryGrid.addThemeVariants(GridVariant.LUMO_COLUMN_BORDERS);

    final Grid<JournalSummaryItem> journalSummaryGrid = new Grid<>(JournalSummaryItem.class, false);
    journalSummaryGrid.addColumn(JournalSummaryItem::getKey).setHeader("Description").setResizable(true).setAutoWidth(true);
    journalSummaryGrid.addColumn(JournalSummaryItem::getValue).setHeader("Value").setResizable(true).setAutoWidth(true).setTextAlign(ColumnTextAlign.END);
    journalSummaryGrid.setAllRowsVisible(true);
    journalSummaryGrid.addThemeVariants(GridVariant.LUMO_COMPACT);
    journalSummaryGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
    journalSummaryGrid.addThemeVariants(GridVariant.LUMO_COLUMN_BORDERS);

    final Details employeeDetails = new Details("Employee Summary", employeeSummaryGrid);
    employeeDetails.setOpened(true);

    final Details summaryDetails = new Details("Time Journal Summary", journalSummaryGrid);
    summaryDetails.setOpened(true);

    final Grid<Workday> workdayGrid = new Grid<>(Workday.class, false);
    workdayGrid.addColumn(Workday::getDay).setKey(COLUMN_DATE).setHeader(COLUMN_DATE).setFooter("Total Days:");
    workdayGrid.addColumn(Workday::getWeekDayName).setKey(COLUMN_WEEKDAY).setHeader(COLUMN_WEEKDAY)
      .setComparator(Comparator.comparing(Workday::getWeekDayValue));
    workdayGrid.addColumn(Workday::getAbsenceTypePrintValueLong).setKey(COLUMN_ABSENCE).setHeader(COLUMN_ABSENCE);
    workdayGrid.addColumn(Workday::getLogin).setKey(COLUMN_LOGIN).setHeader(COLUMN_LOGIN);
    workdayGrid.addColumn(Workday::getLogout).setKey(COLUMN_LOGOUT).setHeader(COLUMN_LOGOUT);
    workdayGrid.addColumn(workday -> workday.isCorrected() ? "X" : "").setKey(COLUMN_CORRECTED).setHeader(COLUMN_CORRECTED);
    workdayGrid.addColumn(Workday::getHoursDayInPlaceFormatted).setKey(COLUMN_HOURS).setHeader(COLUMN_HOURS).setTextAlign(ColumnTextAlign.END);
    workdayGrid.addColumn(Workday::getSaldoFormatted).setKey(COLUMN_SALDO).setHeader(COLUMN_SALDO).setTextAlign(ColumnTextAlign.END);

    workdayGrid.setColumnReorderingAllowed(true);
    workdayGrid.setMultiSort(true, Grid.MultiSortPriority.APPEND);
    workdayGrid.getColumns().forEach(column -> {
      column.setSortable(true);
      column.setResizable(true);
    });
    workdayGrid.setWidthFull();
    workdayGrid.addThemeVariants(GridVariant.LUMO_COMPACT);
    workdayGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
    workdayGrid.addThemeVariants(GridVariant.LUMO_COLUMN_BORDERS);

    final Details workdayGridDetails = new Details("Time Journal Details", workdayGrid);
    workdayGridDetails.setOpened(true);
    workdayGridDetails.setWidthFull();

    final Button showJournalButton = new Button("Show Journal");
    showJournalButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    showJournalButton.setIcon(VaadinIcon.CHART.create());
    showJournalButton.addClickListener(
      clickEvent -> displayJournal(selectEmployee, selectYear, selectMonth, employeeSummaryGrid, journalSummaryGrid, workdayGrid));

    final Anchor downloadJournalPdf = new Anchor(downloadPdf(selectEmployee, selectYear, selectMonth), "Download Journal as PDF");
    final Anchor downloadJournalTxt = new Anchor(downloadTxt(selectEmployee, selectYear, selectMonth), "Download Journal as TXT");
    final Anchor downloadJournalCsv = new Anchor(downloadCsv(selectEmployee, selectYear, selectMonth), "Download Journal as CSV");
    final Anchor downloadJournalBackup = new Anchor(downloadFullBackup(), "Download FullBackup");

    final FormLayout formLayout = new FormLayout(selectEmployee, selectYear, selectMonth, showJournalButton);
    final Details formDetails = new Details("Select Journal", formLayout);
    formDetails.setOpened(true);

    final FormLayout downloadLayout = new FormLayout(downloadJournalPdf, downloadJournalTxt, downloadJournalCsv, downloadJournalBackup);
    downloadLayout.setAutoResponsive(true);
    downloadLayout.setAutoRows(true);
    downloadLayout.setWidthFull();

    final Details downloadDetails = new Details("Download Journal", downloadLayout);
    downloadDetails.setOpened(true);
    downloadDetails.setWidthFull();

    final VerticalLayout verticalLayout = new VerticalLayout(formDetails, employeeDetails, summaryDetails, workdayGridDetails, downloadDetails);
    verticalLayout.setSizeFull();

    setSizeFull();
    add(new ViewToolbar("Time journal"));
    add(verticalLayout);
  }

  @Nonnull
  private static DownloadHandler downloadPdf(@Nonnull final Select<String> selectEmployee,
                                             @Nonnull final Select<Integer> selectYear,
                                             @Nonnull final Select<Integer> selectMonth) {

    return DownloadHandler.fromInputStream((InputStreamDownloadCallback) downloadEvent -> {
      final File file = JOURNAL_SERVICE.downloadPdf(selectEmployee.getValue(), selectYear.getValue(), selectMonth.getValue());
      final FileInputStream fileInputStream = new FileInputStream(file);
      return new DownloadResponse(fileInputStream, file.getName(), "application/pdf", file.length());
    });
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
                                     @Nonnull final Grid<JournalSummaryItem> employeeSummaryGrid,
                                     @Nonnull final Grid<JournalSummaryItem> journalSummaryGrid,
                                     @Nonnull final Grid<Workday> workdayGrid) {

    final boolean isValid = ValidateUtils.validateSelects(List.of(selectEmployee, selectYear));
    if (!isValid) {
      Notification.show("Please fill all required fields").addThemeVariants(NotificationVariant.LUMO_ERROR);
      return;
    }

    final Journal journal = JOURNAL_SERVICE.createJournal(selectEmployee.getValue(), selectYear.getValue(), selectMonth.getValue());
    employeeSummaryGrid.setItems(journal.getEmployeeWorkModelSummaryItems());
    employeeSummaryGrid.recalculateColumnWidths();
    journalSummaryGrid.setItems(journal.getJournalSummaryItems());
    journalSummaryGrid.recalculateColumnWidths();

    workdayGrid.setItems(journal.getSelectedWorkdays());
    workdayGrid.getColumnByKey(COLUMN_DATE).setFooter(TOTAL + journal.getSelectedTotalDays());
    workdayGrid.getColumnByKey(COLUMN_ABSENCE).setFooter(TOTAL + journal.getSelectedTotalAbsenceDays());
    workdayGrid.getColumnByKey(COLUMN_CORRECTED).setFooter(TOTAL + journal.getSelectedTotalCorrectedDays());
    workdayGrid.getColumnByKey(COLUMN_LOGIN).setFooter(TOTAL + journal.getSelectedTotalLogins());
    workdayGrid.getColumnByKey(COLUMN_LOGOUT).setFooter(TOTAL + journal.getSelectedTotalLogouts());
    workdayGrid.getColumnByKey(COLUMN_HOURS).setFooter(journal.getSelectedTotalHours());
    workdayGrid.getColumnByKey(COLUMN_SALDO).setFooter(journal.getSelectedTotalSaldo());
  }
}
