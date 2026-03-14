// Description: Java 25 Spring JPA Repository for MinorVersion

/*
 *	server.markhome.mcf.CFInt
 *
 *	Copyright (c) 2016-2026 Mark Stephen Sobkow
 *	
 *	Mark's Code Fractal 3.1 CFInt - Internet Essentials
 *	
 *	This file is part of Mark's Code Fractal CFInt.
 *	
 *	Licensed under the Apache License, Version 2.0 (the "License");
 *	you may not use this file except in compliance with the License.
 *	You may obtain a copy of the License at
 *	
 *	http://www.apache.org/licenses/LICENSE-2.0
 *	
 *	Unless required by applicable law or agreed to in writing, software
 *	distributed under the License is distributed on an "AS IS" BASIS,
 *	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *	See the License for the specific language governing permissions and
 *	limitations under the License.
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
 *	JpaRepository for the CFIntJpaMinorVersion entities defined in server.markhome.mcf.v3_1.cfint.cfint.jpa.
 *	The manufactured repositories try to provide a rich, do-it-all interface to the JPA data store, with both object and argument-based implementations of the interface defined.
 */
@Transactional(readOnly = true)
public interface CFIntJpaMinorVersionRepository extends JpaRepository<CFIntJpaMinorVersion, CFLibDbKeyHash256> {

	/**
	 *	Argument-based get database instance for compatibility with the current MSS code factory code base.
	 *
	 *		@param requiredId
	 *
	 *		@return The retrieved entity, usually from the JPA cache, or null if no such entity exists.
	 */
	@Query("select r from CFIntJpaMinorVersion r where r.requiredId = :id")
	CFIntJpaMinorVersion get(@Param("id") CFLibDbKeyHash256 requiredId);

	// CFIntJpaMinorVersion specified index readers

	/**
	 *	Read zero or more entities into a List using the columns of the CFIntMinorVersionByTenantIdxKey as arguments.
	 *
	 *		@param requiredTenantId
	 *
	 *		@return List&lt;CFIntJpaMinorVersion&gt; of the found entities, typically from the JPA cache, or an empty list if no such entities exist.
	 */
	@Query("select r from CFIntJpaMinorVersion r where r.requiredTenantId = :tenantId")
	List<CFIntJpaMinorVersion> findByTenantIdx(@Param("tenantId") CFLibDbKeyHash256 requiredTenantId);

	/**
	 *	CFIntMinorVersionByTenantIdxKey entity list reader convenience method for object-based access.
	 *
	 *		@param key The CFIntMinorVersionByTenantIdxKey instance to use for the query arguments.
	 *
	 *		@return The found entity list, which may be empty, typically populated from the JPA cache.
	 */
	default List<CFIntJpaMinorVersion> findByTenantIdx(ICFIntMinorVersionByTenantIdxKey key) {
		return( findByTenantIdx(key.getRequiredTenantId()));
	}

	/**
	 *	Read zero or more entities into a List using the columns of the CFIntMinorVersionByMajorVerIdxKey as arguments.
	 *
	 *		@param requiredMajorVersionId
	 *
	 *		@return List&lt;CFIntJpaMinorVersion&gt; of the found entities, typically from the JPA cache, or an empty list if no such entities exist.
	 */
	@Query("select r from CFIntJpaMinorVersion r where r.requiredContainerParentMajVer.requiredId = :majorVersionId")
	List<CFIntJpaMinorVersion> findByMajorVerIdx(@Param("majorVersionId") CFLibDbKeyHash256 requiredMajorVersionId);

	/**
	 *	CFIntMinorVersionByMajorVerIdxKey entity list reader convenience method for object-based access.
	 *
	 *		@param key The CFIntMinorVersionByMajorVerIdxKey instance to use for the query arguments.
	 *
	 *		@return The found entity list, which may be empty, typically populated from the JPA cache.
	 */
	default List<CFIntJpaMinorVersion> findByMajorVerIdx(ICFIntMinorVersionByMajorVerIdxKey key) {
		return( findByMajorVerIdx(key.getRequiredMajorVersionId()));
	}

