package de.zaehlermann.timetracker.manager.ui.view;

import static java.util.Arrays.asList;

import java.io.Serial;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Set;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import de.zaehlermann.timetracker.base.ui.component.ViewToolbar;
import de.zaehlermann.timetracker.i18n.MessageKeys;
import de.zaehlermann.timetracker.manager.ui.components.DeleteButton;
import de.zaehlermann.timetracker.manager.ui.components.SaveButton;
import de.zaehlermann.timetracker.model.Vacation;
import de.zaehlermann.timetracker.service.JournalService;
import de.zaehlermann.timetracker.service.VacationService;
import jakarta.annotation.Nonnull;
import jakarta.annotation.security.PermitAll;

@Route("vacations")
@PageTitle("Vacations")
@Menu(order = 2, icon = "vaadin:user-heart", title = "Vacations")
@PermitAll // When security is enabled, allow all authenticated users
public class VacationView extends Main {
  @Serial
  private static final long serialVersionUID = -6845528650198433439L;

  private static final VacationService VACATION_SERVICE = new VacationService();
  private static final JournalService JOURNAL_SERVICE = new JournalService();

  // Form fields
  private final Select<String> selectEmployee = new Select<>();
  private final Select<Integer> selectYear = new Select<>();
  private final IntegerField selectVacationInDays = new IntegerField(MessageKeys.VACATION_IN_DAYS.getTranslation());
  private final Grid<Vacation> grid = new Grid<>(Vacation.class, false);

  public VacationView() {

    final List<String> allEmployeeNames = JOURNAL_SERVICE.getAllEmployeeNames();
    selectEmployee.setLabel(MessageKeys.EMPLOYEE_ID.getTranslation());
    selectEmployee.setItems(allEmployeeNames);
    if(!allEmployeeNames.isEmpty()) {
      selectEmployee.setValue(allEmployeeNames.getFirst());
    }
    else {
      Notification.show("No employees found, please add employees first.").addThemeVariants(NotificationVariant.LUMO_ERROR);
    }

    selectYear.setLabel(MessageKeys.VACATION_YEAR.getTranslation());
    selectYear.setItems(asList(2025, 2026, 2027, 2028, 2029, 2030));
    selectYear.setValue(LocalDate.now(ZoneId.systemDefault()).getYear());

    selectVacationInDays.setLabel(MessageKeys.VACATION_IN_DAYS.getTranslation());

    selectVacationInDays.setMin(0);
    selectVacationInDays.setMax(365);
    selectVacationInDays.setValue(30);

    final Details descriptionDetails = new Details(MessageKeys.HINTS.getTranslation(),
                                                   new Div(MessageKeys.VACATION_DESCRIPTION.getTranslation()));
    descriptionDetails.setOpened(false);

    final VerticalLayout verticalLayout = new VerticalLayout();
    verticalLayout.setSizeFull();
    verticalLayout.add(descriptionDetails);
    verticalLayout.add(createForm());
    configureGrid();
    verticalLayout.add(grid);

    setSizeFull();
    add(new ViewToolbar(MessageKeys.VACATIONS.getTranslation()));
    add(verticalLayout);

    updateGrid();
  }

  private void configureGrid() {
    grid.addColumn(Vacation::getEmployeeId).setHeader(MessageKeys.EMPLOYEE_ID.getTranslation());
    grid.addColumn(Vacation::getYear).setHeader(MessageKeys.VACATION_YEAR.getTranslation());
    grid.addColumn(Vacation::getVacationInDays).setHeader(MessageKeys.VACATION_IN_DAYS.getTranslation());
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
    final Button saveButton = new SaveButton(event -> saveVacation());
    final Button deleteButton = new DeleteButton(event -> deleteVacation());
    return new FormLayout(selectEmployee, selectYear, selectVacationInDays, new Div() /* placeholder */,
                          saveButton, deleteButton);
  }

  private void saveVacation() {

    final Vacation vacation = new Vacation(selectEmployee.getValue(), selectYear.getValue(), selectVacationInDays.getValue());
    VACATION_SERVICE.save(vacation);
    Notification.show(MessageKeys.VACATION_SAVED.getTranslation()).addThemeVariants(NotificationVariant.LUMO_SUCCESS);
    updateGrid();
  }

  private void deleteVacation() {
    final Set<Vacation> selectedItems = grid.getSelectedItems();
    if(!selectedItems.isEmpty()) {
      grid.setItems(VACATION_SERVICE.delete(selectedItems));
      Notification.show(selectedItems.size() + " items deleted").addThemeVariants(NotificationVariant.LUMO_SUCCESS);
    }
    else {
      Notification.show("No items selected for deletion").addThemeVariants(NotificationVariant.LUMO_ERROR);
    }
  }

  private void updateGrid() {
    grid.setItems(VACATION_SERVICE.findAll());
  }

}
