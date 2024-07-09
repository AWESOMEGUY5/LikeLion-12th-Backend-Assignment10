package org.likelion.jangsu.user.api.dto.request;

import jakarta.validation.constraints.*;

public record UserUpdateReqDto(
        @NotBlank(message = "아이디를 필수로 입력하시오.")
        @Pattern(regexp = "^[a-z]$", message = "아이디 형식에 맞지 않습니다.")
        @Size(min = 5, max = 15, message = "아이디는 5자 이상 15자 미만으로 입력하시오.")
        String userId,

        @NotBlank(message = "비밀번호를 필수로 입력하시오.")
        @Pattern(regexp = "^[A-za-z0-9!@#]$", message = "비밀번호 형식에 맞지 않습니다. " +
                "대/소문자와 특수문자를 최소 하나 포함하여야 합니다.")
        @Size(min = 10, max = 30, message = "비밀번호는 10자 이상 30자 미만으로 입력하시오.")
        String password,

        @NotBlank(message = "이름을 필수로 입력하시오.")
        @Size(min = 2, max = 15, message = "이름은 2자 이상 15자 미만으로 입력하시오.")
        String name,

        @Email
        String email
) {
}