package de.zaehlermann.timetracker.security.dev;

import java.io.Serial;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.DescriptionList;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.page.WebStorage;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.spring.security.AuthenticationContext;

/**
 * Login view for development.
 */
@PageTitle("Login")
@AnonymousAllowed
  // No @Route annotation - the route is registered dynamically by DevSecurityConfig.
class DevLoginView extends Main implements BeforeEnterObserver {

  static final String LOGIN_PATH = "login";
  private static final String CALLOUT_HIDDEN_KEY = "walking-skeleton-dev-login-callout-hidden";
  @Serial
  private static final long serialVersionUID = 2549806305325647933L;

  private final AuthenticationContext authenticationContext;
  private final LoginForm login;

  DevLoginView(final AuthenticationContext authenticationContext) {
    this.authenticationContext = authenticationContext;

    // Create the components
    login = new LoginForm();
    login.setAction(LOGIN_PATH);
    login.setForgotPasswordButtonVisible(false);

    //final Div exampleUsers = new Div(new Div("Use the following details to login"));
    //SampleUsers.ALL_USERS.forEach(user -> exampleUsers.add(createSampleUserCard(user)));

    // Configure the view
    setSizeFull();
    addClassNames("dev-login-view");

    //exampleUsers.addClassNames("dev-users");

    final Div contentDiv = new Div(login);//, exampleUsers);
    contentDiv.addClassNames("dev-content-div");
    add(contentDiv);

    // Don't show the callout if already hidden once
    WebStorage.getItem(WebStorage.Storage.LOCAL_STORAGE, CALLOUT_HIDDEN_KEY);
  }

  private Component createSampleUserCard(final DevUser user) {
    final Div card = new Div();
    card.addClassNames("dev-user-card");

    final H3 fullName = new H3(user.getAppUser().getFullName());

    final DescriptionList credentials = new DescriptionList();
    credentials.add(new DescriptionList.Term("Username"), new DescriptionList.Description(user.getUsername()));
    credentials.add(new DescriptionList.Term("Password"),
                    new DescriptionList.Description(SampleUsers.SAMPLE_PASSWORD));

    // Make it easier to log in while still going through the normal authentication process.
    final Button loginButton = new Button(VaadinIcon.SIGN_IN.create(), event ->
      login.getElement().executeJs("""
                                     document.getElementById("vaadinLoginUsername").value = $0;
                                     document.getElementById("vaadinLoginPassword").value = $1;
                                     document.forms[0].submit();
                                     """, user.getUsername(), SampleUsers.SAMPLE_PASSWORD));
    loginButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_TERTIARY);

    card.add(new Div(fullName, credentials));
    card.add(loginButton);

    return card;
  }

  @Override
  public void beforeEnter(final BeforeEnterEvent event) {
    if(authenticationContext.isAuthenticated()) {
      // Redirect to the main view if the user is already logged in. This makes impersonation easier to work with.
      event.forwardTo("");
      return;
    }

    if(event.getLocation().getQueryParameters().getParameters().containsKey("error")) {
      login.setError(true);
    }
  }
}
