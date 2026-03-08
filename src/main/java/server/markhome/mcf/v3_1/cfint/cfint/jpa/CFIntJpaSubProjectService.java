// Description: Java 25 Spring JPA Service for SubProject

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
 *	Service for the CFIntSubProject entities defined in server.markhome.mcf.v3_1.cfint.cfint.jpa
 *	using the CFIntSubProjectRepository to access them.
 */
@Service("cfint31JpaSubProjectService")
public class CFIntJpaSubProjectService {

	@Autowired
	@Qualifier("cfint31EntityManagerFactory")
	private LocalContainerEntityManagerFactoryBean cfint31EntityManagerFactory;

	@Autowired
	private CFIntJpaSubProjectRepository cfint31SubProjectRepository;

	/**
	 *	Create an entity, generating any database keys required along the way.
	 *
	 *		@param	data	The entity to be instantiated; must be a specific instance of CFIntJpaSubProject, not a subclass.
	 *
	 *		@return The updated/created entity.
	 */
	@Transactional(propagation = Propagation.REQUIRED,rollbackFor = NoResultException.class, transactionManager = "cfint31TransactionManager")
	public CFIntJpaSubProject create(CFIntJpaSubProject data) {
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
		if(data.getRequiredTopProjectId() == null || data.getRequiredTopProjectId().isNull()) {
			throw new CFLibNullArgumentException(getClass(),
				S_ProcName,
				0,
				"data.requiredTopProjectId");
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
			if(data.getPKey() != null && cfint31SubProjectRepository.existsById((CFLibDbKeyHash256)data.getPKey())) {
				return( (CFIntJpaSubProject)(cfint31SubProjectRepository.findById((CFLibDbKeyHash256)(data.getPKey())).get()));
			}
			return cfint31SubProjectRepository.save(data);
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
	public CFIntJpaSubProject update(CFIntJpaSubProject data) {
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
		if(data.getRequiredTopProjectId() == null || data.getRequiredTopProjectId().isNull()) {
			throw new CFLibNullArgumentException(getClass(),
				S_ProcName,
				0,
				"data.requiredTopProjectId");
		}
		if(data.getRequiredName() == null) {
			throw new CFLibNullArgumentException(getClass(),
				S_ProcName,
				0,
				"data.requiredName");
		}
		// Ensure the entity exists and that the revision matches
		CFIntJpaSubProject existing = cfint31SubProjectRepository.findById((CFLibDbKeyHash256)(data.getPKey()))
			.orElseThrow(() -> new CFLibCollisionDetectedException(getClass(), S_ProcName, data.getPKey()));
		if (existing.getRequiredRevision() != data.getRequiredRevision()) {
			throw new CFLibCollisionDetectedException(getClass(), S_ProcName, data.getPKey());
		}
		// Apply superior data relationships of CFIntSubProject to existing object
		existing.setRequiredOwnerTenant(data.getRequiredTenantId());
		existing.setRequiredContainerParentTPrj(data.getRequiredContainerParentTPrj());
		// Apply data columns of CFIntSubProject to existing object
		existing.setRequiredName(data.getRequiredName());
		existing.setOptionalDescription(data.getOptionalDescription());
		// Update the audit columns
		data.setUpdatedAt(LocalDateTime.now());
		// Save the changes we've made
		return cfint31SubProjectRepository.save(existing);
	}

	/**
	 *	Argument-based find database instance for compatibility with the current MSS code factory code base.
	 *
	 *		@param requiredId
	 *
	 *		@return The retrieved entity, or null if no such entity exists.
	 */
	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = NoResultException.class, transactionManager = "cfint31TransactionManager")
	public CFIntJpaSubProject find(@Param("id") CFLibDbKeyHash256 requiredId) {
		return( cfint31SubProjectRepository.get(requiredId));
	}

	/**
	 *	Retrieve all entities from the repository
	 *
	 *		@return The list of retrieved entities, which may be empty
	 */
	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = NoResultException.class, transactionManager = "cfint31TransactionManager")
	public List<CFIntJpaSubProject> findAll() {
		return( cfint31SubProjectRepository.findAll() );
	}

