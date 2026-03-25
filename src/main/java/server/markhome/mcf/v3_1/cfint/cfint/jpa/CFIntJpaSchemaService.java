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
		bootstrapSession.setOptionalSecDevName(null);
		bootstrapSession.setRequiredStart(now);
		bootstrapSession.setOptionalFinish(null);
		bootstrapSession = ICFSecSchema.getBackingCFSec().getTableSecSession().createSecSession(auth, bootstrapSession);
		bootstrapSessionID = bootstrapSession.getRequiredSecSessionId();

		bootstrapTableSecurity(auth, "License", false, false);
		bootstrapTableSecurity(auth, "MajorVersion", true, false);
		bootstrapTableSecurity(auth, "MimeType", true, false);
		bootstrapTableSecurity(auth, "MinorVersion", true, false);
		bootstrapTableSecurity(auth, "SubProject", true, false);
		bootstrapTableSecurity(auth, "Tld", true, false);
		bootstrapTableSecurity(auth, "TopDomain", true, false);
		bootstrapTableSecurity(auth, "TopProject", true, false);
		bootstrapTableSecurity(auth, "URLProtocol", true, false);

		if (bootstrapSession != null && bootstrapSessionID != null && !bootstrapSessionID.isNull() && bootstrapSession.getOptionalFinish() == null) {
			bootstrapSession.setOptionalFinish(LocalDateTime.now());
			bootstrapSession = ICFSecSchema.getBackingCFSec().getTableSecSession().updateSecSession(auth, bootstrapSession);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = NoResultException.class, transactionManager = "$secdbschemaname$TransactionManager")
	public void bootstrapTableSecurity(ICFSecAuthorization auth, String tableName, boolean hasHistory, boolean isMutable, boolean isTenantScoped) {
/**
		LocalDateTime now = LocalDateTime.now();
		String lowerTableName = tableName.toLowerCase();
		String createPermName = "create" + lowerTableName;
		String readPermName = "read" + lowerTableName;
		String updatePermName = "update" + lowerTableName;
		String deletePermName = "delete" + lowerTableName;
		String restorePermName = "restore" + lowerTableName;
		String mutatePermName = "mutate" + lowerTableName;
		ICFSecSecGroup secGroupCreate;
		CFLibDbKeyHash256 secGroupCreateID;
		ICFSecSecGrpMemb secGroupCreateMembSysadmin;
		CFLibDbKeyHash256 secGroupCreateMembSysadminID;
		ICFSecSecGroup secGroupRead;
		CFLibDbKeyHash256 secGroupReadID;
		ICFSecSecGrpMemb secGroupReadMembSysadmin;
		CFLibDbKeyHash256 secGroupReadMembSysadminID;
		ICFSecSecGroup secGroupUpdate;
		CFLibDbKeyHash256 secGroupUpdateID;
		ICFSecSecGrpMemb secGroupUpdateMembSysadmin;
		CFLibDbKeyHash256 secGroupUpdateMembSysadminID;
		ICFSecSecGroup secGroupDelete;
		CFLibDbKeyHash256 secGroupDeleteID;
		ICFSecSecGrpMemb secGroupDeleteMembSysadmin;
		CFLibDbKeyHash256 secGroupDeleteMembSysadminID;
		ICFSecSecGroup secGroupRestore;
		CFLibDbKeyHash256 secGroupRestoreID;
		ICFSecSecGrpMemb secGroupRestoreMembSysadmin;
		CFLibDbKeyHash256 secGroupRestoreMembSysadminID;
		ICFSecSecGroup secGroupMutate;
		CFLibDbKeyHash256 secGroupMutateID;
		ICFSecSecGrpMemb secGroupMutateMembSysadmin;
		CFLibDbKeyHash256 secGroupMutateMembSysadminID;
		ICFSecTSecGroup tsecGroupCreate;
		CFLibDbKeyHash256 tsecGroupCreateID;
		ICFSecTSecGrpMemb tsecGroupCreateMembSysadmin;
		CFLibDbKeyHash256 tsecGroupCreateMembSysadminID;
		ICFSecTSecGroup tsecGroupRead;
		CFLibDbKeyHash256 tsecGroupReadID;
		ICFSecTSecGrpMemb tsecGroupReadMembSysadmin;
		CFLibDbKeyHash256 tsecGroupReadMembSysadminID;
		ICFSecTSecGroup tsecGroupUpdate;
		CFLibDbKeyHash256 tsecGroupUpdateID;
		ICFSecTSecGrpMemb tsecGroupUpdateMembSysadmin;
		CFLibDbKeyHash256 tsecGroupUpdateMembSysadminID;
		ICFSecTSecGroup tsecGroupDelete;
		CFLibDbKeyHash256 tsecGroupDeleteID;
		ICFSecTSecGrpMemb tsecGroupDeleteMembSysadmin;
		CFLibDbKeyHash256 tsecGroupDeleteMembSysadminID;
		ICFSecTSecGroup tsecGroupRestore;
		CFLibDbKeyHash256 tsecGroupRestoreID;
		ICFSecTSecGrpMemb tsecGroupRestoreMembSysadmin;
		CFLibDbKeyHash256 tsecGroupRestoreMembSysadminID;
		ICFSecTSecGroup tsecGroupMutate;
		CFLibDbKeyHash256 tsecGroupMutateID;
		ICFSecTSecGrpMemb tsecGroupMutateMembSysadmin;
		CFLibDbKeyHash256 tsecGroupMutateMembSysadminID;
		
		secGroupCreate = ICFSecSchema.getBackingCFSec().getTableSecGroup().readDerivedByUNameIdx(auth, ICFSecSchema.getSysClusterId(), createPermName);
		if (secGroupCreate != null) {
			secGroupCreateID = secGroupCreate.getRequiredSecGroupId();
			secGroupCreateMembSysadmin = ICFSecSchema.getBackingCFSec().getTableSecGrpMemb().readDerivedByUUserIdx(auth, ICFSecSchema.getSysClusterId(), secGroupCreateID, ICFSecSchema.getSysAdminId());
			if (secGroupCreateMembSysadmin != null) {
				secGroupCreateMembSysadminID = secGroupCreateMembSysadmin.getRequiredSecGrpMembId();
			}
			else {
				secGroupCreateMembSysadminID = null;
			}
		}
		else {
			secGroupCreateID = null;
			secGroupCreateMembSysadmin = null;
			secGroupCreateMembSysadminID = null;
		}

		secGroupRead = ICFSecSchema.getBackingCFSec().getTableSecGroup().readDerivedByUNameIdx(auth, ICFSecSchema.getSysClusterId(), readPermName);
		if (secGroupRead != null) {
			secGroupReadID = secGroupRead.getRequiredSecGroupId();
			secGroupReadMembSysadmin = ICFSecSchema.getBackingCFSec().getTableSecGrpMemb().readDerivedByUUserIdx(auth, ICFSecSchema.getSysClusterId(), secGroupReadID, ICFSecSchema.getSysAdminId());
			if (secGroupReadMembSysadmin != null) {
				secGroupReadMembSysadminID = secGroupReadMembSysadmin.getRequiredSecGrpMembId();
			}
			else {
				secGroupReadMembSysadminID = null;
			}
		}
		else {
			secGroupReadID = null;
			secGroupReadMembSysadmin = null;
			secGroupReadMembSysadminID = null;
		}

		secGroupUpdate = ICFSecSchema.getBackingCFSec().getTableSecGroup().readDerivedByUNameIdx(auth, ICFSecSchema.getSysClusterId(), updatePermName);
		if (secGroupUpdate != null) {
			secGroupUpdateID = secGroupUpdate.getRequiredSecGroupId();
			secGroupUpdateMembSysadmin = ICFSecSchema.getBackingCFSec().getTableSecGrpMemb().readDerivedByUUserIdx(auth, ICFSecSchema.getSysClusterId(), secGroupUpdateID, ICFSecSchema.getSysAdminId());
			if (secGroupUpdateMembSysadmin != null) {
				secGroupUpdateMembSysadminID = secGroupUpdateMembSysadmin.getRequiredSecGrpMembId();
			}
			else {
				secGroupUpdateMembSysadminID = null;
			}
		}
		else {
			secGroupUpdateID = null;
			secGroupUpdateMembSysadmin = null;
			secGroupUpdateMembSysadminID = null;
		}

		secGroupDelete = ICFSecSchema.getBackingCFSec().getTableSecGroup().readDerivedByUNameIdx(auth, ICFSecSchema.getSysClusterId(), deletePermName);
		if (secGroupDelete != null) {
			secGroupDeleteID = secGroupDelete.getRequiredSecGroupId();
			secGroupDeleteMembSysadmin = ICFSecSchema.getBackingCFSec().getTableSecGrpMemb().readDerivedByUUserIdx(auth, ICFSecSchema.getSysClusterId(), secGroupDeleteID, ICFSecSchema.getSysAdminId());
			if (secGroupDeleteMembSysadmin != null) {
				secGroupDeleteMembSysadminID = secGroupDeleteMembSysadmin.getRequiredSecGrpMembId();
			}
			else {
				secGroupDeleteMembSysadminID = null;
			}
		}
		else {
			secGroupDeleteID = null;
			secGroupDeleteMembSysadmin = null;
			secGroupDeleteMembSysadminID = null;
		}

		if (hasHistory) {
			secGroupRestore = ICFSecSchema.getBackingCFSec().getTableSecGroup().readDerivedByUNameIdx(auth, ICFSecSchema.getSysClusterId(), restorePermName);
			if (secGroupRestore != null) {
				secGroupRestoreID = secGroupRestore.getRequiredSecGroupId();
				secGroupRestoreMembSysadmin = ICFSecSchema.getBackingCFSec().getTableSecGrpMemb().readDerivedByUUserIdx(auth, ICFSecSchema.getSysClusterId(), secGroupRestoreID, ICFSecSchema.getSysAdminId());
				if (secGroupRestoreMembSysadmin != null) {
					secGroupRestoreMembSysadminID = secGroupRestoreMembSysadmin.getRequiredSecGrpMembId();
				}
				else {
					secGroupRestoreMembSysadminID = null;
				}
			}
			else {
				secGroupRestoreID = null;
				secGroupRestoreMembSysadmin = null;
				secGroupRestoreMembSysadminID = null;
			}
		}
		else {
			secGroupRestore = null;
			secGroupRestoreID = null;
			secGroupRestoreMembSysadmin = null;
			secGroupRestoreMembSysadminID = null;
		}

		if (isMutable) {
			secGroupMutate = ICFSecSchema.getBackingCFSec().getTableSecGroup().readDerivedByUNameIdx(auth, ICFSecSchema.getSysClusterId(), mutatePermName);
			if (secGroupMutate != null) {
				secGroupMutateID = secGroupMutate.getRequiredSecGroupId();
				secGroupMutateMembSysadmin = ICFSecSchema.getBackingCFSec().getTableSecGrpMemb().readDerivedByUUserIdx(auth, ICFSecSchema.getSysClusterId(), secGroupMutateID, ICFSecSchema.getSysAdminId());
				if (secGroupMutateMembSysadmin != null) {
					secGroupMutateMembSysadminID = secGroupMutateMembSysadmin.getRequiredSecGrpMembId();
				}
				else {
					secGroupMutateMembSysadminID = null;
				}
			}
			else {
				secGroupMutateID = null;
				secGroupMutateMembSysadmin = null;
				secGroupMutateMembSysadminID = null;
			}
		}
		else {
			secGroupMutate = null;
			secGroupMutateID = null;
			secGroupMutateMembSysadmin = null;
			secGroupMutateMembSysadminID = null;
		}
		
		if (secGroupCreateID == null || secGroupCreateID.isNull()) {
			secGroupCreateID = new CFLibDbKeyHash256(0);
		}
		if (secGroupCreateMembSysadminID == null || secGroupCreateMembSysadminID.isNull()) {
			secGroupCreateMembSysadminID = new CFLibDbKeyHash256(0);
		}
		if (secGroupReadID == null || secGroupReadID.isNull()) {
			secGroupReadID = new CFLibDbKeyHash256(0);
		}
		if (secGroupReadMembSysadminID == null || secGroupReadMembSysadminID.isNull()) {
			secGroupReadMembSysadminID = new CFLibDbKeyHash256(0);
		}
		if (secGroupUpdateID == null || secGroupUpdateID.isNull()) {
			secGroupUpdateID = new CFLibDbKeyHash256(0);
		}
		if (secGroupUpdateMembSysadminID == null || secGroupUpdateMembSysadminID.isNull()) {
			secGroupUpdateMembSysadminID = new CFLibDbKeyHash256(0);
		}
		if (secGroupDeleteID == null || secGroupDeleteID.isNull()) {
			secGroupDeleteID = new CFLibDbKeyHash256(0);
		}
		if (secGroupDeleteMembSysadminID == null || secGroupDeleteMembSysadminID.isNull()) {
			secGroupDeleteMembSysadminID = new CFLibDbKeyHash256(0);
		}
		if (hasHistory) {
			if (secGroupRestoreID == null || secGroupRestoreID.isNull()) {
				secGroupRestoreID = new CFLibDbKeyHash256(0);
			}
			if (secGroupRestoreMembSysadminID == null || secGroupRestoreMembSysadminID.isNull()) {
				secGroupRestoreMembSysadminID = new CFLibDbKeyHash256(0);
			}
		}
		if (isMutable) {
			if (secGroupMutateID == null || secGroupMutateID.isNull()) {
				secGroupMutateID = new CFLibDbKeyHash256(0);
			}
			if (secGroupMutateMembSysadminID == null || secGroupMutateMembSysadminID.isNull()) {
				secGroupMutateMembSysadminID = new CFLibDbKeyHash256(0);
			}
		}

		if (secGroupCreate == null) {
			secGroupCreate = ICFSecSchema.getBackingCFSec().getFactorySecGroup().newRec();
			secGroupCreate.setRequiredRevision(1);
			secGroupCreate.setRequiredContainerCluster(ICFSecSchema.getSysClusterId());
			secGroupCreate.setRequiredName(createPermName);
			secGroupCreate.setRequiredIsVisible(true);
			secGroupCreate.setRequiredSecGroupId(secGroupCreateID);
			secGroupCreate = ICFSecSchema.getBackingCFSec().getTableSecGroup().createSecGroup(auth, secGroupCreate);
			secGroupCreateID = secGroupCreate.getRequiredSecGroupId();
		}

		if (secGroupCreateMembSysadmin == null) {
			secGroupCreateMembSysadmin = ICFSecSchema.getBackingCFSec().getFactorySecGrpMemb().newRec();
			secGroupCreateMembSysadmin.setRequiredRevision(1);
			secGroupCreateMembSysadmin.setRequiredOwnerCluster(ICFSecSchema.getSysClusterId());
			secGroupCreateMembSysadmin.setRequiredContainerGroup(secGroupCreateID);
			secGroupCreateMembSysadmin.setRequiredParentUser(ICFSecSchema.getSysAdminId());
			secGroupCreateMembSysadmin.setRequiredSecGrpMembId(secGroupCreateMembSysadminID);
			secGroupCreateMembSysadmin = ICFSecSchema.getBackingCFSec().getTableSecGrpMemb().createSecGrpMemb(auth, secGroupCreateMembSysadmin);
			secGroupCreateMembSysadminID = secGroupCreateMembSysadmin.getRequiredSecGrpMembId();
		}

		if (secGroupRead == null) {
			secGroupRead = ICFSecSchema.getBackingCFSec().getFactorySecGroup().newRec();
			secGroupRead.setRequiredRevision(1);
			secGroupRead.setRequiredContainerCluster(ICFSecSchema.getSysClusterId());
			secGroupRead.setRequiredName(readPermName);
			secGroupRead.setRequiredIsVisible(true);
			secGroupRead.setRequiredSecGroupId(secGroupReadID);
			secGroupRead = ICFSecSchema.getBackingCFSec().getTableSecGroup().createSecGroup(auth, secGroupRead);
			secGroupReadID = secGroupRead.getRequiredSecGroupId();
		}

		if (secGroupReadMembSysadmin == null) {
			secGroupReadMembSysadmin = ICFSecSchema.getBackingCFSec().getFactorySecGrpMemb().newRec();
			secGroupReadMembSysadmin.setRequiredRevision(1);
			secGroupReadMembSysadmin.setRequiredOwnerCluster(ICFSecSchema.getSysClusterId());
			secGroupReadMembSysadmin.setRequiredContainerGroup(secGroupReadID);
			secGroupReadMembSysadmin.setRequiredParentUser(ICFSecSchema.getSysAdminId());
			secGroupReadMembSysadmin.setRequiredSecGrpMembId(secGroupReadMembSysadminID);
			secGroupReadMembSysadmin = ICFSecSchema.getBackingCFSec().getTableSecGrpMemb().createSecGrpMemb(auth, secGroupReadMembSysadmin);
			secGroupReadMembSysadminID = secGroupReadMembSysadmin.getRequiredSecGrpMembId();
		}

		if (secGroupUpdate == null) {
			secGroupUpdate = ICFSecSchema.getBackingCFSec().getFactorySecGroup().newRec();
			secGroupUpdate.setRequiredRevision(1);
			secGroupUpdate.setRequiredContainerCluster(ICFSecSchema.getSysClusterId());
			secGroupUpdate.setRequiredName(updatePermName);
			secGroupUpdate.setRequiredIsVisible(true);
			secGroupUpdate.setRequiredSecGroupId(secGroupUpdateID);
			secGroupUpdate = ICFSecSchema.getBackingCFSec().getTableSecGroup().createSecGroup(auth, secGroupUpdate);
			secGroupUpdateID = secGroupUpdate.getRequiredSecGroupId();
		}

		if (secGroupUpdateMembSysadmin == null) {
			secGroupUpdateMembSysadmin = ICFSecSchema.getBackingCFSec().getFactorySecGrpMemb().newRec();
			secGroupUpdateMembSysadmin.setRequiredRevision(1);
			secGroupUpdateMembSysadmin.setRequiredOwnerCluster(ICFSecSchema.getSysClusterId());
			secGroupUpdateMembSysadmin.setRequiredContainerGroup(secGroupUpdateID);
			secGroupUpdateMembSysadmin.setRequiredParentUser(ICFSecSchema.getSysAdminId());
			secGroupUpdateMembSysadmin.setRequiredSecGrpMembId(secGroupUpdateMembSysadminID);
			secGroupUpdateMembSysadmin = ICFSecSchema.getBackingCFSec().getTableSecGrpMemb().createSecGrpMemb(auth, secGroupUpdateMembSysadmin);
			secGroupUpdateMembSysadminID = secGroupUpdateMembSysadmin.getRequiredSecGrpMembId();
		}

		if (secGroupDelete == null) {
			secGroupDelete = ICFSecSchema.getBackingCFSec().getFactorySecGroup().newRec();
			secGroupDelete.setRequiredRevision(1);
			secGroupDelete.setRequiredContainerCluster(ICFSecSchema.getSysClusterId());
			secGroupDelete.setRequiredName(deletePermName);
			secGroupDelete.setRequiredIsVisible(true);
			secGroupDelete.setRequiredSecGroupId(secGroupDeleteID);
			secGroupDelete = ICFSecSchema.getBackingCFSec().getTableSecGroup().createSecGroup(auth, secGroupDelete);
			secGroupDeleteID = secGroupDelete.getRequiredSecGroupId();
		}

		if (secGroupDeleteMembSysadmin == null) {
			secGroupDeleteMembSysadmin = ICFSecSchema.getBackingCFSec().getFactorySecGrpMemb().newRec();
			secGroupDeleteMembSysadmin.setRequiredRevision(1);
			secGroupDeleteMembSysadmin.setRequiredOwnerCluster(ICFSecSchema.getSysClusterId());
			secGroupDeleteMembSysadmin.setRequiredContainerGroup(secGroupDeleteID);
			secGroupDeleteMembSysadmin.setRequiredParentUser(ICFSecSchema.getSysAdminId());
			secGroupDeleteMembSysadmin.setRequiredSecGrpMembId(secGroupDeleteMembSysadminID);
			secGroupDeleteMembSysadmin = ICFSecSchema.getBackingCFSec().getTableSecGrpMemb().createSecGrpMemb(auth, secGroupDeleteMembSysadmin);
			secGroupDeleteMembSysadminID = secGroupDeleteMembSysadmin.getRequiredSecGrpMembId();
		}
		
		if (hasHistory) {
			if (secGroupRestore == null) {
				secGroupRestore = ICFSecSchema.getBackingCFSec().getFactorySecGroup().newRec();
				secGroupRestore.setRequiredRevision(1);
				secGroupRestore.setRequiredContainerCluster(ICFSecSchema.getSysClusterId());
				secGroupRestore.setRequiredName(restorePermName);
				secGroupRestore.setRequiredIsVisible(true);
				secGroupRestore.setRequiredSecGroupId(secGroupRestoreID);
				secGroupRestore = ICFSecSchema.getBackingCFSec().getTableSecGroup().createSecGroup(auth, secGroupRestore);
				secGroupRestoreID = secGroupRestore.getRequiredSecGroupId();
			}

			if (secGroupRestoreMembSysadmin == null) {
				secGroupRestoreMembSysadmin = ICFSecSchema.getBackingCFSec().getFactorySecGrpMemb().newRec();
				secGroupRestoreMembSysadmin.setRequiredRevision(1);
				secGroupRestoreMembSysadmin.setRequiredOwnerCluster(ICFSecSchema.getSysClusterId());
				secGroupRestoreMembSysadmin.setRequiredContainerGroup(secGroupRestoreID);
				secGroupRestoreMembSysadmin.setRequiredParentUser(ICFSecSchema.getSysAdminId());
				secGroupRestoreMembSysadmin.setRequiredSecGrpMembId(secGroupRestoreMembSysadminID);
				secGroupRestoreMembSysadmin = ICFSecSchema.getBackingCFSec().getTableSecGrpMemb().createSecGrpMemb(auth, secGroupRestoreMembSysadmin);
				secGroupRestoreMembSysadminID = secGroupRestoreMembSysadmin.getRequiredSecGrpMembId();
			}
		}
		
		if (isMutable) {
			if (secGroupMutate == null) {
				secGroupMutate = ICFSecSchema.getBackingCFSec().getFactorySecGroup().newRec();
				secGroupMutate.setRequiredRevision(1);
				secGroupMutate.setRequiredContainerCluster(ICFSecSchema.getSysClusterId());
				secGroupMutate.setRequiredName(mutatePermName);
				secGroupMutate.setRequiredIsVisible(true);
				secGroupMutate.setRequiredSecGroupId(secGroupMutateID);
				secGroupMutate = ICFSecSchema.getBackingCFSec().getTableSecGroup().createSecGroup(auth, secGroupMutate);
				secGroupMutateID = secGroupMutate.getRequiredSecGroupId();
			}

			if (secGroupMutateMembSysadmin == null) {
				secGroupMutateMembSysadmin = ICFSecSchema.getBackingCFSec().getFactorySecGrpMemb().newRec();
				secGroupMutateMembSysadmin.setRequiredRevision(1);
				secGroupMutateMembSysadmin.setRequiredOwnerCluster(ICFSecSchema.getSysClusterId());
				secGroupMutateMembSysadmin.setRequiredContainerGroup(secGroupMutateID);
				secGroupMutateMembSysadmin.setRequiredParentUser(ICFSecSchema.getSysAdminId());
				secGroupMutateMembSysadmin.setRequiredSecGrpMembId(secGroupMutateMembSysadminID);
				secGroupMutateMembSysadmin = ICFSecSchema.getBackingCFSec().getTableSecGrpMemb().createSecGrpMemb(auth, secGroupMutateMembSysadmin);
				secGroupMutateMembSysadminID = secGroupMutateMembSysadmin.getRequiredSecGrpMembId();
			}
		}

		if (isTenantScoped) {
		
			tsecGroupCreate = ICFSecSchema.getBackingCFSec().getTableTSecGroup().readDerivedByUNameIdx(auth, ICFSecSchema.getSysClusterId(), createPermName);
			if (tsecGroupCreate != null) {
				tsecGroupCreateID = tsecGroupCreate.getRequiredTSecGroupId();
				tsecGroupCreateMembSysadmin = ICFSecSchema.getBackingCFSec().getTableTSecGrpMemb().readDerivedByUUserIdx(auth, ICFSecSchema.getSysClusterId(), tsecGroupCreateID, ICFSecSchema.getSysAdminId());
				if (tsecGroupCreateMembSysadmin != null) {
					tsecGroupCreateMembSysadminID = tsecGroupCreateMembSysadmin.getRequiredTSecGrpMembId();
				}
				else {
					tsecGroupCreateMembSysadminID = null;
				}
			}
			else {
				tsecGroupCreateID = null;
				tsecGroupCreateMembSysadmin = null;
				tsecGroupCreateMembSysadminID = null;
			}

			tsecGroupRead = ICFSecSchema.getBackingCFSec().getTableTSecGroup().readDerivedByUNameIdx(auth, ICFSecSchema.getSysClusterId(), readPermName);
			if (tsecGroupRead != null) {
				tsecGroupReadID = tsecGroupRead.getRequiredSecGroupId();
				tsecGroupReadMembSysadmin = ICFSecSchema.getBackingCFSec().getTableTSecGrpMemb().readDerivedByUUserIdx(auth, ICFSecSchema.getSysClusterId(), tsecGroupReadID, ICFSecSchema.getSysAdminId());
				if (tsecGroupReadMembSysadmin != null) {
					tsecGroupReadMembSysadminID = tsecGroupReadMembSysadmin.getRequiredTSecGrpMembId();
				}
				else {
					tsecGroupReadMembSysadminID = null;
				}
			}
			else {
				tsecGroupReadID = null;
				tsecGroupReadMembSysadmin = null;
				tsecGroupReadMembSysadminID = null;
			}

			tsecGroupUpdate = ICFSecSchema.getBackingCFSec().getTableTSecGroup().readDerivedByUNameIdx(auth, ICFSecSchema.getSysClusterId(), updatePermName);
			if (tsecGroupUpdate != null) {
				tsecGroupUpdateID = tsecGroupUpdate.getRequiredTSecGroupId();
				tsecGroupUpdateMembSysadmin = ICFSecSchema.getBackingCFSec().getTableTSecGrpMemb().readDerivedByUUserIdx(auth, ICFSecSchema.getSysClusterId(), tsecGroupUpdateID, ICFSecSchema.getSysAdminId());
				if (tsecGroupUpdateMembSysadmin != null) {
					tsecGroupUpdateMembSysadminID = tsecGroupUpdateMembSysadmin.getRequiredTSecGrpMembId();
				}
				else {
					tsecGroupUpdateMembSysadminID = null;
				}
			}
			else {
				tsecGroupUpdateID = null;
				tsecGroupUpdateMembSysadmin = null;
				tsecGroupUpdateMembSysadminID = null;
			}

			tsecGroupDelete = ICFSecSchema.getBackingCFSec().getTableTSecGroup().readDerivedByUNameIdx(auth, ICFSecSchema.getSysClusterId(), deletePermName);
			if (tsecGroupDelete != null) {
				tsecGroupDeleteID = tsecGroupDelete.getRequiredTSecGroupId();
				tsecGroupDeleteMembSysadmin = ICFSecSchema.getBackingCFSec().getTableTSecGrpMemb().readDerivedByUUserIdx(auth, ICFSecSchema.getSysClusterId(), tsecGroupDeleteID, ICFSecSchema.getSysAdminId());
				if (tsecGroupDeleteMembSysadmin != null) {
					tsecGroupDeleteMembSysadminID = tsecGroupDeleteMembSysadmin.getRequiredTSecGrpMembId();
				}
				else {
					tsecGroupDeleteMembSysadminID = null;
				}
			}
			else {
				tsecGroupDeleteID = null;
				tsecGroupDeleteMembSysadmin = null;
				tsecGroupDeleteMembSysadminID = null;
			}

			if (hasHistory) {
				tsecGroupRestore = ICFSecSchema.getBackingCFSec().getTableTSecGroup().readDerivedByUNameIdx(auth, ICFSecSchema.getSysClusterId(), restorePermName);
				if (tsecGroupRestore != null) {
					tsecGroupRestoreID = tsecGroupRestore.getRequiredTSecGroupId();
					tsecGroupRestoreMembSysadmin = ICFSecSchema.getBackingCFSec().getTableTSecGrpMemb().readDerivedByUUserIdx(auth, ICFSecSchema.getSysClusterId(), tsecGroupRestoreID, ICFSecSchema.getSysAdminId());
					if (tsecGroupRestoreMembSysadmin != null) {
						tsecGroupRestoreMembSysadminID = tsecGroupRestoreMembSysadmin.getRequiredTSecGrpMembId();
					}
					else {
						tsecGroupRestoreMembSysadminID = null;
					}
				}
				else {
					tsecGroupRestoreID = null;
					tsecGroupRestoreMembSysadmin = null;
					tsecGroupRestoreMembSysadminID = null;
				}
			}
			else {
				tsecGroupRestore = null;
				tsecGroupRestoreID = null;
				tsecGroupRestoreMembSysadmin = null;
				tsecGroupRestoreMembSysadminID = null;
			}

			if (isMutable) {
				tsecGroupMutate = ICFSecSchema.getBackingCFSec().getTableTSecGroup().readDerivedByUNameIdx(auth, ICFSecSchema.getSysClusterId(), mutatePermName);
				if (tsecGroupMutate != null) {
					tsecGroupMutateID = tsecGroupMutate.getRequiredTSecGroupId();
					tsecGroupMutateMembSysadmin = ICFSecSchema.getBackingCFSec().getTableTSecGrpMemb().readDerivedByUUserIdx(auth, ICFSecSchema.getSysClusterId(), tsecGroupMutateID, ICFSecSchema.getSysAdminId());
					if (tsecGroupMutateMembSysadmin != null) {
						tsecGroupMutateMembSysadminID = tsecGroupMutateMembSysadmin.getRequiredTSecGrpMembId();
					}
					else {
						tsecGroupMutateMembSysadminID = null;
					}
				}
				else {
					tsecGroupMutateID = null;
					tsecGroupMutateMembSysadmin = null;
					tsecGroupMutateMembSysadminID = null;
				}
			}
			else {
				tsecGroupMutate = null;
				tsecGroupMutateID = null;
				tsecGroupMutateMembSysadmin = null;
				tsecGroupMutateMembSysadminID = null;
			}

			if (tsecGroupCreateID == null || tsecGroupCreateID.isNull()) {
				tsecGroupCreateID = new CFLibDbKeyHash256(0);
			}
			if (tsecGroupCreateMembSysadminID == null || tsecGroupCreateMembSysadminID.isNull()) {
				tsecGroupCreateMembSysadminID = new CFLibDbKeyHash256(0);
			}
			if (tsecGroupReadID == null || tsecGroupReadID.isNull()) {
				tsecGroupReadID = new CFLibDbKeyHash256(0);
			}
			if (tsecGroupReadMembSysadminID == null || tsecGroupReadMembSysadminID.isNull()) {
				tsecGroupReadMembSysadminID = new CFLibDbKeyHash256(0);
			}
			if (tsecGroupUpdateID == null || tsecGroupUpdateID.isNull()) {
				tsecGroupUpdateID = new CFLibDbKeyHash256(0);
			}
			if (tsecGroupUpdateMembSysadminID == null || tsecGroupUpdateMembSysadminID.isNull()) {
				tsecGroupUpdateMembSysadminID = new CFLibDbKeyHash256(0);
			}
			if (tsecGroupDeleteID == null || tsecGroupDeleteID.isNull()) {
				tsecGroupDeleteID = new CFLibDbKeyHash256(0);
			}
			if (tsecGroupDeleteMembSysadminID == null || tsecGroupDeleteMembSysadminID.isNull()) {
				tsecGroupDeleteMembSysadminID = new CFLibDbKeyHash256(0);
			}
			if (hasHistory) {
				if (tsecGroupRestoreID == null || tsecGroupRestoreID.isNull()) {
					tsecGroupRestoreID = new CFLibDbKeyHash256(0);
				}
				if (tsecGroupRestoreMembSysadminID == null || tsecGroupRestoreMembSysadminID.isNull()) {
					tsecGroupRestoreMembSysadminID = new CFLibDbKeyHash256(0);
				}
			}
			if (isMutable) {
				if (tsecGroupMutateID == null || tsecGroupMutateID.isNull()) {
					tsecGroupMutateID = new CFLibDbKeyHash256(0);
				}
				if (tsecGroupMutateMembSysadminID == null || tsecGroupMutateMembSysadminID.isNull()) {
					tsecGroupMutateMembSysadminID = new CFLibDbKeyHash256(0);
				}
			}

			if (tsecGroupCreate == null) {
				tsecGroupCreate = ICFSecSchema.getBackingCFSec().getFactoryTSecGroup().newRec();
				tsecGroupCreate.setRequiredRevision(1);
				tsecGroupCreate.setRequiredContainerCluster(ICFSecSchema.getSysClusterId());
				tsecGroupCreate.setRequiredName(createPermName);
				tsecGroupCreate.setRequiredIsVisible(true);
				tsecGroupCreate.setRequiredTSecGroupId(tsecGroupCreateID);
				tsecGroupCreate = ICFSecSchema.getBackingCFSec().getTableTSecGroup().createTSecGroup(auth, tsecGroupCreate);
				tsecGroupCreateID = tsecGroupCreate.getRequiredTSecGroupId();
			}

			if (tsecGroupCreateMembSysadmin == null) {
				tsecGroupCreateMembSysadmin = ICFSecSchema.getBackingCFSec().getFactoryTSecGrpMemb().newRec();
				tsecGroupCreateMembSysadmin.setRequiredRevision(1);
				tsecGroupCreateMembSysadmin.setRequiredOwnerCluster(ICFSecSchema.getSysClusterId());
				tsecGroupCreateMembSysadmin.setRequiredContainerGroup(tsecGroupCreateID);
				tsecGroupCreateMembSysadmin.setRequiredParentUser(ICFSecSchema.getSysAdminId());
				tsecGroupCreateMembSysadmin.setRequiredTSecGrpMembId(tsecGroupCreateMembSysadminID);
				tsecGroupCreateMembSysadmin = ICFSecSchema.getBackingCFSec().getTableTSecGrpMemb().createTSecGrpMemb(auth, tsecGroupCreateMembSysadmin);
				tsecGroupCreateMembSysadminID = tsecGroupCreateMembSysadmin.getRequiredTSecGrpMembId();
			}

			if (tsecGroupRead == null) {
				tsecGroupRead = ICFSecSchema.getBackingCFSec().getFactoryTSecGroup().newRec();
				tsecGroupRead.setRequiredRevision(1);
				tsecGroupRead.setRequiredContainerCluster(ICFSecSchema.getSysClusterId());
				tsecGroupRead.setRequiredName(readPermName);
				tsecGroupRead.setRequiredIsVisible(true);
				tsecGroupRead.setRequiredTSecGroupId(tsecGroupReadID);
				tsecGroupRead = ICFSecSchema.getBackingCFSec().getTableTSecGroup().createTSecGroup(auth, tsecGroupRead);
				tsecGroupReadID = tsecGroupRead.getRequiredTSecGroupId();
			}

			if (tsecGroupReadMembSysadmin == null) {
				tsecGroupReadMembSysadmin = ICFSecSchema.getBackingCFSec().getFactoryTSecGrpMemb().newRec();
				tsecGroupReadMembSysadmin.setRequiredRevision(1);
				tsecGroupReadMembSysadmin.setRequiredOwnerCluster(ICFSecSchema.getSysClusterId());
				tsecGroupReadMembSysadmin.setRequiredContainerGroup(tsecGroupReadID);
				tsecGroupReadMembSysadmin.setRequiredParentUser(ICFSecSchema.getSysAdminId());
				tsecGroupReadMembSysadmin.setRequiredTSecGrpMembId(tsecGroupReadMembSysadminID);
				tsecGroupReadMembSysadmin = ICFSecSchema.getBackingCFSec().getTableTSecGrpMemb().createTSecGrpMemb(auth, tsecGroupReadMembSysadmin);
				tsecGroupReadMembSysadminID = tsecGroupReadMembSysadmin.getRequiredTSecGrpMembId();
			}

			if (tsecGroupUpdate == null) {
				tsecGroupUpdate = ICFSecSchema.getBackingCFSec().getFactoryTSecGroup().newRec();
				tsecGroupUpdate.setRequiredRevision(1);
				tsecGroupUpdate.setRequiredContainerCluster(ICFSecSchema.getSysClusterId());
				tsecGroupUpdate.setRequiredName(updatePermName);
				tsecGroupUpdate.setRequiredIsVisible(true);
				tsecGroupUpdate.setRequiredTSecGroupId(tsecGroupUpdateID);
				tsecGroupUpdate = ICFSecSchema.getBackingCFSec().getTableTSecGroup().createTSecGroup(auth, tsecGroupUpdate);
				tsecGroupUpdateID = tsecGroupUpdate.getRequiredTSecGroupId();
			}

			if (tsecGroupUpdateMembSysadmin == null) {
				tsecGroupUpdateMembSysadmin = ICFSecSchema.getBackingCFSec().getFactoryTSecGrpMemb().newRec();
				tsecGroupUpdateMembSysadmin.setRequiredRevision(1);
				tsecGroupUpdateMembSysadmin.setRequiredOwnerCluster(ICFSecSchema.getSysClusterId());
				tsecGroupUpdateMembSysadmin.setRequiredContainerGroup(tsecGroupUpdateID);
				tsecGroupUpdateMembSysadmin.setRequiredParentUser(ICFSecSchema.getSysAdminId());
				tsecGroupUpdateMembSysadmin.setRequiredTSecGrpMembId(tsecGroupUpdateMembSysadminID);
				tsecGroupUpdateMembSysadmin = ICFSecSchema.getBackingCFSec().getTableTSecGrpMemb().createTSecGrpMemb(auth, tsecGroupUpdateMembSysadmin);
				tsecGroupUpdateMembSysadminID = tsecGroupUpdateMembSysadmin.getRequiredTSecGrpMembId();
			}

			if (tsecGroupDelete == null) {
				tsecGroupDelete = ICFSecSchema.getBackingCFSec().getFactoryTSecGroup().newRec();
				tsecGroupDelete.setRequiredRevision(1);
				tsecGroupDelete.setRequiredContainerCluster(ICFSecSchema.getSysClusterId());
				tsecGroupDelete.setRequiredName(deletePermName);
				tsecGroupDelete.setRequiredIsVisible(true);
				tsecGroupDelete.setRequiredTSecGroupId(tsecGroupDeleteID);
				tsecGroupDelete = ICFSecSchema.getBackingCFSec().getTableTSecGroup().createTSecGroup(auth, tsecGroupDelete);
				tsecGroupDeleteID = tsecGroupDelete.getRequiredTSecGroupId();
			}

			if (tsecGroupDeleteMembSysadmin == null) {
				tsecGroupDeleteMembSysadmin = ICFSecSchema.getBackingCFSec().getFactoryTSecGrpMemb().newRec();
				tsecGroupDeleteMembSysadmin.setRequiredRevision(1);
				tsecGroupDeleteMembSysadmin.setRequiredOwnerCluster(ICFSecSchema.getSysClusterId());
				tsecGroupDeleteMembSysadmin.setRequiredContainerGroup(tsecGroupDeleteID);
				tsecGroupDeleteMembSysadmin.setRequiredParentUser(ICFSecSchema.getSysAdminId());
				tsecGroupDeleteMembSysadmin.setRequiredTSecGrpMembId(tsecGroupDeleteMembSysadminID);
				tsecGroupDeleteMembSysadmin = ICFSecSchema.getBackingCFSec().getTableTSecGrpMemb().createTSecGrpMemb(auth, tsecGroupDeleteMembSysadmin);
				tsecGroupDeleteMembSysadminID = tsecGroupDeleteMembSysadmin.getRequiredTSecGrpMembId();
			}

			if (hasHistory) {
				if (tsecGroupRestore == null) {
					tsecGroupRestore = ICFSecSchema.getBackingCFSec().getFactoryTSecGroup().newRec();
					tsecGroupRestore.setRequiredRevision(1);
					tsecGroupRestore.setRequiredContainerCluster(ICFSecSchema.getSysClusterId());
					tsecGroupRestore.setRequiredName(restorePermName);
					tsecGroupRestore.setRequiredIsVisible(true);
					tsecGroupRestore.setRequiredTSecGroupId(tsecGroupRestoreID);
					tsecGroupRestore = ICFSecSchema.getBackingCFSec().getTableTSecGroup().createTSecGroup(auth, tsecGroupRestore);
					tsecGroupRestoreID = tsecGroupRestore.getRequiredTSecGroupId();
				}

				if (tsecGroupRestoreMembSysadmin == null) {
					tsecGroupRestoreMembSysadmin = ICFSecSchema.getBackingCFSec().getFactoryTSecGrpMemb().newRec();
					tsecGroupRestoreMembSysadmin.setRequiredRevision(1);
					tsecGroupRestoreMembSysadmin.setRequiredOwnerCluster(ICFSecSchema.getSysClusterId());
					tsecGroupRestoreMembSysadmin.setRequiredContainerGroup(tsecGroupRestoreID);
					tsecGroupRestoreMembSysadmin.setRequiredParentUser(ICFSecSchema.getSysAdminId());
					tsecGroupRestoreMembSysadmin.setRequiredTSecGrpMembId(tsecGroupRestoreMembSysadminID);
					tsecGroupRestoreMembSysadmin = ICFSecSchema.getBackingCFSec().getTableTSecGrpMemb().createTSecGrpMemb(auth, tsecGroupRestoreMembSysadmin);
					tsecGroupRestoreMembSysadminID = tsecGroupRestoreMembSysadmin.getRequiredTSecGrpMembId();
				}
			}

			if (isMutable) {
				if (tsecGroupMutate == null) {
					tsecGroupMutate = ICFSecSchema.getBackingCFSec().getFactoryTSecGroup().newRec();
					tsecGroupMutate.setRequiredRevision(1);
					tsecGroupMutate.setRequiredContainerCluster(ICFSecSchema.getSysClusterId());
					tsecGroupMutate.setRequiredName(mutatePermName);
					tsecGroupMutate.setRequiredIsVisible(true);
					tsecGroupMutate.setRequiredTSecGroupId(tsecGroupMutateID);
					tsecGroupMutate = ICFSecSchema.getBackingCFSec().getTableTSecGroup().createTSecGroup(auth, tsecGroupMutate);
					tsecGroupMutateID = tsecGroupMutate.getRequiredTSecGroupId();
				}

				if (tsecGroupMutateMembSysadmin == null) {
					tsecGroupMutateMembSysadmin = ICFSecSchema.getBackingCFSec().getFactoryTSecGrpMemb().newRec();
					tsecGroupMutateMembSysadmin.setRequiredRevision(1);
					tsecGroupMutateMembSysadmin.setRequiredOwnerCluster(ICFSecSchema.getSysClusterId());
					tsecGroupMutateMembSysadmin.setRequiredContainerGroup(tsecGroupMutateID);
					tsecGroupMutateMembSysadmin.setRequiredParentUser(ICFSecSchema.getSysAdminId());
					tsecGroupMutateMembSysadmin.setRequiredTSecGrpMembId(tsecGroupMutateMembSysadminID);
					tsecGroupMutateMembSysadmin = ICFSecSchema.getBackingCFSec().getTableTSecGrpMemb().createTSecGrpMemb(auth, tsecGroupMutateMembSysadmin);
					tsecGroupMutateMembSysadminID = tsecGroupMutateMembSysadmin.getRequiredTSecGrpMembId();
				}
			}
		}
**/
	}		


}
