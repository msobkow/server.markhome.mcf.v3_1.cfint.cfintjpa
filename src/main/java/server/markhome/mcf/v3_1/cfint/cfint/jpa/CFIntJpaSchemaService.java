// Description: Java 25 Spring JPA Service for CFInt

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
import java.net.InetAddress;
import java.time.*;
import java.util.*;
import jakarta.persistence.*;
import server.markhome.mcf.v3_1.cflib.*;
import server.markhome.mcf.v3_1.cflib.dbutil.*;
import server.markhome.mcf.v3_1.cflib.xml.CFLibXmlUtil;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import server.markhome.mcf.v3_1.cfsec.cfsec.*;
import server.markhome.mcf.v3_1.cfint.cfint.*;

/**
 *	Services for schema CFInt defined in server.markhome.mcf.v3_1.cfint.cfint.jpa
 *	using the CFInt*Repository objects to access the data directly, bypassing normal application security for the bootstrap and login processing.
 */
@Service("cfint31JpaSchemaService")
public class CFIntJpaSchemaService {

	@Autowired
	@Qualifier("cfint31EntityManagerFactory")
	private LocalContainerEntityManagerFactoryBean cfint31EntityManagerFactory;
	@Autowired
	private CFIntJpaLicenseService licenseService;

	@Autowired
	private CFIntJpaMajorVersionService majorversionService;

	@Autowired
	private CFIntJpaMimeTypeService mimetypeService;

	@Autowired
	private CFIntJpaMinorVersionService minorversionService;

	@Autowired
	private CFIntJpaSubProjectService subprojectService;

	@Autowired
	private CFIntJpaTldService tldService;

	@Autowired
	private CFIntJpaTopDomainService topdomainService;

	@Autowired
	private CFIntJpaTopProjectService topprojectService;

	@Autowired
	private CFIntJpaURLProtocolService urlprotocolService;


