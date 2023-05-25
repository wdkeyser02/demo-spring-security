package com.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *
 * @author Demo User
 * @date   14-Apr-2023
 *
 */
@Table(name = "role")
@Entity
@Data
@EqualsAndHashCode(callSuper = false)
public class Role extends CommonModel {

	/**
	 *
	 */
	private static final long serialVersionUID = -7915137780231594263L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(name = "name")
	private String name;
	@Column(name = "user_type")
	private String userType;
	@Column(name = "description")
	private String description;

	@Column(name = "is_default", columnDefinition = "boolean default false")
	private Boolean isDefault;

}
