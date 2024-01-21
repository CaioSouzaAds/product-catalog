package com.projects.productscatalog.services.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerMapping;

import com.projects.productscatalog.dto.UserUpdateDTO;
import com.projects.productscatalog.entities.User;
import com.projects.productscatalog.repositories.UserRepository;
import com.projects.productscatalog.resources.exception.FieldMessage;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UserUpdateValidator implements ConstraintValidator<UserUpdateValid, UserUpdateDTO> {

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private UserRepository repository;

	@Override
	public void initialize(UserUpdateValid ann) {
	}

	@Override
	public boolean isValid(UserUpdateDTO dto, ConstraintValidatorContext context) {

		List<FieldMessage> list = new ArrayList<>();

		// Obtendo o ID do usuário da URI da requisição
		@SuppressWarnings("unchecked")
		var uriVars = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
		if (uriVars != null && uriVars.containsKey("id")) {
			Long userId = Long.parseLong(uriVars.get("id"));

			Optional<User> userWithSameEmail = repository.findByEmail(dto.getEmail());
			if (userWithSameEmail.isPresent() && !userWithSameEmail.get().getId().equals(userId)) {
				// Se um usuário diferente com o mesmo email já existir
				list.add(new FieldMessage("email", "Email já existe"));
			}
		}

		for (FieldMessage e : list) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName())
					.addConstraintViolation();
		}
		return list.isEmpty();
	}

}