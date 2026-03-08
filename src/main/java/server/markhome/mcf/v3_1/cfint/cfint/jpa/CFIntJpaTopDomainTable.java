
// Description: Java 25 DbIO implementation for TopDomain.

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
 *	CFIntJpaTopDomainTable database implementation for TopDomain
 */
public class CFIntJpaTopDomainTable implements ICFIntTopDomainTable
{
	protected CFIntJpaSchema schema;


	public CFIntJpaTopDomainTable(ICFIntSchema schema) {
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
	public ICFIntTopDomain createTopDomain( ICFSecAuthorization Authorization,
		ICFIntTopDomain rec )
	{
		if (rec == null) {
			throw new CFLibNullArgumentException(getClass(), "createTopDomain", 1, "rec");
		}
		else if (rec instanceof CFIntJpaTopDomain) {
			CFIntJpaTopDomain jparec = (CFIntJpaTopDomain)rec;
			CFIntJpaTopDomain created = schema.getJpaHooksSchema().getTopDomainService().create(jparec);
			return( created );
		}
		else {
			throw new CFLibUnsupportedClassException(getClass(), "createTopDomain", "rec", rec, "CFIntJpaTopDomain");
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
	public ICFIntTopDomain updateTopDomain( ICFSecAuthorization Authorization,
		ICFIntTopDomain rec )
	{
		if (rec == null) {
			throw new CFLibNullArgumentException(getClass(), "updateTopDomain", 1, "rec");
		}
		else if (rec instanceof CFIntJpaTopDomain) {
			CFIntJpaTopDomain jparec = (CFIntJpaTopDomain)rec;
			CFIntJpaTopDomain updated = schema.getJpaHooksSchema().getTopDomainService().update(jparec);
			return( updated );
		}
		else {
			throw new CFLibUnsupportedClassException(getClass(), "updateTopDomain", "rec", rec, "CFIntJpaTopDomain");
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
	public void deleteTopDomain( ICFSecAuthorization Authorization,
		ICFIntTopDomain rec )
	{
		if (rec == null) {
			return;
		}
		if (rec instanceof CFIntJpaTopDomain) {
			CFIntJpaTopDomain jparec = (CFIntJpaTopDomain)rec;
			schema.getJpaHooksSchema().getTopDomainService().deleteByIdIdx(jparec.getPKey());
		}
		else {
			throw new CFLibUnsupportedClassException(getClass(), "deleteTopDomain", "rec", rec, "CFIntJpaTopDomain");
		}

		throw new CFLibNotImplementedYetException(getClass(), "deleteTopDomain");
	}

	/**
	 *	Delete the TopDomain instance identified by the primary key.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	argKey	The primary key identifying the instance to be deleted.
	 */
	@Override
	public void deleteTopDomainByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argKey )
	{
		schema.getJpaHooksSchema().getTopDomainService().deleteByIdIdx(argKey);
	}

	/**
	 *	Delete the TopDomain instances identified by the key TenantIdx.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	TenantId	The TopDomain key attribute of the instance generating the id.
	 */
	@Override
	public void deleteTopDomainByTenantIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argTenantId )
	{
		schema.getJpaHooksSchema().getTopDomainService().deleteByTenantIdx(argTenantId);
	}


	/**
	 *	Delete the TopDomain instances identified by the key TenantIdx.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	argKey	The key identifying the instances to be deleted.
	 */
	@Override
	public void deleteTopDomainByTenantIdx( ICFSecAuthorization Authorization,
		ICFIntTopDomainByTenantIdxKey argKey )
	{
		schema.getJpaHooksSchema().getTopDomainService().deleteByTenantIdx(argKey.getRequiredTenantId());
	}

	/**
	 *	Delete the TopDomain instances identified by the key TldIdx.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	TldId	The TopDomain key attribute of the instance generating the id.
	 */
	@Override
	public void deleteTopDomainByTldIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argTldId )
	{
		schema.getJpaHooksSchema().getTopDomainService().deleteByTldIdx(argTldId);
	}


	/**
	 *	Delete the TopDomain instances identified by the key TldIdx.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	argKey	The key identifying the instances to be deleted.
	 */
	@Override
	public void deleteTopDomainByTldIdx( ICFSecAuthorization Authorization,
		ICFIntTopDomainByTldIdxKey argKey )
	{
		schema.getJpaHooksSchema().getTopDomainService().deleteByTldIdx(argKey.getRequiredTldId());
	}

	/**
	 *	Delete the TopDomain instances identified by the key NameIdx.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	TldId	The TopDomain key attribute of the instance generating the id.
	 *
	 *	@param	Name	The TopDomain key attribute of the instance generating the id.
	 */
	@Override
	public void deleteTopDomainByNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argTldId,
		String argName )
	{
		schema.getJpaHooksSchema().getTopDomainService().deleteByNameIdx(argTldId,
		argName);
	}


	/**
	 *	Delete the TopDomain instances identified by the key NameIdx.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	argKey	The key identifying the instances to be deleted.
	 */
	@Override
	public void deleteTopDomainByNameIdx( ICFSecAuthorization Authorization,
		ICFIntTopDomainByNameIdxKey argKey )
	{
		schema.getJpaHooksSchema().getTopDomainService().deleteByNameIdx(argKey.getRequiredTldId(),
			argKey.getRequiredName());
	}


	/**
	 *	Read the derived TopDomain record instance by primary key.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	PKey	The primary key of the TopDomain instance to be read.
	 *
	 *	@return The record instance for the specified primary key, or null if there is
	 *		no such existing key value.
	 */
	@Override
	public ICFIntTopDomain readDerived( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		return( schema.getJpaHooksSchema().getTopDomainService().find(PKey) );
	}

	/**
	 *	Lock the derived TopDomain record instance by primary key.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	PKey	The primary key of the TopDomain instance to be locked.
	 *
	 *	@return The record instance for the specified primary key, or null if there is
	 *		no such existing key value.
	 */
	@Override
	public ICFIntTopDomain lockDerived( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		return( schema.getJpaHooksSchema().getTopDomainService().lockByIdIdx(PKey) );
	}

	/**
	 *	Read all TopDomain instances.
	 *
	 *	@param	Authorization	The session authorization information.	
	 *
	 *	@return An array of derived record instances, potentially with 0 elements in the set.
	 */
	@Override
	public ICFIntTopDomain[] readAllDerived( ICFSecAuthorization Authorization ) {
		List<CFIntJpaTopDomain> results = schema.getJpaHooksSchema().getTopDomainService().findAll();
		ICFIntTopDomain[] retset = new ICFIntTopDomain[results.size()];
		int idx = 0;
		for (CFIntJpaTopDomain cur: results) {
			retset[idx++] = cur;
		}
		return( retset );
	}

	/**
	 *	Read the derived TopDomain record instance identified by the unique key IdIdx.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	Id	The TopDomain key attribute of the instance generating the id.
	 *
	 *	@return The record instance for the specified key, or null if there is
	 *		no such existing key value.
	 */
	@Override
	public ICFIntTopDomain readDerivedByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argId )
	{
		return( schema.getJpaHooksSchema().getTopDomainService().find(argId) );
	}

