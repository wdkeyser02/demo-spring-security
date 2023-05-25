
package com.demo.entity;

import java.io.Serializable;
import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

/**
 *
 * @author Demo User
 * @date   14-Apr-2023
 *
 */
@MappedSuperclass
@Data
@EntityListeners(AuditingEntityListener.class)
public class CommonModel implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 7869831537646377462L;
	@Column(name = "active", nullable = false)
	private Boolean active;

	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_at", updatable = false, nullable = false)
	private Date createdAt;

	@Temporal(TemporalType.TIMESTAMP)
	@UpdateTimestamp
	@Column(name = "updated_at", nullable = false)
	private Date updatedAt;

	@CreatedBy
	@Column(name = "created_by", updatable = false, nullable = false)
	private Long createdBy;

	@LastModifiedBy
	@Column(name = "updated_by", nullable = false)
	private Long updatedBy;

}
