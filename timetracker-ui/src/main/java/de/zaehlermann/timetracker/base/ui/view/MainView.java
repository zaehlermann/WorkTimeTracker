package de.zaehlermann.timetracker.base.ui.view;

import java.io.Serial;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;

import de.zaehlermann.timetracker.base.ui.component.ViewToolbar;
import de.zaehlermann.timetracker.i18n.MessageKeys;
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
    add(new ViewToolbar(MessageKeys.MAIN.getTranslation()));
    add(new Div(MessageKeys.MAIN_WELCOME_0.getTranslation()));
    add(new Div(MessageKeys.MAIN_WELCOME_1.getTranslation()));
    add(new Div(MessageKeys.MAIN_WELCOME_2.getTranslation()));
    add(new Div(MessageKeys.MAIN_WELCOME_3.getTranslation()));
    add(new Div(MessageKeys.MAIN_WELCOME_4.getTranslation()));
  }

  /**
   * Navigates to the main view.
   */
  public static void showMainView() {
    UI.getCurrent().navigate(MainView.class);
  }
}
