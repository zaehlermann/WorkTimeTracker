package de.zaehlermann.timetracker.manager.ui.view;

import java.io.Serial;
import java.util.List;
import java.util.Set;

import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.button.Button;
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
import de.zaehlermann.timetracker.manager.ui.components.DeleteButton;
import de.zaehlermann.timetracker.manager.ui.components.SaveButton;
import de.zaehlermann.timetracker.model.Employee;
import de.zaehlermann.timetracker.service.EmployeeService;
import de.zaehlermann.timetracker.service.RfidScanService;
import de.zaehlermann.timetracker.validate.ValidateUtils;
import jakarta.annotation.Nonnull;
import jakarta.annotation.security.PermitAll;

@Route("employees")
@PageTitle("Employees")
@Menu(order = 0, icon = "vaadin:user-card", title = "Employees")
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

    final Button saveButton = new SaveButton(event -> saveEmployee());
    final Button deleteButton = new DeleteButton(event -> deleteEmployee());

    employeeGrid = new Grid<>();
    employeeGrid.setItems(EMPLOYEE_SERVICE.findAll());

    // columns
    employeeGrid.addColumn(Employee::getEmployeeId).setHeader("Employee ID");
    employeeGrid.addColumn(Employee::getRfid).setHeader("RFID");
    employeeGrid.addColumn(Employee::getFirstName).setHeader("First Name");
    employeeGrid.addColumn(Employee::getLastName).setHeader("Last Name");
    employeeGrid.setSizeFull();
    employeeGrid.setColumnReorderingAllowed(true);
    employeeGrid.setMultiSort(true, Grid.MultiSortPriority.APPEND);
    employeeGrid.getColumns().forEach(column -> {
      column.setSortable(true);
      column.setResizable(true);
    });
    employeeGrid.addThemeVariants(GridVariant.LUMO_COMPACT);
    employeeGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
    employeeGrid.addThemeVariants(GridVariant.LUMO_COLUMN_BORDERS);

    final FormLayout formLayout = new FormLayout(employeeId, rfid, firstName, lastName, saveButton, deleteButton);

    final VerticalLayout verticalLayout = new VerticalLayout(formLayout, employeeGrid);
    verticalLayout.setSizeFull();

    setSizeFull();
    add(new ViewToolbar("Employees"));
    add(verticalLayout);
  }

  private void saveEmployee() {

    final boolean isValid = ValidateUtils.validateTextFields(List.of(rfid, firstName, lastName, employeeId));
    if(!isValid) {
      Notification.show("Please fill all required fields").addThemeVariants(NotificationVariant.LUMO_ERROR);
      return;
    }

    EMPLOYEE_SERVICE.addEmployee(new Employee(employeeId.getValue(), rfid.getValue(), firstName.getValue(), lastName.getValue()));
    employeeGrid.setItems(EMPLOYEE_SERVICE.findAll());
    clearTextFields(List.of(rfid, firstName, lastName, employeeId));
    Notification.show("Employee added").addThemeVariants(NotificationVariant.LUMO_SUCCESS);
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
    final Set<Employee> selectedItems = employeeGrid.getSelectedItems();
    if(!selectedItems.isEmpty()) {
      employeeGrid.setItems(EMPLOYEE_SERVICE.deleteByRfid(selectedItems));
      Notification.show(selectedItems.size() + " items deleted").addThemeVariants(NotificationVariant.LUMO_SUCCESS);
    }
    else {
      Notification.show("No items selected for deletion").addThemeVariants(NotificationVariant.LUMO_ERROR);
    }
  }

}
