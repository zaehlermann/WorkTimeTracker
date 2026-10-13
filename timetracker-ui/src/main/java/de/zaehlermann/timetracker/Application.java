package de.zaehlermann.timetracker;

import java.io.Serial;
import java.time.Clock;

import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.theme.lumo.Lumo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.theme.Theme;

@SpringBootApplication
@StyleSheet(Lumo.STYLESHEET)
@StyleSheet(Lumo.UTILITY_STYLESHEET)
@StyleSheet("themes/default/dev-login.css")
public class Application implements AppShellConfigurator {

  @Serial
  private static final long serialVersionUID = -2379599047979107326L;
  public static final String APP_TITLE = "Work Time Tracker";

  @Bean
  public Clock clock() {
    return Clock.systemDefaultZone(); // You can also use Clock.systemUTC()
  }

  public static void main(final String[] args) {
    SpringApplication.run(Application.class, args);
  }

}
