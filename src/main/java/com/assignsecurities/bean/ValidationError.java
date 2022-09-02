package com.assignsecurities.bean;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder(value = {"message"}, alphabetic = true)
public class ValidationError
{
    private String message;
    private String inputType;
    private String inputField;
    private String inputValue;

    public static ValidationError map(ValidationError validationError)
    {
        return ValidationError.builder()
                .message(validationError.getMessage())
                .inputField(validationError.getInputField())
                .inputType(validationError.getInputType())
                .inputValue(validationError.getInputValue())
                .build();
    }
}