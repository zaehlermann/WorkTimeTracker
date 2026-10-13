package de.zaehlermann.timetracker.manager.ui.components;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.VaadinIcon;
import de.zaehlermann.timetracker.i18n.MessageKeys;
import de.zaehlermann.timetracker.i18n.Messages;

import java.io.Serial;

public class SaveButton extends Button {

  @Serial
  private static final long serialVersionUID = -8663652365329140695L;

  public SaveButton(final ComponentEventListener<ClickEvent<Button>> clickListener) {
    super(Messages.get(MessageKeys.SAVE), clickListener);
    this.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
    this.setIcon(VaadinIcon.CHECK_CIRCLE.create());
  }
}
