// Description: Java 25 Spring JPA Service for MimeType

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
 *	Service for the CFIntMimeType entities defined in server.markhome.mcf.v3_1.cfint.cfint.jpa
 *	using the CFIntMimeTypeRepository to access them.
 */
@Service("cfint31JpaMimeTypeService")
public class CFIntJpaMimeTypeService {

	@Autowired
	@Qualifier("cfint31EntityManagerFactory")
	private LocalContainerEntityManagerFactoryBean cfint31EntityManagerFactory;

	@Autowired
	private CFIntJpaMimeTypeRepository cfint31MimeTypeRepository;

	/**
	 *	Create an entity, generating any database keys required along the way.
	 *
	 *		@param	data	The entity to be instantiated; must be a specific instance of CFIntJpaMimeType, not a subclass.
	 *
	 *		@return The updated/created entity.
	 */
	@Transactional(propagation = Propagation.REQUIRED,rollbackFor = NoResultException.class, transactionManager = "cfint31TransactionManager")
	public CFIntJpaMimeType create(CFIntJpaMimeType data) {
		final String S_ProcName = "create";
		if (data == null) {
			return( null );
		}
		int originalRequiredMimeTypeId = data.getRequiredMimeTypeId();
		boolean generatedRequiredMimeTypeId = false;
		if(data.getRequiredName() == null) {
			throw new CFLibNullArgumentException(getClass(),
				S_ProcName,
				0,
				"data.requiredName");
		}
		try {
			LocalDateTime now = LocalDateTime.now();
			data.setCreatedAt(now);
			data.setUpdatedAt(now);
			if(data.getPKey() != null && cfint31MimeTypeRepository.existsById((Integer)data.getPKey())) {
				return( (CFIntJpaMimeType)(cfint31MimeTypeRepository.findById((Integer)(data.getPKey())).get()));
			}
			return cfint31MimeTypeRepository.save(data);
		}
		catch(Exception ex) {
				if(generatedRequiredMimeTypeId) {
					data.setRequiredMimeTypeId(originalRequiredMimeTypeId);
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
	public CFIntJpaMimeType update(CFIntJpaMimeType data) {
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
		// Ensure the entity exists and that the revision matches
		CFIntJpaMimeType existing = cfint31MimeTypeRepository.findById((Integer)(data.getPKey()))
			.orElseThrow(() -> new CFLibCollisionDetectedException(getClass(), S_ProcName, data.getPKey()));
		if (existing.getRequiredRevision() != data.getRequiredRevision()) {
			throw new CFLibCollisionDetectedException(getClass(), S_ProcName, data.getPKey());
		}
		// Apply superior data relationships of CFIntMimeType to existing object
		// Apply data columns of CFIntMimeType to existing object
		existing.setRequiredName(data.getRequiredName());
		existing.setOptionalFileTypes(data.getOptionalFileTypes());
		// Update the audit columns
		data.setUpdatedAt(LocalDateTime.now());
		// Save the changes we've made
		return cfint31MimeTypeRepository.save(existing);
	}

	/**
	 *	Argument-based find database instance for compatibility with the current MSS code factory code base.
	 *
	 *		@param requiredMimeTypeId
	 *
	 *		@return The retrieved entity, or null if no such entity exists.
	 */
	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = NoResultException.class, transactionManager = "cfint31TransactionManager")
	public CFIntJpaMimeType find(@Param("mimeTypeId") int requiredMimeTypeId) {
		return( cfint31MimeTypeRepository.get(requiredMimeTypeId));
	}

	/**
	 *	Retrieve all entities from the repository
	 *
	 *		@return The list of retrieved entities, which may be empty
	 */
	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = NoResultException.class, transactionManager = "cfint31TransactionManager")
	public List<CFIntJpaMimeType> findAll() {
		return( cfint31MimeTypeRepository.findAll() );
	}

	// CFIntMimeType specified index finders

	/**
	 *	Find an entity using the columns of the ICFIntMimeTypeByUNameIdxKey as arguments.
	 *
	 *		@param requiredName
	 *
	 *		@return The found entity, or null if no such entity exists.
	 */
	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = NoResultException.class, transactionManager = "cfint31TransactionManager")
	public CFIntJpaMimeType findByUNameIdx(@Param("name") String requiredName) {
		return( cfint31MimeTypeRepository.findByUNameIdx(requiredName));
	}

