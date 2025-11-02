package de.zaehlermann.timetracker.manager.ui.view;

import java.io.Serial;
import java.util.List;

import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.shared.HasValidationProperties;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import de.zaehlermann.timetracker.base.ui.component.ViewToolbar;
import de.zaehlermann.timetracker.model.WorkModel;
import de.zaehlermann.timetracker.service.RfidScanService;
import de.zaehlermann.timetracker.service.WorkModelService;
import de.zaehlermann.timetracker.validate.ValidateUtils;
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
  private static final RfidScanService RFID_SCAN_SERVICE = new RfidScanService();

  private final TextField employeeId = new TextField("Employee ID");
  private final ComboBox<String> rfid = new ComboBox<>("RFID");
  private final TextField firstName = new TextField("First Name");
  private final TextField lastName = new TextField("Last Name");
  private final Grid<WorkModel> workModelGrid;

  public WorkModelView() {

    employeeId.setRequired(true);
    rfid.setRequired(true);
    rfid.setAllowCustomValue(true);
    rfid.setItems(RFID_SCAN_SERVICE.findAllRfids());
    rfid.addCustomValueSetListener(event -> rfid.setValue(event.getDetail()));
    firstName.setRequired(true);
    lastName.setRequired(true);

    final Button saveBtn = new Button("Save", event -> saveEmployee());
    saveBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    final Button deleteBtn = new Button("Delete", event -> deleteWorkModel());

    workModelGrid = new Grid<>();
    workModelGrid.setItems(WORK_MODEL_SERVICE.findAll());

    // columns
    workModelGrid.addColumn(WorkModel::getEmployeeId).setHeader("Employee ID");
    workModelGrid.addColumn(WorkModel::getValidFrom).setHeader("Valid from");
    workModelGrid.addColumn(WorkModel::getValidUntil).setHeader("Valid until");
    workModelGrid.addColumn(WorkModel::getWorktimeADayInMin).setHeader("Worktime a day in minutes");
    workModelGrid.addColumn(WorkModel::getBreaktimeADayInMin).setHeader("Breaktime a day in minutes");
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

    final FormLayout formLayout = new FormLayout(employeeId, rfid, firstName, lastName, saveBtn, deleteBtn);

    final VerticalLayout verticalLayout = new VerticalLayout(formLayout, workModelGrid);
    verticalLayout.setSizeFull();

    setSizeFull();
    add(new ViewToolbar("Work models"));
    add(verticalLayout);
  }

  private void saveEmployee() {

    final boolean isValid = ValidateUtils.validateTextFields(List.of(rfid, firstName, lastName, employeeId));
    if(!isValid) {
      Notification.show("Please fill all required fields", 3000, Notification.Position.BOTTOM_END)
        .addThemeVariants(NotificationVariant.LUMO_ERROR);
      return;
    }

   // WORK_MODEL_SERVICE.addWorkModel(new WorkModel(employeeId.getValue(), rfid.getValue(), firstName.getValue(), lastName.getValue()));
    workModelGrid.setItems(WORK_MODEL_SERVICE.findAll());
    clearTextFields(List.of(rfid, firstName, lastName, employeeId));

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
