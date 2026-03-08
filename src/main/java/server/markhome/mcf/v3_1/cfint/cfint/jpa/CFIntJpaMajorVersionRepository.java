// Description: Java 25 Spring JPA Repository for MajorVersion

/*
 *	server.markhome.mcf.CFInt
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

package server.markhome.mcf.v3_1.cfint.cfint.jpa;

import java.io.Serializable;
import java.math.*;
import java.time.*;
import java.util.*;
import jakarta.persistence.*;
import server.markhome.mcf.v3_1.cflib.*;
import server.markhome.mcf.v3_1.cflib.dbutil.*;
import server.markhome.mcf.v3_1.cflib.xml.CFLibXmlUtil;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;
import server.markhome.mcf.v3_1.cfsec.cfsec.*;
import server.markhome.mcf.v3_1.cfint.cfint.*;

/**
 *	JpaRepository for the CFIntJpaMajorVersion entities defined in server.markhome.mcf.v3_1.cfint.cfint.jpa.
 *	The manufactured repositories try to provide a rich, do-it-all interface to the JPA data store, with both object and argument-based implementations of the interface defined.
 */
@Transactional(readOnly = true)
public interface CFIntJpaMajorVersionRepository extends JpaRepository<CFIntJpaMajorVersion, CFLibDbKeyHash256> {

	/**
	 *	Argument-based get database instance for compatibility with the current MSS code factory code base.
	 *
	 *		@param requiredId
	 *
	 *		@return The retrieved entity, usually from the JPA cache, or null if no such entity exists.
	 */
	@Query("select r from CFIntJpaMajorVersion r where r.requiredId = :id")
	CFIntJpaMajorVersion get(@Param("id") CFLibDbKeyHash256 requiredId);

	// CFIntJpaMajorVersion specified index readers

	/**
	 *	Read zero or more entities into a List using the columns of the CFIntMajorVersionByTenantIdxKey as arguments.
	 *
	 *		@param requiredTenantId
	 *
	 *		@return List&lt;CFIntJpaMajorVersion&gt; of the found entities, typically from the JPA cache, or an empty list if no such entities exist.
	 */
	@Query("select r from CFIntJpaMajorVersion r where r.requiredTenantId = :tenantId")
	List<CFIntJpaMajorVersion> findByTenantIdx(@Param("tenantId") CFLibDbKeyHash256 requiredTenantId);

	/**
	 *	CFIntMajorVersionByTenantIdxKey entity list reader convenience method for object-based access.
	 *
	 *		@param key The CFIntMajorVersionByTenantIdxKey instance to use for the query arguments.
	 *
	 *		@return The found entity list, which may be empty, typically populated from the JPA cache.
	 */
	default List<CFIntJpaMajorVersion> findByTenantIdx(ICFIntMajorVersionByTenantIdxKey key) {
		return( findByTenantIdx(key.getRequiredTenantId()));
	}

	/**
	 *	Read zero or more entities into a List using the columns of the CFIntMajorVersionBySubProjectIdxKey as arguments.
	 *
	 *		@param requiredSubProjectId
	 *
	 *		@return List&lt;CFIntJpaMajorVersion&gt; of the found entities, typically from the JPA cache, or an empty list if no such entities exist.
	 */
	@Query("select r from CFIntJpaMajorVersion r where r.requiredContainerParentSPrj.requiredId = :subProjectId")
	List<CFIntJpaMajorVersion> findBySubProjectIdx(@Param("subProjectId") CFLibDbKeyHash256 requiredSubProjectId);

	/**
	 *	CFIntMajorVersionBySubProjectIdxKey entity list reader convenience method for object-based access.
	 *
	 *		@param key The CFIntMajorVersionBySubProjectIdxKey instance to use for the query arguments.
	 *
	 *		@return The found entity list, which may be empty, typically populated from the JPA cache.
	 */
	default List<CFIntJpaMajorVersion> findBySubProjectIdx(ICFIntMajorVersionBySubProjectIdxKey key) {
		return( findBySubProjectIdx(key.getRequiredSubProjectId()));
	}

