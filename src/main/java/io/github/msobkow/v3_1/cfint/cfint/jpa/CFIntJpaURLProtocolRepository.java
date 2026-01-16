// Description: Java 25 Spring JPA Repository for URLProtocol

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
 *	JpaRepository for the CFIntJpaURLProtocol entities defined in io.github.msobkow.v3_1.cfint.cfint.jpa.
 *	The manufactured repositories try to provide a rich, do-it-all interface to the JPA data store, with both object and argument-based implementations of the interface defined.
 */
@Transactional(readOnly = true)
public interface CFIntJpaURLProtocolRepository extends JpaRepository<CFIntJpaURLProtocol, Integer> {

	/**
	 *	Argument-based get database instance for compatibility with the current MSS code factory code base.
	 *
	 *		@param requiredURLProtocolId
	 *
	 *		@return The retrieved entity, usually from the JPA cache, or null if no such entity exists.
	 */
	@Query("select r from CFIntJpaURLProtocol r where r.requiredURLProtocolId = :uRLProtocolId")
	CFIntJpaURLProtocol get(@Param("uRLProtocolId") int requiredURLProtocolId);

	// CFIntJpaURLProtocol specified index readers

	/**
	 *	Read an entity using the columns of the CFIntURLProtocolByUNameIdxKey as arguments.
	 *
	 *		@param requiredName
	 *
	 *		@return The found entity, typically from the JPA cache, or null if no such entity exists.
	 */
	@Query("select r from CFIntJpaURLProtocol r where r.requiredName = :name")
	CFIntJpaURLProtocol findByUNameIdx(@Param("name") String requiredName);

	/**
	 *	CFIntURLProtocolByUNameIdxKey entity reader convenience method for object-based access.
	 *
	 *		@param key The CFIntURLProtocolByUNameIdxKey instance to use for the query arguments.
	 *
	 *		@return The found entity, typically from the JPA cache, or null if no such entity exists.
	 */
	default CFIntJpaURLProtocol findByUNameIdx(ICFIntURLProtocolByUNameIdxKey key) {
		return( findByUNameIdx(key.getRequiredName()));
	}

	/**
	 *	Read zero or more entities into a List using the columns of the CFIntURLProtocolByIsSecureIdxKey as arguments.
	 *
	 *		@param requiredIsSecure
	 *
	 *		@return List&lt;CFIntJpaURLProtocol&gt; of the found entities, typically from the JPA cache, or an empty list if no such entities exist.
	 */
	@Query("select r from CFIntJpaURLProtocol r where r.requiredIsSecure = :isSecure")
	List<CFIntJpaURLProtocol> findByIsSecureIdx(@Param("isSecure") boolean requiredIsSecure);

	/**
	 *	CFIntURLProtocolByIsSecureIdxKey entity list reader convenience method for object-based access.
	 *
	 *		@param key The CFIntURLProtocolByIsSecureIdxKey instance to use for the query arguments.
	 *
	 *		@return The found entity list, which may be empty, typically populated from the JPA cache.
	 */
	default List<CFIntJpaURLProtocol> findByIsSecureIdx(ICFIntURLProtocolByIsSecureIdxKey key) {
		return( findByIsSecureIdx(key.getRequiredIsSecure()));
	}

	// CFIntJpaURLProtocol specified delete-by-index methods

	/**
	 *	Argument-based lock database entity for compatibility with the current MSS code factory code base, uses @Transactional to acquire a JPA entity locks, which may or may not imply an actual database lock during the transaction.
	 *
	 *		@param requiredURLProtocolId
	 *
	 *		@return The locked entity, refreshed from the data store, or null if no such entity exists.
	 */
	@Transactional
	@Lock(LockModeType.WRITE)
	@Query("select r from CFIntJpaURLProtocol r where r.requiredURLProtocolId = :uRLProtocolId")
	CFIntJpaURLProtocol lockByIdIdx(@Param("uRLProtocolId") int requiredURLProtocolId);

