package de.zaehlermann.timetracker.manager.ui.view;

import java.io.Serial;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoField;
import java.util.List;

import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.shared.HasValidationProperties;
import com.vaadin.flow.component.timepicker.TimePicker;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import de.zaehlermann.timetracker.base.ui.component.ViewToolbar;
import de.zaehlermann.timetracker.model.WorkModel;
import de.zaehlermann.timetracker.service.JournalService;
import de.zaehlermann.timetracker.service.WorkModelService;
import jakarta.annotation.Nonnull;
import jakarta.annotation.security.PermitAll;

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
  private final DatePicker validFromPicker = new DatePicker("Valid from");
  private final DatePicker validUntilPicker = new DatePicker("Valid until");
  private final TimePicker workTimePicker = new TimePicker("Daily working time (HH:mm)");
  private final TimePicker breakTimePicker = new TimePicker("Daily break time (HH:mm)");
  private final Grid<WorkModel> workModelGrid;

  public WorkModelView() {
    final List<String> allEmployeeNames = JOURNAL_SERVICE.getAllEmployeeNames();
    employeeSelect.setLabel("Employee");
    employeeSelect.setItems(allEmployeeNames);
    if(!allEmployeeNames.isEmpty()) {
      employeeSelect.setValue(allEmployeeNames.getFirst());
    }
    else {
      Notification.show("No employees found, please add employees first.").addThemeVariants(NotificationVariant.LUMO_ERROR);
    }

    validFromPicker.setRequired(true);
    validUntilPicker.setRequired(false);
    workTimePicker.setRequired(true);
    breakTimePicker.setRequired(false);

    validFromPicker.setValue(LocalDate.now());
    workTimePicker.setValue(LocalTime.of(8, 0));
    breakTimePicker.setValue(LocalTime.of(0, 30));

    final Button saveBtn = new Button("Save", event -> saveEmployee());
    saveBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    final Button deleteBtn = new Button("Delete", event -> deleteWorkModel());

    workModelGrid = new Grid<>();
    workModelGrid.setItems(WORK_MODEL_SERVICE.findAll());

    // columns
    workModelGrid.addColumn(WorkModel::getEmployeeId).setHeader("Employee ID");
    workModelGrid.addColumn(WorkModel::getValidFrom).setHeader("Valid from");
    workModelGrid.addColumn(WorkModel::getValidUntil).setHeader("Valid until");
    workModelGrid.addColumn(WorkModel::getWorktimeADayInMin).setHeader("Daily working time in minutes");
    workModelGrid.addColumn(WorkModel::getBreaktimeADayInMin).setHeader("Daily break time in minutes");
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

    final FormLayout formLayout = new FormLayout(validFromPicker, validUntilPicker, workTimePicker, breakTimePicker, saveBtn, deleteBtn);

    final VerticalLayout verticalLayout = new VerticalLayout(employeeSelect, formLayout, workModelGrid);
    verticalLayout.setSizeFull();

    setSizeFull();
    add(new ViewToolbar("Work models"));
    add(verticalLayout);
  }

  private void saveEmployee() {

    //    final boolean isValid = ValidateUtils.validateTextFields(List.of(employeeSelect, validFromPicker, validUntilPicker, workTimePicker, breakTimePicker));
    //    if(!isValid) {
    //      Notification.show("Please fill all required fields", 3000, Notification.Position.BOTTOM_END)
    //        .addThemeVariants(NotificationVariant.LUMO_ERROR);
    //      return;
    //    }

    WORK_MODEL_SERVICE.addWorkModel(new WorkModel(employeeSelect.getValue(),
                                                  validFromPicker.getValue(),
                                                  validUntilPicker.getValue(),
                                                  workTimePicker.getValue().get(ChronoField.MINUTE_OF_DAY),
                                                  breakTimePicker.getValue().get(ChronoField.MINUTE_OF_DAY)));
    workModelGrid.setItems(WORK_MODEL_SERVICE.findAll());
    //clearTextFields(List.of(rfid, firstName, lastName, employeeId));

    Notification.show("Work model added", 3000, Notification.Position.BOTTOM_END)
      .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
  }

  private void clearTextFields(@Nonnull final List<? extends HasValue<?, String>> textFields) {
    textFields.forEach(HasValue::clear);
    for(final HasValue<?, String> textField : textFields) {
      if(textField instanceof final HasValidationProperties hasValidationProperties) {
        hasValidationProperties.setInvalid(false);
      }
    }
  }

  private void deleteWorkModel() {
    workModelGrid.setItems(WORK_MODEL_SERVICE.deleteWorkModels(workModelGrid.getSelectedItems()));
    Notification.show("Work models deleted", 3000, Notification.Position.BOTTOM_END)
      .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
  }

}
