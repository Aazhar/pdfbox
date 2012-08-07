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

package org.apache.padaf.xmpbox.schema;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.namespace.QName;

import org.apache.padaf.xmpbox.XMPMetadata;
import org.apache.padaf.xmpbox.parser.XMPDocumentBuilder;
import org.apache.padaf.xmpbox.parser.XMPSchemaFactory;
import org.apache.padaf.xmpbox.parser.XmpSchemaException;
import org.apache.padaf.xmpbox.type.TypeMapping;


/**
 * Retrieve information about schemas
 * 
 * @author a183132
 * 
 */
public class NSMapping {

	private XMPDocumentBuilder builder = null;
	
	private Map<String, String> complexBasicTypesDeclarationEntireXMPLevel;
	
	private Map<String, String> complexBasicTypesDeclarationSchemaLevel;
	
	private Map<String, String> complexBasicTypesDeclarationPropertyLevel;

	/**
	 * Constructor of the NameSpace mapping
	 * 
	 * @throws XmpSchemaException
	 *             When could not read a property data in a Schema Class given
	 */
	public NSMapping(XMPDocumentBuilder builder) throws XmpSchemaException {
		this.builder = builder;
		complexBasicTypesDeclarationEntireXMPLevel = new HashMap<String, String>();
		complexBasicTypesDeclarationSchemaLevel = new HashMap<String, String>();
		complexBasicTypesDeclarationPropertyLevel = new HashMap<String, String>();
//		definedNamespaces = new HashMap<String, XMPSchemaFactory>();
	}


	/**
	 * Import an NSMapping content.
	 * @param imp
	 */
	public void importNSMapping(NSMapping imp) throws XmpSchemaException {
		// merge name sapce maps
		// merge complex basic types declaration entire xmp level
		for (Entry<String, String> entry : imp.complexBasicTypesDeclarationEntireXMPLevel.entrySet()) {
			if(!complexBasicTypesDeclarationEntireXMPLevel.containsKey(entry.getKey())) {
				complexBasicTypesDeclarationEntireXMPLevel.put(entry.getKey(), entry.getValue());
			}
		}
		// merge complex basic types declaration schema level
		for (Entry<String, String> entry : imp.complexBasicTypesDeclarationSchemaLevel.entrySet()) {
			if(!complexBasicTypesDeclarationSchemaLevel.containsKey(entry.getKey())) {
				complexBasicTypesDeclarationSchemaLevel.put(entry.getKey(), entry.getValue());
			}
		}
		// merger complex basic types declaration property level
		for (Entry<String, String> entry : imp.complexBasicTypesDeclarationPropertyLevel.entrySet()) {
			if(!complexBasicTypesDeclarationPropertyLevel.containsKey(entry.getKey())) {
				complexBasicTypesDeclarationPropertyLevel.put(entry.getKey(), entry.getValue());
			}
		}
	}



	/**
	 * Say if a specific namespace is known
	 * 
	 * @param namespace
	 *            The namespace URI checked
	 * @return True if namespace URI is known
	 */
	public boolean isContainedNamespace(String namespace) {
		boolean found = builder.getSchemaMapping().isContainedNamespace(namespace);
		if (!found) {
//			found = definedNamespaces.containsKey(namespace);
			TypeMapping tm = builder.getTypeMapping();
			found = tm.isStructuredTypeNamespace(namespace);
		}
		return found;
	}

	/**
	 * Give type of specified property in specified schema (given by its
	 * namespaceURI)
	 * 
	 * @param namespace
	 *            The namespaceURI to explore
	 * @param prop
	 *            the property Qualified Name
	 * @return Property type declared for namespace specified, null if unknown
	 */
	public String getSpecifiedPropertyType(String namespace, QName prop) {
		XMPSchemaFactory factory = builder.getSchemaMapping().getSchemaFactory(namespace);
		if (factory!=null) {
			return factory.getPropertyType(prop.getLocalPart());
		} else {
			// check if its a complexbasicValueType and if it's has been declared
			return getComplexBasicValueTypeEffectiveType(prop.getPrefix());
		}
	}

