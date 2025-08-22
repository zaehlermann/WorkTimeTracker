package de.zaehlermann.timetracker.taskmanagement.ui.view;

import java.io.Serial;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.timepicker.TimePicker;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import de.zaehlermann.timetracker.model.Absence;
import de.zaehlermann.timetracker.model.AbsenceType;
import de.zaehlermann.timetracker.service.AbsenceService;
import de.zaehlermann.timetracker.service.JournalService;
import jakarta.annotation.security.PermitAll;

@Route("absence-times")
@PageTitle("Absence Times")
@Menu(order = 1, icon = "vaadin:clipboard-check", title = "Absence Times")
@PermitAll // When security is enabled, allow all authenticated users
public class AbsenceTimesView extends Main {
  @Serial
  private static final long serialVersionUID = -6845528650198433439L;

  private static final AbsenceService ABSENCE_SERVICE = new AbsenceService();
  private static final JournalService JOURNAL_SERVICE = new JournalService();

  // Form fields
  private final Select<String> selectEmployee = new Select<>();
  private final Select<AbsenceType> selectType = new Select<>();
  private final DatePicker startDateField = new DatePicker("Start Date");
  private final TimePicker startTimeField = new TimePicker("Start Time");
  private final DatePicker endDateField = new DatePicker("End Date");
  private final TimePicker endTimeField = new TimePicker("End Time");

  private final Grid<Absence> grid = new Grid<>(Absence.class, false);

  public AbsenceTimesView() {

    final List<String> allEmployeeNames = JOURNAL_SERVICE.getAllEmployeeNames();
    selectEmployee.setLabel("Employee");
    selectEmployee.setItems(allEmployeeNames);
    selectEmployee.setValue(allEmployeeNames.getFirst());

    final List<AbsenceType> absenceTypes = AbsenceType.getSelectableValues();
    selectType.setLabel("Type");
    selectType.setItems(absenceTypes);
    selectType.setValue(absenceTypes.getFirst());

    startDateField.setValue(LocalDate.now());

    final VerticalLayout layout = new VerticalLayout();
    layout.setSizeFull();

    configureGrid();
    layout.add(grid);

    layout.add(createForm());

    setSizeFull();
    add(layout);

    updateGrid();
  }

  private void configureGrid() {
    grid.addColumn(Absence::employeeId).setHeader("Employee ID");
    grid.addColumn(Absence::startDay).setHeader("Start Date");
    grid.addColumn(Absence::startTime).setHeader("Start Time");
    grid.addColumn(Absence::endDay).setHeader("End Day");
    grid.addColumn(Absence::endTime).setHeader("End Time");
    grid.addColumn(Absence::type).setHeader("Type");
    grid.setSelectionMode(Grid.SelectionMode.SINGLE);
  }

  private FormLayout createForm() {
    final FormLayout formLayout = new FormLayout();
    formLayout.add(selectEmployee, selectType, startDateField, startTimeField, endDateField, endTimeField);

    final Button saveButton = new Button("Save", event -> saveAbsence());
    final Button deleteButton = new Button("Delete", event -> deleteAbsence());

    final HorizontalLayout buttons = new HorizontalLayout(saveButton, deleteButton);
    formLayout.add(buttons);

    return formLayout;
  }

  private void saveAbsence() {

    final Absence newAbsence = new Absence(selectEmployee.getValue(), selectType.getValue(),
                                           startDateField.getValue(), endDateField.getValue(),
                                           startTimeField.getValue(), endTimeField.getValue());
    ABSENCE_SERVICE.save(newAbsence);
    Notification.show("Absence saved");
    updateGrid();
  }

  private void deleteAbsence() {
    final Set<Absence> selected = grid.getSelectedItems();
    if(!selected.isEmpty()) {
      ABSENCE_SERVICE.delete(selected);
      Notification.show("Absence deleted").addThemeVariants(NotificationVariant.LUMO_SUCCESS);
      updateGrid();
    }
    else {
      Notification.show("No absence selected for deletion").addThemeVariants(NotificationVariant.LUMO_ERROR);
    }
  }

  private void updateGrid() {
    grid.setItems(ABSENCE_SERVICE.findAll());
  }

}
