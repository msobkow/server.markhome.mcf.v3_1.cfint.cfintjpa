// Description: Java 25 Spring JPA Repository for SubProject

/*
 *	io.github.msobkow.CFInt
 *
 *	Copyright (c) 2016-2026 Mark Stephen Sobkow
 *	
 *	Mark's Code Fractal 3.1 CFInt - Internet Essentials
 *	
 *	This file is part of Mark's Code Fractal CFInt.
 *	
 *	Mark's Code Fractal CFInt is available under dual commercial license from
 *	Mark Stephen Sobkow, or under the terms of the GNU Library General Public License,
 *	Version 3 or later.
 *	
 *	Mark's Code Fractal CFInt is free software: you can redistribute it and/or
 *	modify it under the terms of the GNU Library General Public License as published by
 *	the Free Software Foundation, either version 3 of the License, or
 *	(at your option) any later version.
 *	
 *	Mark's Code Fractal CFInt is distributed in the hope that it will be useful,
 *	but WITHOUT ANY WARRANTY; without even the implied warranty of
 *	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *	GNU General Public License for more details.
 *	
 *	You should have received a copy of the GNU Library General Public License
 *	along with Mark's Code Fractal CFInt.  If not, see <https://www.gnu.org/licenses/>.
 *	
 *	If you wish to modify and use this code without publishing your changes in order to
 *	tie it to proprietary code, please contact Mark Stephen Sobkow
 *	for a commercial license at mark.sobkow@gmail.com
 *	
 */

package io.github.msobkow.v3_1.cfint.cfint.jpa;

import java.io.Serializable;
import java.math.*;
import java.time.*;
import java.util.*;
import jakarta.persistence.*;
import io.github.msobkow.v3_1.cflib.*;
import io.github.msobkow.v3_1.cflib.dbutil.*;
import io.github.msobkow.v3_1.cflib.xml.CFLibXmlUtil;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;
import io.github.msobkow.v3_1.cfsec.cfsec.*;
import io.github.msobkow.v3_1.cfint.cfint.*;

/**
 *	JpaRepository for the CFIntJpaSubProject entities defined in io.github.msobkow.v3_1.cfint.cfint.jpa.
 *	The manufactured repositories try to provide a rich, do-it-all interface to the JPA data store, with both object and argument-based implementations of the interface defined.
 */
@Transactional(readOnly = true)
public interface CFIntJpaSubProjectRepository extends JpaRepository<CFIntJpaSubProject, CFLibDbKeyHash256> {

	/**
	 *	Argument-based get database instance for compatibility with the current MSS code factory code base.
	 *
	 *		@param requiredId
	 *
	 *		@return The retrieved entity, usually from the JPA cache, or null if no such entity exists.
	 */
	@Query("select r from CFIntJpaSubProject r where r.requiredId = :id")
	CFIntJpaSubProject get(@Param("id") CFLibDbKeyHash256 requiredId);

	// CFIntJpaSubProject specified index readers

	/**
	 *	Read zero or more entities into a List using the columns of the CFIntSubProjectByTenantIdxKey as arguments.
	 *
	 *		@param requiredTenantId
	 *
	 *		@return List&lt;CFIntJpaSubProject&gt; of the found entities, typically from the JPA cache, or an empty list if no such entities exist.
	 */
	@Query("select r from CFIntJpaSubProject r where r.requiredTenantId = :tenantId")
	List<CFIntJpaSubProject> findByTenantIdx(@Param("tenantId") CFLibDbKeyHash256 requiredTenantId);

	/**
	 *	CFIntSubProjectByTenantIdxKey entity list reader convenience method for object-based access.
	 *
	 *		@param key The CFIntSubProjectByTenantIdxKey instance to use for the query arguments.
	 *
	 *		@return The found entity list, which may be empty, typically populated from the JPA cache.
	 */
	default List<CFIntJpaSubProject> findByTenantIdx(ICFIntSubProjectByTenantIdxKey key) {
		return( findByTenantIdx(key.getRequiredTenantId()));
	}

	/**
	 *	Read zero or more entities into a List using the columns of the CFIntSubProjectByTopProjectIdxKey as arguments.
	 *
	 *		@param requiredTopProjectId
	 *
	 *		@return List&lt;CFIntJpaSubProject&gt; of the found entities, typically from the JPA cache, or an empty list if no such entities exist.
	 */
	@Query("select r from CFIntJpaSubProject r where r.requiredContainerParentTPrj.requiredId = :topProjectId")
	List<CFIntJpaSubProject> findByTopProjectIdx(@Param("topProjectId") CFLibDbKeyHash256 requiredTopProjectId);

