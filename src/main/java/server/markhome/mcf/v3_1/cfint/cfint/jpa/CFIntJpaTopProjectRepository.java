// Description: Java 25 Spring JPA Repository for TopProject

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
 *	JpaRepository for the CFIntJpaTopProject entities defined in server.markhome.mcf.v3_1.cfint.cfint.jpa.
 *	The manufactured repositories try to provide a rich, do-it-all interface to the JPA data store, with both object and argument-based implementations of the interface defined.
 */
@Transactional(readOnly = true)
public interface CFIntJpaTopProjectRepository extends JpaRepository<CFIntJpaTopProject, CFLibDbKeyHash256> {

	/**
	 *	Argument-based get database instance for compatibility with the current MSS code factory code base.
	 *
	 *		@param requiredId
	 *
	 *		@return The retrieved entity, usually from the JPA cache, or null if no such entity exists.
	 */
	@Query("select r from CFIntJpaTopProject r where r.requiredId = :id")
	CFIntJpaTopProject get(@Param("id") CFLibDbKeyHash256 requiredId);

	// CFIntJpaTopProject specified index readers

	/**
	 *	Read zero or more entities into a List using the columns of the CFIntTopProjectByTenantIdxKey as arguments.
	 *
	 *		@param requiredTenantId
	 *
	 *		@return List&lt;CFIntJpaTopProject&gt; of the found entities, typically from the JPA cache, or an empty list if no such entities exist.
	 */
	@Query("select r from CFIntJpaTopProject r where r.requiredTenantId = :tenantId")
	List<CFIntJpaTopProject> findByTenantIdx(@Param("tenantId") CFLibDbKeyHash256 requiredTenantId);

	/**
	 *	CFIntTopProjectByTenantIdxKey entity list reader convenience method for object-based access.
	 *
	 *		@param key The CFIntTopProjectByTenantIdxKey instance to use for the query arguments.
	 *
	 *		@return The found entity list, which may be empty, typically populated from the JPA cache.
	 */
	default List<CFIntJpaTopProject> findByTenantIdx(ICFIntTopProjectByTenantIdxKey key) {
		return( findByTenantIdx(key.getRequiredTenantId()));
	}

	/**
	 *	Read zero or more entities into a List using the columns of the CFIntTopProjectByTopDomainIdxKey as arguments.
	 *
	 *		@param requiredTopDomainId
	 *
	 *		@return List&lt;CFIntJpaTopProject&gt; of the found entities, typically from the JPA cache, or an empty list if no such entities exist.
	 */
	@Query("select r from CFIntJpaTopProject r where r.requiredContainerParentSDom.requiredId = :topDomainId")
	List<CFIntJpaTopProject> findByTopDomainIdx(@Param("topDomainId") CFLibDbKeyHash256 requiredTopDomainId);

	/**
	 *	CFIntTopProjectByTopDomainIdxKey entity list reader convenience method for object-based access.
	 *
	 *		@param key The CFIntTopProjectByTopDomainIdxKey instance to use for the query arguments.
	 *
	 *		@return The found entity list, which may be empty, typically populated from the JPA cache.
	 */
	default List<CFIntJpaTopProject> findByTopDomainIdx(ICFIntTopProjectByTopDomainIdxKey key) {
		return( findByTopDomainIdx(key.getRequiredTopDomainId()));
	}

	/**
	 *	Read an entity using the columns of the CFIntTopProjectByNameIdxKey as arguments.
	 *
	 *		@param requiredTopDomainId
	 *		@param requiredName
	 *
	 *		@return The found entity, typically from the JPA cache, or null if no such entity exists.
	 */
	@Query("select r from CFIntJpaTopProject r where r.requiredContainerParentSDom.requiredId = :topDomainId and r.requiredName = :name")
	CFIntJpaTopProject findByNameIdx(@Param("topDomainId") CFLibDbKeyHash256 requiredTopDomainId,
		@Param("name") String requiredName);

