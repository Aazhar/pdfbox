/*****************************************************************************
 * 
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * 
 ****************************************************************************/

package org.apache.padaf.xmpbox.type;

import org.apache.padaf.xmpbox.XMPMetadata;
import org.apache.padaf.xmpbox.XmpConstants;

public class PDFAFieldType extends AbstractStructuredType {

    public static final String ELEMENT_NS = "http://www.aiim.org/pdfa/ns/field#";

    public static final String PREFERED_PREFIX = "pdfaField";

	@PropertyType(propertyType = "Text")
	public static final String NAME = "name";

	@PropertyType(propertyType = "Choice")
	public static final String VALUETYPE = "valueType";

	@PropertyType(propertyType = "Text")
	public static final String DESCRIPTION = "description";

	public PDFAFieldType(XMPMetadata metadata) {
		super(metadata, XmpConstants.RDF_NAMESPACE, PREFERED_PREFIX);
	}

	@Override
	public String getFieldsNamespace() {
		return ELEMENT_NS;
	}

	public String getName () {
		TextType tt = (TextType) getProperty(NAME);
		return tt == null ? null : tt.getStringValue();
	}

	public String getValueType () {
		TextType tt = (TextType) getProperty(VALUETYPE);
		return tt == null ? null : tt.getStringValue();
	}

	public String getDescription () {
		TextType tt = (TextType) getProperty(DESCRIPTION);
		return tt == null ? null : tt.getStringValue();
	}

	
	
}
