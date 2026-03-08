
// Description: Java 25 DbIO implementation for SubProject.

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
 *	CFIntJpaSubProjectTable database implementation for SubProject
 */
public class CFIntJpaSubProjectTable implements ICFIntSubProjectTable
{
	protected CFIntJpaSchema schema;


	public CFIntJpaSubProjectTable(ICFIntSchema schema) {
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
	public ICFIntSubProject createSubProject( ICFSecAuthorization Authorization,
		ICFIntSubProject rec )
	{
		if (rec == null) {
			throw new CFLibNullArgumentException(getClass(), "createSubProject", 1, "rec");
		}
		else if (rec instanceof CFIntJpaSubProject) {
			CFIntJpaSubProject jparec = (CFIntJpaSubProject)rec;
			CFIntJpaSubProject created = schema.getJpaHooksSchema().getSubProjectService().create(jparec);
			return( created );
		}
		else {
			throw new CFLibUnsupportedClassException(getClass(), "createSubProject", "rec", rec, "CFIntJpaSubProject");
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
	public ICFIntSubProject updateSubProject( ICFSecAuthorization Authorization,
		ICFIntSubProject rec )
	{
		if (rec == null) {
			throw new CFLibNullArgumentException(getClass(), "updateSubProject", 1, "rec");
		}
		else if (rec instanceof CFIntJpaSubProject) {
			CFIntJpaSubProject jparec = (CFIntJpaSubProject)rec;
			CFIntJpaSubProject updated = schema.getJpaHooksSchema().getSubProjectService().update(jparec);
			return( updated );
		}
		else {
			throw new CFLibUnsupportedClassException(getClass(), "updateSubProject", "rec", rec, "CFIntJpaSubProject");
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
	public void deleteSubProject( ICFSecAuthorization Authorization,
		ICFIntSubProject rec )
	{
		if (rec == null) {
			return;
		}
		if (rec instanceof CFIntJpaSubProject) {
			CFIntJpaSubProject jparec = (CFIntJpaSubProject)rec;
			schema.getJpaHooksSchema().getSubProjectService().deleteByIdIdx(jparec.getPKey());
		}
		else {
			throw new CFLibUnsupportedClassException(getClass(), "deleteSubProject", "rec", rec, "CFIntJpaSubProject");
		}

		throw new CFLibNotImplementedYetException(getClass(), "deleteSubProject");
	}

	/**
	 *	Delete the SubProject instance identified by the primary key.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	argKey	The primary key identifying the instance to be deleted.
	 */
	@Override
	public void deleteSubProjectByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argKey )
	{
		schema.getJpaHooksSchema().getSubProjectService().deleteByIdIdx(argKey);
	}

	/**
	 *	Delete the SubProject instances identified by the key TenantIdx.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	TenantId	The SubProject key attribute of the instance generating the id.
	 */
	@Override
	public void deleteSubProjectByTenantIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argTenantId )
	{
		schema.getJpaHooksSchema().getSubProjectService().deleteByTenantIdx(argTenantId);
	}


	/**
	 *	Delete the SubProject instances identified by the key TenantIdx.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	argKey	The key identifying the instances to be deleted.
	 */
	@Override
	public void deleteSubProjectByTenantIdx( ICFSecAuthorization Authorization,
		ICFIntSubProjectByTenantIdxKey argKey )
	{
		schema.getJpaHooksSchema().getSubProjectService().deleteByTenantIdx(argKey.getRequiredTenantId());
	}

	/**
	 *	Delete the SubProject instances identified by the key TopProjectIdx.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	TopProjectId	The SubProject key attribute of the instance generating the id.
	 */
	@Override
	public void deleteSubProjectByTopProjectIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argTopProjectId )
	{
		schema.getJpaHooksSchema().getSubProjectService().deleteByTopProjectIdx(argTopProjectId);
	}


	/**
	 *	Delete the SubProject instances identified by the key TopProjectIdx.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	argKey	The key identifying the instances to be deleted.
	 */
	@Override
	public void deleteSubProjectByTopProjectIdx( ICFSecAuthorization Authorization,
		ICFIntSubProjectByTopProjectIdxKey argKey )
	{
		schema.getJpaHooksSchema().getSubProjectService().deleteByTopProjectIdx(argKey.getRequiredTopProjectId());
	}

	/**
	 *	Delete the SubProject instances identified by the key NameIdx.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	TopProjectId	The SubProject key attribute of the instance generating the id.
	 *
	 *	@param	Name	The SubProject key attribute of the instance generating the id.
	 */
	@Override
	public void deleteSubProjectByNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argTopProjectId,
		String argName )
	{
		schema.getJpaHooksSchema().getSubProjectService().deleteByNameIdx(argTopProjectId,
		argName);
	}


	/**
	 *	Delete the SubProject instances identified by the key NameIdx.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	argKey	The key identifying the instances to be deleted.
	 */
	@Override
	public void deleteSubProjectByNameIdx( ICFSecAuthorization Authorization,
		ICFIntSubProjectByNameIdxKey argKey )
	{
		schema.getJpaHooksSchema().getSubProjectService().deleteByNameIdx(argKey.getRequiredTopProjectId(),
			argKey.getRequiredName());
	}


	/**
	 *	Read the derived SubProject record instance by primary key.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	PKey	The primary key of the SubProject instance to be read.
	 *
	 *	@return The record instance for the specified primary key, or null if there is
	 *		no such existing key value.
	 */
	@Override
	public ICFIntSubProject readDerived( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		return( schema.getJpaHooksSchema().getSubProjectService().find(PKey) );
	}

	/**
	 *	Lock the derived SubProject record instance by primary key.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	PKey	The primary key of the SubProject instance to be locked.
	 *
	 *	@return The record instance for the specified primary key, or null if there is
	 *		no such existing key value.
	 */
	@Override
	public ICFIntSubProject lockDerived( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		return( schema.getJpaHooksSchema().getSubProjectService().lockByIdIdx(PKey) );
	}

	/**
	 *	Read all SubProject instances.
	 *
	 *	@param	Authorization	The session authorization information.	
	 *
	 *	@return An array of derived record instances, potentially with 0 elements in the set.
	 */
	@Override
	public ICFIntSubProject[] readAllDerived( ICFSecAuthorization Authorization ) {
		List<CFIntJpaSubProject> results = schema.getJpaHooksSchema().getSubProjectService().findAll();
		ICFIntSubProject[] retset = new ICFIntSubProject[results.size()];
		int idx = 0;
		for (CFIntJpaSubProject cur: results) {
			retset[idx++] = cur;
		}
		return( retset );
	}

	/**
	 *	Read the derived SubProject record instance identified by the unique key IdIdx.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	Id	The SubProject key attribute of the instance generating the id.
	 *
	 *	@return The record instance for the specified key, or null if there is
	 *		no such existing key value.
	 */
	@Override
	public ICFIntSubProject readDerivedByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argId )
	{
		return( schema.getJpaHooksSchema().getSubProjectService().find(argId) );
	}

