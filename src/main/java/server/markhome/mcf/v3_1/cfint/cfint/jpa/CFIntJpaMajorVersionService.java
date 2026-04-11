// Description: Java 25 Spring JPA Service for MajorVersion

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import server.markhome.mcf.v3_1.cfsec.cfsec.*;
import server.markhome.mcf.v3_1.cfint.cfint.*;

/**
 *	Service for the CFIntMajorVersion entities defined in server.markhome.mcf.v3_1.cfint.cfint.jpa
 *	using the CFIntMajorVersionRepository to access them.
 */
@Service("cfint31JpaMajorVersionService")
public class CFIntJpaMajorVersionService {

	@Autowired
	@Qualifier("cfint31EntityManagerFactory")
	private LocalContainerEntityManagerFactoryBean cfint31EntityManagerFactory;

	@Autowired
	private CFIntJpaMajorVersionRepository cfint31MajorVersionRepository;

	/**
	 *	Create an entity, generating any database keys required along the way.
	 *
	 *		@param	data	The entity to be instantiated; must be a specific instance of CFIntJpaMajorVersion, not a subclass.
	 *
	 *		@return The updated/created entity.
	 */
	@Transactional(propagation = Propagation.REQUIRED,rollbackFor = NoResultException.class, transactionManager = "cfint31TransactionManager")
	public CFIntJpaMajorVersion create(CFIntJpaMajorVersion data) {
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
		if(data.getRequiredSubProjectId() == null || data.getRequiredSubProjectId().isNull()) {
			throw new CFLibNullArgumentException(getClass(),
				S_ProcName,
				0,
				"data.requiredSubProjectId");
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
			if(data.getPKey() != null && cfint31MajorVersionRepository.existsById((CFLibDbKeyHash256)data.getPKey())) {
				return( (CFIntJpaMajorVersion)(cfint31MajorVersionRepository.findById((CFLibDbKeyHash256)(data.getPKey())).get()));
			}
			return cfint31MajorVersionRepository.save(data);
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
	public CFIntJpaMajorVersion update(CFIntJpaMajorVersion data) {
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
		if(data.getRequiredSubProjectId() == null || data.getRequiredSubProjectId().isNull()) {
			throw new CFLibNullArgumentException(getClass(),
				S_ProcName,
				0,
				"data.requiredSubProjectId");
		}
		if(data.getRequiredName() == null) {
			throw new CFLibNullArgumentException(getClass(),
				S_ProcName,
				0,
				"data.requiredName");
		}
		// Ensure the entity exists and that the revision matches
		CFIntJpaMajorVersion existing = cfint31MajorVersionRepository.findById((CFLibDbKeyHash256)(data.getPKey()))
			.orElseThrow(() -> new CFLibCollisionDetectedException(getClass(), S_ProcName, data.getPKey()));
		if (existing.getRequiredRevision() != data.getRequiredRevision()) {
			throw new CFLibCollisionDetectedException(getClass(), S_ProcName, data.getPKey());
		}
		// Apply superior data relationships of CFIntMajorVersion to existing object
		existing.setRequiredOwnerTenant(data.getRequiredTenantId());
		existing.setRequiredContainerParentSPrj(data.getRequiredContainerParentSPrj());
		// Apply data columns of CFIntMajorVersion to existing object
		existing.setRequiredName(data.getRequiredName());
		existing.setOptionalDescription(data.getOptionalDescription());
		// Update the audit columns
		data.setUpdatedAt(LocalDateTime.now());
		// Save the changes we've made
		return cfint31MajorVersionRepository.save(existing);
	}

	/**
	 *	Argument-based find database instance for compatibility with the current MSS code factory code base.
	 *
	 *		@param requiredId
	 *
	 *		@return The retrieved entity, or null if no such entity exists.
	 */
	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = NoResultException.class, transactionManager = "cfint31TransactionManager")
	public CFIntJpaMajorVersion find(@Param("id") CFLibDbKeyHash256 requiredId) {
		return( cfint31MajorVersionRepository.get(requiredId));
	}

	/**
	 *	Retrieve all entities from the repository
	 *
	 *		@return The list of retrieved entities, which may be empty
	 */
	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = NoResultException.class, transactionManager = "cfint31TransactionManager")
	public List<CFIntJpaMajorVersion> findAll() {
		return( cfint31MajorVersionRepository.findAll() );
	}

