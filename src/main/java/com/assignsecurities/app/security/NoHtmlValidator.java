package com.assignsecurities.app.security;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.PolicyFactory;

public class NoHtmlValidator implements ConstraintValidator<NoHtml, String> {

	// http://owasp-java-html-sanitizer.googlecode.com/svn/trunk/distrib/javadoc/org/owasp/html/HtmlPolicyBuilder.html
	// builder is not thread safe, so make local
//   private static final PolicyFactory DISALLOW_ALL = new HtmlPolicyBuilder().toFactory();
	private static final PolicyFactory DISALLOW_ALL = ASHtmlPolicy.POLICY_DEFINITION;

	private static final PolicyFactory PLAIN_TEXT_SANITIZER_POLICY = new HtmlPolicyBuilder().toFactory();

	@Override
	public void initialize(NoHtml constraintAnnotation) {
		// TODO specify the policy as an annotation attribute
		// to use them, values from annotation are stored in private properties here
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		value = value.replace("@", "&#64;");
		String sanitized = DISALLOW_ALL.sanitize(value);
		return sanitized.equals(value);
	}

	public static String toString(Object stringValue) {
		if (stringValue != null && stringValue.getClass() == String.class) {
			return PLAIN_TEXT_SANITIZER_POLICY.sanitize((String) stringValue).replace("&#64;", "@");
		} else {
			return null;
		}
	}
}