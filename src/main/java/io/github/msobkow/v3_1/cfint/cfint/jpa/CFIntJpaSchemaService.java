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

	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = NoResultException.class, transactionManager = "cfint31TransactionManager")
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

		bootstrapTableLicenseSecurity(auth);
		bootstrapTableMajorVersionSecurity(auth);
		bootstrapTableMimeTypeSecurity(auth);
		bootstrapTableMinorVersionSecurity(auth);
		bootstrapTableSubProjectSecurity(auth);
		bootstrapTableTldSecurity(auth);
		bootstrapTableTopDomainSecurity(auth);
		bootstrapTableTopProjectSecurity(auth);
		bootstrapTableURLProtocolSecurity(auth);

		if (bootstrapSession != null && bootstrapSessionID != null && !bootstrapSessionID.isNull() && bootstrapSession.getOptionalFinish() == null) {
			bootstrapSession.setOptionalFinish(LocalDateTime.now());
			bootstrapSession = ICFSecSchema.getBackingCFSec().getTableSecSession().updateSecSession(auth, bootstrapSession);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = NoResultException.class, transactionManager = "cfint31TransactionManager")
	public void bootstrapTableLicenseSecurity(ICFSecAuthorization auth) {
		LocalDateTime now = LocalDateTime.now();

		ICFSecSecGroup secGroupCreateLicense;
		CFLibDbKeyHash256 secGroupCreateLicenseID;
		ICFSecSecGrpMemb secGroupCreateLicenseMembSysadmin;
		CFLibDbKeyHash256 secGroupCreateLicenseMembSysadminID;
		ICFSecSecGroup secGroupReadLicense;
		CFLibDbKeyHash256 secGroupReadLicenseID;
		ICFSecSecGrpMemb secGroupReadLicenseMembSysadmin;
		CFLibDbKeyHash256 secGroupReadLicenseMembSysadminID;
		ICFSecSecGroup secGroupUpdateLicense;
		CFLibDbKeyHash256 secGroupUpdateLicenseID;
		ICFSecSecGrpMemb secGroupUpdateLicenseMembSysadmin;
		CFLibDbKeyHash256 secGroupUpdateLicenseMembSysadminID;
		ICFSecSecGroup secGroupDeleteLicense;
		CFLibDbKeyHash256 secGroupDeleteLicenseID;
		ICFSecSecGrpMemb secGroupDeleteLicenseMembSysadmin;
		CFLibDbKeyHash256 secGroupDeleteLicenseMembSysadminID;

		secGroupCreateLicense = ICFSecSchema.getBackingCFSec().getTableSecGroup().readDerivedByUNameIdx(auth, ICFSecSchema.getSysClusterId(), "CreateLicense");
		if (secGroupCreateLicense != null) {
			secGroupCreateLicenseID = secGroupCreateLicense.getRequiredSecGroupId();
			secGroupCreateLicenseMembSysadmin = ICFSecSchema.getBackingCFSec().getTableSecGrpMemb().readDerivedByUUserIdx(auth, ICFSecSchema.getSysClusterId(), secGroupCreateLicenseID, ICFSecSchema.getSysAdminId());
			if (secGroupCreateLicenseMembSysadmin != null) {
				secGroupCreateLicenseMembSysadminID = secGroupCreateLicenseMembSysadmin.getRequiredSecGrpMembId();
			}
			else {
				secGroupCreateLicenseMembSysadminID = null;
			}
		}
		else {
			secGroupCreateLicenseID = null;
			secGroupCreateLicenseMembSysadmin = null;
			secGroupCreateLicenseMembSysadminID = null;
		}

		secGroupReadLicense = ICFSecSchema.getBackingCFSec().getTableSecGroup().readDerivedByUNameIdx(auth, ICFSecSchema.getSysClusterId(), "ReadLicense");
		if (secGroupReadLicense != null) {
			secGroupReadLicenseID = secGroupReadLicense.getRequiredSecGroupId();
			secGroupReadLicenseMembSysadmin = ICFSecSchema.getBackingCFSec().getTableSecGrpMemb().readDerivedByUUserIdx(auth, ICFSecSchema.getSysClusterId(), secGroupReadLicenseID, ICFSecSchema.getSysAdminId());
			if (secGroupReadLicenseMembSysadmin != null) {
				secGroupReadLicenseMembSysadminID = secGroupReadLicenseMembSysadmin.getRequiredSecGrpMembId();
			}
			else {
				secGroupReadLicenseMembSysadminID = null;
			}
		}
		else {
			secGroupReadLicenseID = null;
			secGroupReadLicenseMembSysadmin = null;
			secGroupReadLicenseMembSysadminID = null;
		}

		secGroupUpdateLicense = ICFSecSchema.getBackingCFSec().getTableSecGroup().readDerivedByUNameIdx(auth, ICFSecSchema.getSysClusterId(), "UpdateLicense");
		if (secGroupUpdateLicense != null) {
			secGroupUpdateLicenseID = secGroupUpdateLicense.getRequiredSecGroupId();
			secGroupUpdateLicenseMembSysadmin = ICFSecSchema.getBackingCFSec().getTableSecGrpMemb().readDerivedByUUserIdx(auth, ICFSecSchema.getSysClusterId(), secGroupUpdateLicenseID, ICFSecSchema.getSysAdminId());
			if (secGroupUpdateLicenseMembSysadmin != null) {
				secGroupUpdateLicenseMembSysadminID = secGroupUpdateLicenseMembSysadmin.getRequiredSecGrpMembId();
			}
			else {
				secGroupUpdateLicenseMembSysadminID = null;
			}
		}
		else {
			secGroupUpdateLicenseID = null;
			secGroupUpdateLicenseMembSysadmin = null;
			secGroupUpdateLicenseMembSysadminID = null;
		}

		secGroupDeleteLicense = ICFSecSchema.getBackingCFSec().getTableSecGroup().readDerivedByUNameIdx(auth, ICFSecSchema.getSysClusterId(), "DeleteLicense");
		if (secGroupDeleteLicense != null) {
			secGroupDeleteLicenseID = secGroupDeleteLicense.getRequiredSecGroupId();
			secGroupDeleteLicenseMembSysadmin = ICFSecSchema.getBackingCFSec().getTableSecGrpMemb().readDerivedByUUserIdx(auth, ICFSecSchema.getSysClusterId(), secGroupDeleteLicenseID, ICFSecSchema.getSysAdminId());
			if (secGroupDeleteLicenseMembSysadmin != null) {
				secGroupDeleteLicenseMembSysadminID = secGroupDeleteLicenseMembSysadmin.getRequiredSecGrpMembId();
			}
			else {
				secGroupDeleteLicenseMembSysadminID = null;
			}
		}
		else {
			secGroupDeleteLicenseID = null;
			secGroupDeleteLicenseMembSysadmin = null;
			secGroupDeleteLicenseMembSysadminID = null;
		}


		if (secGroupCreateLicenseID == null || secGroupCreateLicenseID.isNull()) {
			secGroupCreateLicenseID = new CFLibDbKeyHash256(0);
		}
		if (secGroupCreateLicenseMembSysadminID == null || secGroupCreateLicenseMembSysadminID.isNull()) {
			secGroupCreateLicenseMembSysadminID = new CFLibDbKeyHash256(0);
		}
		if (secGroupReadLicenseID == null || secGroupReadLicenseID.isNull()) {
			secGroupReadLicenseID = new CFLibDbKeyHash256(0);
		}
		if (secGroupReadLicenseMembSysadminID == null || secGroupReadLicenseMembSysadminID.isNull()) {
			secGroupReadLicenseMembSysadminID = new CFLibDbKeyHash256(0);
		}
		if (secGroupUpdateLicenseID == null || secGroupUpdateLicenseID.isNull()) {
			secGroupUpdateLicenseID = new CFLibDbKeyHash256(0);
		}
		if (secGroupUpdateLicenseMembSysadminID == null || secGroupUpdateLicenseMembSysadminID.isNull()) {
			secGroupUpdateLicenseMembSysadminID = new CFLibDbKeyHash256(0);
		}
		if (secGroupDeleteLicenseID == null || secGroupDeleteLicenseID.isNull()) {
			secGroupDeleteLicenseID = new CFLibDbKeyHash256(0);
		}
		if (secGroupDeleteLicenseMembSysadminID == null || secGroupDeleteLicenseMembSysadminID.isNull()) {
			secGroupDeleteLicenseMembSysadminID = new CFLibDbKeyHash256(0);
		}

		if (secGroupCreateLicense == null) {
			secGroupCreateLicense = ICFSecSchema.getBackingCFSec().getFactorySecGroup().newRec();
			secGroupCreateLicense.setRequiredRevision(1);
			secGroupCreateLicense.setRequiredContainerCluster(ICFSecSchema.getSysClusterId());
			secGroupCreateLicense.setRequiredName("CreateLicense");
			secGroupCreateLicense.setRequiredIsVisible(true);
			secGroupCreateLicense.setRequiredSecGroupId(secGroupCreateLicenseID);
			secGroupCreateLicense = ICFSecSchema.getBackingCFSec().getTableSecGroup().createSecGroup(auth, secGroupCreateLicense);
			secGroupCreateLicenseID = secGroupCreateLicense.getRequiredSecGroupId();
		}

		if (secGroupCreateLicenseMembSysadmin == null) {
			secGroupCreateLicenseMembSysadmin = ICFSecSchema.getBackingCFSec().getFactorySecGrpMemb().newRec();
			secGroupCreateLicenseMembSysadmin.setRequiredRevision(1);
			secGroupCreateLicenseMembSysadmin.setRequiredOwnerCluster(ICFSecSchema.getSysClusterId());
			secGroupCreateLicenseMembSysadmin.setRequiredContainerGroup(secGroupCreateLicenseID);
			secGroupCreateLicenseMembSysadmin.setRequiredParentUser(ICFSecSchema.getSysAdminId());
			secGroupCreateLicenseMembSysadmin.setRequiredSecGrpMembId(secGroupCreateLicenseMembSysadminID);
			secGroupCreateLicenseMembSysadmin = ICFSecSchema.getBackingCFSec().getTableSecGrpMemb().createSecGrpMemb(auth, secGroupCreateLicenseMembSysadmin);
			secGroupCreateLicenseMembSysadminID = secGroupCreateLicenseMembSysadmin.getRequiredSecGrpMembId();
		}

		if (secGroupReadLicense == null) {
			secGroupReadLicense = ICFSecSchema.getBackingCFSec().getFactorySecGroup().newRec();
			secGroupReadLicense.setRequiredRevision(1);
			secGroupReadLicense.setRequiredContainerCluster(ICFSecSchema.getSysClusterId());
			secGroupReadLicense.setRequiredName("ReadLicense");
			secGroupReadLicense.setRequiredIsVisible(true);
			secGroupReadLicense.setRequiredSecGroupId(secGroupReadLicenseID);
			secGroupReadLicense = ICFSecSchema.getBackingCFSec().getTableSecGroup().createSecGroup(auth, secGroupReadLicense);
			secGroupReadLicenseID = secGroupReadLicense.getRequiredSecGroupId();
		}

		if (secGroupReadLicenseMembSysadmin == null) {
			secGroupReadLicenseMembSysadmin = ICFSecSchema.getBackingCFSec().getFactorySecGrpMemb().newRec();
			secGroupReadLicenseMembSysadmin.setRequiredRevision(1);
			secGroupReadLicenseMembSysadmin.setRequiredOwnerCluster(ICFSecSchema.getSysClusterId());
			secGroupReadLicenseMembSysadmin.setRequiredContainerGroup(secGroupReadLicenseID);
			secGroupReadLicenseMembSysadmin.setRequiredParentUser(ICFSecSchema.getSysAdminId());
			secGroupReadLicenseMembSysadmin.setRequiredSecGrpMembId(secGroupReadLicenseMembSysadminID);
			secGroupReadLicenseMembSysadmin = ICFSecSchema.getBackingCFSec().getTableSecGrpMemb().createSecGrpMemb(auth, secGroupReadLicenseMembSysadmin);
			secGroupReadLicenseMembSysadminID = secGroupReadLicenseMembSysadmin.getRequiredSecGrpMembId();
		}

		if (secGroupUpdateLicense == null) {
			secGroupUpdateLicense = ICFSecSchema.getBackingCFSec().getFactorySecGroup().newRec();
			secGroupUpdateLicense.setRequiredRevision(1);
			secGroupUpdateLicense.setRequiredContainerCluster(ICFSecSchema.getSysClusterId());
			secGroupUpdateLicense.setRequiredName("UpdateLicense");
			secGroupUpdateLicense.setRequiredIsVisible(true);
			secGroupUpdateLicense.setRequiredSecGroupId(secGroupUpdateLicenseID);
			secGroupUpdateLicense = ICFSecSchema.getBackingCFSec().getTableSecGroup().createSecGroup(auth, secGroupUpdateLicense);
			secGroupUpdateLicenseID = secGroupUpdateLicense.getRequiredSecGroupId();
		}

		if (secGroupUpdateLicenseMembSysadmin == null) {
			secGroupUpdateLicenseMembSysadmin = ICFSecSchema.getBackingCFSec().getFactorySecGrpMemb().newRec();
			secGroupUpdateLicenseMembSysadmin.setRequiredRevision(1);
			secGroupUpdateLicenseMembSysadmin.setRequiredOwnerCluster(ICFSecSchema.getSysClusterId());
			secGroupUpdateLicenseMembSysadmin.setRequiredContainerGroup(secGroupUpdateLicenseID);
			secGroupUpdateLicenseMembSysadmin.setRequiredParentUser(ICFSecSchema.getSysAdminId());
			secGroupUpdateLicenseMembSysadmin.setRequiredSecGrpMembId(secGroupUpdateLicenseMembSysadminID);
			secGroupUpdateLicenseMembSysadmin = ICFSecSchema.getBackingCFSec().getTableSecGrpMemb().createSecGrpMemb(auth, secGroupUpdateLicenseMembSysadmin);
			secGroupUpdateLicenseMembSysadminID = secGroupUpdateLicenseMembSysadmin.getRequiredSecGrpMembId();
		}

		if (secGroupDeleteLicense == null) {
			secGroupDeleteLicense = ICFSecSchema.getBackingCFSec().getFactorySecGroup().newRec();
			secGroupDeleteLicense.setRequiredRevision(1);
			secGroupDeleteLicense.setRequiredContainerCluster(ICFSecSchema.getSysClusterId());
			secGroupDeleteLicense.setRequiredName("DeleteLicense");
			secGroupDeleteLicense.setRequiredIsVisible(true);
			secGroupDeleteLicense.setRequiredSecGroupId(secGroupDeleteLicenseID);
			secGroupDeleteLicense = ICFSecSchema.getBackingCFSec().getTableSecGroup().createSecGroup(auth, secGroupDeleteLicense);
			secGroupDeleteLicenseID = secGroupDeleteLicense.getRequiredSecGroupId();
		}

		if (secGroupDeleteLicenseMembSysadmin == null) {
			secGroupDeleteLicenseMembSysadmin = ICFSecSchema.getBackingCFSec().getFactorySecGrpMemb().newRec();
			secGroupDeleteLicenseMembSysadmin.setRequiredRevision(1);
			secGroupDeleteLicenseMembSysadmin.setRequiredOwnerCluster(ICFSecSchema.getSysClusterId());
			secGroupDeleteLicenseMembSysadmin.setRequiredContainerGroup(secGroupDeleteLicenseID);
			secGroupDeleteLicenseMembSysadmin.setRequiredParentUser(ICFSecSchema.getSysAdminId());
			secGroupDeleteLicenseMembSysadmin.setRequiredSecGrpMembId(secGroupDeleteLicenseMembSysadminID);
			secGroupDeleteLicenseMembSysadmin = ICFSecSchema.getBackingCFSec().getTableSecGrpMemb().createSecGrpMemb(auth, secGroupDeleteLicenseMembSysadmin);
			secGroupDeleteLicenseMembSysadminID = secGroupDeleteLicenseMembSysadmin.getRequiredSecGrpMembId();
		}

	}		

	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = NoResultException.class, transactionManager = "cfint31TransactionManager")
	public void bootstrapTableMajorVersionSecurity(ICFSecAuthorization auth) {
		LocalDateTime now = LocalDateTime.now();

		ICFSecSecGroup secGroupCreateMajorVersion;
		CFLibDbKeyHash256 secGroupCreateMajorVersionID;
		ICFSecSecGrpMemb secGroupCreateMajorVersionMembSysadmin;
		CFLibDbKeyHash256 secGroupCreateMajorVersionMembSysadminID;
		ICFSecSecGroup secGroupReadMajorVersion;
		CFLibDbKeyHash256 secGroupReadMajorVersionID;
		ICFSecSecGrpMemb secGroupReadMajorVersionMembSysadmin;
		CFLibDbKeyHash256 secGroupReadMajorVersionMembSysadminID;
		ICFSecSecGroup secGroupUpdateMajorVersion;
		CFLibDbKeyHash256 secGroupUpdateMajorVersionID;
		ICFSecSecGrpMemb secGroupUpdateMajorVersionMembSysadmin;
		CFLibDbKeyHash256 secGroupUpdateMajorVersionMembSysadminID;
		ICFSecSecGroup secGroupDeleteMajorVersion;
		CFLibDbKeyHash256 secGroupDeleteMajorVersionID;
		ICFSecSecGrpMemb secGroupDeleteMajorVersionMembSysadmin;
		CFLibDbKeyHash256 secGroupDeleteMajorVersionMembSysadminID;

		secGroupCreateMajorVersion = ICFSecSchema.getBackingCFSec().getTableSecGroup().readDerivedByUNameIdx(auth, ICFSecSchema.getSysClusterId(), "CreateMajorVersion");
		if (secGroupCreateMajorVersion != null) {
			secGroupCreateMajorVersionID = secGroupCreateMajorVersion.getRequiredSecGroupId();
			secGroupCreateMajorVersionMembSysadmin = ICFSecSchema.getBackingCFSec().getTableSecGrpMemb().readDerivedByUUserIdx(auth, ICFSecSchema.getSysClusterId(), secGroupCreateMajorVersionID, ICFSecSchema.getSysAdminId());
			if (secGroupCreateMajorVersionMembSysadmin != null) {
				secGroupCreateMajorVersionMembSysadminID = secGroupCreateMajorVersionMembSysadmin.getRequiredSecGrpMembId();
			}
			else {
				secGroupCreateMajorVersionMembSysadminID = null;
			}
		}
		else {
			secGroupCreateMajorVersionID = null;
			secGroupCreateMajorVersionMembSysadmin = null;
			secGroupCreateMajorVersionMembSysadminID = null;
		}

		secGroupReadMajorVersion = ICFSecSchema.getBackingCFSec().getTableSecGroup().readDerivedByUNameIdx(auth, ICFSecSchema.getSysClusterId(), "ReadMajorVersion");
		if (secGroupReadMajorVersion != null) {
			secGroupReadMajorVersionID = secGroupReadMajorVersion.getRequiredSecGroupId();
			secGroupReadMajorVersionMembSysadmin = ICFSecSchema.getBackingCFSec().getTableSecGrpMemb().readDerivedByUUserIdx(auth, ICFSecSchema.getSysClusterId(), secGroupReadMajorVersionID, ICFSecSchema.getSysAdminId());
			if (secGroupReadMajorVersionMembSysadmin != null) {
				secGroupReadMajorVersionMembSysadminID = secGroupReadMajorVersionMembSysadmin.getRequiredSecGrpMembId();
			}
			else {
				secGroupReadMajorVersionMembSysadminID = null;
			}
		}
		else {
			secGroupReadMajorVersionID = null;
			secGroupReadMajorVersionMembSysadmin = null;
			secGroupReadMajorVersionMembSysadminID = null;
		}

		secGroupUpdateMajorVersion = ICFSecSchema.getBackingCFSec().getTableSecGroup().readDerivedByUNameIdx(auth, ICFSecSchema.getSysClusterId(), "UpdateMajorVersion");
		if (secGroupUpdateMajorVersion != null) {
			secGroupUpdateMajorVersionID = secGroupUpdateMajorVersion.getRequiredSecGroupId();
			secGroupUpdateMajorVersionMembSysadmin = ICFSecSchema.getBackingCFSec().getTableSecGrpMemb().readDerivedByUUserIdx(auth, ICFSecSchema.getSysClusterId(), secGroupUpdateMajorVersionID, ICFSecSchema.getSysAdminId());
			if (secGroupUpdateMajorVersionMembSysadmin != null) {
				secGroupUpdateMajorVersionMembSysadminID = secGroupUpdateMajorVersionMembSysadmin.getRequiredSecGrpMembId();
			}
			else {
				secGroupUpdateMajorVersionMembSysadminID = null;
			}
		}
		else {
			secGroupUpdateMajorVersionID = null;
			secGroupUpdateMajorVersionMembSysadmin = null;
			secGroupUpdateMajorVersionMembSysadminID = null;
		}

		secGroupDeleteMajorVersion = ICFSecSchema.getBackingCFSec().getTableSecGroup().readDerivedByUNameIdx(auth, ICFSecSchema.getSysClusterId(), "DeleteMajorVersion");
		if (secGroupDeleteMajorVersion != null) {
			secGroupDeleteMajorVersionID = secGroupDeleteMajorVersion.getRequiredSecGroupId();
			secGroupDeleteMajorVersionMembSysadmin = ICFSecSchema.getBackingCFSec().getTableSecGrpMemb().readDerivedByUUserIdx(auth, ICFSecSchema.getSysClusterId(), secGroupDeleteMajorVersionID, ICFSecSchema.getSysAdminId());
			if (secGroupDeleteMajorVersionMembSysadmin != null) {
				secGroupDeleteMajorVersionMembSysadminID = secGroupDeleteMajorVersionMembSysadmin.getRequiredSecGrpMembId();
			}
			else {
				secGroupDeleteMajorVersionMembSysadminID = null;
			}
		}
		else {
			secGroupDeleteMajorVersionID = null;
			secGroupDeleteMajorVersionMembSysadmin = null;
			secGroupDeleteMajorVersionMembSysadminID = null;
		}


		if (secGroupCreateMajorVersionID == null || secGroupCreateMajorVersionID.isNull()) {
			secGroupCreateMajorVersionID = new CFLibDbKeyHash256(0);
		}
		if (secGroupCreateMajorVersionMembSysadminID == null || secGroupCreateMajorVersionMembSysadminID.isNull()) {
			secGroupCreateMajorVersionMembSysadminID = new CFLibDbKeyHash256(0);
		}
		if (secGroupReadMajorVersionID == null || secGroupReadMajorVersionID.isNull()) {
			secGroupReadMajorVersionID = new CFLibDbKeyHash256(0);
		}
		if (secGroupReadMajorVersionMembSysadminID == null || secGroupReadMajorVersionMembSysadminID.isNull()) {
			secGroupReadMajorVersionMembSysadminID = new CFLibDbKeyHash256(0);
		}
		if (secGroupUpdateMajorVersionID == null || secGroupUpdateMajorVersionID.isNull()) {
			secGroupUpdateMajorVersionID = new CFLibDbKeyHash256(0);
		}
		if (secGroupUpdateMajorVersionMembSysadminID == null || secGroupUpdateMajorVersionMembSysadminID.isNull()) {
			secGroupUpdateMajorVersionMembSysadminID = new CFLibDbKeyHash256(0);
		}
		if (secGroupDeleteMajorVersionID == null || secGroupDeleteMajorVersionID.isNull()) {
			secGroupDeleteMajorVersionID = new CFLibDbKeyHash256(0);
		}
		if (secGroupDeleteMajorVersionMembSysadminID == null || secGroupDeleteMajorVersionMembSysadminID.isNull()) {
			secGroupDeleteMajorVersionMembSysadminID = new CFLibDbKeyHash256(0);
		}

		if (secGroupCreateMajorVersion == null) {
			secGroupCreateMajorVersion = ICFSecSchema.getBackingCFSec().getFactorySecGroup().newRec();
			secGroupCreateMajorVersion.setRequiredRevision(1);
			secGroupCreateMajorVersion.setRequiredContainerCluster(ICFSecSchema.getSysClusterId());
			secGroupCreateMajorVersion.setRequiredName("CreateMajorVersion");
			secGroupCreateMajorVersion.setRequiredIsVisible(true);
			secGroupCreateMajorVersion.setRequiredSecGroupId(secGroupCreateMajorVersionID);
			secGroupCreateMajorVersion = ICFSecSchema.getBackingCFSec().getTableSecGroup().createSecGroup(auth, secGroupCreateMajorVersion);
			secGroupCreateMajorVersionID = secGroupCreateMajorVersion.getRequiredSecGroupId();
		}

		if (secGroupCreateMajorVersionMembSysadmin == null) {
			secGroupCreateMajorVersionMembSysadmin = ICFSecSchema.getBackingCFSec().getFactorySecGrpMemb().newRec();
			secGroupCreateMajorVersionMembSysadmin.setRequiredRevision(1);
			secGroupCreateMajorVersionMembSysadmin.setRequiredOwnerCluster(ICFSecSchema.getSysClusterId());
			secGroupCreateMajorVersionMembSysadmin.setRequiredContainerGroup(secGroupCreateMajorVersionID);
			secGroupCreateMajorVersionMembSysadmin.setRequiredParentUser(ICFSecSchema.getSysAdminId());
			secGroupCreateMajorVersionMembSysadmin.setRequiredSecGrpMembId(secGroupCreateMajorVersionMembSysadminID);
			secGroupCreateMajorVersionMembSysadmin = ICFSecSchema.getBackingCFSec().getTableSecGrpMemb().createSecGrpMemb(auth, secGroupCreateMajorVersionMembSysadmin);
			secGroupCreateMajorVersionMembSysadminID = secGroupCreateMajorVersionMembSysadmin.getRequiredSecGrpMembId();
		}

		if (secGroupReadMajorVersion == null) {
			secGroupReadMajorVersion = ICFSecSchema.getBackingCFSec().getFactorySecGroup().newRec();
			secGroupReadMajorVersion.setRequiredRevision(1);
			secGroupReadMajorVersion.setRequiredContainerCluster(ICFSecSchema.getSysClusterId());
			secGroupReadMajorVersion.setRequiredName("ReadMajorVersion");
			secGroupReadMajorVersion.setRequiredIsVisible(true);
			secGroupReadMajorVersion.setRequiredSecGroupId(secGroupReadMajorVersionID);
			secGroupReadMajorVersion = ICFSecSchema.getBackingCFSec().getTableSecGroup().createSecGroup(auth, secGroupReadMajorVersion);
			secGroupReadMajorVersionID = secGroupReadMajorVersion.getRequiredSecGroupId();
		}

		if (secGroupReadMajorVersionMembSysadmin == null) {
			secGroupReadMajorVersionMembSysadmin = ICFSecSchema.getBackingCFSec().getFactorySecGrpMemb().newRec();
			secGroupReadMajorVersionMembSysadmin.setRequiredRevision(1);
			secGroupReadMajorVersionMembSysadmin.setRequiredOwnerCluster(ICFSecSchema.getSysClusterId());
			secGroupReadMajorVersionMembSysadmin.setRequiredContainerGroup(secGroupReadMajorVersionID);
			secGroupReadMajorVersionMembSysadmin.setRequiredParentUser(ICFSecSchema.getSysAdminId());
			secGroupReadMajorVersionMembSysadmin.setRequiredSecGrpMembId(secGroupReadMajorVersionMembSysadminID);
			secGroupReadMajorVersionMembSysadmin = ICFSecSchema.getBackingCFSec().getTableSecGrpMemb().createSecGrpMemb(auth, secGroupReadMajorVersionMembSysadmin);
			secGroupReadMajorVersionMembSysadminID = secGroupReadMajorVersionMembSysadmin.getRequiredSecGrpMembId();
		}

		if (secGroupUpdateMajorVersion == null) {
			secGroupUpdateMajorVersion = ICFSecSchema.getBackingCFSec().getFactorySecGroup().newRec();
			secGroupUpdateMajorVersion.setRequiredRevision(1);
			secGroupUpdateMajorVersion.setRequiredContainerCluster(ICFSecSchema.getSysClusterId());
			secGroupUpdateMajorVersion.setRequiredName("UpdateMajorVersion");
			secGroupUpdateMajorVersion.setRequiredIsVisible(true);
			secGroupUpdateMajorVersion.setRequiredSecGroupId(secGroupUpdateMajorVersionID);
			secGroupUpdateMajorVersion = ICFSecSchema.getBackingCFSec().getTableSecGroup().createSecGroup(auth, secGroupUpdateMajorVersion);
			secGroupUpdateMajorVersionID = secGroupUpdateMajorVersion.getRequiredSecGroupId();
		}

		if (secGroupUpdateMajorVersionMembSysadmin == null) {
			secGroupUpdateMajorVersionMembSysadmin = ICFSecSchema.getBackingCFSec().getFactorySecGrpMemb().newRec();
			secGroupUpdateMajorVersionMembSysadmin.setRequiredRevision(1);
			secGroupUpdateMajorVersionMembSysadmin.setRequiredOwnerCluster(ICFSecSchema.getSysClusterId());
			secGroupUpdateMajorVersionMembSysadmin.setRequiredContainerGroup(secGroupUpdateMajorVersionID);
			secGroupUpdateMajorVersionMembSysadmin.setRequiredParentUser(ICFSecSchema.getSysAdminId());
			secGroupUpdateMajorVersionMembSysadmin.setRequiredSecGrpMembId(secGroupUpdateMajorVersionMembSysadminID);
			secGroupUpdateMajorVersionMembSysadmin = ICFSecSchema.getBackingCFSec().getTableSecGrpMemb().createSecGrpMemb(auth, secGroupUpdateMajorVersionMembSysadmin);
			secGroupUpdateMajorVersionMembSysadminID = secGroupUpdateMajorVersionMembSysadmin.getRequiredSecGrpMembId();
		}

		if (secGroupDeleteMajorVersion == null) {
			secGroupDeleteMajorVersion = ICFSecSchema.getBackingCFSec().getFactorySecGroup().newRec();
			secGroupDeleteMajorVersion.setRequiredRevision(1);
			secGroupDeleteMajorVersion.setRequiredContainerCluster(ICFSecSchema.getSysClusterId());
			secGroupDeleteMajorVersion.setRequiredName("DeleteMajorVersion");
			secGroupDeleteMajorVersion.setRequiredIsVisible(true);
			secGroupDeleteMajorVersion.setRequiredSecGroupId(secGroupDeleteMajorVersionID);
			secGroupDeleteMajorVersion = ICFSecSchema.getBackingCFSec().getTableSecGroup().createSecGroup(auth, secGroupDeleteMajorVersion);
			secGroupDeleteMajorVersionID = secGroupDeleteMajorVersion.getRequiredSecGroupId();
		}

		if (secGroupDeleteMajorVersionMembSysadmin == null) {
			secGroupDeleteMajorVersionMembSysadmin = ICFSecSchema.getBackingCFSec().getFactorySecGrpMemb().newRec();
			secGroupDeleteMajorVersionMembSysadmin.setRequiredRevision(1);
			secGroupDeleteMajorVersionMembSysadmin.setRequiredOwnerCluster(ICFSecSchema.getSysClusterId());
			secGroupDeleteMajorVersionMembSysadmin.setRequiredContainerGroup(secGroupDeleteMajorVersionID);
			secGroupDeleteMajorVersionMembSysadmin.setRequiredParentUser(ICFSecSchema.getSysAdminId());
			secGroupDeleteMajorVersionMembSysadmin.setRequiredSecGrpMembId(secGroupDeleteMajorVersionMembSysadminID);
			secGroupDeleteMajorVersionMembSysadmin = ICFSecSchema.getBackingCFSec().getTableSecGrpMemb().createSecGrpMemb(auth, secGroupDeleteMajorVersionMembSysadmin);
			secGroupDeleteMajorVersionMembSysadminID = secGroupDeleteMajorVersionMembSysadmin.getRequiredSecGrpMembId();
		}

	}		

	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = NoResultException.class, transactionManager = "cfint31TransactionManager")
	public void bootstrapTableMimeTypeSecurity(ICFSecAuthorization auth) {
		LocalDateTime now = LocalDateTime.now();

		ICFSecSecGroup secGroupCreateMimeType;
		CFLibDbKeyHash256 secGroupCreateMimeTypeID;
		ICFSecSecGrpMemb secGroupCreateMimeTypeMembSysadmin;
		CFLibDbKeyHash256 secGroupCreateMimeTypeMembSysadminID;
		ICFSecSecGroup secGroupReadMimeType;
		CFLibDbKeyHash256 secGroupReadMimeTypeID;
		ICFSecSecGrpMemb secGroupReadMimeTypeMembSysadmin;
		CFLibDbKeyHash256 secGroupReadMimeTypeMembSysadminID;
		ICFSecSecGroup secGroupUpdateMimeType;
		CFLibDbKeyHash256 secGroupUpdateMimeTypeID;
		ICFSecSecGrpMemb secGroupUpdateMimeTypeMembSysadmin;
		CFLibDbKeyHash256 secGroupUpdateMimeTypeMembSysadminID;
		ICFSecSecGroup secGroupDeleteMimeType;
		CFLibDbKeyHash256 secGroupDeleteMimeTypeID;
		ICFSecSecGrpMemb secGroupDeleteMimeTypeMembSysadmin;
		CFLibDbKeyHash256 secGroupDeleteMimeTypeMembSysadminID;

		secGroupCreateMimeType = ICFSecSchema.getBackingCFSec().getTableSecGroup().readDerivedByUNameIdx(auth, ICFSecSchema.getSysClusterId(), "CreateMimeType");
		if (secGroupCreateMimeType != null) {
			secGroupCreateMimeTypeID = secGroupCreateMimeType.getRequiredSecGroupId();
			secGroupCreateMimeTypeMembSysadmin = ICFSecSchema.getBackingCFSec().getTableSecGrpMemb().readDerivedByUUserIdx(auth, ICFSecSchema.getSysClusterId(), secGroupCreateMimeTypeID, ICFSecSchema.getSysAdminId());
			if (secGroupCreateMimeTypeMembSysadmin != null) {
				secGroupCreateMimeTypeMembSysadminID = secGroupCreateMimeTypeMembSysadmin.getRequiredSecGrpMembId();
			}
			else {
				secGroupCreateMimeTypeMembSysadminID = null;
			}
		}
		else {
			secGroupCreateMimeTypeID = null;
			secGroupCreateMimeTypeMembSysadmin = null;
			secGroupCreateMimeTypeMembSysadminID = null;
		}

		secGroupReadMimeType = ICFSecSchema.getBackingCFSec().getTableSecGroup().readDerivedByUNameIdx(auth, ICFSecSchema.getSysClusterId(), "ReadMimeType");
		if (secGroupReadMimeType != null) {
			secGroupReadMimeTypeID = secGroupReadMimeType.getRequiredSecGroupId();
			secGroupReadMimeTypeMembSysadmin = ICFSecSchema.getBackingCFSec().getTableSecGrpMemb().readDerivedByUUserIdx(auth, ICFSecSchema.getSysClusterId(), secGroupReadMimeTypeID, ICFSecSchema.getSysAdminId());
			if (secGroupReadMimeTypeMembSysadmin != null) {
				secGroupReadMimeTypeMembSysadminID = secGroupReadMimeTypeMembSysadmin.getRequiredSecGrpMembId();
			}
			else {
				secGroupReadMimeTypeMembSysadminID = null;
			}
		}
		else {
			secGroupReadMimeTypeID = null;
			secGroupReadMimeTypeMembSysadmin = null;
			secGroupReadMimeTypeMembSysadminID = null;
		}

		secGroupUpdateMimeType = ICFSecSchema.getBackingCFSec().getTableSecGroup().readDerivedByUNameIdx(auth, ICFSecSchema.getSysClusterId(), "UpdateMimeType");
		if (secGroupUpdateMimeType != null) {
			secGroupUpdateMimeTypeID = secGroupUpdateMimeType.getRequiredSecGroupId();
			secGroupUpdateMimeTypeMembSysadmin = ICFSecSchema.getBackingCFSec().getTableSecGrpMemb().readDerivedByUUserIdx(auth, ICFSecSchema.getSysClusterId(), secGroupUpdateMimeTypeID, ICFSecSchema.getSysAdminId());
			if (secGroupUpdateMimeTypeMembSysadmin != null) {
				secGroupUpdateMimeTypeMembSysadminID = secGroupUpdateMimeTypeMembSysadmin.getRequiredSecGrpMembId();
			}
			else {
				secGroupUpdateMimeTypeMembSysadminID = null;
			}
		}
		else {
			secGroupUpdateMimeTypeID = null;
			secGroupUpdateMimeTypeMembSysadmin = null;
			secGroupUpdateMimeTypeMembSysadminID = null;
		}

		secGroupDeleteMimeType = ICFSecSchema.getBackingCFSec().getTableSecGroup().readDerivedByUNameIdx(auth, ICFSecSchema.getSysClusterId(), "DeleteMimeType");
		if (secGroupDeleteMimeType != null) {
			secGroupDeleteMimeTypeID = secGroupDeleteMimeType.getRequiredSecGroupId();
			secGroupDeleteMimeTypeMembSysadmin = ICFSecSchema.getBackingCFSec().getTableSecGrpMemb().readDerivedByUUserIdx(auth, ICFSecSchema.getSysClusterId(), secGroupDeleteMimeTypeID, ICFSecSchema.getSysAdminId());
			if (secGroupDeleteMimeTypeMembSysadmin != null) {
				secGroupDeleteMimeTypeMembSysadminID = secGroupDeleteMimeTypeMembSysadmin.getRequiredSecGrpMembId();
			}
			else {
				secGroupDeleteMimeTypeMembSysadminID = null;
			}
		}
		else {
			secGroupDeleteMimeTypeID = null;
			secGroupDeleteMimeTypeMembSysadmin = null;
			secGroupDeleteMimeTypeMembSysadminID = null;
		}


		if (secGroupCreateMimeTypeID == null || secGroupCreateMimeTypeID.isNull()) {
			secGroupCreateMimeTypeID = new CFLibDbKeyHash256(0);
		}
		if (secGroupCreateMimeTypeMembSysadminID == null || secGroupCreateMimeTypeMembSysadminID.isNull()) {
			secGroupCreateMimeTypeMembSysadminID = new CFLibDbKeyHash256(0);
		}
		if (secGroupReadMimeTypeID == null || secGroupReadMimeTypeID.isNull()) {
			secGroupReadMimeTypeID = new CFLibDbKeyHash256(0);
		}
		if (secGroupReadMimeTypeMembSysadminID == null || secGroupReadMimeTypeMembSysadminID.isNull()) {
			secGroupReadMimeTypeMembSysadminID = new CFLibDbKeyHash256(0);
		}
		if (secGroupUpdateMimeTypeID == null || secGroupUpdateMimeTypeID.isNull()) {
			secGroupUpdateMimeTypeID = new CFLibDbKeyHash256(0);
		}
		if (secGroupUpdateMimeTypeMembSysadminID == null || secGroupUpdateMimeTypeMembSysadminID.isNull()) {
			secGroupUpdateMimeTypeMembSysadminID = new CFLibDbKeyHash256(0);
		}
		if (secGroupDeleteMimeTypeID == null || secGroupDeleteMimeTypeID.isNull()) {
			secGroupDeleteMimeTypeID = new CFLibDbKeyHash256(0);
		}
		if (secGroupDeleteMimeTypeMembSysadminID == null || secGroupDeleteMimeTypeMembSysadminID.isNull()) {
			secGroupDeleteMimeTypeMembSysadminID = new CFLibDbKeyHash256(0);
		}

		if (secGroupCreateMimeType == null) {
			secGroupCreateMimeType = ICFSecSchema.getBackingCFSec().getFactorySecGroup().newRec();
			secGroupCreateMimeType.setRequiredRevision(1);
			secGroupCreateMimeType.setRequiredContainerCluster(ICFSecSchema.getSysClusterId());
			secGroupCreateMimeType.setRequiredName("CreateMimeType");
			secGroupCreateMimeType.setRequiredIsVisible(true);
			secGroupCreateMimeType.setRequiredSecGroupId(secGroupCreateMimeTypeID);
			secGroupCreateMimeType = ICFSecSchema.getBackingCFSec().getTableSecGroup().createSecGroup(auth, secGroupCreateMimeType);
			secGroupCreateMimeTypeID = secGroupCreateMimeType.getRequiredSecGroupId();
		}

		if (secGroupCreateMimeTypeMembSysadmin == null) {
			secGroupCreateMimeTypeMembSysadmin = ICFSecSchema.getBackingCFSec().getFactorySecGrpMemb().newRec();
			secGroupCreateMimeTypeMembSysadmin.setRequiredRevision(1);
			secGroupCreateMimeTypeMembSysadmin.setRequiredOwnerCluster(ICFSecSchema.getSysClusterId());
			secGroupCreateMimeTypeMembSysadmin.setRequiredContainerGroup(secGroupCreateMimeTypeID);
			secGroupCreateMimeTypeMembSysadmin.setRequiredParentUser(ICFSecSchema.getSysAdminId());
			secGroupCreateMimeTypeMembSysadmin.setRequiredSecGrpMembId(secGroupCreateMimeTypeMembSysadminID);
			secGroupCreateMimeTypeMembSysadmin = ICFSecSchema.getBackingCFSec().getTableSecGrpMemb().createSecGrpMemb(auth, secGroupCreateMimeTypeMembSysadmin);
			secGroupCreateMimeTypeMembSysadminID = secGroupCreateMimeTypeMembSysadmin.getRequiredSecGrpMembId();
		}

		if (secGroupReadMimeType == null) {
			secGroupReadMimeType = ICFSecSchema.getBackingCFSec().getFactorySecGroup().newRec();
			secGroupReadMimeType.setRequiredRevision(1);
			secGroupReadMimeType.setRequiredContainerCluster(ICFSecSchema.getSysClusterId());
			secGroupReadMimeType.setRequiredName("ReadMimeType");
			secGroupReadMimeType.setRequiredIsVisible(true);
			secGroupReadMimeType.setRequiredSecGroupId(secGroupReadMimeTypeID);
			secGroupReadMimeType = ICFSecSchema.getBackingCFSec().getTableSecGroup().createSecGroup(auth, secGroupReadMimeType);
			secGroupReadMimeTypeID = secGroupReadMimeType.getRequiredSecGroupId();
		}

		if (secGroupReadMimeTypeMembSysadmin == null) {
			secGroupReadMimeTypeMembSysadmin = ICFSecSchema.getBackingCFSec().getFactorySecGrpMemb().newRec();
			secGroupReadMimeTypeMembSysadmin.setRequiredRevision(1);
			secGroupReadMimeTypeMembSysadmin.setRequiredOwnerCluster(ICFSecSchema.getSysClusterId());
			secGroupReadMimeTypeMembSysadmin.setRequiredContainerGroup(secGroupReadMimeTypeID);
			secGroupReadMimeTypeMembSysadmin.setRequiredParentUser(ICFSecSchema.getSysAdminId());
			secGroupReadMimeTypeMembSysadmin.setRequiredSecGrpMembId(secGroupReadMimeTypeMembSysadminID);
			secGroupReadMimeTypeMembSysadmin = ICFSecSchema.getBackingCFSec().getTableSecGrpMemb().createSecGrpMemb(auth, secGroupReadMimeTypeMembSysadmin);
			secGroupReadMimeTypeMembSysadminID = secGroupReadMimeTypeMembSysadmin.getRequiredSecGrpMembId();
		}

		if (secGroupUpdateMimeType == null) {
			secGroupUpdateMimeType = ICFSecSchema.getBackingCFSec().getFactorySecGroup().newRec();
			secGroupUpdateMimeType.setRequiredRevision(1);
			secGroupUpdateMimeType.setRequiredContainerCluster(ICFSecSchema.getSysClusterId());
			secGroupUpdateMimeType.setRequiredName("UpdateMimeType");
			secGroupUpdateMimeType.setRequiredIsVisible(true);
			secGroupUpdateMimeType.setRequiredSecGroupId(secGroupUpdateMimeTypeID);
			secGroupUpdateMimeType = ICFSecSchema.getBackingCFSec().getTableSecGroup().createSecGroup(auth, secGroupUpdateMimeType);
			secGroupUpdateMimeTypeID = secGroupUpdateMimeType.getRequiredSecGroupId();
		}

		if (secGroupUpdateMimeTypeMembSysadmin == null) {
			secGroupUpdateMimeTypeMembSysadmin = ICFSecSchema.getBackingCFSec().getFactorySecGrpMemb().newRec();
			secGroupUpdateMimeTypeMembSysadmin.setRequiredRevision(1);
			secGroupUpdateMimeTypeMembSysadmin.setRequiredOwnerCluster(ICFSecSchema.getSysClusterId());
			secGroupUpdateMimeTypeMembSysadmin.setRequiredContainerGroup(secGroupUpdateMimeTypeID);
			secGroupUpdateMimeTypeMembSysadmin.setRequiredParentUser(ICFSecSchema.getSysAdminId());
			secGroupUpdateMimeTypeMembSysadmin.setRequiredSecGrpMembId(secGroupUpdateMimeTypeMembSysadminID);
			secGroupUpdateMimeTypeMembSysadmin = ICFSecSchema.getBackingCFSec().getTableSecGrpMemb().createSecGrpMemb(auth, secGroupUpdateMimeTypeMembSysadmin);
			secGroupUpdateMimeTypeMembSysadminID = secGroupUpdateMimeTypeMembSysadmin.getRequiredSecGrpMembId();
		}

		if (secGroupDeleteMimeType == null) {
			secGroupDeleteMimeType = ICFSecSchema.getBackingCFSec().getFactorySecGroup().newRec();
			secGroupDeleteMimeType.setRequiredRevision(1);
			secGroupDeleteMimeType.setRequiredContainerCluster(ICFSecSchema.getSysClusterId());
			secGroupDeleteMimeType.setRequiredName("DeleteMimeType");
			secGroupDeleteMimeType.setRequiredIsVisible(true);
			secGroupDeleteMimeType.setRequiredSecGroupId(secGroupDeleteMimeTypeID);
			secGroupDeleteMimeType = ICFSecSchema.getBackingCFSec().getTableSecGroup().createSecGroup(auth, secGroupDeleteMimeType);
			secGroupDeleteMimeTypeID = secGroupDeleteMimeType.getRequiredSecGroupId();
		}

		if (secGroupDeleteMimeTypeMembSysadmin == null) {
			secGroupDeleteMimeTypeMembSysadmin = ICFSecSchema.getBackingCFSec().getFactorySecGrpMemb().newRec();
			secGroupDeleteMimeTypeMembSysadmin.setRequiredRevision(1);
			secGroupDeleteMimeTypeMembSysadmin.setRequiredOwnerCluster(ICFSecSchema.getSysClusterId());
			secGroupDeleteMimeTypeMembSysadmin.setRequiredContainerGroup(secGroupDeleteMimeTypeID);
			secGroupDeleteMimeTypeMembSysadmin.setRequiredParentUser(ICFSecSchema.getSysAdminId());
			secGroupDeleteMimeTypeMembSysadmin.setRequiredSecGrpMembId(secGroupDeleteMimeTypeMembSysadminID);
			secGroupDeleteMimeTypeMembSysadmin = ICFSecSchema.getBackingCFSec().getTableSecGrpMemb().createSecGrpMemb(auth, secGroupDeleteMimeTypeMembSysadmin);
			secGroupDeleteMimeTypeMembSysadminID = secGroupDeleteMimeTypeMembSysadmin.getRequiredSecGrpMembId();
		}

	}		

	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = NoResultException.class, transactionManager = "cfint31TransactionManager")
	public void bootstrapTableMinorVersionSecurity(ICFSecAuthorization auth) {
		LocalDateTime now = LocalDateTime.now();

		ICFSecSecGroup secGroupCreateMinorVersion;
		CFLibDbKeyHash256 secGroupCreateMinorVersionID;
		ICFSecSecGrpMemb secGroupCreateMinorVersionMembSysadmin;
		CFLibDbKeyHash256 secGroupCreateMinorVersionMembSysadminID;
		ICFSecSecGroup secGroupReadMinorVersion;
		CFLibDbKeyHash256 secGroupReadMinorVersionID;
		ICFSecSecGrpMemb secGroupReadMinorVersionMembSysadmin;
		CFLibDbKeyHash256 secGroupReadMinorVersionMembSysadminID;
		ICFSecSecGroup secGroupUpdateMinorVersion;
		CFLibDbKeyHash256 secGroupUpdateMinorVersionID;
		ICFSecSecGrpMemb secGroupUpdateMinorVersionMembSysadmin;
		CFLibDbKeyHash256 secGroupUpdateMinorVersionMembSysadminID;
		ICFSecSecGroup secGroupDeleteMinorVersion;
		CFLibDbKeyHash256 secGroupDeleteMinorVersionID;
		ICFSecSecGrpMemb secGroupDeleteMinorVersionMembSysadmin;
		CFLibDbKeyHash256 secGroupDeleteMinorVersionMembSysadminID;

		secGroupCreateMinorVersion = ICFSecSchema.getBackingCFSec().getTableSecGroup().readDerivedByUNameIdx(auth, ICFSecSchema.getSysClusterId(), "CreateMinorVersion");
		if (secGroupCreateMinorVersion != null) {
			secGroupCreateMinorVersionID = secGroupCreateMinorVersion.getRequiredSecGroupId();
			secGroupCreateMinorVersionMembSysadmin = ICFSecSchema.getBackingCFSec().getTableSecGrpMemb().readDerivedByUUserIdx(auth, ICFSecSchema.getSysClusterId(), secGroupCreateMinorVersionID, ICFSecSchema.getSysAdminId());
			if (secGroupCreateMinorVersionMembSysadmin != null) {
				secGroupCreateMinorVersionMembSysadminID = secGroupCreateMinorVersionMembSysadmin.getRequiredSecGrpMembId();
			}
			else {
				secGroupCreateMinorVersionMembSysadminID = null;
			}
		}
		else {
			secGroupCreateMinorVersionID = null;
			secGroupCreateMinorVersionMembSysadmin = null;
			secGroupCreateMinorVersionMembSysadminID = null;
		}

		secGroupReadMinorVersion = ICFSecSchema.getBackingCFSec().getTableSecGroup().readDerivedByUNameIdx(auth, ICFSecSchema.getSysClusterId(), "ReadMinorVersion");
		if (secGroupReadMinorVersion != null) {
			secGroupReadMinorVersionID = secGroupReadMinorVersion.getRequiredSecGroupId();
			secGroupReadMinorVersionMembSysadmin = ICFSecSchema.getBackingCFSec().getTableSecGrpMemb().readDerivedByUUserIdx(auth, ICFSecSchema.getSysClusterId(), secGroupReadMinorVersionID, ICFSecSchema.getSysAdminId());
			if (secGroupReadMinorVersionMembSysadmin != null) {
				secGroupReadMinorVersionMembSysadminID = secGroupReadMinorVersionMembSysadmin.getRequiredSecGrpMembId();
			}
			else {
				secGroupReadMinorVersionMembSysadminID = null;
			}
		}
		else {
			secGroupReadMinorVersionID = null;
			secGroupReadMinorVersionMembSysadmin = null;
			secGroupReadMinorVersionMembSysadminID = null;
		}

		secGroupUpdateMinorVersion = ICFSecSchema.getBackingCFSec().getTableSecGroup().readDerivedByUNameIdx(auth, ICFSecSchema.getSysClusterId(), "UpdateMinorVersion");
		if (secGroupUpdateMinorVersion != null) {
			secGroupUpdateMinorVersionID = secGroupUpdateMinorVersion.getRequiredSecGroupId();
			secGroupUpdateMinorVersionMembSysadmin = ICFSecSchema.getBackingCFSec().getTableSecGrpMemb().readDerivedByUUserIdx(auth, ICFSecSchema.getSysClusterId(), secGroupUpdateMinorVersionID, ICFSecSchema.getSysAdminId());
			if (secGroupUpdateMinorVersionMembSysadmin != null) {
				secGroupUpdateMinorVersionMembSysadminID = secGroupUpdateMinorVersionMembSysadmin.getRequiredSecGrpMembId();
			}
			else {
				secGroupUpdateMinorVersionMembSysadminID = null;
			}
		}
		else {
			secGroupUpdateMinorVersionID = null;
			secGroupUpdateMinorVersionMembSysadmin = null;
			secGroupUpdateMinorVersionMembSysadminID = null;
		}

		secGroupDeleteMinorVersion = ICFSecSchema.getBackingCFSec().getTableSecGroup().readDerivedByUNameIdx(auth, ICFSecSchema.getSysClusterId(), "DeleteMinorVersion");
		if (secGroupDeleteMinorVersion != null) {
			secGroupDeleteMinorVersionID = secGroupDeleteMinorVersion.getRequiredSecGroupId();
			secGroupDeleteMinorVersionMembSysadmin = ICFSecSchema.getBackingCFSec().getTableSecGrpMemb().readDerivedByUUserIdx(auth, ICFSecSchema.getSysClusterId(), secGroupDeleteMinorVersionID, ICFSecSchema.getSysAdminId());
			if (secGroupDeleteMinorVersionMembSysadmin != null) {
				secGroupDeleteMinorVersionMembSysadminID = secGroupDeleteMinorVersionMembSysadmin.getRequiredSecGrpMembId();
			}
			else {
				secGroupDeleteMinorVersionMembSysadminID = null;
			}
		}
		else {
			secGroupDeleteMinorVersionID = null;
			secGroupDeleteMinorVersionMembSysadmin = null;
			secGroupDeleteMinorVersionMembSysadminID = null;
		}


		if (secGroupCreateMinorVersionID == null || secGroupCreateMinorVersionID.isNull()) {
			secGroupCreateMinorVersionID = new CFLibDbKeyHash256(0);
		}
		if (secGroupCreateMinorVersionMembSysadminID == null || secGroupCreateMinorVersionMembSysadminID.isNull()) {
			secGroupCreateMinorVersionMembSysadminID = new CFLibDbKeyHash256(0);
		}
		if (secGroupReadMinorVersionID == null || secGroupReadMinorVersionID.isNull()) {
			secGroupReadMinorVersionID = new CFLibDbKeyHash256(0);
		}
		if (secGroupReadMinorVersionMembSysadminID == null || secGroupReadMinorVersionMembSysadminID.isNull()) {
			secGroupReadMinorVersionMembSysadminID = new CFLibDbKeyHash256(0);
		}
		if (secGroupUpdateMinorVersionID == null || secGroupUpdateMinorVersionID.isNull()) {
			secGroupUpdateMinorVersionID = new CFLibDbKeyHash256(0);
		}
		if (secGroupUpdateMinorVersionMembSysadminID == null || secGroupUpdateMinorVersionMembSysadminID.isNull()) {
			secGroupUpdateMinorVersionMembSysadminID = new CFLibDbKeyHash256(0);
		}
		if (secGroupDeleteMinorVersionID == null || secGroupDeleteMinorVersionID.isNull()) {
			secGroupDeleteMinorVersionID = new CFLibDbKeyHash256(0);
		}
		if (secGroupDeleteMinorVersionMembSysadminID == null || secGroupDeleteMinorVersionMembSysadminID.isNull()) {
			secGroupDeleteMinorVersionMembSysadminID = new CFLibDbKeyHash256(0);
		}

		if (secGroupCreateMinorVersion == null) {
			secGroupCreateMinorVersion = ICFSecSchema.getBackingCFSec().getFactorySecGroup().newRec();
			secGroupCreateMinorVersion.setRequiredRevision(1);
			secGroupCreateMinorVersion.setRequiredContainerCluster(ICFSecSchema.getSysClusterId());
			secGroupCreateMinorVersion.setRequiredName("CreateMinorVersion");
			secGroupCreateMinorVersion.setRequiredIsVisible(true);
			secGroupCreateMinorVersion.setRequiredSecGroupId(secGroupCreateMinorVersionID);
			secGroupCreateMinorVersion = ICFSecSchema.getBackingCFSec().getTableSecGroup().createSecGroup(auth, secGroupCreateMinorVersion);
			secGroupCreateMinorVersionID = secGroupCreateMinorVersion.getRequiredSecGroupId();
		}

		if (secGroupCreateMinorVersionMembSysadmin == null) {
			secGroupCreateMinorVersionMembSysadmin = ICFSecSchema.getBackingCFSec().getFactorySecGrpMemb().newRec();
			secGroupCreateMinorVersionMembSysadmin.setRequiredRevision(1);
			secGroupCreateMinorVersionMembSysadmin.setRequiredOwnerCluster(ICFSecSchema.getSysClusterId());
			secGroupCreateMinorVersionMembSysadmin.setRequiredContainerGroup(secGroupCreateMinorVersionID);
			secGroupCreateMinorVersionMembSysadmin.setRequiredParentUser(ICFSecSchema.getSysAdminId());
			secGroupCreateMinorVersionMembSysadmin.setRequiredSecGrpMembId(secGroupCreateMinorVersionMembSysadminID);
			secGroupCreateMinorVersionMembSysadmin = ICFSecSchema.getBackingCFSec().getTableSecGrpMemb().createSecGrpMemb(auth, secGroupCreateMinorVersionMembSysadmin);
			secGroupCreateMinorVersionMembSysadminID = secGroupCreateMinorVersionMembSysadmin.getRequiredSecGrpMembId();
		}

		if (secGroupReadMinorVersion == null) {
			secGroupReadMinorVersion = ICFSecSchema.getBackingCFSec().getFactorySecGroup().newRec();
			secGroupReadMinorVersion.setRequiredRevision(1);
			secGroupReadMinorVersion.setRequiredContainerCluster(ICFSecSchema.getSysClusterId());
			secGroupReadMinorVersion.setRequiredName("ReadMinorVersion");
			secGroupReadMinorVersion.setRequiredIsVisible(true);
			secGroupReadMinorVersion.setRequiredSecGroupId(secGroupReadMinorVersionID);
			secGroupReadMinorVersion = ICFSecSchema.getBackingCFSec().getTableSecGroup().createSecGroup(auth, secGroupReadMinorVersion);
			secGroupReadMinorVersionID = secGroupReadMinorVersion.getRequiredSecGroupId();
		}

		if (secGroupReadMinorVersionMembSysadmin == null) {
			secGroupReadMinorVersionMembSysadmin = ICFSecSchema.getBackingCFSec().getFactorySecGrpMemb().newRec();
			secGroupReadMinorVersionMembSysadmin.setRequiredRevision(1);
			secGroupReadMinorVersionMembSysadmin.setRequiredOwnerCluster(ICFSecSchema.getSysClusterId());
			secGroupReadMinorVersionMembSysadmin.setRequiredContainerGroup(secGroupReadMinorVersionID);
			secGroupReadMinorVersionMembSysadmin.setRequiredParentUser(ICFSecSchema.getSysAdminId());
			secGroupReadMinorVersionMembSysadmin.setRequiredSecGrpMembId(secGroupReadMinorVersionMembSysadminID);
			secGroupReadMinorVersionMembSysadmin = ICFSecSchema.getBackingCFSec().getTableSecGrpMemb().createSecGrpMemb(auth, secGroupReadMinorVersionMembSysadmin);
			secGroupReadMinorVersionMembSysadminID = secGroupReadMinorVersionMembSysadmin.getRequiredSecGrpMembId();
		}

		if (secGroupUpdateMinorVersion == null) {
			secGroupUpdateMinorVersion = ICFSecSchema.getBackingCFSec().getFactorySecGroup().newRec();
			secGroupUpdateMinorVersion.setRequiredRevision(1);
			secGroupUpdateMinorVersion.setRequiredContainerCluster(ICFSecSchema.getSysClusterId());
			secGroupUpdateMinorVersion.setRequiredName("UpdateMinorVersion");
			secGroupUpdateMinorVersion.setRequiredIsVisible(true);
			secGroupUpdateMinorVersion.setRequiredSecGroupId(secGroupUpdateMinorVersionID);
			secGroupUpdateMinorVersion = ICFSecSchema.getBackingCFSec().getTableSecGroup().createSecGroup(auth, secGroupUpdateMinorVersion);
			secGroupUpdateMinorVersionID = secGroupUpdateMinorVersion.getRequiredSecGroupId();
		}

		if (secGroupUpdateMinorVersionMembSysadmin == null) {
			secGroupUpdateMinorVersionMembSysadmin = ICFSecSchema.getBackingCFSec().getFactorySecGrpMemb().newRec();
			secGroupUpdateMinorVersionMembSysadmin.setRequiredRevision(1);
			secGroupUpdateMinorVersionMembSysadmin.setRequiredOwnerCluster(ICFSecSchema.getSysClusterId());
			secGroupUpdateMinorVersionMembSysadmin.setRequiredContainerGroup(secGroupUpdateMinorVersionID);
			secGroupUpdateMinorVersionMembSysadmin.setRequiredParentUser(ICFSecSchema.getSysAdminId());
			secGroupUpdateMinorVersionMembSysadmin.setRequiredSecGrpMembId(secGroupUpdateMinorVersionMembSysadminID);
			secGroupUpdateMinorVersionMembSysadmin = ICFSecSchema.getBackingCFSec().getTableSecGrpMemb().createSecGrpMemb(auth, secGroupUpdateMinorVersionMembSysadmin);
			secGroupUpdateMinorVersionMembSysadminID = secGroupUpdateMinorVersionMembSysadmin.getRequiredSecGrpMembId();
		}

		if (secGroupDeleteMinorVersion == null) {
			secGroupDeleteMinorVersion = ICFSecSchema.getBackingCFSec().getFactorySecGroup().newRec();
			secGroupDeleteMinorVersion.setRequiredRevision(1);
			secGroupDeleteMinorVersion.setRequiredContainerCluster(ICFSecSchema.getSysClusterId());
			secGroupDeleteMinorVersion.setRequiredName("DeleteMinorVersion");
			secGroupDeleteMinorVersion.setRequiredIsVisible(true);
			secGroupDeleteMinorVersion.setRequiredSecGroupId(secGroupDeleteMinorVersionID);
			secGroupDeleteMinorVersion = ICFSecSchema.getBackingCFSec().getTableSecGroup().createSecGroup(auth, secGroupDeleteMinorVersion);
			secGroupDeleteMinorVersionID = secGroupDeleteMinorVersion.getRequiredSecGroupId();
		}

		if (secGroupDeleteMinorVersionMembSysadmin == null) {
			secGroupDeleteMinorVersionMembSysadmin = ICFSecSchema.getBackingCFSec().getFactorySecGrpMemb().newRec();
			secGroupDeleteMinorVersionMembSysadmin.setRequiredRevision(1);
			secGroupDeleteMinorVersionMembSysadmin.setRequiredOwnerCluster(ICFSecSchema.getSysClusterId());
			secGroupDeleteMinorVersionMembSysadmin.setRequiredContainerGroup(secGroupDeleteMinorVersionID);
			secGroupDeleteMinorVersionMembSysadmin.setRequiredParentUser(ICFSecSchema.getSysAdminId());
			secGroupDeleteMinorVersionMembSysadmin.setRequiredSecGrpMembId(secGroupDeleteMinorVersionMembSysadminID);
			secGroupDeleteMinorVersionMembSysadmin = ICFSecSchema.getBackingCFSec().getTableSecGrpMemb().createSecGrpMemb(auth, secGroupDeleteMinorVersionMembSysadmin);
			secGroupDeleteMinorVersionMembSysadminID = secGroupDeleteMinorVersionMembSysadmin.getRequiredSecGrpMembId();
		}

	}		

	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = NoResultException.class, transactionManager = "cfint31TransactionManager")
	public void bootstrapTableSubProjectSecurity(ICFSecAuthorization auth) {
		LocalDateTime now = LocalDateTime.now();

		ICFSecSecGroup secGroupCreateSubProject;
		CFLibDbKeyHash256 secGroupCreateSubProjectID;
		ICFSecSecGrpMemb secGroupCreateSubProjectMembSysadmin;
		CFLibDbKeyHash256 secGroupCreateSubProjectMembSysadminID;
		ICFSecSecGroup secGroupReadSubProject;
		CFLibDbKeyHash256 secGroupReadSubProjectID;
		ICFSecSecGrpMemb secGroupReadSubProjectMembSysadmin;
		CFLibDbKeyHash256 secGroupReadSubProjectMembSysadminID;
		ICFSecSecGroup secGroupUpdateSubProject;
		CFLibDbKeyHash256 secGroupUpdateSubProjectID;
		ICFSecSecGrpMemb secGroupUpdateSubProjectMembSysadmin;
		CFLibDbKeyHash256 secGroupUpdateSubProjectMembSysadminID;
		ICFSecSecGroup secGroupDeleteSubProject;
		CFLibDbKeyHash256 secGroupDeleteSubProjectID;
		ICFSecSecGrpMemb secGroupDeleteSubProjectMembSysadmin;
		CFLibDbKeyHash256 secGroupDeleteSubProjectMembSysadminID;

		secGroupCreateSubProject = ICFSecSchema.getBackingCFSec().getTableSecGroup().readDerivedByUNameIdx(auth, ICFSecSchema.getSysClusterId(), "CreateSubProject");
		if (secGroupCreateSubProject != null) {
			secGroupCreateSubProjectID = secGroupCreateSubProject.getRequiredSecGroupId();
			secGroupCreateSubProjectMembSysadmin = ICFSecSchema.getBackingCFSec().getTableSecGrpMemb().readDerivedByUUserIdx(auth, ICFSecSchema.getSysClusterId(), secGroupCreateSubProjectID, ICFSecSchema.getSysAdminId());
			if (secGroupCreateSubProjectMembSysadmin != null) {
				secGroupCreateSubProjectMembSysadminID = secGroupCreateSubProjectMembSysadmin.getRequiredSecGrpMembId();
			}
			else {
				secGroupCreateSubProjectMembSysadminID = null;
			}
		}
		else {
			secGroupCreateSubProjectID = null;
			secGroupCreateSubProjectMembSysadmin = null;
			secGroupCreateSubProjectMembSysadminID = null;
		}

		secGroupReadSubProject = ICFSecSchema.getBackingCFSec().getTableSecGroup().readDerivedByUNameIdx(auth, ICFSecSchema.getSysClusterId(), "ReadSubProject");
		if (secGroupReadSubProject != null) {
			secGroupReadSubProjectID = secGroupReadSubProject.getRequiredSecGroupId();
			secGroupReadSubProjectMembSysadmin = ICFSecSchema.getBackingCFSec().getTableSecGrpMemb().readDerivedByUUserIdx(auth, ICFSecSchema.getSysClusterId(), secGroupReadSubProjectID, ICFSecSchema.getSysAdminId());
			if (secGroupReadSubProjectMembSysadmin != null) {
				secGroupReadSubProjectMembSysadminID = secGroupReadSubProjectMembSysadmin.getRequiredSecGrpMembId();
			}
			else {
				secGroupReadSubProjectMembSysadminID = null;
			}
		}
		else {
			secGroupReadSubProjectID = null;
			secGroupReadSubProjectMembSysadmin = null;
			secGroupReadSubProjectMembSysadminID = null;
		}

		secGroupUpdateSubProject = ICFSecSchema.getBackingCFSec().getTableSecGroup().readDerivedByUNameIdx(auth, ICFSecSchema.getSysClusterId(), "UpdateSubProject");
		if (secGroupUpdateSubProject != null) {
			secGroupUpdateSubProjectID = secGroupUpdateSubProject.getRequiredSecGroupId();
			secGroupUpdateSubProjectMembSysadmin = ICFSecSchema.getBackingCFSec().getTableSecGrpMemb().readDerivedByUUserIdx(auth, ICFSecSchema.getSysClusterId(), secGroupUpdateSubProjectID, ICFSecSchema.getSysAdminId());
			if (secGroupUpdateSubProjectMembSysadmin != null) {
				secGroupUpdateSubProjectMembSysadminID = secGroupUpdateSubProjectMembSysadmin.getRequiredSecGrpMembId();
			}
			else {
				secGroupUpdateSubProjectMembSysadminID = null;
			}
		}
		else {
			secGroupUpdateSubProjectID = null;
			secGroupUpdateSubProjectMembSysadmin = null;
			secGroupUpdateSubProjectMembSysadminID = null;
		}

		secGroupDeleteSubProject = ICFSecSchema.getBackingCFSec().getTableSecGroup().readDerivedByUNameIdx(auth, ICFSecSchema.getSysClusterId(), "DeleteSubProject");
		if (secGroupDeleteSubProject != null) {
			secGroupDeleteSubProjectID = secGroupDeleteSubProject.getRequiredSecGroupId();
			secGroupDeleteSubProjectMembSysadmin = ICFSecSchema.getBackingCFSec().getTableSecGrpMemb().readDerivedByUUserIdx(auth, ICFSecSchema.getSysClusterId(), secGroupDeleteSubProjectID, ICFSecSchema.getSysAdminId());
			if (secGroupDeleteSubProjectMembSysadmin != null) {
				secGroupDeleteSubProjectMembSysadminID = secGroupDeleteSubProjectMembSysadmin.getRequiredSecGrpMembId();
			}
			else {
				secGroupDeleteSubProjectMembSysadminID = null;
			}
		}
		else {
			secGroupDeleteSubProjectID = null;
			secGroupDeleteSubProjectMembSysadmin = null;
			secGroupDeleteSubProjectMembSysadminID = null;
		}


		if (secGroupCreateSubProjectID == null || secGroupCreateSubProjectID.isNull()) {
			secGroupCreateSubProjectID = new CFLibDbKeyHash256(0);
		}
		if (secGroupCreateSubProjectMembSysadminID == null || secGroupCreateSubProjectMembSysadminID.isNull()) {
			secGroupCreateSubProjectMembSysadminID = new CFLibDbKeyHash256(0);
		}
		if (secGroupReadSubProjectID == null || secGroupReadSubProjectID.isNull()) {
			secGroupReadSubProjectID = new CFLibDbKeyHash256(0);
		}
		if (secGroupReadSubProjectMembSysadminID == null || secGroupReadSubProjectMembSysadminID.isNull()) {
			secGroupReadSubProjectMembSysadminID = new CFLibDbKeyHash256(0);
		}
		if (secGroupUpdateSubProjectID == null || secGroupUpdateSubProjectID.isNull()) {
			secGroupUpdateSubProjectID = new CFLibDbKeyHash256(0);
		}
		if (secGroupUpdateSubProjectMembSysadminID == null || secGroupUpdateSubProjectMembSysadminID.isNull()) {
			secGroupUpdateSubProjectMembSysadminID = new CFLibDbKeyHash256(0);
		}
		if (secGroupDeleteSubProjectID == null || secGroupDeleteSubProjectID.isNull()) {
			secGroupDeleteSubProjectID = new CFLibDbKeyHash256(0);
		}
		if (secGroupDeleteSubProjectMembSysadminID == null || secGroupDeleteSubProjectMembSysadminID.isNull()) {
			secGroupDeleteSubProjectMembSysadminID = new CFLibDbKeyHash256(0);
		}

		if (secGroupCreateSubProject == null) {
			secGroupCreateSubProject = ICFSecSchema.getBackingCFSec().getFactorySecGroup().newRec();
			secGroupCreateSubProject.setRequiredRevision(1);
			secGroupCreateSubProject.setRequiredContainerCluster(ICFSecSchema.getSysClusterId());
			secGroupCreateSubProject.setRequiredName("CreateSubProject");
			secGroupCreateSubProject.setRequiredIsVisible(true);
			secGroupCreateSubProject.setRequiredSecGroupId(secGroupCreateSubProjectID);
			secGroupCreateSubProject = ICFSecSchema.getBackingCFSec().getTableSecGroup().createSecGroup(auth, secGroupCreateSubProject);
			secGroupCreateSubProjectID = secGroupCreateSubProject.getRequiredSecGroupId();
		}

		if (secGroupCreateSubProjectMembSysadmin == null) {
			secGroupCreateSubProjectMembSysadmin = ICFSecSchema.getBackingCFSec().getFactorySecGrpMemb().newRec();
			secGroupCreateSubProjectMembSysadmin.setRequiredRevision(1);
			secGroupCreateSubProjectMembSysadmin.setRequiredOwnerCluster(ICFSecSchema.getSysClusterId());
			secGroupCreateSubProjectMembSysadmin.setRequiredContainerGroup(secGroupCreateSubProjectID);
			secGroupCreateSubProjectMembSysadmin.setRequiredParentUser(ICFSecSchema.getSysAdminId());
			secGroupCreateSubProjectMembSysadmin.setRequiredSecGrpMembId(secGroupCreateSubProjectMembSysadminID);
			secGroupCreateSubProjectMembSysadmin = ICFSecSchema.getBackingCFSec().getTableSecGrpMemb().createSecGrpMemb(auth, secGroupCreateSubProjectMembSysadmin);
			secGroupCreateSubProjectMembSysadminID = secGroupCreateSubProjectMembSysadmin.getRequiredSecGrpMembId();
		}

		if (secGroupReadSubProject == null) {
			secGroupReadSubProject = ICFSecSchema.getBackingCFSec().getFactorySecGroup().newRec();
			secGroupReadSubProject.setRequiredRevision(1);
			secGroupReadSubProject.setRequiredContainerCluster(ICFSecSchema.getSysClusterId());
			secGroupReadSubProject.setRequiredName("ReadSubProject");
			secGroupReadSubProject.setRequiredIsVisible(true);
			secGroupReadSubProject.setRequiredSecGroupId(secGroupReadSubProjectID);
			secGroupReadSubProject = ICFSecSchema.getBackingCFSec().getTableSecGroup().createSecGroup(auth, secGroupReadSubProject);
			secGroupReadSubProjectID = secGroupReadSubProject.getRequiredSecGroupId();
		}

		if (secGroupReadSubProjectMembSysadmin == null) {
			secGroupReadSubProjectMembSysadmin = ICFSecSchema.getBackingCFSec().getFactorySecGrpMemb().newRec();
			secGroupReadSubProjectMembSysadmin.setRequiredRevision(1);
			secGroupReadSubProjectMembSysadmin.setRequiredOwnerCluster(ICFSecSchema.getSysClusterId());
			secGroupReadSubProjectMembSysadmin.setRequiredContainerGroup(secGroupReadSubProjectID);
			secGroupReadSubProjectMembSysadmin.setRequiredParentUser(ICFSecSchema.getSysAdminId());
			secGroupReadSubProjectMembSysadmin.setRequiredSecGrpMembId(secGroupReadSubProjectMembSysadminID);
			secGroupReadSubProjectMembSysadmin = ICFSecSchema.getBackingCFSec().getTableSecGrpMemb().createSecGrpMemb(auth, secGroupReadSubProjectMembSysadmin);
			secGroupReadSubProjectMembSysadminID = secGroupReadSubProjectMembSysadmin.getRequiredSecGrpMembId();
		}

		if (secGroupUpdateSubProject == null) {
			secGroupUpdateSubProject = ICFSecSchema.getBackingCFSec().getFactorySecGroup().newRec();
			secGroupUpdateSubProject.setRequiredRevision(1);
			secGroupUpdateSubProject.setRequiredContainerCluster(ICFSecSchema.getSysClusterId());
			secGroupUpdateSubProject.setRequiredName("UpdateSubProject");
			secGroupUpdateSubProject.setRequiredIsVisible(true);
			secGroupUpdateSubProject.setRequiredSecGroupId(secGroupUpdateSubProjectID);
			secGroupUpdateSubProject = ICFSecSchema.getBackingCFSec().getTableSecGroup().createSecGroup(auth, secGroupUpdateSubProject);
			secGroupUpdateSubProjectID = secGroupUpdateSubProject.getRequiredSecGroupId();
		}

		if (secGroupUpdateSubProjectMembSysadmin == null) {
			secGroupUpdateSubProjectMembSysadmin = ICFSecSchema.getBackingCFSec().getFactorySecGrpMemb().newRec();
			secGroupUpdateSubProjectMembSysadmin.setRequiredRevision(1);
			secGroupUpdateSubProjectMembSysadmin.setRequiredOwnerCluster(ICFSecSchema.getSysClusterId());
			secGroupUpdateSubProjectMembSysadmin.setRequiredContainerGroup(secGroupUpdateSubProjectID);
			secGroupUpdateSubProjectMembSysadmin.setRequiredParentUser(ICFSecSchema.getSysAdminId());
			secGroupUpdateSubProjectMembSysadmin.setRequiredSecGrpMembId(secGroupUpdateSubProjectMembSysadminID);
			secGroupUpdateSubProjectMembSysadmin = ICFSecSchema.getBackingCFSec().getTableSecGrpMemb().createSecGrpMemb(auth, secGroupUpdateSubProjectMembSysadmin);
			secGroupUpdateSubProjectMembSysadminID = secGroupUpdateSubProjectMembSysadmin.getRequiredSecGrpMembId();
		}

		if (secGroupDeleteSubProject == null) {
			secGroupDeleteSubProject = ICFSecSchema.getBackingCFSec().getFactorySecGroup().newRec();
			secGroupDeleteSubProject.setRequiredRevision(1);
			secGroupDeleteSubProject.setRequiredContainerCluster(ICFSecSchema.getSysClusterId());
			secGroupDeleteSubProject.setRequiredName("DeleteSubProject");
			secGroupDeleteSubProject.setRequiredIsVisible(true);
			secGroupDeleteSubProject.setRequiredSecGroupId(secGroupDeleteSubProjectID);
			secGroupDeleteSubProject = ICFSecSchema.getBackingCFSec().getTableSecGroup().createSecGroup(auth, secGroupDeleteSubProject);
			secGroupDeleteSubProjectID = secGroupDeleteSubProject.getRequiredSecGroupId();
		}

		if (secGroupDeleteSubProjectMembSysadmin == null) {
			secGroupDeleteSubProjectMembSysadmin = ICFSecSchema.getBackingCFSec().getFactorySecGrpMemb().newRec();
			secGroupDeleteSubProjectMembSysadmin.setRequiredRevision(1);
			secGroupDeleteSubProjectMembSysadmin.setRequiredOwnerCluster(ICFSecSchema.getSysClusterId());
			secGroupDeleteSubProjectMembSysadmin.setRequiredContainerGroup(secGroupDeleteSubProjectID);
			secGroupDeleteSubProjectMembSysadmin.setRequiredParentUser(ICFSecSchema.getSysAdminId());
			secGroupDeleteSubProjectMembSysadmin.setRequiredSecGrpMembId(secGroupDeleteSubProjectMembSysadminID);
			secGroupDeleteSubProjectMembSysadmin = ICFSecSchema.getBackingCFSec().getTableSecGrpMemb().createSecGrpMemb(auth, secGroupDeleteSubProjectMembSysadmin);
			secGroupDeleteSubProjectMembSysadminID = secGroupDeleteSubProjectMembSysadmin.getRequiredSecGrpMembId();
		}

	}		

	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = NoResultException.class, transactionManager = "cfint31TransactionManager")
	public void bootstrapTableTldSecurity(ICFSecAuthorization auth) {
		LocalDateTime now = LocalDateTime.now();

		ICFSecSecGroup secGroupCreateTld;
		CFLibDbKeyHash256 secGroupCreateTldID;
		ICFSecSecGrpMemb secGroupCreateTldMembSysadmin;
		CFLibDbKeyHash256 secGroupCreateTldMembSysadminID;
		ICFSecSecGroup secGroupReadTld;
		CFLibDbKeyHash256 secGroupReadTldID;
		ICFSecSecGrpMemb secGroupReadTldMembSysadmin;
		CFLibDbKeyHash256 secGroupReadTldMembSysadminID;
		ICFSecSecGroup secGroupUpdateTld;
		CFLibDbKeyHash256 secGroupUpdateTldID;
		ICFSecSecGrpMemb secGroupUpdateTldMembSysadmin;
		CFLibDbKeyHash256 secGroupUpdateTldMembSysadminID;
		ICFSecSecGroup secGroupDeleteTld;
		CFLibDbKeyHash256 secGroupDeleteTldID;
		ICFSecSecGrpMemb secGroupDeleteTldMembSysadmin;
		CFLibDbKeyHash256 secGroupDeleteTldMembSysadminID;

		secGroupCreateTld = ICFSecSchema.getBackingCFSec().getTableSecGroup().readDerivedByUNameIdx(auth, ICFSecSchema.getSysClusterId(), "CreateTld");
		if (secGroupCreateTld != null) {
			secGroupCreateTldID = secGroupCreateTld.getRequiredSecGroupId();
			secGroupCreateTldMembSysadmin = ICFSecSchema.getBackingCFSec().getTableSecGrpMemb().readDerivedByUUserIdx(auth, ICFSecSchema.getSysClusterId(), secGroupCreateTldID, ICFSecSchema.getSysAdminId());
			if (secGroupCreateTldMembSysadmin != null) {
				secGroupCreateTldMembSysadminID = secGroupCreateTldMembSysadmin.getRequiredSecGrpMembId();
			}
			else {
				secGroupCreateTldMembSysadminID = null;
			}
		}
		else {
			secGroupCreateTldID = null;
			secGroupCreateTldMembSysadmin = null;
			secGroupCreateTldMembSysadminID = null;
		}

		secGroupReadTld = ICFSecSchema.getBackingCFSec().getTableSecGroup().readDerivedByUNameIdx(auth, ICFSecSchema.getSysClusterId(), "ReadTld");
		if (secGroupReadTld != null) {
			secGroupReadTldID = secGroupReadTld.getRequiredSecGroupId();
			secGroupReadTldMembSysadmin = ICFSecSchema.getBackingCFSec().getTableSecGrpMemb().readDerivedByUUserIdx(auth, ICFSecSchema.getSysClusterId(), secGroupReadTldID, ICFSecSchema.getSysAdminId());
			if (secGroupReadTldMembSysadmin != null) {
				secGroupReadTldMembSysadminID = secGroupReadTldMembSysadmin.getRequiredSecGrpMembId();
			}
			else {
				secGroupReadTldMembSysadminID = null;
			}
		}
		else {
			secGroupReadTldID = null;
			secGroupReadTldMembSysadmin = null;
			secGroupReadTldMembSysadminID = null;
		}

		secGroupUpdateTld = ICFSecSchema.getBackingCFSec().getTableSecGroup().readDerivedByUNameIdx(auth, ICFSecSchema.getSysClusterId(), "UpdateTld");
		if (secGroupUpdateTld != null) {
			secGroupUpdateTldID = secGroupUpdateTld.getRequiredSecGroupId();
			secGroupUpdateTldMembSysadmin = ICFSecSchema.getBackingCFSec().getTableSecGrpMemb().readDerivedByUUserIdx(auth, ICFSecSchema.getSysClusterId(), secGroupUpdateTldID, ICFSecSchema.getSysAdminId());
			if (secGroupUpdateTldMembSysadmin != null) {
				secGroupUpdateTldMembSysadminID = secGroupUpdateTldMembSysadmin.getRequiredSecGrpMembId();
			}
			else {
				secGroupUpdateTldMembSysadminID = null;
			}
		}
		else {
			secGroupUpdateTldID = null;
			secGroupUpdateTldMembSysadmin = null;
			secGroupUpdateTldMembSysadminID = null;
		}

		secGroupDeleteTld = ICFSecSchema.getBackingCFSec().getTableSecGroup().readDerivedByUNameIdx(auth, ICFSecSchema.getSysClusterId(), "DeleteTld");
		if (secGroupDeleteTld != null) {
			secGroupDeleteTldID = secGroupDeleteTld.getRequiredSecGroupId();
			secGroupDeleteTldMembSysadmin = ICFSecSchema.getBackingCFSec().getTableSecGrpMemb().readDerivedByUUserIdx(auth, ICFSecSchema.getSysClusterId(), secGroupDeleteTldID, ICFSecSchema.getSysAdminId());
			if (secGroupDeleteTldMembSysadmin != null) {
				secGroupDeleteTldMembSysadminID = secGroupDeleteTldMembSysadmin.getRequiredSecGrpMembId();
			}
			else {
				secGroupDeleteTldMembSysadminID = null;
			}
		}
		else {
			secGroupDeleteTldID = null;
			secGroupDeleteTldMembSysadmin = null;
			secGroupDeleteTldMembSysadminID = null;
		}


		if (secGroupCreateTldID == null || secGroupCreateTldID.isNull()) {
			secGroupCreateTldID = new CFLibDbKeyHash256(0);
		}
		if (secGroupCreateTldMembSysadminID == null || secGroupCreateTldMembSysadminID.isNull()) {
			secGroupCreateTldMembSysadminID = new CFLibDbKeyHash256(0);
		}
		if (secGroupReadTldID == null || secGroupReadTldID.isNull()) {
			secGroupReadTldID = new CFLibDbKeyHash256(0);
		}
		if (secGroupReadTldMembSysadminID == null || secGroupReadTldMembSysadminID.isNull()) {
			secGroupReadTldMembSysadminID = new CFLibDbKeyHash256(0);
		}
		if (secGroupUpdateTldID == null || secGroupUpdateTldID.isNull()) {
			secGroupUpdateTldID = new CFLibDbKeyHash256(0);
		}
		if (secGroupUpdateTldMembSysadminID == null || secGroupUpdateTldMembSysadminID.isNull()) {
			secGroupUpdateTldMembSysadminID = new CFLibDbKeyHash256(0);
		}
		if (secGroupDeleteTldID == null || secGroupDeleteTldID.isNull()) {
			secGroupDeleteTldID = new CFLibDbKeyHash256(0);
		}
		if (secGroupDeleteTldMembSysadminID == null || secGroupDeleteTldMembSysadminID.isNull()) {
			secGroupDeleteTldMembSysadminID = new CFLibDbKeyHash256(0);
		}

		if (secGroupCreateTld == null) {
			secGroupCreateTld = ICFSecSchema.getBackingCFSec().getFactorySecGroup().newRec();
			secGroupCreateTld.setRequiredRevision(1);
			secGroupCreateTld.setRequiredContainerCluster(ICFSecSchema.getSysClusterId());
			secGroupCreateTld.setRequiredName("CreateTld");
			secGroupCreateTld.setRequiredIsVisible(true);
			secGroupCreateTld.setRequiredSecGroupId(secGroupCreateTldID);
			secGroupCreateTld = ICFSecSchema.getBackingCFSec().getTableSecGroup().createSecGroup(auth, secGroupCreateTld);
			secGroupCreateTldID = secGroupCreateTld.getRequiredSecGroupId();
		}

		if (secGroupCreateTldMembSysadmin == null) {
			secGroupCreateTldMembSysadmin = ICFSecSchema.getBackingCFSec().getFactorySecGrpMemb().newRec();
			secGroupCreateTldMembSysadmin.setRequiredRevision(1);
			secGroupCreateTldMembSysadmin.setRequiredOwnerCluster(ICFSecSchema.getSysClusterId());
			secGroupCreateTldMembSysadmin.setRequiredContainerGroup(secGroupCreateTldID);
			secGroupCreateTldMembSysadmin.setRequiredParentUser(ICFSecSchema.getSysAdminId());
			secGroupCreateTldMembSysadmin.setRequiredSecGrpMembId(secGroupCreateTldMembSysadminID);
			secGroupCreateTldMembSysadmin = ICFSecSchema.getBackingCFSec().getTableSecGrpMemb().createSecGrpMemb(auth, secGroupCreateTldMembSysadmin);
			secGroupCreateTldMembSysadminID = secGroupCreateTldMembSysadmin.getRequiredSecGrpMembId();
		}

		if (secGroupReadTld == null) {
			secGroupReadTld = ICFSecSchema.getBackingCFSec().getFactorySecGroup().newRec();
			secGroupReadTld.setRequiredRevision(1);
			secGroupReadTld.setRequiredContainerCluster(ICFSecSchema.getSysClusterId());
			secGroupReadTld.setRequiredName("ReadTld");
			secGroupReadTld.setRequiredIsVisible(true);
			secGroupReadTld.setRequiredSecGroupId(secGroupReadTldID);
			secGroupReadTld = ICFSecSchema.getBackingCFSec().getTableSecGroup().createSecGroup(auth, secGroupReadTld);
			secGroupReadTldID = secGroupReadTld.getRequiredSecGroupId();
		}

		if (secGroupReadTldMembSysadmin == null) {
			secGroupReadTldMembSysadmin = ICFSecSchema.getBackingCFSec().getFactorySecGrpMemb().newRec();
			secGroupReadTldMembSysadmin.setRequiredRevision(1);
			secGroupReadTldMembSysadmin.setRequiredOwnerCluster(ICFSecSchema.getSysClusterId());
			secGroupReadTldMembSysadmin.setRequiredContainerGroup(secGroupReadTldID);
			secGroupReadTldMembSysadmin.setRequiredParentUser(ICFSecSchema.getSysAdminId());
			secGroupReadTldMembSysadmin.setRequiredSecGrpMembId(secGroupReadTldMembSysadminID);
			secGroupReadTldMembSysadmin = ICFSecSchema.getBackingCFSec().getTableSecGrpMemb().createSecGrpMemb(auth, secGroupReadTldMembSysadmin);
			secGroupReadTldMembSysadminID = secGroupReadTldMembSysadmin.getRequiredSecGrpMembId();
		}

		if (secGroupUpdateTld == null) {
			secGroupUpdateTld = ICFSecSchema.getBackingCFSec().getFactorySecGroup().newRec();
			secGroupUpdateTld.setRequiredRevision(1);
			secGroupUpdateTld.setRequiredContainerCluster(ICFSecSchema.getSysClusterId());
			secGroupUpdateTld.setRequiredName("UpdateTld");
			secGroupUpdateTld.setRequiredIsVisible(true);
			secGroupUpdateTld.setRequiredSecGroupId(secGroupUpdateTldID);
			secGroupUpdateTld = ICFSecSchema.getBackingCFSec().getTableSecGroup().createSecGroup(auth, secGroupUpdateTld);
			secGroupUpdateTldID = secGroupUpdateTld.getRequiredSecGroupId();
		}

		if (secGroupUpdateTldMembSysadmin == null) {
			secGroupUpdateTldMembSysadmin = ICFSecSchema.getBackingCFSec().getFactorySecGrpMemb().newRec();
			secGroupUpdateTldMembSysadmin.setRequiredRevision(1);
			secGroupUpdateTldMembSysadmin.setRequiredOwnerCluster(ICFSecSchema.getSysClusterId());
			secGroupUpdateTldMembSysadmin.setRequiredContainerGroup(secGroupUpdateTldID);
			secGroupUpdateTldMembSysadmin.setRequiredParentUser(ICFSecSchema.getSysAdminId());
			secGroupUpdateTldMembSysadmin.setRequiredSecGrpMembId(secGroupUpdateTldMembSysadminID);
			secGroupUpdateTldMembSysadmin = ICFSecSchema.getBackingCFSec().getTableSecGrpMemb().createSecGrpMemb(auth, secGroupUpdateTldMembSysadmin);
			secGroupUpdateTldMembSysadminID = secGroupUpdateTldMembSysadmin.getRequiredSecGrpMembId();
		}

		if (secGroupDeleteTld == null) {
			secGroupDeleteTld = ICFSecSchema.getBackingCFSec().getFactorySecGroup().newRec();
			secGroupDeleteTld.setRequiredRevision(1);
			secGroupDeleteTld.setRequiredContainerCluster(ICFSecSchema.getSysClusterId());
			secGroupDeleteTld.setRequiredName("DeleteTld");
			secGroupDeleteTld.setRequiredIsVisible(true);
			secGroupDeleteTld.setRequiredSecGroupId(secGroupDeleteTldID);
			secGroupDeleteTld = ICFSecSchema.getBackingCFSec().getTableSecGroup().createSecGroup(auth, secGroupDeleteTld);
			secGroupDeleteTldID = secGroupDeleteTld.getRequiredSecGroupId();
		}

		if (secGroupDeleteTldMembSysadmin == null) {
			secGroupDeleteTldMembSysadmin = ICFSecSchema.getBackingCFSec().getFactorySecGrpMemb().newRec();
			secGroupDeleteTldMembSysadmin.setRequiredRevision(1);
			secGroupDeleteTldMembSysadmin.setRequiredOwnerCluster(ICFSecSchema.getSysClusterId());
			secGroupDeleteTldMembSysadmin.setRequiredContainerGroup(secGroupDeleteTldID);
			secGroupDeleteTldMembSysadmin.setRequiredParentUser(ICFSecSchema.getSysAdminId());
			secGroupDeleteTldMembSysadmin.setRequiredSecGrpMembId(secGroupDeleteTldMembSysadminID);
			secGroupDeleteTldMembSysadmin = ICFSecSchema.getBackingCFSec().getTableSecGrpMemb().createSecGrpMemb(auth, secGroupDeleteTldMembSysadmin);
			secGroupDeleteTldMembSysadminID = secGroupDeleteTldMembSysadmin.getRequiredSecGrpMembId();
		}

	}		

	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = NoResultException.class, transactionManager = "cfint31TransactionManager")
	public void bootstrapTableTopDomainSecurity(ICFSecAuthorization auth) {
		LocalDateTime now = LocalDateTime.now();

		ICFSecSecGroup secGroupCreateTopDomain;
		CFLibDbKeyHash256 secGroupCreateTopDomainID;
		ICFSecSecGrpMemb secGroupCreateTopDomainMembSysadmin;
		CFLibDbKeyHash256 secGroupCreateTopDomainMembSysadminID;
		ICFSecSecGroup secGroupReadTopDomain;
		CFLibDbKeyHash256 secGroupReadTopDomainID;
		ICFSecSecGrpMemb secGroupReadTopDomainMembSysadmin;
		CFLibDbKeyHash256 secGroupReadTopDomainMembSysadminID;
		ICFSecSecGroup secGroupUpdateTopDomain;
		CFLibDbKeyHash256 secGroupUpdateTopDomainID;
		ICFSecSecGrpMemb secGroupUpdateTopDomainMembSysadmin;
		CFLibDbKeyHash256 secGroupUpdateTopDomainMembSysadminID;
		ICFSecSecGroup secGroupDeleteTopDomain;
		CFLibDbKeyHash256 secGroupDeleteTopDomainID;
		ICFSecSecGrpMemb secGroupDeleteTopDomainMembSysadmin;
		CFLibDbKeyHash256 secGroupDeleteTopDomainMembSysadminID;

		secGroupCreateTopDomain = ICFSecSchema.getBackingCFSec().getTableSecGroup().readDerivedByUNameIdx(auth, ICFSecSchema.getSysClusterId(), "CreateTopDomain");
		if (secGroupCreateTopDomain != null) {
			secGroupCreateTopDomainID = secGroupCreateTopDomain.getRequiredSecGroupId();
			secGroupCreateTopDomainMembSysadmin = ICFSecSchema.getBackingCFSec().getTableSecGrpMemb().readDerivedByUUserIdx(auth, ICFSecSchema.getSysClusterId(), secGroupCreateTopDomainID, ICFSecSchema.getSysAdminId());
			if (secGroupCreateTopDomainMembSysadmin != null) {
				secGroupCreateTopDomainMembSysadminID = secGroupCreateTopDomainMembSysadmin.getRequiredSecGrpMembId();
			}
			else {
				secGroupCreateTopDomainMembSysadminID = null;
			}
		}
		else {
			secGroupCreateTopDomainID = null;
			secGroupCreateTopDomainMembSysadmin = null;
			secGroupCreateTopDomainMembSysadminID = null;
		}

		secGroupReadTopDomain = ICFSecSchema.getBackingCFSec().getTableSecGroup().readDerivedByUNameIdx(auth, ICFSecSchema.getSysClusterId(), "ReadTopDomain");
		if (secGroupReadTopDomain != null) {
			secGroupReadTopDomainID = secGroupReadTopDomain.getRequiredSecGroupId();
			secGroupReadTopDomainMembSysadmin = ICFSecSchema.getBackingCFSec().getTableSecGrpMemb().readDerivedByUUserIdx(auth, ICFSecSchema.getSysClusterId(), secGroupReadTopDomainID, ICFSecSchema.getSysAdminId());
			if (secGroupReadTopDomainMembSysadmin != null) {
				secGroupReadTopDomainMembSysadminID = secGroupReadTopDomainMembSysadmin.getRequiredSecGrpMembId();
			}
			else {
				secGroupReadTopDomainMembSysadminID = null;
			}
		}
		else {
			secGroupReadTopDomainID = null;
			secGroupReadTopDomainMembSysadmin = null;
			secGroupReadTopDomainMembSysadminID = null;
		}

		secGroupUpdateTopDomain = ICFSecSchema.getBackingCFSec().getTableSecGroup().readDerivedByUNameIdx(auth, ICFSecSchema.getSysClusterId(), "UpdateTopDomain");
		if (secGroupUpdateTopDomain != null) {
			secGroupUpdateTopDomainID = secGroupUpdateTopDomain.getRequiredSecGroupId();
			secGroupUpdateTopDomainMembSysadmin = ICFSecSchema.getBackingCFSec().getTableSecGrpMemb().readDerivedByUUserIdx(auth, ICFSecSchema.getSysClusterId(), secGroupUpdateTopDomainID, ICFSecSchema.getSysAdminId());
			if (secGroupUpdateTopDomainMembSysadmin != null) {
				secGroupUpdateTopDomainMembSysadminID = secGroupUpdateTopDomainMembSysadmin.getRequiredSecGrpMembId();
			}
			else {
				secGroupUpdateTopDomainMembSysadminID = null;
			}
		}
		else {
			secGroupUpdateTopDomainID = null;
			secGroupUpdateTopDomainMembSysadmin = null;
			secGroupUpdateTopDomainMembSysadminID = null;
		}

		secGroupDeleteTopDomain = ICFSecSchema.getBackingCFSec().getTableSecGroup().readDerivedByUNameIdx(auth, ICFSecSchema.getSysClusterId(), "DeleteTopDomain");
		if (secGroupDeleteTopDomain != null) {
			secGroupDeleteTopDomainID = secGroupDeleteTopDomain.getRequiredSecGroupId();
			secGroupDeleteTopDomainMembSysadmin = ICFSecSchema.getBackingCFSec().getTableSecGrpMemb().readDerivedByUUserIdx(auth, ICFSecSchema.getSysClusterId(), secGroupDeleteTopDomainID, ICFSecSchema.getSysAdminId());
			if (secGroupDeleteTopDomainMembSysadmin != null) {
				secGroupDeleteTopDomainMembSysadminID = secGroupDeleteTopDomainMembSysadmin.getRequiredSecGrpMembId();
			}
			else {
				secGroupDeleteTopDomainMembSysadminID = null;
			}
		}
		else {
			secGroupDeleteTopDomainID = null;
			secGroupDeleteTopDomainMembSysadmin = null;
			secGroupDeleteTopDomainMembSysadminID = null;
		}


		if (secGroupCreateTopDomainID == null || secGroupCreateTopDomainID.isNull()) {
			secGroupCreateTopDomainID = new CFLibDbKeyHash256(0);
		}
		if (secGroupCreateTopDomainMembSysadminID == null || secGroupCreateTopDomainMembSysadminID.isNull()) {
			secGroupCreateTopDomainMembSysadminID = new CFLibDbKeyHash256(0);
		}
		if (secGroupReadTopDomainID == null || secGroupReadTopDomainID.isNull()) {
			secGroupReadTopDomainID = new CFLibDbKeyHash256(0);
		}
		if (secGroupReadTopDomainMembSysadminID == null || secGroupReadTopDomainMembSysadminID.isNull()) {
			secGroupReadTopDomainMembSysadminID = new CFLibDbKeyHash256(0);
		}
		if (secGroupUpdateTopDomainID == null || secGroupUpdateTopDomainID.isNull()) {
			secGroupUpdateTopDomainID = new CFLibDbKeyHash256(0);
		}
		if (secGroupUpdateTopDomainMembSysadminID == null || secGroupUpdateTopDomainMembSysadminID.isNull()) {
			secGroupUpdateTopDomainMembSysadminID = new CFLibDbKeyHash256(0);
		}
		if (secGroupDeleteTopDomainID == null || secGroupDeleteTopDomainID.isNull()) {
			secGroupDeleteTopDomainID = new CFLibDbKeyHash256(0);
		}
		if (secGroupDeleteTopDomainMembSysadminID == null || secGroupDeleteTopDomainMembSysadminID.isNull()) {
			secGroupDeleteTopDomainMembSysadminID = new CFLibDbKeyHash256(0);
		}

		if (secGroupCreateTopDomain == null) {
			secGroupCreateTopDomain = ICFSecSchema.getBackingCFSec().getFactorySecGroup().newRec();
			secGroupCreateTopDomain.setRequiredRevision(1);
			secGroupCreateTopDomain.setRequiredContainerCluster(ICFSecSchema.getSysClusterId());
			secGroupCreateTopDomain.setRequiredName("CreateTopDomain");
			secGroupCreateTopDomain.setRequiredIsVisible(true);
			secGroupCreateTopDomain.setRequiredSecGroupId(secGroupCreateTopDomainID);
			secGroupCreateTopDomain = ICFSecSchema.getBackingCFSec().getTableSecGroup().createSecGroup(auth, secGroupCreateTopDomain);
			secGroupCreateTopDomainID = secGroupCreateTopDomain.getRequiredSecGroupId();
		}

		if (secGroupCreateTopDomainMembSysadmin == null) {
			secGroupCreateTopDomainMembSysadmin = ICFSecSchema.getBackingCFSec().getFactorySecGrpMemb().newRec();
			secGroupCreateTopDomainMembSysadmin.setRequiredRevision(1);
			secGroupCreateTopDomainMembSysadmin.setRequiredOwnerCluster(ICFSecSchema.getSysClusterId());
			secGroupCreateTopDomainMembSysadmin.setRequiredContainerGroup(secGroupCreateTopDomainID);
			secGroupCreateTopDomainMembSysadmin.setRequiredParentUser(ICFSecSchema.getSysAdminId());
			secGroupCreateTopDomainMembSysadmin.setRequiredSecGrpMembId(secGroupCreateTopDomainMembSysadminID);
			secGroupCreateTopDomainMembSysadmin = ICFSecSchema.getBackingCFSec().getTableSecGrpMemb().createSecGrpMemb(auth, secGroupCreateTopDomainMembSysadmin);
			secGroupCreateTopDomainMembSysadminID = secGroupCreateTopDomainMembSysadmin.getRequiredSecGrpMembId();
		}

		if (secGroupReadTopDomain == null) {
			secGroupReadTopDomain = ICFSecSchema.getBackingCFSec().getFactorySecGroup().newRec();
			secGroupReadTopDomain.setRequiredRevision(1);
			secGroupReadTopDomain.setRequiredContainerCluster(ICFSecSchema.getSysClusterId());
			secGroupReadTopDomain.setRequiredName("ReadTopDomain");
			secGroupReadTopDomain.setRequiredIsVisible(true);
			secGroupReadTopDomain.setRequiredSecGroupId(secGroupReadTopDomainID);
			secGroupReadTopDomain = ICFSecSchema.getBackingCFSec().getTableSecGroup().createSecGroup(auth, secGroupReadTopDomain);
			secGroupReadTopDomainID = secGroupReadTopDomain.getRequiredSecGroupId();
		}

		if (secGroupReadTopDomainMembSysadmin == null) {
			secGroupReadTopDomainMembSysadmin = ICFSecSchema.getBackingCFSec().getFactorySecGrpMemb().newRec();
			secGroupReadTopDomainMembSysadmin.setRequiredRevision(1);
			secGroupReadTopDomainMembSysadmin.setRequiredOwnerCluster(ICFSecSchema.getSysClusterId());
			secGroupReadTopDomainMembSysadmin.setRequiredContainerGroup(secGroupReadTopDomainID);
			secGroupReadTopDomainMembSysadmin.setRequiredParentUser(ICFSecSchema.getSysAdminId());
			secGroupReadTopDomainMembSysadmin.setRequiredSecGrpMembId(secGroupReadTopDomainMembSysadminID);
			secGroupReadTopDomainMembSysadmin = ICFSecSchema.getBackingCFSec().getTableSecGrpMemb().createSecGrpMemb(auth, secGroupReadTopDomainMembSysadmin);
			secGroupReadTopDomainMembSysadminID = secGroupReadTopDomainMembSysadmin.getRequiredSecGrpMembId();
		}

		if (secGroupUpdateTopDomain == null) {
			secGroupUpdateTopDomain = ICFSecSchema.getBackingCFSec().getFactorySecGroup().newRec();
			secGroupUpdateTopDomain.setRequiredRevision(1);
			secGroupUpdateTopDomain.setRequiredContainerCluster(ICFSecSchema.getSysClusterId());
			secGroupUpdateTopDomain.setRequiredName("UpdateTopDomain");
			secGroupUpdateTopDomain.setRequiredIsVisible(true);
			secGroupUpdateTopDomain.setRequiredSecGroupId(secGroupUpdateTopDomainID);
			secGroupUpdateTopDomain = ICFSecSchema.getBackingCFSec().getTableSecGroup().createSecGroup(auth, secGroupUpdateTopDomain);
			secGroupUpdateTopDomainID = secGroupUpdateTopDomain.getRequiredSecGroupId();
		}

		if (secGroupUpdateTopDomainMembSysadmin == null) {
			secGroupUpdateTopDomainMembSysadmin = ICFSecSchema.getBackingCFSec().getFactorySecGrpMemb().newRec();
			secGroupUpdateTopDomainMembSysadmin.setRequiredRevision(1);
			secGroupUpdateTopDomainMembSysadmin.setRequiredOwnerCluster(ICFSecSchema.getSysClusterId());
			secGroupUpdateTopDomainMembSysadmin.setRequiredContainerGroup(secGroupUpdateTopDomainID);
			secGroupUpdateTopDomainMembSysadmin.setRequiredParentUser(ICFSecSchema.getSysAdminId());
			secGroupUpdateTopDomainMembSysadmin.setRequiredSecGrpMembId(secGroupUpdateTopDomainMembSysadminID);
			secGroupUpdateTopDomainMembSysadmin = ICFSecSchema.getBackingCFSec().getTableSecGrpMemb().createSecGrpMemb(auth, secGroupUpdateTopDomainMembSysadmin);
			secGroupUpdateTopDomainMembSysadminID = secGroupUpdateTopDomainMembSysadmin.getRequiredSecGrpMembId();
		}

		if (secGroupDeleteTopDomain == null) {
			secGroupDeleteTopDomain = ICFSecSchema.getBackingCFSec().getFactorySecGroup().newRec();
			secGroupDeleteTopDomain.setRequiredRevision(1);
			secGroupDeleteTopDomain.setRequiredContainerCluster(ICFSecSchema.getSysClusterId());
			secGroupDeleteTopDomain.setRequiredName("DeleteTopDomain");
			secGroupDeleteTopDomain.setRequiredIsVisible(true);
			secGroupDeleteTopDomain.setRequiredSecGroupId(secGroupDeleteTopDomainID);
			secGroupDeleteTopDomain = ICFSecSchema.getBackingCFSec().getTableSecGroup().createSecGroup(auth, secGroupDeleteTopDomain);
			secGroupDeleteTopDomainID = secGroupDeleteTopDomain.getRequiredSecGroupId();
		}

		if (secGroupDeleteTopDomainMembSysadmin == null) {
			secGroupDeleteTopDomainMembSysadmin = ICFSecSchema.getBackingCFSec().getFactorySecGrpMemb().newRec();
			secGroupDeleteTopDomainMembSysadmin.setRequiredRevision(1);
			secGroupDeleteTopDomainMembSysadmin.setRequiredOwnerCluster(ICFSecSchema.getSysClusterId());
			secGroupDeleteTopDomainMembSysadmin.setRequiredContainerGroup(secGroupDeleteTopDomainID);
			secGroupDeleteTopDomainMembSysadmin.setRequiredParentUser(ICFSecSchema.getSysAdminId());
			secGroupDeleteTopDomainMembSysadmin.setRequiredSecGrpMembId(secGroupDeleteTopDomainMembSysadminID);
			secGroupDeleteTopDomainMembSysadmin = ICFSecSchema.getBackingCFSec().getTableSecGrpMemb().createSecGrpMemb(auth, secGroupDeleteTopDomainMembSysadmin);
			secGroupDeleteTopDomainMembSysadminID = secGroupDeleteTopDomainMembSysadmin.getRequiredSecGrpMembId();
		}

	}		

	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = NoResultException.class, transactionManager = "cfint31TransactionManager")
	public void bootstrapTableTopProjectSecurity(ICFSecAuthorization auth) {
		LocalDateTime now = LocalDateTime.now();

		ICFSecSecGroup secGroupCreateTopProject;
		CFLibDbKeyHash256 secGroupCreateTopProjectID;
		ICFSecSecGrpMemb secGroupCreateTopProjectMembSysadmin;
		CFLibDbKeyHash256 secGroupCreateTopProjectMembSysadminID;
		ICFSecSecGroup secGroupReadTopProject;
		CFLibDbKeyHash256 secGroupReadTopProjectID;
		ICFSecSecGrpMemb secGroupReadTopProjectMembSysadmin;
		CFLibDbKeyHash256 secGroupReadTopProjectMembSysadminID;
		ICFSecSecGroup secGroupUpdateTopProject;
		CFLibDbKeyHash256 secGroupUpdateTopProjectID;
		ICFSecSecGrpMemb secGroupUpdateTopProjectMembSysadmin;
		CFLibDbKeyHash256 secGroupUpdateTopProjectMembSysadminID;
		ICFSecSecGroup secGroupDeleteTopProject;
		CFLibDbKeyHash256 secGroupDeleteTopProjectID;
		ICFSecSecGrpMemb secGroupDeleteTopProjectMembSysadmin;
		CFLibDbKeyHash256 secGroupDeleteTopProjectMembSysadminID;

		secGroupCreateTopProject = ICFSecSchema.getBackingCFSec().getTableSecGroup().readDerivedByUNameIdx(auth, ICFSecSchema.getSysClusterId(), "CreateTopProject");
		if (secGroupCreateTopProject != null) {
			secGroupCreateTopProjectID = secGroupCreateTopProject.getRequiredSecGroupId();
			secGroupCreateTopProjectMembSysadmin = ICFSecSchema.getBackingCFSec().getTableSecGrpMemb().readDerivedByUUserIdx(auth, ICFSecSchema.getSysClusterId(), secGroupCreateTopProjectID, ICFSecSchema.getSysAdminId());
			if (secGroupCreateTopProjectMembSysadmin != null) {
				secGroupCreateTopProjectMembSysadminID = secGroupCreateTopProjectMembSysadmin.getRequiredSecGrpMembId();
			}
			else {
				secGroupCreateTopProjectMembSysadminID = null;
			}
		}
		else {
			secGroupCreateTopProjectID = null;
			secGroupCreateTopProjectMembSysadmin = null;
			secGroupCreateTopProjectMembSysadminID = null;
		}

		secGroupReadTopProject = ICFSecSchema.getBackingCFSec().getTableSecGroup().readDerivedByUNameIdx(auth, ICFSecSchema.getSysClusterId(), "ReadTopProject");
		if (secGroupReadTopProject != null) {
			secGroupReadTopProjectID = secGroupReadTopProject.getRequiredSecGroupId();
			secGroupReadTopProjectMembSysadmin = ICFSecSchema.getBackingCFSec().getTableSecGrpMemb().readDerivedByUUserIdx(auth, ICFSecSchema.getSysClusterId(), secGroupReadTopProjectID, ICFSecSchema.getSysAdminId());
			if (secGroupReadTopProjectMembSysadmin != null) {
				secGroupReadTopProjectMembSysadminID = secGroupReadTopProjectMembSysadmin.getRequiredSecGrpMembId();
			}
			else {
				secGroupReadTopProjectMembSysadminID = null;
			}
		}
		else {
			secGroupReadTopProjectID = null;
			secGroupReadTopProjectMembSysadmin = null;
			secGroupReadTopProjectMembSysadminID = null;
		}

		secGroupUpdateTopProject = ICFSecSchema.getBackingCFSec().getTableSecGroup().readDerivedByUNameIdx(auth, ICFSecSchema.getSysClusterId(), "UpdateTopProject");
		if (secGroupUpdateTopProject != null) {
			secGroupUpdateTopProjectID = secGroupUpdateTopProject.getRequiredSecGroupId();
			secGroupUpdateTopProjectMembSysadmin = ICFSecSchema.getBackingCFSec().getTableSecGrpMemb().readDerivedByUUserIdx(auth, ICFSecSchema.getSysClusterId(), secGroupUpdateTopProjectID, ICFSecSchema.getSysAdminId());
			if (secGroupUpdateTopProjectMembSysadmin != null) {
				secGroupUpdateTopProjectMembSysadminID = secGroupUpdateTopProjectMembSysadmin.getRequiredSecGrpMembId();
			}
			else {
				secGroupUpdateTopProjectMembSysadminID = null;
			}
		}
		else {
			secGroupUpdateTopProjectID = null;
			secGroupUpdateTopProjectMembSysadmin = null;
			secGroupUpdateTopProjectMembSysadminID = null;
		}

		secGroupDeleteTopProject = ICFSecSchema.getBackingCFSec().getTableSecGroup().readDerivedByUNameIdx(auth, ICFSecSchema.getSysClusterId(), "DeleteTopProject");
		if (secGroupDeleteTopProject != null) {
			secGroupDeleteTopProjectID = secGroupDeleteTopProject.getRequiredSecGroupId();
			secGroupDeleteTopProjectMembSysadmin = ICFSecSchema.getBackingCFSec().getTableSecGrpMemb().readDerivedByUUserIdx(auth, ICFSecSchema.getSysClusterId(), secGroupDeleteTopProjectID, ICFSecSchema.getSysAdminId());
			if (secGroupDeleteTopProjectMembSysadmin != null) {
				secGroupDeleteTopProjectMembSysadminID = secGroupDeleteTopProjectMembSysadmin.getRequiredSecGrpMembId();
			}
			else {
				secGroupDeleteTopProjectMembSysadminID = null;
			}
		}
		else {
			secGroupDeleteTopProjectID = null;
			secGroupDeleteTopProjectMembSysadmin = null;
			secGroupDeleteTopProjectMembSysadminID = null;
		}


		if (secGroupCreateTopProjectID == null || secGroupCreateTopProjectID.isNull()) {
			secGroupCreateTopProjectID = new CFLibDbKeyHash256(0);
		}
		if (secGroupCreateTopProjectMembSysadminID == null || secGroupCreateTopProjectMembSysadminID.isNull()) {
			secGroupCreateTopProjectMembSysadminID = new CFLibDbKeyHash256(0);
		}
		if (secGroupReadTopProjectID == null || secGroupReadTopProjectID.isNull()) {
			secGroupReadTopProjectID = new CFLibDbKeyHash256(0);
		}
		if (secGroupReadTopProjectMembSysadminID == null || secGroupReadTopProjectMembSysadminID.isNull()) {
			secGroupReadTopProjectMembSysadminID = new CFLibDbKeyHash256(0);
		}
		if (secGroupUpdateTopProjectID == null || secGroupUpdateTopProjectID.isNull()) {
			secGroupUpdateTopProjectID = new CFLibDbKeyHash256(0);
		}
		if (secGroupUpdateTopProjectMembSysadminID == null || secGroupUpdateTopProjectMembSysadminID.isNull()) {
			secGroupUpdateTopProjectMembSysadminID = new CFLibDbKeyHash256(0);
		}
		if (secGroupDeleteTopProjectID == null || secGroupDeleteTopProjectID.isNull()) {
			secGroupDeleteTopProjectID = new CFLibDbKeyHash256(0);
		}
		if (secGroupDeleteTopProjectMembSysadminID == null || secGroupDeleteTopProjectMembSysadminID.isNull()) {
			secGroupDeleteTopProjectMembSysadminID = new CFLibDbKeyHash256(0);
		}

		if (secGroupCreateTopProject == null) {
			secGroupCreateTopProject = ICFSecSchema.getBackingCFSec().getFactorySecGroup().newRec();
			secGroupCreateTopProject.setRequiredRevision(1);
			secGroupCreateTopProject.setRequiredContainerCluster(ICFSecSchema.getSysClusterId());
			secGroupCreateTopProject.setRequiredName("CreateTopProject");
			secGroupCreateTopProject.setRequiredIsVisible(true);
			secGroupCreateTopProject.setRequiredSecGroupId(secGroupCreateTopProjectID);
			secGroupCreateTopProject = ICFSecSchema.getBackingCFSec().getTableSecGroup().createSecGroup(auth, secGroupCreateTopProject);
			secGroupCreateTopProjectID = secGroupCreateTopProject.getRequiredSecGroupId();
		}

		if (secGroupCreateTopProjectMembSysadmin == null) {
			secGroupCreateTopProjectMembSysadmin = ICFSecSchema.getBackingCFSec().getFactorySecGrpMemb().newRec();
			secGroupCreateTopProjectMembSysadmin.setRequiredRevision(1);
			secGroupCreateTopProjectMembSysadmin.setRequiredOwnerCluster(ICFSecSchema.getSysClusterId());
			secGroupCreateTopProjectMembSysadmin.setRequiredContainerGroup(secGroupCreateTopProjectID);
			secGroupCreateTopProjectMembSysadmin.setRequiredParentUser(ICFSecSchema.getSysAdminId());
			secGroupCreateTopProjectMembSysadmin.setRequiredSecGrpMembId(secGroupCreateTopProjectMembSysadminID);
			secGroupCreateTopProjectMembSysadmin = ICFSecSchema.getBackingCFSec().getTableSecGrpMemb().createSecGrpMemb(auth, secGroupCreateTopProjectMembSysadmin);
			secGroupCreateTopProjectMembSysadminID = secGroupCreateTopProjectMembSysadmin.getRequiredSecGrpMembId();
		}

		if (secGroupReadTopProject == null) {
			secGroupReadTopProject = ICFSecSchema.getBackingCFSec().getFactorySecGroup().newRec();
			secGroupReadTopProject.setRequiredRevision(1);
			secGroupReadTopProject.setRequiredContainerCluster(ICFSecSchema.getSysClusterId());
			secGroupReadTopProject.setRequiredName("ReadTopProject");
			secGroupReadTopProject.setRequiredIsVisible(true);
			secGroupReadTopProject.setRequiredSecGroupId(secGroupReadTopProjectID);
			secGroupReadTopProject = ICFSecSchema.getBackingCFSec().getTableSecGroup().createSecGroup(auth, secGroupReadTopProject);
			secGroupReadTopProjectID = secGroupReadTopProject.getRequiredSecGroupId();
		}

		if (secGroupReadTopProjectMembSysadmin == null) {
			secGroupReadTopProjectMembSysadmin = ICFSecSchema.getBackingCFSec().getFactorySecGrpMemb().newRec();
			secGroupReadTopProjectMembSysadmin.setRequiredRevision(1);
			secGroupReadTopProjectMembSysadmin.setRequiredOwnerCluster(ICFSecSchema.getSysClusterId());
			secGroupReadTopProjectMembSysadmin.setRequiredContainerGroup(secGroupReadTopProjectID);
			secGroupReadTopProjectMembSysadmin.setRequiredParentUser(ICFSecSchema.getSysAdminId());
			secGroupReadTopProjectMembSysadmin.setRequiredSecGrpMembId(secGroupReadTopProjectMembSysadminID);
			secGroupReadTopProjectMembSysadmin = ICFSecSchema.getBackingCFSec().getTableSecGrpMemb().createSecGrpMemb(auth, secGroupReadTopProjectMembSysadmin);
			secGroupReadTopProjectMembSysadminID = secGroupReadTopProjectMembSysadmin.getRequiredSecGrpMembId();
		}

		if (secGroupUpdateTopProject == null) {
			secGroupUpdateTopProject = ICFSecSchema.getBackingCFSec().getFactorySecGroup().newRec();
			secGroupUpdateTopProject.setRequiredRevision(1);
			secGroupUpdateTopProject.setRequiredContainerCluster(ICFSecSchema.getSysClusterId());
			secGroupUpdateTopProject.setRequiredName("UpdateTopProject");
			secGroupUpdateTopProject.setRequiredIsVisible(true);
			secGroupUpdateTopProject.setRequiredSecGroupId(secGroupUpdateTopProjectID);
			secGroupUpdateTopProject = ICFSecSchema.getBackingCFSec().getTableSecGroup().createSecGroup(auth, secGroupUpdateTopProject);
			secGroupUpdateTopProjectID = secGroupUpdateTopProject.getRequiredSecGroupId();
		}

		if (secGroupUpdateTopProjectMembSysadmin == null) {
			secGroupUpdateTopProjectMembSysadmin = ICFSecSchema.getBackingCFSec().getFactorySecGrpMemb().newRec();
			secGroupUpdateTopProjectMembSysadmin.setRequiredRevision(1);
			secGroupUpdateTopProjectMembSysadmin.setRequiredOwnerCluster(ICFSecSchema.getSysClusterId());
			secGroupUpdateTopProjectMembSysadmin.setRequiredContainerGroup(secGroupUpdateTopProjectID);
			secGroupUpdateTopProjectMembSysadmin.setRequiredParentUser(ICFSecSchema.getSysAdminId());
			secGroupUpdateTopProjectMembSysadmin.setRequiredSecGrpMembId(secGroupUpdateTopProjectMembSysadminID);
			secGroupUpdateTopProjectMembSysadmin = ICFSecSchema.getBackingCFSec().getTableSecGrpMemb().createSecGrpMemb(auth, secGroupUpdateTopProjectMembSysadmin);
			secGroupUpdateTopProjectMembSysadminID = secGroupUpdateTopProjectMembSysadmin.getRequiredSecGrpMembId();
		}

		if (secGroupDeleteTopProject == null) {
			secGroupDeleteTopProject = ICFSecSchema.getBackingCFSec().getFactorySecGroup().newRec();
			secGroupDeleteTopProject.setRequiredRevision(1);
			secGroupDeleteTopProject.setRequiredContainerCluster(ICFSecSchema.getSysClusterId());
			secGroupDeleteTopProject.setRequiredName("DeleteTopProject");
			secGroupDeleteTopProject.setRequiredIsVisible(true);
			secGroupDeleteTopProject.setRequiredSecGroupId(secGroupDeleteTopProjectID);
			secGroupDeleteTopProject = ICFSecSchema.getBackingCFSec().getTableSecGroup().createSecGroup(auth, secGroupDeleteTopProject);
			secGroupDeleteTopProjectID = secGroupDeleteTopProject.getRequiredSecGroupId();
		}

		if (secGroupDeleteTopProjectMembSysadmin == null) {
			secGroupDeleteTopProjectMembSysadmin = ICFSecSchema.getBackingCFSec().getFactorySecGrpMemb().newRec();
			secGroupDeleteTopProjectMembSysadmin.setRequiredRevision(1);
			secGroupDeleteTopProjectMembSysadmin.setRequiredOwnerCluster(ICFSecSchema.getSysClusterId());
			secGroupDeleteTopProjectMembSysadmin.setRequiredContainerGroup(secGroupDeleteTopProjectID);
			secGroupDeleteTopProjectMembSysadmin.setRequiredParentUser(ICFSecSchema.getSysAdminId());
			secGroupDeleteTopProjectMembSysadmin.setRequiredSecGrpMembId(secGroupDeleteTopProjectMembSysadminID);
			secGroupDeleteTopProjectMembSysadmin = ICFSecSchema.getBackingCFSec().getTableSecGrpMemb().createSecGrpMemb(auth, secGroupDeleteTopProjectMembSysadmin);
			secGroupDeleteTopProjectMembSysadminID = secGroupDeleteTopProjectMembSysadmin.getRequiredSecGrpMembId();
		}

	}		

	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = NoResultException.class, transactionManager = "cfint31TransactionManager")
	public void bootstrapTableURLProtocolSecurity(ICFSecAuthorization auth) {
		LocalDateTime now = LocalDateTime.now();

		ICFSecSecGroup secGroupCreateURLProtocol;
		CFLibDbKeyHash256 secGroupCreateURLProtocolID;
		ICFSecSecGrpMemb secGroupCreateURLProtocolMembSysadmin;
		CFLibDbKeyHash256 secGroupCreateURLProtocolMembSysadminID;
		ICFSecSecGroup secGroupReadURLProtocol;
		CFLibDbKeyHash256 secGroupReadURLProtocolID;
		ICFSecSecGrpMemb secGroupReadURLProtocolMembSysadmin;
		CFLibDbKeyHash256 secGroupReadURLProtocolMembSysadminID;
		ICFSecSecGroup secGroupUpdateURLProtocol;
		CFLibDbKeyHash256 secGroupUpdateURLProtocolID;
		ICFSecSecGrpMemb secGroupUpdateURLProtocolMembSysadmin;
		CFLibDbKeyHash256 secGroupUpdateURLProtocolMembSysadminID;
		ICFSecSecGroup secGroupDeleteURLProtocol;
		CFLibDbKeyHash256 secGroupDeleteURLProtocolID;
		ICFSecSecGrpMemb secGroupDeleteURLProtocolMembSysadmin;
		CFLibDbKeyHash256 secGroupDeleteURLProtocolMembSysadminID;

		secGroupCreateURLProtocol = ICFSecSchema.getBackingCFSec().getTableSecGroup().readDerivedByUNameIdx(auth, ICFSecSchema.getSysClusterId(), "CreateURLProtocol");
		if (secGroupCreateURLProtocol != null) {
			secGroupCreateURLProtocolID = secGroupCreateURLProtocol.getRequiredSecGroupId();
			secGroupCreateURLProtocolMembSysadmin = ICFSecSchema.getBackingCFSec().getTableSecGrpMemb().readDerivedByUUserIdx(auth, ICFSecSchema.getSysClusterId(), secGroupCreateURLProtocolID, ICFSecSchema.getSysAdminId());
			if (secGroupCreateURLProtocolMembSysadmin != null) {
				secGroupCreateURLProtocolMembSysadminID = secGroupCreateURLProtocolMembSysadmin.getRequiredSecGrpMembId();
			}
			else {
				secGroupCreateURLProtocolMembSysadminID = null;
			}
		}
		else {
			secGroupCreateURLProtocolID = null;
			secGroupCreateURLProtocolMembSysadmin = null;
			secGroupCreateURLProtocolMembSysadminID = null;
		}

		secGroupReadURLProtocol = ICFSecSchema.getBackingCFSec().getTableSecGroup().readDerivedByUNameIdx(auth, ICFSecSchema.getSysClusterId(), "ReadURLProtocol");
		if (secGroupReadURLProtocol != null) {
			secGroupReadURLProtocolID = secGroupReadURLProtocol.getRequiredSecGroupId();
			secGroupReadURLProtocolMembSysadmin = ICFSecSchema.getBackingCFSec().getTableSecGrpMemb().readDerivedByUUserIdx(auth, ICFSecSchema.getSysClusterId(), secGroupReadURLProtocolID, ICFSecSchema.getSysAdminId());
			if (secGroupReadURLProtocolMembSysadmin != null) {
				secGroupReadURLProtocolMembSysadminID = secGroupReadURLProtocolMembSysadmin.getRequiredSecGrpMembId();
			}
			else {
				secGroupReadURLProtocolMembSysadminID = null;
			}
		}
		else {
			secGroupReadURLProtocolID = null;
			secGroupReadURLProtocolMembSysadmin = null;
			secGroupReadURLProtocolMembSysadminID = null;
		}

		secGroupUpdateURLProtocol = ICFSecSchema.getBackingCFSec().getTableSecGroup().readDerivedByUNameIdx(auth, ICFSecSchema.getSysClusterId(), "UpdateURLProtocol");
		if (secGroupUpdateURLProtocol != null) {
			secGroupUpdateURLProtocolID = secGroupUpdateURLProtocol.getRequiredSecGroupId();
			secGroupUpdateURLProtocolMembSysadmin = ICFSecSchema.getBackingCFSec().getTableSecGrpMemb().readDerivedByUUserIdx(auth, ICFSecSchema.getSysClusterId(), secGroupUpdateURLProtocolID, ICFSecSchema.getSysAdminId());
			if (secGroupUpdateURLProtocolMembSysadmin != null) {
				secGroupUpdateURLProtocolMembSysadminID = secGroupUpdateURLProtocolMembSysadmin.getRequiredSecGrpMembId();
			}
			else {
				secGroupUpdateURLProtocolMembSysadminID = null;
			}
		}
		else {
			secGroupUpdateURLProtocolID = null;
			secGroupUpdateURLProtocolMembSysadmin = null;
			secGroupUpdateURLProtocolMembSysadminID = null;
		}

		secGroupDeleteURLProtocol = ICFSecSchema.getBackingCFSec().getTableSecGroup().readDerivedByUNameIdx(auth, ICFSecSchema.getSysClusterId(), "DeleteURLProtocol");
		if (secGroupDeleteURLProtocol != null) {
			secGroupDeleteURLProtocolID = secGroupDeleteURLProtocol.getRequiredSecGroupId();
			secGroupDeleteURLProtocolMembSysadmin = ICFSecSchema.getBackingCFSec().getTableSecGrpMemb().readDerivedByUUserIdx(auth, ICFSecSchema.getSysClusterId(), secGroupDeleteURLProtocolID, ICFSecSchema.getSysAdminId());
			if (secGroupDeleteURLProtocolMembSysadmin != null) {
				secGroupDeleteURLProtocolMembSysadminID = secGroupDeleteURLProtocolMembSysadmin.getRequiredSecGrpMembId();
			}
			else {
				secGroupDeleteURLProtocolMembSysadminID = null;
			}
		}
		else {
			secGroupDeleteURLProtocolID = null;
			secGroupDeleteURLProtocolMembSysadmin = null;
			secGroupDeleteURLProtocolMembSysadminID = null;
		}


		if (secGroupCreateURLProtocolID == null || secGroupCreateURLProtocolID.isNull()) {
			secGroupCreateURLProtocolID = new CFLibDbKeyHash256(0);
		}
		if (secGroupCreateURLProtocolMembSysadminID == null || secGroupCreateURLProtocolMembSysadminID.isNull()) {
			secGroupCreateURLProtocolMembSysadminID = new CFLibDbKeyHash256(0);
		}
		if (secGroupReadURLProtocolID == null || secGroupReadURLProtocolID.isNull()) {
			secGroupReadURLProtocolID = new CFLibDbKeyHash256(0);
		}
		if (secGroupReadURLProtocolMembSysadminID == null || secGroupReadURLProtocolMembSysadminID.isNull()) {
			secGroupReadURLProtocolMembSysadminID = new CFLibDbKeyHash256(0);
		}
		if (secGroupUpdateURLProtocolID == null || secGroupUpdateURLProtocolID.isNull()) {
			secGroupUpdateURLProtocolID = new CFLibDbKeyHash256(0);
		}
		if (secGroupUpdateURLProtocolMembSysadminID == null || secGroupUpdateURLProtocolMembSysadminID.isNull()) {
			secGroupUpdateURLProtocolMembSysadminID = new CFLibDbKeyHash256(0);
		}
		if (secGroupDeleteURLProtocolID == null || secGroupDeleteURLProtocolID.isNull()) {
			secGroupDeleteURLProtocolID = new CFLibDbKeyHash256(0);
		}
		if (secGroupDeleteURLProtocolMembSysadminID == null || secGroupDeleteURLProtocolMembSysadminID.isNull()) {
			secGroupDeleteURLProtocolMembSysadminID = new CFLibDbKeyHash256(0);
		}

		if (secGroupCreateURLProtocol == null) {
			secGroupCreateURLProtocol = ICFSecSchema.getBackingCFSec().getFactorySecGroup().newRec();
			secGroupCreateURLProtocol.setRequiredRevision(1);
			secGroupCreateURLProtocol.setRequiredContainerCluster(ICFSecSchema.getSysClusterId());
			secGroupCreateURLProtocol.setRequiredName("CreateURLProtocol");
			secGroupCreateURLProtocol.setRequiredIsVisible(true);
			secGroupCreateURLProtocol.setRequiredSecGroupId(secGroupCreateURLProtocolID);
			secGroupCreateURLProtocol = ICFSecSchema.getBackingCFSec().getTableSecGroup().createSecGroup(auth, secGroupCreateURLProtocol);
			secGroupCreateURLProtocolID = secGroupCreateURLProtocol.getRequiredSecGroupId();
		}

		if (secGroupCreateURLProtocolMembSysadmin == null) {
			secGroupCreateURLProtocolMembSysadmin = ICFSecSchema.getBackingCFSec().getFactorySecGrpMemb().newRec();
			secGroupCreateURLProtocolMembSysadmin.setRequiredRevision(1);
			secGroupCreateURLProtocolMembSysadmin.setRequiredOwnerCluster(ICFSecSchema.getSysClusterId());
			secGroupCreateURLProtocolMembSysadmin.setRequiredContainerGroup(secGroupCreateURLProtocolID);
			secGroupCreateURLProtocolMembSysadmin.setRequiredParentUser(ICFSecSchema.getSysAdminId());
			secGroupCreateURLProtocolMembSysadmin.setRequiredSecGrpMembId(secGroupCreateURLProtocolMembSysadminID);
			secGroupCreateURLProtocolMembSysadmin = ICFSecSchema.getBackingCFSec().getTableSecGrpMemb().createSecGrpMemb(auth, secGroupCreateURLProtocolMembSysadmin);
			secGroupCreateURLProtocolMembSysadminID = secGroupCreateURLProtocolMembSysadmin.getRequiredSecGrpMembId();
		}

		if (secGroupReadURLProtocol == null) {
			secGroupReadURLProtocol = ICFSecSchema.getBackingCFSec().getFactorySecGroup().newRec();
			secGroupReadURLProtocol.setRequiredRevision(1);
			secGroupReadURLProtocol.setRequiredContainerCluster(ICFSecSchema.getSysClusterId());
			secGroupReadURLProtocol.setRequiredName("ReadURLProtocol");
			secGroupReadURLProtocol.setRequiredIsVisible(true);
			secGroupReadURLProtocol.setRequiredSecGroupId(secGroupReadURLProtocolID);
			secGroupReadURLProtocol = ICFSecSchema.getBackingCFSec().getTableSecGroup().createSecGroup(auth, secGroupReadURLProtocol);
			secGroupReadURLProtocolID = secGroupReadURLProtocol.getRequiredSecGroupId();
		}

		if (secGroupReadURLProtocolMembSysadmin == null) {
			secGroupReadURLProtocolMembSysadmin = ICFSecSchema.getBackingCFSec().getFactorySecGrpMemb().newRec();
			secGroupReadURLProtocolMembSysadmin.setRequiredRevision(1);
			secGroupReadURLProtocolMembSysadmin.setRequiredOwnerCluster(ICFSecSchema.getSysClusterId());
			secGroupReadURLProtocolMembSysadmin.setRequiredContainerGroup(secGroupReadURLProtocolID);
			secGroupReadURLProtocolMembSysadmin.setRequiredParentUser(ICFSecSchema.getSysAdminId());
			secGroupReadURLProtocolMembSysadmin.setRequiredSecGrpMembId(secGroupReadURLProtocolMembSysadminID);
			secGroupReadURLProtocolMembSysadmin = ICFSecSchema.getBackingCFSec().getTableSecGrpMemb().createSecGrpMemb(auth, secGroupReadURLProtocolMembSysadmin);
			secGroupReadURLProtocolMembSysadminID = secGroupReadURLProtocolMembSysadmin.getRequiredSecGrpMembId();
		}

		if (secGroupUpdateURLProtocol == null) {
			secGroupUpdateURLProtocol = ICFSecSchema.getBackingCFSec().getFactorySecGroup().newRec();
			secGroupUpdateURLProtocol.setRequiredRevision(1);
			secGroupUpdateURLProtocol.setRequiredContainerCluster(ICFSecSchema.getSysClusterId());
			secGroupUpdateURLProtocol.setRequiredName("UpdateURLProtocol");
			secGroupUpdateURLProtocol.setRequiredIsVisible(true);
			secGroupUpdateURLProtocol.setRequiredSecGroupId(secGroupUpdateURLProtocolID);
			secGroupUpdateURLProtocol = ICFSecSchema.getBackingCFSec().getTableSecGroup().createSecGroup(auth, secGroupUpdateURLProtocol);
			secGroupUpdateURLProtocolID = secGroupUpdateURLProtocol.getRequiredSecGroupId();
		}

		if (secGroupUpdateURLProtocolMembSysadmin == null) {
			secGroupUpdateURLProtocolMembSysadmin = ICFSecSchema.getBackingCFSec().getFactorySecGrpMemb().newRec();
			secGroupUpdateURLProtocolMembSysadmin.setRequiredRevision(1);
			secGroupUpdateURLProtocolMembSysadmin.setRequiredOwnerCluster(ICFSecSchema.getSysClusterId());
			secGroupUpdateURLProtocolMembSysadmin.setRequiredContainerGroup(secGroupUpdateURLProtocolID);
			secGroupUpdateURLProtocolMembSysadmin.setRequiredParentUser(ICFSecSchema.getSysAdminId());
			secGroupUpdateURLProtocolMembSysadmin.setRequiredSecGrpMembId(secGroupUpdateURLProtocolMembSysadminID);
			secGroupUpdateURLProtocolMembSysadmin = ICFSecSchema.getBackingCFSec().getTableSecGrpMemb().createSecGrpMemb(auth, secGroupUpdateURLProtocolMembSysadmin);
			secGroupUpdateURLProtocolMembSysadminID = secGroupUpdateURLProtocolMembSysadmin.getRequiredSecGrpMembId();
		}

		if (secGroupDeleteURLProtocol == null) {
			secGroupDeleteURLProtocol = ICFSecSchema.getBackingCFSec().getFactorySecGroup().newRec();
			secGroupDeleteURLProtocol.setRequiredRevision(1);
			secGroupDeleteURLProtocol.setRequiredContainerCluster(ICFSecSchema.getSysClusterId());
			secGroupDeleteURLProtocol.setRequiredName("DeleteURLProtocol");
			secGroupDeleteURLProtocol.setRequiredIsVisible(true);
			secGroupDeleteURLProtocol.setRequiredSecGroupId(secGroupDeleteURLProtocolID);
			secGroupDeleteURLProtocol = ICFSecSchema.getBackingCFSec().getTableSecGroup().createSecGroup(auth, secGroupDeleteURLProtocol);
			secGroupDeleteURLProtocolID = secGroupDeleteURLProtocol.getRequiredSecGroupId();
		}

		if (secGroupDeleteURLProtocolMembSysadmin == null) {
			secGroupDeleteURLProtocolMembSysadmin = ICFSecSchema.getBackingCFSec().getFactorySecGrpMemb().newRec();
			secGroupDeleteURLProtocolMembSysadmin.setRequiredRevision(1);
			secGroupDeleteURLProtocolMembSysadmin.setRequiredOwnerCluster(ICFSecSchema.getSysClusterId());
			secGroupDeleteURLProtocolMembSysadmin.setRequiredContainerGroup(secGroupDeleteURLProtocolID);
			secGroupDeleteURLProtocolMembSysadmin.setRequiredParentUser(ICFSecSchema.getSysAdminId());
			secGroupDeleteURLProtocolMembSysadmin.setRequiredSecGrpMembId(secGroupDeleteURLProtocolMembSysadminID);
			secGroupDeleteURLProtocolMembSysadmin = ICFSecSchema.getBackingCFSec().getTableSecGrpMemb().createSecGrpMemb(auth, secGroupDeleteURLProtocolMembSysadmin);
			secGroupDeleteURLProtocolMembSysadminID = secGroupDeleteURLProtocolMembSysadmin.getRequiredSecGrpMembId();
		}

	}		


}