	/**
	 *	CFIntSubProjectByTopProjectIdxKey entity list reader convenience method for object-based access.
	 *
	 *		@param key The CFIntSubProjectByTopProjectIdxKey instance to use for the query arguments.
	 *
	 *		@return The found entity list, which may be empty, typically populated from the JPA cache.
	 */
	default List<CFIntJpaSubProject> findByTopProjectIdx(ICFIntSubProjectByTopProjectIdxKey key) {
		return( findByTopProjectIdx(key.getRequiredTopProjectId()));
	}

	/**
	 *	Read an entity using the columns of the CFIntSubProjectByNameIdxKey as arguments.
	 *
	 *		@param requiredTopProjectId
	 *		@param requiredName
	 *
	 *		@return The found entity, typically from the JPA cache, or null if no such entity exists.
	 */
	@Query("select r from CFIntJpaSubProject r where r.requiredContainerParentTPrj.requiredId = :topProjectId and r.requiredName = :name")
	CFIntJpaSubProject findByNameIdx(@Param("topProjectId") CFLibDbKeyHash256 requiredTopProjectId,
		@Param("name") String requiredName);

	/**
	 *	CFIntSubProjectByNameIdxKey entity reader convenience method for object-based access.
	 *
	 *		@param key The CFIntSubProjectByNameIdxKey instance to use for the query arguments.
	 *
	 *		@return The found entity, typically from the JPA cache, or null if no such entity exists.
	 */
	default CFIntJpaSubProject findByNameIdx(ICFIntSubProjectByNameIdxKey key) {
		return( findByNameIdx(key.getRequiredTopProjectId(), key.getRequiredName()));
	}

	// CFIntJpaSubProject specified delete-by-index methods

	/**
	 *	Argument-based lock database entity for compatibility with the current MSS code factory code base, uses @Transactional to acquire a JPA entity locks, which may or may not imply an actual database lock during the transaction.
	 *
	 *		@param requiredId
	 *
	 *		@return The locked entity, refreshed from the data store, or null if no such entity exists.
	 */
	@Transactional
	@Lock(LockModeType.WRITE)
	@Query("select r from CFIntJpaSubProject r where r.requiredId = :id")
	CFIntJpaSubProject lockByIdIdx(@Param("id") CFLibDbKeyHash256 requiredId);

	/**
	 *	Argument-based lock database instance for compatibility with the current MSS code factory code base, uses @Transactional to acquire a JPA entity locks, which may or may not imply an actual database lock during the transaction.
	 *
	 *		@param requiredTenantId
	 *
	 *		@return A list of locked entities, refreshed from the data store, or an empty list if no such entities exist.
	 */
	@Transactional
	@Lock(LockModeType.WRITE)
	@Query("select r from CFIntJpaSubProject r where r.requiredTenantId = :tenantId")
	List<CFIntJpaSubProject> lockByTenantIdx(@Param("tenantId") CFLibDbKeyHash256 requiredTenantId);

	/**
	 *	CFIntSubProjectByTenantIdxKey based lock method for object-based access.
	 *
	 *		@param key The key of the entity to be locked.
	 *
	 *		@return A list of locked entities, refreshed from the data store, or an empty list if no such entities exist.
	 */
	default List<CFIntJpaSubProject> lockByTenantIdx(ICFIntSubProjectByTenantIdxKey key) {
		return( lockByTenantIdx(key.getRequiredTenantId()));
	}

	/**
	 *	Argument-based lock database instance for compatibility with the current MSS code factory code base, uses @Transactional to acquire a JPA entity locks, which may or may not imply an actual database lock during the transaction.
	 *
	 *		@param requiredTopProjectId
	 *
	 *		@return A list of locked entities, refreshed from the data store, or an empty list if no such entities exist.
	 */
	@Transactional
	@Lock(LockModeType.WRITE)
	@Query("select r from CFIntJpaSubProject r where r.requiredContainerParentTPrj.requiredId = :topProjectId")
	List<CFIntJpaSubProject> lockByTopProjectIdx(@Param("topProjectId") CFLibDbKeyHash256 requiredTopProjectId);

	/**
	 *	CFIntSubProjectByTopProjectIdxKey based lock method for object-based access.
	 *
	 *		@param key The key of the entity to be locked.
	 *
	 *		@return A list of locked entities, refreshed from the data store, or an empty list if no such entities exist.
	 */
	default List<CFIntJpaSubProject> lockByTopProjectIdx(ICFIntSubProjectByTopProjectIdxKey key) {
		return( lockByTopProjectIdx(key.getRequiredTopProjectId()));
	}

