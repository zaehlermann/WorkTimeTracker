package de.zaehlermann.timetracker.validate;

import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.shared.HasValidationProperties;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import java.util.List;

@NonNullByDefault
public class ValidateUtils {

  public static boolean validateSelects(final List<? extends Select<?>> textFields) {
    final boolean isInvalid = textFields.stream()
      .map(ValidateUtils::validate)
      .toList()
      .stream()
      .anyMatch(e -> e.equals(false));
    return !isInvalid;
  }

  public static boolean validateTextFields(final List<? extends HasValue<?, String>> textFields) {
    final boolean isInvalid = textFields.stream()
      .map(ValidateUtils::validate)
      .toList()
      .stream()
      .anyMatch(e -> e.equals(false));
    return !isInvalid;
  }

  private static boolean validate(final HasValue<?, @Nullable String> textField) {
    final boolean invalid = textField.getValue() == null || textField.getValue().trim().isEmpty();
    if (textField instanceof final HasValidationProperties hasValidationProperties) {
      hasValidationProperties.setInvalid(invalid);
    }
    return !invalid;
  }

  private static boolean validate(final Select<?> select) {
    final boolean invalid = select.getValue() == null;
    select.setInvalid(invalid);
    return !invalid;
  }

}
