package com.moviebooking.webapp.domain;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.text.WordUtils;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.moviebooking.webapp.responsedto.Response;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@DynamoDBDocument
public class Address extends Response {

	@NotNull
	@NotEmpty
	@DynamoDBAttribute
	private String building;
	
	@NotNull
	@NotEmpty
	@DynamoDBAttribute
	private String area;
	
	@NotNull
	@NotEmpty
	@DynamoDBAttribute
	private String city;
	
	@NotNull
	@NotEmpty
	@DynamoDBAttribute
	private String state;
	
	@NotNull
	@DynamoDBAttribute
	@Digits(integer = 6, fraction = 0)
	@Size(min = 6, max = 6)
	private String pincode;

	public Address(String building, String area, String city, String state, String pincode) {
		super();
		this.setBuilding(building);
		this.setArea(area);
		this.setCity(city);
		this.setPincode(pincode);
		this.setState(state);
	}

	public void setBuilding(String building) {
		this.building = WordUtils.capitalizeFully(building);
	}

	public void setArea(String area) {
		this.area = WordUtils.capitalizeFully(area);
	}

	public void setCity(String city) {
		this.city = WordUtils.capitalizeFully(city);
	}

	public void setState(String state) {
		this.state = WordUtils.capitalizeFully(state);
	}
}