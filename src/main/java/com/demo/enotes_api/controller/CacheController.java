package com.demo.enotes_api.controller;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.demo.enotes_api.endpoint.CacheEndpoint;
import com.demo.enotes_api.service.CacheManagerService;
import com.demo.enotes_api.util.CommonUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class CacheController implements CacheEndpoint {
	
	@Autowired
	private CacheManagerService cacheManagerService;
	
	@Override
	public ResponseEntity<?> getAllCache() {
		Collection<String> cache = cacheManagerService.getCache();
		return CommonUtil.createBuildResponse(HttpStatus.OK, cache);
	}

	@Override
	public ResponseEntity<?> getCache(String cache_name) {
		Cache cacheName = cacheManagerService.getCacheName(cache_name);
		return CommonUtil.createBuildResponse(HttpStatus.OK, cacheName);
	}

	@Override
	public ResponseEntity<?> removeAllCache() {
		cacheManagerService.removeAllCache();
		return CommonUtil.createBuildResponseMessage(HttpStatus.OK, "Cache removed successfully");
	}

	
}
