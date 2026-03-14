
// Description: Java 25 JPA Default Factory implementation for License.

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
 *	CFIntLicenseFactory JPA implementation for License
 */
public class CFIntJpaLicenseDefaultFactory
    implements ICFIntLicenseFactory
{
    public CFIntJpaLicenseDefaultFactory() {
    }

    @Override
    public ICFIntLicenseByLicnTenantIdxKey newByLicnTenantIdxKey() {
	ICFIntLicenseByLicnTenantIdxKey key =
            new CFIntJpaLicenseByLicnTenantIdxKey();
	return( key );
    }

	public CFIntJpaLicenseByLicnTenantIdxKey ensureByLicnTenantIdxKey(ICFIntLicenseByLicnTenantIdxKey key) {
		if (key == null) {
			return( null );
		}
		else if (key instanceof CFIntJpaLicenseByLicnTenantIdxKey) {
			return( (CFIntJpaLicenseByLicnTenantIdxKey)key );
		}
		else {
			CFIntJpaLicenseByLicnTenantIdxKey mapped = new CFIntJpaLicenseByLicnTenantIdxKey();
			mapped.setRequiredTenantId( key.getRequiredTenantId() );
			return( mapped );
		}
	}

    @Override
    public ICFIntLicenseByDomainIdxKey newByDomainIdxKey() {
	ICFIntLicenseByDomainIdxKey key =
            new CFIntJpaLicenseByDomainIdxKey();
	return( key );
    }

	public CFIntJpaLicenseByDomainIdxKey ensureByDomainIdxKey(ICFIntLicenseByDomainIdxKey key) {
		if (key == null) {
			return( null );
		}
		else if (key instanceof CFIntJpaLicenseByDomainIdxKey) {
			return( (CFIntJpaLicenseByDomainIdxKey)key );
		}
		else {
			CFIntJpaLicenseByDomainIdxKey mapped = new CFIntJpaLicenseByDomainIdxKey();
			mapped.setRequiredTopDomainId( key.getRequiredTopDomainId() );
			return( mapped );
		}
	}

    @Override
    public ICFIntLicenseByUNameIdxKey newByUNameIdxKey() {
	ICFIntLicenseByUNameIdxKey key =
            new CFIntJpaLicenseByUNameIdxKey();
	return( key );
    }

	public CFIntJpaLicenseByUNameIdxKey ensureByUNameIdxKey(ICFIntLicenseByUNameIdxKey key) {
		if (key == null) {
			return( null );
		}
		else if (key instanceof CFIntJpaLicenseByUNameIdxKey) {
			return( (CFIntJpaLicenseByUNameIdxKey)key );
		}
		else {
			CFIntJpaLicenseByUNameIdxKey mapped = new CFIntJpaLicenseByUNameIdxKey();
			mapped.setRequiredTopDomainId( key.getRequiredTopDomainId() );
			mapped.setRequiredName( key.getRequiredName() );
			return( mapped );
		}
	}

    @Override
    public ICFIntLicense newRec() {
        ICFIntLicense rec =
            new CFIntJpaLicense();
        return( rec );
    }

	public CFIntJpaLicense ensureRec(ICFIntLicense rec) {
		if( rec == null ) {
			return( null );
		}
		else if (rec instanceof CFIntJpaLicense) {
			return( (CFIntJpaLicense)rec );
		}
		else {
			CFIntJpaLicense mapped = new CFIntJpaLicense();
			mapped.set(rec);
			return( mapped );
		}
	}
}
