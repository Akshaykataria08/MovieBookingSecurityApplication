package com.moviebooking.webapp.requestdto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserChangePasswordRequest {

	private String prevPassword;
	private String proposedPassword;
}
