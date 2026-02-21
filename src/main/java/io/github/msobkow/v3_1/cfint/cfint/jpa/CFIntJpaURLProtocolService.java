// Description: Java 25 Spring JPA Service for URLProtocol

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import io.github.msobkow.v3_1.cfsec.cfsec.*;
import io.github.msobkow.v3_1.cfint.cfint.*;

/**
 *	Service for the CFIntURLProtocol entities defined in io.github.msobkow.v3_1.cfint.cfint.jpa
 *	using the CFIntURLProtocolRepository to access them.
 */
@Service("cfint31JpaURLProtocolService")
public class CFIntJpaURLProtocolService {

	@Autowired
	@Qualifier("cfint31EntityManagerFactory")
	private LocalContainerEntityManagerFactoryBean cfint31EntityManagerFactory;

	@Autowired
	private CFIntJpaURLProtocolRepository cfint31URLProtocolRepository;

	/**
	 *	Create an entity, generating any database keys required along the way.
	 *
	 *		@param	data	The entity to be instantiated; must be a specific instance of CFIntJpaURLProtocol, not a subclass.
	 *
	 *		@return The updated/created entity.
	 */
	@Transactional(propagation = Propagation.REQUIRED,rollbackFor = NoResultException.class, transactionManager = "cfint31TransactionManager")
	public CFIntJpaURLProtocol create(CFIntJpaURLProtocol data) {
		final String S_ProcName = "create";
		if (data == null) {
			return( null );
		}
		int originalRequiredURLProtocolId = data.getRequiredURLProtocolId();
		boolean generatedRequiredURLProtocolId = false;
		if(data.getRequiredName() == null) {
			throw new CFLibNullArgumentException(getClass(),
				S_ProcName,
				0,
				"data.requiredName");
		}
		if(data.getRequiredDescription() == null) {
			throw new CFLibNullArgumentException(getClass(),
				S_ProcName,
				0,
				"data.requiredDescription");
		}
		try {
			LocalDateTime now = LocalDateTime.now();
			data.setCreatedAt(now);
			data.setUpdatedAt(now);
			if(data.getPKey() != null && cfint31URLProtocolRepository.existsById((Integer)data.getPKey())) {
				return( (CFIntJpaURLProtocol)(cfint31URLProtocolRepository.findById((Integer)(data.getPKey())).get()));
			}
			return cfint31URLProtocolRepository.save(data);
		}
		catch(Exception ex) {
				if(generatedRequiredURLProtocolId) {
					data.setRequiredURLProtocolId(originalRequiredURLProtocolId);
				}
			throw new CFLibDbException(getClass(),
				S_ProcName,
				ex);
		}
	}

	/**
	 *	Update an existing entity.
	 *
	 *		@param	data	The entity to be updated.
	 *
	 *		@return The updated entity.
	 */
	@Transactional(propagation = Propagation.REQUIRED,rollbackFor = NoResultException.class, transactionManager = "cfint31TransactionManager")
	public CFIntJpaURLProtocol update(CFIntJpaURLProtocol data) {
		final String S_ProcName = "update";
		if (data == null) {
			return( null );
		}
		if (data.getPKey() == null) {
			throw new CFLibNullArgumentException(getClass(),
				S_ProcName,
				0,
				"data.getPKey()");
		}
		if(data.getRequiredName() == null) {
			throw new CFLibNullArgumentException(getClass(),
				S_ProcName,
				0,
				"data.requiredName");
		}
		if(data.getRequiredDescription() == null) {
			throw new CFLibNullArgumentException(getClass(),
				S_ProcName,
				0,
				"data.requiredDescription");
		}
		// Ensure the entity exists and that the revision matches
		CFIntJpaURLProtocol existing = cfint31URLProtocolRepository.findById((Integer)(data.getPKey()))
			.orElseThrow(() -> new CFLibCollisionDetectedException(getClass(), S_ProcName, data.getPKey()));
		if (existing.getRequiredRevision() != data.getRequiredRevision()) {
			throw new CFLibCollisionDetectedException(getClass(), S_ProcName, data.getPKey());
		}
		// Apply superior data relationships of CFIntURLProtocol to existing object
		// Apply data columns of CFIntURLProtocol to existing object
		existing.setRequiredName(data.getRequiredName());
		existing.setRequiredDescription(data.getRequiredDescription());
		existing.setRequiredIsSecure(data.getRequiredIsSecure());
		// Update the audit columns
		data.setUpdatedAt(LocalDateTime.now());
		// Save the changes we've made
		return cfint31URLProtocolRepository.save(existing);
	}

