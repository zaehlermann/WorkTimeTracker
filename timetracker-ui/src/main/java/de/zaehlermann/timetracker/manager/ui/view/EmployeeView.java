package de.zaehlermann.timetracker.manager.ui.view;

import java.io.Serial;
import java.util.List;

import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.shared.HasValidationProperties;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;

import de.zaehlermann.timetracker.base.ui.component.ViewToolbar;
import de.zaehlermann.timetracker.model.Employee;
import de.zaehlermann.timetracker.service.EmployeeService;
import de.zaehlermann.timetracker.service.RfidScanService;
import jakarta.annotation.Nonnull;
import jakarta.annotation.security.PermitAll;

@Route("task-list")
@PageTitle("Task List")
@Menu(order = 0, icon = "vaadin:clipboard-check", title = "Employees")
@PermitAll // When security is enabled, allow all authenticated users
public class EmployeeView extends Main {

  @Serial
  private static final long serialVersionUID = -8665685480589825450L;
  private static final EmployeeService EMPLOYEE_SERVICE = new EmployeeService();
  private static final RfidScanService RFID_SCAN_SERVICE = new RfidScanService();

  private final TextField employeeId = new TextField("Employee ID");
  private final ComboBox<String> rfid = new ComboBox<>("RFID");
  private final TextField firstName = new TextField("First Name");
  private final TextField lastName = new TextField("Last Name");
  private final Grid<Employee> employeeGrid;

  public EmployeeView() {

    employeeId.setRequired(true);
    rfid.setRequired(true);
    rfid.setAllowCustomValue(true);
    rfid.setItems(RFID_SCAN_SERVICE.findAllRfids());
    rfid.addCustomValueSetListener(event -> rfid.setValue(event.getDetail()));
    firstName.setRequired(true);
    lastName.setRequired(true);

    final Button saveBtn = new Button("Save", event -> saveEmployee());
    saveBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    final Button deleteBtn = new Button("Delete", event -> deleteEmployee());

    employeeGrid = new Grid<>();
    employeeGrid.setItems(EMPLOYEE_SERVICE.findAll());

    // columns
    employeeGrid.addColumn(Employee::getEmployeeId).setHeader("Employee ID");
    employeeGrid.addColumn(Employee::getRfid).setHeader("RFID");
    employeeGrid.addColumn(Employee::getFirstName).setHeader("First Name");
    employeeGrid.addColumn(Employee::getLastName).setHeader("Last Name");
    employeeGrid.setSizeFull();

    addClassNames(LumoUtility.BoxSizing.BORDER, LumoUtility.Display.FLEX, LumoUtility.FlexDirection.COLUMN,
                  LumoUtility.Padding.MEDIUM, LumoUtility.Gap.SMALL);

    final FormLayout formLayout = new FormLayout(employeeId, rfid, firstName, lastName, saveBtn, deleteBtn);

    final VerticalLayout verticalLayout = new VerticalLayout(formLayout, employeeGrid);
    verticalLayout.setSizeFull();

    setSizeFull();
    add(new ViewToolbar("Employees"));
    add(verticalLayout);
  }

  private void saveEmployee() {

    final boolean isValid = validate(List.of(rfid, firstName, lastName, employeeId));
    if(!isValid) {
      Notification.show("Please fill all required fields", 3000, Notification.Position.BOTTOM_END)
        .addThemeVariants(NotificationVariant.LUMO_ERROR);
      return;
    }

    EMPLOYEE_SERVICE.addEmployee(new Employee(employeeId.getValue(), rfid.getValue(), firstName.getValue(), lastName.getValue()));
    employeeGrid.setItems(EMPLOYEE_SERVICE.findAll());
    clearTextFields(List.of(rfid, firstName, lastName, employeeId));

    Notification.show("Employee added", 3000, Notification.Position.BOTTOM_END)
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

  private void deleteEmployee() {
    employeeGrid.setItems(EMPLOYEE_SERVICE.deleteByRfid(employeeGrid.getSelectedItems()));
    Notification.show("Employee deleted", 3000, Notification.Position.BOTTOM_END)
      .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
  }

  private boolean validate(@Nonnull final List<? extends HasValue<?, String>> textFields) {
    final boolean isInvalid = textFields.stream()
      .map(this::validate)
      .toList()
      .stream()
      .anyMatch(e -> e.equals(false));
    return !isInvalid;
  }

  private boolean validate(@Nonnull final HasValue<?, String> textField) {
    final boolean invalid = textField.getValue() == null || textField.getValue().trim().isEmpty();
    if(textField instanceof final HasValidationProperties hasValidationProperties) {
      hasValidationProperties.setInvalid(invalid);
    }
    return !invalid;
  }

}
