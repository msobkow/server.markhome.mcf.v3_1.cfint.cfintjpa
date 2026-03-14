// Description: Java 25 JPA implementation of a MimeType entity definition object.

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

@Entity
@Table(
	name = "MimeType", schema = "CFInt31",
	indexes = {
		@Index(name = "MimeTypeIdIdx", columnList = "MimeTypeId", unique = true),
		@Index(name = "MimeTypeUNameIdx", columnList = "safe_name", unique = true)
	}
)
@Transactional(Transactional.TxType.SUPPORTS)
@PersistenceContext(unitName = "CFIntPU")
public class CFIntJpaMimeType
	implements Comparable<Object>,
		ICFIntMimeType,
		Serializable
{
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="MimeTypeIdGenSeq")
	@SequenceGenerator(name = "MimeTypeIdGenSeq", allocationSize = 1, initialValue = 0, schema = "CFInt31")
	@Column( name="MimeTypeId", nullable=false )
	protected int requiredMimeTypeId;
	protected int requiredRevision;


	@AttributeOverrides({
		@AttributeOverride( name="bytes", column = @Column( name="CreatedByUserId", nullable=false, length=CFLibDbKeyHash256.HASH_LENGTH ) )
	})
	protected CFLibDbKeyHash256 createdByUserId = CFLibDbKeyHash256.fromHex(ICFIntMimeType.S_INIT_CREATED_BY);

	@Column(name="CreatedAt", nullable=false)
	protected LocalDateTime createdAt = LocalDateTime.now();

	@AttributeOverrides({
		@AttributeOverride( name="bytes", column= @Column( name="UpdatedByUserId", nullable=false, length=CFLibDbKeyHash256.HASH_LENGTH ) )
	})
	protected CFLibDbKeyHash256 updatedByUserId = CFLibDbKeyHash256.fromHex(ICFIntMimeType.S_INIT_UPDATED_BY);

	@Column(name="UpdatedAt", nullable=false)
	protected LocalDateTime updatedAt = LocalDateTime.now();
	@Column( name="safe_name", nullable=false, length=128 )
	protected String requiredName;
	@Column( name="FileTypes", nullable=true, length=128 )
	protected String optionalFileTypes;

	public CFIntJpaMimeType() {
		requiredMimeTypeId = ICFIntMimeType.MIMETYPEID_INIT_VALUE;
		requiredName = ICFIntMimeType.NAME_INIT_VALUE;
		optionalFileTypes = null;
	}

	@Override
	public int getClassCode() {
		return( ICFIntMimeType.CLASS_CODE );
	}

	@Override
	public CFLibDbKeyHash256 getCreatedByUserId() {
		return( createdByUserId );
	}

	@Override
	public void setCreatedByUserId( CFLibDbKeyHash256 value ) {
		if (value == null || value.isNull()) {
			throw new CFLibNullArgumentException(getClass(), "setCreatedByUserId", 1, "value");
		}
		createdByUserId = value;
	}

	@Override
	public LocalDateTime getCreatedAt() {
		return( createdAt );
	}

	@Override
	public void setCreatedAt( LocalDateTime value ) {
		if (value == null) {
			throw new CFLibNullArgumentException(getClass(), "setCreatedAt", 1, "value");
		}
		createdAt = value;
	}

	@Override
	public CFLibDbKeyHash256 getUpdatedByUserId() {
		return( updatedByUserId );
	}

	@Override
	public void setUpdatedByUserId( CFLibDbKeyHash256 value ) {
		if (value == null || value.isNull()) {
			throw new CFLibNullArgumentException(getClass(), "setUpdatedByUserId", 1, "value");
		}
		updatedByUserId = value;
	}

	@Override
	public LocalDateTime getUpdatedAt() {
		return( updatedAt );
	}

	@Override
	public void setUpdatedAt( LocalDateTime value ) {
		if (value == null) {
			throw new CFLibNullArgumentException(getClass(), "setUpdatedAt", 1, "value");
		}
		updatedAt = value;
	}

	@Override
	public Integer getPKey() {
		return getRequiredMimeTypeId();
	}

	@Override
	public void setPKey(Integer requiredMimeTypeId) {
		if (requiredMimeTypeId != null) {
			setRequiredMimeTypeId(requiredMimeTypeId);
		}
	}
	
	@Override
	public int getRequiredMimeTypeId() {
		return( requiredMimeTypeId );
	}

	@Override
	public void setRequiredMimeTypeId( int value ) {
		if( value < ICFIntMimeType.MIMETYPEID_MIN_VALUE ) {
			throw new CFLibArgumentUnderflowException( getClass(),
				"setRequiredMimeTypeId",
				1,
				"value",
				value,
				ICFIntMimeType.MIMETYPEID_MIN_VALUE );
		}
		requiredMimeTypeId = value;
	}

	
	@Override
	public int getRequiredRevision() {
		return( requiredRevision );
	}

	@Override
	public void setRequiredRevision( int value ) {
		requiredRevision = value;
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
		else if( value.length() > 128 ) {
			throw new CFLibArgumentOverflowException( getClass(),
				"setRequiredName",
				1,
				"value.length()",
				value.length(),
				128 );
		}
		requiredName = value;
	}

	@Override
	public String getOptionalFileTypes() {
		return( optionalFileTypes );
	}

	@Override
	public void setOptionalFileTypes( String value ) {
		if( value != null && value.length() > 128 ) {
			throw new CFLibArgumentOverflowException( getClass(),
				"setOptionalFileTypes",
				1,
				"value.length()",
				value.length(),
				128 );
		}
		optionalFileTypes = value;
	}

	@Override
	public boolean equals( Object obj ) {
		if (obj == null) {
			return( false );
		}
		else if (obj instanceof ICFIntMimeType) {
			ICFIntMimeType rhs = (ICFIntMimeType)obj;
			if( ! getCreatedByUserId().equals( rhs.getCreatedByUserId() ) ) {
				return( false );
			}
			if( ! getCreatedAt().equals( rhs.getCreatedAt() ) ) {
				return( false );
			}
			if( ! getUpdatedByUserId().equals( rhs.getUpdatedByUserId() ) ) {
				return( false );
			}
			if( ! getUpdatedAt().equals( rhs.getUpdatedAt() ) ) {
				return( false );
			}
			if( getRequiredMimeTypeId() != rhs.getRequiredMimeTypeId() ) {
				return( false );
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
			if( getOptionalFileTypes() != null ) {
				if( rhs.getOptionalFileTypes() != null ) {
					if( ! getOptionalFileTypes().equals( rhs.getOptionalFileTypes() ) ) {
						return( false );
					}
				}
				else {
					return( false );
				}
			}
			else {
				if( rhs.getOptionalFileTypes() != null ) {
					return( false );
				}
			}
			return( true );
		}
		else if (obj instanceof ICFIntMimeTypeH) {
			ICFIntMimeTypeH rhs = (ICFIntMimeTypeH)obj;
			if( ! getCreatedByUserId().equals( rhs.getCreatedByUserId() ) ) {
				return( false );
			}
			if( ! getCreatedAt().equals( rhs.getCreatedAt() ) ) {
				return( false );
			}
			if( ! getUpdatedByUserId().equals( rhs.getUpdatedByUserId() ) ) {
				return( false );
			}
			if( ! getUpdatedAt().equals( rhs.getUpdatedAt() ) ) {
				return( false );
			}
			if( getRequiredMimeTypeId() != rhs.getRequiredMimeTypeId() ) {
				return( false );
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
			if( getOptionalFileTypes() != null ) {
				if( rhs.getOptionalFileTypes() != null ) {
					if( ! getOptionalFileTypes().equals( rhs.getOptionalFileTypes() ) ) {
						return( false );
					}
				}
				else {
					return( false );
				}
			}
			else {
				if( rhs.getOptionalFileTypes() != null ) {
					return( false );
				}
			}
			return( true );
		}
		else if (obj instanceof ICFIntMimeTypeHPKey) {
			ICFIntMimeTypeHPKey rhs = (ICFIntMimeTypeHPKey)obj;
			if( getRequiredMimeTypeId() != rhs.getRequiredMimeTypeId() ) {
				return( false );
			}
			return( true );
		}
		else if (obj instanceof ICFIntMimeTypeByUNameIdxKey) {
			ICFIntMimeTypeByUNameIdxKey rhs = (ICFIntMimeTypeByUNameIdxKey)obj;
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
		int hashCode = getPKey().hashCode();
		hashCode = hashCode + getCreatedByUserId().hashCode();
		hashCode = hashCode + getCreatedAt().hashCode();
		hashCode = hashCode + getUpdatedByUserId().hashCode();
		hashCode = hashCode + getUpdatedAt().hashCode();
		hashCode = hashCode + getRequiredMimeTypeId();
		if( getRequiredName() != null ) {
			hashCode = hashCode + getRequiredName().hashCode();
		}
		if( getOptionalFileTypes() != null ) {
			hashCode = hashCode + getOptionalFileTypes().hashCode();
		}
		return( hashCode & 0x7fffffff );
	}

	@Override
	public int compareTo( Object obj ) {
		int cmp;
		if (obj == null) {
			return( 1 );
		}
		else if (obj instanceof ICFIntMimeType) {
			ICFIntMimeType rhs = (ICFIntMimeType)obj;
			if (getPKey() == null) {
				if (rhs.getPKey() != null) {
					return( -1 );
				}
			}
			else {
				if (rhs.getPKey() == null) {
					return( 1 );
				}
				else {
					cmp = getPKey().compareTo(rhs.getPKey());
					if (cmp != 0) {
						return( cmp );
					}
				}
			}
			cmp = getCreatedByUserId().compareTo( rhs.getCreatedByUserId() );
			if( cmp != 0 ) {
				return( cmp );
			}
			cmp = getCreatedAt().compareTo( rhs.getCreatedAt() );
			if( cmp != 0 ) {
				return( cmp );
			}
			cmp = getUpdatedByUserId().compareTo( rhs.getUpdatedByUserId() );
			if( cmp != 0 ) {
				return( cmp );
			}
			cmp = getUpdatedAt().compareTo( rhs.getUpdatedAt() );
			if( cmp != 0 ) {
				return( cmp );
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
			if( getOptionalFileTypes() != null ) {
				if( rhs.getOptionalFileTypes() != null ) {
					cmp = getOptionalFileTypes().compareTo( rhs.getOptionalFileTypes() );
					if( cmp != 0 ) {
						return( cmp );
					}
				}
				else {
					return( 1 );
				}
			}
			else {
				if( rhs.getOptionalFileTypes() != null ) {
					return( -1 );
				}
			}
			return( 0 );
		}
		else if (obj instanceof ICFIntMimeTypeHPKey) {
			ICFIntMimeTypeHPKey rhs = (ICFIntMimeTypeHPKey)obj;
			if( getRequiredMimeTypeId() < rhs.getRequiredMimeTypeId() ) {
				return( -1 );
			}
			else if( getRequiredMimeTypeId() > rhs.getRequiredMimeTypeId() ) {
				return( 1 );
			}
			return( 0 );
		}
		else if( obj instanceof ICFIntMimeTypeH ) {
			ICFIntMimeTypeH rhs = (ICFIntMimeTypeH)obj;
			if( getRequiredMimeTypeId() < rhs.getRequiredMimeTypeId() ) {
				return( -1 );
			}
			else if( getRequiredMimeTypeId() > rhs.getRequiredMimeTypeId() ) {
				return( 1 );
			}
			cmp = getCreatedByUserId().compareTo( rhs.getCreatedByUserId() );
			if( cmp != 0 ) {
				return( cmp );
			}
			cmp = getCreatedAt().compareTo( rhs.getCreatedAt() );
			if( cmp != 0 ) {
				return( cmp );
			}
			cmp = getUpdatedByUserId().compareTo( rhs.getUpdatedByUserId() );
			if( cmp != 0 ) {
				return( cmp );
			}
			cmp = getUpdatedAt().compareTo( rhs.getUpdatedAt() );
			if( cmp != 0 ) {
				return( cmp );
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
			if( getOptionalFileTypes() != null ) {
				if( rhs.getOptionalFileTypes() != null ) {
					cmp = getOptionalFileTypes().compareTo( rhs.getOptionalFileTypes() );
					if( cmp != 0 ) {
						return( cmp );
					}
				}
				else {
					return( 1 );
				}
			}
			else {
				if( rhs.getOptionalFileTypes() != null ) {
					return( -1 );
				}
			}
			return( 0 );
		}
		else if (obj instanceof ICFIntMimeTypeByUNameIdxKey) {
			ICFIntMimeTypeByUNameIdxKey rhs = (ICFIntMimeTypeByUNameIdxKey)obj;
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
			throw new CFLibUnsupportedClassException( getClass(),
				"compareTo",
				"obj",
				obj,
				null );
		}
	}

	@Override
	public void set( ICFIntMimeType src ) {
		setMimeType( src );
	}

	@Override
	public void setMimeType( ICFIntMimeType src ) {
		setRequiredMimeTypeId(src.getRequiredMimeTypeId());
		setRequiredRevision( src.getRequiredRevision() );
		setCreatedByUserId( src.getCreatedByUserId() );
		setCreatedAt( src.getCreatedAt() );
		setUpdatedByUserId( src.getUpdatedByUserId() );
		setUpdatedAt( src.getUpdatedAt() );
		setRequiredName(src.getRequiredName());
		setOptionalFileTypes(src.getOptionalFileTypes());
	}

	@Override
	public void set( ICFIntMimeTypeH src ) {
		setMimeType( src );
	}

	@Override
	public void setMimeType( ICFIntMimeTypeH src ) {
		setRequiredMimeTypeId(src.getRequiredMimeTypeId());
		setRequiredName(src.getRequiredName());
		setOptionalFileTypes(src.getOptionalFileTypes());
	}

	@Override
	public String getXmlAttrFragment() {
		String ret = ""
			+ " RequiredMimeTypeId=" + "\"" + Integer.toString( getRequiredMimeTypeId() ) + "\""
			+ " RequiredRevision=\"" + Integer.toString( getRequiredRevision() ) + "\""
			+ " RequiredMimeTypeId=" + "\"" + Integer.toString( getRequiredMimeTypeId() ) + "\""
			+ " RequiredName=" + "\"" + StringEscapeUtils.escapeXml11( getRequiredName() ) + "\""
			+ " OptionalFileTypes=" + ( ( getOptionalFileTypes() == null ) ? "null" : "\"" + StringEscapeUtils.escapeXml11( getOptionalFileTypes() ) + "\"" );
		return( ret );
	}

	@Override
	public String toString() {
		String ret = "<CFIntJpaMimeType" + getXmlAttrFragment() + "/>";
		return( ret );
	}
}
