// Description: Java 25 Spring JPA Service for MinorVersion

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
 *	Service for the CFIntMinorVersion entities defined in io.github.msobkow.v3_1.cfint.cfint.jpa
 *	using the CFIntMinorVersionRepository to access them.
 */
@Service("cfint31JpaMinorVersionService")
public class CFIntJpaMinorVersionService {

	@Autowired
	@Qualifier("cfint31EntityManagerFactory")
	private LocalContainerEntityManagerFactoryBean cfint31EntityManagerFactory;

	@Autowired
	private CFIntJpaMinorVersionRepository cfint31MinorVersionRepository;

	/**
	 *	Create an entity, generating any database keys required along the way.
	 *
	 *		@param	data	The entity to be instantiated; must be a specific instance of CFIntJpaMinorVersion, not a subclass.
	 *
	 *		@return The updated/created entity.
	 */
	@Transactional(propagation = Propagation.REQUIRED,rollbackFor = NoResultException.class, transactionManager = "cfint31TransactionManager")
	public CFIntJpaMinorVersion create(CFIntJpaMinorVersion data) {
		final String S_ProcName = "create";
		if (data == null) {
			return( null );
		}
		CFLibDbKeyHash256 originalRequiredId = data.getRequiredId();
		boolean generatedRequiredId = false;
		if(data.getRequiredTenantId() == null || data.getRequiredTenantId().isNull()) {
			throw new CFLibNullArgumentException(getClass(),
				S_ProcName,
				0,
				"data.requiredTenantId");
		}
		if(data.getRequiredMajorVersionId() == null || data.getRequiredMajorVersionId().isNull()) {
			throw new CFLibNullArgumentException(getClass(),
				S_ProcName,
				0,
				"data.requiredMajorVersionId");
		}
		if(data.getRequiredName() == null) {
			throw new CFLibNullArgumentException(getClass(),
				S_ProcName,
				0,
				"data.requiredName");
		}
		try {
			if (data.getRequiredId() == null || data.getRequiredId().isNull()) {
				data.setRequiredId(new CFLibDbKeyHash256(0));
				generatedRequiredId = true;
			}
			LocalDateTime now = LocalDateTime.now();
			data.setCreatedAt(now);
			data.setUpdatedAt(now);
			if(data.getPKey() != null && cfint31MinorVersionRepository.existsById((CFLibDbKeyHash256)data.getPKey())) {
				return( (CFIntJpaMinorVersion)(cfint31MinorVersionRepository.findById((CFLibDbKeyHash256)(data.getPKey())).get()));
			}
			return cfint31MinorVersionRepository.save(data);
		}
		catch(Exception ex) {
				if(generatedRequiredId) {
					data.setRequiredId(originalRequiredId);
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
	public CFIntJpaMinorVersion update(CFIntJpaMinorVersion data) {
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
		if(data.getRequiredTenantId() == null || data.getRequiredTenantId().isNull()) {
			throw new CFLibNullArgumentException(getClass(),
				S_ProcName,
				0,
				"data.requiredTenantId");
		}
		if(data.getRequiredMajorVersionId() == null || data.getRequiredMajorVersionId().isNull()) {
			throw new CFLibNullArgumentException(getClass(),
				S_ProcName,
				0,
				"data.requiredMajorVersionId");
		}
		if(data.getRequiredName() == null) {
			throw new CFLibNullArgumentException(getClass(),
				S_ProcName,
				0,
				"data.requiredName");
		}
		// Ensure the entity exists and that the revision matches
		CFIntJpaMinorVersion existing = cfint31MinorVersionRepository.findById((CFLibDbKeyHash256)(data.getPKey()))
			.orElseThrow(() -> new CFLibCollisionDetectedException(getClass(), S_ProcName, data.getPKey()));
		if (existing.getRequiredRevision() != data.getRequiredRevision()) {
			throw new CFLibCollisionDetectedException(getClass(), S_ProcName, data.getPKey());
		}
		// Apply superior data relationships of CFIntMinorVersion to existing object
		existing.setRequiredOwnerTenant(data.getRequiredTenantId());
		existing.setRequiredContainerParentMajVer(data.getRequiredContainerParentMajVer());
		// Apply data columns of CFIntMinorVersion to existing object
		existing.setRequiredName(data.getRequiredName());
		existing.setOptionalDescription(data.getOptionalDescription());
		// Update the audit columns
		data.setUpdatedAt(LocalDateTime.now());
		// Save the changes we've made
		return cfint31MinorVersionRepository.save(existing);
	}

	/**
	 *	Argument-based find database instance for compatibility with the current MSS code factory code base.
	 *
	 *		@param requiredId
	 *
	 *		@return The retrieved entity, or null if no such entity exists.
	 */
	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = NoResultException.class, transactionManager = "cfint31TransactionManager")
	public CFIntJpaMinorVersion find(@Param("id") CFLibDbKeyHash256 requiredId) {
		return( cfint31MinorVersionRepository.get(requiredId));
	}

	/**
	 *	Retrieve all entities from the repository
	 *
	 *		@return The list of retrieved entities, which may be empty
	 */
	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = NoResultException.class, transactionManager = "cfint31TransactionManager")
	public List<CFIntJpaMinorVersion> findAll() {
		return( cfint31MinorVersionRepository.findAll() );
	}

	// CFIntMinorVersion specified index finders

	/**
	 *	Find zero or more entities into a List using the columns of the ICFIntMinorVersionByTenantIdxKey as arguments.
	 *
	 *		@param requiredTenantId
	 *
	 *		@return List&lt;CFIntJpaMinorVersion&gt; of the found entities, or an empty list if no such entities exist.
	 */
	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = NoResultException.class, transactionManager = "cfint31TransactionManager")
	public List<CFIntJpaMinorVersion> findByTenantIdx(@Param("tenantId") CFLibDbKeyHash256 requiredTenantId) {
		return( cfint31MinorVersionRepository.findByTenantIdx(requiredTenantId));
	}

