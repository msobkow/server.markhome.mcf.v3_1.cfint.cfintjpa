// Description: Java 25 Spring JPA Service for License

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
 *	Service for the CFIntLicense entities defined in server.markhome.mcf.v3_1.cfint.cfint.jpa
 *	using the CFIntLicenseRepository to access them.
 */
@Service("cfint31JpaLicenseService")
public class CFIntJpaLicenseService {

	@Autowired
	@Qualifier("cfint31EntityManagerFactory")
	private LocalContainerEntityManagerFactoryBean cfint31EntityManagerFactory;

	@Autowired
	private CFIntJpaLicenseRepository cfint31LicenseRepository;

	/**
	 *	Create an entity, generating any database keys required along the way.
	 *
	 *		@param	data	The entity to be instantiated; must be a specific instance of CFIntJpaLicense, not a subclass.
	 *
	 *		@return The updated/created entity.
	 */
	@Transactional(propagation = Propagation.REQUIRED,rollbackFor = NoResultException.class, transactionManager = "cfint31TransactionManager")
	public CFIntJpaLicense create(CFIntJpaLicense data) {
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
		if(data.getRequiredTopDomainId() == null || data.getRequiredTopDomainId().isNull()) {
			throw new CFLibNullArgumentException(getClass(),
				S_ProcName,
				0,
				"data.requiredTopDomainId");
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
			if(data.getPKey() != null && cfint31LicenseRepository.existsById((CFLibDbKeyHash256)data.getPKey())) {
				return( (CFIntJpaLicense)(cfint31LicenseRepository.findById((CFLibDbKeyHash256)(data.getPKey())).get()));
			}
			return cfint31LicenseRepository.save(data);
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
	public CFIntJpaLicense update(CFIntJpaLicense data) {
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
		if(data.getRequiredTopDomainId() == null || data.getRequiredTopDomainId().isNull()) {
			throw new CFLibNullArgumentException(getClass(),
				S_ProcName,
				0,
				"data.requiredTopDomainId");
		}
		if(data.getRequiredName() == null) {
			throw new CFLibNullArgumentException(getClass(),
				S_ProcName,
				0,
				"data.requiredName");
		}
		// Ensure the entity exists and that the revision matches
		CFIntJpaLicense existing = cfint31LicenseRepository.findById((CFLibDbKeyHash256)(data.getPKey()))
			.orElseThrow(() -> new CFLibCollisionDetectedException(getClass(), S_ProcName, data.getPKey()));
		if (existing.getRequiredRevision() != data.getRequiredRevision()) {
			throw new CFLibCollisionDetectedException(getClass(), S_ProcName, data.getPKey());
		}
		// Apply superior data relationships of CFIntLicense to existing object
		existing.setRequiredOwnerTenant(data.getRequiredTenantId());
		existing.setRequiredContainerTopDomain(data.getRequiredContainerTopDomain());
		// Apply data columns of CFIntLicense to existing object
		existing.setRequiredName(data.getRequiredName());
		existing.setOptionalDescription(data.getOptionalDescription());
		existing.setOptionalEmbeddedText(data.getOptionalEmbeddedText());
		existing.setOptionalFullText(data.getOptionalFullText());
		// Save the changes we've made
		return cfint31LicenseRepository.save(existing);
	}

	/**
	 *	Argument-based find database instance for compatibility with the current MSS code factory code base.
	 *
	 *		@param requiredId
	 *
	 *		@return The retrieved entity, or null if no such entity exists.
	 */
	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = NoResultException.class, transactionManager = "cfint31TransactionManager")
	public CFIntJpaLicense find(@Param("id") CFLibDbKeyHash256 requiredId) {
		return( cfint31LicenseRepository.get(requiredId));
	}

	/**
	 *	Retrieve all entities from the repository
	 *
	 *		@return The list of retrieved entities, which may be empty
	 */
	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = NoResultException.class, transactionManager = "cfint31TransactionManager")
	public List<CFIntJpaLicense> findAll() {
		return( cfint31LicenseRepository.findAll() );
	}

