package de.zaehlermann.timetracker.security.dev;

import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.component.page.WebStorage;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.spring.security.AuthenticationContext;
import de.zaehlermann.timetracker.i18n.MessageKeys;

import java.io.Serial;

/**
 * Login view for development.
 */
@PageTitle("Login")
@AnonymousAllowed
public// No @Route annotation - the route is registered dynamically by DevSecurityConfig.
class DevLoginView extends Main implements BeforeEnterObserver {

  static final String LOGIN_PATH = "login";
  private static final String CALLOUT_HIDDEN_KEY = "walking-skeleton-dev-login-callout-hidden";
  @Serial
  private static final long serialVersionUID = 2549806305325647933L;

  private final AuthenticationContext authenticationContext;
  private final LoginOverlay login;

  DevLoginView(final AuthenticationContext authenticationContext) {
    this.authenticationContext = authenticationContext;

    final LoginI18n.ErrorMessage i18nErrorMessage = new LoginI18n.ErrorMessage();
    i18nErrorMessage.setTitle(MessageKeys.LOGIN_ERROR_TITLE.getTranslation());
    i18nErrorMessage.setMessage(MessageKeys.LOGIN_ERROR_MESSAGE.getTranslation());
    i18nErrorMessage.setUsername(MessageKeys.LOGIN_ERROR_USERNAME.getTranslation());
    i18nErrorMessage.setPassword(MessageKeys.LOGIN_ERROR_PASSWORD.getTranslation());

    final LoginI18n.Form i18nForm = new LoginI18n.Form();
    i18nForm.setTitle(MessageKeys.LOGIN_TITLE.getTranslation());
    i18nForm.setUsername(MessageKeys.LOGIN_USERNAME.getTranslation());
    i18nForm.setPassword(MessageKeys.LOGIN_PASSWORD.getTranslation());
    i18nForm.setSubmit(MessageKeys.LOGIN_SUBMIT.getTranslation());
    i18nForm.setForgotPassword(MessageKeys.LOGIN_FORGOT.getTranslation());

    final LoginI18n i18n = new LoginI18n();
    i18n.setForm(i18nForm);
    i18n.setErrorMessage(i18nErrorMessage);

    login = new LoginOverlay();
    login.setTitle("Work Time Tracker");
    login.setDescription("© by zaehlermann");
    login.setAction(LOGIN_PATH);
    login.setForgotPasswordButtonVisible(false);
    login.setI18n(i18n);

    // Configure the view
    setSizeFull();
    addClassNames("dev-login-view");
    add(login);
    login.setOpened(true);

    // Don't show the callout if already hidden once
    WebStorage.getItem(WebStorage.Storage.LOCAL_STORAGE, CALLOUT_HIDDEN_KEY);
  }

  @Override
  public void beforeEnter(final BeforeEnterEvent event) {
    if (authenticationContext.isAuthenticated()) {
      // Redirect to the main view if the user is already logged in. This makes impersonation easier to work with.
      event.forwardTo("");
      return;
    }

    if (event.getLocation().getQueryParameters().getParameters().containsKey("error")) {
      login.setError(true);
    }
  }
}
