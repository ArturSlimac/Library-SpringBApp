package validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PriceRangeValidator implements ConstraintValidator<PriceRange, Double> {
	@Override
	public void initialize(PriceRange constraintAnnotation) {
	}

	@Override
	public boolean isValid(Double value, ConstraintValidatorContext context) {
		if (value == null) {
			return true;
		}

		return value > 0 && value < 100;
	}
}