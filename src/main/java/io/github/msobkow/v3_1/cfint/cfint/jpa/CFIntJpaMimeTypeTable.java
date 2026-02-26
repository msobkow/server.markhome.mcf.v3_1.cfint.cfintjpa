
// Description: Java 25 DbIO implementation for MimeType.

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

import java.lang.reflect.*;
import java.net.*;
import java.rmi.*;
import java.sql.*;
import java.text.*;
import java.time.*;
import java.util.*;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.text.StringEscapeUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

import io.github.msobkow.v3_1.cflib.*;
import io.github.msobkow.v3_1.cflib.dbutil.*;
import io.github.msobkow.v3_1.cfsec.cfsec.*;
import io.github.msobkow.v3_1.cfint.cfint.*;
import io.github.msobkow.v3_1.cfsec.cfsecobj.*;
import io.github.msobkow.v3_1.cfint.cfintobj.*;
import io.github.msobkow.v3_1.cfint.cfint.jpa.CFIntJpaHooksSchema;

/*
 *	CFIntJpaMimeTypeTable database implementation for MimeType
 */
public class CFIntJpaMimeTypeTable implements ICFIntMimeTypeTable
{
	protected CFIntJpaSchema schema;


	public CFIntJpaMimeTypeTable(ICFIntSchema schema) {
		if( schema == null ) {
			throw new CFLibNullArgumentException(getClass(), "constructor", 1, "schema" );
		}
		if (schema instanceof CFIntJpaSchema) {
			this.schema = (CFIntJpaSchema)schema;
		}
		else {
			throw new CFLibUnsupportedClassException(getClass(), "constructor", "schema", schema, "CFIntJpaSchema");
		}
	}

	/**
	 *	Create the instance in the database, and update the specified record
	 *	with the assigned primary key.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	rec	The instance interface to be created.
	 */
	@Override
	public ICFIntMimeType createMimeType( ICFSecAuthorization Authorization,
		ICFIntMimeType rec )
	{
		if (rec == null) {
			throw new CFLibNullArgumentException(getClass(), "createMimeType", 1, "rec");
		}
		else if (rec instanceof CFIntJpaMimeType) {
			CFIntJpaMimeType jparec = (CFIntJpaMimeType)rec;
			CFIntJpaMimeType created = schema.getJpaHooksSchema().getMimeTypeService().create(jparec);
			return( created );
		}
		else {
			throw new CFLibUnsupportedClassException(getClass(), "createMimeType", "rec", rec, "CFIntJpaMimeType");
		}
	}

	/**
	 *	Update the instance in the database, and update the specified record
	 *	with any calculated changes imposed by the associated stored procedure.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	rec	The instance interface to be updated
	 */
	@Override
	public ICFIntMimeType updateMimeType( ICFSecAuthorization Authorization,
		ICFIntMimeType rec )
	{
		if (rec == null) {
			throw new CFLibNullArgumentException(getClass(), "updateMimeType", 1, "rec");
		}
		else if (rec instanceof CFIntJpaMimeType) {
			CFIntJpaMimeType jparec = (CFIntJpaMimeType)rec;
			CFIntJpaMimeType updated = schema.getJpaHooksSchema().getMimeTypeService().update(jparec);
			return( updated );
		}
		else {
			throw new CFLibUnsupportedClassException(getClass(), "updateMimeType", "rec", rec, "CFIntJpaMimeType");
		}
	}

	/**
	 *	Delete the instance from the database.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	rec	The instance interface to be deleted.
	 */
	@Override
	public void deleteMimeType( ICFSecAuthorization Authorization,
		ICFIntMimeType rec )
	{
		if (rec == null) {
			return;
		}
		if (rec instanceof CFIntJpaMimeType) {
			CFIntJpaMimeType jparec = (CFIntJpaMimeType)rec;
			schema.getJpaHooksSchema().getMimeTypeService().deleteByIdIdx(jparec.getPKey());
		}
		else {
			throw new CFLibUnsupportedClassException(getClass(), "deleteMimeType", "rec", rec, "CFIntJpaMimeType");
		}

		throw new CFLibNotImplementedYetException(getClass(), "deleteMimeType");
	}

	/**
	 *	Delete the MimeType instance identified by the primary key.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	argKey	The primary key identifying the instance to be deleted.
	 */
	@Override
	public void deleteMimeTypeByIdIdx( ICFSecAuthorization Authorization,
		Integer argKey )
	{
		schema.getJpaHooksSchema().getMimeTypeService().deleteByIdIdx(argKey);
	}

	/**
	 *	Delete the MimeType instances identified by the key UNameIdx.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	Name	The MimeType key attribute of the instance generating the id.
	 */
	@Override
	public void deleteMimeTypeByUNameIdx( ICFSecAuthorization Authorization,
		String argName )
	{
		schema.getJpaHooksSchema().getMimeTypeService().deleteByUNameIdx(argName);
	}


	/**
	 *	Delete the MimeType instances identified by the key UNameIdx.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	argKey	The key identifying the instances to be deleted.
	 */
	@Override
	public void deleteMimeTypeByUNameIdx( ICFSecAuthorization Authorization,
		ICFIntMimeTypeByUNameIdxKey argKey )
	{
		schema.getJpaHooksSchema().getMimeTypeService().deleteByUNameIdx(argKey.getRequiredName());
	}