	/**
	 *	Argument-based find database instance for compatibility with the current MSS code factory code base.
	 *
	 *		@param requiredURLProtocolId
	 *
	 *		@return The retrieved entity, or null if no such entity exists.
	 */
	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = NoResultException.class, transactionManager = "cfint31TransactionManager")
	public CFIntJpaURLProtocol find(@Param("uRLProtocolId") int requiredURLProtocolId) {
		return( cfint31URLProtocolRepository.get(requiredURLProtocolId));
	}

	/**
	 *	Retrieve all entities from the repository
	 *
	 *		@return The list of retrieved entities, which may be empty
	 */
	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = NoResultException.class, transactionManager = "cfint31TransactionManager")
	public List<CFIntJpaURLProtocol> findAll() {
		return( cfint31URLProtocolRepository.findAll() );
	}

	// CFIntURLProtocol specified index finders

	/**
	 *	Find an entity using the columns of the ICFIntURLProtocolByUNameIdxKey as arguments.
	 *
	 *		@param requiredName
	 *
	 *		@return The found entity, or null if no such entity exists.
	 */
	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = NoResultException.class, transactionManager = "cfint31TransactionManager")
	public CFIntJpaURLProtocol findByUNameIdx(@Param("name") String requiredName) {
		return( cfint31URLProtocolRepository.findByUNameIdx(requiredName));
	}

	/**
	 *	ICFIntURLProtocolByUNameIdxKey entity finder convenience method for object-based access.
	 *
	 *		@param key The ICFIntURLProtocolByUNameIdxKey instance to use for the query arguments.
	 *
	 *		@return The found entity, or null if no such entity exists.
	 */
	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = NoResultException.class, transactionManager = "cfint31TransactionManager")
	public CFIntJpaURLProtocol findByUNameIdx(ICFIntURLProtocolByUNameIdxKey key) {
		return( cfint31URLProtocolRepository.findByUNameIdx(key.getRequiredName()));
	}

	/**
	 *	Find zero or more entities into a List using the columns of the ICFIntURLProtocolByIsSecureIdxKey as arguments.
	 *
	 *		@param requiredIsSecure
	 *
	 *		@return List&lt;CFIntJpaURLProtocol&gt; of the found entities, or an empty list if no such entities exist.
	 */
	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = NoResultException.class, transactionManager = "cfint31TransactionManager")
	public List<CFIntJpaURLProtocol> findByIsSecureIdx(@Param("isSecure") boolean requiredIsSecure) {
		return( cfint31URLProtocolRepository.findByIsSecureIdx(requiredIsSecure));
	}

	/**
	 *	ICFIntURLProtocolByIsSecureIdxKey entity list finder convenience method for object-based access.
	 *
	 *		@param key The ICFIntURLProtocolByIsSecureIdxKey instance to use for the query arguments.
	 *
	 *		@return The found entity list, which may be empty.
	 */
	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = NoResultException.class, transactionManager = "cfint31TransactionManager")
	public List<CFIntJpaURLProtocol> findByIsSecureIdx(ICFIntURLProtocolByIsSecureIdxKey key) {
		return( cfint31URLProtocolRepository.findByIsSecureIdx(key.getRequiredIsSecure()));
	}

	// CFIntURLProtocol specified lock-by-index methods

	/**
	 *	Argument-based lock database entity for compatibility with the current MSS code factory code base, uses @Transactional to acquire a JPA entity locks, which may or may not imply an actual database lock during the transaction.
	 *
	 *		@param requiredURLProtocolId
	 *
	 *		@return The locked entity, refreshed from the data store, or null if no such entity exists.
	 */
	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = NoResultException.class, transactionManager = "cfint31TransactionManager")
	public CFIntJpaURLProtocol lockByIdIdx(@Param("uRLProtocolId") int requiredURLProtocolId) {
		return( cfint31URLProtocolRepository.lockByIdIdx(requiredURLProtocolId));
	}

	/**
	 *	Argument-based lock database entity for compatibility with the current MSS code factory code base, uses @Transactional to acquire a JPA entity locks, which may or may not imply an actual database lock during the transaction.
	 *
	 *		@param requiredName
	 *
	 *		@return The locked entity, refreshed from the data store, or null if no such entity exists.
	 */
	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = NoResultException.class, transactionManager = "cfint31TransactionManager")
	public CFIntJpaURLProtocol lockByUNameIdx(@Param("name") String requiredName) {
		return( cfint31URLProtocolRepository.lockByUNameIdx(requiredName));
	}

	/**
	 *	ICFIntURLProtocolByUNameIdxKey based lock method for object-based access.
	 *
	 *		@param key The key of the entity to be locked.
	 *
	 *		@return The locked entity, refreshed from the data store, or null if no such entity exists.
	 */
	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = NoResultException.class, transactionManager = "cfint31TransactionManager")
	public CFIntJpaURLProtocol lockByUNameIdx(ICFIntURLProtocolByUNameIdxKey key) {
		return( cfint31URLProtocolRepository.lockByUNameIdx(key.getRequiredName()));
	}

	/**
	 *	Argument-based lock database instance for compatibility with the current MSS code factory code base, uses @Transactional to acquire a JPA entity locks, which may or may not imply an actual database lock during the transaction.
	 *
	 *		@param requiredIsSecure
	 *
	 *		@return A list of locked entities, refreshed from the data store, or an empty list if no such entities exist.
	 */
	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = NoResultException.class, transactionManager = "cfint31TransactionManager")
	public List<CFIntJpaURLProtocol> lockByIsSecureIdx(@Param("isSecure") boolean requiredIsSecure) {
		return( cfint31URLProtocolRepository.lockByIsSecureIdx(requiredIsSecure));
	}

	/**
	 *	ICFIntURLProtocolByIsSecureIdxKey based lock method for object-based access.
	 *
	 *		@param key The key of the entity to be locked.
	 *
	 *		@return A list of locked entities, refreshed from the data store, or an empty list if no such entities exist.
	 */
	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = NoResultException.class, transactionManager = "cfint31TransactionManager")
	public List<CFIntJpaURLProtocol> lockByIsSecureIdx(ICFIntURLProtocolByIsSecureIdxKey key) {
		return( cfint31URLProtocolRepository.lockByIsSecureIdx(key.getRequiredIsSecure()));
	}

	// CFIntURLProtocol specified delete-by-index methods

	/**
	 *	Argument-based delete entity for compatibility with the current MSS code factory code base, uses @Transactional to acquire a JPA entity lock, which may or may not imply an actual database lock during the transaction.
	 *
	 *		@param requiredURLProtocolId
	 */
	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = NoResultException.class, transactionManager = "cfint31TransactionManager")
	public void deleteByIdIdx(@Param("uRLProtocolId") int requiredURLProtocolId) {
		cfint31URLProtocolRepository.deleteByIdIdx(requiredURLProtocolId);
	}

	/**
	 *	Argument-based delete entity for compatibility with the current MSS code factory code base, uses @Transactional to acquire a JPA entity lock, which may or may not imply an actual database lock during the transaction.
	 *
	 *		@param requiredName
	 */
	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = NoResultException.class, transactionManager = "cfint31TransactionManager")
	public void deleteByUNameIdx(@Param("name") String requiredName) {
		cfint31URLProtocolRepository.deleteByUNameIdx(requiredName);
	}

	/**
	 *	ICFIntURLProtocolByUNameIdxKey based lock method for object-based access.
	 *
	 *		@param key The ICFIntURLProtocolByUNameIdxKey of the entity to be locked.
	 */
	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = NoResultException.class, transactionManager = "cfint31TransactionManager")
	public void deleteByUNameIdx(ICFIntURLProtocolByUNameIdxKey key) {
		cfint31URLProtocolRepository.deleteByUNameIdx(key.getRequiredName());
	}

	/**
	 *	Argument-based delete entity for compatibility with the current MSS code factory code base, uses @Transactional to acquire a JPA entity lock, which may or may not imply an actual database lock during the transaction.
	 *
	 *		@param requiredIsSecure
	 */
	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = NoResultException.class, transactionManager = "cfint31TransactionManager")
	public void deleteByIsSecureIdx(@Param("isSecure") boolean requiredIsSecure) {
		cfint31URLProtocolRepository.deleteByIsSecureIdx(requiredIsSecure);
	}

	/**
	 *	ICFIntURLProtocolByIsSecureIdxKey based lock method for object-based access.
	 *
	 *		@param key The ICFIntURLProtocolByIsSecureIdxKey of the entity to be locked.
	 */
	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = NoResultException.class, transactionManager = "cfint31TransactionManager")
	public void deleteByIsSecureIdx(ICFIntURLProtocolByIsSecureIdxKey key) {
		cfint31URLProtocolRepository.deleteByIsSecureIdx(key.getRequiredIsSecure());
	}
}
