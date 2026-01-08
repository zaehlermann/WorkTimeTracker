package de.zaehlermann.timetracker.manager.ui.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.timepicker.TimePicker;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import de.zaehlermann.timetracker.base.ui.component.ViewToolbar;
import de.zaehlermann.timetracker.i18n.MessageKeys;
import de.zaehlermann.timetracker.i18n.Messages;
import de.zaehlermann.timetracker.manager.ui.components.DeleteButton;
import de.zaehlermann.timetracker.manager.ui.components.SaveButton;
import de.zaehlermann.timetracker.model.WorkModel;
import de.zaehlermann.timetracker.service.JournalService;
import de.zaehlermann.timetracker.service.WorkModelService;
import jakarta.annotation.security.PermitAll;

import java.io.Serial;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoField;
import java.util.List;
import java.util.Set;

@Route("work-models")
@PageTitle("Work models")
@Menu(order = 1, icon = "vaadin:workplace", title = "Work models")
@PermitAll // When security is enabled, allow all authenticated users
public class WorkModelView extends Main {

  @Serial
  private static final long serialVersionUID = -8665685480589825450L;
  private static final WorkModelService WORK_MODEL_SERVICE = new WorkModelService();
  private static final JournalService JOURNAL_SERVICE = new JournalService();

  private final Select<String> employeeSelect = new Select<>();
  private final DatePicker validFromPicker = new DatePicker(Messages.get(MessageKeys.WORKMODEL_VALID_FROM));
  private final DatePicker validUntilPicker = new DatePicker(Messages.get(MessageKeys.WORKMODEL_VALID_UNTIL));
  private final TimePicker workTimePicker = new TimePicker("Daily working time (HH:mm)");
  private final TimePicker breakTimePicker = new TimePicker("Daily break time (HH:mm)");
  private final IntegerField initialHourField = new IntegerField(Messages.get(MessageKeys.WORKMODEL_INITIAL_HOURS));
  private final Grid<WorkModel> workModelGrid;

  public WorkModelView() {
    final List<String> allEmployeeNames = JOURNAL_SERVICE.getAllEmployeeNames();
    employeeSelect.setLabel(MessageKeys.EMPLOYEE_ID.getTranslation());
    employeeSelect.setItems(allEmployeeNames);
    if (!allEmployeeNames.isEmpty()) {
      employeeSelect.setValue(allEmployeeNames.getFirst());
    } else {
      Notification.show("No employees found, please add employees first.").addThemeVariants(NotificationVariant.LUMO_ERROR);
    }

    validFromPicker.setRequired(true);
    validUntilPicker.setRequired(false);
    workTimePicker.setRequired(true);
    breakTimePicker.setRequired(false);
    initialHourField.setRequired(true);

    validFromPicker.setValue(LocalDate.now());
    workTimePicker.setValue(LocalTime.of(8, 0));
    breakTimePicker.setValue(LocalTime.of(0, 30));
    initialHourField.setValue(0);

    final Button saveButton = new SaveButton(event -> saveEmployee());
    final Button deleteButton = new DeleteButton(event -> deleteWorkModel());

    workModelGrid = new Grid<>();
    workModelGrid.setItems(WORK_MODEL_SERVICE.findAll());

    // columns
    workModelGrid.addColumn(WorkModel::getEmployeeId).setHeader(MessageKeys.EMPLOYEE_ID.getTranslation());
    workModelGrid.addColumn(WorkModel::getValidFrom).setHeader(Messages.get(MessageKeys.WORKMODEL_VALID_FROM));
    workModelGrid.addColumn(WorkModel::getValidUntil).setHeader(Messages.get(MessageKeys.WORKMODEL_VALID_UNTIL));
    workModelGrid.addColumn(WorkModel::getWorktimeADayInMin).setHeader("Daily working time in minutes");
    workModelGrid.addColumn(WorkModel::getBreaktimeADayInMin).setHeader("Daily break time in minutes");
    workModelGrid.addColumn(WorkModel::getInitialHours).setHeader(Messages.get(MessageKeys.WORKMODEL_INITIAL_HOURS));
    workModelGrid.setSizeFull();
    workModelGrid.setColumnReorderingAllowed(true);
    workModelGrid.setMultiSort(true, Grid.MultiSortPriority.APPEND);
    workModelGrid.getColumns().forEach(column -> {
      column.setSortable(true);
      column.setResizable(true);
    });
    workModelGrid.addThemeVariants(GridVariant.LUMO_COMPACT);
    workModelGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
    workModelGrid.addThemeVariants(GridVariant.LUMO_COLUMN_BORDERS);

    final FormLayout employeeFormLayout = new FormLayout(employeeSelect, initialHourField);
    final FormLayout validFormLayout = new FormLayout(validFromPicker, validUntilPicker, workTimePicker, breakTimePicker, saveButton, deleteButton);

    final VerticalLayout verticalLayout = new VerticalLayout(employeeFormLayout, validFormLayout, workModelGrid);
    verticalLayout.setSizeFull();

    setSizeFull();
    add(new ViewToolbar("Work models"));
    add(verticalLayout);
  }

  private void saveEmployee() {
    WORK_MODEL_SERVICE.addWorkModel(new WorkModel(employeeSelect.getValue(),
      validFromPicker.getValue(),
      validUntilPicker.getValue(),
      workTimePicker.getValue().get(ChronoField.MINUTE_OF_DAY),
      breakTimePicker.getValue().get(ChronoField.MINUTE_OF_DAY),
      initialHourField.getValue()));
    workModelGrid.setItems(WORK_MODEL_SERVICE.findAll());
    Notification.show("Work model added").addThemeVariants(NotificationVariant.LUMO_SUCCESS);
  }

  private void deleteWorkModel() {
    final Set<WorkModel> selectedItems = workModelGrid.getSelectedItems();
    if (!selectedItems.isEmpty()) {
      workModelGrid.setItems(WORK_MODEL_SERVICE.deleteWorkModels(selectedItems));
      Notification.show(selectedItems.size() + " items deleted").addThemeVariants(NotificationVariant.LUMO_SUCCESS);
    } else {
      Notification.show("No items selected for deletion").addThemeVariants(NotificationVariant.LUMO_ERROR);
    }
  }

}
