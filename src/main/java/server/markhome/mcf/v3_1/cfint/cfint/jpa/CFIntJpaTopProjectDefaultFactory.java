
// Description: Java 25 JPA Default Factory implementation for TopProject.

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
 *	CFIntTopProjectFactory JPA implementation for TopProject
 */
public class CFIntJpaTopProjectDefaultFactory
    implements ICFIntTopProjectFactory
{
    public CFIntJpaTopProjectDefaultFactory() {
    }

    @Override
    public ICFIntTopProjectHPKey newHPKey() {
        ICFIntTopProjectHPKey hpkey =
            new CFIntJpaTopProjectHPKey();
        return( hpkey );
    }

	public CFIntJpaTopProjectHPKey ensureHPKey(ICFIntTopProjectHPKey key) {
		if (key == null) {
			return( null );
		}
		else if( key instanceof CFIntJpaTopProjectHPKey) {
			return( (CFIntJpaTopProjectHPKey)key );
		}
		else {
			CFIntJpaTopProjectHPKey mapped = new CFIntJpaTopProjectHPKey();
			mapped.setAuditClusterId(key.getAuditClusterId());
			mapped.setAuditActionId(key.getAuditActionId());
			mapped.setAuditSessionId(key.getAuditSessionId());
			mapped.setAuditStamp(key.getAuditStamp());
			mapped.setRequiredId( key.getRequiredId() );
			return( mapped );
		}
	}

    @Override
    public ICFIntTopProjectByTenantIdxKey newByTenantIdxKey() {
	ICFIntTopProjectByTenantIdxKey key =
            new CFIntJpaTopProjectByTenantIdxKey();
	return( key );
    }

	public CFIntJpaTopProjectByTenantIdxKey ensureByTenantIdxKey(ICFIntTopProjectByTenantIdxKey key) {
		if (key == null) {
			return( null );
		}
		else if (key instanceof CFIntJpaTopProjectByTenantIdxKey) {
			return( (CFIntJpaTopProjectByTenantIdxKey)key );
		}
		else {
			CFIntJpaTopProjectByTenantIdxKey mapped = new CFIntJpaTopProjectByTenantIdxKey();
			mapped.setRequiredTenantId( key.getRequiredTenantId() );
			return( mapped );
		}
	}

    @Override
    public ICFIntTopProjectByTopDomainIdxKey newByTopDomainIdxKey() {
	ICFIntTopProjectByTopDomainIdxKey key =
            new CFIntJpaTopProjectByTopDomainIdxKey();
	return( key );
    }

	public CFIntJpaTopProjectByTopDomainIdxKey ensureByTopDomainIdxKey(ICFIntTopProjectByTopDomainIdxKey key) {
		if (key == null) {
			return( null );
		}
		else if (key instanceof CFIntJpaTopProjectByTopDomainIdxKey) {
			return( (CFIntJpaTopProjectByTopDomainIdxKey)key );
		}
		else {
			CFIntJpaTopProjectByTopDomainIdxKey mapped = new CFIntJpaTopProjectByTopDomainIdxKey();
			mapped.setRequiredTopDomainId( key.getRequiredTopDomainId() );
			return( mapped );
		}
	}

    @Override
    public ICFIntTopProjectByNameIdxKey newByNameIdxKey() {
	ICFIntTopProjectByNameIdxKey key =
            new CFIntJpaTopProjectByNameIdxKey();
	return( key );
    }

	public CFIntJpaTopProjectByNameIdxKey ensureByNameIdxKey(ICFIntTopProjectByNameIdxKey key) {
		if (key == null) {
			return( null );
		}
		else if (key instanceof CFIntJpaTopProjectByNameIdxKey) {
			return( (CFIntJpaTopProjectByNameIdxKey)key );
		}
		else {
			CFIntJpaTopProjectByNameIdxKey mapped = new CFIntJpaTopProjectByNameIdxKey();
			mapped.setRequiredTopDomainId( key.getRequiredTopDomainId() );
			mapped.setRequiredName( key.getRequiredName() );
			return( mapped );
		}
	}

    @Override
    public ICFIntTopProject newRec() {
        ICFIntTopProject rec =
            new CFIntJpaTopProject();
        return( rec );
    }

	public CFIntJpaTopProject ensureRec(ICFIntTopProject rec) {
		if( rec == null ) {
			return( null );
		}
		else if (rec instanceof CFIntJpaTopProject) {
			return( (CFIntJpaTopProject)rec );
		}
		else {
			CFIntJpaTopProject mapped = new CFIntJpaTopProject();
			mapped.set(rec);
			return( mapped );
		}
	}

    @Override
    public ICFIntTopProjectH newHRec() {
        ICFIntTopProjectH hrec =
            new CFIntJpaTopProjectH();
        return( hrec );
    }

	public CFIntJpaTopProjectH ensureHRec(ICFIntTopProjectH hrec) {
		if (hrec == null) {
			return( null );
		}
		else if( hrec instanceof CFIntJpaTopProjectH) {
			return( (CFIntJpaTopProjectH)hrec );
		}
		else {
			CFIntJpaTopProjectH mapped = new CFIntJpaTopProjectH();
			mapped.set(hrec);
			return( mapped );
		}
	}
}
