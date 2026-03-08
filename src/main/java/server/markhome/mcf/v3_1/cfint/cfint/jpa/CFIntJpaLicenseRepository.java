// Description: Java 25 Spring JPA Repository for License

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
 *	JpaRepository for the CFIntJpaLicense entities defined in server.markhome.mcf.v3_1.cfint.cfint.jpa.
 *	The manufactured repositories try to provide a rich, do-it-all interface to the JPA data store, with both object and argument-based implementations of the interface defined.
 */
@Transactional(readOnly = true)
public interface CFIntJpaLicenseRepository extends JpaRepository<CFIntJpaLicense, CFLibDbKeyHash256> {

	/**
	 *	Argument-based get database instance for compatibility with the current MSS code factory code base.
	 *
	 *		@param requiredId
	 *
	 *		@return The retrieved entity, usually from the JPA cache, or null if no such entity exists.
	 */
	@Query("select r from CFIntJpaLicense r where r.requiredId = :id")
	CFIntJpaLicense get(@Param("id") CFLibDbKeyHash256 requiredId);

	// CFIntJpaLicense specified index readers

	/**
	 *	Read zero or more entities into a List using the columns of the CFIntLicenseByLicnTenantIdxKey as arguments.
	 *
	 *		@param requiredTenantId
	 *
	 *		@return List&lt;CFIntJpaLicense&gt; of the found entities, typically from the JPA cache, or an empty list if no such entities exist.
	 */
	@Query("select r from CFIntJpaLicense r where r.requiredTenantId = :tenantId")
	List<CFIntJpaLicense> findByLicnTenantIdx(@Param("tenantId") CFLibDbKeyHash256 requiredTenantId);

	/**
	 *	CFIntLicenseByLicnTenantIdxKey entity list reader convenience method for object-based access.
	 *
	 *		@param key The CFIntLicenseByLicnTenantIdxKey instance to use for the query arguments.
	 *
	 *		@return The found entity list, which may be empty, typically populated from the JPA cache.
	 */
	default List<CFIntJpaLicense> findByLicnTenantIdx(ICFIntLicenseByLicnTenantIdxKey key) {
		return( findByLicnTenantIdx(key.getRequiredTenantId()));
	}

	/**
	 *	Read zero or more entities into a List using the columns of the CFIntLicenseByDomainIdxKey as arguments.
	 *
	 *		@param requiredTopDomainId
	 *
	 *		@return List&lt;CFIntJpaLicense&gt; of the found entities, typically from the JPA cache, or an empty list if no such entities exist.
	 */
	@Query("select r from CFIntJpaLicense r where r.requiredContainerTopDomain.requiredId = :topDomainId")
	List<CFIntJpaLicense> findByDomainIdx(@Param("topDomainId") CFLibDbKeyHash256 requiredTopDomainId);

	/**
	 *	CFIntLicenseByDomainIdxKey entity list reader convenience method for object-based access.
	 *
	 *		@param key The CFIntLicenseByDomainIdxKey instance to use for the query arguments.
	 *
	 *		@return The found entity list, which may be empty, typically populated from the JPA cache.
	 */
	default List<CFIntJpaLicense> findByDomainIdx(ICFIntLicenseByDomainIdxKey key) {
		return( findByDomainIdx(key.getRequiredTopDomainId()));
	}

	/**
	 *	Read an entity using the columns of the CFIntLicenseByUNameIdxKey as arguments.
	 *
	 *		@param requiredTopDomainId
	 *		@param requiredName
	 *
	 *		@return The found entity, typically from the JPA cache, or null if no such entity exists.
	 */
	@Query("select r from CFIntJpaLicense r where r.requiredContainerTopDomain.requiredId = :topDomainId and r.requiredName = :name")
	CFIntJpaLicense findByUNameIdx(@Param("topDomainId") CFLibDbKeyHash256 requiredTopDomainId,
		@Param("name") String requiredName);

	/**
	 *	CFIntLicenseByUNameIdxKey entity reader convenience method for object-based access.
	 *
	 *		@param key The CFIntLicenseByUNameIdxKey instance to use for the query arguments.
	 *
	 *		@return The found entity, typically from the JPA cache, or null if no such entity exists.
	 */
	default CFIntJpaLicense findByUNameIdx(ICFIntLicenseByUNameIdxKey key) {
		return( findByUNameIdx(key.getRequiredTopDomainId(), key.getRequiredName()));
	}

	// CFIntJpaLicense specified delete-by-index methods

	/**
	 *	Argument-based lock database entity for compatibility with the current MSS code factory code base, uses @Transactional to acquire a JPA entity locks, which may or may not imply an actual database lock during the transaction.
	 *
	 *		@param requiredId
	 *
	 *		@return The locked entity, refreshed from the data store, or null if no such entity exists.
	 */
	@Transactional
	@Lock(LockModeType.WRITE)
	@Query("select r from CFIntJpaLicense r where r.requiredId = :id")
	CFIntJpaLicense lockByIdIdx(@Param("id") CFLibDbKeyHash256 requiredId);

	/**
	 *	Argument-based lock database instance for compatibility with the current MSS code factory code base, uses @Transactional to acquire a JPA entity locks, which may or may not imply an actual database lock during the transaction.
	 *
	 *		@param requiredTenantId
	 *
	 *		@return A list of locked entities, refreshed from the data store, or an empty list if no such entities exist.
	 */
	@Transactional
	@Lock(LockModeType.WRITE)
	@Query("select r from CFIntJpaLicense r where r.requiredTenantId = :tenantId")
	List<CFIntJpaLicense> lockByLicnTenantIdx(@Param("tenantId") CFLibDbKeyHash256 requiredTenantId);

