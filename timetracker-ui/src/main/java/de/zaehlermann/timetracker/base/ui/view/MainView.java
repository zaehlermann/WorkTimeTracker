package de.zaehlermann.timetracker.base.ui.view;

import java.io.Serial;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;

import de.zaehlermann.timetracker.base.ui.component.ViewToolbar;
import jakarta.annotation.security.PermitAll;

/**
 * This view shows up when a user navigates to the root ('/') of the application.
 */
@Route
@PermitAll // When security is enabled, allow all authenticated users
public final class MainView extends Main {

  @Serial
  private static final long serialVersionUID = -5009310100331747758L;

  MainView() {
    addClassName(LumoUtility.Padding.MEDIUM);
    add(new ViewToolbar("Main"));
    add(new Div("Welcome to the Time Tracker application!"));
    add(new Div("1. Setup  your employee configuration."));
    add(new Div("2. Manage your employees absence times."));
    add(new Div("3. Scan your rfid tag to login/logout."));
    add(new Div("4. Create time journal for an employee."));
  }

  /**
   * Navigates to the main view.
   */
  public static void showMainView() {
    UI.getCurrent().navigate(MainView.class);
  }
}
