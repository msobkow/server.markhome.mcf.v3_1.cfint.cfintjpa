// Description: Java 25 JPA implementation of a CFInt schema.

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
//package io.github.msobkow.v3_1.cfint.cfint.jpa;

import java.io.Serializable;
import java.math.*;
import java.time.*;
import java.net.InetAddress;
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

public class CFIntJpaSchema
	implements ICFIntSchema,
		ICFSecSchema
{
	protected ICFSecClusterTable tableCluster;
	protected ICFSecHostNodeTable tableHostNode;
	protected ICFSecISOCcyTable tableISOCcy;
	protected ICFSecISOCtryTable tableISOCtry;
	protected ICFSecISOCtryCcyTable tableISOCtryCcy;
	protected ICFSecISOCtryLangTable tableISOCtryLang;
	protected ICFSecISOLangTable tableISOLang;
	protected ICFSecISOTZoneTable tableISOTZone;
	protected ICFIntLicenseTable tableLicense;
	protected ICFIntMajorVersionTable tableMajorVersion;
	protected ICFIntMimeTypeTable tableMimeType;
	protected ICFIntMinorVersionTable tableMinorVersion;
	protected ICFSecSecDeviceTable tableSecDevice;
	protected ICFSecSecGroupTable tableSecGroup;
	protected ICFSecSecGrpIncTable tableSecGrpInc;
	protected ICFSecSecGrpMembTable tableSecGrpMemb;
	protected ICFSecSecSessionTable tableSecSession;
	protected ICFSecSecUserTable tableSecUser;
	protected ICFSecServiceTable tableService;
	protected ICFSecServiceTypeTable tableServiceType;
	protected ICFIntSubProjectTable tableSubProject;
	protected ICFSecSysClusterTable tableSysCluster;
	protected ICFSecTSecGroupTable tableTSecGroup;
	protected ICFSecTSecGrpIncTable tableTSecGrpInc;
	protected ICFSecTSecGrpMembTable tableTSecGrpMemb;
	protected ICFSecTenantTable tableTenant;
	protected ICFIntTldTable tableTld;
	protected ICFIntTopDomainTable tableTopDomain;
	protected ICFIntTopProjectTable tableTopProject;
	protected ICFIntURLProtocolTable tableURLProtocol;

	protected ICFSecClusterFactory factoryCluster;
	protected ICFSecHostNodeFactory factoryHostNode;
	protected ICFSecISOCcyFactory factoryISOCcy;
	protected ICFSecISOCtryFactory factoryISOCtry;
	protected ICFSecISOCtryCcyFactory factoryISOCtryCcy;
	protected ICFSecISOCtryLangFactory factoryISOCtryLang;
	protected ICFSecISOLangFactory factoryISOLang;
	protected ICFSecISOTZoneFactory factoryISOTZone;
	protected ICFIntLicenseFactory factoryLicense;
	protected ICFIntMajorVersionFactory factoryMajorVersion;
	protected ICFIntMimeTypeFactory factoryMimeType;
	protected ICFIntMinorVersionFactory factoryMinorVersion;
	protected ICFSecSecDeviceFactory factorySecDevice;
	protected ICFSecSecGroupFactory factorySecGroup;
	protected ICFSecSecGrpIncFactory factorySecGrpInc;
	protected ICFSecSecGrpMembFactory factorySecGrpMemb;
	protected ICFSecSecSessionFactory factorySecSession;
	protected ICFSecSecUserFactory factorySecUser;
	protected ICFSecServiceFactory factoryService;
	protected ICFSecServiceTypeFactory factoryServiceType;
	protected ICFIntSubProjectFactory factorySubProject;
	protected ICFSecSysClusterFactory factorySysCluster;
	protected ICFSecTSecGroupFactory factoryTSecGroup;
	protected ICFSecTSecGrpIncFactory factoryTSecGrpInc;
	protected ICFSecTSecGrpMembFactory factoryTSecGrpMemb;
	protected ICFSecTenantFactory factoryTenant;
	protected ICFIntTldFactory factoryTld;
	protected ICFIntTopDomainFactory factoryTopDomain;
	protected ICFIntTopProjectFactory factoryTopProject;
	protected ICFIntURLProtocolFactory factoryURLProtocol;


	@Override
	public int initClassMapEntries(int value) {
		return( ICFIntSchema.doInitClassMapEntries(value) );
	}

	@Override
	public void wireRecConstructors() {
		ICFSecSchema.ClassMapEntry entry;
		entry = ICFIntSchema.getClassMapByBackingClassCode(ICFIntLicense.CLASS_CODE);
		if (entry != null) {
			entry.setBackingRecConstructor( new BackingRecConstructor() {
				@Override
				public Object instantiate() {
					ICFIntLicense ret = new CFIntJpaLicense();
					return(ret);
				}
			});
		}
		else {
			throw new CFLibNullArgumentException(CFIntJpaSchema.class, "wireRecConstructors", 0, "ICFIntSchema.getClassMapByBackingClassCode(ICFIntLicense.CLASS_CODE)[" + ICFIntLicense.CLASS_CODE + "]");
		}
	
		entry = ICFIntSchema.getClassMapByBackingClassCode(ICFIntMajorVersion.CLASS_CODE);
		if (entry != null) {
			entry.setBackingRecConstructor( new BackingRecConstructor() {
				@Override
				public Object instantiate() {
					ICFIntMajorVersion ret = new CFIntJpaMajorVersion();
					return(ret);
				}
			});
		}
		else {
			throw new CFLibNullArgumentException(CFIntJpaSchema.class, "wireRecConstructors", 0, "ICFIntSchema.getClassMapByBackingClassCode(ICFIntMajorVersion.CLASS_CODE)[" + ICFIntMajorVersion.CLASS_CODE + "]");
		}
	
		entry = ICFIntSchema.getClassMapByBackingClassCode(ICFIntMimeType.CLASS_CODE);
		if (entry != null) {
			entry.setBackingRecConstructor( new BackingRecConstructor() {
				@Override
				public Object instantiate() {
					ICFIntMimeType ret = new CFIntJpaMimeType();
					return(ret);
				}
			});
		}
		else {
			throw new CFLibNullArgumentException(CFIntJpaSchema.class, "wireRecConstructors", 0, "ICFIntSchema.getClassMapByBackingClassCode(ICFIntMimeType.CLASS_CODE)[" + ICFIntMimeType.CLASS_CODE + "]");
		}
	
		entry = ICFIntSchema.getClassMapByBackingClassCode(ICFIntMinorVersion.CLASS_CODE);
		if (entry != null) {
			entry.setBackingRecConstructor( new BackingRecConstructor() {
				@Override
				public Object instantiate() {
					ICFIntMinorVersion ret = new CFIntJpaMinorVersion();
					return(ret);
				}
			});
		}
		else {
			throw new CFLibNullArgumentException(CFIntJpaSchema.class, "wireRecConstructors", 0, "ICFIntSchema.getClassMapByBackingClassCode(ICFIntMinorVersion.CLASS_CODE)[" + ICFIntMinorVersion.CLASS_CODE + "]");
		}
	
		entry = ICFIntSchema.getClassMapByBackingClassCode(ICFIntSubProject.CLASS_CODE);
		if (entry != null) {
			entry.setBackingRecConstructor( new BackingRecConstructor() {
				@Override
				public Object instantiate() {
					ICFIntSubProject ret = new CFIntJpaSubProject();
					return(ret);
				}
			});
		}
		else {
			throw new CFLibNullArgumentException(CFIntJpaSchema.class, "wireRecConstructors", 0, "ICFIntSchema.getClassMapByBackingClassCode(ICFIntSubProject.CLASS_CODE)[" + ICFIntSubProject.CLASS_CODE + "]");
		}
	
		entry = ICFIntSchema.getClassMapByBackingClassCode(ICFIntTld.CLASS_CODE);
		if (entry != null) {
			entry.setBackingRecConstructor( new BackingRecConstructor() {
				@Override
				public Object instantiate() {
					ICFIntTld ret = new CFIntJpaTld();
					return(ret);
				}
			});
		}
		else {
			throw new CFLibNullArgumentException(CFIntJpaSchema.class, "wireRecConstructors", 0, "ICFIntSchema.getClassMapByBackingClassCode(ICFIntTld.CLASS_CODE)[" + ICFIntTld.CLASS_CODE + "]");
		}
	
		entry = ICFIntSchema.getClassMapByBackingClassCode(ICFIntTopDomain.CLASS_CODE);
		if (entry != null) {
			entry.setBackingRecConstructor( new BackingRecConstructor() {
				@Override
				public Object instantiate() {
					ICFIntTopDomain ret = new CFIntJpaTopDomain();
					return(ret);
				}
			});
		}
		else {
			throw new CFLibNullArgumentException(CFIntJpaSchema.class, "wireRecConstructors", 0, "ICFIntSchema.getClassMapByBackingClassCode(ICFIntTopDomain.CLASS_CODE)[" + ICFIntTopDomain.CLASS_CODE + "]");
		}
	
		entry = ICFIntSchema.getClassMapByBackingClassCode(ICFIntTopProject.CLASS_CODE);
		if (entry != null) {
			entry.setBackingRecConstructor( new BackingRecConstructor() {
				@Override
				public Object instantiate() {
					ICFIntTopProject ret = new CFIntJpaTopProject();
					return(ret);
				}
			});
		}
		else {
			throw new CFLibNullArgumentException(CFIntJpaSchema.class, "wireRecConstructors", 0, "ICFIntSchema.getClassMapByBackingClassCode(ICFIntTopProject.CLASS_CODE)[" + ICFIntTopProject.CLASS_CODE + "]");
		}
	
		entry = ICFIntSchema.getClassMapByBackingClassCode(ICFIntURLProtocol.CLASS_CODE);
		if (entry != null) {
			entry.setBackingRecConstructor( new BackingRecConstructor() {
				@Override
				public Object instantiate() {
					ICFIntURLProtocol ret = new CFIntJpaURLProtocol();
					return(ret);
				}
			});
		}
		else {
			throw new CFLibNullArgumentException(CFIntJpaSchema.class, "wireRecConstructors", 0, "ICFIntSchema.getClassMapByBackingClassCode(ICFIntURLProtocol.CLASS_CODE)[" + ICFIntURLProtocol.CLASS_CODE + "]");
		}
	
	}

	@Override
	public void wireTableTableInstances() {
		if (tableLicense == null || !(tableLicense instanceof CFIntJpaLicenseTable)) {
			tableLicense = new CFIntJpaLicenseTable(this);
		}
		if (tableMajorVersion == null || !(tableMajorVersion instanceof CFIntJpaMajorVersionTable)) {
			tableMajorVersion = new CFIntJpaMajorVersionTable(this);
		}
		if (tableMimeType == null || !(tableMimeType instanceof CFIntJpaMimeTypeTable)) {
			tableMimeType = new CFIntJpaMimeTypeTable(this);
		}
		if (tableMinorVersion == null || !(tableMinorVersion instanceof CFIntJpaMinorVersionTable)) {
			tableMinorVersion = new CFIntJpaMinorVersionTable(this);
		}
		if (tableSubProject == null || !(tableSubProject instanceof CFIntJpaSubProjectTable)) {
			tableSubProject = new CFIntJpaSubProjectTable(this);
		}
		if (tableTld == null || !(tableTld instanceof CFIntJpaTldTable)) {
			tableTld = new CFIntJpaTldTable(this);
		}
		if (tableTopDomain == null || !(tableTopDomain instanceof CFIntJpaTopDomainTable)) {
			tableTopDomain = new CFIntJpaTopDomainTable(this);
		}
		if (tableTopProject == null || !(tableTopProject instanceof CFIntJpaTopProjectTable)) {
			tableTopProject = new CFIntJpaTopProjectTable(this);
		}
		if (tableURLProtocol == null || !(tableURLProtocol instanceof CFIntJpaURLProtocolTable)) {
			tableURLProtocol = new CFIntJpaURLProtocolTable(this);
		}
	}

	@Override		
	public ICFSecSchema getCFSecSchema() {
		return( ICFSecSchema.getBackingCFSec() );
	}

	@Override
	public void setCFSecSchema(ICFSecSchema schema) {
		ICFSecSchema.setBackingCFSec(schema);
		schema.wireRecConstructors();
	}

	@Override		
	public ICFIntSchema getCFIntSchema() {
		return( ICFIntSchema.getBackingCFInt() );
	}

	@Override
	public void setCFIntSchema(ICFIntSchema schema) {
		ICFIntSchema.setBackingCFInt(schema);
		schema.wireRecConstructors();
	}

	public CFIntJpaSchema() {

		tableCluster = null;
		tableHostNode = null;
		tableISOCcy = null;
		tableISOCtry = null;
		tableISOCtryCcy = null;
		tableISOCtryLang = null;
		tableISOLang = null;
		tableISOTZone = null;
		tableLicense = null;
		tableMajorVersion = null;
		tableMimeType = null;
		tableMinorVersion = null;
		tableSecDevice = null;
		tableSecGroup = null;
		tableSecGrpInc = null;
		tableSecGrpMemb = null;
		tableSecSession = null;
		tableSecUser = null;
		tableService = null;
		tableServiceType = null;
		tableSubProject = null;
		tableSysCluster = null;
		tableTSecGroup = null;
		tableTSecGrpInc = null;
		tableTSecGrpMemb = null;
		tableTenant = null;
		tableTld = null;
		tableTopDomain = null;
		tableTopProject = null;
		tableURLProtocol = null;

		factoryCluster = new CFSecJpaClusterDefaultFactory();
		factoryHostNode = new CFSecJpaHostNodeDefaultFactory();
		factoryISOCcy = new CFSecJpaISOCcyDefaultFactory();
		factoryISOCtry = new CFSecJpaISOCtryDefaultFactory();
		factoryISOCtryCcy = new CFSecJpaISOCtryCcyDefaultFactory();
		factoryISOCtryLang = new CFSecJpaISOCtryLangDefaultFactory();
		factoryISOLang = new CFSecJpaISOLangDefaultFactory();
		factoryISOTZone = new CFSecJpaISOTZoneDefaultFactory();
		factoryLicense = new CFIntJpaLicenseDefaultFactory();
		factoryMajorVersion = new CFIntJpaMajorVersionDefaultFactory();
		factoryMimeType = new CFIntJpaMimeTypeDefaultFactory();
		factoryMinorVersion = new CFIntJpaMinorVersionDefaultFactory();
		factorySecDevice = new CFSecJpaSecDeviceDefaultFactory();
		factorySecGroup = new CFSecJpaSecGroupDefaultFactory();
		factorySecGrpInc = new CFSecJpaSecGrpIncDefaultFactory();
		factorySecGrpMemb = new CFSecJpaSecGrpMembDefaultFactory();
		factorySecSession = new CFSecJpaSecSessionDefaultFactory();
		factorySecUser = new CFSecJpaSecUserDefaultFactory();
		factoryService = new CFSecJpaServiceDefaultFactory();
		factoryServiceType = new CFSecJpaServiceTypeDefaultFactory();
		factorySubProject = new CFIntJpaSubProjectDefaultFactory();
		factorySysCluster = new CFSecJpaSysClusterDefaultFactory();
		factoryTSecGroup = new CFSecJpaTSecGroupDefaultFactory();
		factoryTSecGrpInc = new CFSecJpaTSecGrpIncDefaultFactory();
		factoryTSecGrpMemb = new CFSecJpaTSecGrpMembDefaultFactory();
		factoryTenant = new CFSecJpaTenantDefaultFactory();
		factoryTld = new CFIntJpaTldDefaultFactory();
		factoryTopDomain = new CFIntJpaTopDomainDefaultFactory();
		factoryTopProject = new CFIntJpaTopProjectDefaultFactory();
		factoryURLProtocol = new CFIntJpaURLProtocolDefaultFactory();	}

	@Override
	public ICFIntSchema newSchema() {
		throw new CFLibMustOverrideException( getClass(), "newSchema" );
	}

	@Override
	public short nextISOCcyIdGen() {
		throw new CFLibNotImplementedYetException( getClass(), "nextISOCcyIdGen" );
	}

	@Override
	public short nextISOCtryIdGen() {
		throw new CFLibNotImplementedYetException( getClass(), "nextISOCtryIdGen" );
	}

	@Override
	public short nextISOLangIdGen() {
		throw new CFLibNotImplementedYetException( getClass(), "nextISOLangIdGen" );
	}

	@Override
	public short nextISOTZoneIdGen() {
		throw new CFLibNotImplementedYetException( getClass(), "nextISOTZoneIdGen" );
	}

	@Override
	public int nextMimeTypeIdGen() {
		throw new CFLibNotImplementedYetException( getClass(), "nextMimeTypeIdGen" );
	}

	@Override
	public int nextURLProtocolIdGen() {
		throw new CFLibNotImplementedYetException( getClass(), "nextURLProtocolIdGen" );
	}

	@Override
	public CFLibDbKeyHash256 nextClusterIdGen() {
		CFLibDbKeyHash256 retval = new CFLibDbKeyHash256(0);
		return( retval );
	}

	@Override
	public CFLibDbKeyHash256 nextSecSessionIdGen() {
		CFLibDbKeyHash256 retval = new CFLibDbKeyHash256(0);
		return( retval );
	}

	@Override
	public CFLibDbKeyHash256 nextSecUserIdGen() {
		CFLibDbKeyHash256 retval = new CFLibDbKeyHash256(0);
		return( retval );
	}

	@Override
	public CFLibDbKeyHash256 nextServiceTypeIdGen() {
		CFLibDbKeyHash256 retval = new CFLibDbKeyHash256(0);
		return( retval );
	}

	@Override
	public CFLibDbKeyHash256 nextTenantIdGen() {
		CFLibDbKeyHash256 retval = new CFLibDbKeyHash256(0);
		return( retval );
	}

	@Override
	public CFLibDbKeyHash256 nextHostNodeIdGen() {
		CFLibDbKeyHash256 retval = new CFLibDbKeyHash256(0);
		return( retval );
	}

	@Override
	public CFLibDbKeyHash256 nextSecGroupIdGen() {
		CFLibDbKeyHash256 retval = new CFLibDbKeyHash256(0);
		return( retval );
	}

	@Override
	public CFLibDbKeyHash256 nextSecGrpIncIdGen() {
		CFLibDbKeyHash256 retval = new CFLibDbKeyHash256(0);
		return( retval );
	}

	@Override
	public CFLibDbKeyHash256 nextSecGrpMembIdGen() {
		CFLibDbKeyHash256 retval = new CFLibDbKeyHash256(0);
		return( retval );
	}

	@Override
	public CFLibDbKeyHash256 nextServiceIdGen() {
		CFLibDbKeyHash256 retval = new CFLibDbKeyHash256(0);
		return( retval );
	}

	@Override
	public CFLibDbKeyHash256 nextTSecGroupIdGen() {
		CFLibDbKeyHash256 retval = new CFLibDbKeyHash256(0);
		return( retval );
	}

	@Override
	public CFLibDbKeyHash256 nextTSecGrpIncIdGen() {
		CFLibDbKeyHash256 retval = new CFLibDbKeyHash256(0);
		return( retval );
	}

	@Override
	public CFLibDbKeyHash256 nextTSecGrpMembIdGen() {
		CFLibDbKeyHash256 retval = new CFLibDbKeyHash256(0);
		return( retval );
	}

	@Override
	public CFLibDbKeyHash256 nextMajorVersionIdGen() {
		CFLibDbKeyHash256 retval = new CFLibDbKeyHash256(0);
		return( retval );
	}

	@Override
	public CFLibDbKeyHash256 nextMinorVersionIdGen() {
		CFLibDbKeyHash256 retval = new CFLibDbKeyHash256(0);
		return( retval );
	}

	@Override
	public CFLibDbKeyHash256 nextSubProjectIdGen() {
		CFLibDbKeyHash256 retval = new CFLibDbKeyHash256(0);
		return( retval );
	}

	@Override
	public CFLibDbKeyHash256 nextTldIdGen() {
		CFLibDbKeyHash256 retval = new CFLibDbKeyHash256(0);
		return( retval );
	}

	@Override
	public CFLibDbKeyHash256 nextTopDomainIdGen() {
		CFLibDbKeyHash256 retval = new CFLibDbKeyHash256(0);
		return( retval );
	}

	@Override
	public CFLibDbKeyHash256 nextTopProjectIdGen() {
		CFLibDbKeyHash256 retval = new CFLibDbKeyHash256(0);
		return( retval );
	}

	@Override
	public CFLibDbKeyHash256 nextLicenseIdGen() {
		CFLibDbKeyHash256 retval = new CFLibDbKeyHash256(0);
		return( retval );
	}

	public ICFSecClusterTable getTableCluster() {
		return( tableCluster );
	}

	public void setTableCluster( ICFSecClusterTable value ) {
		tableCluster = value;
	}

	public ICFSecClusterFactory getFactoryCluster() {
		return( factoryCluster );
	}

	public void setFactoryCluster( ICFSecClusterFactory value ) {
		factoryCluster = value;
	}

	public ICFSecHostNodeTable getTableHostNode() {
		return( tableHostNode );
	}

	public void setTableHostNode( ICFSecHostNodeTable value ) {
		tableHostNode = value;
	}

	public ICFSecHostNodeFactory getFactoryHostNode() {
		return( factoryHostNode );
	}

	public void setFactoryHostNode( ICFSecHostNodeFactory value ) {
		factoryHostNode = value;
	}

	public ICFSecISOCcyTable getTableISOCcy() {
		return( tableISOCcy );
	}

	public void setTableISOCcy( ICFSecISOCcyTable value ) {
		tableISOCcy = value;
	}

	public ICFSecISOCcyFactory getFactoryISOCcy() {
		return( factoryISOCcy );
	}

	public void setFactoryISOCcy( ICFSecISOCcyFactory value ) {
		factoryISOCcy = value;
	}

	public ICFSecISOCtryTable getTableISOCtry() {
		return( tableISOCtry );
	}

	public void setTableISOCtry( ICFSecISOCtryTable value ) {
		tableISOCtry = value;
	}

	public ICFSecISOCtryFactory getFactoryISOCtry() {
		return( factoryISOCtry );
	}

	public void setFactoryISOCtry( ICFSecISOCtryFactory value ) {
		factoryISOCtry = value;
	}

	public ICFSecISOCtryCcyTable getTableISOCtryCcy() {
		return( tableISOCtryCcy );
	}

	public void setTableISOCtryCcy( ICFSecISOCtryCcyTable value ) {
		tableISOCtryCcy = value;
	}

	public ICFSecISOCtryCcyFactory getFactoryISOCtryCcy() {
		return( factoryISOCtryCcy );
	}

	public void setFactoryISOCtryCcy( ICFSecISOCtryCcyFactory value ) {
		factoryISOCtryCcy = value;
	}

	public ICFSecISOCtryLangTable getTableISOCtryLang() {
		return( tableISOCtryLang );
	}

	public void setTableISOCtryLang( ICFSecISOCtryLangTable value ) {
		tableISOCtryLang = value;
	}

	public ICFSecISOCtryLangFactory getFactoryISOCtryLang() {
		return( factoryISOCtryLang );
	}

	public void setFactoryISOCtryLang( ICFSecISOCtryLangFactory value ) {
		factoryISOCtryLang = value;
	}

	public ICFSecISOLangTable getTableISOLang() {
		return( tableISOLang );
	}

	public void setTableISOLang( ICFSecISOLangTable value ) {
		tableISOLang = value;
	}

	public ICFSecISOLangFactory getFactoryISOLang() {
		return( factoryISOLang );
	}

	public void setFactoryISOLang( ICFSecISOLangFactory value ) {
		factoryISOLang = value;
	}

	public ICFSecISOTZoneTable getTableISOTZone() {
		return( tableISOTZone );
	}

	public void setTableISOTZone( ICFSecISOTZoneTable value ) {
		tableISOTZone = value;
	}

	public ICFSecISOTZoneFactory getFactoryISOTZone() {
		return( factoryISOTZone );
	}

	public void setFactoryISOTZone( ICFSecISOTZoneFactory value ) {
		factoryISOTZone = value;
	}

	public ICFIntLicenseTable getTableLicense() {
		return( tableLicense );
	}

	public void setTableLicense( ICFIntLicenseTable value ) {
		tableLicense = value;
	}

	public ICFIntLicenseFactory getFactoryLicense() {
		return( factoryLicense );
	}

	public void setFactoryLicense( ICFIntLicenseFactory value ) {
		factoryLicense = value;
	}

	public ICFIntMajorVersionTable getTableMajorVersion() {
		return( tableMajorVersion );
	}

	public void setTableMajorVersion( ICFIntMajorVersionTable value ) {
		tableMajorVersion = value;
	}

	public ICFIntMajorVersionFactory getFactoryMajorVersion() {
		return( factoryMajorVersion );
	}

	public void setFactoryMajorVersion( ICFIntMajorVersionFactory value ) {
		factoryMajorVersion = value;
	}

	public ICFIntMimeTypeTable getTableMimeType() {
		return( tableMimeType );
	}

	public void setTableMimeType( ICFIntMimeTypeTable value ) {
		tableMimeType = value;
	}

	public ICFIntMimeTypeFactory getFactoryMimeType() {
		return( factoryMimeType );
	}

	public void setFactoryMimeType( ICFIntMimeTypeFactory value ) {
		factoryMimeType = value;
	}

	public ICFIntMinorVersionTable getTableMinorVersion() {
		return( tableMinorVersion );
	}

	public void setTableMinorVersion( ICFIntMinorVersionTable value ) {
		tableMinorVersion = value;
	}

	public ICFIntMinorVersionFactory getFactoryMinorVersion() {
		return( factoryMinorVersion );
	}

	public void setFactoryMinorVersion( ICFIntMinorVersionFactory value ) {
		factoryMinorVersion = value;
	}

	public ICFSecSecDeviceTable getTableSecDevice() {
		return( tableSecDevice );
	}

	public void setTableSecDevice( ICFSecSecDeviceTable value ) {
		tableSecDevice = value;
	}

	public ICFSecSecDeviceFactory getFactorySecDevice() {
		return( factorySecDevice );
	}

	public void setFactorySecDevice( ICFSecSecDeviceFactory value ) {
		factorySecDevice = value;
	}

	public ICFSecSecGroupTable getTableSecGroup() {
		return( tableSecGroup );
	}

	public void setTableSecGroup( ICFSecSecGroupTable value ) {
		tableSecGroup = value;
	}

	public ICFSecSecGroupFactory getFactorySecGroup() {
		return( factorySecGroup );
	}

	public void setFactorySecGroup( ICFSecSecGroupFactory value ) {
		factorySecGroup = value;
	}

	public ICFSecSecGrpIncTable getTableSecGrpInc() {
		return( tableSecGrpInc );
	}

	public void setTableSecGrpInc( ICFSecSecGrpIncTable value ) {
		tableSecGrpInc = value;
	}

	public ICFSecSecGrpIncFactory getFactorySecGrpInc() {
		return( factorySecGrpInc );
	}

	public void setFactorySecGrpInc( ICFSecSecGrpIncFactory value ) {
		factorySecGrpInc = value;
	}

	public ICFSecSecGrpMembTable getTableSecGrpMemb() {
		return( tableSecGrpMemb );
	}

	public void setTableSecGrpMemb( ICFSecSecGrpMembTable value ) {
		tableSecGrpMemb = value;
	}

	public ICFSecSecGrpMembFactory getFactorySecGrpMemb() {
		return( factorySecGrpMemb );
	}

	public void setFactorySecGrpMemb( ICFSecSecGrpMembFactory value ) {
		factorySecGrpMemb = value;
	}

	public ICFSecSecSessionTable getTableSecSession() {
		return( tableSecSession );
	}

	public void setTableSecSession( ICFSecSecSessionTable value ) {
		tableSecSession = value;
	}

	public ICFSecSecSessionFactory getFactorySecSession() {
		return( factorySecSession );
	}

	public void setFactorySecSession( ICFSecSecSessionFactory value ) {
		factorySecSession = value;
	}

	public ICFSecSecUserTable getTableSecUser() {
		return( tableSecUser );
	}

	public void setTableSecUser( ICFSecSecUserTable value ) {
		tableSecUser = value;
	}

	public ICFSecSecUserFactory getFactorySecUser() {
		return( factorySecUser );
	}

	public void setFactorySecUser( ICFSecSecUserFactory value ) {
		factorySecUser = value;
	}

	public ICFSecServiceTable getTableService() {
		return( tableService );
	}

	public void setTableService( ICFSecServiceTable value ) {
		tableService = value;
	}

	public ICFSecServiceFactory getFactoryService() {
		return( factoryService );
	}

	public void setFactoryService( ICFSecServiceFactory value ) {
		factoryService = value;
	}

	public ICFSecServiceTypeTable getTableServiceType() {
		return( tableServiceType );
	}

	public void setTableServiceType( ICFSecServiceTypeTable value ) {
		tableServiceType = value;
	}

	public ICFSecServiceTypeFactory getFactoryServiceType() {
		return( factoryServiceType );
	}

	public void setFactoryServiceType( ICFSecServiceTypeFactory value ) {
		factoryServiceType = value;
	}

	public ICFIntSubProjectTable getTableSubProject() {
		return( tableSubProject );
	}

	public void setTableSubProject( ICFIntSubProjectTable value ) {
		tableSubProject = value;
	}

	public ICFIntSubProjectFactory getFactorySubProject() {
		return( factorySubProject );
	}

	public void setFactorySubProject( ICFIntSubProjectFactory value ) {
		factorySubProject = value;
	}

	public ICFSecSysClusterTable getTableSysCluster() {
		return( tableSysCluster );
	}

	public void setTableSysCluster( ICFSecSysClusterTable value ) {
		tableSysCluster = value;
	}

	public ICFSecSysClusterFactory getFactorySysCluster() {
		return( factorySysCluster );
	}

	public void setFactorySysCluster( ICFSecSysClusterFactory value ) {
		factorySysCluster = value;
	}

	public ICFSecTSecGroupTable getTableTSecGroup() {
		return( tableTSecGroup );
	}

	public void setTableTSecGroup( ICFSecTSecGroupTable value ) {
		tableTSecGroup = value;
	}

	public ICFSecTSecGroupFactory getFactoryTSecGroup() {
		return( factoryTSecGroup );
	}

	public void setFactoryTSecGroup( ICFSecTSecGroupFactory value ) {
		factoryTSecGroup = value;
	}

	public ICFSecTSecGrpIncTable getTableTSecGrpInc() {
		return( tableTSecGrpInc );
	}

	public void setTableTSecGrpInc( ICFSecTSecGrpIncTable value ) {
		tableTSecGrpInc = value;
	}

	public ICFSecTSecGrpIncFactory getFactoryTSecGrpInc() {
		return( factoryTSecGrpInc );
	}

	public void setFactoryTSecGrpInc( ICFSecTSecGrpIncFactory value ) {
		factoryTSecGrpInc = value;
	}

	public ICFSecTSecGrpMembTable getTableTSecGrpMemb() {
		return( tableTSecGrpMemb );
	}

	public void setTableTSecGrpMemb( ICFSecTSecGrpMembTable value ) {
		tableTSecGrpMemb = value;
	}

	public ICFSecTSecGrpMembFactory getFactoryTSecGrpMemb() {
		return( factoryTSecGrpMemb );
	}

	public void setFactoryTSecGrpMemb( ICFSecTSecGrpMembFactory value ) {
		factoryTSecGrpMemb = value;
	}

	public ICFSecTenantTable getTableTenant() {
		return( tableTenant );
	}

	public void setTableTenant( ICFSecTenantTable value ) {
		tableTenant = value;
	}

	public ICFSecTenantFactory getFactoryTenant() {
		return( factoryTenant );
	}

	public void setFactoryTenant( ICFSecTenantFactory value ) {
		factoryTenant = value;
	}

	public ICFIntTldTable getTableTld() {
		return( tableTld );
	}

	public void setTableTld( ICFIntTldTable value ) {
		tableTld = value;
	}

	public ICFIntTldFactory getFactoryTld() {
		return( factoryTld );
	}

	public void setFactoryTld( ICFIntTldFactory value ) {
		factoryTld = value;
	}

	public ICFIntTopDomainTable getTableTopDomain() {
		return( tableTopDomain );
	}

	public void setTableTopDomain( ICFIntTopDomainTable value ) {
		tableTopDomain = value;
	}

	public ICFIntTopDomainFactory getFactoryTopDomain() {
		return( factoryTopDomain );
	}

	public void setFactoryTopDomain( ICFIntTopDomainFactory value ) {
		factoryTopDomain = value;
	}

	public ICFIntTopProjectTable getTableTopProject() {
		return( tableTopProject );
	}

	public void setTableTopProject( ICFIntTopProjectTable value ) {
		tableTopProject = value;
	}

	public ICFIntTopProjectFactory getFactoryTopProject() {
		return( factoryTopProject );
	}

	public void setFactoryTopProject( ICFIntTopProjectFactory value ) {
		factoryTopProject = value;
	}

	public ICFIntURLProtocolTable getTableURLProtocol() {
		return( tableURLProtocol );
	}

	public void setTableURLProtocol( ICFIntURLProtocolTable value ) {
		tableURLProtocol = value;
	}

	public ICFIntURLProtocolFactory getFactoryURLProtocol() {
		return( factoryURLProtocol );
	}

	public void setFactoryURLProtocol( ICFIntURLProtocolFactory value ) {
		factoryURLProtocol = value;
	}

	/**
	 *	Get the Table Permissions interface for the schema.
	 *
	 *	@return	The Table Permissions interface for the schema.
	 *
	 *	@throws CFLibNotSupportedException thrown by client-side implementations.
	 */
	public static ICFSecTablePerms getTablePerms() {
		return(CFSecJpaSchema.getTablePerms());
	}

	/**
	 *	Get the Table Permissions interface cast to this schema's implementation.
	 *
	 *	@return The Table Permissions interface for this schema.
	 */
	public static ICFIntTablePerms getCFIntTablePerms() {
		return (ICFIntTablePerms)getTablePerms();
	}

	/**
	 *	Set the Table Permissions interface for the schema.  All fractal subclasses of
	 *	the ICFSecTablePerms implement at least that interface plus their own
	 *	accessors.
	 *
	 *	@param	value	The Table Permissions interface to be used by the schema.
	 *
	 *	@throws CFLibNotSupportedException thrown by client-side implementations.
	 */
	public static void setTablePerms( ICFSecTablePerms value ) {
		CFSecJpaSchema.setTablePerms(value);
	}

	public void bootstrapSchema() {
	}
}
