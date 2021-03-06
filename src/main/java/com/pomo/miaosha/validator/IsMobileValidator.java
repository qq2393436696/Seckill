package com.pomo.miaosha.validator;

import com.pomo.miaosha.util.ValidatorUtil;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IsMobileValidator implements ConstraintValidator<IsMobile, String> {

	private boolean required = false;

	/*
	 * 初始化
	 * */
	@Override
	public void initialize(IsMobile constraintAnnotation) {
		required = constraintAnnotation.require();
	}


	/*
	 * 验证
	 * */
	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (required) {
			return ValidatorUtil.isMobile(value);
		} else {
			if (StringUtils.isEmpty(value)) {
				return true;
			} else {
				return ValidatorUtil.isMobile(value);
			}
		}
	}
}
