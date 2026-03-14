// Description: Java 25 JPA implementation of a MinorVersion by MajorVerIdx index key object.

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

import java.io.Serializable;
import java.math.*;
import java.time.*;
import java.util.*;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.text.StringEscapeUtils;
import server.markhome.mcf.v3_1.cflib.*;
import server.markhome.mcf.v3_1.cflib.dbutil.*;
import server.markhome.mcf.v3_1.cflib.xml.CFLibXmlUtil;
import server.markhome.mcf.v3_1.cfsec.cfsec.*;
import server.markhome.mcf.v3_1.cfint.cfint.*;
import server.markhome.mcf.v3_1.cfsec.cfsec.jpa.*;

public class CFIntJpaMinorVersionByMajorVerIdxKey
	implements ICFIntMinorVersionByMajorVerIdxKey, Comparable<Object>, Serializable
{
	protected CFLibDbKeyHash256 requiredMajorVersionId;
	public CFIntJpaMinorVersionByMajorVerIdxKey() {
		requiredMajorVersionId = CFLibDbKeyHash256.fromHex( ICFIntMinorVersion.MAJORVERSIONID_INIT_VALUE.toString() );
	}

	@Override
	public CFLibDbKeyHash256 getRequiredMajorVersionId() {
		return( requiredMajorVersionId );
	}

	@Override
	public void setRequiredMajorVersionId( CFLibDbKeyHash256 value ) {
		if( value == null || value.isNull() ) {
			throw new CFLibNullArgumentException( getClass(),
				"setRequiredMajorVersionId",
				1,
				"value" );
		}
		requiredMajorVersionId = value;
	}

	@Override
	public boolean equals( Object obj ) {
		if (obj == null) {
			return( false );
		}
		else if (obj instanceof ICFIntMinorVersionByMajorVerIdxKey) {
			ICFIntMinorVersionByMajorVerIdxKey rhs = (ICFIntMinorVersionByMajorVerIdxKey)obj;
			if( getRequiredMajorVersionId() != null ) {
				if( rhs.getRequiredMajorVersionId() != null ) {
					if( ! getRequiredMajorVersionId().equals( rhs.getRequiredMajorVersionId() ) ) {
						return( false );
					}
				}
				else {
					return( false );
				}
			}
			else {
				if( rhs.getRequiredMajorVersionId() != null ) {
					return( false );
				}
			}
			return( true );
		}
		else if (obj instanceof ICFIntMinorVersion) {
			ICFIntMinorVersion rhs = (ICFIntMinorVersion)obj;
			if( getRequiredMajorVersionId() != null ) {
				if( rhs.getRequiredMajorVersionId() != null ) {
					if( ! getRequiredMajorVersionId().equals( rhs.getRequiredMajorVersionId() ) ) {
						return( false );
					}
				}
				else {
					return( false );
				}
			}
			else {
				if( rhs.getRequiredMajorVersionId() != null ) {
					return( false );
				}
			}
			return( true );
		}
		else if (obj instanceof ICFIntMinorVersionH) {
			ICFIntMinorVersionH rhs = (ICFIntMinorVersionH)obj;
			if( getRequiredMajorVersionId() != null ) {
				if( rhs.getRequiredMajorVersionId() != null ) {
					if( ! getRequiredMajorVersionId().equals( rhs.getRequiredMajorVersionId() ) ) {
						return( false );
					}
				}
				else {
					return( false );
				}
			}
			else {
				if( rhs.getRequiredMajorVersionId() != null ) {
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
		hashCode = hashCode + getRequiredMajorVersionId().hashCode();
		return( hashCode & 0x7fffffff );
	}

	@Override
	public int compareTo( Object obj ) {
		int cmp;
		if (obj == null) {
			return( 1 );
		}
		else if (obj instanceof ICFIntMinorVersionByMajorVerIdxKey) {
			ICFIntMinorVersionByMajorVerIdxKey rhs = (ICFIntMinorVersionByMajorVerIdxKey)obj;
			if (getRequiredMajorVersionId() != null) {
				if (rhs.getRequiredMajorVersionId() != null) {
					cmp = getRequiredMajorVersionId().compareTo( rhs.getRequiredMajorVersionId() );
					if( cmp != 0 ) {
						return( cmp );
					}
				}
				else {
					return( 1 );
				}
			}
			else if (rhs.getRequiredMajorVersionId() != null) {
				return( -1 );
			}
			return( 0 );
		}
		else if (obj instanceof ICFIntMinorVersion) {
			ICFIntMinorVersion rhs = (ICFIntMinorVersion)obj;
			if (getRequiredMajorVersionId() != null) {
				if (rhs.getRequiredMajorVersionId() != null) {
					cmp = getRequiredMajorVersionId().compareTo( rhs.getRequiredMajorVersionId() );
					if( cmp != 0 ) {
						return( cmp );
					}
				}
				else {
					return( 1 );
				}
			}
			else if (rhs.getRequiredMajorVersionId() != null) {
				return( -1 );
			}
			return( 0 );
		}
		else if (obj instanceof ICFIntMinorVersionH) {
			ICFIntMinorVersionH rhs = (ICFIntMinorVersionH)obj;
			if (getRequiredMajorVersionId() != null) {
				if (rhs.getRequiredMajorVersionId() != null) {
					cmp = getRequiredMajorVersionId().compareTo( rhs.getRequiredMajorVersionId() );
					if( cmp != 0 ) {
						return( cmp );
					}
				}
				else {
					return( 1 );
				}
			}
			else if (rhs.getRequiredMajorVersionId() != null) {
				return( -1 );
			}
			return( 0 );
		}
		else {
			throw new CFLibUnsupportedClassException(getClass(),
				"compareTo",
				"obj",
				obj,
				"ICFIntMinorVersionByMajorVerIdxKey, ICFIntMinorVersion, ICFIntMinorVersionH");
		}
	}

	public String getXmlAttrFragment() {
		String ret = "" 
			+ " RequiredMajorVersionId=" + "\"" + getRequiredMajorVersionId().toString() + "\"";
		return( ret );
	}

	@Override
	public String toString() {
		String ret = "<CFIntMinorVersionByMajorVerIdxKey" + getXmlAttrFragment() + "/>";
		return( ret );
	}
}