	/**
	 *	Read an array of the derived TopDomain record instances identified by the duplicate key TenantIdx.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	TenantId	The TopDomain key attribute of the instance generating the id.
	 *
	 *	@return An array of derived instances for the specified key, potentially with 0 elements in the set.
	 */
	@Override
	public ICFIntTopDomain[] readDerivedByTenantIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argTenantId )
	{
		List<CFIntJpaTopDomain> results = schema.getJpaHooksSchema().getTopDomainService().findByTenantIdx(argTenantId);
		ICFIntTopDomain[] retset = new ICFIntTopDomain[results.size()];
		int idx = 0;
		for (CFIntJpaTopDomain cur: results) {
			retset[idx++] = cur;
		}
		return( retset );
	}

	/**
	 *	Read an array of the derived TopDomain record instances identified by the duplicate key TldIdx.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	TldId	The TopDomain key attribute of the instance generating the id.
	 *
	 *	@return An array of derived instances for the specified key, potentially with 0 elements in the set.
	 */
	@Override
	public ICFIntTopDomain[] readDerivedByTldIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argTldId )
	{
		List<CFIntJpaTopDomain> results = schema.getJpaHooksSchema().getTopDomainService().findByTldIdx(argTldId);
		ICFIntTopDomain[] retset = new ICFIntTopDomain[results.size()];
		int idx = 0;
		for (CFIntJpaTopDomain cur: results) {
			retset[idx++] = cur;
		}
		return( retset );
	}

	/**
	 *	Read the derived TopDomain record instance identified by the unique key NameIdx.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	TldId	The TopDomain key attribute of the instance generating the id.
	 *
	 *	@param	Name	The TopDomain key attribute of the instance generating the id.
	 *
	 *	@return The record instance for the specified key, or null if there is
	 *		no such existing key value.
	 */
	@Override
	public ICFIntTopDomain readDerivedByNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argTldId,
		String argName )
	{
		return( schema.getJpaHooksSchema().getTopDomainService().findByNameIdx(argTldId,
		argName) );
	}

	/**
	 *	Read the specific TopDomain record instance identified by the primary key.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	PKey	The primary key of the TopDomain instance to be locked.
	 *
	 *	@return The record instance for the specified primary key, or null if there is
	 *		no such existing key value.
	 *
	 *	@throws	CFLibNotSupportedException thrown by client-side implementations.
	 */
	@Override
	public ICFIntTopDomain readRec( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		throw new CFLibNotImplementedYetException(getClass(), "readRec");
	}

	/**
	 *	Lock the specific TopDomain record instance identified by the primary key.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	PKey	The primary key of the TopDomain instance to be locked.
	 *
	 *	@return The record instance for the specified primary key, or null if there is
	 *		no such existing key value.
	 *
	 *	@throws	CFLibNotSupportedException thrown by client-side implementations.
	 */
	@Override
	public ICFIntTopDomain lockRec( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		throw new CFLibNotImplementedYetException(getClass(), "lockRec");
	}

	/**
	 *	Read all the specific TopDomain record instances.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@return All the specific TopDomain instances in the database accessible for the Authorization.
	 */
	@Override
	public ICFIntTopDomain[] readAllRec( ICFSecAuthorization Authorization ) {
		throw new CFLibNotImplementedYetException(getClass(), "readAllRec");
	}


	/**
	 *	Read the specific TopDomain record instance identified by the unique key IdIdx.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	Id	The TopDomain key attribute of the instance generating the id.
	 *
	 *	@return The record instance for the specified key, or null if there is
	 *		no such existing key value.
	 *
	 *	@throws	CFLibNotSupportedException thrown by client-side implementations.
	 */
	@Override
	public ICFIntTopDomain readRecByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argId )
	{
		throw new CFLibNotImplementedYetException(getClass(), "readRecByIdIdx");
	}

	/**
	 *	Read an array of the specific TopDomain record instances identified by the duplicate key TenantIdx.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	TenantId	The TopDomain key attribute of the instance generating the id.
	 *
	 *	@return An array of derived record instances for the specified key, potentially with 0 elements in the set.
	 *
	 *	@throws	CFLibNotSupportedException thrown by client-side implementations.
	 */
	@Override
	public ICFIntTopDomain[] readRecByTenantIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argTenantId )
	{
		throw new CFLibNotImplementedYetException(getClass(), "readRecByTenantIdx");
	}

	/**
	 *	Read an array of the specific TopDomain record instances identified by the duplicate key TldIdx.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	TldId	The TopDomain key attribute of the instance generating the id.
	 *
	 *	@return An array of derived record instances for the specified key, potentially with 0 elements in the set.
	 *
	 *	@throws	CFLibNotSupportedException thrown by client-side implementations.
	 */
	@Override
	public ICFIntTopDomain[] readRecByTldIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argTldId )
	{
		throw new CFLibNotImplementedYetException(getClass(), "readRecByTldIdx");
	}

	/**
	 *	Read the specific TopDomain record instance identified by the unique key NameIdx.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	TldId	The TopDomain key attribute of the instance generating the id.
	 *
	 *	@param	Name	The TopDomain key attribute of the instance generating the id.
	 *
	 *	@return The record instance for the specified key, or null if there is
	 *		no such existing key value.
	 *
	 *	@throws	CFLibNotSupportedException thrown by client-side implementations.
	 */
	@Override
	public ICFIntTopDomain readRecByNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argTldId,
		String argName )
	{
		throw new CFLibNotImplementedYetException(getClass(), "readRecByNameIdx");
	}
}