	/**
	 *	CFIntLicenseByLicnTenantIdxKey based lock method for object-based access.
	 *
	 *		@param key The key of the entity to be locked.
	 *
	 *		@return A list of locked entities, refreshed from the data store, or an empty list if no such entities exist.
	 */
	default List<CFIntJpaLicense> lockByLicnTenantIdx(ICFIntLicenseByLicnTenantIdxKey key) {
		return( lockByLicnTenantIdx(key.getRequiredTenantId()));
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
	@Query("select r from CFIntJpaLicense r where r.requiredContainerTopDomain.requiredId = :topDomainId")
	List<CFIntJpaLicense> lockByDomainIdx(@Param("topDomainId") CFLibDbKeyHash256 requiredTopDomainId);

	/**
	 *	CFIntLicenseByDomainIdxKey based lock method for object-based access.
	 *
	 *		@param key The key of the entity to be locked.
	 *
	 *		@return A list of locked entities, refreshed from the data store, or an empty list if no such entities exist.
	 */
	default List<CFIntJpaLicense> lockByDomainIdx(ICFIntLicenseByDomainIdxKey key) {
		return( lockByDomainIdx(key.getRequiredTopDomainId()));
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
	@Query("select r from CFIntJpaLicense r where r.requiredContainerTopDomain.requiredId = :topDomainId and r.requiredName = :name")
	CFIntJpaLicense lockByUNameIdx(@Param("topDomainId") CFLibDbKeyHash256 requiredTopDomainId,
		@Param("name") String requiredName);

	/**
	 *	CFIntLicenseByUNameIdxKey based lock method for object-based access.
	 *
	 *		@param key The key of the entity to be locked.
	 *
	 *		@return The locked entity, refreshed from the data store, or null if no such entity exists.
	 */
	default CFIntJpaLicense lockByUNameIdx(ICFIntLicenseByUNameIdxKey key) {
		return( lockByUNameIdx(key.getRequiredTopDomainId(), key.getRequiredName()));
	}

	// CFIntJpaLicense specified delete-by-index methods

	/**
	 *	Argument-based delete entity for compatibility with the current MSS code factory code base, uses @Transactional to acquire a JPA entity lock, which may or may not imply an actual database lock during the transaction.
	 *
	 *		@param requiredId
	 */
	@Transactional
	@Modifying
	@Query("delete from CFIntJpaLicense r where r.requiredId = :id")
	void deleteByIdIdx(@Param("id") CFLibDbKeyHash256 requiredId);

	/**
	 *	Argument-based delete entity for compatibility with the current MSS code factory code base, uses @Transactional to acquire a JPA entity lock, which may or may not imply an actual database lock during the transaction.
	 *
	 *		@param requiredTenantId
	 */
	@Transactional
	@Modifying
	@Query("delete from CFIntJpaLicense r where r.requiredTenantId = :tenantId")
	void deleteByLicnTenantIdx(@Param("tenantId") CFLibDbKeyHash256 requiredTenantId);

	/**
	 *	CFIntLicenseByLicnTenantIdxKey based lock method for object-based access.
	 *
	 *		@param key The CFIntLicenseByLicnTenantIdxKey of the entity to be locked.
	 */
	default void deleteByLicnTenantIdx(ICFIntLicenseByLicnTenantIdxKey key) {
		deleteByLicnTenantIdx(key.getRequiredTenantId());
	}

	/**
	 *	Argument-based delete entity for compatibility with the current MSS code factory code base, uses @Transactional to acquire a JPA entity lock, which may or may not imply an actual database lock during the transaction.
	 *
	 *		@param requiredTopDomainId
	 */
	@Transactional
	@Modifying
	@Query("delete from CFIntJpaLicense r where r.requiredContainerTopDomain.requiredId = :topDomainId")
	void deleteByDomainIdx(@Param("topDomainId") CFLibDbKeyHash256 requiredTopDomainId);

	/**
	 *	CFIntLicenseByDomainIdxKey based lock method for object-based access.
	 *
	 *		@param key The CFIntLicenseByDomainIdxKey of the entity to be locked.
	 */
	default void deleteByDomainIdx(ICFIntLicenseByDomainIdxKey key) {
		deleteByDomainIdx(key.getRequiredTopDomainId());
	}

	/**
	 *	Argument-based delete entity for compatibility with the current MSS code factory code base, uses @Transactional to acquire a JPA entity lock, which may or may not imply an actual database lock during the transaction.
	 *
	 *		@param requiredTopDomainId
	 *		@param requiredName
	 */
	@Transactional
	@Modifying
	@Query("delete from CFIntJpaLicense r where r.requiredContainerTopDomain.requiredId = :topDomainId and r.requiredName = :name")
	void deleteByUNameIdx(@Param("topDomainId") CFLibDbKeyHash256 requiredTopDomainId,
		@Param("name") String requiredName);

	/**
	 *	CFIntLicenseByUNameIdxKey based lock method for object-based access.
	 *
	 *		@param key The CFIntLicenseByUNameIdxKey of the entity to be locked.
	 */
	default void deleteByUNameIdx(ICFIntLicenseByUNameIdxKey key) {
		deleteByUNameIdx(key.getRequiredTopDomainId(), key.getRequiredName());
	}

}
