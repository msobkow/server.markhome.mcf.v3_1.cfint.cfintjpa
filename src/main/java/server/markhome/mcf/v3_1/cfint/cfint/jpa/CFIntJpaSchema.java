// Description: Java 25 JPA implementation of a CFInt schema.

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
//package server.markhome.mcf.v3_1.cfint.cfint.jpa;

import java.io.Serializable;
import java.math.*;
import java.time.*;
import java.net.InetAddress;
import java.util.*;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.concurrent.atomic.*;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import server.markhome.mcf.v3_1.cflib.*;
import server.markhome.mcf.v3_1.cflib.dbutil.*;
import server.markhome.mcf.v3_1.cflib.inz.Inz;
import server.markhome.mcf.v3_1.cflib.xml.CFLibXmlUtil;
import server.markhome.mcf.v3_1.cfsec.cfsec.*;
import server.markhome.mcf.v3_1.cfint.cfint.*;
import server.markhome.mcf.v3_1.cfsec.cfsec.jpa.*;
import server.markhome.mcf.v3_1.cfint.cfint.jpa.CFIntJpaHooksSchema;

public class CFIntJpaSchema
	implements ICFIntSchema,
		ICFSecSchema
{
	private CFIntJpaHooksSchema cfintJpaHooksSchema = null;

	protected ICFSecClusterTable tableCluster;
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
	protected ICFSecSecClusGrpTable tableSecClusGrp;
	protected ICFSecSecClusGrpIncTable tableSecClusGrpInc;
	protected ICFSecSecClusGrpMembTable tableSecClusGrpMemb;
	protected ICFSecSecSessionTable tableSecSession;
	protected ICFSecSecSysGrpTable tableSecSysGrp;
	protected ICFSecSecSysGrpIncTable tableSecSysGrpInc;
	protected ICFSecSecSysGrpMembTable tableSecSysGrpMemb;
	protected ICFSecSecTentGrpTable tableSecTentGrp;
	protected ICFSecSecTentGrpIncTable tableSecTentGrpInc;
	protected ICFSecSecTentGrpMembTable tableSecTentGrpMemb;
	protected ICFSecSecUserTable tableSecUser;
	protected ICFSecSecUserEMConfTable tableSecUserEMConf;
	protected ICFSecSecUserPWHistoryTable tableSecUserPWHistory;
	protected ICFSecSecUserPWResetTable tableSecUserPWReset;
	protected ICFSecSecUserPasswordTable tableSecUserPassword;
	protected ICFIntSubProjectTable tableSubProject;
	protected ICFSecSysClusterTable tableSysCluster;
	protected ICFSecTenantTable tableTenant;
	protected ICFIntTldTable tableTld;
	protected ICFIntTopDomainTable tableTopDomain;
	protected ICFIntTopProjectTable tableTopProject;
	protected ICFIntURLProtocolTable tableURLProtocol;

	protected ICFSecClusterFactory factoryCluster;
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
	protected ICFSecSecClusGrpFactory factorySecClusGrp;
	protected ICFSecSecClusGrpIncFactory factorySecClusGrpInc;
	protected ICFSecSecClusGrpMembFactory factorySecClusGrpMemb;
	protected ICFSecSecSessionFactory factorySecSession;
	protected ICFSecSecSysGrpFactory factorySecSysGrp;
	protected ICFSecSecSysGrpIncFactory factorySecSysGrpInc;
	protected ICFSecSecSysGrpMembFactory factorySecSysGrpMemb;
	protected ICFSecSecTentGrpFactory factorySecTentGrp;
	protected ICFSecSecTentGrpIncFactory factorySecTentGrpInc;
	protected ICFSecSecTentGrpMembFactory factorySecTentGrpMemb;
	protected ICFSecSecUserFactory factorySecUser;
	protected ICFSecSecUserEMConfFactory factorySecUserEMConf;
	protected ICFSecSecUserPWHistoryFactory factorySecUserPWHistory;
	protected ICFSecSecUserPWResetFactory factorySecUserPWReset;
	protected ICFSecSecUserPasswordFactory factorySecUserPassword;
	protected ICFIntSubProjectFactory factorySubProject;
	protected ICFSecSysClusterFactory factorySysCluster;
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

	public CFIntJpaHooksSchema getJpaHooksSchema() {
		return( cfintJpaHooksSchema );
	}

	public void setJpaHooksSchema(CFIntJpaHooksSchema jpaHooksSchema) {
		cfintJpaHooksSchema = jpaHooksSchema;
	}

	public CFIntJpaSchemaService getSchemaService() {
		return( getJpaHooksSchema().getSchemaService() );
	}

	public CFIntJpaSchema() {

		tableCluster = null;
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
		tableSecClusGrp = null;
		tableSecClusGrpInc = null;
		tableSecClusGrpMemb = null;
		tableSecSession = null;
		tableSecSysGrp = null;
		tableSecSysGrpInc = null;
		tableSecSysGrpMemb = null;
		tableSecTentGrp = null;
		tableSecTentGrpInc = null;
		tableSecTentGrpMemb = null;
		tableSecUser = null;
		tableSecUserEMConf = null;
		tableSecUserPWHistory = null;
		tableSecUserPWReset = null;
		tableSecUserPassword = null;
		tableSubProject = null;
		tableSysCluster = null;
		tableTenant = null;
		tableTld = null;
		tableTopDomain = null;
		tableTopProject = null;
		tableURLProtocol = null;

		factoryCluster = new CFSecJpaClusterDefaultFactory();
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
		factorySecClusGrp = new CFSecJpaSecClusGrpDefaultFactory();
		factorySecClusGrpInc = new CFSecJpaSecClusGrpIncDefaultFactory();
		factorySecClusGrpMemb = new CFSecJpaSecClusGrpMembDefaultFactory();
		factorySecSession = new CFSecJpaSecSessionDefaultFactory();
		factorySecSysGrp = new CFSecJpaSecSysGrpDefaultFactory();
		factorySecSysGrpInc = new CFSecJpaSecSysGrpIncDefaultFactory();
		factorySecSysGrpMemb = new CFSecJpaSecSysGrpMembDefaultFactory();
		factorySecTentGrp = new CFSecJpaSecTentGrpDefaultFactory();
		factorySecTentGrpInc = new CFSecJpaSecTentGrpIncDefaultFactory();
		factorySecTentGrpMemb = new CFSecJpaSecTentGrpMembDefaultFactory();
		factorySecUser = new CFSecJpaSecUserDefaultFactory();
		factorySecUserEMConf = new CFSecJpaSecUserEMConfDefaultFactory();
		factorySecUserPWHistory = new CFSecJpaSecUserPWHistoryDefaultFactory();
		factorySecUserPWReset = new CFSecJpaSecUserPWResetDefaultFactory();
		factorySecUserPassword = new CFSecJpaSecUserPasswordDefaultFactory();
		factorySubProject = new CFIntJpaSubProjectDefaultFactory();
		factorySysCluster = new CFSecJpaSysClusterDefaultFactory();
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
	public boolean isMemberOfTenantGroup(CFLibDbKeyHash256 userId, CFLibDbKeyHash256 clusterId, CFLibDbKeyHash256 tenantId, String permissionName) {
		return ICFSecSchema.getBackingCFSec().isMemberOfTenantGroup(userId, clusterId, tenantId, permissionName);
	}

	@Override
	public boolean isMemberOfTenantGroup(String userLogin, CFLibDbKeyHash256 clusterId, CFLibDbKeyHash256 tenantId, String permissionName) {
		return ICFSecSchema.getBackingCFSec().isMemberOfTenantGroup(userLogin, clusterId, tenantId, permissionName);
	}

	@Override
	public boolean isMemberOfClusterGroup(CFLibDbKeyHash256 userId, CFLibDbKeyHash256 clusterId, String permissionName) {
		return ICFSecSchema.getBackingCFSec().isMemberOfClusterGroup(userId, clusterId, permissionName);
	}

	@Override
	public boolean isMemberOfClusterGroup(String userLogin, CFLibDbKeyHash256 clusterId, String permissionName) {
		return ICFSecSchema.getBackingCFSec().isMemberOfClusterGroup(userLogin, clusterId, permissionName);
	}

	@Override
	public boolean isMemberOfSystemGroup(CFLibDbKeyHash256 userId, String permissionName) {
		return ICFSecSchema.getBackingCFSec().isMemberOfSystemGroup(userId, permissionName);
	}

	@Override
	public boolean isMemberOfSystemGroup(String userLogin, String permissionName) {
		return ICFSecSchema.getBackingCFSec().isMemberOfSystemGroup(userLogin, permissionName);
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
	public CFLibDbKeyHash256 nextTenantIdGen() {
		CFLibDbKeyHash256 retval = new CFLibDbKeyHash256(0);
		return( retval );
	}

	@Override
	public CFLibDbKeyHash256 nextSecSysGrpIdGen() {
		CFLibDbKeyHash256 retval = new CFLibDbKeyHash256(0);
		return( retval );
	}

	@Override
	public CFLibDbKeyHash256 nextSecClusGrpIdGen() {
		CFLibDbKeyHash256 retval = new CFLibDbKeyHash256(0);
		return( retval );
	}

	@Override
	public CFLibDbKeyHash256 nextSecTentGrpIdGen() {
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

	public ICFSecSecClusGrpTable getTableSecClusGrp() {
		return( tableSecClusGrp );
	}

	public void setTableSecClusGrp( ICFSecSecClusGrpTable value ) {
		tableSecClusGrp = value;
	}

	public ICFSecSecClusGrpFactory getFactorySecClusGrp() {
		return( factorySecClusGrp );
	}

	public void setFactorySecClusGrp( ICFSecSecClusGrpFactory value ) {
		factorySecClusGrp = value;
	}

	public ICFSecSecClusGrpIncTable getTableSecClusGrpInc() {
		return( tableSecClusGrpInc );
	}

	public void setTableSecClusGrpInc( ICFSecSecClusGrpIncTable value ) {
		tableSecClusGrpInc = value;
	}

	public ICFSecSecClusGrpIncFactory getFactorySecClusGrpInc() {
		return( factorySecClusGrpInc );
	}

	public void setFactorySecClusGrpInc( ICFSecSecClusGrpIncFactory value ) {
		factorySecClusGrpInc = value;
	}

	public ICFSecSecClusGrpMembTable getTableSecClusGrpMemb() {
		return( tableSecClusGrpMemb );
	}

	public void setTableSecClusGrpMemb( ICFSecSecClusGrpMembTable value ) {
		tableSecClusGrpMemb = value;
	}

	public ICFSecSecClusGrpMembFactory getFactorySecClusGrpMemb() {
		return( factorySecClusGrpMemb );
	}

	public void setFactorySecClusGrpMemb( ICFSecSecClusGrpMembFactory value ) {
		factorySecClusGrpMemb = value;
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

	public ICFSecSecSysGrpTable getTableSecSysGrp() {
		return( tableSecSysGrp );
	}

	public void setTableSecSysGrp( ICFSecSecSysGrpTable value ) {
		tableSecSysGrp = value;
	}

	public ICFSecSecSysGrpFactory getFactorySecSysGrp() {
		return( factorySecSysGrp );
	}

	public void setFactorySecSysGrp( ICFSecSecSysGrpFactory value ) {
		factorySecSysGrp = value;
	}

	public ICFSecSecSysGrpIncTable getTableSecSysGrpInc() {
		return( tableSecSysGrpInc );
	}

	public void setTableSecSysGrpInc( ICFSecSecSysGrpIncTable value ) {
		tableSecSysGrpInc = value;
	}

	public ICFSecSecSysGrpIncFactory getFactorySecSysGrpInc() {
		return( factorySecSysGrpInc );
	}

	public void setFactorySecSysGrpInc( ICFSecSecSysGrpIncFactory value ) {
		factorySecSysGrpInc = value;
	}

	public ICFSecSecSysGrpMembTable getTableSecSysGrpMemb() {
		return( tableSecSysGrpMemb );
	}

	public void setTableSecSysGrpMemb( ICFSecSecSysGrpMembTable value ) {
		tableSecSysGrpMemb = value;
	}

	public ICFSecSecSysGrpMembFactory getFactorySecSysGrpMemb() {
		return( factorySecSysGrpMemb );
	}

	public void setFactorySecSysGrpMemb( ICFSecSecSysGrpMembFactory value ) {
		factorySecSysGrpMemb = value;
	}

	public ICFSecSecTentGrpTable getTableSecTentGrp() {
		return( tableSecTentGrp );
	}

	public void setTableSecTentGrp( ICFSecSecTentGrpTable value ) {
		tableSecTentGrp = value;
	}

	public ICFSecSecTentGrpFactory getFactorySecTentGrp() {
		return( factorySecTentGrp );
	}

	public void setFactorySecTentGrp( ICFSecSecTentGrpFactory value ) {
		factorySecTentGrp = value;
	}

	public ICFSecSecTentGrpIncTable getTableSecTentGrpInc() {
		return( tableSecTentGrpInc );
	}

	public void setTableSecTentGrpInc( ICFSecSecTentGrpIncTable value ) {
		tableSecTentGrpInc = value;
	}

	public ICFSecSecTentGrpIncFactory getFactorySecTentGrpInc() {
		return( factorySecTentGrpInc );
	}

	public void setFactorySecTentGrpInc( ICFSecSecTentGrpIncFactory value ) {
		factorySecTentGrpInc = value;
	}

	public ICFSecSecTentGrpMembTable getTableSecTentGrpMemb() {
		return( tableSecTentGrpMemb );
	}

	public void setTableSecTentGrpMemb( ICFSecSecTentGrpMembTable value ) {
		tableSecTentGrpMemb = value;
	}

	public ICFSecSecTentGrpMembFactory getFactorySecTentGrpMemb() {
		return( factorySecTentGrpMemb );
	}

	public void setFactorySecTentGrpMemb( ICFSecSecTentGrpMembFactory value ) {
		factorySecTentGrpMemb = value;
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

	public ICFSecSecUserEMConfTable getTableSecUserEMConf() {
		return( tableSecUserEMConf );
	}

	public void setTableSecUserEMConf( ICFSecSecUserEMConfTable value ) {
		tableSecUserEMConf = value;
	}

	public ICFSecSecUserEMConfFactory getFactorySecUserEMConf() {
		return( factorySecUserEMConf );
	}

	public void setFactorySecUserEMConf( ICFSecSecUserEMConfFactory value ) {
		factorySecUserEMConf = value;
	}

	public ICFSecSecUserPWHistoryTable getTableSecUserPWHistory() {
		return( tableSecUserPWHistory );
	}

	public void setTableSecUserPWHistory( ICFSecSecUserPWHistoryTable value ) {
		tableSecUserPWHistory = value;
	}

	public ICFSecSecUserPWHistoryFactory getFactorySecUserPWHistory() {
		return( factorySecUserPWHistory );
	}

	public void setFactorySecUserPWHistory( ICFSecSecUserPWHistoryFactory value ) {
		factorySecUserPWHistory = value;
	}

	public ICFSecSecUserPWResetTable getTableSecUserPWReset() {
		return( tableSecUserPWReset );
	}

	public void setTableSecUserPWReset( ICFSecSecUserPWResetTable value ) {
		tableSecUserPWReset = value;
	}

	public ICFSecSecUserPWResetFactory getFactorySecUserPWReset() {
		return( factorySecUserPWReset );
	}

	public void setFactorySecUserPWReset( ICFSecSecUserPWResetFactory value ) {
		factorySecUserPWReset = value;
	}

	public ICFSecSecUserPasswordTable getTableSecUserPassword() {
		return( tableSecUserPassword );
	}

	public void setTableSecUserPassword( ICFSecSecUserPasswordTable value ) {
		tableSecUserPassword = value;
	}

	public ICFSecSecUserPasswordFactory getFactorySecUserPassword() {
		return( factorySecUserPassword );
	}

	public void setFactorySecUserPassword( ICFSecSecUserPasswordFactory value ) {
		factorySecUserPassword = value;
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

	public void bootstrapSchema(CFSecTableInfo tableInfo[]) {
		ICFSecSchema.getBackingCFSec().bootstrapSchema(tableInfo);
	}

	public void bootstrapAllTablesSecurity(CFLibDbKeyHash256 clusterId, CFLibDbKeyHash256 tenantId, CFSecTableInfo tableInfo[]) {
		ICFSecSchema.getBackingCFSec().bootstrapAllTablesSecurity(clusterId, tenantId, tableInfo);
	}
}