	/**
	 *	Read an array of the derived SubProject record instances identified by the duplicate key TenantIdx.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	TenantId	The SubProject key attribute of the instance generating the id.
	 *
	 *	@return An array of derived instances for the specified key, potentially with 0 elements in the set.
	 */
	@Override
	public ICFIntSubProject[] readDerivedByTenantIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argTenantId )
	{
		List<CFIntJpaSubProject> results = schema.getJpaHooksSchema().getSubProjectService().findByTenantIdx(argTenantId);
		ICFIntSubProject[] retset = new ICFIntSubProject[results.size()];
		int idx = 0;
		for (CFIntJpaSubProject cur: results) {
			retset[idx++] = cur;
		}
		return( retset );
	}

	/**
	 *	Read an array of the derived SubProject record instances identified by the duplicate key TopProjectIdx.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	TopProjectId	The SubProject key attribute of the instance generating the id.
	 *
	 *	@return An array of derived instances for the specified key, potentially with 0 elements in the set.
	 */
	@Override
	public ICFIntSubProject[] readDerivedByTopProjectIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argTopProjectId )
	{
		List<CFIntJpaSubProject> results = schema.getJpaHooksSchema().getSubProjectService().findByTopProjectIdx(argTopProjectId);
		ICFIntSubProject[] retset = new ICFIntSubProject[results.size()];
		int idx = 0;
		for (CFIntJpaSubProject cur: results) {
			retset[idx++] = cur;
		}
		return( retset );
	}

	/**
	 *	Read the derived SubProject record instance identified by the unique key NameIdx.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	TopProjectId	The SubProject key attribute of the instance generating the id.
	 *
	 *	@param	Name	The SubProject key attribute of the instance generating the id.
	 *
	 *	@return The record instance for the specified key, or null if there is
	 *		no such existing key value.
	 */
	@Override
	public ICFIntSubProject readDerivedByNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argTopProjectId,
		String argName )
	{
		return( schema.getJpaHooksSchema().getSubProjectService().findByNameIdx(argTopProjectId,
		argName) );
	}

	/**
	 *	Read the specific SubProject record instance identified by the primary key.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	PKey	The primary key of the SubProject instance to be locked.
	 *
	 *	@return The record instance for the specified primary key, or null if there is
	 *		no such existing key value.
	 *
	 *	@throws	CFLibNotSupportedException thrown by client-side implementations.
	 */
	@Override
	public ICFIntSubProject readRec( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		throw new CFLibNotImplementedYetException(getClass(), "readRec");
	}

	/**
	 *	Lock the specific SubProject record instance identified by the primary key.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	PKey	The primary key of the SubProject instance to be locked.
	 *
	 *	@return The record instance for the specified primary key, or null if there is
	 *		no such existing key value.
	 *
	 *	@throws	CFLibNotSupportedException thrown by client-side implementations.
	 */
	@Override
	public ICFIntSubProject lockRec( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		throw new CFLibNotImplementedYetException(getClass(), "lockRec");
	}

	/**
	 *	Read all the specific SubProject record instances.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@return All the specific SubProject instances in the database accessible for the Authorization.
	 */
	@Override
	public ICFIntSubProject[] readAllRec( ICFSecAuthorization Authorization ) {
		throw new CFLibNotImplementedYetException(getClass(), "readAllRec");
	}


	/**
	 *	Read the specific SubProject record instance identified by the unique key IdIdx.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	Id	The SubProject key attribute of the instance generating the id.
	 *
	 *	@return The record instance for the specified key, or null if there is
	 *		no such existing key value.
	 *
	 *	@throws	CFLibNotSupportedException thrown by client-side implementations.
	 */
	@Override
	public ICFIntSubProject readRecByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argId )
	{
		throw new CFLibNotImplementedYetException(getClass(), "readRecByIdIdx");
	}

	/**
	 *	Read an array of the specific SubProject record instances identified by the duplicate key TenantIdx.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	TenantId	The SubProject key attribute of the instance generating the id.
	 *
	 *	@return An array of derived record instances for the specified key, potentially with 0 elements in the set.
	 *
	 *	@throws	CFLibNotSupportedException thrown by client-side implementations.
	 */
	@Override
	public ICFIntSubProject[] readRecByTenantIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argTenantId )
	{
		throw new CFLibNotImplementedYetException(getClass(), "readRecByTenantIdx");
	}

	/**
	 *	Read an array of the specific SubProject record instances identified by the duplicate key TopProjectIdx.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	TopProjectId	The SubProject key attribute of the instance generating the id.
	 *
	 *	@return An array of derived record instances for the specified key, potentially with 0 elements in the set.
	 *
	 *	@throws	CFLibNotSupportedException thrown by client-side implementations.
	 */
	@Override
	public ICFIntSubProject[] readRecByTopProjectIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argTopProjectId )
	{
		throw new CFLibNotImplementedYetException(getClass(), "readRecByTopProjectIdx");
	}

	/**
	 *	Read the specific SubProject record instance identified by the unique key NameIdx.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	TopProjectId	The SubProject key attribute of the instance generating the id.
	 *
	 *	@param	Name	The SubProject key attribute of the instance generating the id.
	 *
	 *	@return The record instance for the specified key, or null if there is
	 *		no such existing key value.
	 *
	 *	@throws	CFLibNotSupportedException thrown by client-side implementations.
	 */
	@Override
	public ICFIntSubProject readRecByNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argTopProjectId,
		String argName )
	{
		throw new CFLibNotImplementedYetException(getClass(), "readRecByNameIdx");
	}
}
