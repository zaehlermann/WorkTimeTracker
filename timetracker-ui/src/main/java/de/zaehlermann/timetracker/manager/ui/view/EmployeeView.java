package de.zaehlermann.timetracker.manager.ui.view;

import java.io.Serial;
import java.util.List;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;

import de.zaehlermann.timetracker.base.ui.component.ViewToolbar;
import de.zaehlermann.timetracker.model.Employee;
import de.zaehlermann.timetracker.service.EmployeeService;
import jakarta.annotation.security.PermitAll;

@Route("task-list")
@PageTitle("Task List")
@Menu(order = 0, icon = "vaadin:clipboard-check", title = "Employees")
@PermitAll // When security is enabled, allow all authenticated users
public class EmployeeView extends Main {

  @Serial
  private static final long serialVersionUID = -8665685480589825450L;
  private static final EmployeeService EMPLOYEE_SERVICE = new EmployeeService();

  private final TextField employeeId = new TextField();
  private final TextField rfid = new TextField();
  private final TextField firstName = new TextField();
  private final TextField lastName = new TextField();

  private final Button saveBtn;
  private final Button deleteBtn;
  private final Grid<Employee> employeeGrid;

  public EmployeeView() {

    employeeId.setPlaceholder("Employee ID");
    rfid.setPlaceholder("RFID");
    firstName.setPlaceholder("First Name");
    lastName.setPlaceholder("Last Name");

    employeeId.setRequired(true);
    rfid.setRequired(true);
    firstName.setRequired(true);
    lastName.setRequired(true);

    saveBtn = new Button("Save", event -> createTask());
    saveBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    deleteBtn = new Button("Delete", event -> deleteTask());

    employeeGrid = new Grid<>();
    employeeGrid.setItems(EMPLOYEE_SERVICE.findAll());

    // columns
    employeeGrid.addColumn(Employee::getEmployeeId).setHeader("Employee ID");
    employeeGrid.addColumn(Employee::getRfid).setHeader("RFID");
    employeeGrid.addColumn(Employee::getFirstName).setHeader("First Name");
    employeeGrid.addColumn(Employee::getLastName).setHeader("Last Name");
    employeeGrid.setSizeFull();

    setSizeFull();
    addClassNames(LumoUtility.BoxSizing.BORDER, LumoUtility.Display.FLEX, LumoUtility.FlexDirection.COLUMN,
                  LumoUtility.Padding.MEDIUM, LumoUtility.Gap.SMALL);

    add(new ViewToolbar("Employees", ViewToolbar.group(employeeId, rfid, firstName, lastName, saveBtn, deleteBtn)));
    add(employeeGrid);
  }

  private void createTask() {

    final boolean isValid = validate(List.of(rfid, firstName, lastName, employeeId));
    if(!isValid) {
      Notification.show("Please fill all required fields", 3000, Notification.Position.BOTTOM_END)
        .addThemeVariants(NotificationVariant.LUMO_ERROR);
      return;
    }

    EMPLOYEE_SERVICE.addEmployee(new Employee(employeeId.getValue(), rfid.getValue(), firstName.getValue(), lastName.getValue()));
    employeeGrid.setItems(EMPLOYEE_SERVICE.findAll());
    employeeId.clear();
    rfid.clear();
    firstName.clear();
    lastName.clear();
    Notification.show("Employee added", 3000, Notification.Position.BOTTOM_END)
      .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
  }

  private void deleteTask() {
    employeeGrid.setItems(EMPLOYEE_SERVICE.deleteByRfid(employeeGrid.getSelectedItems()));
    Notification.show("Employee deleted", 3000, Notification.Position.BOTTOM_END)
      .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
  }

  private boolean validate(final List<TextField> textFields) {
    final boolean isInvalid = textFields.stream()
      .map(this::validate)
      .toList()
      .stream()
      .anyMatch(e -> e.equals(false));
    return !isInvalid;
  }

  private boolean validate(final TextField textField) {
    final boolean invalid = textField.getValue() == null || textField.getValue().trim().isEmpty();
    textField.setInvalid(invalid);
    return !invalid;
  }

}