	// CFIntSubProject specified index finders

	/**
	 *	Find zero or more entities into a List using the columns of the ICFIntSubProjectByTenantIdxKey as arguments.
	 *
	 *		@param requiredTenantId
	 *
	 *		@return List&lt;CFIntJpaSubProject&gt; of the found entities, or an empty list if no such entities exist.
	 */
	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = NoResultException.class, transactionManager = "cfint31TransactionManager")
	public List<CFIntJpaSubProject> findByTenantIdx(@Param("tenantId") CFLibDbKeyHash256 requiredTenantId) {
		return( cfint31SubProjectRepository.findByTenantIdx(requiredTenantId));
	}

	/**
	 *	ICFIntSubProjectByTenantIdxKey entity list finder convenience method for object-based access.
	 *
	 *		@param key The ICFIntSubProjectByTenantIdxKey instance to use for the query arguments.
	 *
	 *		@return The found entity list, which may be empty.
	 */
	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = NoResultException.class, transactionManager = "cfint31TransactionManager")
	public List<CFIntJpaSubProject> findByTenantIdx(ICFIntSubProjectByTenantIdxKey key) {
		return( cfint31SubProjectRepository.findByTenantIdx(key.getRequiredTenantId()));
	}

	/**
	 *	Find zero or more entities into a List using the columns of the ICFIntSubProjectByTopProjectIdxKey as arguments.
	 *
	 *		@param requiredTopProjectId
	 *
	 *		@return List&lt;CFIntJpaSubProject&gt; of the found entities, or an empty list if no such entities exist.
	 */
	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = NoResultException.class, transactionManager = "cfint31TransactionManager")
	public List<CFIntJpaSubProject> findByTopProjectIdx(@Param("topProjectId") CFLibDbKeyHash256 requiredTopProjectId) {
		return( cfint31SubProjectRepository.findByTopProjectIdx(requiredTopProjectId));
	}

	/**
	 *	ICFIntSubProjectByTopProjectIdxKey entity list finder convenience method for object-based access.
	 *
	 *		@param key The ICFIntSubProjectByTopProjectIdxKey instance to use for the query arguments.
	 *
	 *		@return The found entity list, which may be empty.
	 */
	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = NoResultException.class, transactionManager = "cfint31TransactionManager")
	public List<CFIntJpaSubProject> findByTopProjectIdx(ICFIntSubProjectByTopProjectIdxKey key) {
		return( cfint31SubProjectRepository.findByTopProjectIdx(key.getRequiredTopProjectId()));
	}

	/**
	 *	Find an entity using the columns of the ICFIntSubProjectByNameIdxKey as arguments.
	 *
	 *		@param requiredTopProjectId
	 *		@param requiredName
	 *
	 *		@return The found entity, or null if no such entity exists.
	 */
	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = NoResultException.class, transactionManager = "cfint31TransactionManager")
	public CFIntJpaSubProject findByNameIdx(@Param("topProjectId") CFLibDbKeyHash256 requiredTopProjectId,
		@Param("name") String requiredName) {
		return( cfint31SubProjectRepository.findByNameIdx(requiredTopProjectId,
			requiredName));
	}

	/**
	 *	ICFIntSubProjectByNameIdxKey entity finder convenience method for object-based access.
	 *
	 *		@param key The ICFIntSubProjectByNameIdxKey instance to use for the query arguments.
	 *
	 *		@return The found entity, or null if no such entity exists.
	 */
	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = NoResultException.class, transactionManager = "cfint31TransactionManager")
	public CFIntJpaSubProject findByNameIdx(ICFIntSubProjectByNameIdxKey key) {
		return( cfint31SubProjectRepository.findByNameIdx(key.getRequiredTopProjectId(), key.getRequiredName()));
	}

	// CFIntSubProject specified lock-by-index methods

	/**
	 *	Argument-based lock database entity for compatibility with the current MSS code factory code base, uses @Transactional to acquire a JPA entity locks, which may or may not imply an actual database lock during the transaction.
	 *
	 *		@param requiredId
	 *
	 *		@return The locked entity, refreshed from the data store, or null if no such entity exists.
	 */
	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = NoResultException.class, transactionManager = "cfint31TransactionManager")
	public CFIntJpaSubProject lockByIdIdx(@Param("id") CFLibDbKeyHash256 requiredId) {
		return( cfint31SubProjectRepository.lockByIdIdx(requiredId));
	}

	/**
	 *	Argument-based lock database instance for compatibility with the current MSS code factory code base, uses @Transactional to acquire a JPA entity locks, which may or may not imply an actual database lock during the transaction.
	 *
	 *		@param requiredTenantId
	 *
	 *		@return A list of locked entities, refreshed from the data store, or an empty list if no such entities exist.
	 */
	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = NoResultException.class, transactionManager = "cfint31TransactionManager")
	public List<CFIntJpaSubProject> lockByTenantIdx(@Param("tenantId") CFLibDbKeyHash256 requiredTenantId) {
		return( cfint31SubProjectRepository.lockByTenantIdx(requiredTenantId));
	}

	/**
	 *	ICFIntSubProjectByTenantIdxKey based lock method for object-based access.
	 *
	 *		@param key The key of the entity to be locked.
	 *
	 *		@return A list of locked entities, refreshed from the data store, or an empty list if no such entities exist.
	 */
	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = NoResultException.class, transactionManager = "cfint31TransactionManager")
	public List<CFIntJpaSubProject> lockByTenantIdx(ICFIntSubProjectByTenantIdxKey key) {
		return( cfint31SubProjectRepository.lockByTenantIdx(key.getRequiredTenantId()));
	}

	/**
	 *	Argument-based lock database instance for compatibility with the current MSS code factory code base, uses @Transactional to acquire a JPA entity locks, which may or may not imply an actual database lock during the transaction.
	 *
	 *		@param requiredTopProjectId
	 *
	 *		@return A list of locked entities, refreshed from the data store, or an empty list if no such entities exist.
	 */
	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = NoResultException.class, transactionManager = "cfint31TransactionManager")
	public List<CFIntJpaSubProject> lockByTopProjectIdx(@Param("topProjectId") CFLibDbKeyHash256 requiredTopProjectId) {
		return( cfint31SubProjectRepository.lockByTopProjectIdx(requiredTopProjectId));
	}

	/**
	 *	ICFIntSubProjectByTopProjectIdxKey based lock method for object-based access.
	 *
	 *		@param key The key of the entity to be locked.
	 *
	 *		@return A list of locked entities, refreshed from the data store, or an empty list if no such entities exist.
	 */
	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = NoResultException.class, transactionManager = "cfint31TransactionManager")
	public List<CFIntJpaSubProject> lockByTopProjectIdx(ICFIntSubProjectByTopProjectIdxKey key) {
		return( cfint31SubProjectRepository.lockByTopProjectIdx(key.getRequiredTopProjectId()));
	}

	/**
	 *	Argument-based lock database entity for compatibility with the current MSS code factory code base, uses @Transactional to acquire a JPA entity locks, which may or may not imply an actual database lock during the transaction.
	 *
	 *		@param requiredTopProjectId
	 *		@param requiredName
	 *
	 *		@return The locked entity, refreshed from the data store, or null if no such entity exists.
	 */
	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = NoResultException.class, transactionManager = "cfint31TransactionManager")
	public CFIntJpaSubProject lockByNameIdx(@Param("topProjectId") CFLibDbKeyHash256 requiredTopProjectId,
		@Param("name") String requiredName) {
		return( cfint31SubProjectRepository.lockByNameIdx(requiredTopProjectId,
			requiredName));
	}

	/**
	 *	ICFIntSubProjectByNameIdxKey based lock method for object-based access.
	 *
	 *		@param key The key of the entity to be locked.
	 *
	 *		@return The locked entity, refreshed from the data store, or null if no such entity exists.
	 */
	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = NoResultException.class, transactionManager = "cfint31TransactionManager")
	public CFIntJpaSubProject lockByNameIdx(ICFIntSubProjectByNameIdxKey key) {
		return( cfint31SubProjectRepository.lockByNameIdx(key.getRequiredTopProjectId(), key.getRequiredName()));
	}

	// CFIntSubProject specified delete-by-index methods

	/**
	 *	Argument-based delete entity for compatibility with the current MSS code factory code base, uses @Transactional to acquire a JPA entity lock, which may or may not imply an actual database lock during the transaction.
	 *
	 *		@param requiredId
	 */
	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = NoResultException.class, transactionManager = "cfint31TransactionManager")
	public void deleteByIdIdx(@Param("id") CFLibDbKeyHash256 requiredId) {
		cfint31SubProjectRepository.deleteByIdIdx(requiredId);
	}

	/**
	 *	Argument-based delete entity for compatibility with the current MSS code factory code base, uses @Transactional to acquire a JPA entity lock, which may or may not imply an actual database lock during the transaction.
	 *
	 *		@param requiredTenantId
	 */
	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = NoResultException.class, transactionManager = "cfint31TransactionManager")
	public void deleteByTenantIdx(@Param("tenantId") CFLibDbKeyHash256 requiredTenantId) {
		cfint31SubProjectRepository.deleteByTenantIdx(requiredTenantId);
	}

	/**
	 *	ICFIntSubProjectByTenantIdxKey based lock method for object-based access.
	 *
	 *		@param key The ICFIntSubProjectByTenantIdxKey of the entity to be locked.
	 */
	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = NoResultException.class, transactionManager = "cfint31TransactionManager")
	public void deleteByTenantIdx(ICFIntSubProjectByTenantIdxKey key) {
		cfint31SubProjectRepository.deleteByTenantIdx(key.getRequiredTenantId());
	}

	/**
	 *	Argument-based delete entity for compatibility with the current MSS code factory code base, uses @Transactional to acquire a JPA entity lock, which may or may not imply an actual database lock during the transaction.
	 *
	 *		@param requiredTopProjectId
	 */
	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = NoResultException.class, transactionManager = "cfint31TransactionManager")
	public void deleteByTopProjectIdx(@Param("topProjectId") CFLibDbKeyHash256 requiredTopProjectId) {
		cfint31SubProjectRepository.deleteByTopProjectIdx(requiredTopProjectId);
	}

	/**
	 *	ICFIntSubProjectByTopProjectIdxKey based lock method for object-based access.
	 *
	 *		@param key The ICFIntSubProjectByTopProjectIdxKey of the entity to be locked.
	 */
	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = NoResultException.class, transactionManager = "cfint31TransactionManager")
	public void deleteByTopProjectIdx(ICFIntSubProjectByTopProjectIdxKey key) {
		cfint31SubProjectRepository.deleteByTopProjectIdx(key.getRequiredTopProjectId());
	}

	/**
	 *	Argument-based delete entity for compatibility with the current MSS code factory code base, uses @Transactional to acquire a JPA entity lock, which may or may not imply an actual database lock during the transaction.
	 *
	 *		@param requiredTopProjectId
	 *		@param requiredName
	 */
	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = NoResultException.class, transactionManager = "cfint31TransactionManager")
	public void deleteByNameIdx(@Param("topProjectId") CFLibDbKeyHash256 requiredTopProjectId,
		@Param("name") String requiredName) {
		cfint31SubProjectRepository.deleteByNameIdx(requiredTopProjectId,
			requiredName);
	}

	/**
	 *	ICFIntSubProjectByNameIdxKey based lock method for object-based access.
	 *
	 *		@param key The ICFIntSubProjectByNameIdxKey of the entity to be locked.
	 */
	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = NoResultException.class, transactionManager = "cfint31TransactionManager")
	public void deleteByNameIdx(ICFIntSubProjectByNameIdxKey key) {
		cfint31SubProjectRepository.deleteByNameIdx(key.getRequiredTopProjectId(), key.getRequiredName());
	}
}
