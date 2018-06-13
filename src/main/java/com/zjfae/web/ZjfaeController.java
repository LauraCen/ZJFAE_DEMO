/**
 * 
 */
package com.zjfae.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.zjfae.service.ZjfaeService;

/**
 * @author shenz
 *
 */
@RestController
public class ZjfaeController {
	
	@Autowired
	private ZjfaeService zjfaeService;
	
	@GetMapping("/")
	@ResponseBody
	public String Login() {
		return "Pass";
	}

}

