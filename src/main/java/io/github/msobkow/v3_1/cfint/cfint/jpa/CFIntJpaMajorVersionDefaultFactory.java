
// Description: Java 25 JPA Default Factory implementation for MajorVersion.

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

import java.lang.reflect.*;
import java.net.*;
import java.rmi.*;
import java.sql.*;
import java.text.*;
import java.util.*;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.text.StringEscapeUtils;
import io.github.msobkow.v3_1.cflib.*;
import io.github.msobkow.v3_1.cflib.dbutil.*;
import io.github.msobkow.v3_1.cfsec.cfsec.*;
import io.github.msobkow.v3_1.cfint.cfint.*;
import io.github.msobkow.v3_1.cfsec.cfsec.jpa.*;

/*
 *	CFIntMajorVersionFactory JPA implementation for MajorVersion
 */
public class CFIntJpaMajorVersionDefaultFactory
    implements ICFIntMajorVersionFactory
{
    public CFIntJpaMajorVersionDefaultFactory() {
    }

    @Override
    public ICFIntMajorVersionHPKey newHPKey() {
        ICFIntMajorVersionHPKey hpkey =
            new CFIntJpaMajorVersionHPKey();
        return( hpkey );
    }

    @Override
    public ICFIntMajorVersionByTenantIdxKey newByTenantIdxKey() {
	ICFIntMajorVersionByTenantIdxKey key =
            new CFIntJpaMajorVersionByTenantIdxKey();
	return( key );
    }

    @Override
    public ICFIntMajorVersionBySubProjectIdxKey newBySubProjectIdxKey() {
	ICFIntMajorVersionBySubProjectIdxKey key =
            new CFIntJpaMajorVersionBySubProjectIdxKey();
	return( key );
    }

    @Override
    public ICFIntMajorVersionByNameIdxKey newByNameIdxKey() {
	ICFIntMajorVersionByNameIdxKey key =
            new CFIntJpaMajorVersionByNameIdxKey();
	return( key );
    }

    @Override
    public ICFIntMajorVersion newRec() {
        ICFIntMajorVersion rec =
            new CFIntJpaMajorVersion();
        return( rec );
    }

    @Override
    public ICFIntMajorVersionH newHRec() {
        ICFIntMajorVersionH hrec =
            new CFIntJpaMajorVersionH();
        return( hrec );
    }
}