	/**
	 * Check if a namespace declaration for a complex basic type has been found
	 * and if its valid for the entire XMP stream
	 * 
	 * @param namespace
	 *            the namespace URI
	 * @param prefix
	 *            the prefix associated to this namespace
	 */
	public void setComplexBasicTypesDeclarationForLevelXMP(String namespace,
			String prefix) {
		if (builder.getTypeMapping().isStructuredTypeNamespace(namespace)) {
			complexBasicTypesDeclarationEntireXMPLevel.put(prefix, namespace);
		}
	}

	/**
	 * Check if a namespace declaration for a complex basic type has been found
	 * and if its valid for the current schema description (at level of
	 * rdf:Description)
	 * 
	 * @param namespace
	 *            the namespace URI
	 * @param prefix
	 *            the prefix associated to this namespace
	 */
	public void setComplexBasicTypesDeclarationForLevelSchema(String namespace,
			String prefix) {
		if (builder.getTypeMapping().isStructuredTypeNamespace(namespace)) {
			complexBasicTypesDeclarationSchemaLevel.put(prefix, namespace);
		}

	}

	/**
	 * Check if a namespace declaration for a complex basic type has been found
	 * and if its valid for the current property description
	 * 
	 * @param namespace
	 *            the namespace URI
	 * @param prefix
	 *            the prefix associated to this namespace
	 */
	public void setComplexBasicTypesDeclarationForLevelProperty(
			String namespace, String prefix) {
		if (builder.getTypeMapping().isStructuredTypeNamespace(namespace)) {
			complexBasicTypesDeclarationPropertyLevel.put(prefix, namespace);
		}
	}


	/**
	 * Reset complex Basic types declaration for property level
	 */
	public void resetComplexBasicTypesDeclarationInPropertyLevel() {
		complexBasicTypesDeclarationPropertyLevel.clear();
	}

	/**
	 * Reset complex Basic types declaration for schema level
	 */
	public void resetComplexBasicTypesDeclarationInSchemaLevel() {
		complexBasicTypesDeclarationSchemaLevel.clear();
	}

	/**
	 * Reset complex Basic types declaration for Entire XMP level
	 */
	public void resetComplexBasicTypesDeclarationInEntireXMPLevel() {
		complexBasicTypesDeclarationEntireXMPLevel.clear();
	}

	/**
	 * Check for all XMP level if a complexBasicValueType prefix has been
	 * declared
	 * 
	 * @param prefix
	 *            The prefix which may design the namespace URI of the complex
	 *            basic type
	 * @return The type if it is known, else null.
	 */
	public String getComplexBasicValueTypeEffectiveType(String prefix) {
		// stop when found in first map
		String tmp = complexBasicTypesDeclarationPropertyLevel.get(prefix);
		if (tmp==null) {
			tmp = complexBasicTypesDeclarationSchemaLevel.get(prefix);
		}
		if (tmp ==null) {
			tmp = complexBasicTypesDeclarationEntireXMPLevel.get(prefix);
		}
		// return complex basic type
		if (tmp!=null) {
			return builder.getTypeMapping().getStructuredTypeName(tmp).getType();
		} else {
			// 
			return null;
		}
	}

	/**
	 * Return the specialized schema class representation if it's known (create
	 * and add it to metadata). In other cases, return null
	 * 
	 * @param metadata
	 *            Metadata to link the new schema
	 * @param namespace
	 *            The namespace URI
	 * @return Schema representation
	 * @throws XmpSchemaException
	 *             When Instancing specified Object Schema failed
	 */
	public XMPSchema getAssociatedSchemaObject(XMPMetadata metadata, String namespace, String prefix) throws XmpSchemaException {
		XMPSchema found = builder.getSchemaMapping().getAssociatedSchemaObject(metadata, namespace, prefix);
		if (found!=null) {
			return found;
		} else {
			// look in local
			XMPSchemaFactory factory = builder.getSchemaMapping().getSchemaFactory(namespace);
			if (factory==null) {
				return null;
			}
			return factory.createXMPSchema(metadata, prefix);
		}
	}

	
}
