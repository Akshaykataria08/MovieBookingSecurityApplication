package com.moviebooking.webapp.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserForgotPasswordRequest {

	private String email;
}
