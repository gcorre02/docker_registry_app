package com.dockerapp.pluginapi.http

import com.dockerapp.clientApi.entities.ClientResponse
import spock.lang.Specification

class StaticTestSpec extends Specification {

    def "test"() {
        given:
        def spy = GroovySpy(HttpUtil, global: true)
        ClientResponse expected = new ClientResponse(200, [:], 'hello')
        HttpUtil.sendRequest('1', '2', [:], '3', [:]) >> expected

        when:
        ClientResponse result = HttpUtil.sendRequest('1', '2', [:], '3', [:])

        then:
        println "'''''''''''''''''' ${result.body}"
        result.body == 'hello'

    }
}
