
// Description: Java 25 JPA Default Factory implementation for TopDomain.

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
 *	CFIntTopDomainFactory JPA implementation for TopDomain
 */
public class CFIntJpaTopDomainDefaultFactory
    implements ICFIntTopDomainFactory
{
    public CFIntJpaTopDomainDefaultFactory() {
    }

    @Override
    public ICFIntTopDomainHPKey newHPKey() {
        ICFIntTopDomainHPKey hpkey =
            new CFIntJpaTopDomainHPKey();
        return( hpkey );
    }

	public CFIntJpaTopDomainHPKey ensureHPKey(ICFIntTopDomainHPKey key) {
		if (key == null) {
			return( null );
		}
		else if( key instanceof CFIntJpaTopDomainHPKey) {
			return( (CFIntJpaTopDomainHPKey)key );
		}
		else {
			CFIntJpaTopDomainHPKey mapped = new CFIntJpaTopDomainHPKey();
			mapped.setAuditClusterId(key.getAuditClusterId());
			mapped.setAuditActionId(key.getAuditActionId());
			mapped.setAuditSessionId(key.getAuditSessionId());
			mapped.setAuditStamp(key.getAuditStamp());
			mapped.setRequiredId( key.getRequiredId() );
			return( mapped );
		}
	}

    @Override
    public ICFIntTopDomainByTenantIdxKey newByTenantIdxKey() {
	ICFIntTopDomainByTenantIdxKey key =
            new CFIntJpaTopDomainByTenantIdxKey();
	return( key );
    }

	public CFIntJpaTopDomainByTenantIdxKey ensureByTenantIdxKey(ICFIntTopDomainByTenantIdxKey key) {
		if (key == null) {
			return( null );
		}
		else if (key instanceof CFIntJpaTopDomainByTenantIdxKey) {
			return( (CFIntJpaTopDomainByTenantIdxKey)key );
		}
		else {
			CFIntJpaTopDomainByTenantIdxKey mapped = new CFIntJpaTopDomainByTenantIdxKey();
			mapped.setRequiredTenantId( key.getRequiredTenantId() );
			return( mapped );
		}
	}

    @Override
    public ICFIntTopDomainByTldIdxKey newByTldIdxKey() {
	ICFIntTopDomainByTldIdxKey key =
            new CFIntJpaTopDomainByTldIdxKey();
	return( key );
    }

	public CFIntJpaTopDomainByTldIdxKey ensureByTldIdxKey(ICFIntTopDomainByTldIdxKey key) {
		if (key == null) {
			return( null );
		}
		else if (key instanceof CFIntJpaTopDomainByTldIdxKey) {
			return( (CFIntJpaTopDomainByTldIdxKey)key );
		}
		else {
			CFIntJpaTopDomainByTldIdxKey mapped = new CFIntJpaTopDomainByTldIdxKey();
			mapped.setRequiredTldId( key.getRequiredTldId() );
			return( mapped );
		}
	}

    @Override
    public ICFIntTopDomainByNameIdxKey newByNameIdxKey() {
	ICFIntTopDomainByNameIdxKey key =
            new CFIntJpaTopDomainByNameIdxKey();
	return( key );
    }

	public CFIntJpaTopDomainByNameIdxKey ensureByNameIdxKey(ICFIntTopDomainByNameIdxKey key) {
		if (key == null) {
			return( null );
		}
		else if (key instanceof CFIntJpaTopDomainByNameIdxKey) {
			return( (CFIntJpaTopDomainByNameIdxKey)key );
		}
		else {
			CFIntJpaTopDomainByNameIdxKey mapped = new CFIntJpaTopDomainByNameIdxKey();
			mapped.setRequiredTldId( key.getRequiredTldId() );
			mapped.setRequiredName( key.getRequiredName() );
			return( mapped );
		}
	}

    @Override
    public ICFIntTopDomain newRec() {
        ICFIntTopDomain rec =
            new CFIntJpaTopDomain();
        return( rec );
    }

	public CFIntJpaTopDomain ensureRec(ICFIntTopDomain rec) {
		if( rec == null ) {
			return( null );
		}
		else if (rec instanceof CFIntJpaTopDomain) {
			return( (CFIntJpaTopDomain)rec );
		}
		else {
			CFIntJpaTopDomain mapped = new CFIntJpaTopDomain();
			mapped.set(rec);
			return( mapped );
		}
	}

    @Override
    public ICFIntTopDomainH newHRec() {
        ICFIntTopDomainH hrec =
            new CFIntJpaTopDomainH();
        return( hrec );
    }

	public CFIntJpaTopDomainH ensureHRec(ICFIntTopDomainH hrec) {
		if (hrec == null) {
			return( null );
		}
		else if( hrec instanceof CFIntJpaTopDomainH) {
			return( (CFIntJpaTopDomainH)hrec );
		}
		else {
			CFIntJpaTopDomainH mapped = new CFIntJpaTopDomainH();
			mapped.set(hrec);
			return( mapped );
		}
	}
}
