
// Description: Java 25 DbIO implementation for URLProtocol.

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

import server.markhome.mcf.v3_1.cflib.*;
import server.markhome.mcf.v3_1.cflib.dbutil.*;
import server.markhome.mcf.v3_1.cfsec.cfsec.*;
import server.markhome.mcf.v3_1.cfint.cfint.*;
import server.markhome.mcf.v3_1.cfsec.cfsecobj.*;
import server.markhome.mcf.v3_1.cfint.cfintobj.*;
import server.markhome.mcf.v3_1.cfint.cfint.jpa.CFIntJpaHooksSchema;

/*
 *	CFIntJpaURLProtocolTable database implementation for URLProtocol
 */
public class CFIntJpaURLProtocolTable implements ICFIntURLProtocolTable
{
	protected CFIntJpaSchema schema;


	public CFIntJpaURLProtocolTable(ICFIntSchema schema) {
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
	public ICFIntURLProtocol createURLProtocol( ICFSecAuthorization Authorization,
		ICFIntURLProtocol rec )
	{
		if (rec == null) {
			throw new CFLibNullArgumentException(getClass(), "createURLProtocol", 1, "rec");
		}
		else if (rec instanceof CFIntJpaURLProtocol) {
			CFIntJpaURLProtocol jparec = (CFIntJpaURLProtocol)rec;
			CFIntJpaURLProtocol created = schema.getJpaHooksSchema().getURLProtocolService().create(jparec);
			return( created );
		}
		else {
			throw new CFLibUnsupportedClassException(getClass(), "createURLProtocol", "rec", rec, "CFIntJpaURLProtocol");
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
	public ICFIntURLProtocol updateURLProtocol( ICFSecAuthorization Authorization,
		ICFIntURLProtocol rec )
	{
		if (rec == null) {
			throw new CFLibNullArgumentException(getClass(), "updateURLProtocol", 1, "rec");
		}
		else if (rec instanceof CFIntJpaURLProtocol) {
			CFIntJpaURLProtocol jparec = (CFIntJpaURLProtocol)rec;
			CFIntJpaURLProtocol updated = schema.getJpaHooksSchema().getURLProtocolService().update(jparec);
			return( updated );
		}
		else {
			throw new CFLibUnsupportedClassException(getClass(), "updateURLProtocol", "rec", rec, "CFIntJpaURLProtocol");
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
	public void deleteURLProtocol( ICFSecAuthorization Authorization,
		ICFIntURLProtocol rec )
	{
		if (rec == null) {
			return;
		}
		if (rec instanceof CFIntJpaURLProtocol) {
			CFIntJpaURLProtocol jparec = (CFIntJpaURLProtocol)rec;
			schema.getJpaHooksSchema().getURLProtocolService().deleteByIdIdx(jparec.getPKey());
		}
		else {
			throw new CFLibUnsupportedClassException(getClass(), "deleteURLProtocol", "rec", rec, "CFIntJpaURLProtocol");
		}

		throw new CFLibNotImplementedYetException(getClass(), "deleteURLProtocol");
	}

	/**
	 *	Delete the URLProtocol instance identified by the primary key.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	argKey	The primary key identifying the instance to be deleted.
	 */
	@Override
	public void deleteURLProtocolByIdIdx( ICFSecAuthorization Authorization,
		Integer argKey )
	{
		schema.getJpaHooksSchema().getURLProtocolService().deleteByIdIdx(argKey);
	}

	/**
	 *	Delete the URLProtocol instances identified by the key UNameIdx.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	Name	The URLProtocol key attribute of the instance generating the id.
	 */
	@Override
	public void deleteURLProtocolByUNameIdx( ICFSecAuthorization Authorization,
		String argName )
	{
		schema.getJpaHooksSchema().getURLProtocolService().deleteByUNameIdx(argName);
	}


	/**
	 *	Delete the URLProtocol instances identified by the key UNameIdx.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	argKey	The key identifying the instances to be deleted.
	 */
	@Override
	public void deleteURLProtocolByUNameIdx( ICFSecAuthorization Authorization,
		ICFIntURLProtocolByUNameIdxKey argKey )
	{
		schema.getJpaHooksSchema().getURLProtocolService().deleteByUNameIdx(argKey.getRequiredName());
	}

	/**
	 *	Delete the URLProtocol instances identified by the key IsSecureIdx.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	IsSecure	The URLProtocol key attribute of the instance generating the id.
	 */
	@Override
	public void deleteURLProtocolByIsSecureIdx( ICFSecAuthorization Authorization,
		boolean argIsSecure )
	{
		schema.getJpaHooksSchema().getURLProtocolService().deleteByIsSecureIdx(argIsSecure);
	}


	/**
	 *	Delete the URLProtocol instances identified by the key IsSecureIdx.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	argKey	The key identifying the instances to be deleted.
	 */
	@Override
	public void deleteURLProtocolByIsSecureIdx( ICFSecAuthorization Authorization,
		ICFIntURLProtocolByIsSecureIdxKey argKey )
	{
		schema.getJpaHooksSchema().getURLProtocolService().deleteByIsSecureIdx(argKey.getRequiredIsSecure());
	}


	/**
	 *	Read the derived URLProtocol record instance by primary key.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	PKey	The primary key of the URLProtocol instance to be read.
	 *
	 *	@return The record instance for the specified primary key, or null if there is
	 *		no such existing key value.
	 */
	@Override
	public ICFIntURLProtocol readDerived( ICFSecAuthorization Authorization,
		Integer PKey )
	{
		return( schema.getJpaHooksSchema().getURLProtocolService().find(PKey) );
	}

	/**
	 *	Lock the derived URLProtocol record instance by primary key.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	PKey	The primary key of the URLProtocol instance to be locked.
	 *
	 *	@return The record instance for the specified primary key, or null if there is
	 *		no such existing key value.
	 */
	@Override
	public ICFIntURLProtocol lockDerived( ICFSecAuthorization Authorization,
		Integer PKey )
	{
		return( schema.getJpaHooksSchema().getURLProtocolService().lockByIdIdx(PKey) );
	}

	/**
	 *	Read all URLProtocol instances.
	 *
	 *	@param	Authorization	The session authorization information.	
	 *
	 *	@return An array of derived record instances, potentially with 0 elements in the set.
	 */
	@Override
	public ICFIntURLProtocol[] readAllDerived( ICFSecAuthorization Authorization ) {
		List<CFIntJpaURLProtocol> results = schema.getJpaHooksSchema().getURLProtocolService().findAll();
		ICFIntURLProtocol[] retset = new ICFIntURLProtocol[results.size()];
		int idx = 0;
		for (CFIntJpaURLProtocol cur: results) {
			retset[idx++] = cur;
		}
		return( retset );
	}

	/**
	 *	Read the derived URLProtocol record instance identified by the unique key IdIdx.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	URLProtocolId	The URLProtocol key attribute of the instance generating the id.
	 *
	 *	@return The record instance for the specified key, or null if there is
	 *		no such existing key value.
	 */
	@Override
	public ICFIntURLProtocol readDerivedByIdIdx( ICFSecAuthorization Authorization,
		int argURLProtocolId )
	{
		return( schema.getJpaHooksSchema().getURLProtocolService().find(argURLProtocolId) );
	}

	/**
	 *	Read the derived URLProtocol record instance identified by the unique key UNameIdx.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	Name	The URLProtocol key attribute of the instance generating the id.
	 *
	 *	@return The record instance for the specified key, or null if there is
	 *		no such existing key value.
	 */
	@Override
	public ICFIntURLProtocol readDerivedByUNameIdx( ICFSecAuthorization Authorization,
		String argName )
	{
		return( schema.getJpaHooksSchema().getURLProtocolService().findByUNameIdx(argName) );
	}

	/**
	 *	Read an array of the derived URLProtocol record instances identified by the duplicate key IsSecureIdx.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	IsSecure	The URLProtocol key attribute of the instance generating the id.
	 *
	 *	@return An array of derived instances for the specified key, potentially with 0 elements in the set.
	 */
	@Override
	public ICFIntURLProtocol[] readDerivedByIsSecureIdx( ICFSecAuthorization Authorization,
		boolean argIsSecure )
	{
		List<CFIntJpaURLProtocol> results = schema.getJpaHooksSchema().getURLProtocolService().findByIsSecureIdx(argIsSecure);
		ICFIntURLProtocol[] retset = new ICFIntURLProtocol[results.size()];
		int idx = 0;
		for (CFIntJpaURLProtocol cur: results) {
			retset[idx++] = cur;
		}
		return( retset );
	}

	/**
	 *	Read the specific URLProtocol record instance identified by the primary key.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	PKey	The primary key of the URLProtocol instance to be locked.
	 *
	 *	@return The record instance for the specified primary key, or null if there is
	 *		no such existing key value.
	 *
	 *	@throws	CFLibNotSupportedException thrown by client-side implementations.
	 */
	@Override
	public ICFIntURLProtocol readRec( ICFSecAuthorization Authorization,
		Integer PKey )
	{
		throw new CFLibNotImplementedYetException(getClass(), "readRec");
	}

	/**
	 *	Lock the specific URLProtocol record instance identified by the primary key.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	PKey	The primary key of the URLProtocol instance to be locked.
	 *
	 *	@return The record instance for the specified primary key, or null if there is
	 *		no such existing key value.
	 *
	 *	@throws	CFLibNotSupportedException thrown by client-side implementations.
	 */
	@Override
	public ICFIntURLProtocol lockRec( ICFSecAuthorization Authorization,
		Integer PKey )
	{
		throw new CFLibNotImplementedYetException(getClass(), "lockRec");
	}

	/**
	 *	Read all the specific URLProtocol record instances.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@return All the specific URLProtocol instances in the database accessible for the Authorization.
	 */
	@Override
	public ICFIntURLProtocol[] readAllRec( ICFSecAuthorization Authorization ) {
		throw new CFLibNotImplementedYetException(getClass(), "readAllRec");
	}


	/**
	 *	Read the specific URLProtocol record instance identified by the unique key IdIdx.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	URLProtocolId	The URLProtocol key attribute of the instance generating the id.
	 *
	 *	@return The record instance for the specified key, or null if there is
	 *		no such existing key value.
	 *
	 *	@throws	CFLibNotSupportedException thrown by client-side implementations.
	 */
	@Override
	public ICFIntURLProtocol readRecByIdIdx( ICFSecAuthorization Authorization,
		int argURLProtocolId )
	{
		throw new CFLibNotImplementedYetException(getClass(), "readRecByIdIdx");
	}

	/**
	 *	Read the specific URLProtocol record instance identified by the unique key UNameIdx.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	Name	The URLProtocol key attribute of the instance generating the id.
	 *
	 *	@return The record instance for the specified key, or null if there is
	 *		no such existing key value.
	 *
	 *	@throws	CFLibNotSupportedException thrown by client-side implementations.
	 */
	@Override
	public ICFIntURLProtocol readRecByUNameIdx( ICFSecAuthorization Authorization,
		String argName )
	{
		throw new CFLibNotImplementedYetException(getClass(), "readRecByUNameIdx");
	}

	/**
	 *	Read an array of the specific URLProtocol record instances identified by the duplicate key IsSecureIdx.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	IsSecure	The URLProtocol key attribute of the instance generating the id.
	 *
	 *	@return An array of derived record instances for the specified key, potentially with 0 elements in the set.
	 *
	 *	@throws	CFLibNotSupportedException thrown by client-side implementations.
	 */
	@Override
	public ICFIntURLProtocol[] readRecByIsSecureIdx( ICFSecAuthorization Authorization,
		boolean argIsSecure )
	{
		throw new CFLibNotImplementedYetException(getClass(), "readRecByIsSecureIdx");
	}
}