	/**
	 *	Argument-based lock database entity for compatibility with the current MSS code factory code base, uses @Transactional to acquire a JPA entity locks, which may or may not imply an actual database lock during the transaction.
	 *
	 *		@param requiredTopProjectId
	 *		@param requiredName
	 *
	 *		@return The locked entity, refreshed from the data store, or null if no such entity exists.
	 */
	@Transactional
	@Lock(LockModeType.WRITE)
	@Query("select r from CFIntJpaSubProject r where r.requiredContainerParentTPrj.requiredId = :topProjectId and r.requiredName = :name")
	CFIntJpaSubProject lockByNameIdx(@Param("topProjectId") CFLibDbKeyHash256 requiredTopProjectId,
		@Param("name") String requiredName);

	/**
	 *	CFIntSubProjectByNameIdxKey based lock method for object-based access.
	 *
	 *		@param key The key of the entity to be locked.
	 *
	 *		@return The locked entity, refreshed from the data store, or null if no such entity exists.
	 */
	default CFIntJpaSubProject lockByNameIdx(ICFIntSubProjectByNameIdxKey key) {
		return( lockByNameIdx(key.getRequiredTopProjectId(), key.getRequiredName()));
	}

	// CFIntJpaSubProject specified delete-by-index methods

	/**
	 *	Argument-based delete entity for compatibility with the current MSS code factory code base, uses @Transactional to acquire a JPA entity lock, which may or may not imply an actual database lock during the transaction.
	 *
	 *		@param requiredId
	 */
	@Transactional
	@Modifying
	@Query("delete from CFIntJpaSubProject r where r.requiredId = :id")
	void deleteByIdIdx(@Param("id") CFLibDbKeyHash256 requiredId);

	/**
	 *	Argument-based delete entity for compatibility with the current MSS code factory code base, uses @Transactional to acquire a JPA entity lock, which may or may not imply an actual database lock during the transaction.
	 *
	 *		@param requiredTenantId
	 */
	@Transactional
	@Modifying
	@Query("delete from CFIntJpaSubProject r where r.requiredTenantId = :tenantId")
	void deleteByTenantIdx(@Param("tenantId") CFLibDbKeyHash256 requiredTenantId);

	/**
	 *	CFIntSubProjectByTenantIdxKey based lock method for object-based access.
	 *
	 *		@param key The CFIntSubProjectByTenantIdxKey of the entity to be locked.
	 */
	default void deleteByTenantIdx(ICFIntSubProjectByTenantIdxKey key) {
		deleteByTenantIdx(key.getRequiredTenantId());
	}

	/**
	 *	Argument-based delete entity for compatibility with the current MSS code factory code base, uses @Transactional to acquire a JPA entity lock, which may or may not imply an actual database lock during the transaction.
	 *
	 *		@param requiredTopProjectId
	 */
	@Transactional
	@Modifying
	@Query("delete from CFIntJpaSubProject r where r.requiredContainerParentTPrj.requiredId = :topProjectId")
	void deleteByTopProjectIdx(@Param("topProjectId") CFLibDbKeyHash256 requiredTopProjectId);

	/**
	 *	CFIntSubProjectByTopProjectIdxKey based lock method for object-based access.
	 *
	 *		@param key The CFIntSubProjectByTopProjectIdxKey of the entity to be locked.
	 */
	default void deleteByTopProjectIdx(ICFIntSubProjectByTopProjectIdxKey key) {
		deleteByTopProjectIdx(key.getRequiredTopProjectId());
	}

	/**
	 *	Argument-based delete entity for compatibility with the current MSS code factory code base, uses @Transactional to acquire a JPA entity lock, which may or may not imply an actual database lock during the transaction.
	 *
	 *		@param requiredTopProjectId
	 *		@param requiredName
	 */
	@Transactional
	@Modifying
	@Query("delete from CFIntJpaSubProject r where r.requiredContainerParentTPrj.requiredId = :topProjectId and r.requiredName = :name")
	void deleteByNameIdx(@Param("topProjectId") CFLibDbKeyHash256 requiredTopProjectId,
		@Param("name") String requiredName);

	/**
	 *	CFIntSubProjectByNameIdxKey based lock method for object-based access.
	 *
	 *		@param key The CFIntSubProjectByNameIdxKey of the entity to be locked.
	 */
	default void deleteByNameIdx(ICFIntSubProjectByNameIdxKey key) {
		deleteByNameIdx(key.getRequiredTopProjectId(), key.getRequiredName());
	}

}