	/**
	 *	Read an entity using the columns of the CFIntMinorVersionByNameIdxKey as arguments.
	 *
	 *		@param requiredMajorVersionId
	 *		@param requiredName
	 *
	 *		@return The found entity, typically from the JPA cache, or null if no such entity exists.
	 */
	@Query("select r from CFIntJpaMinorVersion r where r.requiredContainerParentMajVer.requiredId = :majorVersionId and r.requiredName = :name")
	CFIntJpaMinorVersion findByNameIdx(@Param("majorVersionId") CFLibDbKeyHash256 requiredMajorVersionId,
		@Param("name") String requiredName);

	/**
	 *	CFIntMinorVersionByNameIdxKey entity reader convenience method for object-based access.
	 *
	 *		@param key The CFIntMinorVersionByNameIdxKey instance to use for the query arguments.
	 *
	 *		@return The found entity, typically from the JPA cache, or null if no such entity exists.
	 */
	default CFIntJpaMinorVersion findByNameIdx(ICFIntMinorVersionByNameIdxKey key) {
		return( findByNameIdx(key.getRequiredMajorVersionId(), key.getRequiredName()));
	}

	// CFIntJpaMinorVersion specified delete-by-index methods

	/**
	 *	Argument-based lock database entity for compatibility with the current MSS code factory code base, uses @Transactional to acquire a JPA entity locks, which may or may not imply an actual database lock during the transaction.
	 *
	 *		@param requiredId
	 *
	 *		@return The locked entity, refreshed from the data store, or null if no such entity exists.
	 */
	@Transactional
	@Lock(LockModeType.WRITE)
	@Query("select r from CFIntJpaMinorVersion r where r.requiredId = :id")
	CFIntJpaMinorVersion lockByIdIdx(@Param("id") CFLibDbKeyHash256 requiredId);

	/**
	 *	Argument-based lock database instance for compatibility with the current MSS code factory code base, uses @Transactional to acquire a JPA entity locks, which may or may not imply an actual database lock during the transaction.
	 *
	 *		@param requiredTenantId
	 *
	 *		@return A list of locked entities, refreshed from the data store, or an empty list if no such entities exist.
	 */
	@Transactional
	@Lock(LockModeType.WRITE)
	@Query("select r from CFIntJpaMinorVersion r where r.requiredTenantId = :tenantId")
	List<CFIntJpaMinorVersion> lockByTenantIdx(@Param("tenantId") CFLibDbKeyHash256 requiredTenantId);

	/**
	 *	CFIntMinorVersionByTenantIdxKey based lock method for object-based access.
	 *
	 *		@param key The key of the entity to be locked.
	 *
	 *		@return A list of locked entities, refreshed from the data store, or an empty list if no such entities exist.
	 */
	default List<CFIntJpaMinorVersion> lockByTenantIdx(ICFIntMinorVersionByTenantIdxKey key) {
		return( lockByTenantIdx(key.getRequiredTenantId()));
	}

	/**
	 *	Argument-based lock database instance for compatibility with the current MSS code factory code base, uses @Transactional to acquire a JPA entity locks, which may or may not imply an actual database lock during the transaction.
	 *
	 *		@param requiredMajorVersionId
	 *
	 *		@return A list of locked entities, refreshed from the data store, or an empty list if no such entities exist.
	 */
	@Transactional
	@Lock(LockModeType.WRITE)
	@Query("select r from CFIntJpaMinorVersion r where r.requiredContainerParentMajVer.requiredId = :majorVersionId")
	List<CFIntJpaMinorVersion> lockByMajorVerIdx(@Param("majorVersionId") CFLibDbKeyHash256 requiredMajorVersionId);

	/**
	 *	CFIntMinorVersionByMajorVerIdxKey based lock method for object-based access.
	 *
	 *		@param key The key of the entity to be locked.
	 *
	 *		@return A list of locked entities, refreshed from the data store, or an empty list if no such entities exist.
	 */
	default List<CFIntJpaMinorVersion> lockByMajorVerIdx(ICFIntMinorVersionByMajorVerIdxKey key) {
		return( lockByMajorVerIdx(key.getRequiredMajorVersionId()));
	}

