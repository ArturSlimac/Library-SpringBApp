package validator;

import java.util.List;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import domain.LocationPlace;
import io.micrometer.common.util.StringUtils;

public class LocationPlaceValidation implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		// TODO Auto-generated method stub
		return LocationPlace.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		// TODO Auto-generated method stub
		List<LocationPlace> locations = (List<LocationPlace>) target;

		if (locations.size() > 3)
			errors.rejectValue("locationPlaces", "locationValidation.maxLocations", "max 3 locations");

		boolean isValid = locations.stream().anyMatch(this::hasValidLocationPlace);

		if (!isValid) {
			errors.rejectValue("locationPlaces", "locationValidation.required", "at least 1 place requered");
			return;
		}

		locations.forEach(location -> {
			if (location.getCode1() < 50 || location.getCode1() > 300 || location.getCode2() < 50
					|| location.getCode2() > 300) {
				errors.rejectValue("locationPlaces", "locationValidation.placecodeRange",
						"Place code should be between 50 and 300");
				return;
			}

			if (!location.getName().matches("^[a-zA-Z ' -]+$")) {
				errors.rejectValue("locationPlaces", "locationValidation.placeName", "The name is letters, ' and -");
				return;
			}

			if (Math.abs(location.getCode1() - location.getCode2()) < 50) {
				errors.rejectValue("locationPlaces", "locationValidation.placeCodes",
						"Placecode1 - palcecode2 sould be at least 50");
				return;
			}
		});
	}

	private boolean hasValidLocationPlace(LocationPlace locationPlace) {
		return StringUtils.isNotBlank(locationPlace.getName()) && locationPlace.getCode1() != null
				&& locationPlace.getCode2() != null;
	}

}