	/**
	 *	CFIntTopProjectByNameIdxKey entity reader convenience method for object-based access.
	 *
	 *		@param key The CFIntTopProjectByNameIdxKey instance to use for the query arguments.
	 *
	 *		@return The found entity, typically from the JPA cache, or null if no such entity exists.
	 */
	default CFIntJpaTopProject findByNameIdx(ICFIntTopProjectByNameIdxKey key) {
		return( findByNameIdx(key.getRequiredTopDomainId(), key.getRequiredName()));
	}

	// CFIntJpaTopProject specified delete-by-index methods

	/**
	 *	Argument-based lock database entity for compatibility with the current MSS code factory code base, uses @Transactional to acquire a JPA entity locks, which may or may not imply an actual database lock during the transaction.
	 *
	 *		@param requiredId
	 *
	 *		@return The locked entity, refreshed from the data store, or null if no such entity exists.
	 */
	@Transactional
	@Lock(LockModeType.WRITE)
	@Query("select r from CFIntJpaTopProject r where r.requiredId = :id")
	CFIntJpaTopProject lockByIdIdx(@Param("id") CFLibDbKeyHash256 requiredId);

	/**
	 *	Argument-based lock database instance for compatibility with the current MSS code factory code base, uses @Transactional to acquire a JPA entity locks, which may or may not imply an actual database lock during the transaction.
	 *
	 *		@param requiredTenantId
	 *
	 *		@return A list of locked entities, refreshed from the data store, or an empty list if no such entities exist.
	 */
	@Transactional
	@Lock(LockModeType.WRITE)
	@Query("select r from CFIntJpaTopProject r where r.requiredTenantId = :tenantId")
	List<CFIntJpaTopProject> lockByTenantIdx(@Param("tenantId") CFLibDbKeyHash256 requiredTenantId);

	/**
	 *	CFIntTopProjectByTenantIdxKey based lock method for object-based access.
	 *
	 *		@param key The key of the entity to be locked.
	 *
	 *		@return A list of locked entities, refreshed from the data store, or an empty list if no such entities exist.
	 */
	default List<CFIntJpaTopProject> lockByTenantIdx(ICFIntTopProjectByTenantIdxKey key) {
		return( lockByTenantIdx(key.getRequiredTenantId()));
	}

	/**
	 *	Argument-based lock database instance for compatibility with the current MSS code factory code base, uses @Transactional to acquire a JPA entity locks, which may or may not imply an actual database lock during the transaction.
	 *
	 *		@param requiredTopDomainId
	 *
	 *		@return A list of locked entities, refreshed from the data store, or an empty list if no such entities exist.
	 */
	@Transactional
	@Lock(LockModeType.WRITE)
	@Query("select r from CFIntJpaTopProject r where r.requiredContainerParentSDom.requiredId = :topDomainId")
	List<CFIntJpaTopProject> lockByTopDomainIdx(@Param("topDomainId") CFLibDbKeyHash256 requiredTopDomainId);

	/**
	 *	CFIntTopProjectByTopDomainIdxKey based lock method for object-based access.
	 *
	 *		@param key The key of the entity to be locked.
	 *
	 *		@return A list of locked entities, refreshed from the data store, or an empty list if no such entities exist.
	 */
	default List<CFIntJpaTopProject> lockByTopDomainIdx(ICFIntTopProjectByTopDomainIdxKey key) {
		return( lockByTopDomainIdx(key.getRequiredTopDomainId()));
	}

	/**
	 *	Argument-based lock database entity for compatibility with the current MSS code factory code base, uses @Transactional to acquire a JPA entity locks, which may or may not imply an actual database lock during the transaction.
	 *
	 *		@param requiredTopDomainId
	 *		@param requiredName
	 *
	 *		@return The locked entity, refreshed from the data store, or null if no such entity exists.
	 */
	@Transactional
	@Lock(LockModeType.WRITE)
	@Query("select r from CFIntJpaTopProject r where r.requiredContainerParentSDom.requiredId = :topDomainId and r.requiredName = :name")
	CFIntJpaTopProject lockByNameIdx(@Param("topDomainId") CFLibDbKeyHash256 requiredTopDomainId,
		@Param("name") String requiredName);

