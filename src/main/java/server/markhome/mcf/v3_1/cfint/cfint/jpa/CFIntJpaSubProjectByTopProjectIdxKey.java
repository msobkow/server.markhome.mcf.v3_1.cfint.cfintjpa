// Description: Java 25 JPA implementation of a SubProject by TopProjectIdx index key object.

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

public class CFIntJpaSubProjectByTopProjectIdxKey
	implements ICFIntSubProjectByTopProjectIdxKey, Comparable<Object>, Serializable
{
	protected CFLibDbKeyHash256 requiredTopProjectId;
	public CFIntJpaSubProjectByTopProjectIdxKey() {
		requiredTopProjectId = CFLibDbKeyHash256.fromHex( ICFIntSubProject.TOPPROJECTID_INIT_VALUE.toString() );
	}

	@Override
	public CFLibDbKeyHash256 getRequiredTopProjectId() {
		return( requiredTopProjectId );
	}

	@Override
	public void setRequiredTopProjectId( CFLibDbKeyHash256 value ) {
		if( value == null || value.isNull() ) {
			throw new CFLibNullArgumentException( getClass(),
				"setRequiredTopProjectId",
				1,
				"value" );
		}
		requiredTopProjectId = value;
	}

	@Override
	public boolean equals( Object obj ) {
		if (obj == null) {
			return( false );
		}
		else if (obj instanceof ICFIntSubProjectByTopProjectIdxKey) {
			ICFIntSubProjectByTopProjectIdxKey rhs = (ICFIntSubProjectByTopProjectIdxKey)obj;
			if( getRequiredTopProjectId() != null ) {
				if( rhs.getRequiredTopProjectId() != null ) {
					if( ! getRequiredTopProjectId().equals( rhs.getRequiredTopProjectId() ) ) {
						return( false );
					}
				}
				else {
					return( false );
				}
			}
			else {
				if( rhs.getRequiredTopProjectId() != null ) {
					return( false );
				}
			}
			return( true );
		}
		else if (obj instanceof ICFIntSubProject) {
			ICFIntSubProject rhs = (ICFIntSubProject)obj;
			if( getRequiredTopProjectId() != null ) {
				if( rhs.getRequiredTopProjectId() != null ) {
					if( ! getRequiredTopProjectId().equals( rhs.getRequiredTopProjectId() ) ) {
						return( false );
					}
				}
				else {
					return( false );
				}
			}
			else {
				if( rhs.getRequiredTopProjectId() != null ) {
					return( false );
				}
			}
			return( true );
		}
		else if (obj instanceof ICFIntSubProjectH) {
			ICFIntSubProjectH rhs = (ICFIntSubProjectH)obj;
			if( getRequiredTopProjectId() != null ) {
				if( rhs.getRequiredTopProjectId() != null ) {
					if( ! getRequiredTopProjectId().equals( rhs.getRequiredTopProjectId() ) ) {
						return( false );
					}
				}
				else {
					return( false );
				}
			}
			else {
				if( rhs.getRequiredTopProjectId() != null ) {
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
		hashCode = hashCode + getRequiredTopProjectId().hashCode();
		return( hashCode & 0x7fffffff );
	}

	@Override
	public int compareTo( Object obj ) {
		int cmp;
		if (obj == null) {
			return( 1 );
		}
		else if (obj instanceof ICFIntSubProjectByTopProjectIdxKey) {
			ICFIntSubProjectByTopProjectIdxKey rhs = (ICFIntSubProjectByTopProjectIdxKey)obj;
			if (getRequiredTopProjectId() != null) {
				if (rhs.getRequiredTopProjectId() != null) {
					cmp = getRequiredTopProjectId().compareTo( rhs.getRequiredTopProjectId() );
					if( cmp != 0 ) {
						return( cmp );
					}
				}
				else {
					return( 1 );
				}
			}
			else if (rhs.getRequiredTopProjectId() != null) {
				return( -1 );
			}
			return( 0 );
		}
		else if (obj instanceof ICFIntSubProject) {
			ICFIntSubProject rhs = (ICFIntSubProject)obj;
			if (getRequiredTopProjectId() != null) {
				if (rhs.getRequiredTopProjectId() != null) {
					cmp = getRequiredTopProjectId().compareTo( rhs.getRequiredTopProjectId() );
					if( cmp != 0 ) {
						return( cmp );
					}
				}
				else {
					return( 1 );
				}
			}
			else if (rhs.getRequiredTopProjectId() != null) {
				return( -1 );
			}
			return( 0 );
		}
		else if (obj instanceof ICFIntSubProjectH) {
			ICFIntSubProjectH rhs = (ICFIntSubProjectH)obj;
			if (getRequiredTopProjectId() != null) {
				if (rhs.getRequiredTopProjectId() != null) {
					cmp = getRequiredTopProjectId().compareTo( rhs.getRequiredTopProjectId() );
					if( cmp != 0 ) {
						return( cmp );
					}
				}
				else {
					return( 1 );
				}
			}
			else if (rhs.getRequiredTopProjectId() != null) {
				return( -1 );
			}
			return( 0 );
		}
		else {
			throw new CFLibUnsupportedClassException(getClass(),
				"compareTo",
				"obj",
				obj,
				"ICFIntSubProjectByTopProjectIdxKey, ICFIntSubProject, ICFIntSubProjectH");
		}
	}

	public String getXmlAttrFragment() {
		String ret = "" 
			+ " RequiredTopProjectId=" + "\"" + getRequiredTopProjectId().toString() + "\"";
		return( ret );
	}

	@Override
	public String toString() {
		String ret = "<CFIntSubProjectByTopProjectIdxKey" + getXmlAttrFragment() + "/>";
		return( ret );
	}
}
