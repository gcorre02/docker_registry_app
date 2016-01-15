package com.dockerapp.pluginapi.http;

import com.dockerapp.dao.error.DockerAppError;
import com.dockerapp.clientApi.AuthenticationEncoderFactory;
import com.dockerapp.clientApi.DockerRegistryClient;
import com.dockerapp.clientApi.ClientConnectionException;
import com.dockerapp.clientApi.entities.DockerRegistryRepositories;
import com.dockerapp.clientApi.entities.ImagesObj;
import com.dockerapp.clientApi.entities.ClientResponse;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class DockerRegistryHttpClient implements DockerRegistryClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(DockerRegistryHttpClient.class);
    private static final String DOCKER_REGISTRY = "https://registry.<WHERE THE REGISTRY IS LOCATED>.com:2083/v2";
    private final AuthenticationEncoderFactory authenticationFactory = new AuthenticationEncoderFactory();
    private final UriTokenMapper uriTokenMapper = new UriTokenMapper();

    @Override
    public DockerRegistryRepositories getDockerRepos() throws IOException {
        String body = getWoutConnection("_catalog", null).getBody();
        ObjectMapper om = new ObjectMapper();
        System.out.println(">>>>>>>>>>>> " + body);
        Map<String, List<String>> repos = om.readValue(body, Map.class);
        return new DockerRegistryRepositories(repos);
    }

    @Override
    public Map<String, ImagesObj> getRepoImages(DockerRegistryRepositories repositories) throws IOException {
        Map<String, ImagesObj> returnable = new HashMap<>();
        String body;
        for (String repo : repositories.getRepositories()) {
            body = getWoutConnection(String.format("%s/tags/list", repo), null).getBody();
            System.out.println(">>>>>>>>>>>> " + body);
            ObjectMapper om = new ObjectMapper();

            Map<String, Object> imageMaps = om.readValue(body, Map.class);

            ImagesObj images = new ImagesObj(imageMaps);
            returnable.put(repo, images);
        }
        return returnable;
    }

    private ClientResponse getWoutConnection(String endpoint, Map<String, String> parameters) throws ClientConnectionException {
        String uri = uriTokenMapper.map(endpoint, parameters);
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>> " + uri);
        return executeWoutConnection(HttpUtil.HTTP_METHOD_GET, uri, null);
    }

    private ClientResponse executeWoutConnection(String method, String uri, Map<String, String> parameters) throws ClientConnectionException {
        try {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("executing request, baseUrl=[{}], uri=[{}], method=[{}]", DOCKER_REGISTRY, uri, method);
            }

            return HttpUtil.sendRequest(
                    MessageFormat.format("{0}/{1}", DOCKER_REGISTRY, uri),
                    method,
                    parameters,
                    null,
                    null);

        } catch (IOException e) {
            LOGGER.error(String.format(
                    "error executing plugin request, url=%s, error=%s",
                    MessageFormat.format("{0}/{1}", DOCKER_REGISTRY, uri), e.getMessage()));
            throw new ClientConnectionException(DockerAppError.REMOTE_CLIENT_COMMUNICATION_ERROR, e.getMessage(), e);
        }
    }
}
