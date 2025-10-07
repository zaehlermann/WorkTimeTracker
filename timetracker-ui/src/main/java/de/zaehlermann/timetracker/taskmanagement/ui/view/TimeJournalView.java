package de.zaehlermann.timetracker.taskmanagement.ui.view;

import static java.util.Arrays.asList;

import java.io.Serial;
import java.time.LocalDate;
import java.util.List;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import de.zaehlermann.timetracker.service.JournalService;
import jakarta.annotation.security.PermitAll;

@Route("time-journal")
@PageTitle("Time Journal")
@Menu(order = 2, icon = "vaadin:clipboard-check", title = "Time Journal")
@PermitAll // When security is enabled, allow all authenticated users
public class TimeJournalView extends Main {

  @Serial
  private static final long serialVersionUID = -7891828904175930796L;
  private static final JournalService JOURNAL_SERVICE = new JournalService();

  public TimeJournalView() {

    final VerticalLayout layout = new VerticalLayout();

    final List<String> allEmployeeNames = JOURNAL_SERVICE.getAllEmployeeNames();

    final Select<String> selectEmployees = new Select<>();
    selectEmployees.setLabel("Employees");
    selectEmployees.setItems(allEmployeeNames); // select from the employee file
    selectEmployees.setValue(allEmployeeNames.getFirst());

    final Select<Integer> selectYear = new Select<>();
    selectYear.setLabel("Year (Not implemented yet.)");
    selectYear.setItems(asList(2025, 2026, 2027, 2028, 2029, 2030)); // select from the employee file
    selectYear.setValue(LocalDate.now().getYear());

    final Select<Integer> selectMonth = new Select<>();
    selectMonth.setLabel("Month (Not implemented yet.)");
    selectMonth.setItems(asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12)); // select from the employee file
    selectMonth.setValue(LocalDate.now().getMonth().getValue());

    final TextArea textArea = new TextArea();
    textArea.setWidthFull();
    textArea.setLabel("Time Journal:");
    textArea.addClassName("journal-font");
    add(textArea);

    final Button button = new Button("Create Journal");
    button.addClickListener(clickEvent -> textArea.setValue(JOURNAL_SERVICE.createJournal(selectEmployees.getValue())));

    layout.add(selectEmployees, selectYear, selectMonth, button, textArea);
    add(layout);

  }

}
