
// Description: Java 25 JPA Default Factory implementation for SubProject.

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
import java.util.*;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.text.StringEscapeUtils;
import server.markhome.mcf.v3_1.cflib.*;
import server.markhome.mcf.v3_1.cflib.dbutil.*;
import server.markhome.mcf.v3_1.cfsec.cfsec.*;
import server.markhome.mcf.v3_1.cfint.cfint.*;
import server.markhome.mcf.v3_1.cfsec.cfsec.jpa.*;

/*
 *	CFIntSubProjectFactory JPA implementation for SubProject
 */
public class CFIntJpaSubProjectDefaultFactory
    implements ICFIntSubProjectFactory
{
    public CFIntJpaSubProjectDefaultFactory() {
    }

    @Override
    public ICFIntSubProjectHPKey newHPKey() {
        ICFIntSubProjectHPKey hpkey =
            new CFIntJpaSubProjectHPKey();
        return( hpkey );
    }

	public CFIntJpaSubProjectHPKey ensureHPKey(ICFIntSubProjectHPKey key) {
		if (key == null) {
			return( null );
		}
		else if( key instanceof CFIntJpaSubProjectHPKey) {
			return( (CFIntJpaSubProjectHPKey)key );
		}
		else {
			CFIntJpaSubProjectHPKey mapped = new CFIntJpaSubProjectHPKey();
			mapped.setAuditClusterId(key.getAuditClusterId());
			mapped.setAuditActionId(key.getAuditActionId());
			mapped.setAuditSessionId(key.getAuditSessionId());
			mapped.setAuditStamp(key.getAuditStamp());
			mapped.setRequiredId( key.getRequiredId() );
			return( mapped );
		}
	}

    @Override
    public ICFIntSubProjectByTenantIdxKey newByTenantIdxKey() {
	ICFIntSubProjectByTenantIdxKey key =
            new CFIntJpaSubProjectByTenantIdxKey();
	return( key );
    }

	public CFIntJpaSubProjectByTenantIdxKey ensureByTenantIdxKey(ICFIntSubProjectByTenantIdxKey key) {
		if (key == null) {
			return( null );
		}
		else if (key instanceof CFIntJpaSubProjectByTenantIdxKey) {
			return( (CFIntJpaSubProjectByTenantIdxKey)key );
		}
		else {
			CFIntJpaSubProjectByTenantIdxKey mapped = new CFIntJpaSubProjectByTenantIdxKey();
			mapped.setRequiredTenantId( key.getRequiredTenantId() );
			return( mapped );
		}
	}

    @Override
    public ICFIntSubProjectByTopProjectIdxKey newByTopProjectIdxKey() {
	ICFIntSubProjectByTopProjectIdxKey key =
            new CFIntJpaSubProjectByTopProjectIdxKey();
	return( key );
    }

	public CFIntJpaSubProjectByTopProjectIdxKey ensureByTopProjectIdxKey(ICFIntSubProjectByTopProjectIdxKey key) {
		if (key == null) {
			return( null );
		}
		else if (key instanceof CFIntJpaSubProjectByTopProjectIdxKey) {
			return( (CFIntJpaSubProjectByTopProjectIdxKey)key );
		}
		else {
			CFIntJpaSubProjectByTopProjectIdxKey mapped = new CFIntJpaSubProjectByTopProjectIdxKey();
			mapped.setRequiredTopProjectId( key.getRequiredTopProjectId() );
			return( mapped );
		}
	}

    @Override
    public ICFIntSubProjectByNameIdxKey newByNameIdxKey() {
	ICFIntSubProjectByNameIdxKey key =
            new CFIntJpaSubProjectByNameIdxKey();
	return( key );
    }

	public CFIntJpaSubProjectByNameIdxKey ensureByNameIdxKey(ICFIntSubProjectByNameIdxKey key) {
		if (key == null) {
			return( null );
		}
		else if (key instanceof CFIntJpaSubProjectByNameIdxKey) {
			return( (CFIntJpaSubProjectByNameIdxKey)key );
		}
		else {
			CFIntJpaSubProjectByNameIdxKey mapped = new CFIntJpaSubProjectByNameIdxKey();
			mapped.setRequiredTopProjectId( key.getRequiredTopProjectId() );
			mapped.setRequiredName( key.getRequiredName() );
			return( mapped );
		}
	}

    @Override
    public ICFIntSubProject newRec() {
        ICFIntSubProject rec =
            new CFIntJpaSubProject();
        return( rec );
    }

	public CFIntJpaSubProject ensureRec(ICFIntSubProject rec) {
		if( rec == null ) {
			return( null );
		}
		else if (rec instanceof CFIntJpaSubProject) {
			return( (CFIntJpaSubProject)rec );
		}
		else {
			CFIntJpaSubProject mapped = new CFIntJpaSubProject();
			mapped.set(rec);
			return( mapped );
		}
	}

    @Override
    public ICFIntSubProjectH newHRec() {
        ICFIntSubProjectH hrec =
            new CFIntJpaSubProjectH();
        return( hrec );
    }

	public CFIntJpaSubProjectH ensureHRec(ICFIntSubProjectH hrec) {
		if (hrec == null) {
			return( null );
		}
		else if( hrec instanceof CFIntJpaSubProjectH) {
			return( (CFIntJpaSubProjectH)hrec );
		}
		else {
			CFIntJpaSubProjectH mapped = new CFIntJpaSubProjectH();
			mapped.set(hrec);
			return( mapped );
		}
	}
}