	/**
	 *	Argument-based lock database entity for compatibility with the current MSS code factory code base, uses @Transactional to acquire a JPA entity locks, which may or may not imply an actual database lock during the transaction.
	 *
	 *		@param requiredMajorVersionId
	 *		@param requiredName
	 *
	 *		@return The locked entity, refreshed from the data store, or null if no such entity exists.
	 */
	@Transactional
	@Lock(LockModeType.WRITE)
	@Query("select r from CFIntJpaMinorVersion r where r.requiredContainerParentMajVer.requiredId = :majorVersionId and r.requiredName = :name")
	CFIntJpaMinorVersion lockByNameIdx(@Param("majorVersionId") CFLibDbKeyHash256 requiredMajorVersionId,
		@Param("name") String requiredName);

	/**
	 *	CFIntMinorVersionByNameIdxKey based lock method for object-based access.
	 *
	 *		@param key The key of the entity to be locked.
	 *
	 *		@return The locked entity, refreshed from the data store, or null if no such entity exists.
	 */
	default CFIntJpaMinorVersion lockByNameIdx(ICFIntMinorVersionByNameIdxKey key) {
		return( lockByNameIdx(key.getRequiredMajorVersionId(), key.getRequiredName()));
	}

	// CFIntJpaMinorVersion specified delete-by-index methods

	/**
	 *	Argument-based delete entity for compatibility with the current MSS code factory code base, uses @Transactional to acquire a JPA entity lock, which may or may not imply an actual database lock during the transaction.
	 *
	 *		@param requiredId
	 */
	@Transactional
	@Modifying
	@Query("delete from CFIntJpaMinorVersion r where r.requiredId = :id")
	void deleteByIdIdx(@Param("id") CFLibDbKeyHash256 requiredId);

	/**
	 *	Argument-based delete entity for compatibility with the current MSS code factory code base, uses @Transactional to acquire a JPA entity lock, which may or may not imply an actual database lock during the transaction.
	 *
	 *		@param requiredTenantId
	 */
	@Transactional
	@Modifying
	@Query("delete from CFIntJpaMinorVersion r where r.requiredTenantId = :tenantId")
	void deleteByTenantIdx(@Param("tenantId") CFLibDbKeyHash256 requiredTenantId);

	/**
	 *	CFIntMinorVersionByTenantIdxKey based lock method for object-based access.
	 *
	 *		@param key The CFIntMinorVersionByTenantIdxKey of the entity to be locked.
	 */
	default void deleteByTenantIdx(ICFIntMinorVersionByTenantIdxKey key) {
		deleteByTenantIdx(key.getRequiredTenantId());
	}

	/**
	 *	Argument-based delete entity for compatibility with the current MSS code factory code base, uses @Transactional to acquire a JPA entity lock, which may or may not imply an actual database lock during the transaction.
	 *
	 *		@param requiredMajorVersionId
	 */
	@Transactional
	@Modifying
	@Query("delete from CFIntJpaMinorVersion r where r.requiredContainerParentMajVer.requiredId = :majorVersionId")
	void deleteByMajorVerIdx(@Param("majorVersionId") CFLibDbKeyHash256 requiredMajorVersionId);

	/**
	 *	CFIntMinorVersionByMajorVerIdxKey based lock method for object-based access.
	 *
	 *		@param key The CFIntMinorVersionByMajorVerIdxKey of the entity to be locked.
	 */
	default void deleteByMajorVerIdx(ICFIntMinorVersionByMajorVerIdxKey key) {
		deleteByMajorVerIdx(key.getRequiredMajorVersionId());
	}

	/**
	 *	Argument-based delete entity for compatibility with the current MSS code factory code base, uses @Transactional to acquire a JPA entity lock, which may or may not imply an actual database lock during the transaction.
	 *
	 *		@param requiredMajorVersionId
	 *		@param requiredName
	 */
	@Transactional
	@Modifying
	@Query("delete from CFIntJpaMinorVersion r where r.requiredContainerParentMajVer.requiredId = :majorVersionId and r.requiredName = :name")
	void deleteByNameIdx(@Param("majorVersionId") CFLibDbKeyHash256 requiredMajorVersionId,
		@Param("name") String requiredName);

	/**
	 *	CFIntMinorVersionByNameIdxKey based lock method for object-based access.
	 *
	 *		@param key The CFIntMinorVersionByNameIdxKey of the entity to be locked.
	 */
	default void deleteByNameIdx(ICFIntMinorVersionByNameIdxKey key) {
		deleteByNameIdx(key.getRequiredMajorVersionId(), key.getRequiredName());
	}

}
