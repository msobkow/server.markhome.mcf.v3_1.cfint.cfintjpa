
// Description: Java 25 JPA Default Factory implementation for MajorVersion.

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
 *	CFIntMajorVersionFactory JPA implementation for MajorVersion
 */
public class CFIntJpaMajorVersionDefaultFactory
    implements ICFIntMajorVersionFactory
{
    public CFIntJpaMajorVersionDefaultFactory() {
    }

    @Override
    public ICFIntMajorVersionHPKey newHPKey() {
        ICFIntMajorVersionHPKey hpkey =
            new CFIntJpaMajorVersionHPKey();
        return( hpkey );
    }

	public CFIntJpaMajorVersionHPKey ensureHPKey(ICFIntMajorVersionHPKey key) {
		if (key == null) {
			return( null );
		}
		else if( key instanceof CFIntJpaMajorVersionHPKey) {
			return( (CFIntJpaMajorVersionHPKey)key );
		}
		else {
			CFIntJpaMajorVersionHPKey mapped = new CFIntJpaMajorVersionHPKey();
			mapped.setAuditClusterId(key.getAuditClusterId());
			mapped.setAuditActionId(key.getAuditActionId());
			mapped.setAuditSessionId(key.getAuditSessionId());
			mapped.setAuditStamp(key.getAuditStamp());
			mapped.setRequiredId( key.getRequiredId() );
			return( mapped );
		}
	}

    @Override
    public ICFIntMajorVersionByTenantIdxKey newByTenantIdxKey() {
	ICFIntMajorVersionByTenantIdxKey key =
            new CFIntJpaMajorVersionByTenantIdxKey();
	return( key );
    }

	public CFIntJpaMajorVersionByTenantIdxKey ensureByTenantIdxKey(ICFIntMajorVersionByTenantIdxKey key) {
		if (key == null) {
			return( null );
		}
		else if (key instanceof CFIntJpaMajorVersionByTenantIdxKey) {
			return( (CFIntJpaMajorVersionByTenantIdxKey)key );
		}
		else {
			CFIntJpaMajorVersionByTenantIdxKey mapped = new CFIntJpaMajorVersionByTenantIdxKey();
			mapped.setRequiredTenantId( key.getRequiredTenantId() );
			return( mapped );
		}
	}

    @Override
    public ICFIntMajorVersionBySubProjectIdxKey newBySubProjectIdxKey() {
	ICFIntMajorVersionBySubProjectIdxKey key =
            new CFIntJpaMajorVersionBySubProjectIdxKey();
	return( key );
    }

	public CFIntJpaMajorVersionBySubProjectIdxKey ensureBySubProjectIdxKey(ICFIntMajorVersionBySubProjectIdxKey key) {
		if (key == null) {
			return( null );
		}
		else if (key instanceof CFIntJpaMajorVersionBySubProjectIdxKey) {
			return( (CFIntJpaMajorVersionBySubProjectIdxKey)key );
		}
		else {
			CFIntJpaMajorVersionBySubProjectIdxKey mapped = new CFIntJpaMajorVersionBySubProjectIdxKey();
			mapped.setRequiredSubProjectId( key.getRequiredSubProjectId() );
			return( mapped );
		}
	}

    @Override
    public ICFIntMajorVersionByNameIdxKey newByNameIdxKey() {
	ICFIntMajorVersionByNameIdxKey key =
            new CFIntJpaMajorVersionByNameIdxKey();
	return( key );
    }

	public CFIntJpaMajorVersionByNameIdxKey ensureByNameIdxKey(ICFIntMajorVersionByNameIdxKey key) {
		if (key == null) {
			return( null );
		}
		else if (key instanceof CFIntJpaMajorVersionByNameIdxKey) {
			return( (CFIntJpaMajorVersionByNameIdxKey)key );
		}
		else {
			CFIntJpaMajorVersionByNameIdxKey mapped = new CFIntJpaMajorVersionByNameIdxKey();
			mapped.setRequiredSubProjectId( key.getRequiredSubProjectId() );
			mapped.setRequiredName( key.getRequiredName() );
			return( mapped );
		}
	}

    @Override
    public ICFIntMajorVersion newRec() {
        ICFIntMajorVersion rec =
            new CFIntJpaMajorVersion();
        return( rec );
    }

	public CFIntJpaMajorVersion ensureRec(ICFIntMajorVersion rec) {
		if( rec == null ) {
			return( null );
		}
		else if (rec instanceof CFIntJpaMajorVersion) {
			return( (CFIntJpaMajorVersion)rec );
		}
		else {
			CFIntJpaMajorVersion mapped = new CFIntJpaMajorVersion();
			mapped.set(rec);
			return( mapped );
		}
	}

    @Override
    public ICFIntMajorVersionH newHRec() {
        ICFIntMajorVersionH hrec =
            new CFIntJpaMajorVersionH();
        return( hrec );
    }

	public CFIntJpaMajorVersionH ensureHRec(ICFIntMajorVersionH hrec) {
		if (hrec == null) {
			return( null );
		}
		else if( hrec instanceof CFIntJpaMajorVersionH) {
			return( (CFIntJpaMajorVersionH)hrec );
		}
		else {
			CFIntJpaMajorVersionH mapped = new CFIntJpaMajorVersionH();
			mapped.set(hrec);
			return( mapped );
		}
	}
}
