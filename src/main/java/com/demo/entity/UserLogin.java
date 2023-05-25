package com.demo.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *
 * @author Demo User
 * @date   14-Apr-2023
 *
 */
@Entity
@Table(name = "user_login")
@Data
@EqualsAndHashCode(callSuper = false)
public class UserLogin extends CommonModel {

	/**
	 *
	 */
	private static final long serialVersionUID = -8712594030416874969L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;

	@Column(name = "entity_id")
	private Long entityId;

	@Column(name = "entity_type")
	private String entityType;

	@Column(name = "email", nullable = false)
	private String email;

	@Column(name = "contact_number")
	private String contactNumber;

	@Column(name = "password")
	private String password;

	@JoinColumn(name = "role_id", nullable = false, columnDefinition = "BIGINT default 1")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Role role;

	@Column(name = "facebook_key")
	private String facebookKey;

	@Column(name = "google_key")
	private String googleKey;

	@Column(name = "apple_key")
	private String appleKey;

	@Column(name = "otp")
	private String otp;

	@Column(name = "facebook_unique_key")
	private String facebookUniqueKey;

	@Column(name = "google_unique_key")
	private String googleUniqueKey;

	@Column(name = "apple_unique_key")
	private String appleUniqueKey;

}
