/*
 * DBeaver - Universal Database Manager
 * Copyright (C) 2010-2023 DBeaver Corp and others
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jkiss.dbeaver.ext.athena.model;

/**
 * Athena constants
 */
public class AthenaConstants
{

    public static final String JDBC_URL_PREFIX = "jdbc:awsathena://";

    public static final String DRIVER_PROP_REGION = "AwsRegion";
    public static final String DRIVER_PROP_AWS_CREDENTIALS_PROVIDER_CLASS = "AwsCredentialsProviderClass";
    public static final String DRIVER_PROP_S3_OUTPUT_LOCATION = "S3OutputLocation";
    public static final String DRIVER_PROP_METADATA_RETRIEVAL_METHOD = "MetadataRetrievalMethod";
    public static final String PROP_SHOW_CATALOGS = "show-aws-catalogs";

}
