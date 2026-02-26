// Description: Java 25 Spring JPA Hooks for CFInt

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
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import io.github.msobkow.v3_1.cfsec.cfsec.*;
import io.github.msobkow.v3_1.cfint.cfint.*;
import io.github.msobkow.v3_1.cfsec.cfsec.jpa.*;
import io.github.msobkow.v3_1.cfint.cfint.jpa.*;

/**
 *	Hooks for schema CFInt Spring resources that need to be used by getter-wrappers for AtomicReference members of the multi-threaded wedge between Spring resources and POJO code.
 *	This implementation wraps the Spring singletons instantiated during Spring's boot process for the io.github.msobkow.v3_1.cfint.cfint.jpa package.  It resolves all known standard Spring resources for the LocalContainerEntityManagerFactoryBean, SchemaService, IdGenService, and the CFInt*Repository and CFInt*Service singletons that the POJOs need to invoke.  However, it relies on late-initialization during dynamic instance creation (i.e. new CFIntJpaSchemaHooks()) instead of being initialized as a Spring singleton. As the Spring instance hierarchy is instantiated before any instances of this class are instantiated, the net result bridges the gap between POJO and Spring JPA.
 */
@Service("cfint31JpaHooksSchema")
public class CFIntJpaHooksSchema {

	@Autowired
	@Qualifier("cfint31EntityManagerFactory")
	private LocalContainerEntityManagerFactoryBean cfint31EntityManagerFactory;

	@Autowired
	private CFIntJpaLicenseRepository licenseRepository;

	@Autowired
	private CFIntJpaMajorVersionRepository majorVersionRepository;

	@Autowired
	private CFIntJpaMimeTypeRepository mimeTypeRepository;

	@Autowired
	private CFIntJpaMinorVersionRepository minorVersionRepository;

	@Autowired
	private CFIntJpaSubProjectRepository subProjectRepository;

	@Autowired
	private CFIntJpaTldRepository tldRepository;

	@Autowired
	private CFIntJpaTopDomainRepository topDomainRepository;

	@Autowired
	private CFIntJpaTopProjectRepository topProjectRepository;

	@Autowired
	private CFIntJpaURLProtocolRepository uRLProtocolRepository;

	@Autowired
	@Qualifier("cfint31JpaSchemaService")
	private CFIntJpaSchemaService schemaService;

	@Autowired
	@Qualifier("CFIntJpaIdGenService")
	private CFIntJpaIdGenService idGenService;

	@Autowired
	@Qualifier("cfint31JpaLicenseService")
	private CFIntJpaLicenseService licenseService;

	@Autowired
	@Qualifier("cfint31JpaMajorVersionService")
	private CFIntJpaMajorVersionService majorVersionService;

	@Autowired
	@Qualifier("cfint31JpaMimeTypeService")
	private CFIntJpaMimeTypeService mimeTypeService;

	@Autowired
	@Qualifier("cfint31JpaMinorVersionService")
	private CFIntJpaMinorVersionService minorVersionService;

	@Autowired
	@Qualifier("cfint31JpaSubProjectService")
	private CFIntJpaSubProjectService subProjectService;

	@Autowired
	@Qualifier("cfint31JpaTldService")
	private CFIntJpaTldService tldService;

	@Autowired
	@Qualifier("cfint31JpaTopDomainService")
	private CFIntJpaTopDomainService topDomainService;

	@Autowired
	@Qualifier("cfint31JpaTopProjectService")
	private CFIntJpaTopProjectService topProjectService;

	@Autowired
	@Qualifier("cfint31JpaURLProtocolService")
	private CFIntJpaURLProtocolService uRLProtocolService;

	public LocalContainerEntityManagerFactoryBean getEntityManagerFactoryBean() {
		if ( cfint31EntityManagerFactory == null ) {
			// Dynamically resolve the repository by qualifier name
			throw new CFLibNotImplementedYetException( getClass(), "getEntityManagerFactoryBean",
				"ERROR - do not know how to dynamically resolve Spring beans from POJO code yet and AspectJ did not resolve it either",
				"ERROR - do not know how to dynamically resolve Spring beans from POJO code yet and AspectJ did not resolve it either" );
		}
		return( cfint31EntityManagerFactory );
	}

	public CFIntJpaSchemaService getSchemaService() {
		if ( schemaService == null ) {
			// Dynamically resolve the repository by qualifier name
			throw new CFLibNotImplementedYetException( getClass(), "getSchemaService",
				"ERROR - do not know how to dynamically resolve Spring beans from POJO code yet and AspectJ did not resolve it either",
				"ERROR - do not know how to dynamically resolve Spring beans from POJO code yet and AspectJ did not resolve it either" );
		}
		return( schemaService );
	}

