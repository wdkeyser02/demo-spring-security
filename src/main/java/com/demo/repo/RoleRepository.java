package com.demo.repo;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.demo.entity.Role;

/**
 *
 * @author Demo User
 * @date   14-Apr-2023
 *
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

	/**
	 * Get role by name ignore case if exist
	 *
	 * @param  name
	 * @return
	 */
	Optional<Role> findByNameIgnoreCase(String name);

	/**
	 * Get role by name ignore case and id not if exist
	 *
	 * @param  roleName
	 * @param  id
	 * @return
	 */
	Optional<Role> findByNameIgnoreCaseAndIdNot(String name, Long id);

	/**
	 * Get page of role by active
	 *
	 * @param  activeRecords
	 * @param  pageable
	 * @return
	 */
	Page<Role> findAllByActive(Boolean activeRecords, Pageable pageable);

	/**
	 * Get page of role by active and isDefault
	 *
	 * @param  activeRecords
	 * @param  isDefault
	 * @param  pageable
	 * @return
	 */
	Page<Role> findAllByActiveAndIsDefault(Boolean activeRecords, Boolean isDefault, Pageable pageable);

	/**
	 * Get page of role by isDefault
	 *
	 * @param  isDefault
	 * @param  pageable
	 * @return
	 */
	Page<Role> findAllByIsDefault(Boolean isDefault, Pageable pageable);

	/**
	 * Get page of role by userType
	 *
	 * @param  userType
	 * @param  pageable
	 * @return
	 */
	Page<Role> findAllByUserType(String userType, Pageable pageable);

	/**
	 * Get page of role by activeRecords and userType
	 *
	 * @param  activeRecords
	 * @param  userType
	 * @param  pageable
	 * @return
	 */
	Page<Role> findAllByActiveAndUserType(Boolean activeRecords, String userType, Pageable pageable);

	/**
	 * Get page of role by activeRecords, isDefault and userType
	 *
	 * @param  activeRecords
	 * @param  isDefault
	 * @param  userType
	 * @param  pageable
	 * @return
	 */
	Page<Role> findAllByActiveAndIsDefaultAndUserType(Boolean activeRecords, Boolean isDefault, String userType, Pageable pageable);

	/**
	 * Get page of role by isDefault and userType
	 *
	 * @param  isDefault
	 * @param  userType
	 * @param  pageable
	 * @return
	 */
	Page<Role> findAllByIsDefaultAndUserType(Boolean isDefault, String userType, Pageable pageable);

}