	/**
	 *	Argument-based lock database entity for compatibility with the current MSS code factory code base, uses @Transactional to acquire a JPA entity locks, which may or may not imply an actual database lock during the transaction.
	 *
	 *		@param requiredName
	 *
	 *		@return The locked entity, refreshed from the data store, or null if no such entity exists.
	 */
	@Transactional
	@Lock(LockModeType.WRITE)
	@Query("select r from CFIntJpaURLProtocol r where r.requiredName = :name")
	CFIntJpaURLProtocol lockByUNameIdx(@Param("name") String requiredName);

	/**
	 *	CFIntURLProtocolByUNameIdxKey based lock method for object-based access.
	 *
	 *		@param key The key of the entity to be locked.
	 *
	 *		@return The locked entity, refreshed from the data store, or null if no such entity exists.
	 */
	default CFIntJpaURLProtocol lockByUNameIdx(ICFIntURLProtocolByUNameIdxKey key) {
		return( lockByUNameIdx(key.getRequiredName()));
	}

	/**
	 *	Argument-based lock database instance for compatibility with the current MSS code factory code base, uses @Transactional to acquire a JPA entity locks, which may or may not imply an actual database lock during the transaction.
	 *
	 *		@param requiredIsSecure
	 *
	 *		@return A list of locked entities, refreshed from the data store, or an empty list if no such entities exist.
	 */
	@Transactional
	@Lock(LockModeType.WRITE)
	@Query("select r from CFIntJpaURLProtocol r where r.requiredIsSecure = :isSecure")
	List<CFIntJpaURLProtocol> lockByIsSecureIdx(@Param("isSecure") boolean requiredIsSecure);

	/**
	 *	CFIntURLProtocolByIsSecureIdxKey based lock method for object-based access.
	 *
	 *		@param key The key of the entity to be locked.
	 *
	 *		@return A list of locked entities, refreshed from the data store, or an empty list if no such entities exist.
	 */
	default List<CFIntJpaURLProtocol> lockByIsSecureIdx(ICFIntURLProtocolByIsSecureIdxKey key) {
		return( lockByIsSecureIdx(key.getRequiredIsSecure()));
	}

	// CFIntJpaURLProtocol specified delete-by-index methods

	/**
	 *	Argument-based delete entity for compatibility with the current MSS code factory code base, uses @Transactional to acquire a JPA entity lock, which may or may not imply an actual database lock during the transaction.
	 *
	 *		@param requiredURLProtocolId
	 */
	@Transactional
	@Modifying
	@Query("delete from CFIntJpaURLProtocol r where r.requiredURLProtocolId = :uRLProtocolId")
	void deleteByIdIdx(@Param("uRLProtocolId") int requiredURLProtocolId);

	/**
	 *	Argument-based delete entity for compatibility with the current MSS code factory code base, uses @Transactional to acquire a JPA entity lock, which may or may not imply an actual database lock during the transaction.
	 *
	 *		@param requiredName
	 */
	@Transactional
	@Modifying
	@Query("delete from CFIntJpaURLProtocol r where r.requiredName = :name")
	void deleteByUNameIdx(@Param("name") String requiredName);

	/**
	 *	CFIntURLProtocolByUNameIdxKey based lock method for object-based access.
	 *
	 *		@param key The CFIntURLProtocolByUNameIdxKey of the entity to be locked.
	 */
	default void deleteByUNameIdx(ICFIntURLProtocolByUNameIdxKey key) {
		deleteByUNameIdx(key.getRequiredName());
	}

	/**
	 *	Argument-based delete entity for compatibility with the current MSS code factory code base, uses @Transactional to acquire a JPA entity lock, which may or may not imply an actual database lock during the transaction.
	 *
	 *		@param requiredIsSecure
	 */
	@Transactional
	@Modifying
	@Query("delete from CFIntJpaURLProtocol r where r.requiredIsSecure = :isSecure")
	void deleteByIsSecureIdx(@Param("isSecure") boolean requiredIsSecure);

	/**
	 *	CFIntURLProtocolByIsSecureIdxKey based lock method for object-based access.
	 *
	 *		@param key The CFIntURLProtocolByIsSecureIdxKey of the entity to be locked.
	 */
	default void deleteByIsSecureIdx(ICFIntURLProtocolByIsSecureIdxKey key) {
		deleteByIsSecureIdx(key.getRequiredIsSecure());
	}

}
