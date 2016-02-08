package com.epam.freelancer.business.util;

import com.epam.freelancer.business.util.ValidationParametersBuilder.Parameters;

public abstract class Validator {
	public static boolean validate(Parameters validationParam,
			String... strings)
	{
		if (validationParam.isNumber())
			return validateNumbers(validationParam, strings);
		else
			return validateStrings(validationParam, strings);
	}

	private static boolean validateNumbers(Parameters validationParam,
			String[] strings)
	{
		if (strings == null || strings.length == 0)
			return false;

		if (validationParam.getIsInteger() == null
				|| validationParam.getIsInteger())
			try {
				return validateInteger(validationParam, strings);
			} catch (Exception e) {
				return false;
			}
		else
			try {
				return validateDouble(validationParam, strings);
			} catch (Exception e) {
				return false;
			}
	}

	private static boolean validateDouble(Parameters validationParam,
			String[] strings)
	{
		Double[] numbers = new Double[strings.length];
		for (int i = 0; i < strings.length; i++)
			numbers[i] = Double.valueOf(strings[i]);

		if (validationParam.getMax() != null)
			for (Double integer : numbers)
				if (integer == null
						|| Double.compare(integer, validationParam.getMax()) >= 0)
					return false;
		if (validationParam.getMin() != null)
			for (Double integer : numbers)
				if (integer == null
						|| Double.compare(integer, validationParam.getMin()) < 0)
					return false;
		return true;
	}

	private static boolean validateInteger(Parameters validationParam,
			String[] strings)
	{
		Integer[] numbers = new Integer[strings.length];
		for (int i = 0; i < strings.length; i++)
			numbers[i] = Integer.valueOf(strings[i]);

		if (validationParam.getMax() != null)
			for (Integer integer : numbers)
				if (integer == null
						|| Integer.compare(integer, validationParam.getMax()
								.intValue()) >= 0)
					return false;
		if (validationParam.getMin() != null)
			for (Integer integer : numbers)
				if (integer == null
						|| Integer.compare(integer, validationParam.getMin()
								.intValue()) < 0)
					return false;
		return true;
	}

	private static boolean validateStrings(Parameters validationParam,
			String[] strings)
	{
		if (validationParam.getNotEmptyString() != null)
			if (validationParam.getNotEmptyString()) {
				for (String string : strings)
					if (string == null || string.isEmpty())
						return false;
			} else {
				boolean emptyStrings = true;
				for (String string : strings) {
					if (string != null)
						if (string.isEmpty())
							return false;
						else
							emptyStrings = false;

					if (emptyStrings)
						return true;
				}
			}

		if (validationParam.getMaxLength() != null)
			for (String string : strings)
				if (string == null
						|| validationParam.getMaxLength() < string.length())
					return false;
		if (validationParam.getMinLength() != null)
			for (String string : strings)
				if (string == null
						|| validationParam.getMinLength() > string.length())
					return false;
		if (validationParam.getPattern() != null)
			for (String string : strings)
				if (string == null
						|| !string.matches(validationParam.getPattern()))
					return false;

		return true;
	}

}
