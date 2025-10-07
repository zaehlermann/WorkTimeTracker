package de.zaehlermann.timetracker.base.ui.view;

import static com.vaadin.flow.theme.lumo.LumoUtility.AlignItems;
import static com.vaadin.flow.theme.lumo.LumoUtility.Display;
import static com.vaadin.flow.theme.lumo.LumoUtility.FontSize;
import static com.vaadin.flow.theme.lumo.LumoUtility.FontWeight;
import static com.vaadin.flow.theme.lumo.LumoUtility.Gap;
import static com.vaadin.flow.theme.lumo.LumoUtility.IconSize;
import static com.vaadin.flow.theme.lumo.LumoUtility.Margin;
import static com.vaadin.flow.theme.lumo.LumoUtility.Padding;
import static com.vaadin.flow.theme.lumo.LumoUtility.TextColor;

import java.io.Serial;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.avatar.AvatarVariant;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.menubar.MenuBarVariant;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.Layout;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.server.menu.MenuConfiguration;
import com.vaadin.flow.server.menu.MenuEntry;
import com.vaadin.flow.spring.security.AuthenticationContext;

import de.zaehlermann.timetracker.security.AppUserInfo;
import de.zaehlermann.timetracker.security.CurrentUser;
import de.zaehlermann.timetracker.service.InfoService;

@Layout
@AnonymousAllowed // Allow all users, including anonymous ones. If you want only authenticated users, change to @PermitAll.
public final class MainLayout extends AppLayout {

  private static final InfoService INFO_SERVICE = new InfoService();

  @Serial
  private static final long serialVersionUID = -1005348531124747334L;
  private final AuthenticationContext authenticationContext;

  MainLayout(final CurrentUser currentUser, final AuthenticationContext authenticationContext) {
    this.authenticationContext = authenticationContext;
    setPrimarySection(Section.DRAWER);
    addToDrawer(createHeader(), new Scroller(createSideNav()));
    currentUser.get().ifPresent(user -> addToDrawer(createUserMenu(user)));
  }

  private VerticalLayout createHeader() {
    final Icon appLogo = VaadinIcon.CUBES.create();
    appLogo.addClassNames(TextColor.PRIMARY, IconSize.LARGE);

    final Span appName = new Span("TimeTrackerManager");
    appName.addClassNames(FontWeight.SEMIBOLD, FontSize.LARGE);

    final Div header = new Div(appLogo, appName);
    header.addClassNames(Display.FLEX, Padding.MEDIUM, Gap.MEDIUM, AlignItems.CENTER);

    final Div divVersion = new Div(INFO_SERVICE.getAppVersion());

    return new VerticalLayout(header, divVersion);
  }

  private SideNav createSideNav() {
    final SideNav nav = new SideNav();
    nav.addClassNames(Margin.Horizontal.MEDIUM);
    MenuConfiguration.getMenuEntries().forEach(entry -> nav.addItem(createSideNavItem(entry)));
    return nav;
  }

  private SideNavItem createSideNavItem(final MenuEntry menuEntry) {
    if(menuEntry.icon() != null) {
      return new SideNavItem(menuEntry.title(), menuEntry.path(), new Icon(menuEntry.icon()));
    }
    else {
      return new SideNavItem(menuEntry.title(), menuEntry.path());
    }
  }

  private Component createUserMenu(final AppUserInfo user) {
    final Avatar avatar = new Avatar(user.getFullName(), user.getPictureUrl());
    avatar.addThemeVariants(AvatarVariant.LUMO_XSMALL);
    avatar.addClassNames(Margin.Right.SMALL);
    avatar.setColorIndex(5);

    final MenuBar userMenu = new MenuBar();
    userMenu.addThemeVariants(MenuBarVariant.LUMO_TERTIARY_INLINE);
    userMenu.addClassNames(Margin.MEDIUM);

    final MenuItem userMenuItem = userMenu.addItem(avatar);
    userMenuItem.add(user.getFullName());
    if(user.getProfileUrl() != null) {
      userMenuItem.getSubMenu().addItem("View Profile",
                                        event -> UI.getCurrent().getPage().open(user.getProfileUrl()));
    }

    userMenuItem.getSubMenu().addItem("Logout", event -> authenticationContext.logout());
    return userMenu;
  }

}
