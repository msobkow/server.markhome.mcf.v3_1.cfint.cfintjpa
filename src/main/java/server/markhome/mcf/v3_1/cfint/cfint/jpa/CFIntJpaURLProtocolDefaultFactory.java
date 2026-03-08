
// Description: Java 25 JPA Default Factory implementation for URLProtocol.

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
 *	CFIntURLProtocolFactory JPA implementation for URLProtocol
 */
public class CFIntJpaURLProtocolDefaultFactory
    implements ICFIntURLProtocolFactory
{
    public CFIntJpaURLProtocolDefaultFactory() {
    }

    @Override
    public ICFIntURLProtocolHPKey newHPKey() {
        ICFIntURLProtocolHPKey hpkey =
            new CFIntJpaURLProtocolHPKey();
        return( hpkey );
    }

	public CFIntJpaURLProtocolHPKey ensureHPKey(ICFIntURLProtocolHPKey key) {
		if (key == null) {
			return( null );
		}
		else if( key instanceof CFIntJpaURLProtocolHPKey) {
			return( (CFIntJpaURLProtocolHPKey)key );
		}
		else {
			CFIntJpaURLProtocolHPKey mapped = new CFIntJpaURLProtocolHPKey();
			mapped.setAuditClusterId(key.getAuditClusterId());
			mapped.setAuditActionId(key.getAuditActionId());
			mapped.setAuditSessionId(key.getAuditSessionId());
			mapped.setAuditStamp(key.getAuditStamp());
			mapped.setRequiredURLProtocolId( key.getRequiredURLProtocolId() );
			return( mapped );
		}
	}

    @Override
    public ICFIntURLProtocolByUNameIdxKey newByUNameIdxKey() {
	ICFIntURLProtocolByUNameIdxKey key =
            new CFIntJpaURLProtocolByUNameIdxKey();
	return( key );
    }

	public CFIntJpaURLProtocolByUNameIdxKey ensureByUNameIdxKey(ICFIntURLProtocolByUNameIdxKey key) {
		if (key == null) {
			return( null );
		}
		else if (key instanceof CFIntJpaURLProtocolByUNameIdxKey) {
			return( (CFIntJpaURLProtocolByUNameIdxKey)key );
		}
		else {
			CFIntJpaURLProtocolByUNameIdxKey mapped = new CFIntJpaURLProtocolByUNameIdxKey();
			mapped.setRequiredName( key.getRequiredName() );
			return( mapped );
		}
	}

    @Override
    public ICFIntURLProtocolByIsSecureIdxKey newByIsSecureIdxKey() {
	ICFIntURLProtocolByIsSecureIdxKey key =
            new CFIntJpaURLProtocolByIsSecureIdxKey();
	return( key );
    }

	public CFIntJpaURLProtocolByIsSecureIdxKey ensureByIsSecureIdxKey(ICFIntURLProtocolByIsSecureIdxKey key) {
		if (key == null) {
			return( null );
		}
		else if (key instanceof CFIntJpaURLProtocolByIsSecureIdxKey) {
			return( (CFIntJpaURLProtocolByIsSecureIdxKey)key );
		}
		else {
			CFIntJpaURLProtocolByIsSecureIdxKey mapped = new CFIntJpaURLProtocolByIsSecureIdxKey();
			mapped.setRequiredIsSecure( key.getRequiredIsSecure() );
			return( mapped );
		}
	}

    @Override
    public ICFIntURLProtocol newRec() {
        ICFIntURLProtocol rec =
            new CFIntJpaURLProtocol();
        return( rec );
    }

	public CFIntJpaURLProtocol ensureRec(ICFIntURLProtocol rec) {
		if( rec == null ) {
			return( null );
		}
		else if (rec instanceof CFIntJpaURLProtocol) {
			return( (CFIntJpaURLProtocol)rec );
		}
		else {
			CFIntJpaURLProtocol mapped = new CFIntJpaURLProtocol();
			mapped.set(rec);
			return( mapped );
		}
	}

    @Override
    public ICFIntURLProtocolH newHRec() {
        ICFIntURLProtocolH hrec =
            new CFIntJpaURLProtocolH();
        return( hrec );
    }

	public CFIntJpaURLProtocolH ensureHRec(ICFIntURLProtocolH hrec) {
		if (hrec == null) {
			return( null );
		}
		else if( hrec instanceof CFIntJpaURLProtocolH) {
			return( (CFIntJpaURLProtocolH)hrec );
		}
		else {
			CFIntJpaURLProtocolH mapped = new CFIntJpaURLProtocolH();
			mapped.set(hrec);
			return( mapped );
		}
	}
}
