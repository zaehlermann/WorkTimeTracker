package de.zaehlermann.timetracker.taskmanagement.ui.view;

import java.io.Serial;

import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import jakarta.annotation.security.PermitAll;

@Route("absence-times")
@PageTitle("Absence Times")
@Menu(order = 1, icon = "vaadin:clipboard-check", title = "Absence Times")
@PermitAll // When security is enabled, allow all authenticated users
public class AbsenceTimesView extends Main {
  @Serial
  private static final long serialVersionUID = -6845528650198433439L;
}
