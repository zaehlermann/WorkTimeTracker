package de.zaehlermann.timetracker.manager.ui.components;

import com.vaadin.flow.component.datepicker.DatePicker;

import java.io.Serial;

public class MyDatePicker extends DatePicker {

  @Serial
  private static final long serialVersionUID = -4056688533592820201L;

  public MyDatePicker(final String label) {
    super(label);
    final DatePicker.DatePickerI18n i18n = new DatePicker.DatePickerI18n();
    i18n.setFirstDayOfWeek(1);
    this.setI18n(i18n);
  }
}