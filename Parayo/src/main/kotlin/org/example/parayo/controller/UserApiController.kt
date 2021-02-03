package org.example.parayo.controller

import org.example.parayo.common.ApiResponse
import org.example.parayo.domain.auth.SignupRequest
import org.example.parayo.domain.auth.SignupService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
//@RequestMapping("/api/v1", method = [RequestMethod.GET, RequestMethod.POST])
@RequestMapping("/api/v1")
class UserApiController @Autowired constructor(private val signupService: SignupService) {

    @PostMapping("/users")
    fun signup(@RequestBody signupRequest: SignupRequest) {
        ApiResponse.ok(signupService.signup(signupRequest))
    }
}