	/**
	 *	Read the derived MimeType record instance by primary key.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	PKey	The primary key of the MimeType instance to be read.
	 *
	 *	@return The record instance for the specified primary key, or null if there is
	 *		no such existing key value.
	 */
	@Override
	public ICFIntMimeType readDerived( ICFSecAuthorization Authorization,
		Integer PKey )
	{
		return( schema.getJpaHooksSchema().getMimeTypeService().find(PKey) );
	}

	/**
	 *	Lock the derived MimeType record instance by primary key.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	PKey	The primary key of the MimeType instance to be locked.
	 *
	 *	@return The record instance for the specified primary key, or null if there is
	 *		no such existing key value.
	 */
	@Override
	public ICFIntMimeType lockDerived( ICFSecAuthorization Authorization,
		Integer PKey )
	{
		return( schema.getJpaHooksSchema().getMimeTypeService().lockByIdIdx(PKey) );
	}

	/**
	 *	Read all MimeType instances.
	 *
	 *	@param	Authorization	The session authorization information.	
	 *
	 *	@return An array of derived record instances, potentially with 0 elements in the set.
	 */
	@Override
	public ICFIntMimeType[] readAllDerived( ICFSecAuthorization Authorization ) {
		List<CFIntJpaMimeType> results = schema.getJpaHooksSchema().getMimeTypeService().findAll();
		ICFIntMimeType[] retset = new ICFIntMimeType[results.size()];
		int idx = 0;
		for (CFIntJpaMimeType cur: results) {
			retset[idx++] = cur;
		}
		return( retset );
	}

	/**
	 *	Read the derived MimeType record instance identified by the unique key IdIdx.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	MimeTypeId	The MimeType key attribute of the instance generating the id.
	 *
	 *	@return The record instance for the specified key, or null if there is
	 *		no such existing key value.
	 */
	@Override
	public ICFIntMimeType readDerivedByIdIdx( ICFSecAuthorization Authorization,
		int argMimeTypeId )
	{
		return( schema.getJpaHooksSchema().getMimeTypeService().find(argMimeTypeId) );
	}

	/**
	 *	Read the derived MimeType record instance identified by the unique key UNameIdx.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	Name	The MimeType key attribute of the instance generating the id.
	 *
	 *	@return The record instance for the specified key, or null if there is
	 *		no such existing key value.
	 */
	@Override
	public ICFIntMimeType readDerivedByUNameIdx( ICFSecAuthorization Authorization,
		String argName )
	{
		return( schema.getJpaHooksSchema().getMimeTypeService().findByUNameIdx(argName) );
	}

	/**
	 *	Read the specific MimeType record instance identified by the primary key.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	PKey	The primary key of the MimeType instance to be locked.
	 *
	 *	@return The record instance for the specified primary key, or null if there is
	 *		no such existing key value.
	 *
	 *	@throws	CFLibNotSupportedException thrown by client-side implementations.
	 */
	@Override
	public ICFIntMimeType readRec( ICFSecAuthorization Authorization,
		Integer PKey )
	{
		throw new CFLibNotImplementedYetException(getClass(), "readRec");
	}

	/**
	 *	Lock the specific MimeType record instance identified by the primary key.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	PKey	The primary key of the MimeType instance to be locked.
	 *
	 *	@return The record instance for the specified primary key, or null if there is
	 *		no such existing key value.
	 *
	 *	@throws	CFLibNotSupportedException thrown by client-side implementations.
	 */
	@Override
	public ICFIntMimeType lockRec( ICFSecAuthorization Authorization,
		Integer PKey )
	{
		throw new CFLibNotImplementedYetException(getClass(), "lockRec");
	}

	/**
	 *	Read all the specific MimeType record instances.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@return All the specific MimeType instances in the database accessible for the Authorization.
	 */
	@Override
	public ICFIntMimeType[] readAllRec( ICFSecAuthorization Authorization ) {
		throw new CFLibNotImplementedYetException(getClass(), "readAllRec");
	}


	/**
	 *	Read the specific MimeType record instance identified by the unique key IdIdx.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	MimeTypeId	The MimeType key attribute of the instance generating the id.
	 *
	 *	@return The record instance for the specified key, or null if there is
	 *		no such existing key value.
	 *
	 *	@throws	CFLibNotSupportedException thrown by client-side implementations.
	 */
	@Override
	public ICFIntMimeType readRecByIdIdx( ICFSecAuthorization Authorization,
		int argMimeTypeId )
	{
		throw new CFLibNotImplementedYetException(getClass(), "readRecByIdIdx");
	}

	/**
	 *	Read the specific MimeType record instance identified by the unique key UNameIdx.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	Name	The MimeType key attribute of the instance generating the id.
	 *
	 *	@return The record instance for the specified key, or null if there is
	 *		no such existing key value.
	 *
	 *	@throws	CFLibNotSupportedException thrown by client-side implementations.
	 */
	@Override
	public ICFIntMimeType readRecByUNameIdx( ICFSecAuthorization Authorization,
		String argName )
	{
		throw new CFLibNotImplementedYetException(getClass(), "readRecByUNameIdx");
	}
}
