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
import com.vaadin.flow.component.timepicker.TimePicker;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import de.zaehlermann.timetracker.base.ui.component.ViewToolbar;
import de.zaehlermann.timetracker.i18n.MessageKeys;
import de.zaehlermann.timetracker.manager.ui.components.DeleteButton;
import de.zaehlermann.timetracker.manager.ui.components.SaveButton;
import de.zaehlermann.timetracker.model.Correction;
import de.zaehlermann.timetracker.service.CorrectionService;
import de.zaehlermann.timetracker.service.JournalService;
import jakarta.annotation.Nonnull;
import jakarta.annotation.security.PermitAll;

import java.io.Serial;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Route("correction-times")
@PageTitle("Correction times")
@Menu(order = 3, icon = "vaadin:user-check", title = "Correction times")
@PermitAll // When security is enabled, allow all authenticated users
public class CorrectionTimesView extends Main {
  @Serial
  private static final long serialVersionUID = -6845528650198433439L;

  private static final CorrectionService CORRECTION_SERVICE = new CorrectionService();
  private static final JournalService JOURNAL_SERVICE = new JournalService();

  // Form fields
  private final Select<String> selectEmployee = new Select<>();
  private final DatePicker workdayField = new DatePicker("Date");
  private final TimePicker loginTimeField = new TimePicker("Login Time Correction");
  private final TimePicker logoutTimeField = new TimePicker("Logout Time Correction");

  private final Grid<Correction> grid = new Grid<>(Correction.class, false);

  public CorrectionTimesView() {

    final List<String> allEmployeeNames = JOURNAL_SERVICE.getAllEmployeeNames();
    selectEmployee.setLabel(MessageKeys.EMPLOYEE_ID.getTranslation());
    selectEmployee.setItems(allEmployeeNames);
    if(!allEmployeeNames.isEmpty()) {
      selectEmployee.setValue(allEmployeeNames.getFirst());
    }
    else {
      Notification.show("No employees found, please add employees first.").addThemeVariants(NotificationVariant.LUMO_ERROR);
    }

    workdayField.setValue(LocalDate.now());

    final VerticalLayout verticalLayout = new VerticalLayout();
    verticalLayout.setSizeFull();
    verticalLayout.add(createForm());
    configureGrid();
    verticalLayout.add(grid);

    setSizeFull();
    add(new ViewToolbar("Correction times"));
    add(verticalLayout);

    updateGrid();
  }

  private void configureGrid() {
    grid.addColumn(Correction::getEmployeeId).setHeader(MessageKeys.EMPLOYEE_ID.getTranslation());
    grid.addColumn(Correction::getWorkday).setHeader("Date");
    grid.addColumn(Correction::getLogin).setHeader("Login correction");
    grid.addColumn(Correction::getLogout).setHeader("Logout correction");
    grid.setSelectionMode(Grid.SelectionMode.SINGLE);
    grid.setColumnReorderingAllowed(true);
    grid.setMultiSort(true, Grid.MultiSortPriority.APPEND);
    grid.getColumns().forEach(column -> {
      column.setSortable(true);
      column.setResizable(true);
    });
    grid.addThemeVariants(GridVariant.LUMO_COMPACT);
    grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
    grid.addThemeVariants(GridVariant.LUMO_COLUMN_BORDERS);
  }

  @Nonnull
  private FormLayout createForm() {
    final Button saveButton = new SaveButton(event -> saveAbsence());
    final Button deleteButton = new DeleteButton(event -> deleteAbsence());
    return new FormLayout(selectEmployee, workdayField,
                          loginTimeField, logoutTimeField,
                          saveButton, deleteButton);
  }

  private void saveAbsence() {

    final Correction correction = new Correction(selectEmployee.getValue(), workdayField.getValue(),
                                                 loginTimeField.getValue(), logoutTimeField.getValue());
    CORRECTION_SERVICE.save(correction);
    Notification.show("Correction saved").addThemeVariants(NotificationVariant.LUMO_SUCCESS);
    updateGrid();
  }

  private void deleteAbsence() {
    final Set<Correction> selectedItems = grid.getSelectedItems();
    if(!selectedItems.isEmpty()) {
      grid.setItems(CORRECTION_SERVICE.delete(selectedItems));
      Notification.show(selectedItems.size() + " items deleted").addThemeVariants(NotificationVariant.LUMO_SUCCESS);
    }
    else {
      Notification.show("No items selected for deletion").addThemeVariants(NotificationVariant.LUMO_ERROR);
    }
  }

  private void updateGrid() {
    grid.setItems(CORRECTION_SERVICE.findAll());
  }

}