	public CFIntJpaIdGenService getIdGenService() {
		if ( idGenService == null ) {
			// Dynamically resolve the repository by qualifier name
			throw new CFLibNotImplementedYetException( getClass(), "getIdGenService",
				"ERROR - do not know how to dynamically resolve Spring beans from POJO code yet and AspectJ did not resolve it either",
				"ERROR - do not know how to dynamically resolve Spring beans from POJO code yet and AspectJ did not resolve it either" );
		}
		return( idGenService );
	}

	public CFIntJpaLicenseRepository getLicenseRepository() {
		if ( licenseRepository == null ) {
			// Dynamically resolve the repository by interface type
			throw new CFLibNotImplementedYetException( getClass(), "getLicenseRepository",
				"ERROR - do not know how to dynamically resolve Spring beans from POJO code yet and AspectJ did not resolve it either",
				"ERROR - do not know how to dynamically resolve Spring beans from POJO code yet and AspectJ did not resolve it either" );
		}
		return( licenseRepository );
	}

	public CFIntJpaMajorVersionRepository getMajorVersionRepository() {
		if ( majorVersionRepository == null ) {
			// Dynamically resolve the repository by interface type
			throw new CFLibNotImplementedYetException( getClass(), "getMajorVersionRepository",
				"ERROR - do not know how to dynamically resolve Spring beans from POJO code yet and AspectJ did not resolve it either",
				"ERROR - do not know how to dynamically resolve Spring beans from POJO code yet and AspectJ did not resolve it either" );
		}
		return( majorVersionRepository );
	}

	public CFIntJpaMimeTypeRepository getMimeTypeRepository() {
		if ( mimeTypeRepository == null ) {
			// Dynamically resolve the repository by interface type
			throw new CFLibNotImplementedYetException( getClass(), "getMimeTypeRepository",
				"ERROR - do not know how to dynamically resolve Spring beans from POJO code yet and AspectJ did not resolve it either",
				"ERROR - do not know how to dynamically resolve Spring beans from POJO code yet and AspectJ did not resolve it either" );
		}
		return( mimeTypeRepository );
	}

	public CFIntJpaMinorVersionRepository getMinorVersionRepository() {
		if ( minorVersionRepository == null ) {
			// Dynamically resolve the repository by interface type
			throw new CFLibNotImplementedYetException( getClass(), "getMinorVersionRepository",
				"ERROR - do not know how to dynamically resolve Spring beans from POJO code yet and AspectJ did not resolve it either",
				"ERROR - do not know how to dynamically resolve Spring beans from POJO code yet and AspectJ did not resolve it either" );
		}
		return( minorVersionRepository );
	}

	public CFIntJpaSubProjectRepository getSubProjectRepository() {
		if ( subProjectRepository == null ) {
			// Dynamically resolve the repository by interface type
			throw new CFLibNotImplementedYetException( getClass(), "getSubProjectRepository",
				"ERROR - do not know how to dynamically resolve Spring beans from POJO code yet and AspectJ did not resolve it either",
				"ERROR - do not know how to dynamically resolve Spring beans from POJO code yet and AspectJ did not resolve it either" );
		}
		return( subProjectRepository );
	}

	public CFIntJpaTldRepository getTldRepository() {
		if ( tldRepository == null ) {
			// Dynamically resolve the repository by interface type
			throw new CFLibNotImplementedYetException( getClass(), "getTldRepository",
				"ERROR - do not know how to dynamically resolve Spring beans from POJO code yet and AspectJ did not resolve it either",
				"ERROR - do not know how to dynamically resolve Spring beans from POJO code yet and AspectJ did not resolve it either" );
		}
		return( tldRepository );
	}

	public CFIntJpaTopDomainRepository getTopDomainRepository() {
		if ( topDomainRepository == null ) {
			// Dynamically resolve the repository by interface type
			throw new CFLibNotImplementedYetException( getClass(), "getTopDomainRepository",
				"ERROR - do not know how to dynamically resolve Spring beans from POJO code yet and AspectJ did not resolve it either",
				"ERROR - do not know how to dynamically resolve Spring beans from POJO code yet and AspectJ did not resolve it either" );
		}
		return( topDomainRepository );
	}

	public CFIntJpaTopProjectRepository getTopProjectRepository() {
		if ( topProjectRepository == null ) {
			// Dynamically resolve the repository by interface type
			throw new CFLibNotImplementedYetException( getClass(), "getTopProjectRepository",
				"ERROR - do not know how to dynamically resolve Spring beans from POJO code yet and AspectJ did not resolve it either",
				"ERROR - do not know how to dynamically resolve Spring beans from POJO code yet and AspectJ did not resolve it either" );
		}
		return( topProjectRepository );
	}

	public CFIntJpaURLProtocolRepository getURLProtocolRepository() {
		if ( uRLProtocolRepository == null ) {
			// Dynamically resolve the repository by interface type
			throw new CFLibNotImplementedYetException( getClass(), "getURLProtocolRepository",
				"ERROR - do not know how to dynamically resolve Spring beans from POJO code yet and AspectJ did not resolve it either",
				"ERROR - do not know how to dynamically resolve Spring beans from POJO code yet and AspectJ did not resolve it either" );
		}
		return( uRLProtocolRepository );
	}

