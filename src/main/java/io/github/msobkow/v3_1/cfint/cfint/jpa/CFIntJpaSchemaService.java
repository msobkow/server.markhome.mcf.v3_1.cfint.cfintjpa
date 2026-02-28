// Description: Java 25 Spring JPA Service for CFInt

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
import java.net.InetAddress;
import java.time.*;
import java.util.*;
import jakarta.persistence.*;
import io.github.msobkow.v3_1.cflib.*;
import io.github.msobkow.v3_1.cflib.dbutil.*;
import io.github.msobkow.v3_1.cflib.xml.CFLibXmlUtil;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import io.github.msobkow.v3_1.cfsec.cfsec.*;
import io.github.msobkow.v3_1.cfint.cfint.*;

/**
 *	Services for schema CFInt defined in io.github.msobkow.v3_1.cfint.cfint.jpa
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
	public void bootstrapTableSecurity(ICFSecAuthorization auth, String tableName, boolean hasHistory, boolean isMutable) {
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
	}		


}
