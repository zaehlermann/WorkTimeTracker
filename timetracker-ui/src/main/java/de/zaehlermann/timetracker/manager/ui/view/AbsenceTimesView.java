package de.zaehlermann.timetracker.manager.ui.view;

import java.io.Serial;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Set;

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
import de.zaehlermann.timetracker.i18n.Messages;
import de.zaehlermann.timetracker.manager.ui.components.DeleteButton;
import de.zaehlermann.timetracker.manager.ui.components.SaveButton;
import de.zaehlermann.timetracker.model.Absence;
import de.zaehlermann.timetracker.model.AbsenceType;
import de.zaehlermann.timetracker.service.AbsenceService;
import de.zaehlermann.timetracker.service.JournalService;
import jakarta.annotation.security.PermitAll;

@Route("absence-times")
@PageTitle("Absence times")
@Menu(order = 2, icon = "vaadin:user-clock", title = "Absence times")
@PermitAll // When security is enabled, allow all authenticated users
public class AbsenceTimesView extends Main {
  @Serial
  private static final long serialVersionUID = -6845528650198433439L;

  private static final AbsenceService ABSENCE_SERVICE = new AbsenceService();
  private static final JournalService JOURNAL_SERVICE = new JournalService();

  // Form fields
  private final Select<String> selectEmployee = new Select<>();
  private final Select<AbsenceType> selectAbsenceType = new Select<>();
  private final DatePicker startDateField = new DatePicker(Messages.get(MessageKeys.ABSENCE_START_DATE));
  private final TimePicker startTimeField = new TimePicker(Messages.get(MessageKeys.ABSENCE_START_TIME));
  private final DatePicker endDateField = new DatePicker(Messages.get(MessageKeys.ABSENCE_END_DATE));
  private final TimePicker endTimeField = new TimePicker(Messages.get(MessageKeys.ABSENCE_END_TIME));

  private final Grid<Absence> grid = new Grid<>(Absence.class, false);

  public AbsenceTimesView() {

    final List<String> allEmployeeNames = JOURNAL_SERVICE.getAllEmployeeNames();
    selectEmployee.setLabel(MessageKeys.EMPLOYEE_ID.getTranslation());
    selectEmployee.setItems(allEmployeeNames);
    if(!allEmployeeNames.isEmpty()) {
      selectEmployee.setValue(allEmployeeNames.getFirst());
    }
    else {
      Notification.show("No employees found, please add employees first.").addThemeVariants(NotificationVariant.LUMO_ERROR);
    }

    final List<AbsenceType> absenceTypes = AbsenceType.getSelectableValues();
    selectAbsenceType.setLabel(Messages.get(MessageKeys.ABSENCE_TYPE));
    selectAbsenceType.setItems(absenceTypes);
    selectAbsenceType.setValue(absenceTypes.getFirst());
    selectAbsenceType.setItemLabelGenerator(AbsenceType::getPrintValueLong);

    startDateField.setValue(LocalDate.now(ZoneId.systemDefault()));

    final VerticalLayout verticalLayout = new VerticalLayout();
    verticalLayout.setSizeFull();
    verticalLayout.add(createForm());
    configureGrid();
    verticalLayout.add(grid);

    setSizeFull();
    add(new ViewToolbar(Messages.get(MessageKeys.ABSENCE_TIMES)));
    add(verticalLayout);

    grid.setItems(ABSENCE_SERVICE.findAll());
  }

  private void configureGrid() {
    grid.addColumn(Absence::employeeId).setHeader(MessageKeys.EMPLOYEE_ID.getTranslation());
    grid.addColumn(Absence::startDay).setHeader(Messages.get(MessageKeys.ABSENCE_START_DATE));
    grid.addColumn(Absence::startTime).setHeader(Messages.get(MessageKeys.ABSENCE_START_TIME));
    grid.addColumn(Absence::endDay).setHeader(Messages.get(MessageKeys.ABSENCE_END_DATE));
    grid.addColumn(Absence::endTime).setHeader(Messages.get(MessageKeys.ABSENCE_END_TIME));
    grid.addColumn(absence -> absence.type().getPrintValueLong()).setHeader(Messages.get(MessageKeys.ABSENCE_TYPE));
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

  private FormLayout createForm() {
    final Button saveButton = new SaveButton(event -> saveAbsence());
    final Button deleteButton = new DeleteButton(event -> deleteAbsence());
    return new FormLayout(selectEmployee, selectAbsenceType,
                          startDateField, startTimeField,
                          endDateField, endTimeField,
                          saveButton, deleteButton);
  }

  private void saveAbsence() {

    final Absence newAbsence = new Absence(selectEmployee.getValue(), selectAbsenceType.getValue(),
                                           startDateField.getValue(), endDateField.getValue(),
                                           startTimeField.getValue(), endTimeField.getValue());
    ABSENCE_SERVICE.save(newAbsence);
    Notification.show("Absence saved").addThemeVariants(NotificationVariant.LUMO_SUCCESS);
    grid.setItems(ABSENCE_SERVICE.findAll());
  }

  private void deleteAbsence() {
    final Set<Absence> selectedItems = grid.getSelectedItems();
    if(!selectedItems.isEmpty()) {
      grid.setItems(ABSENCE_SERVICE.delete(selectedItems));
      Notification.show(selectedItems.size() + " items deleted").addThemeVariants(NotificationVariant.LUMO_SUCCESS);
    }
    else {
      Notification.show("No items selected for deletion").addThemeVariants(NotificationVariant.LUMO_ERROR);
    }
  }

}
