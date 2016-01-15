package com.dockerapp.server.controller.docker;

import com.dockerapp.clientApi.entities.ImagesObj;
import com.dockerapp.server.api.docker.DockerResponse;
import com.dockerapp.server.api.security.Permission;
import com.dockerapp.server.api.security.PermissionsAllowed;
import com.dockerapp.server.api.service.docker.DockerService;
import com.dockerapp.server.controller.ErrorHandlingController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

@RequestMapping(value = "docker/")
@Controller
public class DockerController extends ErrorHandlingController {

    private static final String REGISTRY_ENDPOINT = "registry.<LOCATION OF THE DOCKER REGISTRY>.com:2083";
    @Autowired
    private DockerService dockerService;

    //TODO:[GR][14/8/15] need to add authorization validations to all endpoints.

    @PermissionsAllowed(Permission.TEST)
    @RequestMapping(value = "registry", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public DockerResponse getRegistry() throws IOException {
        return dockerService.getRegistryDetails();
    }

    @PermissionsAllowed(Permission.TEST)
    @RequestMapping(value = "registry/page", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String getRegistryPage() throws IOException {
        DockerResponse response = dockerService.getRegistryDetails();
        StringBuilder htmlBuilder = new StringBuilder();
        htmlBuilder.append(String.format("<p>Check Registry is live, Should show '{}' when you press:  <a href=\"https://%s/v2\">Ping Registry</a></p><p></p>", REGISTRY_ENDPOINT));
        htmlBuilder.append("<table>");
        htmlBuilder.append(String.format("<tr><td>%s</td><td>%s</td><td>%s</td><td>%s</td></tr>", "IMAGE", "TAG", "URL", "FULL COMMAND"));
        for (Map.Entry<String, ImagesObj> entry : response.getRepositories().entrySet()) {
            Iterator<String> tags = entry.getValue().getTags().iterator();
            String tag = tags.next();
            htmlBuilder.append(String.format("<tr><td>%s</td><td>%s</td>><td>%s</td><td>%s</td></tr>", entry.getKey(), tag, String.format("%s/%s:%s", REGISTRY_ENDPOINT, entry.getKey(), tag), ""));
            while (tags.hasNext()) {
                tag = tags.next();
                htmlBuilder.append(String.format("<tr><td>%s</td><td>%s</td>><td>%s</td><td>%s</td></tr>", "", tag, String.format("%s/%s:%s", REGISTRY_ENDPOINT, entry.getKey(), tag), ""));
            }
        }
        htmlBuilder.append("</table>");
        return htmlBuilder.toString();
    }
/*
    @PermissionsAllowed(Permission.TEST)
    @RequestMapping(value = "{image}/{tag}/link", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public something getImageLink(@PathVariable(value = "image") String image, @PathVariable(value = "tag") String tag) {
        return dockerService.getImageUri(image);
    }
    @PermissionsAllowed(Permission.TEST)
    @RequestMapping(value = "run", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public digest getImageLink(@RequestParam String link) {
        //needs DB!
        return dockerService.startDocker(link);
    }
    */
}