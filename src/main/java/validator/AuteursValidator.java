package validator;

import java.util.List;

import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import domain.Auteur;

public class AuteursValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		// TODO Auto-generated method stub
		return Auteur.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		// TODO Auto-generated method stub
		List<Auteur> auteurs = (List<Auteur>) target;

		if (auteurs.size() > 3)
			errors.rejectValue("auteurs", "auteursValidation.maxAuteurs", "max 3 auteurs");

		if (auteurs.isEmpty() || !hasValidAuthorCombination(auteurs)) {
			errors.rejectValue("auteurs", "auteursValidation.required", "at least 1 author requered");
		}
	}

	private boolean hasValidAuthorCombination(List<Auteur> auteurs) {
		return auteurs.stream().anyMatch(
				auteur -> StringUtils.hasText(auteur.getFirstName()) && StringUtils.hasText(auteur.getLastName()));
	}

}
