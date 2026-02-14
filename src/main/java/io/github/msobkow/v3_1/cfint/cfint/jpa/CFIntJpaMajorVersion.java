// Description: Java 25 JPA implementation of a MajorVersion entity definition object.

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

@Entity
@Table(
	name = "mjvrdef", schema = "CFInt31",
	indexes = {
		@Index(name = "MajorVersionIdIdx", columnList = "Id", unique = true),
		@Index(name = "MajorVersionTenantIdx", columnList = "TenantId", unique = false),
		@Index(name = "MajorVersionSubProjectIdx", columnList = "SubProjectId", unique = false),
		@Index(name = "MajorVersionNameIdx", columnList = "SubProjectId, safe_name", unique = true)
	}
)
@Transactional(Transactional.TxType.SUPPORTS)
@PersistenceContext(unitName = "CFIntPU")
public class CFIntJpaMajorVersion
	implements Comparable<Object>,
		ICFIntMajorVersion,
		Serializable
{
	@Id
	@AttributeOverrides({
		@AttributeOverride(name="bytes", column = @Column( name="Id", nullable=false, length=CFLibDbKeyHash256.HASH_LENGTH ) )
	})
	protected CFLibDbKeyHash256 requiredId;
	@OneToMany(fetch=FetchType.LAZY, mappedBy="requiredContainerParentMajVer")
	protected Set<CFIntJpaMinorVersion> optionalComponentsMinorVer;
	protected int requiredRevision;

	@ManyToOne(fetch=FetchType.LAZY, optional=false)
	@JoinColumn( name="SubProjectId" )
	protected CFIntJpaSubProject requiredContainerParentSPrj;

	@AttributeOverrides({
		@AttributeOverride( name="bytes", column = @Column( name="CreatedByUserId", nullable=false, length=CFLibDbKeyHash256.HASH_LENGTH ) )
	})
	protected CFLibDbKeyHash256 createdByUserId = CFLibDbKeyHash256.fromHex(ICFIntMajorVersion.S_INIT_CREATED_BY);

	@Column(name="CreatedAt", nullable=false)
	protected LocalDateTime createdAt = LocalDateTime.now();

	@AttributeOverrides({
		@AttributeOverride( name="bytes", column= @Column( name="UpdatedByUserId", nullable=false, length=CFLibDbKeyHash256.HASH_LENGTH ) )
	})
	protected CFLibDbKeyHash256 updatedByUserId = CFLibDbKeyHash256.fromHex(ICFIntMajorVersion.S_INIT_UPDATED_BY);

	@Column(name="UpdatedAt", nullable=false)
	protected LocalDateTime updatedAt = LocalDateTime.now();
	@AttributeOverrides({
		@AttributeOverride(name="bytes", column = @Column( name="TenantId", nullable=false, length=CFLibDbKeyHash256.HASH_LENGTH ) )
	})
	protected CFLibDbKeyHash256 requiredTenantId;
	@Column( name="safe_name", nullable=false, length=64 )
	protected String requiredName;
	@Column( name="descr", nullable=true, length=1024 )
	protected String optionalDescription;

	public CFIntJpaMajorVersion() {
		requiredId = CFLibDbKeyHash256.fromHex( ICFIntMajorVersion.ID_INIT_VALUE.toString() );
		requiredTenantId = CFLibDbKeyHash256.fromHex( ICFIntMajorVersion.TENANTID_INIT_VALUE.toString() );
		requiredName = ICFIntMajorVersion.NAME_INIT_VALUE;
		optionalDescription = null;
	}

	@Override
	public int getClassCode() {
		return( ICFIntMajorVersion.CLASS_CODE );
	}

	@Override
	public List<ICFIntMinorVersion> getOptionalComponentsMinorVer() {
		List<ICFIntMinorVersion> retlist = new ArrayList<>(optionalComponentsMinorVer.size());
		for (CFIntJpaMinorVersion cur: optionalComponentsMinorVer) {
			retlist.add(cur);
		}
		return( retlist );
	}
	@Override
	public ICFSecTenant getRequiredOwnerTenant() {
		ICFSecSchema targetBackingSchema = ICFSecSchema.getBackingCFSec();
		if (targetBackingSchema == null) {
			throw new CFLibNullArgumentException(getClass(), "setRequiredOwnerTenant", 0, "ICFSecSchema.getBackingCFSec()");
		}
		ICFSecTenantTable targetTable = targetBackingSchema.getTableTenant();
		if (targetTable == null) {
			throw new CFLibNullArgumentException(getClass(), "setRequiredOwnerTenant", 0, "ICFSecSchema.getBackingCFSec().getTableTenant()");
		}
		ICFSecTenant targetRec = targetTable.readDerivedByIdIdx(null, getRequiredTenantId());
		return(targetRec);
	}
	@Override
	public void setRequiredOwnerTenant(ICFSecTenant argObj) {
		if(argObj == null) {
			throw new CFLibNullArgumentException(getClass(), "setOwnerTenant", 1, "argObj");
		}
		else {
			requiredTenantId = argObj.getRequiredId();
		}
	}

	@Override
	public void setRequiredOwnerTenant(CFLibDbKeyHash256 argTenantId) {
		requiredTenantId = argTenantId;
	}

	@Override
	public ICFIntSubProject getRequiredContainerParentSPrj() {
		return( requiredContainerParentSPrj );
	}
	@Override
	public void setRequiredContainerParentSPrj(ICFIntSubProject argObj) {
		if(argObj == null) {
			throw new CFLibNullArgumentException(getClass(), "setContainerParentSPrj", 1, "argObj");
		}
		else if (argObj instanceof CFIntJpaSubProject) {
			requiredContainerParentSPrj = (CFIntJpaSubProject)argObj;
		}
		else {
			throw new CFLibUnsupportedClassException(getClass(), "setContainerParentSPrj", "argObj", argObj, "CFIntJpaSubProject");
		}
	}

	@Override
	public void setRequiredContainerParentSPrj(CFLibDbKeyHash256 argSubProjectId) {
		ICFIntSchema targetBackingSchema = ICFIntSchema.getBackingCFInt();
		if (targetBackingSchema == null) {
			throw new CFLibNullArgumentException(getClass(), "setRequiredContainerParentSPrj", 0, "ICFIntSchema.getBackingCFInt()");
		}
		ICFIntSubProjectTable targetTable = targetBackingSchema.getTableSubProject();
		if (targetTable == null) {
			throw new CFLibNullArgumentException(getClass(), "setRequiredContainerParentSPrj", 0, "ICFIntSchema.getBackingCFInt().getTableSubProject()");
		}
		ICFIntSubProject targetRec = targetTable.readDerived(null, argSubProjectId);
		setRequiredContainerParentSPrj(targetRec);
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
	public CFLibDbKeyHash256 getPKey() {
		return getRequiredId();
	}

	@Override
	public void setPKey(CFLibDbKeyHash256 requiredId) {
		if (requiredId != null) {
			setRequiredId(requiredId);
		}
	}
	
	@Override
	public CFLibDbKeyHash256 getRequiredId() {
		return( requiredId );
	}

	@Override
	public void setRequiredId( CFLibDbKeyHash256 value ) {
		if( value == null || value.isNull() ) {
			throw new CFLibNullArgumentException( getClass(),
				"setRequiredId",
				1,
				"value" );
		}
		requiredId = value;
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
	public CFLibDbKeyHash256 getRequiredTenantId() {
		return( requiredTenantId );
	}

	@Override
	public CFLibDbKeyHash256 getRequiredSubProjectId() {
		ICFIntSubProject result = getRequiredContainerParentSPrj();
		if (result != null) {
			return result.getRequiredId();
		}
		else {
			return( ICFIntSubProject.ID_INIT_VALUE );
		}
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
	public String getOptionalDescription() {
		return( optionalDescription );
	}

	@Override
	public void setOptionalDescription( String value ) {
		if( value != null && value.length() > 1024 ) {
			throw new CFLibArgumentOverflowException( getClass(),
				"setOptionalDescription",
				1,
				"value.length()",
				value.length(),
				1024 );
		}
		optionalDescription = value;
	}

	@Override
	public boolean equals( Object obj ) {
		if (obj == null) {
			return( false );
		}
		else if (obj instanceof ICFIntMajorVersion) {
			ICFIntMajorVersion rhs = (ICFIntMajorVersion)obj;
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
			if( getRequiredId() != null && !getRequiredId().isNull() ) {
				if( rhs.getRequiredId() != null && !rhs.getRequiredId().isNull() ) {
					if( ! getRequiredId().equals( rhs.getRequiredId() ) ) {
						return( false );
					}
				}
				else {
					return( false );
				}
			}
			else {
				if( rhs.getRequiredId() != null && !getRequiredId().isNull()) {
					return( false );
				}
			}
			if( getRequiredTenantId() != null && !getRequiredTenantId().isNull() ) {
				if( rhs.getRequiredTenantId() != null && !rhs.getRequiredTenantId().isNull() ) {
					if( ! getRequiredTenantId().equals( rhs.getRequiredTenantId() ) ) {
						return( false );
					}
				}
				else {
					return( false );
				}
			}
			else {
				if( rhs.getRequiredTenantId() != null && !getRequiredTenantId().isNull()) {
					return( false );
				}
			}
			if( getRequiredSubProjectId() != null && !getRequiredSubProjectId().isNull() ) {
				if( rhs.getRequiredSubProjectId() != null && !rhs.getRequiredSubProjectId().isNull() ) {
					if( ! getRequiredSubProjectId().equals( rhs.getRequiredSubProjectId() ) ) {
						return( false );
					}
				}
				else {
					return( false );
				}
			}
			else {
				if( rhs.getRequiredSubProjectId() != null && !getRequiredSubProjectId().isNull()) {
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
			if( getOptionalDescription() != null ) {
				if( rhs.getOptionalDescription() != null ) {
					if( ! getOptionalDescription().equals( rhs.getOptionalDescription() ) ) {
						return( false );
					}
				}
				else {
					return( false );
				}
			}
			else {
				if( rhs.getOptionalDescription() != null ) {
					return( false );
				}
			}
			return( true );
		}
		else if (obj instanceof ICFIntMajorVersionH) {
			ICFIntMajorVersionH rhs = (ICFIntMajorVersionH)obj;
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
			if( getRequiredId() != null && !getRequiredId().isNull() ) {
				if( rhs.getRequiredId() != null && !rhs.getRequiredId().isNull() ) {
					if( ! getRequiredId().equals( rhs.getRequiredId() ) ) {
						return( false );
					}
				}
				else {
					return( false );
				}
			}
			else {
				if( rhs.getRequiredId() != null && !getRequiredId().isNull()) {
					return( false );
				}
			}
			if( getRequiredTenantId() != null && !getRequiredTenantId().isNull() ) {
				if( rhs.getRequiredTenantId() != null && !rhs.getRequiredTenantId().isNull() ) {
					if( ! getRequiredTenantId().equals( rhs.getRequiredTenantId() ) ) {
						return( false );
					}
				}
				else {
					return( false );
				}
			}
			else {
				if( rhs.getRequiredTenantId() != null && !getRequiredTenantId().isNull()) {
					return( false );
				}
			}
			if( getRequiredSubProjectId() != null && !getRequiredSubProjectId().isNull() ) {
				if( rhs.getRequiredSubProjectId() != null && !rhs.getRequiredSubProjectId().isNull() ) {
					if( ! getRequiredSubProjectId().equals( rhs.getRequiredSubProjectId() ) ) {
						return( false );
					}
				}
				else {
					return( false );
				}
			}
			else {
				if( rhs.getRequiredSubProjectId() != null && !getRequiredSubProjectId().isNull()) {
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
			if( getOptionalDescription() != null ) {
				if( rhs.getOptionalDescription() != null ) {
					if( ! getOptionalDescription().equals( rhs.getOptionalDescription() ) ) {
						return( false );
					}
				}
				else {
					return( false );
				}
			}
			else {
				if( rhs.getOptionalDescription() != null ) {
					return( false );
				}
			}
			return( true );
		}
		else if (obj instanceof ICFIntMajorVersionHPKey) {
			ICFIntMajorVersionHPKey rhs = (ICFIntMajorVersionHPKey)obj;
			if( getRequiredId() != null && !getRequiredId().isNull() ) {
				if( rhs.getRequiredId() != null && !rhs.getRequiredId().isNull() ) {
					if( ! getRequiredId().equals( rhs.getRequiredId() ) ) {
						return( false );
					}
				}
				else {
					return( false );
				}
			}
			else {
				if( rhs.getRequiredId() != null && !getRequiredId().isNull()) {
					return( false );
				}
			}
			return( true );
		}
		else if (obj instanceof ICFIntMajorVersionByTenantIdxKey) {
			ICFIntMajorVersionByTenantIdxKey rhs = (ICFIntMajorVersionByTenantIdxKey)obj;
			if( getRequiredTenantId() != null && !getRequiredTenantId().isNull() ) {
				if( rhs.getRequiredTenantId() != null && !rhs.getRequiredTenantId().isNull() ) {
					if( ! getRequiredTenantId().equals( rhs.getRequiredTenantId() ) ) {
						return( false );
					}
				}
				else {
					return( false );
				}
			}
			else {
				if( rhs.getRequiredTenantId() != null && !getRequiredTenantId().isNull()) {
					return( false );
				}
			}
			return( true );
		}
		else if (obj instanceof ICFIntMajorVersionBySubProjectIdxKey) {
			ICFIntMajorVersionBySubProjectIdxKey rhs = (ICFIntMajorVersionBySubProjectIdxKey)obj;
			if( getRequiredSubProjectId() != null && !getRequiredSubProjectId().isNull() ) {
				if( rhs.getRequiredSubProjectId() != null && !rhs.getRequiredSubProjectId().isNull() ) {
					if( ! getRequiredSubProjectId().equals( rhs.getRequiredSubProjectId() ) ) {
						return( false );
					}
				}
				else {
					return( false );
				}
			}
			else {
				if( rhs.getRequiredSubProjectId() != null && !getRequiredSubProjectId().isNull()) {
					return( false );
				}
			}
			return( true );
		}
		else if (obj instanceof ICFIntMajorVersionByNameIdxKey) {
			ICFIntMajorVersionByNameIdxKey rhs = (ICFIntMajorVersionByNameIdxKey)obj;
			if( getRequiredSubProjectId() != null && !getRequiredSubProjectId().isNull() ) {
				if( rhs.getRequiredSubProjectId() != null && !rhs.getRequiredSubProjectId().isNull() ) {
					if( ! getRequiredSubProjectId().equals( rhs.getRequiredSubProjectId() ) ) {
						return( false );
					}
				}
				else {
					return( false );
				}
			}
			else {
				if( rhs.getRequiredSubProjectId() != null && !getRequiredSubProjectId().isNull()) {
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
		int hashCode = getPKey().hashCode();
		hashCode = hashCode + getCreatedByUserId().hashCode();
		hashCode = hashCode + getCreatedAt().hashCode();
		hashCode = hashCode + getUpdatedByUserId().hashCode();
		hashCode = hashCode + getUpdatedAt().hashCode();
		hashCode = hashCode + getRequiredId().hashCode();
		hashCode = hashCode + getRequiredTenantId().hashCode();
		hashCode = hashCode + getRequiredSubProjectId().hashCode();
		if( getRequiredName() != null ) {
			hashCode = hashCode + getRequiredName().hashCode();
		}
		if( getOptionalDescription() != null ) {
			hashCode = hashCode + getOptionalDescription().hashCode();
		}
		return( hashCode & 0x7fffffff );
	}

	@Override
	public int compareTo( Object obj ) {
		int cmp;
		if (obj == null) {
			return( 1 );
		}
		else if (obj instanceof ICFIntMajorVersion) {
			ICFIntMajorVersion rhs = (ICFIntMajorVersion)obj;
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
			if (getRequiredTenantId() != null) {
				if (rhs.getRequiredTenantId() != null) {
					cmp = getRequiredTenantId().compareTo( rhs.getRequiredTenantId() );
					if( cmp != 0 ) {
						return( cmp );
					}
				}
				else {
					return( 1 );
				}
			}
			else if (rhs.getRequiredTenantId() != null) {
				return( -1 );
			}
			if (getRequiredSubProjectId() != null) {
				if (rhs.getRequiredSubProjectId() != null) {
					cmp = getRequiredSubProjectId().compareTo( rhs.getRequiredSubProjectId() );
					if( cmp != 0 ) {
						return( cmp );
					}
				}
				else {
					return( 1 );
				}
			}
			else if (rhs.getRequiredSubProjectId() != null) {
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
			if( getOptionalDescription() != null ) {
				if( rhs.getOptionalDescription() != null ) {
					cmp = getOptionalDescription().compareTo( rhs.getOptionalDescription() );
					if( cmp != 0 ) {
						return( cmp );
					}
				}
				else {
					return( 1 );
				}
			}
			else {
				if( rhs.getOptionalDescription() != null ) {
					return( -1 );
				}
			}
			return( 0 );
		}
		else if (obj instanceof ICFIntMajorVersionHPKey) {
			ICFIntMajorVersionHPKey rhs = (ICFIntMajorVersionHPKey)obj;
			if (getRequiredId() != null) {
				if (rhs.getRequiredId() != null) {
					cmp = getRequiredId().compareTo( rhs.getRequiredId() );
					if( cmp != 0 ) {
						return( cmp );
					}
				}
				else {
					return( 1 );
				}
			}
			else if (rhs.getRequiredId() != null) {
				return( -1 );
			}
			return( 0 );
		}
		else if( obj instanceof ICFIntMajorVersionH ) {
			ICFIntMajorVersionH rhs = (ICFIntMajorVersionH)obj;
			if (getRequiredId() != null) {
				if (rhs.getRequiredId() != null) {
					cmp = getRequiredId().compareTo( rhs.getRequiredId() );
					if( cmp != 0 ) {
						return( cmp );
					}
				}
				else {
					return( 1 );
				}
			}
			else if (rhs.getRequiredId() != null) {
				return( -1 );
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
			if (getRequiredTenantId() != null) {
				if (rhs.getRequiredTenantId() != null) {
					cmp = getRequiredTenantId().compareTo( rhs.getRequiredTenantId() );
					if( cmp != 0 ) {
						return( cmp );
					}
				}
				else {
					return( 1 );
				}
			}
			else if (rhs.getRequiredTenantId() != null) {
				return( -1 );
			}
			if (getRequiredSubProjectId() != null) {
				if (rhs.getRequiredSubProjectId() != null) {
					cmp = getRequiredSubProjectId().compareTo( rhs.getRequiredSubProjectId() );
					if( cmp != 0 ) {
						return( cmp );
					}
				}
				else {
					return( 1 );
				}
			}
			else if (rhs.getRequiredSubProjectId() != null) {
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
			if( getOptionalDescription() != null ) {
				if( rhs.getOptionalDescription() != null ) {
					cmp = getOptionalDescription().compareTo( rhs.getOptionalDescription() );
					if( cmp != 0 ) {
						return( cmp );
					}
				}
				else {
					return( 1 );
				}
			}
			else {
				if( rhs.getOptionalDescription() != null ) {
					return( -1 );
				}
			}
			return( 0 );
		}
		else if (obj instanceof ICFIntMajorVersionByTenantIdxKey) {
			ICFIntMajorVersionByTenantIdxKey rhs = (ICFIntMajorVersionByTenantIdxKey)obj;
			if (getRequiredTenantId() != null) {
				if (rhs.getRequiredTenantId() != null) {
					cmp = getRequiredTenantId().compareTo( rhs.getRequiredTenantId() );
					if( cmp != 0 ) {
						return( cmp );
					}
				}
				else {
					return( 1 );
				}
			}
			else if (rhs.getRequiredTenantId() != null) {
				return( -1 );
			}
			return( 0 );
		}
		else if (obj instanceof ICFIntMajorVersionBySubProjectIdxKey) {
			ICFIntMajorVersionBySubProjectIdxKey rhs = (ICFIntMajorVersionBySubProjectIdxKey)obj;
			if (getRequiredSubProjectId() != null) {
				if (rhs.getRequiredSubProjectId() != null) {
					cmp = getRequiredSubProjectId().compareTo( rhs.getRequiredSubProjectId() );
					if( cmp != 0 ) {
						return( cmp );
					}
				}
				else {
					return( 1 );
				}
			}
			else if (rhs.getRequiredSubProjectId() != null) {
				return( -1 );
			}
			return( 0 );
		}
		else if (obj instanceof ICFIntMajorVersionByNameIdxKey) {
			ICFIntMajorVersionByNameIdxKey rhs = (ICFIntMajorVersionByNameIdxKey)obj;
			if (getRequiredSubProjectId() != null) {
				if (rhs.getRequiredSubProjectId() != null) {
					cmp = getRequiredSubProjectId().compareTo( rhs.getRequiredSubProjectId() );
					if( cmp != 0 ) {
						return( cmp );
					}
				}
				else {
					return( 1 );
				}
			}
			else if (rhs.getRequiredSubProjectId() != null) {
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
			throw new CFLibUnsupportedClassException( getClass(),
				"compareTo",
				"obj",
				obj,
				null );
		}
	}

	@Override
	public void set( ICFIntMajorVersion src ) {
		setMajorVersion( src );
	}

	@Override
	public void setMajorVersion( ICFIntMajorVersion src ) {
		setRequiredId(src.getRequiredId());
		setRequiredRevision( src.getRequiredRevision() );
		setCreatedByUserId( src.getCreatedByUserId() );
		setCreatedAt( src.getCreatedAt() );
		setUpdatedByUserId( src.getUpdatedByUserId() );
		setUpdatedAt( src.getUpdatedAt() );
		setRequiredOwnerTenant(src.getRequiredTenantId());
		setRequiredContainerParentSPrj(src.getRequiredContainerParentSPrj());
		setRequiredName(src.getRequiredName());
		setOptionalDescription(src.getOptionalDescription());
	}

	@Override
	public void set( ICFIntMajorVersionH src ) {
		setMajorVersion( src );
	}

	@Override
	public void setMajorVersion( ICFIntMajorVersionH src ) {
		setRequiredId(src.getRequiredId());
		setRequiredOwnerTenant(src.getRequiredTenantId());
		setRequiredContainerParentSPrj(src.getRequiredSubProjectId());
		setRequiredName(src.getRequiredName());
		setOptionalDescription(src.getOptionalDescription());
	}

	@Override
	public String getXmlAttrFragment() {
		String ret = ""
			+ " RequiredId=" + "\"" + getRequiredId().toString() + "\""
			+ " RequiredRevision=\"" + Integer.toString( getRequiredRevision() ) + "\""
			+ " RequiredId=" + "\"" + getRequiredId().toString() + "\""
			+ " RequiredTenantId=" + "\"" + getRequiredTenantId().toString() + "\""
			+ " RequiredSubProjectId=" + "\"" + getRequiredSubProjectId().toString() + "\""
			+ " RequiredName=" + "\"" + StringEscapeUtils.escapeXml11( getRequiredName() ) + "\""
			+ " OptionalDescription=" + ( ( getOptionalDescription() == null ) ? "null" : "\"" + StringEscapeUtils.escapeXml11( getOptionalDescription() ) + "\"" );
		return( ret );
	}

	@Override
	public String toString() {
		String ret = "<CFIntJpaMajorVersion" + getXmlAttrFragment() + "/>";
		return( ret );
	}
}
