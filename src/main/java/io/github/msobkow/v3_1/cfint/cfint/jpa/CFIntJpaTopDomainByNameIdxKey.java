// Description: Java 25 JPA implementation of a TopDomain by NameIdx index key object.

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

public class CFIntJpaTopDomainByNameIdxKey
	implements ICFIntTopDomainByNameIdxKey, Comparable<Object>, Serializable
{
	protected CFLibDbKeyHash256 requiredTldId;
	protected String requiredName;
	public CFIntJpaTopDomainByNameIdxKey() {
		requiredTldId = CFLibDbKeyHash256.fromHex( ICFIntTopDomain.TLDID_INIT_VALUE.toString() );
		requiredName = ICFIntTopDomain.NAME_INIT_VALUE;
	}

	@Override
	public CFLibDbKeyHash256 getRequiredTldId() {
		return( requiredTldId );
	}

	@Override
	public void setRequiredTldId( CFLibDbKeyHash256 value ) {
		if( value == null || value.isNull() ) {
			throw new CFLibNullArgumentException( getClass(),
				"setRequiredTldId",
				1,
				"value" );
		}
		requiredTldId = value;
	}

	@Override
	public String getRequiredName() {
		return( requiredName );
	}

	@Override
	public void setRequiredName( String value ) {
		if( value == null ) {
			throw new CFLibNullArgumentException( getClass(),
				"setRequiredName",
				1,
				"value" );
		}
		else if( value.length() > 64 ) {
			throw new CFLibArgumentOverflowException( getClass(),
				"setRequiredName",
				1,
				"value.length()",
				value.length(),
				64 );
		}
		requiredName = value;
	}

	@Override
	public boolean equals( Object obj ) {
		if (obj == null) {
			return( false );
		}
		else if (obj instanceof ICFIntTopDomainByNameIdxKey) {
			ICFIntTopDomainByNameIdxKey rhs = (ICFIntTopDomainByNameIdxKey)obj;
			if( getRequiredTldId() != null && !getRequiredTldId().isNull() ) {
				if( rhs.getRequiredTldId() != null && !rhs.getRequiredTldId().isNull() ) {
					if( ! getRequiredTldId().equals( rhs.getRequiredTldId() ) ) {
						return( false );
					}
				}
				else {
					return( false );
				}
			}
			else {
				if( rhs.getRequiredTldId() != null && !getRequiredTldId().isNull()) {
					return( false );
				}
			}
			if( getRequiredName() != null ) {
				if( rhs.getRequiredName() != null ) {
					if( ! getRequiredName().equals( rhs.getRequiredName() ) ) {
						return( false );
					}
				}
				else {
					return( false );
				}
			}
			else {
				if( rhs.getRequiredName() != null ) {
					return( false );
				}
			}
			return( true );
		}
		else if (obj instanceof ICFIntTopDomain) {
			ICFIntTopDomain rhs = (ICFIntTopDomain)obj;
			if( getRequiredTldId() != null && !getRequiredTldId().isNull() ) {
				if( rhs.getRequiredTldId() != null && !rhs.getRequiredTldId().isNull() ) {
					if( ! getRequiredTldId().equals( rhs.getRequiredTldId() ) ) {
						return( false );
					}
				}
				else {
					return( false );
				}
			}
			else {
				if( rhs.getRequiredTldId() != null && !getRequiredTldId().isNull()) {
					return( false );
				}
			}
			if( getRequiredName() != null ) {
				if( rhs.getRequiredName() != null ) {
					if( ! getRequiredName().equals( rhs.getRequiredName() ) ) {
						return( false );
					}
				}
				else {
					return( false );
				}
			}
			else {
				if( rhs.getRequiredName() != null ) {
					return( false );
				}
			}
			return( true );
		}
		else if (obj instanceof ICFIntTopDomainH) {
			ICFIntTopDomainH rhs = (ICFIntTopDomainH)obj;
			if( getRequiredTldId() != null && !getRequiredTldId().isNull() ) {
				if( rhs.getRequiredTldId() != null && !rhs.getRequiredTldId().isNull() ) {
					if( ! getRequiredTldId().equals( rhs.getRequiredTldId() ) ) {
						return( false );
					}
				}
				else {
					return( false );
				}
			}
			else {
				if( rhs.getRequiredTldId() != null && !getRequiredTldId().isNull()) {
					return( false );
				}
			}
			if( getRequiredName() != null ) {
				if( rhs.getRequiredName() != null ) {
					if( ! getRequiredName().equals( rhs.getRequiredName() ) ) {
						return( false );
					}
				}
				else {
					return( false );
				}
			}
			else {
				if( rhs.getRequiredName() != null ) {
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
		hashCode = hashCode + getRequiredTldId().hashCode();
		if( getRequiredName() != null ) {
			hashCode = hashCode + getRequiredName().hashCode();
		}
		return( hashCode & 0x7fffffff );
	}

	@Override
	public int compareTo( Object obj ) {
		int cmp;
		if (obj == null) {
			return( 1 );
		}
		else if (obj instanceof ICFIntTopDomainByNameIdxKey) {
			ICFIntTopDomainByNameIdxKey rhs = (ICFIntTopDomainByNameIdxKey)obj;
			if (getRequiredTldId() != null) {
				if (rhs.getRequiredTldId() != null) {
					cmp = getRequiredTldId().compareTo( rhs.getRequiredTldId() );
					if( cmp != 0 ) {
						return( cmp );
					}
				}
				else {
					return( 1 );
				}
			}
			else if (rhs.getRequiredTldId() != null) {
				return( -1 );
			}
			if (getRequiredName() != null) {
				if (rhs.getRequiredName() != null) {
					cmp = getRequiredName().compareTo( rhs.getRequiredName() );
					if( cmp != 0 ) {
						return( cmp );
					}
				}
				else {
					return( 1 );
				}
			}
			else if (rhs.getRequiredName() != null) {
				return( -1 );
			}
			return( 0 );
		}
		else if (obj instanceof ICFIntTopDomain) {
			ICFIntTopDomain rhs = (ICFIntTopDomain)obj;
			if (getRequiredTldId() != null) {
				if (rhs.getRequiredTldId() != null) {
					cmp = getRequiredTldId().compareTo( rhs.getRequiredTldId() );
					if( cmp != 0 ) {
						return( cmp );
					}
				}
				else {
					return( 1 );
				}
			}
			else if (rhs.getRequiredTldId() != null) {
				return( -1 );
			}
			if (getRequiredName() != null) {
				if (rhs.getRequiredName() != null) {
					cmp = getRequiredName().compareTo( rhs.getRequiredName() );
					if( cmp != 0 ) {
						return( cmp );
					}
				}
				else {
					return( 1 );
				}
			}
			else if (rhs.getRequiredName() != null) {
				return( -1 );
			}
			return( 0 );
		}
		else if (obj instanceof ICFIntTopDomainH) {
			ICFIntTopDomainH rhs = (ICFIntTopDomainH)obj;
			if (getRequiredTldId() != null) {
				if (rhs.getRequiredTldId() != null) {
					cmp = getRequiredTldId().compareTo( rhs.getRequiredTldId() );
					if( cmp != 0 ) {
						return( cmp );
					}
				}
				else {
					return( 1 );
				}
			}
			else if (rhs.getRequiredTldId() != null) {
				return( -1 );
			}
			if (getRequiredName() != null) {
				if (rhs.getRequiredName() != null) {
					cmp = getRequiredName().compareTo( rhs.getRequiredName() );
					if( cmp != 0 ) {
						return( cmp );
					}
				}
				else {
					return( 1 );
				}
			}
			else if (rhs.getRequiredName() != null) {
				return( -1 );
			}
			return( 0 );
		}
		else {
			throw new CFLibUnsupportedClassException(getClass(),
				"compareTo",
				"obj",
				obj,
				"ICFIntTopDomainByNameIdxKey, ICFIntTopDomain, ICFIntTopDomainH");
		}
	}

	public String getXmlAttrFragment() {
		String ret = "" 
			+ " RequiredTldId=" + "\"" + getRequiredTldId().toString() + "\""
			+ " RequiredName=" + "\"" + StringEscapeUtils.escapeXml11( getRequiredName() ) + "\"";
		return( ret );
	}

	@Override
	public String toString() {
		String ret = "<CFIntTopDomainByNameIdxKey" + getXmlAttrFragment() + "/>";
		return( ret );
	}
}
