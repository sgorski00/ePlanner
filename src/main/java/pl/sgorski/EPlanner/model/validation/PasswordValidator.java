package pl.sgorski.EPlanner.model.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import pl.sgorski.EPlanner.model.ApplicationUser;

public class PasswordValidator implements ConstraintValidator<ValidPassword, ApplicationUser> {
    @Override
    public boolean isValid(ApplicationUser user, ConstraintValidatorContext constraintValidatorContext) {
        if (user == null) return true;
        String provider = user.getProvider();
        String password = user.getPassword();

        if (provider.equalsIgnoreCase("LOCAL")){
            if (password == null || password.length() < 5) return false;
            return !password.contains(" ");
        } else {
            return password == null || password.isBlank();
        }
    }
}