	// CFIntLicense specified index finders

	/**
	 *	Find zero or more entities into a List using the columns of the ICFIntLicenseByLicnTenantIdxKey as arguments.
	 *
	 *		@param requiredTenantId
	 *
	 *		@return List&lt;CFIntJpaLicense&gt; of the found entities, or an empty list if no such entities exist.
	 */
	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = NoResultException.class, transactionManager = "cfint31TransactionManager")
	public List<CFIntJpaLicense> findByLicnTenantIdx(@Param("tenantId") CFLibDbKeyHash256 requiredTenantId) {
		return( cfint31LicenseRepository.findByLicnTenantIdx(requiredTenantId));
	}

	/**
	 *	ICFIntLicenseByLicnTenantIdxKey entity list finder convenience method for object-based access.
	 *
	 *		@param key The ICFIntLicenseByLicnTenantIdxKey instance to use for the query arguments.
	 *
	 *		@return The found entity list, which may be empty.
	 */
	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = NoResultException.class, transactionManager = "cfint31TransactionManager")
	public List<CFIntJpaLicense> findByLicnTenantIdx(ICFIntLicenseByLicnTenantIdxKey key) {
		return( cfint31LicenseRepository.findByLicnTenantIdx(key.getRequiredTenantId()));
	}

	/**
	 *	Find zero or more entities into a List using the columns of the ICFIntLicenseByDomainIdxKey as arguments.
	 *
	 *		@param requiredTopDomainId
	 *
	 *		@return List&lt;CFIntJpaLicense&gt; of the found entities, or an empty list if no such entities exist.
	 */
	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = NoResultException.class, transactionManager = "cfint31TransactionManager")
	public List<CFIntJpaLicense> findByDomainIdx(@Param("topDomainId") CFLibDbKeyHash256 requiredTopDomainId) {
		return( cfint31LicenseRepository.findByDomainIdx(requiredTopDomainId));
	}

	/**
	 *	ICFIntLicenseByDomainIdxKey entity list finder convenience method for object-based access.
	 *
	 *		@param key The ICFIntLicenseByDomainIdxKey instance to use for the query arguments.
	 *
	 *		@return The found entity list, which may be empty.
	 */
	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = NoResultException.class, transactionManager = "cfint31TransactionManager")
	public List<CFIntJpaLicense> findByDomainIdx(ICFIntLicenseByDomainIdxKey key) {
		return( cfint31LicenseRepository.findByDomainIdx(key.getRequiredTopDomainId()));
	}

	/**
	 *	Find an entity using the columns of the ICFIntLicenseByUNameIdxKey as arguments.
	 *
	 *		@param requiredTopDomainId
	 *		@param requiredName
	 *
	 *		@return The found entity, or null if no such entity exists.
	 */
	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = NoResultException.class, transactionManager = "cfint31TransactionManager")
	public CFIntJpaLicense findByUNameIdx(@Param("topDomainId") CFLibDbKeyHash256 requiredTopDomainId,
		@Param("name") String requiredName) {
		return( cfint31LicenseRepository.findByUNameIdx(requiredTopDomainId,
			requiredName));
	}

	/**
	 *	ICFIntLicenseByUNameIdxKey entity finder convenience method for object-based access.
	 *
	 *		@param key The ICFIntLicenseByUNameIdxKey instance to use for the query arguments.
	 *
	 *		@return The found entity, or null if no such entity exists.
	 */
	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = NoResultException.class, transactionManager = "cfint31TransactionManager")
	public CFIntJpaLicense findByUNameIdx(ICFIntLicenseByUNameIdxKey key) {
		return( cfint31LicenseRepository.findByUNameIdx(key.getRequiredTopDomainId(), key.getRequiredName()));
	}

	// CFIntLicense specified lock-by-index methods

	/**
	 *	Argument-based lock database entity for compatibility with the current MSS code factory code base, uses @Transactional to acquire a JPA entity locks, which may or may not imply an actual database lock during the transaction.
	 *
	 *		@param requiredId
	 *
	 *		@return The locked entity, refreshed from the data store, or null if no such entity exists.
	 */
	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = NoResultException.class, transactionManager = "cfint31TransactionManager")
	public CFIntJpaLicense lockByIdIdx(@Param("id") CFLibDbKeyHash256 requiredId) {
		return( cfint31LicenseRepository.lockByIdIdx(requiredId));
	}

	/**
	 *	Argument-based lock database instance for compatibility with the current MSS code factory code base, uses @Transactional to acquire a JPA entity locks, which may or may not imply an actual database lock during the transaction.
	 *
	 *		@param requiredTenantId
	 *
	 *		@return A list of locked entities, refreshed from the data store, or an empty list if no such entities exist.
	 */
	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = NoResultException.class, transactionManager = "cfint31TransactionManager")
	public List<CFIntJpaLicense> lockByLicnTenantIdx(@Param("tenantId") CFLibDbKeyHash256 requiredTenantId) {
		return( cfint31LicenseRepository.lockByLicnTenantIdx(requiredTenantId));
	}

	/**
	 *	ICFIntLicenseByLicnTenantIdxKey based lock method for object-based access.
	 *
	 *		@param key The key of the entity to be locked.
	 *
	 *		@return A list of locked entities, refreshed from the data store, or an empty list if no such entities exist.
	 */
	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = NoResultException.class, transactionManager = "cfint31TransactionManager")
	public List<CFIntJpaLicense> lockByLicnTenantIdx(ICFIntLicenseByLicnTenantIdxKey key) {
		return( cfint31LicenseRepository.lockByLicnTenantIdx(key.getRequiredTenantId()));
	}

	/**
	 *	Argument-based lock database instance for compatibility with the current MSS code factory code base, uses @Transactional to acquire a JPA entity locks, which may or may not imply an actual database lock during the transaction.
	 *
	 *		@param requiredTopDomainId
	 *
	 *		@return A list of locked entities, refreshed from the data store, or an empty list if no such entities exist.
	 */
	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = NoResultException.class, transactionManager = "cfint31TransactionManager")
	public List<CFIntJpaLicense> lockByDomainIdx(@Param("topDomainId") CFLibDbKeyHash256 requiredTopDomainId) {
		return( cfint31LicenseRepository.lockByDomainIdx(requiredTopDomainId));
	}

	/**
	 *	ICFIntLicenseByDomainIdxKey based lock method for object-based access.
	 *
	 *		@param key The key of the entity to be locked.
	 *
	 *		@return A list of locked entities, refreshed from the data store, or an empty list if no such entities exist.
	 */
	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = NoResultException.class, transactionManager = "cfint31TransactionManager")
	public List<CFIntJpaLicense> lockByDomainIdx(ICFIntLicenseByDomainIdxKey key) {
		return( cfint31LicenseRepository.lockByDomainIdx(key.getRequiredTopDomainId()));
	}

	/**
	 *	Argument-based lock database entity for compatibility with the current MSS code factory code base, uses @Transactional to acquire a JPA entity locks, which may or may not imply an actual database lock during the transaction.
	 *
	 *		@param requiredTopDomainId
	 *		@param requiredName
	 *
	 *		@return The locked entity, refreshed from the data store, or null if no such entity exists.
	 */
	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = NoResultException.class, transactionManager = "cfint31TransactionManager")
	public CFIntJpaLicense lockByUNameIdx(@Param("topDomainId") CFLibDbKeyHash256 requiredTopDomainId,
		@Param("name") String requiredName) {
		return( cfint31LicenseRepository.lockByUNameIdx(requiredTopDomainId,
			requiredName));
	}

	/**
	 *	ICFIntLicenseByUNameIdxKey based lock method for object-based access.
	 *
	 *		@param key The key of the entity to be locked.
	 *
	 *		@return The locked entity, refreshed from the data store, or null if no such entity exists.
	 */
	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = NoResultException.class, transactionManager = "cfint31TransactionManager")
	public CFIntJpaLicense lockByUNameIdx(ICFIntLicenseByUNameIdxKey key) {
		return( cfint31LicenseRepository.lockByUNameIdx(key.getRequiredTopDomainId(), key.getRequiredName()));
	}

	// CFIntLicense specified delete-by-index methods

	/**
	 *	Argument-based delete entity for compatibility with the current MSS code factory code base, uses @Transactional to acquire a JPA entity lock, which may or may not imply an actual database lock during the transaction.
	 *
	 *		@param requiredId
	 */
	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = NoResultException.class, transactionManager = "cfint31TransactionManager")
	public void deleteByIdIdx(@Param("id") CFLibDbKeyHash256 requiredId) {
		cfint31LicenseRepository.deleteByIdIdx(requiredId);
	}

	/**
	 *	Argument-based delete entity for compatibility with the current MSS code factory code base, uses @Transactional to acquire a JPA entity lock, which may or may not imply an actual database lock during the transaction.
	 *
	 *		@param requiredTenantId
	 */
	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = NoResultException.class, transactionManager = "cfint31TransactionManager")
	public void deleteByLicnTenantIdx(@Param("tenantId") CFLibDbKeyHash256 requiredTenantId) {
		cfint31LicenseRepository.deleteByLicnTenantIdx(requiredTenantId);
	}

	/**
	 *	ICFIntLicenseByLicnTenantIdxKey based lock method for object-based access.
	 *
	 *		@param key The ICFIntLicenseByLicnTenantIdxKey of the entity to be locked.
	 */
	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = NoResultException.class, transactionManager = "cfint31TransactionManager")
	public void deleteByLicnTenantIdx(ICFIntLicenseByLicnTenantIdxKey key) {
		cfint31LicenseRepository.deleteByLicnTenantIdx(key.getRequiredTenantId());
	}

	/**
	 *	Argument-based delete entity for compatibility with the current MSS code factory code base, uses @Transactional to acquire a JPA entity lock, which may or may not imply an actual database lock during the transaction.
	 *
	 *		@param requiredTopDomainId
	 */
	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = NoResultException.class, transactionManager = "cfint31TransactionManager")
	public void deleteByDomainIdx(@Param("topDomainId") CFLibDbKeyHash256 requiredTopDomainId) {
		cfint31LicenseRepository.deleteByDomainIdx(requiredTopDomainId);
	}

	/**
	 *	ICFIntLicenseByDomainIdxKey based lock method for object-based access.
	 *
	 *		@param key The ICFIntLicenseByDomainIdxKey of the entity to be locked.
	 */
	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = NoResultException.class, transactionManager = "cfint31TransactionManager")
	public void deleteByDomainIdx(ICFIntLicenseByDomainIdxKey key) {
		cfint31LicenseRepository.deleteByDomainIdx(key.getRequiredTopDomainId());
	}

	/**
	 *	Argument-based delete entity for compatibility with the current MSS code factory code base, uses @Transactional to acquire a JPA entity lock, which may or may not imply an actual database lock during the transaction.
	 *
	 *		@param requiredTopDomainId
	 *		@param requiredName
	 */
	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = NoResultException.class, transactionManager = "cfint31TransactionManager")
	public void deleteByUNameIdx(@Param("topDomainId") CFLibDbKeyHash256 requiredTopDomainId,
		@Param("name") String requiredName) {
		cfint31LicenseRepository.deleteByUNameIdx(requiredTopDomainId,
			requiredName);
	}

	/**
	 *	ICFIntLicenseByUNameIdxKey based lock method for object-based access.
	 *
	 *		@param key The ICFIntLicenseByUNameIdxKey of the entity to be locked.
	 */
	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = NoResultException.class, transactionManager = "cfint31TransactionManager")
	public void deleteByUNameIdx(ICFIntLicenseByUNameIdxKey key) {
		cfint31LicenseRepository.deleteByUNameIdx(key.getRequiredTopDomainId(), key.getRequiredName());
	}
}
