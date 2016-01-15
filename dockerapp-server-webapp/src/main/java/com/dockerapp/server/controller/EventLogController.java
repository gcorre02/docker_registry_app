package com.dockerapp.server.controller;

import com.dockerapp.dao.entities.EventLogEntry;
import com.dockerapp.server.api.EventSearchRequest;
import com.dockerapp.server.api.security.Permission;
import com.dockerapp.server.api.security.PermissionsAllowed;
import com.dockerapp.server.api.service.event.EventLogService;
import com.dockerapp.server.dto.EventLogDto;
import com.dockerapp.server.dto.PageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping(value = "admin/events/*")
@Controller
public class EventLogController extends ErrorHandlingController {
    @Autowired
    private EventLogService eventLogService;

    @PermissionsAllowed(Permission.EVENT_LOG_SEARCH)
    @RequestMapping(value = "/search", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public PageDto<EventLogDto> search(@RequestBody EventSearchRequest request) {
        Page<EventLogEntry> results = eventLogService.search(request);
        return new PageDto<>(
                results.getNumber(),
                results.getSize(),
                results.getTotalPages(),
                results.getTotalElements(),
                EventLogDto.build(results.getContent()));
    }
}