
// Description: Java 25 JPA Default Factory implementation for License.

/*
 *	io.github.msobkow.CFInt
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

package io.github.msobkow.v3_1.cfint.cfint.jpa;

import java.lang.reflect.*;
import java.net.*;
import java.rmi.*;
import java.sql.*;
import java.text.*;
import java.util.*;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.text.StringEscapeUtils;
import io.github.msobkow.v3_1.cflib.*;
import io.github.msobkow.v3_1.cflib.dbutil.*;
import io.github.msobkow.v3_1.cfsec.cfsec.*;
import io.github.msobkow.v3_1.cfint.cfint.*;
import io.github.msobkow.v3_1.cfsec.cfsec.jpa.*;

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