	public CFIntJpaLicenseService getLicenseService() {
		if ( licenseService == null ) {
			// Dynamically resolve the repository by qualifier name
			throw new CFLibNotImplementedYetException( getClass(), "getLicenseService",
				"ERROR - do not know how to dynamically resolve Spring beans from POJO code yet and AspectJ did not resolve it either",
				"ERROR - do not know how to dynamically resolve Spring beans from POJO code yet and AspectJ did not resolve it either" );
		}
		return( licenseService );
	}

	public CFIntJpaMajorVersionService getMajorVersionService() {
		if ( majorVersionService == null ) {
			// Dynamically resolve the repository by qualifier name
			throw new CFLibNotImplementedYetException( getClass(), "getMajorVersionService",
				"ERROR - do not know how to dynamically resolve Spring beans from POJO code yet and AspectJ did not resolve it either",
				"ERROR - do not know how to dynamically resolve Spring beans from POJO code yet and AspectJ did not resolve it either" );
		}
		return( majorVersionService );
	}

	public CFIntJpaMimeTypeService getMimeTypeService() {
		if ( mimeTypeService == null ) {
			// Dynamically resolve the repository by qualifier name
			throw new CFLibNotImplementedYetException( getClass(), "getMimeTypeService",
				"ERROR - do not know how to dynamically resolve Spring beans from POJO code yet and AspectJ did not resolve it either",
				"ERROR - do not know how to dynamically resolve Spring beans from POJO code yet and AspectJ did not resolve it either" );
		}
		return( mimeTypeService );
	}

	public CFIntJpaMinorVersionService getMinorVersionService() {
		if ( minorVersionService == null ) {
			// Dynamically resolve the repository by qualifier name
			throw new CFLibNotImplementedYetException( getClass(), "getMinorVersionService",
				"ERROR - do not know how to dynamically resolve Spring beans from POJO code yet and AspectJ did not resolve it either",
				"ERROR - do not know how to dynamically resolve Spring beans from POJO code yet and AspectJ did not resolve it either" );
		}
		return( minorVersionService );
	}

	public CFIntJpaSubProjectService getSubProjectService() {
		if ( subProjectService == null ) {
			// Dynamically resolve the repository by qualifier name
			throw new CFLibNotImplementedYetException( getClass(), "getSubProjectService",
				"ERROR - do not know how to dynamically resolve Spring beans from POJO code yet and AspectJ did not resolve it either",
				"ERROR - do not know how to dynamically resolve Spring beans from POJO code yet and AspectJ did not resolve it either" );
		}
		return( subProjectService );
	}

	public CFIntJpaTldService getTldService() {
		if ( tldService == null ) {
			// Dynamically resolve the repository by qualifier name
			throw new CFLibNotImplementedYetException( getClass(), "getTldService",
				"ERROR - do not know how to dynamically resolve Spring beans from POJO code yet and AspectJ did not resolve it either",
				"ERROR - do not know how to dynamically resolve Spring beans from POJO code yet and AspectJ did not resolve it either" );
		}
		return( tldService );
	}

	public CFIntJpaTopDomainService getTopDomainService() {
		if ( topDomainService == null ) {
			// Dynamically resolve the repository by qualifier name
			throw new CFLibNotImplementedYetException( getClass(), "getTopDomainService",
				"ERROR - do not know how to dynamically resolve Spring beans from POJO code yet and AspectJ did not resolve it either",
				"ERROR - do not know how to dynamically resolve Spring beans from POJO code yet and AspectJ did not resolve it either" );
		}
		return( topDomainService );
	}

	public CFIntJpaTopProjectService getTopProjectService() {
		if ( topProjectService == null ) {
			// Dynamically resolve the repository by qualifier name
			throw new CFLibNotImplementedYetException( getClass(), "getTopProjectService",
				"ERROR - do not know how to dynamically resolve Spring beans from POJO code yet and AspectJ did not resolve it either",
				"ERROR - do not know how to dynamically resolve Spring beans from POJO code yet and AspectJ did not resolve it either" );
		}
		return( topProjectService );
	}

	public CFIntJpaURLProtocolService getURLProtocolService() {
		if ( uRLProtocolService == null ) {
			// Dynamically resolve the repository by qualifier name
			throw new CFLibNotImplementedYetException( getClass(), "getURLProtocolService",
				"ERROR - do not know how to dynamically resolve Spring beans from POJO code yet and AspectJ did not resolve it either",
				"ERROR - do not know how to dynamically resolve Spring beans from POJO code yet and AspectJ did not resolve it either" );
		}
		return( uRLProtocolService );
	}
}