	/**
	 *	Read an entity using the columns of the CFIntMajorVersionByNameIdxKey as arguments.
	 *
	 *		@param requiredSubProjectId
	 *		@param requiredName
	 *
	 *		@return The found entity, typically from the JPA cache, or null if no such entity exists.
	 */
	@Query("select r from CFIntJpaMajorVersion r where r.requiredContainerParentSPrj.requiredId = :subProjectId and r.requiredName = :name")
	CFIntJpaMajorVersion findByNameIdx(@Param("subProjectId") CFLibDbKeyHash256 requiredSubProjectId,
		@Param("name") String requiredName);

	/**
	 *	CFIntMajorVersionByNameIdxKey entity reader convenience method for object-based access.
	 *
	 *		@param key The CFIntMajorVersionByNameIdxKey instance to use for the query arguments.
	 *
	 *		@return The found entity, typically from the JPA cache, or null if no such entity exists.
	 */
	default CFIntJpaMajorVersion findByNameIdx(ICFIntMajorVersionByNameIdxKey key) {
		return( findByNameIdx(key.getRequiredSubProjectId(), key.getRequiredName()));
	}

	// CFIntJpaMajorVersion specified delete-by-index methods

	/**
	 *	Argument-based lock database entity for compatibility with the current MSS code factory code base, uses @Transactional to acquire a JPA entity locks, which may or may not imply an actual database lock during the transaction.
	 *
	 *		@param requiredId
	 *
	 *		@return The locked entity, refreshed from the data store, or null if no such entity exists.
	 */
	@Transactional
	@Lock(LockModeType.WRITE)
	@Query("select r from CFIntJpaMajorVersion r where r.requiredId = :id")
	CFIntJpaMajorVersion lockByIdIdx(@Param("id") CFLibDbKeyHash256 requiredId);

	/**
	 *	Argument-based lock database instance for compatibility with the current MSS code factory code base, uses @Transactional to acquire a JPA entity locks, which may or may not imply an actual database lock during the transaction.
	 *
	 *		@param requiredTenantId
	 *
	 *		@return A list of locked entities, refreshed from the data store, or an empty list if no such entities exist.
	 */
	@Transactional
	@Lock(LockModeType.WRITE)
	@Query("select r from CFIntJpaMajorVersion r where r.requiredTenantId = :tenantId")
	List<CFIntJpaMajorVersion> lockByTenantIdx(@Param("tenantId") CFLibDbKeyHash256 requiredTenantId);

	/**
	 *	CFIntMajorVersionByTenantIdxKey based lock method for object-based access.
	 *
	 *		@param key The key of the entity to be locked.
	 *
	 *		@return A list of locked entities, refreshed from the data store, or an empty list if no such entities exist.
	 */
	default List<CFIntJpaMajorVersion> lockByTenantIdx(ICFIntMajorVersionByTenantIdxKey key) {
		return( lockByTenantIdx(key.getRequiredTenantId()));
	}

	/**
	 *	Argument-based lock database instance for compatibility with the current MSS code factory code base, uses @Transactional to acquire a JPA entity locks, which may or may not imply an actual database lock during the transaction.
	 *
	 *		@param requiredSubProjectId
	 *
	 *		@return A list of locked entities, refreshed from the data store, or an empty list if no such entities exist.
	 */
	@Transactional
	@Lock(LockModeType.WRITE)
	@Query("select r from CFIntJpaMajorVersion r where r.requiredContainerParentSPrj.requiredId = :subProjectId")
	List<CFIntJpaMajorVersion> lockBySubProjectIdx(@Param("subProjectId") CFLibDbKeyHash256 requiredSubProjectId);

	/**
	 *	CFIntMajorVersionBySubProjectIdxKey based lock method for object-based access.
	 *
	 *		@param key The key of the entity to be locked.
	 *
	 *		@return A list of locked entities, refreshed from the data store, or an empty list if no such entities exist.
	 */
	default List<CFIntJpaMajorVersion> lockBySubProjectIdx(ICFIntMajorVersionBySubProjectIdxKey key) {
		return( lockBySubProjectIdx(key.getRequiredSubProjectId()));
	}

