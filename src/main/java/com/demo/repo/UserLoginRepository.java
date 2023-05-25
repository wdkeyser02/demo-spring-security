package com.demo.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.demo.entity.Role;
import com.demo.entity.UserLogin;

/**
 *
 * @author Demo User
 * @date   14-Apr-2023
 *
 */
@Repository
public interface UserLoginRepository extends JpaRepository<UserLogin, Long> {

	/**
	 * @param  contactNumber
	 * @param  entityType
	 * @return
	 */
	Optional<UserLogin> findByContactNumberAndEntityType(String contactNumber, String entityType);

	/**
	 * Get user login based on email, contact number and entityType
	 *
	 * @param  email
	 * @param  contactNumber
	 * @param  entityType
	 * @return
	 */
	Optional<UserLogin> findByEmailAndContactNumberAndEntityType(String email, String contactNumber, String entityType);

	/**
	 * @param entityId
	 * @param entityType
	 */
	Optional<UserLogin> findByEntityIdAndEntityType(Long entityId, String entityType);

	/**
	 * @param  email
	 * @param  upperCase
	 * @return
	 */
	Optional<UserLogin> findByEmailAndEntityType(String email, String entityType);

	/**
	 * @param  email
	 * @param  name
	 * @return
	 */
	Optional<UserLogin> findByEmailAndEntityTypeIsNull(String email);

	/**
	 * get admin panel user's login detail based entity type(null or admin panel user's user Type) and contactNumber
	 *
	 * @param  contactNuber
	 * @param  adminPanelUsers
	 * @return
	 */
	@Query("select u from UserLogin u where ( u.entityType IS NULL or u.entityType in :adminPanelUsers ) and u.contactNumber=:contactNumber")
	Optional<UserLogin> getAdminPanelUserBasedOnContactNumberAndEntityType(String contactNumber, List<String> adminPanelUsers);

	/**
	 * get admin panel user's login detail based entity type(null or admin panel user's user Type) and email
	 *
	 * @param  email
	 * @param  adminPanelUsers
	 * @return
	 */
	@Query("select u from UserLogin u where ( u.entityType IS NULL or u.entityType in :adminPanelUsers ) and u.email=:email")
	Optional<UserLogin> getAdminPanelUserBasedOnEmailAndEntityType(String email, List<String> adminPanelUsers);

	/**
	 * Get user list based on role if exist
	 *
	 * @param  actualUser
	 * @param  role
	 * @return
	 */
	Optional<List<UserLogin>> findAllByRole(Role role);

	/**
	 * get User login based on contact number and role
	 *
	 * @param  contactNumber
	 * @param  role
	 * @return
	 */
	Optional<UserLogin> findByContactNumberAndRole(String contactNumber, Role role);

	/**
	 * get User login based on email and role
	 *
	 * @param  email
	 * @param  name
	 * @return
	 */
	Optional<UserLogin> findByEmailAndRole(String email, Role role);

	/**
	 * Get user login based on email, entity type and entityId not : used for update user validation.
	 *
	 * @param  email
	 * @param  entityType
	 * @param  entityId
	 * @return
	 */
	Optional<UserLogin> findByEmailAndEntityTypeAndEntityIdNot(String email, String entityType, Long entityId);

	/**
	 * Get user login based on contactNumber, entity type and entityId not : used for update user validation.
	 *
	 * @param  contactNumber
	 * @param  entityType
	 * @param  entityId
	 * @return
	 */
	Optional<UserLogin> findByContactNumberAndEntityTypeAndEntityIdNot(String contactNumber, String entityType, Long entityId);

	/**
	 * Get user login based on admin user login based on contactNumber
	 *
	 * @param  contactNumber
	 * @return
	 */
	Optional<UserLogin> findByContactNumberAndEntityTypeIsNull(String contactNumber);

	/**
	 * Get user login based on entityTypeList and email
	 *
	 * @param  email
	 * @param  entityTypeList
	 * @return
	 */
	@Query("select u from UserLogin u where u.entityType in :entityTypeList and u.email=:email")
	Optional<UserLogin> findByEmailAndEntityTypeList(String email, List<String> entityTypeList);

	/**
	 * Get user login based on entityTypeList and contactNumber
	 *
	 * @param  contactNumber
	 * @param  entityTypeList
	 * @return
	 */
	@Query("select u from UserLogin u where u.entityType in :entityTypeList and u.contactNumber=:contactNumber")
	Optional<UserLogin> findByContactNumberAndEntityTypeList(String contactNumber, List<String> entityTypeList);

	/**
	 * Get user login based on apple unique key
	 *
	 * @param uniqueId
	 */
	Optional<UserLogin> findByAppleUniqueKey(String appleUniqueKey);

	/**
	 *
	 * @param  role
	 * @param  active
	 * @return
	 */
	Optional<List<UserLogin>> findAllByRoleAndActive(Role role, boolean active);

	/**
	 *
	 * @param  role
	 * @param  active
	 * @return
	 */
	List<UserLogin> findAllByEntityTypeAndActive(String entityType, boolean active);

	/**
	 *
	 * @param  entityIds
	 * @param  entityType
	 * @param  active
	 * @return
	 */
	@Query("select u from UserLogin u where u.entityId in :entityIds and u.entityType=:entityType and u.active=:active")
	List<UserLogin> findAllByEntityIdsAndEntityTypeAndActive(List<Long> entityIds, String entityType, boolean active);

}
