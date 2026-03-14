
// Description: Java 25 JPA Default Factory implementation for Tld.

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
 *	CFIntTldFactory JPA implementation for Tld
 */
public class CFIntJpaTldDefaultFactory
    implements ICFIntTldFactory
{
    public CFIntJpaTldDefaultFactory() {
    }

    @Override
    public ICFIntTldHPKey newHPKey() {
        ICFIntTldHPKey hpkey =
            new CFIntJpaTldHPKey();
        return( hpkey );
    }

	public CFIntJpaTldHPKey ensureHPKey(ICFIntTldHPKey key) {
		if (key == null) {
			return( null );
		}
		else if( key instanceof CFIntJpaTldHPKey) {
			return( (CFIntJpaTldHPKey)key );
		}
		else {
			CFIntJpaTldHPKey mapped = new CFIntJpaTldHPKey();
			mapped.setAuditClusterId(key.getAuditClusterId());
			mapped.setAuditActionId(key.getAuditActionId());
			mapped.setAuditSessionId(key.getAuditSessionId());
			mapped.setAuditStamp(key.getAuditStamp());
			mapped.setRequiredId( key.getRequiredId() );
			return( mapped );
		}
	}

    @Override
    public ICFIntTldByTenantIdxKey newByTenantIdxKey() {
	ICFIntTldByTenantIdxKey key =
            new CFIntJpaTldByTenantIdxKey();
	return( key );
    }

	public CFIntJpaTldByTenantIdxKey ensureByTenantIdxKey(ICFIntTldByTenantIdxKey key) {
		if (key == null) {
			return( null );
		}
		else if (key instanceof CFIntJpaTldByTenantIdxKey) {
			return( (CFIntJpaTldByTenantIdxKey)key );
		}
		else {
			CFIntJpaTldByTenantIdxKey mapped = new CFIntJpaTldByTenantIdxKey();
			mapped.setRequiredTenantId( key.getRequiredTenantId() );
			return( mapped );
		}
	}

    @Override
    public ICFIntTldByNameIdxKey newByNameIdxKey() {
	ICFIntTldByNameIdxKey key =
            new CFIntJpaTldByNameIdxKey();
	return( key );
    }

	public CFIntJpaTldByNameIdxKey ensureByNameIdxKey(ICFIntTldByNameIdxKey key) {
		if (key == null) {
			return( null );
		}
		else if (key instanceof CFIntJpaTldByNameIdxKey) {
			return( (CFIntJpaTldByNameIdxKey)key );
		}
		else {
			CFIntJpaTldByNameIdxKey mapped = new CFIntJpaTldByNameIdxKey();
			mapped.setRequiredName( key.getRequiredName() );
			return( mapped );
		}
	}

    @Override
    public ICFIntTld newRec() {
        ICFIntTld rec =
            new CFIntJpaTld();
        return( rec );
    }

	public CFIntJpaTld ensureRec(ICFIntTld rec) {
		if( rec == null ) {
			return( null );
		}
		else if (rec instanceof CFIntJpaTld) {
			return( (CFIntJpaTld)rec );
		}
		else {
			CFIntJpaTld mapped = new CFIntJpaTld();
			mapped.set(rec);
			return( mapped );
		}
	}

    @Override
    public ICFIntTldH newHRec() {
        ICFIntTldH hrec =
            new CFIntJpaTldH();
        return( hrec );
    }

	public CFIntJpaTldH ensureHRec(ICFIntTldH hrec) {
		if (hrec == null) {
			return( null );
		}
		else if( hrec instanceof CFIntJpaTldH) {
			return( (CFIntJpaTldH)hrec );
		}
		else {
			CFIntJpaTldH mapped = new CFIntJpaTldH();
			mapped.set(hrec);
			return( mapped );
		}
	}
}