	/**
	 *	ICFIntMinorVersionByTenantIdxKey entity list finder convenience method for object-based access.
	 *
	 *		@param key The ICFIntMinorVersionByTenantIdxKey instance to use for the query arguments.
	 *
	 *		@return The found entity list, which may be empty.
	 */
	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = NoResultException.class, transactionManager = "cfint31TransactionManager")
	public List<CFIntJpaMinorVersion> findByTenantIdx(ICFIntMinorVersionByTenantIdxKey key) {
		return( cfint31MinorVersionRepository.findByTenantIdx(key.getRequiredTenantId()));
	}

	/**
	 *	Find zero or more entities into a List using the columns of the ICFIntMinorVersionByMajorVerIdxKey as arguments.
	 *
	 *		@param requiredMajorVersionId
	 *
	 *		@return List&lt;CFIntJpaMinorVersion&gt; of the found entities, or an empty list if no such entities exist.
	 */
	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = NoResultException.class, transactionManager = "cfint31TransactionManager")
	public List<CFIntJpaMinorVersion> findByMajorVerIdx(@Param("majorVersionId") CFLibDbKeyHash256 requiredMajorVersionId) {
		return( cfint31MinorVersionRepository.findByMajorVerIdx(requiredMajorVersionId));
	}

	/**
	 *	ICFIntMinorVersionByMajorVerIdxKey entity list finder convenience method for object-based access.
	 *
	 *		@param key The ICFIntMinorVersionByMajorVerIdxKey instance to use for the query arguments.
	 *
	 *		@return The found entity list, which may be empty.
	 */
	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = NoResultException.class, transactionManager = "cfint31TransactionManager")
	public List<CFIntJpaMinorVersion> findByMajorVerIdx(ICFIntMinorVersionByMajorVerIdxKey key) {
		return( cfint31MinorVersionRepository.findByMajorVerIdx(key.getRequiredMajorVersionId()));
	}

	/**
	 *	Find an entity using the columns of the ICFIntMinorVersionByNameIdxKey as arguments.
	 *
	 *		@param requiredMajorVersionId
	 *		@param requiredName
	 *
	 *		@return The found entity, or null if no such entity exists.
	 */
	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = NoResultException.class, transactionManager = "cfint31TransactionManager")
	public CFIntJpaMinorVersion findByNameIdx(@Param("majorVersionId") CFLibDbKeyHash256 requiredMajorVersionId,
		@Param("name") String requiredName) {
		return( cfint31MinorVersionRepository.findByNameIdx(requiredMajorVersionId,
			requiredName));
	}

	/**
	 *	ICFIntMinorVersionByNameIdxKey entity finder convenience method for object-based access.
	 *
	 *		@param key The ICFIntMinorVersionByNameIdxKey instance to use for the query arguments.
	 *
	 *		@return The found entity, or null if no such entity exists.
	 */
	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = NoResultException.class, transactionManager = "cfint31TransactionManager")
	public CFIntJpaMinorVersion findByNameIdx(ICFIntMinorVersionByNameIdxKey key) {
		return( cfint31MinorVersionRepository.findByNameIdx(key.getRequiredMajorVersionId(), key.getRequiredName()));
	}

	// CFIntMinorVersion specified lock-by-index methods

	/**
	 *	Argument-based lock database entity for compatibility with the current MSS code factory code base, uses @Transactional to acquire a JPA entity locks, which may or may not imply an actual database lock during the transaction.
	 *
	 *		@param requiredId
	 *
	 *		@return The locked entity, refreshed from the data store, or null if no such entity exists.
	 */
	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = NoResultException.class, transactionManager = "cfint31TransactionManager")
	public CFIntJpaMinorVersion lockByIdIdx(@Param("id") CFLibDbKeyHash256 requiredId) {
		return( cfint31MinorVersionRepository.lockByIdIdx(requiredId));
	}

	/**
	 *	Argument-based lock database instance for compatibility with the current MSS code factory code base, uses @Transactional to acquire a JPA entity locks, which may or may not imply an actual database lock during the transaction.
	 *
	 *		@param requiredTenantId
	 *
	 *		@return A list of locked entities, refreshed from the data store, or an empty list if no such entities exist.
	 */
	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = NoResultException.class, transactionManager = "cfint31TransactionManager")
	public List<CFIntJpaMinorVersion> lockByTenantIdx(@Param("tenantId") CFLibDbKeyHash256 requiredTenantId) {
		return( cfint31MinorVersionRepository.lockByTenantIdx(requiredTenantId));
	}

	/**
	 *	ICFIntMinorVersionByTenantIdxKey based lock method for object-based access.
	 *
	 *		@param key The key of the entity to be locked.
	 *
	 *		@return A list of locked entities, refreshed from the data store, or an empty list if no such entities exist.
	 */
	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = NoResultException.class, transactionManager = "cfint31TransactionManager")
	public List<CFIntJpaMinorVersion> lockByTenantIdx(ICFIntMinorVersionByTenantIdxKey key) {
		return( cfint31MinorVersionRepository.lockByTenantIdx(key.getRequiredTenantId()));
	}

	/**
	 *	Argument-based lock database instance for compatibility with the current MSS code factory code base, uses @Transactional to acquire a JPA entity locks, which may or may not imply an actual database lock during the transaction.
	 *
	 *		@param requiredMajorVersionId
	 *
	 *		@return A list of locked entities, refreshed from the data store, or an empty list if no such entities exist.
	 */
	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = NoResultException.class, transactionManager = "cfint31TransactionManager")
	public List<CFIntJpaMinorVersion> lockByMajorVerIdx(@Param("majorVersionId") CFLibDbKeyHash256 requiredMajorVersionId) {
		return( cfint31MinorVersionRepository.lockByMajorVerIdx(requiredMajorVersionId));
	}

	/**
	 *	ICFIntMinorVersionByMajorVerIdxKey based lock method for object-based access.
	 *
	 *		@param key The key of the entity to be locked.
	 *
	 *		@return A list of locked entities, refreshed from the data store, or an empty list if no such entities exist.
	 */
	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = NoResultException.class, transactionManager = "cfint31TransactionManager")
	public List<CFIntJpaMinorVersion> lockByMajorVerIdx(ICFIntMinorVersionByMajorVerIdxKey key) {
		return( cfint31MinorVersionRepository.lockByMajorVerIdx(key.getRequiredMajorVersionId()));
	}

	/**
	 *	Argument-based lock database entity for compatibility with the current MSS code factory code base, uses @Transactional to acquire a JPA entity locks, which may or may not imply an actual database lock during the transaction.
	 *
	 *		@param requiredMajorVersionId
	 *		@param requiredName
	 *
	 *		@return The locked entity, refreshed from the data store, or null if no such entity exists.
	 */
	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = NoResultException.class, transactionManager = "cfint31TransactionManager")
	public CFIntJpaMinorVersion lockByNameIdx(@Param("majorVersionId") CFLibDbKeyHash256 requiredMajorVersionId,
		@Param("name") String requiredName) {
		return( cfint31MinorVersionRepository.lockByNameIdx(requiredMajorVersionId,
			requiredName));
	}

	/**
	 *	ICFIntMinorVersionByNameIdxKey based lock method for object-based access.
	 *
	 *		@param key The key of the entity to be locked.
	 *
	 *		@return The locked entity, refreshed from the data store, or null if no such entity exists.
	 */
	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = NoResultException.class, transactionManager = "cfint31TransactionManager")
	public CFIntJpaMinorVersion lockByNameIdx(ICFIntMinorVersionByNameIdxKey key) {
		return( cfint31MinorVersionRepository.lockByNameIdx(key.getRequiredMajorVersionId(), key.getRequiredName()));
	}

	// CFIntMinorVersion specified delete-by-index methods

	/**
	 *	Argument-based delete entity for compatibility with the current MSS code factory code base, uses @Transactional to acquire a JPA entity lock, which may or may not imply an actual database lock during the transaction.
	 *
	 *		@param requiredId
	 */
	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = NoResultException.class, transactionManager = "cfint31TransactionManager")
	public void deleteByIdIdx(@Param("id") CFLibDbKeyHash256 requiredId) {
		cfint31MinorVersionRepository.deleteByIdIdx(requiredId);
	}

	/**
	 *	Argument-based delete entity for compatibility with the current MSS code factory code base, uses @Transactional to acquire a JPA entity lock, which may or may not imply an actual database lock during the transaction.
	 *
	 *		@param requiredTenantId
	 */
	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = NoResultException.class, transactionManager = "cfint31TransactionManager")
	public void deleteByTenantIdx(@Param("tenantId") CFLibDbKeyHash256 requiredTenantId) {
		cfint31MinorVersionRepository.deleteByTenantIdx(requiredTenantId);
	}

	/**
	 *	ICFIntMinorVersionByTenantIdxKey based lock method for object-based access.
	 *
	 *		@param key The ICFIntMinorVersionByTenantIdxKey of the entity to be locked.
	 */
	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = NoResultException.class, transactionManager = "cfint31TransactionManager")
	public void deleteByTenantIdx(ICFIntMinorVersionByTenantIdxKey key) {
		cfint31MinorVersionRepository.deleteByTenantIdx(key.getRequiredTenantId());
	}

	/**
	 *	Argument-based delete entity for compatibility with the current MSS code factory code base, uses @Transactional to acquire a JPA entity lock, which may or may not imply an actual database lock during the transaction.
	 *
	 *		@param requiredMajorVersionId
	 */
	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = NoResultException.class, transactionManager = "cfint31TransactionManager")
	public void deleteByMajorVerIdx(@Param("majorVersionId") CFLibDbKeyHash256 requiredMajorVersionId) {
		cfint31MinorVersionRepository.deleteByMajorVerIdx(requiredMajorVersionId);
	}

	/**
	 *	ICFIntMinorVersionByMajorVerIdxKey based lock method for object-based access.
	 *
	 *		@param key The ICFIntMinorVersionByMajorVerIdxKey of the entity to be locked.
	 */
	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = NoResultException.class, transactionManager = "cfint31TransactionManager")
	public void deleteByMajorVerIdx(ICFIntMinorVersionByMajorVerIdxKey key) {
		cfint31MinorVersionRepository.deleteByMajorVerIdx(key.getRequiredMajorVersionId());
	}

	/**
	 *	Argument-based delete entity for compatibility with the current MSS code factory code base, uses @Transactional to acquire a JPA entity lock, which may or may not imply an actual database lock during the transaction.
	 *
	 *		@param requiredMajorVersionId
	 *		@param requiredName
	 */
	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = NoResultException.class, transactionManager = "cfint31TransactionManager")
	public void deleteByNameIdx(@Param("majorVersionId") CFLibDbKeyHash256 requiredMajorVersionId,
		@Param("name") String requiredName) {
		cfint31MinorVersionRepository.deleteByNameIdx(requiredMajorVersionId,
			requiredName);
	}

	/**
	 *	ICFIntMinorVersionByNameIdxKey based lock method for object-based access.
	 *
	 *		@param key The ICFIntMinorVersionByNameIdxKey of the entity to be locked.
	 */
	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = NoResultException.class, transactionManager = "cfint31TransactionManager")
	public void deleteByNameIdx(ICFIntMinorVersionByNameIdxKey key) {
		cfint31MinorVersionRepository.deleteByNameIdx(key.getRequiredMajorVersionId(), key.getRequiredName());
	}
}