	/**
	 *	CFIntTopProjectByNameIdxKey based lock method for object-based access.
	 *
	 *		@param key The key of the entity to be locked.
	 *
	 *		@return The locked entity, refreshed from the data store, or null if no such entity exists.
	 */
	default CFIntJpaTopProject lockByNameIdx(ICFIntTopProjectByNameIdxKey key) {
		return( lockByNameIdx(key.getRequiredTopDomainId(), key.getRequiredName()));
	}

	// CFIntJpaTopProject specified delete-by-index methods

	/**
	 *	Argument-based delete entity for compatibility with the current MSS code factory code base, uses @Transactional to acquire a JPA entity lock, which may or may not imply an actual database lock during the transaction.
	 *
	 *		@param requiredId
	 */
	@Transactional
	@Modifying
	@Query("delete from CFIntJpaTopProject r where r.requiredId = :id")
	void deleteByIdIdx(@Param("id") CFLibDbKeyHash256 requiredId);

	/**
	 *	Argument-based delete entity for compatibility with the current MSS code factory code base, uses @Transactional to acquire a JPA entity lock, which may or may not imply an actual database lock during the transaction.
	 *
	 *		@param requiredTenantId
	 */
	@Transactional
	@Modifying
	@Query("delete from CFIntJpaTopProject r where r.requiredTenantId = :tenantId")
	void deleteByTenantIdx(@Param("tenantId") CFLibDbKeyHash256 requiredTenantId);

	/**
	 *	CFIntTopProjectByTenantIdxKey based lock method for object-based access.
	 *
	 *		@param key The CFIntTopProjectByTenantIdxKey of the entity to be locked.
	 */
	default void deleteByTenantIdx(ICFIntTopProjectByTenantIdxKey key) {
		deleteByTenantIdx(key.getRequiredTenantId());
	}

	/**
	 *	Argument-based delete entity for compatibility with the current MSS code factory code base, uses @Transactional to acquire a JPA entity lock, which may or may not imply an actual database lock during the transaction.
	 *
	 *		@param requiredTopDomainId
	 */
	@Transactional
	@Modifying
	@Query("delete from CFIntJpaTopProject r where r.requiredContainerParentSDom.requiredId = :topDomainId")
	void deleteByTopDomainIdx(@Param("topDomainId") CFLibDbKeyHash256 requiredTopDomainId);

	/**
	 *	CFIntTopProjectByTopDomainIdxKey based lock method for object-based access.
	 *
	 *		@param key The CFIntTopProjectByTopDomainIdxKey of the entity to be locked.
	 */
	default void deleteByTopDomainIdx(ICFIntTopProjectByTopDomainIdxKey key) {
		deleteByTopDomainIdx(key.getRequiredTopDomainId());
	}

	/**
	 *	Argument-based delete entity for compatibility with the current MSS code factory code base, uses @Transactional to acquire a JPA entity lock, which may or may not imply an actual database lock during the transaction.
	 *
	 *		@param requiredTopDomainId
	 *		@param requiredName
	 */
	@Transactional
	@Modifying
	@Query("delete from CFIntJpaTopProject r where r.requiredContainerParentSDom.requiredId = :topDomainId and r.requiredName = :name")
	void deleteByNameIdx(@Param("topDomainId") CFLibDbKeyHash256 requiredTopDomainId,
		@Param("name") String requiredName);

	/**
	 *	CFIntTopProjectByNameIdxKey based lock method for object-based access.
	 *
	 *		@param key The CFIntTopProjectByNameIdxKey of the entity to be locked.
	 */
	default void deleteByNameIdx(ICFIntTopProjectByNameIdxKey key) {
		deleteByNameIdx(key.getRequiredTopDomainId(), key.getRequiredName());
	}

}
