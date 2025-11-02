package de.zaehlermann.timetracker.manager.ui.components;

import java.io.Serial;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.VaadinIcon;

public class DeleteButton extends Button {

  @Serial
  private static final long serialVersionUID = -8663652365329140695L;

  public DeleteButton(final ComponentEventListener<ClickEvent<Button>> clickListener) {
    super("Delete", clickListener);
    this.addThemeVariants(ButtonVariant.LUMO_ERROR);
    this.setIcon(VaadinIcon.CLOSE_CIRCLE.create());
  }
}
