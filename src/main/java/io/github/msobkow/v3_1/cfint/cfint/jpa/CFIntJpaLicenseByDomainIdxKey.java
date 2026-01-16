// Description: Java 25 JPA implementation of a License by DomainIdx index key object.

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

import java.io.Serializable;
import java.math.*;
import java.time.*;
import java.util.*;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.text.StringEscapeUtils;
import io.github.msobkow.v3_1.cflib.*;
import io.github.msobkow.v3_1.cflib.dbutil.*;
import io.github.msobkow.v3_1.cflib.xml.CFLibXmlUtil;
import io.github.msobkow.v3_1.cfsec.cfsec.*;
import io.github.msobkow.v3_1.cfint.cfint.*;
import io.github.msobkow.v3_1.cfsec.cfsec.jpa.*;

public class CFIntJpaLicenseByDomainIdxKey
	implements ICFIntLicenseByDomainIdxKey, Comparable<Object>, Serializable
{
	protected CFLibDbKeyHash256 requiredTopDomainId;
	public CFIntJpaLicenseByDomainIdxKey() {
		requiredTopDomainId = CFLibDbKeyHash256.fromHex( ICFIntLicense.TOPDOMAINID_INIT_VALUE.toString() );
	}

	@Override
	public CFLibDbKeyHash256 getRequiredTopDomainId() {
		return( requiredTopDomainId );
	}

	@Override
	public void setRequiredTopDomainId( CFLibDbKeyHash256 value ) {
		if( value == null || value.isNull() ) {
			throw new CFLibNullArgumentException( getClass(),
				"setRequiredTopDomainId",
				1,
				"value" );
		}
		requiredTopDomainId = value;
	}

	@Override
	public boolean equals( Object obj ) {
		if (obj == null) {
			return( false );
		}
		else if (obj instanceof ICFIntLicenseByDomainIdxKey) {
			ICFIntLicenseByDomainIdxKey rhs = (ICFIntLicenseByDomainIdxKey)obj;
			if( getRequiredTopDomainId() != null && !getRequiredTopDomainId().isNull() ) {
				if( rhs.getRequiredTopDomainId() != null && !rhs.getRequiredTopDomainId().isNull() ) {
					if( ! getRequiredTopDomainId().equals( rhs.getRequiredTopDomainId() ) ) {
						return( false );
					}
				}
				else {
					return( false );
				}
			}
			else {
				if( rhs.getRequiredTopDomainId() != null && !getRequiredTopDomainId().isNull()) {
					return( false );
				}
			}
			return( true );
		}
		else if (obj instanceof ICFIntLicense) {
			ICFIntLicense rhs = (ICFIntLicense)obj;
			if( getRequiredTopDomainId() != null && !getRequiredTopDomainId().isNull() ) {
				if( rhs.getRequiredTopDomainId() != null && !rhs.getRequiredTopDomainId().isNull() ) {
					if( ! getRequiredTopDomainId().equals( rhs.getRequiredTopDomainId() ) ) {
						return( false );
					}
				}
				else {
					return( false );
				}
			}
			else {
				if( rhs.getRequiredTopDomainId() != null && !getRequiredTopDomainId().isNull()) {
					return( false );
				}
			}
			return( true );
		}
		else {
			return( false );
		}
	}

	@Override
	public int hashCode() {
		int hashCode = 0;
		hashCode = hashCode + getRequiredTopDomainId().hashCode();
		return( hashCode & 0x7fffffff );
	}

	@Override
	public int compareTo( Object obj ) {
		int cmp;
		if (obj == null) {
			return( 1 );
		}
		else if (obj instanceof ICFIntLicenseByDomainIdxKey) {
			ICFIntLicenseByDomainIdxKey rhs = (ICFIntLicenseByDomainIdxKey)obj;
			if (getRequiredTopDomainId() != null) {
				if (rhs.getRequiredTopDomainId() != null) {
					cmp = getRequiredTopDomainId().compareTo( rhs.getRequiredTopDomainId() );
					if( cmp != 0 ) {
						return( cmp );
					}
				}
				else {
					return( 1 );
				}
			}
			else if (rhs.getRequiredTopDomainId() != null) {
				return( -1 );
			}
			return( 0 );
		}
		else if (obj instanceof ICFIntLicense) {
			ICFIntLicense rhs = (ICFIntLicense)obj;
			if (getRequiredTopDomainId() != null) {
				if (rhs.getRequiredTopDomainId() != null) {
					cmp = getRequiredTopDomainId().compareTo( rhs.getRequiredTopDomainId() );
					if( cmp != 0 ) {
						return( cmp );
					}
				}
				else {
					return( 1 );
				}
			}
			else if (rhs.getRequiredTopDomainId() != null) {
				return( -1 );
			}
			return( 0 );
		}
		else {
			throw new CFLibUnsupportedClassException(getClass(),
				"compareTo",
				"obj",
				obj,
				"ICFIntLicenseByDomainIdxKey, ICFIntLicense");
		}
	}

	public String getXmlAttrFragment() {
		String ret = "" 
			+ " RequiredTopDomainId=" + "\"" + getRequiredTopDomainId().toString() + "\"";
		return( ret );
	}

	@Override
	public String toString() {
		String ret = "<CFIntLicenseByDomainIdxKey" + getXmlAttrFragment() + "/>";
		return( ret );
	}
}
