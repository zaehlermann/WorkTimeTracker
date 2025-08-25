package de.zaehlermann.timetracker.manager.ui.view;

import java.io.Serial;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
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
import de.zaehlermann.timetracker.model.Correction;
import de.zaehlermann.timetracker.service.CorrectionService;
import de.zaehlermann.timetracker.service.JournalService;
import jakarta.annotation.Nonnull;
import jakarta.annotation.security.PermitAll;

@Route("correction-times")
@PageTitle("Correction times")
@Menu(order = 2, icon = "vaadin:clipboard-check", title = "Correction times")
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
    selectEmployee.setLabel("Employee");
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
    grid.addColumn(Correction::getEmployeeId).setHeader("Employee ID");
    grid.addColumn(Correction::getWorkday).setHeader("Date");
    grid.addColumn(Correction::getLogin).setHeader("Login correction");
    grid.addColumn(Correction::getLogout).setHeader("Logout correction");
    grid.setSelectionMode(Grid.SelectionMode.SINGLE);
  }

  @Nonnull
  private FormLayout createForm() {
    final Button saveButton = new Button("Save", event -> saveAbsence());
    saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    final Button deleteButton = new Button("Delete", event -> deleteAbsence());
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
    final Set<Correction> selected = grid.getSelectedItems();
    if(!selected.isEmpty()) {
      CORRECTION_SERVICE.delete(selected);
      Notification.show("Absence deleted").addThemeVariants(NotificationVariant.LUMO_SUCCESS);
      updateGrid();
    }
    else {
      Notification.show("No absence selected for deletion").addThemeVariants(NotificationVariant.LUMO_ERROR);
    }
  }

  private void updateGrid() {
    grid.setItems(CORRECTION_SERVICE.findAll());
  }

}