	public void bootstrapSchema() {
		ICFSecSchema.getBackingCFSec().bootstrapSchema();
		bootstrapAllTablesSecurity();
	}

	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = NoResultException.class, transactionManager = "$secdbschemaname$TransactionManager")
	public void bootstrapAllTablesSecurity() {
		LocalDateTime now = LocalDateTime.now();
		ICFSecSecSession bootstrapSession;
		CFLibDbKeyHash256 bootstrapSessionID = new CFLibDbKeyHash256(0);

		ICFSecAuthorization auth = new CFSecAuthorization();
		auth.setAuthUuid6(CFLibUuid6.generateUuid6());
		auth.setSecClusterId(ICFSecSchema.getSysClusterId());
		auth.setSecTenantId(ICFSecSchema.getSysTenantId());
		auth.setSecSessionId(bootstrapSessionID);

//ICFSecSchema.getSysTenantId(), ICFSecSchema.getSysAdminId()
		bootstrapSession = ICFSecSchema.getBackingCFSec().getFactorySecSession().newRec();
		bootstrapSession.setRequiredRevision(1);
		bootstrapSession.setRequiredSecSessionId(bootstrapSessionID);
		bootstrapSession.setRequiredSecUserId(ICFSecSchema.getSysAdminId());
		bootstrapSession.setOptionalSecProxyId(ICFSecSchema.getSysAdminId());
		bootstrapSession.setRequiredStart(now);
		bootstrapSession.setOptionalFinish(null);
		bootstrapSession = ICFSecSchema.getBackingCFSec().getTableSecSession().createSecSession(auth, bootstrapSession);
		bootstrapSessionID = bootstrapSession.getRequiredSecSessionId();

		ICFSecSecSysGrp secSysGroupSysAdmin = ICFSecSchema.getBackingCFSec().getTableSecSysGrp().readDerivedByUNameIdx( auth, "sysadmin");
		if (secSysGroupSysAdmin == null) {
			throw new CFLibNullArgumentException(getClass(), "bootstrapAllTablesSecurity", 0, "secSysGroupSysAdmin");
		}

		ICFSecSecClusGrp secSysClusGroupSysAdmin = ICFSecSchema.getBackingCFSec().getTableSecClusGrp().readDerivedByUNameIdx(auth, ICFSecSchema.getSysClusterId(), "sysclusteradmin");
		if (secSysClusGroupSysAdmin == null) {
			throw new CFLibNullArgumentException(getClass(), "bootstrapAllTablesSecurity", 0, "secSysClusGroupSysAdmin");
		}

		ICFSecSecTentGrp secSysTentGroupSysAdmin = ICFSecSchema.getBackingCFSec().getTableSecTentGrp().readDerivedByUNameIdx(auth, ICFSecSchema.getSysTenantId(), "systenantadmin");
		if (secSysTentGroupSysAdmin == null) {
			throw new CFLibNullArgumentException(getClass(), "bootstrapAllTablesSecurity", 0, "secSysTentGroupSysAdmin");
		}

		bootstrapTableSecurity(auth, now, "License", false, false, "Tenant", secSysGroupSysAdmin, secSysClusGroupSysAdmin, secSysTentGroupSysAdmin);
		bootstrapTableSecurity(auth, now, "MajorVersion", true, false, "Tenant", secSysGroupSysAdmin, secSysClusGroupSysAdmin, secSysTentGroupSysAdmin);
		bootstrapTableSecurity(auth, now, "MimeType", true, false, "System", secSysGroupSysAdmin, secSysClusGroupSysAdmin, secSysTentGroupSysAdmin);
		bootstrapTableSecurity(auth, now, "MinorVersion", true, false, "Tenant", secSysGroupSysAdmin, secSysClusGroupSysAdmin, secSysTentGroupSysAdmin);
		bootstrapTableSecurity(auth, now, "SubProject", true, false, "Tenant", secSysGroupSysAdmin, secSysClusGroupSysAdmin, secSysTentGroupSysAdmin);
		bootstrapTableSecurity(auth, now, "Tld", true, false, "Tenant", secSysGroupSysAdmin, secSysClusGroupSysAdmin, secSysTentGroupSysAdmin);
		bootstrapTableSecurity(auth, now, "TopDomain", true, false, "Tenant", secSysGroupSysAdmin, secSysClusGroupSysAdmin, secSysTentGroupSysAdmin);
		bootstrapTableSecurity(auth, now, "TopProject", true, false, "Tenant", secSysGroupSysAdmin, secSysClusGroupSysAdmin, secSysTentGroupSysAdmin);
		bootstrapTableSecurity(auth, now, "URLProtocol", true, false, "System", secSysGroupSysAdmin, secSysClusGroupSysAdmin, secSysTentGroupSysAdmin);
		if (bootstrapSession != null && bootstrapSessionID != null && !bootstrapSessionID.isNull() && bootstrapSession.getOptionalFinish() == null) {
			bootstrapSession.setOptionalFinish(LocalDateTime.now());
			bootstrapSession = ICFSecSchema.getBackingCFSec().getTableSecSession().updateSecSession(auth, bootstrapSession);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = NoResultException.class, transactionManager = "$secdbschemaname$TransactionManager")
	public void bootstrapTableSecurity(ICFSecAuthorization auth,
		LocalDateTime now,
		String tableName,
		boolean hasHistory,
		boolean isMutable,
		String secScope,
		ICFSecSecSysGrp secSysGroupSysAdmin,
		ICFSecSecClusGrp secSysClusGroupSysAdmin,
		ICFSecSecTentGrp secSysTentGroupSysAdmin )
	{
		ICFSecSchema.SecLevelEnum level;
		if (secScope.equalsIgnoreCase("global")) {
			level = ICFSecSchema.SecLevelEnum.Global;
		}
		else if (secScope.toLowerCase().startsWith("cluster")) {
			level = ICFSecSchema.SecLevelEnum.Cluster;
		}
		else if (secScope.toLowerCase().startsWith("tenant")) {
			level = ICFSecSchema.SecLevelEnum.Tenant;
		}
		else {
			level = ICFSecSchema.SecLevelEnum.System;
		}
			
		String lowerTableName = tableName.toLowerCase();
		String createPermName = "create" + lowerTableName;
		String readPermName = "read" + lowerTableName;
		String updatePermName = "update" + lowerTableName;
		String deletePermName = "delete" + lowerTableName;
		String restorePermName = "restore" + lowerTableName;
		String mutatePermName = "mutate" + lowerTableName;
		String sysadminGroup = secSysGroupSysAdmin.getRequiredName();
		String sysclusadminGroup = secSysClusGroupSysAdmin.getRequiredName();
		String systentadminGroup = secSysTentGroupSysAdmin.getRequiredName();

		ICFSecSecSysGrp secGroupCreate;
		CFLibDbKeyHash256 secGroupCreateID;
		ICFSecSecSysGrpInc secGroupCreateIncSysadmin;
		ICFSecSecSysGrp secGroupRead;
		CFLibDbKeyHash256 secGroupReadID;
		ICFSecSecSysGrpInc secGroupReadIncSysadmin;
		ICFSecSecSysGrp secGroupUpdate;
		CFLibDbKeyHash256 secGroupUpdateID;
		ICFSecSecSysGrpInc secGroupUpdateIncSysadmin;
		ICFSecSecSysGrp secGroupDelete;
		CFLibDbKeyHash256 secGroupDeleteID;
		ICFSecSecSysGrpInc secGroupDeleteIncSysadmin;
		ICFSecSecSysGrp secGroupRestore;
		CFLibDbKeyHash256 secGroupRestoreID;
		ICFSecSecSysGrpInc secGroupRestoreIncSysadmin;
		ICFSecSecSysGrp secGroupMutate;
		CFLibDbKeyHash256 secGroupMutateID;
		ICFSecSecSysGrpInc secGroupMutateIncSysadmin;

		ICFSecSecClusGrp csecGroupCreate;
		CFLibDbKeyHash256 csecGroupCreateID;
		ICFSecSecClusGrpInc csecGroupCreateIncSystentadmin;
		ICFSecSecClusGrp csecGroupRead;
		CFLibDbKeyHash256 csecGroupReadID;
		ICFSecSecClusGrpInc csecGroupReadIncSystentadmin;
		ICFSecSecClusGrp csecGroupUpdate;
		CFLibDbKeyHash256 csecGroupUpdateID;
		ICFSecSecClusGrpInc csecGroupUpdateIncSystentadmin;
		ICFSecSecClusGrp csecGroupDelete;
		CFLibDbKeyHash256 csecGroupDeleteID;
		ICFSecSecClusGrpInc csecGroupDeleteIncSystentadmin;
		ICFSecSecClusGrp csecGroupRestore;
		CFLibDbKeyHash256 csecGroupRestoreID;
		ICFSecSecClusGrpInc csecGroupRestoreIncSystentadmin;
		ICFSecSecClusGrp csecGroupMutate;
		CFLibDbKeyHash256 csecGroupMutateID;
		ICFSecSecClusGrpInc csecGroupMutateIncSystentadmin;
		
		ICFSecSecTentGrp tsecGroupCreate;
		CFLibDbKeyHash256 tsecGroupCreateID;
		ICFSecSecTentGrpInc tsecGroupCreateIncSystentadmin;
		ICFSecSecTentGrp tsecGroupRead;
		CFLibDbKeyHash256 tsecGroupReadID;
		ICFSecSecTentGrpInc tsecGroupReadIncSystentadmin;
		ICFSecSecTentGrp tsecGroupUpdate;
		CFLibDbKeyHash256 tsecGroupUpdateID;
		ICFSecSecTentGrpInc tsecGroupUpdateIncSystentadmin;
		ICFSecSecTentGrp tsecGroupDelete;
		CFLibDbKeyHash256 tsecGroupDeleteID;
		ICFSecSecTentGrpInc tsecGroupDeleteIncSystentadmin;
		ICFSecSecTentGrp tsecGroupRestore;
		CFLibDbKeyHash256 tsecGroupRestoreID;
		ICFSecSecTentGrpInc tsecGroupRestoreIncSystentadmin;
		ICFSecSecTentGrp tsecGroupMutate;
		CFLibDbKeyHash256 tsecGroupMutateID;
		ICFSecSecTentGrpInc tsecGroupMutateIncSystentadmin;

		secGroupCreate = ICFSecSchema.getBackingCFSec().getTableSecSysGrp().readDerivedByUNameIdx(auth, createPermName);
		if (secGroupCreate != null) {
			secGroupCreateID = secGroupCreate.getRequiredSecSysGrpId();
			secGroupCreateIncSysadmin = ICFSecSchema.getBackingCFSec().getTableSecSysGrpInc().readDerived(auth, secGroupCreateID, sysadminGroup);
		}
		else {
			secGroupCreateID = null;
			secGroupCreateIncSysadmin = null;
		}

		secGroupRead = ICFSecSchema.getBackingCFSec().getTableSecSysGrp().readDerivedByUNameIdx(auth, readPermName);
		if (secGroupRead != null) {
			secGroupReadID = secGroupRead.getRequiredSecSysGrpId();
			secGroupReadIncSysadmin = ICFSecSchema.getBackingCFSec().getTableSecSysGrpInc().readDerived(auth, secGroupReadID, sysadminGroup);
		}
		else {
			secGroupReadID = null;
			secGroupReadIncSysadmin = null;
		}

		secGroupUpdate = ICFSecSchema.getBackingCFSec().getTableSecSysGrp().readDerivedByUNameIdx(auth, updatePermName);
		if (secGroupUpdate != null) {
			secGroupUpdateID = secGroupUpdate.getRequiredSecSysGrpId();
			secGroupUpdateIncSysadmin = ICFSecSchema.getBackingCFSec().getTableSecSysGrpInc().readDerived(auth, secGroupUpdateID, sysadminGroup);
		}
		else {
			secGroupUpdateID = null;
			secGroupUpdateIncSysadmin = null;
		}

		secGroupDelete = ICFSecSchema.getBackingCFSec().getTableSecSysGrp().readDerivedByUNameIdx(auth, deletePermName);
		if (secGroupDelete != null) {
			secGroupDeleteID = secGroupDelete.getRequiredSecSysGrpId();
			secGroupDeleteIncSysadmin = ICFSecSchema.getBackingCFSec().getTableSecSysGrpInc().readDerived(auth, secGroupDeleteID, sysadminGroup);
		}
		else {
			secGroupDeleteID = null;
			secGroupDeleteIncSysadmin = null;
		}
		
		if (hasHistory) {
			secGroupRestore = ICFSecSchema.getBackingCFSec().getTableSecSysGrp().readDerivedByUNameIdx(auth, restorePermName);
			if (secGroupRestore != null) {
				secGroupRestoreID = secGroupRestore.getRequiredSecSysGrpId();
				secGroupRestoreIncSysadmin = ICFSecSchema.getBackingCFSec().getTableSecSysGrpInc().readDerived(auth, secGroupRestoreID, sysadminGroup);
			}
			else {
				secGroupRestoreID = null;
				secGroupRestoreIncSysadmin = null;
			}
		}
		else {
			secGroupRestore = null;
			secGroupRestoreID = null;
			secGroupRestoreIncSysadmin = null;
		}
		
		if (isMutable) {
			secGroupMutate = ICFSecSchema.getBackingCFSec().getTableSecSysGrp().readDerivedByUNameIdx(auth, mutatePermName);
			if (secGroupMutate != null) {
				secGroupMutateID = secGroupMutate.getRequiredSecSysGrpId();
				secGroupMutateIncSysadmin = ICFSecSchema.getBackingCFSec().getTableSecSysGrpInc().readDerived(auth, secGroupMutateID, sysadminGroup);
			}
			else {
				secGroupMutateID = null;
				secGroupMutateIncSysadmin = null;
			}
		}
		else {
			secGroupMutate = null;
			secGroupMutateID = null;
			secGroupMutateIncSysadmin = null;
		}

		if (secGroupCreateID == null || secGroupCreateID.isNull()) {
			secGroupCreateID = new CFLibDbKeyHash256(0);
		}
		if (secGroupReadID == null || secGroupReadID.isNull()) {
			secGroupReadID = new CFLibDbKeyHash256(0);
		}
		if (secGroupUpdateID == null || secGroupUpdateID.isNull()) {
			secGroupUpdateID = new CFLibDbKeyHash256(0);
		}
		if (secGroupDeleteID == null || secGroupDeleteID.isNull()) {
			secGroupDeleteID = new CFLibDbKeyHash256(0);
		}
		if (hasHistory) {
			if (secGroupRestoreID == null || secGroupRestoreID.isNull()) {
				secGroupRestoreID = new CFLibDbKeyHash256(0);
			}
		}
		if (isMutable) {
			if (secGroupMutateID == null || secGroupMutateID.isNull()) {
				secGroupMutateID = new CFLibDbKeyHash256(0);
			}
		}

		if (secGroupCreate == null) {
			secGroupCreate = ICFSecSchema.getBackingCFSec().getFactorySecSysGrp().newRec();
			secGroupCreate.setRequiredRevision(1);
			secGroupCreate.setCreatedAt(now);
			secGroupCreate.setCreatedByUserId(auth.getSecUserId());
			secGroupCreate.setUpdatedAt(now);
			secGroupCreate.setUpdatedByUserId(auth.getSecUserId());
			secGroupCreate.setRequiredName(createPermName);
			secGroupCreate.setRequiredSecLevel(level);
			secGroupCreate.setRequiredSecSysGrpId(secGroupCreateID);
			secGroupCreate = ICFSecSchema.getBackingCFSec().getTableSecSysGrp().createSecSysGrp(auth, secGroupCreate);
			secGroupCreateID = secGroupCreate.getRequiredSecSysGrpId();
		}

		if (secGroupCreateIncSysadmin == null) {
			secGroupCreateIncSysadmin = ICFSecSchema.getBackingCFSec().getFactorySecSysGrpInc().newRec();
			secGroupCreateIncSysadmin.setRequiredRevision(1);
			secGroupCreateIncSysadmin.setCreatedAt(now);
			secGroupCreateIncSysadmin.setCreatedByUserId(auth.getSecUserId());
			secGroupCreateIncSysadmin.setUpdatedAt(now);
			secGroupCreateIncSysadmin.setUpdatedByUserId(auth.getSecUserId());
			secGroupCreateIncSysadmin.setRequiredContainerGroup(secGroupCreateID);
			secGroupCreateIncSysadmin.setRequiredParentSubGroup(sysadminGroup);
			secGroupCreateIncSysadmin = ICFSecSchema.getBackingCFSec().getTableSecSysGrpInc().createSecSysGrpInc(auth, secGroupCreateIncSysadmin);
		}

		if (secGroupRead == null) {
			secGroupRead = ICFSecSchema.getBackingCFSec().getFactorySecSysGrp().newRec();
			secGroupRead.setRequiredRevision(1);
			secGroupRead.setCreatedAt(now);
			secGroupRead.setCreatedByUserId(auth.getSecUserId());
			secGroupRead.setUpdatedAt(now);
			secGroupRead.setUpdatedByUserId(auth.getSecUserId());
			secGroupRead.setRequiredName(readPermName);
			secGroupRead.setRequiredSecLevel(level);
			secGroupRead.setRequiredSecSysGrpId(secGroupReadID);
			secGroupRead = ICFSecSchema.getBackingCFSec().getTableSecSysGrp().createSecSysGrp(auth, secGroupRead);
			secGroupReadID = secGroupRead.getRequiredSecSysGrpId();
		}

		if (secGroupReadIncSysadmin == null) {
			secGroupReadIncSysadmin = ICFSecSchema.getBackingCFSec().getFactorySecSysGrpInc().newRec();
			secGroupReadIncSysadmin.setRequiredRevision(1);
			secGroupReadIncSysadmin.setCreatedAt(now);
			secGroupReadIncSysadmin.setCreatedByUserId(auth.getSecUserId());
			secGroupReadIncSysadmin.setUpdatedAt(now);
			secGroupReadIncSysadmin.setUpdatedByUserId(auth.getSecUserId());
			secGroupReadIncSysadmin.setRequiredContainerGroup(secGroupReadID);
			secGroupReadIncSysadmin.setRequiredParentSubGroup(sysadminGroup);
			secGroupReadIncSysadmin = ICFSecSchema.getBackingCFSec().getTableSecSysGrpInc().createSecSysGrpInc(auth, secGroupReadIncSysadmin);
		}

		if (secGroupUpdate == null) {
			secGroupUpdate = ICFSecSchema.getBackingCFSec().getFactorySecSysGrp().newRec();
			secGroupUpdate.setRequiredRevision(1);
			secGroupUpdate.setCreatedAt(now);
			secGroupUpdate.setCreatedByUserId(auth.getSecUserId());
			secGroupUpdate.setUpdatedAt(now);
			secGroupUpdate.setUpdatedByUserId(auth.getSecUserId());
			secGroupUpdate.setRequiredName(updatePermName);
			secGroupUpdate.setRequiredSecLevel(level);
			secGroupUpdate.setRequiredSecSysGrpId(secGroupUpdateID);
			secGroupUpdate = ICFSecSchema.getBackingCFSec().getTableSecSysGrp().createSecSysGrp(auth, secGroupUpdate);
			secGroupUpdateID = secGroupUpdate.getRequiredSecSysGrpId();
		}

		if (secGroupUpdateIncSysadmin == null) {
			secGroupUpdateIncSysadmin = ICFSecSchema.getBackingCFSec().getFactorySecSysGrpInc().newRec();
			secGroupUpdateIncSysadmin.setRequiredRevision(1);
			secGroupUpdateIncSysadmin.setCreatedAt(now);
			secGroupUpdateIncSysadmin.setCreatedByUserId(auth.getSecUserId());
			secGroupUpdateIncSysadmin.setUpdatedAt(now);
			secGroupUpdateIncSysadmin.setUpdatedByUserId(auth.getSecUserId());
			secGroupUpdateIncSysadmin.setRequiredContainerGroup(secGroupUpdateID);
			secGroupUpdateIncSysadmin.setRequiredParentSubGroup(sysadminGroup);
			secGroupUpdateIncSysadmin = ICFSecSchema.getBackingCFSec().getTableSecSysGrpInc().createSecSysGrpInc(auth, secGroupUpdateIncSysadmin);
		}

		if (secGroupDelete == null) {
			secGroupDelete = ICFSecSchema.getBackingCFSec().getFactorySecSysGrp().newRec();
			secGroupDelete.setRequiredRevision(1);
			secGroupDelete.setCreatedAt(now);
			secGroupDelete.setCreatedByUserId(auth.getSecUserId());
			secGroupDelete.setUpdatedAt(now);
			secGroupDelete.setUpdatedByUserId(auth.getSecUserId());
			secGroupDelete.setRequiredName(deletePermName);
			secGroupDelete.setRequiredSecLevel(level);
			secGroupDelete.setRequiredSecSysGrpId(secGroupDeleteID);
			secGroupDelete = ICFSecSchema.getBackingCFSec().getTableSecSysGrp().createSecSysGrp(auth, secGroupDelete);
			secGroupDeleteID = secGroupDelete.getRequiredSecSysGrpId();
		}

		if (secGroupDeleteIncSysadmin == null) {
			secGroupDeleteIncSysadmin = ICFSecSchema.getBackingCFSec().getFactorySecSysGrpInc().newRec();
			secGroupDeleteIncSysadmin.setRequiredRevision(1);
			secGroupDeleteIncSysadmin.setCreatedAt(now);
			secGroupDeleteIncSysadmin.setCreatedByUserId(auth.getSecUserId());
			secGroupDeleteIncSysadmin.setUpdatedAt(now);
			secGroupDeleteIncSysadmin.setUpdatedByUserId(auth.getSecUserId());
			secGroupDeleteIncSysadmin.setRequiredContainerGroup(secGroupDeleteID);
			secGroupDeleteIncSysadmin.setRequiredParentSubGroup(sysadminGroup);
			secGroupDeleteIncSysadmin = ICFSecSchema.getBackingCFSec().getTableSecSysGrpInc().createSecSysGrpInc(auth, secGroupDeleteIncSysadmin);
		}
		
		if (hasHistory) {
			if (secGroupRestore == null) {
				secGroupRestore = ICFSecSchema.getBackingCFSec().getFactorySecSysGrp().newRec();
				secGroupRestore.setRequiredRevision(1);
				secGroupRestore.setCreatedAt(now);
				secGroupRestore.setCreatedByUserId(auth.getSecUserId());
				secGroupRestore.setUpdatedAt(now);
				secGroupRestore.setUpdatedByUserId(auth.getSecUserId());
				secGroupRestore.setRequiredName(restorePermName);
				secGroupRestore.setRequiredSecLevel(level);
				secGroupRestore.setRequiredSecSysGrpId(secGroupRestoreID);
				secGroupRestore = ICFSecSchema.getBackingCFSec().getTableSecSysGrp().createSecSysGrp(auth, secGroupRestore);
				secGroupRestoreID = secGroupRestore.getRequiredSecSysGrpId();
			}

			if (secGroupRestoreIncSysadmin == null) {
				secGroupRestoreIncSysadmin = ICFSecSchema.getBackingCFSec().getFactorySecSysGrpInc().newRec();
				secGroupRestoreIncSysadmin.setRequiredRevision(1);
				secGroupRestoreIncSysadmin.setCreatedAt(now);
				secGroupRestoreIncSysadmin.setCreatedByUserId(auth.getSecUserId());
				secGroupRestoreIncSysadmin.setUpdatedAt(now);
				secGroupRestoreIncSysadmin.setUpdatedByUserId(auth.getSecUserId());
				secGroupRestoreIncSysadmin.setRequiredContainerGroup(secGroupRestoreID);
				secGroupRestoreIncSysadmin.setRequiredParentSubGroup(sysadminGroup);
				secGroupRestoreIncSysadmin = ICFSecSchema.getBackingCFSec().getTableSecSysGrpInc().createSecSysGrpInc(auth, secGroupRestoreIncSysadmin);
			}
		}
		
		if (isMutable) {
			if (secGroupMutate == null) {
				secGroupMutate = ICFSecSchema.getBackingCFSec().getFactorySecSysGrp().newRec();
				secGroupMutate.setRequiredRevision(1);
				secGroupMutate.setCreatedAt(now);
				secGroupMutate.setCreatedByUserId(auth.getSecUserId());
				secGroupMutate.setUpdatedAt(now);
				secGroupMutate.setUpdatedByUserId(auth.getSecUserId());
				secGroupMutate.setRequiredName(mutatePermName);
				secGroupMutate.setRequiredSecLevel(level);
				secGroupMutate.setRequiredSecSysGrpId(secGroupMutateID);
				secGroupMutate = ICFSecSchema.getBackingCFSec().getTableSecSysGrp().createSecSysGrp(auth, secGroupMutate);
				secGroupMutateID = secGroupMutate.getRequiredSecSysGrpId();
			}

			if (secGroupMutateIncSysadmin == null) {
				secGroupMutateIncSysadmin = ICFSecSchema.getBackingCFSec().getFactorySecSysGrpInc().newRec();
				secGroupMutateIncSysadmin.setRequiredRevision(1);
				secGroupMutateIncSysadmin.setCreatedAt(now);
				secGroupMutateIncSysadmin.setCreatedByUserId(auth.getSecUserId());
				secGroupMutateIncSysadmin.setUpdatedAt(now);
				secGroupMutateIncSysadmin.setUpdatedByUserId(auth.getSecUserId());
				secGroupMutateIncSysadmin.setRequiredContainerGroup(secGroupMutateID);
				secGroupMutateIncSysadmin.setRequiredParentSubGroup(sysadminGroup);
				secGroupMutateIncSysadmin = ICFSecSchema.getBackingCFSec().getTableSecSysGrpInc().createSecSysGrpInc(auth, secGroupMutateIncSysadmin);
			}
		}
		
		if (level == ICFSecSchema.SecLevelEnum.Cluster ) {
		}
		else if (level == ICFSecSchema.SecLevelEnum.Tenant ) {
		}
	}		


}