	// CFIntMajorVersion specified index finders

	/**
	 *	Find zero or more entities into a List using the columns of the ICFIntMajorVersionByTenantIdxKey as arguments.
	 *
	 *		@param requiredTenantId
	 *
	 *		@return List&lt;CFIntJpaMajorVersion&gt; of the found entities, or an empty list if no such entities exist.
	 */
	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = NoResultException.class, transactionManager = "cfint31TransactionManager")
	public List<CFIntJpaMajorVersion> findByTenantIdx(@Param("tenantId") CFLibDbKeyHash256 requiredTenantId) {
		return( cfint31MajorVersionRepository.findByTenantIdx(requiredTenantId));
	}

	/**
	 *	ICFIntMajorVersionByTenantIdxKey entity list finder convenience method for object-based access.
	 *
	 *		@param key The ICFIntMajorVersionByTenantIdxKey instance to use for the query arguments.
	 *
	 *		@return The found entity list, which may be empty.
	 */
	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = NoResultException.class, transactionManager = "cfint31TransactionManager")
	public List<CFIntJpaMajorVersion> findByTenantIdx(ICFIntMajorVersionByTenantIdxKey key) {
		return( cfint31MajorVersionRepository.findByTenantIdx(key.getRequiredTenantId()));
	}

	/**
	 *	Find zero or more entities into a List using the columns of the ICFIntMajorVersionBySubProjectIdxKey as arguments.
	 *
	 *		@param requiredSubProjectId
	 *
	 *		@return List&lt;CFIntJpaMajorVersion&gt; of the found entities, or an empty list if no such entities exist.
	 */
	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = NoResultException.class, transactionManager = "cfint31TransactionManager")
	public List<CFIntJpaMajorVersion> findBySubProjectIdx(@Param("subProjectId") CFLibDbKeyHash256 requiredSubProjectId) {
		return( cfint31MajorVersionRepository.findBySubProjectIdx(requiredSubProjectId));
	}

	/**
	 *	ICFIntMajorVersionBySubProjectIdxKey entity list finder convenience method for object-based access.
	 *
	 *		@param key The ICFIntMajorVersionBySubProjectIdxKey instance to use for the query arguments.
	 *
	 *		@return The found entity list, which may be empty.
	 */
	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = NoResultException.class, transactionManager = "cfint31TransactionManager")
	public List<CFIntJpaMajorVersion> findBySubProjectIdx(ICFIntMajorVersionBySubProjectIdxKey key) {
		return( cfint31MajorVersionRepository.findBySubProjectIdx(key.getRequiredSubProjectId()));
	}

	/**
	 *	Find an entity using the columns of the ICFIntMajorVersionByNameIdxKey as arguments.
	 *
	 *		@param requiredSubProjectId
	 *		@param requiredName
	 *
	 *		@return The found entity, or null if no such entity exists.
	 */
	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = NoResultException.class, transactionManager = "cfint31TransactionManager")
	public CFIntJpaMajorVersion findByNameIdx(@Param("subProjectId") CFLibDbKeyHash256 requiredSubProjectId,
		@Param("name") String requiredName) {
		return( cfint31MajorVersionRepository.findByNameIdx(requiredSubProjectId,
			requiredName));
	}

	/**
	 *	ICFIntMajorVersionByNameIdxKey entity finder convenience method for object-based access.
	 *
	 *		@param key The ICFIntMajorVersionByNameIdxKey instance to use for the query arguments.
	 *
	 *		@return The found entity, or null if no such entity exists.
	 */
	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = NoResultException.class, transactionManager = "cfint31TransactionManager")
	public CFIntJpaMajorVersion findByNameIdx(ICFIntMajorVersionByNameIdxKey key) {
		return( cfint31MajorVersionRepository.findByNameIdx(key.getRequiredSubProjectId(), key.getRequiredName()));
	}

	// CFIntMajorVersion specified lock-by-index methods

	/**
	 *	Argument-based lock database entity for compatibility with the current MSS code factory code base, uses @Transactional to acquire a JPA entity locks, which may or may not imply an actual database lock during the transaction.
	 *
	 *		@param requiredId
	 *
	 *		@return The locked entity, refreshed from the data store, or null if no such entity exists.
	 */
	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = NoResultException.class, transactionManager = "cfint31TransactionManager")
	public CFIntJpaMajorVersion lockByIdIdx(@Param("id") CFLibDbKeyHash256 requiredId) {
		return( cfint31MajorVersionRepository.lockByIdIdx(requiredId));
	}

	/**
	 *	Argument-based lock database instance for compatibility with the current MSS code factory code base, uses @Transactional to acquire a JPA entity locks, which may or may not imply an actual database lock during the transaction.
	 *
	 *		@param requiredTenantId
	 *
	 *		@return A list of locked entities, refreshed from the data store, or an empty list if no such entities exist.
	 */
	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = NoResultException.class, transactionManager = "cfint31TransactionManager")
	public List<CFIntJpaMajorVersion> lockByTenantIdx(@Param("tenantId") CFLibDbKeyHash256 requiredTenantId) {
		return( cfint31MajorVersionRepository.lockByTenantIdx(requiredTenantId));
	}

	/**
	 *	ICFIntMajorVersionByTenantIdxKey based lock method for object-based access.
	 *
	 *		@param key The key of the entity to be locked.
	 *
	 *		@return A list of locked entities, refreshed from the data store, or an empty list if no such entities exist.
	 */
	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = NoResultException.class, transactionManager = "cfint31TransactionManager")
	public List<CFIntJpaMajorVersion> lockByTenantIdx(ICFIntMajorVersionByTenantIdxKey key) {
		return( cfint31MajorVersionRepository.lockByTenantIdx(key.getRequiredTenantId()));
	}

	/**
	 *	Argument-based lock database instance for compatibility with the current MSS code factory code base, uses @Transactional to acquire a JPA entity locks, which may or may not imply an actual database lock during the transaction.
	 *
	 *		@param requiredSubProjectId
	 *
	 *		@return A list of locked entities, refreshed from the data store, or an empty list if no such entities exist.
	 */
	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = NoResultException.class, transactionManager = "cfint31TransactionManager")
	public List<CFIntJpaMajorVersion> lockBySubProjectIdx(@Param("subProjectId") CFLibDbKeyHash256 requiredSubProjectId) {
		return( cfint31MajorVersionRepository.lockBySubProjectIdx(requiredSubProjectId));
	}

	/**
	 *	ICFIntMajorVersionBySubProjectIdxKey based lock method for object-based access.
	 *
	 *		@param key The key of the entity to be locked.
	 *
	 *		@return A list of locked entities, refreshed from the data store, or an empty list if no such entities exist.
	 */
	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = NoResultException.class, transactionManager = "cfint31TransactionManager")
	public List<CFIntJpaMajorVersion> lockBySubProjectIdx(ICFIntMajorVersionBySubProjectIdxKey key) {
		return( cfint31MajorVersionRepository.lockBySubProjectIdx(key.getRequiredSubProjectId()));
	}

	/**
	 *	Argument-based lock database entity for compatibility with the current MSS code factory code base, uses @Transactional to acquire a JPA entity locks, which may or may not imply an actual database lock during the transaction.
	 *
	 *		@param requiredSubProjectId
	 *		@param requiredName
	 *
	 *		@return The locked entity, refreshed from the data store, or null if no such entity exists.
	 */
	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = NoResultException.class, transactionManager = "cfint31TransactionManager")
	public CFIntJpaMajorVersion lockByNameIdx(@Param("subProjectId") CFLibDbKeyHash256 requiredSubProjectId,
		@Param("name") String requiredName) {
		return( cfint31MajorVersionRepository.lockByNameIdx(requiredSubProjectId,
			requiredName));
	}

	/**
	 *	ICFIntMajorVersionByNameIdxKey based lock method for object-based access.
	 *
	 *		@param key The key of the entity to be locked.
	 *
	 *		@return The locked entity, refreshed from the data store, or null if no such entity exists.
	 */
	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = NoResultException.class, transactionManager = "cfint31TransactionManager")
	public CFIntJpaMajorVersion lockByNameIdx(ICFIntMajorVersionByNameIdxKey key) {
		return( cfint31MajorVersionRepository.lockByNameIdx(key.getRequiredSubProjectId(), key.getRequiredName()));
	}

	// CFIntMajorVersion specified delete-by-index methods

	/**
	 *	Argument-based delete entity for compatibility with the current MSS code factory code base, uses @Transactional to acquire a JPA entity lock, which may or may not imply an actual database lock during the transaction.
	 *
	 *		@param requiredId
	 */
	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = NoResultException.class, transactionManager = "cfint31TransactionManager")
	public void deleteByIdIdx(@Param("id") CFLibDbKeyHash256 requiredId) {
		cfint31MajorVersionRepository.deleteByIdIdx(requiredId);
	}

	/**
	 *	Argument-based delete entity for compatibility with the current MSS code factory code base, uses @Transactional to acquire a JPA entity lock, which may or may not imply an actual database lock during the transaction.
	 *
	 *		@param requiredTenantId
	 */
	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = NoResultException.class, transactionManager = "cfint31TransactionManager")
	public void deleteByTenantIdx(@Param("tenantId") CFLibDbKeyHash256 requiredTenantId) {
		cfint31MajorVersionRepository.deleteByTenantIdx(requiredTenantId);
	}

	/**
	 *	ICFIntMajorVersionByTenantIdxKey based lock method for object-based access.
	 *
	 *		@param key The ICFIntMajorVersionByTenantIdxKey of the entity to be locked.
	 */
	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = NoResultException.class, transactionManager = "cfint31TransactionManager")
	public void deleteByTenantIdx(ICFIntMajorVersionByTenantIdxKey key) {
		cfint31MajorVersionRepository.deleteByTenantIdx(key.getRequiredTenantId());
	}

	/**
	 *	Argument-based delete entity for compatibility with the current MSS code factory code base, uses @Transactional to acquire a JPA entity lock, which may or may not imply an actual database lock during the transaction.
	 *
	 *		@param requiredSubProjectId
	 */
	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = NoResultException.class, transactionManager = "cfint31TransactionManager")
	public void deleteBySubProjectIdx(@Param("subProjectId") CFLibDbKeyHash256 requiredSubProjectId) {
		cfint31MajorVersionRepository.deleteBySubProjectIdx(requiredSubProjectId);
	}

	/**
	 *	ICFIntMajorVersionBySubProjectIdxKey based lock method for object-based access.
	 *
	 *		@param key The ICFIntMajorVersionBySubProjectIdxKey of the entity to be locked.
	 */
	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = NoResultException.class, transactionManager = "cfint31TransactionManager")
	public void deleteBySubProjectIdx(ICFIntMajorVersionBySubProjectIdxKey key) {
		cfint31MajorVersionRepository.deleteBySubProjectIdx(key.getRequiredSubProjectId());
	}

	/**
	 *	Argument-based delete entity for compatibility with the current MSS code factory code base, uses @Transactional to acquire a JPA entity lock, which may or may not imply an actual database lock during the transaction.
	 *
	 *		@param requiredSubProjectId
	 *		@param requiredName
	 */
	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = NoResultException.class, transactionManager = "cfint31TransactionManager")
	public void deleteByNameIdx(@Param("subProjectId") CFLibDbKeyHash256 requiredSubProjectId,
		@Param("name") String requiredName) {
		cfint31MajorVersionRepository.deleteByNameIdx(requiredSubProjectId,
			requiredName);
	}

	/**
	 *	ICFIntMajorVersionByNameIdxKey based lock method for object-based access.
	 *
	 *		@param key The ICFIntMajorVersionByNameIdxKey of the entity to be locked.
	 */
	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = NoResultException.class, transactionManager = "cfint31TransactionManager")
	public void deleteByNameIdx(ICFIntMajorVersionByNameIdxKey key) {
		cfint31MajorVersionRepository.deleteByNameIdx(key.getRequiredSubProjectId(), key.getRequiredName());
	}

}