	/**
	 *	ICFIntMimeTypeByUNameIdxKey entity finder convenience method for object-based access.
	 *
	 *		@param key The ICFIntMimeTypeByUNameIdxKey instance to use for the query arguments.
	 *
	 *		@return The found entity, or null if no such entity exists.
	 */
	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = NoResultException.class, transactionManager = "cfint31TransactionManager")
	public CFIntJpaMimeType findByUNameIdx(ICFIntMimeTypeByUNameIdxKey key) {
		return( cfint31MimeTypeRepository.findByUNameIdx(key.getRequiredName()));
	}

	// CFIntMimeType specified lock-by-index methods

	/**
	 *	Argument-based lock database entity for compatibility with the current MSS code factory code base, uses @Transactional to acquire a JPA entity locks, which may or may not imply an actual database lock during the transaction.
	 *
	 *		@param requiredMimeTypeId
	 *
	 *		@return The locked entity, refreshed from the data store, or null if no such entity exists.
	 */
	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = NoResultException.class, transactionManager = "cfint31TransactionManager")
	public CFIntJpaMimeType lockByIdIdx(@Param("mimeTypeId") int requiredMimeTypeId) {
		return( cfint31MimeTypeRepository.lockByIdIdx(requiredMimeTypeId));
	}

	/**
	 *	Argument-based lock database entity for compatibility with the current MSS code factory code base, uses @Transactional to acquire a JPA entity locks, which may or may not imply an actual database lock during the transaction.
	 *
	 *		@param requiredName
	 *
	 *		@return The locked entity, refreshed from the data store, or null if no such entity exists.
	 */
	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = NoResultException.class, transactionManager = "cfint31TransactionManager")
	public CFIntJpaMimeType lockByUNameIdx(@Param("name") String requiredName) {
		return( cfint31MimeTypeRepository.lockByUNameIdx(requiredName));
	}

	/**
	 *	ICFIntMimeTypeByUNameIdxKey based lock method for object-based access.
	 *
	 *		@param key The key of the entity to be locked.
	 *
	 *		@return The locked entity, refreshed from the data store, or null if no such entity exists.
	 */
	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = NoResultException.class, transactionManager = "cfint31TransactionManager")
	public CFIntJpaMimeType lockByUNameIdx(ICFIntMimeTypeByUNameIdxKey key) {
		return( cfint31MimeTypeRepository.lockByUNameIdx(key.getRequiredName()));
	}

	// CFIntMimeType specified delete-by-index methods

	/**
	 *	Argument-based delete entity for compatibility with the current MSS code factory code base, uses @Transactional to acquire a JPA entity lock, which may or may not imply an actual database lock during the transaction.
	 *
	 *		@param requiredMimeTypeId
	 */
	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = NoResultException.class, transactionManager = "cfint31TransactionManager")
	public void deleteByIdIdx(@Param("mimeTypeId") int requiredMimeTypeId) {
		cfint31MimeTypeRepository.deleteByIdIdx(requiredMimeTypeId);
	}

	/**
	 *	Argument-based delete entity for compatibility with the current MSS code factory code base, uses @Transactional to acquire a JPA entity lock, which may or may not imply an actual database lock during the transaction.
	 *
	 *		@param requiredName
	 */
	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = NoResultException.class, transactionManager = "cfint31TransactionManager")
	public void deleteByUNameIdx(@Param("name") String requiredName) {
		cfint31MimeTypeRepository.deleteByUNameIdx(requiredName);
	}

	/**
	 *	ICFIntMimeTypeByUNameIdxKey based lock method for object-based access.
	 *
	 *		@param key The ICFIntMimeTypeByUNameIdxKey of the entity to be locked.
	 */
	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = NoResultException.class, transactionManager = "cfint31TransactionManager")
	public void deleteByUNameIdx(ICFIntMimeTypeByUNameIdxKey key) {
		cfint31MimeTypeRepository.deleteByUNameIdx(key.getRequiredName());
	}
}
