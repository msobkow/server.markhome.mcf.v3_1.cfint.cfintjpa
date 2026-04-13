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


	public void bootstrapSchema(CFSecTableInfo tableInfo[]) {
		ICFSecSchema.getBackingCFSec().bootstrapSchema(tableInfo);
	}

	public void bootstrapAllTablesSecurity(CFSecTableInfo tableInfo[]) {
		bootstrapAllTablesSecurity(ICFSecSchema.getSysClusterId(), ICFSecSchema.getSysTenantId(), tableInfo);
	}

	public void bootstrapAllTablesSecurity(CFLibDbKeyHash256 clusterId, CFLibDbKeyHash256 tenantId, CFSecTableInfo tableInfo[]) {
		ICFSecSchema.getBackingCFSec().bootstrapAllTablesSecurity(clusterId, tenantId, tableInfo);
	}


		// Customized schematweak [CFSec::CFInt].JpaSchemaServiceCustomServices
}