	/**
	 *	Argument-based lock database entity for compatibility with the current MSS code factory code base, uses @Transactional to acquire a JPA entity locks, which may or may not imply an actual database lock during the transaction.
	 *
	 *		@param requiredSubProjectId
	 *		@param requiredName
	 *
	 *		@return The locked entity, refreshed from the data store, or null if no such entity exists.
	 */
	@Transactional
	@Lock(LockModeType.WRITE)
	@Query("select r from CFIntJpaMajorVersion r where r.requiredContainerParentSPrj.requiredId = :subProjectId and r.requiredName = :name")
	CFIntJpaMajorVersion lockByNameIdx(@Param("subProjectId") CFLibDbKeyHash256 requiredSubProjectId,
		@Param("name") String requiredName);

	/**
	 *	CFIntMajorVersionByNameIdxKey based lock method for object-based access.
	 *
	 *		@param key The key of the entity to be locked.
	 *
	 *		@return The locked entity, refreshed from the data store, or null if no such entity exists.
	 */
	default CFIntJpaMajorVersion lockByNameIdx(ICFIntMajorVersionByNameIdxKey key) {
		return( lockByNameIdx(key.getRequiredSubProjectId(), key.getRequiredName()));
	}

	// CFIntJpaMajorVersion specified delete-by-index methods

	/**
	 *	Argument-based delete entity for compatibility with the current MSS code factory code base, uses @Transactional to acquire a JPA entity lock, which may or may not imply an actual database lock during the transaction.
	 *
	 *		@param requiredId
	 */
	@Transactional
	@Modifying
	@Query("delete from CFIntJpaMajorVersion r where r.requiredId = :id")
	void deleteByIdIdx(@Param("id") CFLibDbKeyHash256 requiredId);

	/**
	 *	Argument-based delete entity for compatibility with the current MSS code factory code base, uses @Transactional to acquire a JPA entity lock, which may or may not imply an actual database lock during the transaction.
	 *
	 *		@param requiredTenantId
	 */
	@Transactional
	@Modifying
	@Query("delete from CFIntJpaMajorVersion r where r.requiredTenantId = :tenantId")
	void deleteByTenantIdx(@Param("tenantId") CFLibDbKeyHash256 requiredTenantId);

	/**
	 *	CFIntMajorVersionByTenantIdxKey based lock method for object-based access.
	 *
	 *		@param key The CFIntMajorVersionByTenantIdxKey of the entity to be locked.
	 */
	default void deleteByTenantIdx(ICFIntMajorVersionByTenantIdxKey key) {
		deleteByTenantIdx(key.getRequiredTenantId());
	}

	/**
	 *	Argument-based delete entity for compatibility with the current MSS code factory code base, uses @Transactional to acquire a JPA entity lock, which may or may not imply an actual database lock during the transaction.
	 *
	 *		@param requiredSubProjectId
	 */
	@Transactional
	@Modifying
	@Query("delete from CFIntJpaMajorVersion r where r.requiredContainerParentSPrj.requiredId = :subProjectId")
	void deleteBySubProjectIdx(@Param("subProjectId") CFLibDbKeyHash256 requiredSubProjectId);

	/**
	 *	CFIntMajorVersionBySubProjectIdxKey based lock method for object-based access.
	 *
	 *		@param key The CFIntMajorVersionBySubProjectIdxKey of the entity to be locked.
	 */
	default void deleteBySubProjectIdx(ICFIntMajorVersionBySubProjectIdxKey key) {
		deleteBySubProjectIdx(key.getRequiredSubProjectId());
	}

	/**
	 *	Argument-based delete entity for compatibility with the current MSS code factory code base, uses @Transactional to acquire a JPA entity lock, which may or may not imply an actual database lock during the transaction.
	 *
	 *		@param requiredSubProjectId
	 *		@param requiredName
	 */
	@Transactional
	@Modifying
	@Query("delete from CFIntJpaMajorVersion r where r.requiredContainerParentSPrj.requiredId = :subProjectId and r.requiredName = :name")
	void deleteByNameIdx(@Param("subProjectId") CFLibDbKeyHash256 requiredSubProjectId,
		@Param("name") String requiredName);

	/**
	 *	CFIntMajorVersionByNameIdxKey based lock method for object-based access.
	 *
	 *		@param key The CFIntMajorVersionByNameIdxKey of the entity to be locked.
	 */
	default void deleteByNameIdx(ICFIntMajorVersionByNameIdxKey key) {
		deleteByNameIdx(key.getRequiredSubProjectId(), key.getRequiredName());
	}

}
