/*
* Copyright 2015 herd contributors
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
package org.finra.dm.tools.downloader;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;

import javax.xml.bind.JAXBException;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import org.finra.dm.model.api.xml.BusinessObjectData;
import org.finra.dm.model.api.xml.S3KeyPrefixInformation;
import org.finra.dm.model.dto.DataBridgeBaseManifestDto;
import org.finra.dm.model.dto.DownloaderInputManifestDto;
import org.finra.dm.tools.common.databridge.DataBridgeWebClient;

/**
 * This class encapsulates web client functionality required to communicate with the Data Management Registration Service.
 */
@Component
public class DownloaderWebClient extends DataBridgeWebClient
{
    private static final Logger LOGGER = Logger.getLogger(DownloaderWebClient.class);

    /**
     * Retrieves S3 key prefix from the Data Management Service.
     *
     * @param businessObjectData the business object data
     *
     * @return the S3 key prefix
     * @throws JAXBException if a JAXB error was encountered.
     * @throws IOException if an I/O error was encountered.
     * @throws URISyntaxException if a URI syntax error was encountered.
     */
    public S3KeyPrefixInformation getS3KeyPrefix(BusinessObjectData businessObjectData) throws IOException, JAXBException, URISyntaxException
    {
        DataBridgeBaseManifestDto dataBridgeBaseManifestDto = new DataBridgeBaseManifestDto();
        dataBridgeBaseManifestDto.setNamespace(businessObjectData.getNamespace());
        dataBridgeBaseManifestDto.setBusinessObjectDefinitionName(businessObjectData.getBusinessObjectDefinitionName());
        dataBridgeBaseManifestDto.setBusinessObjectFormatUsage(businessObjectData.getBusinessObjectFormatUsage());
        dataBridgeBaseManifestDto.setBusinessObjectFormatFileType(businessObjectData.getBusinessObjectFormatFileType());
        dataBridgeBaseManifestDto.setBusinessObjectFormatVersion(String.valueOf(businessObjectData.getBusinessObjectFormatVersion()));
        dataBridgeBaseManifestDto.setPartitionKey(businessObjectData.getPartitionKey());
        dataBridgeBaseManifestDto.setPartitionValue(businessObjectData.getPartitionValue());
        dataBridgeBaseManifestDto.setSubPartitionValues(businessObjectData.getSubPartitionValues());
        return super.getS3KeyPrefix(dataBridgeBaseManifestDto, businessObjectData.getVersion(), Boolean.FALSE);
    }

    /**
     * Retrieves business object data from the Data Management Service.
     *
     * @param manifest the downloader input manifest file information
     *
     * @return the business object data information
     * @throws JAXBException if a JAXB error was encountered.
     * @throws IOException if an I/O error was encountered.
     * @throws URISyntaxException if a URI syntax error was encountered.
     */
    public BusinessObjectData getBusinessObjectData(DownloaderInputManifestDto manifest) throws IOException, JAXBException, URISyntaxException
    {
        LOGGER.info("Retrieving business object data information from the Data Management Service ...");

        StringBuilder uriPathBuilder = new StringBuilder(DM_APP_REST_URI_PREFIX);
        uriPathBuilder.append("/businessObjectData");
        if (manifest.getNamespace() != null)
        {
            uriPathBuilder.append("/namespaces/").append(manifest.getNamespace());
        }
        uriPathBuilder.append("/businessObjectDefinitionNames/").append(manifest.getBusinessObjectDefinitionName());
        uriPathBuilder.append("/businessObjectFormatUsages/").append(manifest.getBusinessObjectFormatUsage());
        uriPathBuilder.append("/businessObjectFormatFileTypes/").append(manifest.getBusinessObjectFormatFileType());

        URIBuilder uriBuilder = new URIBuilder().setScheme(getUriScheme()).setHost(dmRegServerAccessParamsDto.getDmRegServerHost())
            .setPort(dmRegServerAccessParamsDto.getDmRegServerPort()).setPath(uriPathBuilder.toString())
            .setParameter("partitionKey", manifest.getPartitionKey()).setParameter("partitionValue", manifest.getPartitionValue())
            .setParameter("businessObjectFormatVersion", manifest.getBusinessObjectFormatVersion())
            .setParameter("businessObjectDataVersion", manifest.getBusinessObjectDataVersion());

        if (manifest.getSubPartitionValues() != null)
        {
            uriBuilder.setParameter("subPartitionValues", dmStringHelper.join(manifest.getSubPartitionValues(), "|", "\\"));
        }

        URI uri = uriBuilder.build();

        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(uri);
        request.addHeader("Accepts", "application/xml");

        // If SSL is enabled, set the client authentication header.
        if (dmRegServerAccessParamsDto.getUseSsl())
        {
            request.addHeader(getAuthorizationHeader());
        }

        LOGGER.info(String.format("    HTTP GET URI: %s", request.getURI().toString()));
        LOGGER.info(String.format("    HTTP GET Headers: %s", Arrays.toString(request.getAllHeaders())));

        BusinessObjectData businessObjectData =
            getBusinessObjectData(httpClientOperations.execute(client, request), "retrieve business object data from the Data Management Service");

        LOGGER.info("Successfully retrieved business object data from the Data Management Service.");

        return businessObjectData;
    }
}
